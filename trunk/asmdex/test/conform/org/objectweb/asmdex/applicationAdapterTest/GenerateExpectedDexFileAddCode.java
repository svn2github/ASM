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
package org.objectweb.asmdex.applicationAdapterTest;

import org.ow2.asmdex.*;
import org.ow2.asmdex.structureCommon.*;

/**
 * Generates an "expected" dex file that only contains one activity (FirstActivity),
 * and three methods : the constructor, onCreate and myMethod which only contains
 * a RETURN_VOID instruction.
 * 
 * It is useful for the "Add Code" Adapter tests, that will add some instructions at the end
 * of "myMethod". This file should match the result of the Adapter.
 * 
 * @author Julien Névo
 */
public class GenerateExpectedDexFileAddCode implements Opcodes {

	/**
	 * Generate the dex file
	 * @return byte code
	 */
	public static byte[] generate() {
	
		ApplicationWriter aw = new ApplicationWriter();
		aw.visit();
		generateFirstActivity(aw);
		aw.visitEnd();
	
		return aw.toByteArray();
	}

	private static void generateFirstActivity(ApplicationWriter aw) {
		ClassVisitor cv;
		MethodVisitor mv;
	
		cv = aw.visitClass(ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null);
		cv.visit(0, ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null);
		
		cv.visitSource("FirstActivity.java", null);
		{
			mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(6, l0);
			mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Landroid/app/Activity;", "<init>", "V", new int[] { 0 });
			mv.visitInsn(INSN_RETURN_VOID);
			mv.visitMaxs(1, 0);
			mv.visitEnd();
		}
		{
			mv = cv.visitMethod(ACC_PUBLIC, "onCreate", "VLandroid/os/Bundle;", null, null);
			mv.visitCode();
			mv.visitParameters(new String[] { "savedInstanceState" });
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(10, l0);
			mv.visitMethodInsn(INSN_INVOKE_SUPER, "Landroid/app/Activity;", "onCreate", "VLandroid/os/Bundle;", new int[] { 1, 2 });
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(11, l1);
			mv.visitVarInsn(INSN_CONST_HIGH16, 0, 2130903040);
			mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lft/nevo/FirstActivity;", "setContentView", "VI", new int[] { 1, 0 });
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(14, l2);
			mv.visitInsn(INSN_RETURN_VOID);
			
			// Added code by the Adapter.
			{
				mv.visitIntInsn(Opcodes.INSN_THROW, 0);
				mv.visitIntInsn(Opcodes.INSN_THROW, 1);
				mv.visitIntInsn(Opcodes.INSN_THROW, 2);
				mv.visitIntInsn(Opcodes.INSN_THROW, 3);
			}
			
			mv.visitMaxs(3, 0);
			mv.visitEnd();
		}
		
		{
			mv = cv.visitMethod(ACC_PUBLIC, "myMethod", "V", null, null);
			mv.visitCode();
			mv.visitInsn(INSN_RETURN_VOID);
			mv.visitMaxs(1, 0);
			mv.visitEnd();
		}
		cv.visitEnd();
	}

}

