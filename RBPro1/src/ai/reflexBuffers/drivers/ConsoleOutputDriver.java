package ai.reflexBuffers.drivers;

import java.util.ArrayList;

import ai.reflexBuffers.core.Token;
import ai.reflexBuffers.core.alg.Driver;
import ai.reflexBuffers.core.alg.Provision;

public class ConsoleOutputDriver extends Driver {
	private String _bufferName = null;
	// Maybe at some point the program learns to output continuously, so that
	//   console output driver delimiters are not needed anymore by that time
	boolean _doDelimit;
	
	public ConsoleOutputDriver(String bufferName, boolean doDelimit) {
		_bufferName = bufferName;
		_doDelimit = doDelimit;
	}

	@Override
	public void run(Provision provision) {
		ArrayList<Token> tokens = provision.give(_bufferName);
		if( _doDelimit ) {
			System.out.println(">>> Start ConOut driver >>>");
		}
		for(int i=0; i<tokens.size(); i++) {
			String s = tokens.get(i).toString();
			System.out.print(s);
		}
		System.out.println();
		if( _doDelimit ) {
			System.out.println("<<< Stop ConOunt driver <<<");
		}
		//TODO: it may be more appropriate to move this call to an outer method
		provision.expectExhausted();
	}
}