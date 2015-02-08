package ai.reflexBuffers.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.reflexBuffers.core.stimuli.RewiringComponent;

public class Stimulus {
	private Map<String, Map<String, List<Token>>> _directions 
		= new HashMap<String, Map<String, List<Token>>>();
	// Reflex creation&deletion requests, response creation&deletion, and altering of
	//   activation condition and algorithm of a response
	private ArrayList<RewiringComponent> _rewiring = new ArrayList<RewiringComponent>();
	
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
	
	public void addRewiring(RewiringComponent rc) {
		_rewiring.add(rc);
	}
	
	public Map<String, Map<String, List<Token>>> getDirections() {
		return _directions;
	}
	public ArrayList<RewiringComponent> getRewiring() {
		return _rewiring;
	}
}
