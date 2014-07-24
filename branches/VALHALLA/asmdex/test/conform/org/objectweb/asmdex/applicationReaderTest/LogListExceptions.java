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
package org.objectweb.asmdex.applicationReaderTest;

import org.objectweb.asmdex.TestUtil;
import org.objectweb.asmdex.logging.*;
import org.ow2.asmdex.structureCommon.Label;

/**
 * Log List of an application that tests the Exceptions. Only the most important classes are parsed,
 * all the "boiler plate" (R, R$layout etc.) are skipped.
 *  
 * @author Julien Névo
 */
public class LogListExceptions implements LogList {

	@Override
	public String[] getClassesToParse() {
		return new String[] {
				"Lft/nevo/FirstActivity;"
		};
	}

	@Override
	public String getDexFile() {
		return TestUtil.PATH_AND_FILENAME_EXCEPTION_TESTS_DEX;
	}

	@Override
	public LogElement[] getLogElements() {
		Label l0 = new Label();
		l0.setOffset(0);
		
		Label l0MyMethod1 = new Label();
		l0MyMethod1.setOffset(0);
		Label l1MyMethod1 = new Label();
		l1MyMethod1.setOffset(2);
		Label l2MyMethod1 = new Label();
		l2MyMethod1.setOffset(6);
		
		Label l0MyMethod2 = new Label();
		l0MyMethod2.setOffset(0);
		Label l1MyMethod2 = new Label();
		l1MyMethod2.setOffset(2);
		Label l2MyMethod2 = new Label();
		l2MyMethod2.setOffset(6);
		Label l3MyMethod2 = new Label();
		l3MyMethod2.setOffset(10);
		
		Label l0MethodOnCreate = new Label();
		l0MethodOnCreate.setOffset(0);
		Label l1MethodOnCreate = new Label();
		l1MethodOnCreate.setOffset(6);
		Label l2MethodOnCreate = new Label();
		l2MethodOnCreate.setOffset(16);
		
		return new LogElement[] {
			new LogElementApplicationVisit(),
			new LogElementApplicationVisitClass(ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null),
			new LogElementClassVisit(ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null),
			new LogElementClassVisitSource("FirstActivity.java", null),
			
			new LogElementClassVisitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null),
			new LogElementMethodVisitCode(),
			new LogElementMethodVisitMaxs(1, 0),
			new LogElementMethodVisitLabel(l0),
			new LogElementMethodVisitLineNumber(8, l0),
			new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Landroid/app/Activity;", "<init>", "V", new int[] { 0 }),
			new LogElementMethodVisitInsn(INSN_RETURN_VOID),
			new LogElementMethodVisitEnd(),
			
			new LogElementClassVisitMethod(ACC_PUBLIC, "myMethod1", "V", null, new String[] { "Ljava/io/IOException;" }),
			new LogElementMethodVisitCode(),
			new LogElementMethodVisitMaxs(2, 0),
			new LogElementMethodVisitLabel(l0MyMethod1),
			new LogElementMethodVisitLineNumber(17, l0MyMethod1),
			new LogElementMethodVisitVarInsn(INSN_CONST_4, 0, 5),
			new LogElementMethodVisitLabel(l1MyMethod1),
			new LogElementMethodVisitLineNumber(18, l1MyMethod1),
			new LogElementMethodVisitOperationInsn(INSN_ADD_INT_LIT8, 0, 0, 0, 1),
			new LogElementMethodVisitLabel(l2MyMethod1),
			new LogElementMethodVisitLineNumber(19, l2MyMethod1),
			new LogElementMethodVisitInsn(INSN_RETURN_VOID),
			new LogElementMethodVisitLocalVariableList("a", "I", null, l1MyMethod1, null, null, 0),
			new LogElementMethodVisitEnd(),
				
			// Element 26.
			new LogElementClassVisitMethod(ACC_PUBLIC, "myMethod2", "V", null, new String[] { "Ljava/io/IOException;", "Ljava/lang/IllegalArgumentException;" }),
			new LogElementMethodVisitCode(),
			new LogElementMethodVisitMaxs(2, 0),
			new LogElementMethodVisitLabel(l0),
			new LogElementMethodVisitLineNumber(22, l0MyMethod2),
			new LogElementMethodVisitVarInsn(INSN_CONST_4, 0, 5),
			new LogElementMethodVisitLabel(l1MyMethod2),
			new LogElementMethodVisitLineNumber(23, l1MyMethod2),
			new LogElementMethodVisitOperationInsn(INSN_ADD_INT_LIT8, 0, 0, 0, 1),
			new LogElementMethodVisitLabel(l2MyMethod2),
			new LogElementMethodVisitLineNumber(26, l2MyMethod2),
			new LogElementMethodVisitOperationInsn(INSN_ADD_INT_LIT8, 0, 0, 0, -1),
			new LogElementMethodVisitLabel(l3MyMethod2),
			new LogElementMethodVisitLineNumber(31, l3MyMethod2),
			new LogElementMethodVisitInsn(INSN_RETURN_VOID),
			new LogElementMethodVisitLocalVariableList("a", "I", null, l1MyMethod2, null, null, 0),
			new LogElementMethodVisitEnd(),
			
			// Element 43.
			new LogElementClassVisitMethod(ACC_PUBLIC, "onCreate", "VLandroid/os/Bundle;", null, null),
			new LogElementMethodVisitParameters(new String[] { "savedInstanceState" }),
			new LogElementMethodVisitCode(),
			new LogElementMethodVisitMaxs(3, 0),
			new LogElementMethodVisitLabel(l0MethodOnCreate),
			new LogElementMethodVisitLineNumber(12, l0MethodOnCreate),
			new LogElementMethodVisitMethodInsn(INSN_INVOKE_SUPER, "Landroid/app/Activity;", "onCreate", "VLandroid/os/Bundle;", new int[] { 1, 2 }),
			new LogElementMethodVisitLabel(l1MethodOnCreate),
			new LogElementMethodVisitLineNumber(13, l1MethodOnCreate),
			new LogElementMethodVisitVarInsn(INSN_CONST_HIGH16, 0, 2130903040),
			new LogElementMethodVisitMethodInsn(INSN_INVOKE_VIRTUAL, "Lft/nevo/FirstActivity;", "setContentView", "VI", new int[] { 1, 0 }),
			new LogElementMethodVisitLabel(l2MethodOnCreate),
			new LogElementMethodVisitLineNumber(14, l2MethodOnCreate),
			new LogElementMethodVisitInsn(INSN_RETURN_VOID),
			new LogElementMethodVisitEnd(),

			new LogElementClassVisitEnd(),
			
			new LogElementApplicationVisitEnd(),
		};
	}

}
