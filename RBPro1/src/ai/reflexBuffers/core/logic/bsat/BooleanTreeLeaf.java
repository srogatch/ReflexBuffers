package ai.reflexBuffers.core.logic.bsat;

public class BooleanTreeLeaf extends BooleanTreeNode {
	int _iVar;
	
	public BooleanTreeLeaf(int iVar) {
		_iVar = iVar;
	}
}
