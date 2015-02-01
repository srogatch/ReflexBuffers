package ai.reflexBuffers.core.tokens;

import ai.reflexBuffers.core.ActivationCondition;
import ai.reflexBuffers.core.Token;

public class Algorithm extends Token {
	private ActivationCondition _activationCondition = new ActivationCondition();
	
	public Algorithm(Algorithm prototype) {
		//TODO: perform a deep copy of the prototype algorithm
	}
	
	public ActivationCondition getActivationCondition() {
		return _activationCondition;
	}
}
