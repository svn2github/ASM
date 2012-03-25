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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import org.objectweb.asmdex.applicationReaderTest.ApplicationTestVisitor;
import org.objectweb.asmdex.applicationReaderTest.LogListAllInstructions;
import org.objectweb.asmdex.applicationReaderTest.LogListAnnotationTests;
import org.objectweb.asmdex.applicationReaderTest.LogListExceptions;
import org.objectweb.asmdex.applicationReaderTest.LogListHelloWorld;
import org.objectweb.asmdex.logging.LogList;
import org.objectweb.asmdex.logging.Logger;
import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.Opcodes;

/**
 * Tests the Application Reader.
 * 
 * This is mostly done by reading some test dex files and comparing the outgoing events from the reader
 * with logged events.
 * 
 * @author Julien Névo
 */
public class ApplicationReaderTest {

	private File file;
	
	/** The expected exception. */
	@Rule
	public ExpectedException exception = ExpectedException.none();

	
	/**
	 * Tests the throwing of IOException if the InputStream is null when creating the ApplicationReader. 
	 * @throws IOException thrown if the InputStream is null.
	 */
	@Test(expected = IOException.class)
    public void testIllegalConstructorArgument() throws IOException {
		new ApplicationReader(Opcodes.ASM4, (InputStream) null);
    }
    
	/**
	 * Tests the loading of a dex file and parsing of the header.
	 * @throws IOException
	 */
	@Test
	public void testApplicationReaderByteArray() throws IOException {
		byte[] bytes = TestUtil.readFile(TestUtil.PATH_AND_FILENAME_HELLO_WORLD_DEX);
		if (bytes != null) new ApplicationReader(Opcodes.ASM4, bytes);
		else throw new IOException("Test file not found");
	}
	
	/**
	 * Tests the loading of a dex file and parsing of the header, with a correct start and end.
	 */
	@Test
	public void testApplicationReaderByteArrayIntInt() throws IOException {
		byte[] bytes = null;
		bytes = TestUtil.readFile(TestUtil.PATH_AND_FILENAME_HELLO_WORLD_DEX);
		
		if (bytes != null) new ApplicationReader(Opcodes.ASM4, bytes, 0, bytes.length);
		else throw new IOException("Test file not found");
	}
	
	/**
	 * Tests the loading of a dex file and parsing of the header, with an incorrect end.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testApplicationReaderByteArrayIntIntFail() throws IOException {
		byte[] bytes = null;
		bytes = TestUtil.readFile(TestUtil.PATH_AND_FILENAME_HELLO_WORLD_DEX);
		
		if (bytes != null) new ApplicationReader(Opcodes.ASM4, bytes, 0, bytes.length - 1);
		else throw new IOException("Test file not found");
	}

	/**
	 * Tests the loading of a dex file and the parsing of the header from an Input Stream.
	 * @throws IOException
	 */
	@Test
	public void testApplicationReaderInputStream() throws IOException {
		file = new File(TestUtil.PATH_AND_FILENAME_HELLO_WORLD_DEX);
		InputStream is = new FileInputStream(file);

		new ApplicationReader(Opcodes.ASM4, is);
	}

	/**
	 * Tests the loading of a dex file and the parsing of the header from a filename String.
	 * @throws IOException
	 */
	@Test
	public void testApplicationReaderString() throws IOException {
		InputStream is = new FileInputStream(TestUtil.PATH_AND_FILENAME_HELLO_WORLD_DEX);
		new ApplicationReader(Opcodes.ASM4, is);
	}

	/**
	 * Tests the Application Reader by sending it a "hello world" application, and checking every
	 * message from the Reader. 
	 * @throws IOException
	 */
	@Test
	public void testApplicationReaderHelloWorld() throws IOException {
		testApplicationReader(new LogListHelloWorld());
	}
	
	/**
	 * Tests the Application Reader by sending it the "Annotation Tests" application, and checking every
	 * message from the Reader. 
	 * @throws IOException
	 */
	@Test
	public void testApplicationReaderAnnotationTests() throws IOException {
		testApplicationReader(new LogListAnnotationTests());
	}
	
	/**
	 * Tests the Application Reader by sending it the "Generated Dex All Instructions" application, and checking every
	 * message from the Reader. It has been generated by the Java file GenerateDexAllInstructions.
	 * @throws IOException
	 */
	@Test
	public void testApplicationReaderAllInstructions() throws IOException {
		testApplicationReader(new LogListAllInstructions());
	}
	
	/**
	 * Tests the Application Reader by sending it the "ExceptionTests" application, and checking every
	 * message from the Reader. It has been generated by the Java file GenerateDexAllInstructions.
	 * @throws IOException
	 */
	@Test
	public void testApplicationReaderExceptions() throws IOException {
		testApplicationReader(new LogListExceptions());
	}
	
	/**
	 * Tests the Application Reader by checking every messages from it. The expected messages and
	 * source dex file are given by the given LogList.
	 * @param logList the LogList containing the messages to compare with the dex file is points to.
	 * @throws IOException
	 */
	private void testApplicationReader(LogList logList) throws IOException {
		Logger logger = new Logger(logList);
		InputStream is = new FileInputStream(logList.getDexFile());
		ApplicationTestVisitor av = new ApplicationTestVisitor(logger);
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, is);
		ar.accept(av, logList.getClassesToParse(), 0);
		
		logger.logEnd();
	}
	


}
