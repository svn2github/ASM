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

import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

/**
 * Adapter Test that adds a Switch case in myMethod of the FirstActivity.
 * In order to simply the test, we choose the meeting with INSN_RETURN_VOID
 * to replace it with our instructions.
 * 
 * We must also change the visitMax value.
 * 
 * @author Julien Névo
 */
public class MethodAdapterAddSwitchCase extends MethodVisitor {

	/**
	 * Instantiates a new method adapter add switch case.
	 *
	 * @param mv the mv
	 */
	public MethodAdapterAddSwitchCase(MethodVisitor mv) {
		super(Opcodes.ASM4, mv);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		super.visitMaxs(2, 0);
	}
	
	@Override
	public void visitInsn(int opcode) {
		if (opcode != Opcodes.INSN_RETURN_VOID) {
			super.visitInsn(opcode);
		} else {
			
			// Generates all the instructions of a little switch/case :
//			int a = 0;
//	    	switch (a) {
//	    	case 0 : a = 56;
//	    	break;
//	    	case 1 : a = 57;
//	    	break;
//	    	case 2 : a = 58;
//	    	break;
//	    	default:
//	    		a = 59;
//	    		break;
//	    	}
			
			
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(15, l0);
			mv.visitVarInsn(Opcodes.INSN_CONST_4, 0, 0);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(16, l1);
			Label l2 = new Label();
			Label l3 = new Label();
			Label l4 = new Label();
			Label l5 = new Label();
			mv.visitTableSwitchInsn(0, 0, 2, l2, new Label[] { l3, l4, l5 });
			mv.visitLabel(l2);
			mv.visitLineNumber(24, l2);
			mv.visitVarInsn(Opcodes.INSN_CONST_16, 0, 59);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(27, l6);
			mv.visitInsn(Opcodes.INSN_RETURN_VOID);
			mv.visitLabel(l3);
			mv.visitLineNumber(17, l3);
			mv.visitVarInsn(Opcodes.INSN_CONST_16, 0, 56);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(18, l7);
			mv.visitJumpInsn(Opcodes.INSN_GOTO, l6, 0, 0);
			mv.visitLabel(l4);
			mv.visitLineNumber(19, l4);
			mv.visitVarInsn(Opcodes.INSN_CONST_16, 0, 57);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(20, l8);
			mv.visitJumpInsn(Opcodes.INSN_GOTO, l6, 0, 0);
			mv.visitLabel(l5);
			mv.visitLineNumber(21, l5);
			mv.visitVarInsn(Opcodes.INSN_CONST_16, 0, 58);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(22, l9);
			mv.visitJumpInsn(Opcodes.INSN_GOTO, l6, 0, 0);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(16, l10);
			mv.visitLocalVariable("a", "I", null, l1, null, null, 0);
			
			
			
		}
	}
	
//	@Override
//	public void visitVarInsn(int opcode, int destinationRegister, int var) {
//		
//		// Creates the original instruction.
//		super.visitVarInsn(opcode, destinationRegister, var);
//		
//		// Duplicates the CONST_4 instruction.
//		//if (opcode == Opcodes.INSN_CONST_4) {
//			//mv.visitVarInsn(opcode, destinationRegister, var);
//		//}
//		
//	}
}
