package ai.reflexBuffers.utils.stability;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
	
}
