package ai.reflexBuffers.utils.stability;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ai.reflexBuffers.core.Token;
import ai.reflexBuffers.core.stimuli.RewiringComponent;

public class CoreLog {
	public static CoreLog _ = new CoreLog();
	private PrintWriter _pwr = null;
	
	public CoreLog() {
		this(String.format("coreLog%s.txt", 
			new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())));
	}
	public CoreLog(String filePath) {
		try {
			_pwr = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
		} catch(Exception ex) {
			_pwr = new PrintWriter(System.err);
			ex.printStackTrace(_pwr);
		}
	}
	
	public void tokensToAbsentReflex(String reflexName, Map<String, List<Token>> bufferTokens) {
		_pwr.printf("Found tokens directed to buffers of an absent reflex [%s].", reflexName);
		for(Map.Entry<String, List<Token>> atBuffer : bufferTokens.entrySet()) {
			_pwr.printf(" %d tokens to buffer [%s]. ", atBuffer.getValue().size(), atBuffer.getKey());
			// Maybe print the content of the tokens, if this information would be so much 
			//  desired in logging
		}
		_pwr.println();
	}
	public void rewiringOfAbsentReflex(String reflexName, RewiringComponent rewComp) {
		_pwr.printf("Encountered rewiring [%s] for absent reflex [%s]. Details of rewiring: %s",
			rewComp.getClass().toString(), reflexName, rewComp.toString());
		_pwr.println();
	}
	public void removalOfAbsentResponse(String reflexName, String responseName) {
		_pwr.printf("Encountered removal of absent response [%s] from reflex [%s].",
			responseName, reflexName);
		_pwr.println();
	}
	public void insufficientProvision(String bufferName) {
		_pwr.printf("Requested more than provisioned in buffer [%s].", bufferName);
		_pwr.println();
		new Throwable().printStackTrace(_pwr);
	}
	public void unexhaustedProvision(Map<String, ArrayList<Token>> remaining) {
		_pwr.println("Unexhausted provision detected.");
		for(Map.Entry<String, ArrayList<Token>> e : remaining.entrySet()) {
			_pwr.printf("  Buffer [%s] has tokens:", e.getKey());
			for(Token token : e.getValue()) {
				_pwr.printf(" [%s]", token.toString());
			}
			_pwr.println();
		}
		new Throwable().printStackTrace(_pwr);
	}
	public void failedToClose(Closeable c) {
		_pwr.println("Failed to close: " + c.toString());
		new Throwable().printStackTrace(_pwr);
	}
	public void malformedDimacsCnf(String line) {
		_pwr.println("Malformed Dimacs CNF at: " + line);
		new Throwable().printStackTrace(); // to see where exactly in the parser
	}
	
}
