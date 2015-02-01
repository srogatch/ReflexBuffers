package ai.reflexBuffers.core.tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ai.reflexBuffers.core.Token;

// Algorithm ready to run: for that algorithms that take input must be first provisioned
//   with the values of input parameters
public class ProvisionedAlgorithm extends Algorithm {
	private Map<String, ArrayList<Token>> _inputValues
		= new HashMap<String, ArrayList<Token>>();
	
	public ProvisionedAlgorithm(Algorithm prototype) {
		super(prototype);
	}
	public void acceptInput(String bufferName, ArrayList<Token> tokens) {
		ArrayList<Token> inMap = _inputValues.get(bufferName);
		if( inMap == null ) {
			inMap = new ArrayList<Token>();
			_inputValues.put(bufferName, inMap);
		}
		inMap.addAll(tokens);
	}
}
