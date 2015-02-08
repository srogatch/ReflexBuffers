package ai.reflexBuffers.core.stimuli;

import ai.reflexBuffers.core.Neuron;
import ai.reflexBuffers.core.tokens.Algorithm;

// This class is intended to be used for both change of algorithm in an existing
//   Response, and for creation of a new Response with the given name and algorithm
public class ResponseAlgorithmChange extends RewiringComponent {
	private String _reflexName;
	private String _responseName;
	private Algorithm _newAlgorithm;
	
	public ResponseAlgorithmChange(String reflexName, String responseName, 
		Algorithm newAlgorithm)
	{
		_reflexName = reflexName;
		_responseName = responseName;
		_newAlgorithm = newAlgorithm; 
	}
	
	@Override
	public void implement(Neuron neuron) {
		// TODO implement the change of Response algorithm
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
}
