package ai.reflexBuffers.core.line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ai.reflexBuffers.core.Neuron;
import ai.reflexBuffers.core.Stimulus;
import ai.reflexBuffers.core.tokens.ProvisionedAlgorithm;

public class Conveyor {
	public static final int _shutdownPollPeriodMS = 500;
	private boolean _bShutdown = false;
	private BlockingQueue<ProvisionedAlgorithm> _tasks 
		= new LinkedBlockingQueue<ProvisionedAlgorithm>();
	private Set<Executor> _executors = new HashSet<Executor>();
	private Neuron _neuron = null;
	
	public Conveyor(Neuron neuron, ArrayList<Executor> executors) {
		_neuron = neuron;
		for(Executor e : executors) {
			// to guard repetitions in the array list passed
			boolean contained = !(_executors.add(e));
			assert( !contained );
			e.launch(this);
		}
	}
	
	public void requestShutdown() {
		_bShutdown = true;
	}
	
	public ProvisionedAlgorithm getNextTask() throws InterruptedException {
		while( !_bShutdown ) {
			ProvisionedAlgorithm ans = _tasks.poll(_shutdownPollPeriodMS, TimeUnit.MILLISECONDS);
			if( ans != null ) {
				return ans;
			}
		}
		return null;
	}
	
	public void propagateStimulus(Stimulus stimulus) throws InterruptedException {
		_neuron.propagateStimulus(stimulus);
	}
	
	public void orderExecution(ProvisionedAlgorithm provAlg) {
		_tasks.add(provAlg);
	}
}
