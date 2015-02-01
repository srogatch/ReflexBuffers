package ai.reflexBuffers.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stimulus {
	private Map<String, Map<String, List<Token>>> _directions 
		= new HashMap<String, Map<String, List<Token>>>();
	//TODO: stimulus should also be able to contain reflex creation&deletion requests,
	//  response creation&deletion, and altering of activation condition of a response
	
	public void addTokens(String reflexName, String bufferName, List<Token> tokens) {
		Map<String, List<Token>> atReflex = _directions.get(reflexName);
		if( atReflex == null ) {
			atReflex = new HashMap<String, List<Token>>();
			_directions.put(reflexName, atReflex);
		}
		List<Token> atBuffer = atReflex.get(bufferName);
		if( atBuffer == null ) {
			atBuffer = new ArrayList<Token>();
			atReflex.put(bufferName, atBuffer);
		}
		atBuffer.addAll(tokens);
	}
	
	public Map<String, Map<String, List<Token>>> getDirections() {
		return _directions;
	}
}
