package ai.reflexBuffers.core.logic.bsat;

public class BooleanOperation implements Cloneable {
	public static final int _noop = 0; // special value for no operation
	public static final int _neg = 1; // negation
	public static final int _and = 2;
	public static final int _or = 3;
	// not necessary
	//public static final int _exor = 4; // exclusive or
	// lunatism
	//public static final int _impl = 5; // implication
	
	private int _opCode;
	
	public BooleanOperation(int opCode) {
		_opCode = opCode;
	}
	public int getOpCode() {
		return _opCode;
	}
	@Override
	public BooleanOperation clone() {
		return new BooleanOperation(_opCode);
	}
	public static BooleanOperation makeNoop() {
		return new BooleanOperation(_noop);
	}
	public static BooleanOperation makeNeg() {
		return new BooleanOperation(_neg);
	}
	public static BooleanOperation makeAnd() {
		return new BooleanOperation(_and);
	}
	public static BooleanOperation makeOr() {
		return new BooleanOperation(_or);
	}
	public static BooleanOperation makeExor() throws Exception {
		throw new Exception("XOR is not currently supported: use a$b == ((!a)b)|(a(!b))");
		// replacement all a$b (XORs) with ((!a)b)|(a(!b)) increases formula size
		//   no more than twice, so the algorithmic complexity stays polynomial
		//return new BooleanOperation(_exor);
	}
	public static BooleanOperation makeImpl() throws Exception { 
		throw new Exception("Implication operation seems a lunatism, sorry for the shock.");
		// a b (a=>b)
		// 0 0   ?
		// 0 1   ?
		// 1 0   0
		// 1 1   ?
		// In implication operation there is only one pair for which the result can be
		//   truly inferred: when b=0 given a=1 we can truly conclude that "a" does not
		//   imply "b". For the rest of cases the result is undefined.
		//return new BooleanOperation(_impl);
	}
}