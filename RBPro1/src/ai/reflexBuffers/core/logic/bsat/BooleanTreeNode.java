package ai.reflexBuffers.core.logic.bsat;

public abstract class BooleanTreeNode implements Cloneable {
	@Override
	public BooleanTreeNode clone() throws CloneNotSupportedException {
		super.clone();
		// Derived classes must override clone() method, this is just a guard
		return null;
	}
}
