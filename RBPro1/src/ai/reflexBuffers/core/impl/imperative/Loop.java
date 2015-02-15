package ai.reflexBuffers.core.impl.imperative;

// A "while" loop: condition is checked before each traversal over the sequence
public class Loop extends Instruction {
	Condition _condition = null;
	Sequence _sequence = null;
}
