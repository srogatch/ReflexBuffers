package ai.reflexBuffers.core.stimuli;

import ai.reflexBuffers.core.Neuron;
import ai.reflexBuffers.core.tokens.Algorithm;

// This class is intended to be used for both change of algorithm in an existing
//   Response, and for creation of a new Response with the given name and algorithm
public class SetupResponse extends RewiringComponent {
	private String _reflexName;
	private String _responseName;
	private Algorithm _newAlgorithm;
	
	public SetupResponse(String reflexName, String responseName, 
		Algorithm newAlgorithm)
	{
		_reflexName = reflexName;
		_responseName = responseName;
		_newAlgorithm = newAlgorithm; 
	}
	
	@Override
	public void implement(Neuron neuron) {
		neuron.setupResponse(this);
	}

	@Override
	public String getReflexName() {
		return _reflexName;
	}
	@Override
	public String toString() {
		return String.format("Reflex name [%s], response name [%s], new algorithm [%s].", 
			_reflexName, _responseName, _newAlgorithm.toString());
	}
	public String getResponseName() {
		return _responseName;
	}
	public Algorithm getAlgorithm() {
		return _newAlgorithm;
	}
}
