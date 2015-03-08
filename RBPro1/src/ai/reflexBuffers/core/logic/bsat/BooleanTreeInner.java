package ai.reflexBuffers.core.logic.bsat;

import java.util.ArrayList;

public class BooleanTreeInner extends BooleanTreeNode implements Cloneable {
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
	public LogicRule makeChildRule(BooleanTreeLeaf condition, BooleanTreeNode ifTrue,
		BooleanTreeNode ifFalse)
	{
		LogicRule lr = new LogicRule(condition, ifTrue, ifFalse);
		_children.add(lr);
		return lr;
	}
	@Override
	public BooleanTreeInner clone() throws CloneNotSupportedException {
		BooleanTreeInner ans = new BooleanTreeInner(_op.clone());
		for(int i=0; i<_children.size(); i++) {
			BooleanTreeNode childClone = _children.get(i).clone();
			ans._children.add(childClone);
		}
		return ans;
	}
	public BooleanOperation getOperation() {
		return _op;
	}
	public int getChildCount() {
		return _children.size();
	}
	public BooleanTreeNode getChildAt(int index) {
		assert( index < getChildCount() );
		BooleanTreeNode ans = _children.get(index);
		return ans;
	}
}
