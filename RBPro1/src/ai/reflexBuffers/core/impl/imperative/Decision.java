package ai.reflexBuffers.core.impl.imperative;

public class Decision extends Instruction {
	Condition _condition = null;
	// If true then branch 1 else branch 0 is executed
	Sequence _branches[] = new Sequence[]{null, null};
}
