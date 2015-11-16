/* Copyright 2015 CINCHEO SAS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jsweet.test.transpiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsweet.test.transpiler.source.init.Constructor;
import org.jsweet.test.transpiler.source.init.ConstructorField;
import org.jsweet.test.transpiler.source.init.ConstructorFieldInInterface;
import org.jsweet.test.transpiler.source.init.ConstructorMethod;
import org.jsweet.test.transpiler.source.init.ConstructorMethodInInterface;
import org.jsweet.test.transpiler.source.init.Initializer;
import org.jsweet.test.transpiler.source.init.InitializerStatementConditionError;
import org.jsweet.test.transpiler.source.init.InitializerStatementError;
import org.jsweet.test.transpiler.source.init.InterfaceRawConstruction;
import org.jsweet.test.transpiler.source.init.MultipleMains;
import org.jsweet.test.transpiler.source.init.NoOptionalFieldsInClass;
import org.jsweet.test.transpiler.source.init.StaticInitializer;
import org.jsweet.test.transpiler.source.structural.OptionalField;
import org.jsweet.test.transpiler.source.structural.OptionalFieldError;
import org.jsweet.transpiler.JSweetProblem;
import org.jsweet.transpiler.util.EvaluationResult;
import org.junit.Assert;
import org.junit.Test;

public class InitTests extends AbstractTest {

	@Test
	public void testStaticInitializer() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			EvaluationResult result = transpiler.eval(logHandler, getSourceFile(StaticInitializer.class));
			assertEquals(4, result.<Number> get("n").intValue());
			assertEquals("test", result.<String> get("s"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testInitializer() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			EvaluationResult result = transpiler.eval(logHandler, getSourceFile(Initializer.class));
			assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
			assertEquals(4, result.<Number> get("out").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testInitializerStatementError() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(InitializerStatementError.class));
			assertEquals("There should be 1 problem", 1, logHandler.reportedProblems.size());
			assertTrue("Missing expected error on wrong initializer", logHandler.reportedProblems.contains(JSweetProblem.INVALID_INITIALIZER_STATEMENT));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testInitializerStatementWithConditionError() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(InitializerStatementConditionError.class));
			assertEquals("Missing expected error on non-optional field", 1, logHandler.reportedProblems.size());
			assertEquals("Missing expected error on wrong initializer", JSweetProblem.INVALID_INITIALIZER_STATEMENT, logHandler.reportedProblems.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testOptionalField() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(OptionalField.class));
			assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testOptionalFieldError() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(OptionalFieldError.class));
			assertEquals("There should be 1 problem", 1, logHandler.reportedProblems.size());
			assertTrue("Missing expected error on optional field", logHandler.reportedProblems.contains(JSweetProblem.UNINITIALIZED_FIELD));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testNoOptionalFieldsInClass() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(NoOptionalFieldsInClass.class));
			assertEquals("Missing expected warning on non-optional field", 1, logHandler.reportedProblems.size());
			assertTrue("Missing expected warning on non-optional field", logHandler.reportedProblems.contains(JSweetProblem.USELESS_OPTIONAL_ANNOTATION));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testConstructors() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(Constructor.class));
			assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testConstructorMethod() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(ConstructorMethod.class));
			Assert.assertEquals("Missing constructor keyword error", 1, logHandler.reportedProblems.size());
			Assert.assertEquals("Missing constructor keyword error", JSweetProblem.CONSTRUCTOR_MEMBER, logHandler.reportedProblems.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testConstructorField() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(ConstructorField.class));
			Assert.assertEquals("Missing constructor keyword error", 1, logHandler.reportedProblems.size());
			Assert.assertEquals("Missing constructor keyword error", JSweetProblem.CONSTRUCTOR_MEMBER, logHandler.reportedProblems.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testConstructorFieldInInterface() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(ConstructorFieldInInterface.class));
			Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testConstructorMethodInInterface() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(ConstructorMethodInInterface.class));
			Assert.assertEquals("Missing expected error on constructor method", 1, logHandler.reportedProblems.size());
			Assert.assertEquals("Missing expected error on constructor method", JSweetProblem.CONSTRUCTOR_MEMBER, logHandler.reportedProblems.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testInterfaceRawConstruction() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			transpiler.transpile(logHandler, getSourceFile(InterfaceRawConstruction.class));
			Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

	@Test
	public void testMultipleMains() {
		TestTranspilationHandler logHandler = new TestTranspilationHandler();
		try {
			EvaluationResult result = transpiler.eval(logHandler, getSourceFile(MultipleMains.class));
			Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());

			assertEquals(0, result.<Number> get("a").intValue());
			assertNull(result.<Number> get("b"));
			assertEquals(1, result.<Number> get("c").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception occured while running test");
		}
	}

}
