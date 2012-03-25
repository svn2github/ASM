/* Software Name : AsmDex
 * Version : 1.0
 *
 * Copyright © 2012 France Télécom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.objectweb.asmdex;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asmdex.applicationAdapterTest.ApplicationAdapterAddCode;
import org.objectweb.asmdex.applicationAdapterTest.ApplicationAdapterAddMethod;
import org.objectweb.asmdex.applicationAdapterTest.ApplicationAdapterAddSwitchCase;
import org.objectweb.asmdex.applicationAdapterTest.ApplicationAdapterRemoveMethod;
import org.objectweb.asmdex.applicationAdapterTest.ApplicationAdapterResizeGoto8Bits;
import org.objectweb.asmdex.applicationAdapterTest.ApplicationAdapterResizeGoto8BitsDoubleRefs;
import org.objectweb.asmdex.applicationAdapterTest.ApplicationAdapterResizeGoto8BitsDoubleRefsDoubleLabels;
import org.objectweb.asmdex.applicationAdapterTest.ApplicationAdapterShiftSwitchCase;
import org.objectweb.asmdex.applicationAdapterTest.GenerateBasicDexFile;
import org.objectweb.asmdex.applicationAdapterTest.GenerateDexFileShiftedSwitchCase;
import org.objectweb.asmdex.applicationAdapterTest.GenerateDexFileSwitchCase;
import org.objectweb.asmdex.applicationAdapterTest.GenerateExpectedDexFileAddAndRemoveMethod;
import org.objectweb.asmdex.applicationAdapterTest.GenerateExpectedDexFileAddCode;
import org.objectweb.asmdex.applicationAdapterTest.GenerateExpectedDexFileAddMethod;
import org.objectweb.asmdex.applicationAdapterTest.GenerateExpectedDexFileRemoveMethod;
import org.objectweb.asmdex.applicationAdapterTest.GenerateExpectedDexFileResizeGoto8Bits;
import org.objectweb.asmdex.applicationAdapterTest.GenerateExpectedDexFileResizeGoto8BitsDoubleRefs;
import org.objectweb.asmdex.applicationAdapterTest.GenerateExpectedDexFileResizeGoto8BitsDoubleRefsDoubleLabels;
import org.objectweb.asmdex.applicationAdapterTest.GenerateOriginalDexFileResizeGoto8Bits;
import org.objectweb.asmdex.applicationAdapterTest.GenerateOriginalDexFileResizeGoto8BitsDoubleRefs;
import org.objectweb.asmdex.applicationAdapterTest.GenerateOriginalDexFileResizeGoto8BitsDoubleRefsDoubleLabels;
import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.Opcodes;

/**
 * Tests the Application Adapter.
 * 
 * The scripts below generate dex files thanks to the Reader, Adapter(s) and Writer.
 * Each dex file is disassembled thanks to baksmali and compare to its "expected" .smali file.
 * 
 * @author Julien Névo
 */
public class ApplicationAdapterTest {

	/**
	 * Initializes an Adapter test by creating a temporary folder.
	 */
	@Before
	public void initializeAdapterTest() {
		TestUtil.removeTemporaryFolder();
		TestUtil.createTemporaryFolder();
	}
	
	/**
	 * End adapter test.
	 */
	@After
	public void endAdapterTest() {
		TestUtil.removeTemporaryFolder();
	}
	
