package ai.reflexBuffers.core;

import ai.reflexBuffers.core.tokens.Algorithm;

public class Response {
	private String _name = null;
	// circular dependency between Response and Activator: if not bound via a hashmap
	//   in Reflex, then both objects need reference to each other
	private Activator _activator = null;
	private Reflex _host;
	private Algorithm _algorithm;
	
	public Response(Reflex host, String name, Algorithm algorithm) {
		_host = host;
		_name = name;
		_algorithm = algorithm;
	}
	
	public ActivationCondition getActivationCondition() { 
		return _algorithm.getActivationCondition();
	}
	public String getName() {
		return _name;
	}
	public Algorithm getAlgorithm() {
		return _algorithm;
	}
	public void setActivator(Activator a) {
		// the previous activator must be properly removed first
		assert(_activator == null);
		_activator = a;
	}
	// Returns "true" in case with the new activation condition the response is ready
	//   to activate
	public boolean changeAlgorithm(Algorithm newAlgorithm) {
		_host.removeActivator(_activator);
		_activator = null; // prepare to assign new activator
		_algorithm = newAlgorithm;
		Activator activator = new Activator(_host, this);
		// Activator must have called Response.setActivator
		assert( _activator == activator );
		return activator.canActivate();
	}
}
