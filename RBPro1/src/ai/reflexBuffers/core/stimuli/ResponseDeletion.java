package ai.reflexBuffers.core.stimuli;

import ai.reflexBuffers.core.Neuron;

public class ResponseDeletion extends RewiringComponent {
	private String _reflexName;
	private String _responseName;
	public ResponseDeletion(String reflexName, String responseName) {
		_reflexName = reflexName;
		_responseName = responseName;
	}
	@Override
	public void implement(Neuron neuron) {
		neuron.deleteResponse(this);
	}
	@Override
	public String getReflexName() {
		return _reflexName;
	}
	@Override
	public String toString() {
		return String.format("Reflex name [%s], response name [%s]",  _reflexName,
			_responseName);
	}
	public String getResponseName() {
		return _responseName;
	}
}
