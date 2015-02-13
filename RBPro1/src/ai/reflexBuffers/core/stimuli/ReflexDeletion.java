package ai.reflexBuffers.core.stimuli;

import ai.reflexBuffers.core.Neuron;

public class ReflexDeletion extends RewiringComponent {
	private String _reflexName;
	public ReflexDeletion(String reflexName) {
		_reflexName = reflexName;
	}
	@Override
	public void implement(Neuron neuron) {
		neuron.deleteReflex(this);
	}
	@Override
	public String getReflexName() {
		return _reflexName;
	}
}
