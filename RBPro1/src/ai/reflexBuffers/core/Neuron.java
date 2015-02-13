package ai.reflexBuffers.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import ai.reflexBuffers.core.line.Conveyor;
import ai.reflexBuffers.core.stimuli.ReflexCreation;
import ai.reflexBuffers.core.stimuli.ReflexDeletion;
import ai.reflexBuffers.core.stimuli.ResponseAlgorithmChange;
import ai.reflexBuffers.core.stimuli.ResponseDeletion;
import ai.reflexBuffers.core.stimuli.RewiringComponent;
import ai.reflexBuffers.core.tokens.ProvisionedAlgorithm;
import ai.reflexBuffers.utils.stability.CoreLog;

public class Neuron {
	// A pipe to reflex holding the reflex even if it was deleted concurrently
	protected static class ReflexDirectionPipe {
		private Reflex _reflex;
		private Map<String, List<Token>> _direction;
		public ReflexDirectionPipe(Reflex reflex, Map<String, List<Token>> direction) {
			_reflex = reflex;
			_direction = direction;
		}
		public Reflex getReflex() {
			return _reflex;
		}
		public Map<String, List<Token>> getDirection() {
			return _direction;
		}
	};
	
	private Map<String, Reflex> _reflexes = new HashMap<String, Reflex>();
	// Neuron is passed as a parameter to Conveyor constructor, so we have to create
	//   Neuron before we create Conveyor, which is also logical because by the time of
	//   launch of Conveyour, core structures (Neuron, Reflexes, etc) must be operational
	private Conveyor _conveyor = null;
	private ReentrantLock _lock = new ReentrantLock(true);
	
	public void setConveyor(Conveyor conveyor) {
		_conveyor = conveyor;
	}
	
	public Reflex getReflex(String name) {
		try {
			_lock.lock();
			Reflex ans = _reflexes.get(name);
			return ans;
		}
		finally {
			_lock.unlock();
		}
	}
	
	public Reflex createReflex(ReflexCreation rewComp) {
		String reflexName = rewComp.getReflexName();
		try {
			_lock.lock();
			Reflex reflex = _reflexes.get(reflexName);
			// Don't delete the reflex if it exists: that must have been specified
			//   with another rewiring component
			if( reflex == null ) {
				reflex = new Reflex(this, reflexName);
				_reflexes.put(reflexName, reflex);
			}
			return reflex;
		}
		finally {
			_lock.unlock();
		}
	}
	
	public void deleteReflex(ReflexDeletion rewComp) {
		String reflexName = rewComp.getReflexName();
		try {
			_lock.lock();
			_reflexes.remove(reflexName);
		}
		finally {
			_lock.unlock();
		}
	}
	
	public void deleteResponse(ResponseDeletion rewComp) {
		String reflexName = rewComp.getReflexName();
		Reflex reflex = getReflex(reflexName);
		if( reflex == null ) {
			CoreLog._.rewiringOfAbsentReflex(reflexName, rewComp);
			return;
		}
		try {
			reflex.getLock().lock();
			reflex.removeResponse(rewComp.getResponseName());
		}
		finally {
			reflex.getLock().unlock();
		}
	}
	
	public void changeResponseAlgorithm(ResponseAlgorithmChange rac) {
		String reflexName = rac.getReflexName();
		Reflex reflex = getReflex(reflexName);
		if( reflex == null ) {
			CoreLog._.rewiringOfAbsentReflex(reflexName, rac);
			return;
		}
		try {
			reflex.getLock().lock();
			reflex.setupResponse(rac.getResponseName(), rac.getAlgorithm());
		}
		finally {
			reflex.getLock().unlock();
		}
	}
	
