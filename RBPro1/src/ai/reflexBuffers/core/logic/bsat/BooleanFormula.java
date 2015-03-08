package ai.reflexBuffers.core.logic.bsat;

public class BooleanFormula {
	BooleanTreeNode _root = null;
	int _nVars = 0; // the number of variables in the formula
	
	public BooleanFormula(int nVars, BooleanTreeNode root) {
		_nVars = nVars;
		_root = root;
	}
	public BooleanTreeNode getRoot() {
		return _root;
	}
	public int getVarCount() {
		return _nVars;
	}
}
