package ai.reflexBuffers.core.logic.bsat;

import java.util.ArrayList;

public class BooleanTreeInner extends BooleanTreeNode {
	private BooleanOperation _op = new BooleanOperation(BooleanOperation._noop);
	private ArrayList<BooleanTreeNode> _children = new ArrayList<BooleanTreeNode>();
	
	public BooleanTreeInner(BooleanOperation bop) {
		_op = bop;
	}
	public BooleanTreeInner makeChildInner(BooleanOperation bop) {
		BooleanTreeInner ans = new BooleanTreeInner(bop);
		_children.add(ans);
		return ans;
	}
	public BooleanTreeLeaf makeChildLeaf(int iVar) {
		BooleanTreeLeaf ans = new BooleanTreeLeaf(iVar);
		_children.add(ans);
		return ans;
	}
}
