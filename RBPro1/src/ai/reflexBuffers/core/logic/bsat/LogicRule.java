package ai.reflexBuffers.core.logic.bsat;

// a ? b : c
public class LogicRule extends BooleanTreeNode {
	private BooleanTreeLeaf _condition = null;
	private BooleanTreeNode _ifTrue = null;
	private BooleanTreeNode _ifFalse = null;
	
	public LogicRule(BooleanTreeLeaf condition, BooleanTreeNode ifTrue, BooleanTreeNode ifFalse) {
		_condition = condition;
		_ifTrue = ifTrue;
		_ifFalse = ifFalse;
	}
}
