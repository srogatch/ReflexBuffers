package ai.reflexBuffers.core.logic.bsat;

public class BooleanTreeLeaf extends BooleanTreeNode implements Cloneable {
	int _iVar;
	
	public BooleanTreeLeaf(int iVar) {
		_iVar = iVar;
	}
	@Override
	public BooleanTreeLeaf clone() {
		return new BooleanTreeLeaf(_iVar);
	}
}
