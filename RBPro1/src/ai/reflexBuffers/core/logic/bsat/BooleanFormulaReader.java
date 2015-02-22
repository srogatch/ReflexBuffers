package ai.reflexBuffers.core.logic.bsat;

import java.io.File;

import ai.reflexBuffers.utils.convenience.FileHelper;

public abstract class BooleanFormulaReader {
	public BooleanFormula readFromFile(File fp) {
		String []strings = FileHelper.readAllLines(fp);
		return readFromStrings(strings);
	}
	public abstract BooleanFormula readFromStrings(String[] lines);
}
