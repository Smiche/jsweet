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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.jsweet.test.transpiler.source.tscomparison.AbstractClasses;
import org.jsweet.test.transpiler.source.tscomparison.ActualScoping;
import org.jsweet.test.transpiler.source.tscomparison.CompileTimeWarnings;
import org.jsweet.test.transpiler.source.tscomparison.OtherThisExample;
import org.jsweet.test.transpiler.source.tscomparison.SaferVarargs;
import org.jsweet.test.transpiler.source.tscomparison.StrongerTyping;
import org.jsweet.test.transpiler.source.tscomparison.ThisIsThis;
import org.jsweet.transpiler.SourceFile;
import org.jsweet.transpiler.util.EvaluationResult;
import org.junit.Ignore;
import org.junit.Test;

public class TsComparisonTest extends AbstractTest {

	@Ignore
	@Test
	public void strongerTypingTest() {
		// jsweet part
		SourceFile file = getSourceFile(StrongerTyping.class);
		eval(file);

		// ts part
		evalTs(getTsSourceFile(file));
	}

	@Ignore
	@Test
	public void compileTimeWarningsTest() {
		// jsweet part
		SourceFile file = getSourceFile(CompileTimeWarnings.class);
		eval(file);

		// ts part
		evalTs(getTsSourceFile(file));
	}

	@Test
	public void abstractClassesTest() {
		// jsweet part
		SourceFile file = getSourceFile(AbstractClasses.class);
		eval(file);
		
		// ts part
		evalTs(getTsSourceFile(file));
	}

	@Ignore
	@Test
	public void actualScopingTest() {
		// jsweet part
		SourceFile file = getSourceFile(ActualScoping.class);
		eval(file);

		// ts part
		evalTs(getTsSourceFile(file));
	}

	@Ignore
	@Test
	public void thisIsThisTest() {
		// jsweet part
		SourceFile file = getSourceFile(ThisIsThis.class);
		eval(file);

		// ts part
		evalTs(getTsSourceFile(file));
	}

	@Test
	public void otherThisExampleTest() {
		// jsweet part
		SourceFile file = getSourceFile(OtherThisExample.class);
		eval(file);
		

		// ts part (to be done)
		//evalTs(getTsSourceFile(file));
	}
	
	@Ignore
	@Test
	public void saferVarargsTest() {
		// jsweet part

		SourceFile file = getSourceFile(SaferVarargs.class);
		EvaluationResult result = eval(file);
		assertEquals("foo", result.get("firstArg"));

		// ts part
		result = evalTs(getTsSourceFile(file));
		
		assertTrue(result.get("firstArg").getClass().isArray());
		Object[] res = (Object[]) result.get("firstArg");
		assertEquals("blah", res[0]);
		assertEquals("bluh", res[1]);
	}

	private TsSourceFile getTsSourceFile(SourceFile jsweetSourceFile) {
		String javaTestFilePath = jsweetSourceFile.getJavaFile().getAbsolutePath();
		File tsFile = new File(javaTestFilePath.substring(0, javaTestFilePath.length() - 5) + ".ts");
		TsSourceFile tsSourceFile = new TsSourceFile(tsFile);
		return tsSourceFile;
	}

	private EvaluationResult evalTs(TsSourceFile sourceFile) {
		return evalTs(sourceFile, false);
	}

	private EvaluationResult evalTs(TsSourceFile sourceFile, boolean expectErrors) {
		try {
			System.out.println("running tsc: " + sourceFile);

			transpiler.setTsOutputDir(sourceFile.getTsFile().getParentFile());
			EvaluationResult result = eval(sourceFile);
			FileUtils.deleteQuietly(sourceFile.getJsFile());

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			fail("Cannot compile Typescript file: " + sourceFile);
			return null;
		}
	}

	private class TsSourceFile extends SourceFile {
		public TsSourceFile(File tsFile) {
			super(null);
			this.setTsFile(tsFile);
		}

		@Override
		public String toString() {
			return getTsFile().toString();
		}
	}
}
