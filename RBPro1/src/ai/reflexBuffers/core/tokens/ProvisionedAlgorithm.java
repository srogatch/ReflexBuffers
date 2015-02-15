package ai.reflexBuffers.core.tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ai.reflexBuffers.core.Token;
import ai.reflexBuffers.core.alg.Provision;

// Algorithm ready to run: for that algorithms that take input must be first provisioned
//   with the values of input parameters
public class ProvisionedAlgorithm extends Algorithm {
	private Provision _provision = new Provision();
	
	public ProvisionedAlgorithm(Algorithm prototype) {
		super(prototype);
	}
	public void acceptInput(String bufferName, ArrayList<Token> tokens) {
		_provision.acceptValues(bufferName, tokens);
	}
	
}
