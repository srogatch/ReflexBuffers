package ai.reflexBuffers.startup;

import java.util.ArrayList;
import java.util.Scanner;

import ai.reflexBuffers.core.Neuron;
import ai.reflexBuffers.core.line.Conveyor;
import ai.reflexBuffers.core.line.Executor;
import ai.reflexBuffers.core.line.LocalExecutor;

public class LocalStartup {
	
	public static void main(String[] args) {
		Neuron neuron = new Neuron();
		//TODO: load the state from a file
		
		ArrayList<Executor> executors = new ArrayList<Executor>();
		int nProcessors = Runtime.getRuntime().availableProcessors();
		for(int i=0; i<nProcessors; i++) {
			executors.add(new LocalExecutor());
		}
		Conveyor conveyor = new Conveyor(neuron, executors);
		neuron.setConveyor(conveyor);
		
		Scanner scin = new Scanner(System.in);
		while(scin.hasNextLine()) {
			String cmd = scin.next();
			if( cmd.equals("quit") ) {
				break;
			}
		}
		scin.close();
		
		//TODO: save to a file reflexes, buffers, responses, tokens, conveyor tasks
	}

}
