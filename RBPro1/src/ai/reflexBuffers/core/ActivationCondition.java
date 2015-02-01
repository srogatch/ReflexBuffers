package ai.reflexBuffers.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActivationCondition {
	private Map<String, Integer> _requiredBuffers = new HashMap<String, Integer>();
	
	public Set<Map.Entry<String, Integer>> getRequiredBuffers() {
		 return _requiredBuffers.entrySet();
	}
	public int getRequirement(String bufferName) {
		Integer requirement = _requiredBuffers.get(bufferName);
		if( requirement == null ) {
			return 0;
		}
		return requirement;
	}
	public Set<Map.Entry<String, Integer>> getAllRequirements() {
		return _requiredBuffers.entrySet();
	}
}