	public void rewireReflex(String reflexName, ArrayList<RewiringComponent> rcs) {
		int pos = 0;
		while( pos<rcs.size() ) {
			RewiringComponent rewComp = null;
			// Skip rewirings for non-existing reflexes
			for(; pos<rcs.size(); pos++) {
				rewComp = rcs.get(pos);
				Reflex reflex = getReflex(reflexName);
				if( (reflex != null) || (rewComp instanceof ReflexCreation) ) {
					break;
				}
				CoreLog._.rewiringOfAbsentReflex(reflexName, rewComp);
			}
			if( pos >= rcs.size() ) {
				return;
			}
			for(; pos<rcs.size(); pos++) {
				rewComp = rcs.get(pos);
				if( rewComp instanceof ReflexDeletion ) {
					break;
				}
				// ReflexCreation must not delete existing reflex
				rewComp.implement(this);
			}
			if( pos >= rcs.size() ) {
				return;
			}
			assert(rewComp instanceof ReflexDeletion);
			assert(reflexName == rewComp.getReflexName());
			rewComp.implement(this);
			pos++;
		}
	}
	
	public void propagateStimulus(Stimulus stimulus) throws InterruptedException {
		//Handle reflex creation&deletion requests here first, then response
		//  creation&deletion, then altering of activation condition of a response
		//There can be a better order of implementing components of rewiring, e.g.
		//  skip doing anything on a reflex that is going to be deleted, and sorting
		//  the components so to obtain a lock once per Reflex, etc.
		Map<String, ArrayList<RewiringComponent>> rrcs
			= new HashMap<String, ArrayList<RewiringComponent>>();
		{
			ArrayList<RewiringComponent> rcs = stimulus.getRewiring();
			for(int i=0; i<rcs.size(); i++) {
				RewiringComponent rc = rcs.get(i);
				String reflexName = rc.getReflexName();
				ArrayList<RewiringComponent> entry = rrcs.get(reflexName);
				if( entry == null ) {
					entry = new ArrayList<RewiringComponent>();
					rrcs.put(reflexName, entry);
				}
				entry.add(rc);
			}
		}
		for(Map.Entry<String, ArrayList<RewiringComponent>> atReflex : 
			rrcs.entrySet()) 
		{
			rewireReflex(atReflex.getKey(), atReflex.getValue());
		}
		
		// Propagate tokens to reflex buffers
		ArrayList<ReflexDirectionPipe> rdps = new ArrayList<ReflexDirectionPipe>();
		try {
			_lock.lock();
			for(Map.Entry<String, Map<String, List<Token>>> atReflex
				: stimulus.getDirections().entrySet()) 
			{
				String reflexName = atReflex.getKey();
				Reflex reflex = _reflexes.get(reflexName);
				Map<String, List<Token>> direction = atReflex.getValue();
				if( reflex == null ) {
					CoreLog._.tokensToAbsentReflex(reflexName, direction);
					continue;
				}
				rdps.add(new ReflexDirectionPipe(reflex, direction));
			}
		} finally {
			_lock.unlock();
		}
		for(int i=rdps.size()-1; i>=0; i--) {
			Reflex reflex = rdps.get(i).getReflex();
			// The 2-argument version is used to enforce fairness
			if( !reflex.getLock().tryLock(0, TimeUnit.NANOSECONDS) ) {
				continue;
			}
			try {
				// Push tokens to buffers and regard activations
				reflex.acceptDirection(rdps.get(i).getDirection());
				if( i <= rdps.size()-2 ) {
					rdps.set(i, rdps.get(rdps.size()-1));
				}
				rdps.remove(rdps.size()-1);
			} finally {
				reflex.getLock().unlock();
			}
		}
		// Process the reflexes which were locked by another thread in the first pass
		for(ReflexDirectionPipe rp : rdps) 
		{
			Reflex reflex = rp.getReflex();
			try {
				reflex.getLock().lock();
				reflex.acceptDirection(rp.getDirection());
			} finally {
				reflex.getLock().unlock();
			}
		}
	}
	
	public void propagateResponse(ProvisionedAlgorithm provAlg) {
		_conveyor.orderExecution(provAlg);
	}
	
}