	/**
	 * Tests with an Adapter that does nothing.
	 * @throws IOException 
	 */
	@Test
	public void testAdapterDoNothing() throws IOException {
		// Transforms the "original file" thanks to the Reader, Adapter, and Writer,
		// and generates an "adapted" dex file.
		byte[] originalBytes = GenerateBasicDexFile.generate();
		
		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, originalBytes);
		ApplicationVisitor aa = new ApplicationVisitor(Opcodes.ASM4, aw){};
		ar.accept(aa, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), originalBytes));
	}
	
	/**
	 * Tests that adds a few instructions at the end of the onCreate method.
	 * @throws IOException 
	 */
	@Test
	public void testAdapterSimpleAddInstructions() throws IOException {
		// Transforms the "original file" thanks to the Reader, Adapter, and Writer,
		// and generates an "adapted" dex file.
		byte[] originalBytes = GenerateBasicDexFile.generate();
		byte[] expectedBytes = GenerateExpectedDexFileAddCode.generate();
		
		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, originalBytes);
		ApplicationVisitor aa = new ApplicationAdapterAddCode(aw);
		ar.accept(aa, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), expectedBytes));
	}
	
	/**
	 * Tests that checks the resize of an 8-bit GOTO into a 16-bit instruction as we include more
	 * operations between the instruction and its target.
	 * Expects a double-resize (Label1 - GOTO Label2 - NOPS * x - Label2 - GOTO Label1).
	 * @throws IOException 
	 */
	@Test
	public void testAdapterResizeGoto8Bits() throws IOException {
		byte[] originalBytes = GenerateOriginalDexFileResizeGoto8Bits.generate();
		byte[] expectedBytes = GenerateExpectedDexFileResizeGoto8Bits.generate();
		
		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, originalBytes);
		ApplicationVisitor aa = new ApplicationAdapterResizeGoto8Bits(aw);
		ar.accept(aa, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), expectedBytes));
	}
	
	/**
	 * Tests that checks the resize of an 8-bit GOTO into a 16-bit instruction as we include more
	 * operations between the instruction and its target.
	 * Expects a double-resize and includes a double forward references to one label
	 * (Label1 - GOTO Label2 - GOTO Label2 - NOPS * x - Label2 - GOTO Label1 - GOTO Label2).
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testAdapterResizeGoto8BitsDoubleRefs() throws IOException {
		byte[] originalBytes = GenerateOriginalDexFileResizeGoto8BitsDoubleRefs.generate();
		byte[] expectedBytes = GenerateExpectedDexFileResizeGoto8BitsDoubleRefs.generate();

		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, originalBytes);
		ApplicationVisitor aa = new ApplicationAdapterResizeGoto8BitsDoubleRefs(aw);
		ar.accept(aa, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), expectedBytes));
	}
	
	/**
	 * Tests that checks the resize of an 8-bit GOTO into a 16-bit instruction as we include more
	 * operations between the instruction and its target, as two labels refers the same instruction.
	 * Expects a double-resize and includes two forward references to two labels
	 * (Label1 - Label2 - GOTO Label3 - GOTO Label4 - NOPS * x -
	 * Label3 - Label4 - GOTO Label1 - GOTO Label2).
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testAdapterResizeGoto8BitsDoubleRefsDoubleLabels() throws IOException {
		byte[] originalBytes = GenerateOriginalDexFileResizeGoto8BitsDoubleRefsDoubleLabels.generate();
		byte[] expectedBytes = GenerateExpectedDexFileResizeGoto8BitsDoubleRefsDoubleLabels.generate();

		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, originalBytes);
		ApplicationVisitor aa = new ApplicationAdapterResizeGoto8BitsDoubleRefsDoubleLabels(aw);
		ar.accept(aa, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), expectedBytes));
	}
	
	
	/**
	 * Tests that removes a Method.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testAdapterRemoveMethod() throws IOException {
		byte[] originalBytes = GenerateBasicDexFile.generate();
		byte[] expectedBytes = GenerateExpectedDexFileRemoveMethod.generate();

		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, originalBytes);
		ApplicationVisitor aa = new ApplicationAdapterRemoveMethod(aw);
		ar.accept(aa, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), expectedBytes));
	}
	
	/**
	 * Tests that adds a Method and adds a call to this method in another Method.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testAdapterAddMethod() throws IOException {
		byte[] originalBytes = GenerateBasicDexFile.generate();
		byte[] expectedBytes = GenerateExpectedDexFileAddMethod.generate();

		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, originalBytes);
		ApplicationVisitor aa = new ApplicationAdapterAddMethod(aw);
		ar.accept(aa, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), expectedBytes));
	}
	
	/**
	 * Tests that adds a Method and adds a call to this method in another Method,
	 * then remove the first method. This consists in linking the testAdapterAddMethod
	 * and the testAdapterRemoveMethod tests.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testAdapterAddAndRemoveMethod() throws IOException {
		byte[] originalBytes = GenerateBasicDexFile.generate();
		byte[] expectedBytes = GenerateExpectedDexFileAddAndRemoveMethod.generate();

		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, originalBytes);
		ApplicationVisitor aa2 = new ApplicationAdapterRemoveMethod(aw);
		ApplicationVisitor aa1 = new ApplicationAdapterAddMethod(aa2);
		ar.accept(aa1, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), expectedBytes));
	}
	
	/**
	 * Tests that adds a Switch Case to one method.
	 * @throws IOException
	 */
	@Test
	public void testAdapterAddSwitchCase() throws IOException {
		byte[] originalBytes = GenerateBasicDexFile.generate();
		byte[] expectedBytes = GenerateDexFileSwitchCase.generate();

		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, originalBytes);
		ApplicationVisitor aa = new ApplicationAdapterAddSwitchCase(aw);
		ar.accept(aa, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), expectedBytes));
	}
	
	/**
	 * Tests that shifts a Switch Case to one method by adding NOPs before its code.
	 * @throws IOException
	 */
	@Test
	public void testAdapterShiftSwitchCase() throws IOException {
		byte[] originalBytes = GenerateDexFileSwitchCase.generate();
		byte[] expectedBytes = GenerateDexFileShiftedSwitchCase.generate();

		ApplicationWriter aw = new ApplicationWriter();
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4,originalBytes);
		ApplicationVisitor aa = new ApplicationAdapterShiftSwitchCase(aw);
		ar.accept(aa, 0);
		
		assertTrue(testAdapter(aw.toByteArray(), expectedBytes));
	}
	
	
	// ----------------------------------------------------------------------
	// Private methods
	// ----------------------------------------------------------------------
	
	/**
	 * Tests the Adapter result by generating the baksmali files for both the "adapter" dex file
	 * and the "expected" dex file, given as bytes.
	 * @param adapterBytes the bytes from the Adapter.
	 * @param expectedBytes the bytes we expect to have.
	 * @return true if the baksmali files match.
	 * @throws IOException
	 */
	private boolean testAdapter(byte[] adapterBytes, byte[] expectedBytes) throws IOException {
		File adaptedFile = TestUtil.createFileFromByteArray(adapterBytes,
				TestUtil.TEMP_FOLDER_ROOT + TestUtil.FILENAME_ADAPTED_DEX);

		// Uses baksmali to generate the smali files related to the adapted file.
		TestUtil.baksmali(new String[] { adaptedFile.getAbsolutePath(),
				"-o" + TestUtil.TEMP_FOLDER_GENERATED});
		
		// Generates the "expected" dex file thanks to the writer.
		File expectedFile = TestUtil.createFileFromByteArray(expectedBytes,
				TestUtil.TEMP_FOLDER_ROOT + TestUtil.FILENAME_EXPECTED_DEX);
		
		// Uses baksmali to generate the smali files related to the "expected" file.
		TestUtil.baksmali(new String[] { expectedFile.getAbsolutePath(),
				"-o" + TestUtil.TEMP_FOLDER_EXPECTED});
		
		// Compares the two outputs.
		boolean result = TestUtil.testSmaliFoldersEquality(TestUtil.TEMP_FOLDER_GENERATED,
				TestUtil.TEMP_FOLDER_EXPECTED, false);
		
		return result;
	}	
}
