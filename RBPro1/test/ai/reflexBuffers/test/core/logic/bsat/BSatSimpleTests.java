package ai.reflexBuffers.test.core.logic.bsat;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ai.reflexBuffers.core.logic.bsat.BooleanFormula;
import ai.reflexBuffers.core.logic.bsat.DimacsCnfReader;

public class BSatSimpleTests {

	@Test
	public void test1() {
		//fail("Not yet implemented");
		System.out.println("Working Directory = " +
			System.getProperty("user.dir"));
		BooleanFormula bf = new DimacsCnfReader().readFromFile(new File("data/bsat/simpleTest1.inp"));
		assertNotNull(bf);
	}

}
