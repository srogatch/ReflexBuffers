package ai.reflexBuffers.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Buffer {
	private Reflex _host;
	private String _name;
	private Queue<Token> _tokens  = new LinkedList<Token>();
	private Set<Activator> _waitingActivators = new HashSet<Activator>();
	private Set<Activator> _fulfilledActivators = new HashSet<Activator>();
	
	public Buffer(Reflex host, String name) {
		_host = host;
		_name = name;
	}
	public int getTokenCount() {
		return _tokens.size();
	}
	public String getName() {
		return _name;
	}
	void onActivatorAdded(Activator activator, boolean isWaiting) {
		boolean contained = false;
		if( isWaiting ) {
			contained = !(_waitingActivators.add(activator));
			_host.addActivatorTrackingSet(activator, _waitingActivators); 
		} else {
			contained = !(_fulfilledActivators.add(activator));
			_host.addActivatorTrackingSet(activator, _fulfilledActivators);
		}
		assert(!contained);
	}
	// Returns "null" if nothing fires, otherwise returns the array of firing activators
	public ArrayList<Activator> acceptTokens(List<Token> tokens) {
		_tokens.addAll(tokens);
		ArrayList<Activator> firing = new ArrayList<Activator>(_waitingActivators.size());
		ArrayList<Activator> justFulfilled = new ArrayList<Activator>(_waitingActivators.size());
		for(Activator wa : _waitingActivators) {
			if( !wa.onTokenArrived(this) ) {
				justFulfilled.add(wa);
				if( wa.canActivate() ) {
					firing.add(wa);
				}
			}
		}
		for(Activator a : justFulfilled) {
			_waitingActivators.remove(a);
			_host.removeActivatorTrackingSet(a, _waitingActivators);
			_fulfilledActivators.add(a);
			_host.addActivatorTrackingSet(a, _fulfilledActivators);
		}
		if( firing.size() == 0 ) {
			return null;
		}
		return firing;
	}
	// At least 1 token must be requested, and no more than the number of tokens in the queue
	public ArrayList<Token> provideTokens(int count) {
		assert(1 <= count && _tokens.size() <= count);
		ArrayList<Token> ans = new ArrayList<Token>(count);
		for(int i=0; i<count; i++) {
			ans.add(_tokens.remove());
		}
		ArrayList<Activator> toMove = new ArrayList<Activator>(_fulfilledActivators.size());
		for(Activator a : _fulfilledActivators) {
			if( a.onTokensConsumed(this) ) {
				toMove.add(a);
			}
		}
		for(Activator a : toMove) {
			_fulfilledActivators.remove(a);
			_host.removeActivatorTrackingSet(a, _fulfilledActivators); 
			_waitingActivators.add(a);
			_host.addActivatorTrackingSet(a, _waitingActivators); 
		}
		return ans;
	}
}
