package ai.reflexBuffers.core.alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ai.reflexBuffers.core.Token;
import ai.reflexBuffers.utils.stability.CoreLog;

public class Provision {
	private Map<String, ArrayList<Token>> _values = new HashMap<String, ArrayList<Token>>();

	public void acceptValues(String bufferName, ArrayList<Token> tokens) {
		ArrayList<Token> inMap = _values.get(bufferName);
		if( inMap == null ) {
			inMap = new ArrayList<Token>();
			_values.put(bufferName, inMap);
		}
		inMap.addAll(tokens);
	}
	public ArrayList<Token> give(String bufferName) {
		ArrayList<Token> inMap = _values.get(bufferName);
		if( inMap == null ) {
			CoreLog._.insufficientProvision(bufferName);
			return null;
		}
		ArrayList<Token> copy = new ArrayList<Token>(inMap);
		return copy;
	}
	public boolean expectExhausted() {
		if( _values.size() != 0 ) {
			CoreLog._.unexhaustedProvision(_values);
			return false;
		}
		return true;
	}
}
