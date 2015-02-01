package ai.reflexBuffers.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ai.reflexBuffers.core.line.Conveyor;
import ai.reflexBuffers.core.tokens.ProvisionedAlgorithm;
import ai.reflexBuffers.utils.diversity.Randomizer;
import ai.reflexBuffers.utils.stability.CoreLog;

public class Neuron {
	private Map<String, Reflex> _reflexes = new HashMap<String, Reflex>();
	// Neuron is passed as a parameter to Conveyor constructor, so we have to create
	//   Neuron before we create Conveyor, which is also logical because by the time of
	//   launch of Conveyour, core structures (Neuron, Reflexes, etc) must be operational
	private Conveyor _conveyor = null;
	
	public void setConveyor(Conveyor conveyor) {
		_conveyor = conveyor;
	}
	
	public void propagateStimulus(Stimulus stimulus) {
		//TODO: handle reflex creation&deletion requests here first, then response
		//  creation&deletion, then altering of activation condition of a response
		for(Map.Entry<String, Map<String, List<Token>>> atReflex 
			: stimulus.getDirections().entrySet()) 
		{
			Reflex reflex = _reflexes.get(atReflex.getKey());
			if( reflex == null ) {
				// no such reflex (?anymore)
				CoreLog._.tokensToAbsentReflex(atReflex.getKey(), atReflex.getValue());
				continue;
			}
			Set<Activator> allFiring = new HashSet<Activator>();
			Set<Map.Entry<String, List<Token>>> atBufferSet = atReflex.getValue().entrySet();
			//TODO: performance can be improved if threads work with reflexes in 2 passes
			//  In the 1st pass - only propagate to reflexes which are not blocked by other threads
			//  In the 2nd pass - propagate to the remaining reflexes, competing with other threads
			//  So the implementation should be changed to ReentrantLock.tryLock()
			synchronized(reflex) {
				for(Map.Entry<String, List<Token>> atBuffer : atBufferSet) {
					Buffer buffer = reflex.getBuffer(atBuffer.getKey(), true);
					ArrayList<Activator> curFiring = buffer.acceptTokens(atBuffer.getValue());
					if( curFiring != null ) {
						assert( curFiring.size() >= 1);
						allFiring.addAll(curFiring);
					}
				}
				//TODO: if there are more than 1 activators firing, then permutate the 
				//  array randomly and then greedily try to activate as many responses 
				//  as possible
				if( allFiring.size() == 0 ) {
					break;
				}
				if( allFiring.size() == 1 ) {
					reflex.activateResponse(allFiring.iterator().next());
					break;
				}
				ArrayList<Activator> ordered = Randomizer._.makePermutatedArray(allFiring);
				for(Activator activator : ordered) {
					// after the previous were activated, this one may be not firing anymore
					if( activator.canActivate() ) {
						reflex.activateResponse(activator);
					}
				}
			} // synchronized(reflex)
		} //endfor reflexes receiving from this stimulus
	}
	
	public void propagateResponse(ProvisionedAlgorithm provAlg) {
		_conveyor.orderExecution(provAlg);
	}
	
}
