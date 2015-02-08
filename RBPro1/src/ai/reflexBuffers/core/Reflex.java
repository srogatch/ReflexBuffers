package ai.reflexBuffers.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import ai.reflexBuffers.core.tokens.Algorithm;
import ai.reflexBuffers.core.tokens.ProvisionedAlgorithm;
import ai.reflexBuffers.utils.diversity.Randomizer;

// Here is emulation of stimulus-response architecture on top of von-Neumann architecture
// https://en.wikipedia.org/wiki/Classical_conditioning
public class Reflex {
	private Map<String, Buffer> _buffers = new HashMap<String, Buffer>();
	private Map<Activator, Set<Set<Activator>>> _activatorTrackingSets 
		= new HashMap<Activator, Set<Set<Activator>>>();
	private Map<String, Response> _responses = new HashMap<String, Response>();
	private String _name = null;
	private Neuron _host = null;
	private ReentrantLock _lock = new ReentrantLock(true);
	
	public Reflex(Neuron host, String name) {
		_host = host;
		_name = name;
		// value = take(bufferName)
		// give(reflexName, bufferName, value)
		// take-process-give
	}
	public ReentrantLock getLock() {
		return _lock;
	}
	public Set<Map.Entry<String, Buffer>> getAllBuffers() {
		return _buffers.entrySet();
	}
	public Buffer getBuffer(String name, boolean createIfNotExists) {
		Buffer ans = _buffers.get(name);
		if( ans == null && createIfNotExists ) {
			ans = new Buffer(this, name);
			_buffers.put(name, ans);
		}
		return ans;
	}
	public int getBufferTokenCount(String bufferName) {
		Buffer buffer = _buffers.get(bufferName);
		if( buffer == null ) {
			return 0;
		}
		return buffer.getTokenCount();
	}
	public void addActivatorTrackingSet(Activator a, Set<Activator> s) {
		Set<Set<Activator>> ssa = _activatorTrackingSets.get(a);
		// If this fails, then likely the activator was not properly registered with the reflex
		assert( ssa != null );
		boolean contained = !(ssa.add(s));
		// If this fails, then likely the tracking set was not properly removed 
		//   from the set of tracking sets
		assert( !contained );
	}
	public void removeActivatorTrackingSet(Activator a, Set<Activator> s) {
		Set<Set<Activator>> ssa = _activatorTrackingSets.get(a);
		// If this fails, then likely the activator was not properly registered with the reflex
		assert( ssa != null );
		// If this fails, then likely the tracking set was not properly added
		boolean contained = ssa.remove(s);
		assert( contained );
	}
	public void removeActivator(Activator a) {
		Set<Set<Activator>> ssa = _activatorTrackingSets.get(a);
		assert(ssa != null);
		for(Set<Activator> sa : ssa) {
			boolean contained = sa.remove(a);
			assert(contained);
		}
	}
	public void addResponse(Response response) {
		String repsonseName = response.getName();
		assert( !_responses.containsKey(repsonseName) );
		_responses.put(repsonseName, response);
		@SuppressWarnings("unused") // for debugging
		Activator activator = new Activator(this, response);
	}
	public ProvisionedAlgorithm provisionAlgorithm(Algorithm algorithm) {
		ProvisionedAlgorithm ans = new ProvisionedAlgorithm(algorithm);
		for(Map.Entry<String, Integer> requirement : 
			algorithm.getActivationCondition().getAllRequirements())
		{
			String bufferName = requirement.getKey();
			int nTokens = requirement.getValue();
			assert( nTokens >= 1 );
			// Buffer must exist, otherwise there is something wrong in the code which
			//   has executed earlier (emulator code, not user code)
			ans.acceptInput(bufferName, 
				getBuffer(bufferName, false).provideTokens(nTokens));
		}
		return ans;
	}
	public void activateResponse(Activator activator) {
		ProvisionedAlgorithm provAlg = provisionAlgorithm(activator.getResponse().getAlgorithm());
		_host.propagateResponse(provAlg);
	}
	public void acceptDirection(Map<String, List<Token>> direction) {
		assert( _lock.isHeldByCurrentThread() );
		Set<Activator> allFiring = new HashSet<Activator>();
		for(Map.Entry<String, List<Token>> atBuffer : direction.entrySet()) {
			Buffer buffer = getBuffer(atBuffer.getKey(), true);
			ArrayList<Activator> curFiring = buffer.acceptTokens(atBuffer.getValue());
			if( curFiring != null ) {
				assert( curFiring.size() >= 1);
				allFiring.addAll(curFiring);
			}
		}
		if( allFiring.size() == 0 ) {
			return;
		}
		if( allFiring.size() == 1 ) {
			activateResponse(allFiring.iterator().next());
			return;
		}
		//If there are more than 1 activators firing, then permutate the 
		//  array randomly and then greedily try to activate as many responses 
		//  as possible
		ArrayList<Activator> ordered = Randomizer._.makePermutatedArray(allFiring);
		for(Activator activator : ordered) {
			// after the previous were activated, this one may be not firing anymore
			if( activator.canActivate() ) {
				activateResponse(activator);
			}
		}
	}
}
