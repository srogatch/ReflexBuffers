package ai.reflexBuffers.core.line;

import ai.reflexBuffers.core.Stimulus;
import ai.reflexBuffers.core.tokens.ProvisionedAlgorithm;

// We allocate name LocalExecutor for the major approach to local execution:
//   in a dedicated thread, in the same process. Other approaches like execution in
//   another process, or multiple executors per OS thread, should receive longer names.
public class LocalExecutor extends Executor {
	private static int _objectCounter = 0;
	private int _objectOrdinal = ++_objectCounter;
	private Thread _thread = null;
	
	@Override
	protected void launchImpl() {
		assert( _thread == null );
		_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				watchConveyor();
			}
		}, String.format("LocExec %d", _objectOrdinal));
		_thread.start();
	}
	
	protected void watchConveyor() {
		assert( getConveyor() != null );
		ProvisionedAlgorithm task = null;
		try {
			while( (task = getConveyor().getNextTask()) != null ) {
				Stimulus algorithmOutput = execute(task);
				getConveyor().propagateStimulus(algorithmOutput);
			}
		} catch(InterruptedException ex) {
			// someone doesn't want to run the thread anymore, but did this not in the
			//   normal way of calling Conveyor.requestShutdown()
			ex.printStackTrace(System.err);
		}
	}

	@Override
	protected Stimulus execute(ProvisionedAlgorithm pa) {
		// TODO Auto-generated method stub
		return null;
	}
}
