package ai.reflexBuffers.utils.diversity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
	// Let at least about a 1000 of nanoseconds pass between re-querying the timer
	public static final int _timerQueryInterval = 1000;
	public static Randomizer _ = new Randomizer();
	private long _remainingEntropy = 0;
	private long _timeMask = System.nanoTime();
	private int _nAfterTimer = 0;
	
	public int nextModulo(int divisor) {
		if( divisor > _remainingEntropy ) {
			_remainingEntropy ^= ThreadLocalRandom.current().nextLong();
			_nAfterTimer++;
			if( _nAfterTimer >= _timerQueryInterval ) {
				_timeMask = System.nanoTime();
				_nAfterTimer = 0;
			}
			_remainingEntropy ^= _timeMask;
			// ensure it's non-negative
			_remainingEntropy &= (~Long.MIN_VALUE);
		}
		assert( _remainingEntropy >= 0 );
		int ans = (int)(_remainingEntropy % divisor);
		_remainingEntropy /= divisor;
		return ans;
	}
	public <T extends Object> ArrayList<T> makePermutatedArray(Collection<T> source) {
		ArrayList<T> ans = new ArrayList<T>(source.size());
		for(T item : source) {
			int oldSize = ans.size(); 
			if( oldSize == 0 ) {
				ans.add(item);
				continue;
			}
			int swapAt = _.nextModulo(oldSize + 1);
			if( swapAt < oldSize ) {
				ans.add(ans.get(swapAt));
				ans.set(swapAt, item);
			} else {
				// decided to insert in the end
				ans.add(item);
			}
		}
		return ans;
	}
}
