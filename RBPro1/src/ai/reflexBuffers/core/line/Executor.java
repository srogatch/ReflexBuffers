package ai.reflexBuffers.core.line;

import ai.reflexBuffers.core.Stimulus;
import ai.reflexBuffers.core.tokens.ProvisionedAlgorithm;

public abstract class Executor {
	private Conveyor _conveyor = null;
	
	public void launch(Conveyor conveyor) {
		// For thread-safety, it is allowed only once per executor: before it's started
		assert( _conveyor == null );
		_conveyor = conveyor;
		launchImpl();
	}
	protected abstract void launchImpl();
	protected abstract Stimulus execute(ProvisionedAlgorithm pa);
	
	public Conveyor getConveyor() {
		return _conveyor;
	}
}
