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

import java.io.IOException;

import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.tree.ApplicationNode;

/**
 * Performance tests for bytecode generation.
 * <br/><br/>
 * The first tests mimic the ASM performance test with the same (as "same" as possible) instructions.
 * However, note that ASM only needs to generate a class, whereas AsmDex needs to generate an Application around.
 * 
 * @author Julien Névo, based on the ASM framework.
 */
public class GenPerfTest {

	/** Number of times we do the tests. */
	final static int NB_TESTS = 5;
	
	// Various iterations according to the duration of the tests.
	final static int NB_ITERATIONS_HIGH = 100000;
	final static int NB_ITERATIONS_NORMAL = 10000;
	final static int NB_ITERATIONS_SMALL = 1000;
	final static int NB_ITERATIONS_SMALLER = 100;
	
	/** Filename to test when using both the Reader and Writer. */
	private static final String testFilename = TestUtil.PATH_AND_FILENAME_API_DEMOS_DEX;
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		
		asmDexTestReaderOnly(50);
        //asmDexTestWriterOnly(1);
        asmDexTestWriterOnly(50);
        asmDexTestReaderWriter();
        asmDexTestReaderWriterTree();
        asmDexTestReaderWriterShortCut();
		//asmDexTestReaderWriterLoop(1);
		asmDexTestReaderWriterLoop(50);
		//asmDexTestReaderWriterLoop(100);
	}
	
	
	// -------------------------------------------------------------
	// Test main loops
	// -------------------------------------------------------------
	
	/**
	 * Performance test that runs the Reader with a generated Application. Contrary to ASM, Classes
	 * are wrapped into an Application, which means there *will* be an overhead. Increasing the
	 * number of Classes the Application possesses will reduce it, as most Application won't contain
	 * just a few Classes.
	 * @param nbClasses number of Classes to generated inside the Application.
	 */
	public static void asmDexTestReaderOnly(int nbClasses) {
		
		// Gets the bytecode of the program to test.
		byte[] bytes = generateLittleApplication(nbClasses);
		
		int nbIterations = NB_ITERATIONS_HIGH;
		for (int nbTests = 0; nbTests < NB_TESTS; nbTests++) {
	        long t = System.currentTimeMillis();
	        for (int i = 0; i < nbIterations; i++) {
	        	ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, bytes);
	        	ApplicationVisitor av = new ApplicationVisitor(Opcodes.ASM4){};
	        	ar.accept(av, 0);
	        }
	        
	        t = System.currentTimeMillis() - t;
	        System.out.println("AsmDex Reader Only generation time: " + ((float) t) / nbIterations / nbClasses
	                + " ms/class");
		}
    }
	
	/**
	 * Performance test that calculates the time taken by the generation an Application with the
	 * desired number of Classes, in order to reduce/weigh the overhead of the Application.
	 * @param nbClasses number of Classes to generated inside the Application.
	 */
	public static void asmDexTestWriterOnly(int nbClasses) {
		int nbIterations = NB_ITERATIONS_NORMAL;
		for (int nbTests = 0; nbTests < NB_TESTS; nbTests++) {
	        long t = System.currentTimeMillis();
	        for (int i = 0; i < nbIterations; i++) {
	            generateLittleApplication(nbClasses);
	        }
	        t = System.currentTimeMillis() - t;
	        System.out.println("AsmDex Writer Only generation time: " + ((float) t) / nbIterations / nbClasses
	                + " ms/class");
		}
    }
	
	/**
	 * Performance test that calculates the time taken by reading/writing a dex file.
	 * @throws IOException
	 */
	public static void asmDexTestReaderWriter() throws IOException {
		int nbIterations = NB_ITERATIONS_SMALLER;
		for (int nbTests = 0; nbTests < NB_TESTS; nbTests++) {
	        long t = System.currentTimeMillis();
	        for (int i = 0; i < nbIterations; i++) {
	        	asmDexTestReaderWriterPerform();
	        }
	        t = System.currentTimeMillis() - t;
	        System.out.println("AsmDex Reader/Writer generation time: " + ((float) t) / nbIterations
	                + " ms/application");
		}
    }
	
	/**
	 * Performance test that calculates the time taken by reading/writing a dex file using the
	 * Tree API.
	 * @throws IOException
	 */
	public static void asmDexTestReaderWriterTree() throws IOException {
		int nbIterations = NB_ITERATIONS_SMALLER;
		for (int nbTests = 0; nbTests < NB_TESTS; nbTests++) {
	        long t = System.currentTimeMillis();
	        for (int i = 0; i < nbIterations; i++) {
	        	asmDexTestReaderWriterTreePerform();
	        }
	        t = System.currentTimeMillis() - t;
	        System.out.println("AsmDex Reader/Writer Tree generation time: " + ((float) t) / nbIterations
	                + " ms/application");
		}
    }
	
	/**
	 * Performance test that calculates the time taken by reading/writing a dex file using the
	 * "Constant Pool" optimization.
	 * @throws IOException
	 */
	public static void asmDexTestReaderWriterShortCut() throws IOException {
		int nbIterations = NB_ITERATIONS_SMALLER;
		for (int nbTests = 0; nbTests < NB_TESTS; nbTests++) {
	        long t = System.currentTimeMillis();
	        for (int i = 0; i < nbIterations; i++) {
	        	asmDexTestReaderWriterShortCutPerform();
	        }
	        t = System.currentTimeMillis() - t;
	        System.out.println("AsmDex Reader/Writer with ShortCut generation time: " + ((float) t) / nbIterations
	                + " ms/application");
		}
    }
	
	/**
	 * Performance test that calculates the time taken by reading/writing a generated dex file,
	 * including the desired number of Classes in order to reduce/weight the overhead induced by
	 * the Application layer.
	 * @param nbClasses number of Classes to generated inside the Application.
	 * @throws IOException
	 */
	public static void asmDexTestReaderWriterLoop(int nbClasses) throws IOException {
		int nbIterations = NB_ITERATIONS_NORMAL;
		for (int nbTests = 0; nbTests < NB_TESTS; nbTests++) {
			// Gets the bytecode of the program to test. 
			byte[] bytes = generateLittleApplication(nbClasses);
			
			// Inserts it into a loop of Reader/Writer.
	        long t = System.currentTimeMillis();
	        for (int i = 0; i < nbIterations; i++) {
	        	ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, bytes);
	        	ApplicationWriter aw = new ApplicationWriter();
	        	ar.accept(aw, 0);
	        	bytes = aw.toByteArray();
	        }
	        t = System.currentTimeMillis() - t;
	        
	        System.out.println("AsmDex Reader/Writer in Loop with " + nbClasses +
	        		" classe(s) generation time: " + ((float) t) / nbIterations / nbClasses
	                + " ms/class");
		}
    }
	
	

	// -------------------------------------------------------------
	// Utility methods
	// -------------------------------------------------------------
	
	/**
	 * Generate a little Application containing as much Classes as desired. This code wouldn't work
	 * on hardware. It just mimics the ASM generated instructions for the same test. It consists in
	 * one or more Classes containing two little methods.
	 * @return an array of bytes representing the generated dex file.
	 */
	private static byte[] generateLittleApplication(int nbClasses) {
        ApplicationWriter aw = new ApplicationWriter();
        
        aw.visit();
        
        for (int i = 0; i < nbClasses; i++) {
        
		    ClassVisitor cv = aw.visitClass(Opcodes.ACC_PUBLIC, "HelloWorld" + i, null, "Ljava/lang/Object;", null);
		    cv.visit(0, Opcodes.ACC_PUBLIC, "HelloWorld" + i, null, "Ljava/lang/Object;", null);
		    cv.visitSource("HelloWorld.java", null);
		    
		    MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "V", null, null);
		    mv.visitVarInsn(Opcodes.INSN_MOVE, 0, 0);
		    mv.visitMethodInsn(Opcodes.INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] {} );
		    mv.visitInsn(Opcodes.INSN_RETURN_VOID);
		    mv.visitMaxs(0, 0);
		    mv.visitEnd();
		
		    mv = cv.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "V[Ljava/lang/String;",
		            null, null);
		    mv.visitFieldInsn(Opcodes.INSN_SGET, "Ljava/lang/System;", "out", "Ljava/io/PrintStream;", 0, 0);
		    mv.visitStringInsn(Opcodes.INSN_CONST_STRING, 0, "Hello world!");
		    mv.visitMethodInsn(Opcodes.INSN_INVOKE_VIRTUAL, "Ljava/io/PrintStream;", "println", "VLjava/lang/String;", new int[] {});
		    mv.visitInsn(Opcodes.INSN_RETURN_VOID);
		    mv.visitMaxs(0, 0);
		    mv.visitEnd();
		    
		    cv.visitEnd();
        }

        aw.visitEnd();
        return aw.toByteArray();
    }
	
	/**
	 * Performs the Reader/Writer chain from a dex file.
	 * @return the generated dex file as an array.
	 * @throws IOException
	 */
	private static byte[] asmDexTestReaderWriterPerform() throws IOException {
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, testFilename);
		ApplicationWriter aw = new ApplicationWriter();
		ar.accept(aw, 0);
		return aw.toByteArray();
	}
	
	/**
	 * Performs the Reader/Writer chain from a dex file and the Tree API.
	 * @return the generated dex file as an array.
	 * @throws IOException
	 */
	private static byte[] asmDexTestReaderWriterTreePerform() throws IOException {
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, testFilename);
		ApplicationNode an = new ApplicationNode(Opcodes.ASM4);
		ar.accept(an, 0);
		
		ApplicationWriter aw = new ApplicationWriter();
		an.accept(aw);
		return aw.toByteArray();
	}
	
	/**
	 * Performs the Reader/Writer chain from a dex file and the "Constant Pool" optimization.
	 * @return the generated dex file as an array.
	 * @throws IOException
	 */
	private static byte[] asmDexTestReaderWriterShortCutPerform() throws IOException {
		ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, testFilename);
		ApplicationWriter aw = new ApplicationWriter(ar);
		ar.accept(aw, 0);
		return aw.toByteArray();
	}
}
