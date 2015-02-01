package ai.reflexBuffers.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Each object of this class must return its memory address in hashCode()
//   and compare object memory addresses in equals(), because it is used
//   in sets designed this way.
public class Activator {
	private Set<String> _remaining = new HashSet<String>();
	private Response _response = null;
	
	public Activator(Reflex reflex, Response response) {
		_response = response;
		response.setActivator(this);
		resetRequirements(reflex);
	}
	
	private ActivationCondition getActivationCondition() {
		return _response.getActivationCondition();
	}
	
	public Response getResponse() {
		return _response;
	}
	
	// Reset the remaining requirements to the state of the buffers of the reflex
	private void resetRequirements(Reflex from) {
		_remaining.clear();
		Set<Map.Entry<String, Integer>> requiredBuffers = getActivationCondition().getRequiredBuffers();
		for(Map.Entry<String, Integer> e : requiredBuffers) {
			String bufferName = e.getKey();
			int nRequiredTokens = e.getValue();
			assert(nRequiredTokens >= 1);
			Buffer buffer = from.getBuffer(bufferName, true);
			int nAvailableTokens = buffer.getTokenCount();
			boolean waiting = false;
			if( nRequiredTokens > nAvailableTokens ) {
				_remaining.add(bufferName);
				waiting = true;
			}
			buffer.onActivatorAdded(this, waiting);
		}
	}
	
	public boolean canActivate() {
		return (_remaining.size() == 0);
	}
	
	// Returns "true" in case the activator is still waiting for more tokens in this buffer.
	//   After this function returns "false" for some buffer, it must not be called for the
	//   same buffer until reset or token(s) removed from that buffer
	public boolean onTokenArrived(Buffer buffer) {
		String bufferName = buffer.getName();
		if( !_remaining.contains(bufferName) ) {
			return false;
		}
		int nRequiredTokens = getActivationCondition().getRequirement(bufferName);
		int nAvailableTokens = buffer.getTokenCount();
		if( nRequiredTokens <= nAvailableTokens ) {
			_remaining.remove(bufferName);
			return false;
		}
		return true;
	}
	
	// Returns "true" in case the activator requires more tokens from this buffer than the
	//   amount currently in the buffer. Otherwise returns "false".
	public boolean onTokensConsumed(Buffer buffer) {
		String bufferName = buffer.getName();
		int nRequiredTokens = getActivationCondition().getRequirement(bufferName);
		if( nRequiredTokens == 0 ) {
			assert( !_remaining.contains(bufferName) );
			return false;
		}
		int nAvailableTokens = buffer.getTokenCount();
		if( nRequiredTokens <= nAvailableTokens ) {
			// If this assertion fails, then it is likely that this activator did not receive
			//   the onTokenArrived() call which would let this activator to remove this buffer
			//   from the _remaining set
			assert( !_remaining.contains(bufferName) );
			return false;
		}
		_remaining.add(bufferName);
		return true;
	}
}
