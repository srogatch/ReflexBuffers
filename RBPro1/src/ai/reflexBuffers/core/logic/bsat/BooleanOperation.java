package ai.reflexBuffers.core.logic.bsat;

public class BooleanOperation implements Cloneable {
	public static final int _noop = 0; // special value for no operation
	public static final int _neg = 1; // negation
	public static final int _and = 2;
	public static final int _or = 3;
	public static final int _exor = 4; // exclusive or
	public static final int _impl = 5; // implication
	
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
	public static BooleanOperation makeExor() {
		return new BooleanOperation(_exor);
	}
	public static BooleanOperation makeImpl() { 
		return new BooleanOperation(_impl);
	}
}