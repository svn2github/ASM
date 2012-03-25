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
 * Adapter Test that adds code at the end of the onCreate method of the FirstActivity
 * so that a GOTO encoded for a 8-bit jump is resized into 16-bit.
 * Covers double backwards and a double forwards jumps.
 * Expects a double-resize (Label1 - GOTO Label2 - GOTO Label2 - NOPS * x - Label2 - GOTO Label1 - GOTO Label2).
 * 
 * @author Julien Névo
 */
public class MethodAdapterResizeGoto8BitsDoubleRefs extends MethodVisitor {

	/** The number of NOPS to insert. */
	public final static int NB_NOPS = 0x7f;
	
	private int nbGotoFound = 0;
	
	/**
	 * Instantiates a new method adapter resize goto8 bits double refs.
	 *
	 * @param mv the mv
	 */
	public MethodAdapterResizeGoto8BitsDoubleRefs(MethodVisitor mv) {
		super(Opcodes.ASM4, mv);
	}

	@Override
	public void visitJumpInsn(int opcode, Label label, int registerA, int registerB) {
		
		// We use the second GOTO instruction to detect when the NOPs must be inserted.
		if (nbGotoFound == 1 && (opcode == Opcodes.INSN_GOTO)) {
			// Inserts the GOTO right now.
			super.visitJumpInsn(opcode, label, registerA, registerB);
			
			for (int i = 0; i < NB_NOPS; i++) {
				mv.visitInsn(Opcodes.INSN_NOP);
			}
		} else {
			super.visitJumpInsn(opcode, label, registerA, registerB);
		}
		nbGotoFound++;
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
