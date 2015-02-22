package ai.reflexBuffers.core.logic.bsat;

import ai.reflexBuffers.utils.stability.CoreLog;

public class DimacsCnfReader extends BooleanFormulaReader {

	@Override
	public BooleanFormula readFromStrings(String[] lines) {
		try {
			BooleanFormula result = catchedReadFromStrings(lines);
			return result;
		} catch(Exception ex) {
			return null;
		}
	}
	protected BooleanFormula catchedReadFromStrings(String[] lines) {
		BooleanFormula ans = null;
		int nClauses = -1;
		for(int i=0; i<lines.length; i++) {
			String curLine = lines[i].trim();
			if( curLine.startsWith("c") ) {
				continue; // skip comment
			}
			if( curLine.startsWith("p ") ) {
				String[] tokens = curLine.split("\\s");
				if( !"cnf".equals(tokens[1]) ) {
					CoreLog._.malformedDimacsCnf(curLine);
					return null;
				}
				int nVars = Integer.parseInt(tokens[2]);
				nClauses = Integer.parseInt(tokens[3]);
				ans = new BooleanFormula(nVars, new BooleanTreeInner(
					BooleanOperation.makeAnd()));
				continue;
			}
			if( ans == null ) {
				// it seems that numbers are coming earlier than definition
				CoreLog._.malformedDimacsCnf(curLine);
				return null;
			}
			nClauses--;
			String[] tokens = curLine.split("\\s");
			if( !("0".equals(tokens[tokens.length-1])) ) {
				// a clause must end with "0"
				CoreLog._.malformedDimacsCnf(curLine);
				return null;
			}
			BooleanTreeInner curClause = ((BooleanTreeInner)ans.getRoot()).makeChildInner(
				BooleanOperation.makeOr());
			for(int j=0; j<=tokens.length-2; j++) {
				int iVar = Integer.parseInt(tokens[j]);
				if( iVar == 0 ) {
					// Zero is not the last token
					CoreLog._.malformedDimacsCnf(curLine);
					return null;
				}
				if( iVar > 0 ) {
					curClause.makeChildLeaf(iVar);
				} else { // iVar < 0
					BooleanTreeInner negated = curClause.makeChildInner(
						BooleanOperation.makeNeg());
					negated.makeChildLeaf(-iVar);
				}
			}
		}
		if( nClauses != 0 ) {
			CoreLog._.malformedDimacsCnf("The difference between the number of clauses"
				+ " defined and the number of clauses read is: " + nClauses);
			return null;
		}
		return ans;
	}
}
