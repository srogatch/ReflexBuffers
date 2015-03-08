package ai.reflexBuffers.core.logic.bsat;

public class BsatSolver {
	//// Initialized in constructor
	private BooleanFormula _bfInp = null;
	//// Used in computations
	private BooleanFormula _bfHybrid = null;
	
	public BsatSolver(BooleanFormula bfInp) {
		_bfInp = bfInp;
	}
	
	public BooleanTreeNode generateHybrid(BooleanTreeNode source) throws Exception {
		if( source instanceof BooleanTreeLeaf ) {
			return source.clone();
		}
		if( source instanceof LogicRule ) {
			throw new Exception("Logic rules are not expected in input boolean trees.");
		}
		assert(source instanceof BooleanTreeInner);
		BooleanTreeInner bti = (BooleanTreeInner)source;
		switch(bti.getOperation().getOpCode()) {
		case BooleanOperation._neg:
			assert( bti.getChildCount() == 1 );
			BooleanTreeNode child = bti.getChildAt(0);
			if( !(child instanceof BooleanTreeLeaf) ) {
				throw new Exception("Only CNFs are supported currently. Negations of"
					+ " boolean subtrees should be replaced using negated conjunctive"
					+ " to disjunctive and negated disjunctive to conjunctive"
					+ " conversions: !(ab) into ((!a)|(!b)), !(a|b) into ((!a)(!b)).");
			}
			//TODO: implement conversion into hybrid tree
		}
	}
	public void go() throws Exception {
		BooleanTreeNode hybridRoot = generateHybrid(_bfInp.getRoot());
		_bfHybrid = new BooleanFormula(_bfInp.getVarCount(), hybridRoot);
	}
}
