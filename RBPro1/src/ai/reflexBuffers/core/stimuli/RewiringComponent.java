package ai.reflexBuffers.core.stimuli;

import ai.reflexBuffers.core.Neuron;

public abstract class RewiringComponent {
	public abstract void implement(Neuron neuron);
	// It seems that every rewiring is associated with some reflex, and it
	//   would be convenient to sort rewiring components into buckets by the
	//   hallmark of Reflex name
	public abstract String getReflexName();
}
