package ai.reflexBuffers.core.stimuli;

import ai.reflexBuffers.core.Neuron;

public class ReflexCreation extends RewiringComponent {
	private String _reflexName;
	public ReflexCreation(String reflexName) {
		_reflexName = reflexName;
	}
	@Override
	public void implement(Neuron neuron) {
		// TODO implement reflex creation on the given Neuron
	}
	@Override
	public String getReflexName() {
		return _reflexName;
	}
}
