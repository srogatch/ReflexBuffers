package ai.reflexBuffers.core.tokens;

import ai.reflexBuffers.core.ActivationCondition;
import ai.reflexBuffers.core.Token;
import ai.reflexBuffers.core.alg.Implementation;

public class Algorithm extends Token {
	private ActivationCondition _activationCondition = new ActivationCondition();
	// do nothing
	private Implementation _implementation = new Implementation();
	
	public Algorithm(Algorithm prototype) {
		//TODO: perform a deep copy of the prototype algorithm
	}
	
	public ActivationCondition getActivationCondition() {
		return _activationCondition;
	}
}
