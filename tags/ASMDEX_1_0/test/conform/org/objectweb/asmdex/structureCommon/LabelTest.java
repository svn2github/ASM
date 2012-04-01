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
package org.objectweb.asmdex.structureCommon;

import static org.junit.Assert.*;

import org.junit.Test;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.instruction.Instruction;
import org.ow2.asmdex.instruction.InstructionFormat10X;
import org.ow2.asmdex.structureCommon.Label;

/**
 * Label unit tests.
 * 
 * @author Julien Névo
 */
public class LabelTest {

	/**
	 * Tests that the offset of a Label put inside a method as a the first instruction is 0. 
	 */
	@Test
	public void testGetOffset() {
		Label l = new Label();
		ApplicationWriter aw = new ApplicationWriter();
		ClassVisitor cv = aw.visitClass(Opcodes.ACC_PUBLIC, "TestClass", null, null, null);
        MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "testMethod", "V", null, null);
        mv.visitCode();
        mv.visitLabel(l);
        assertEquals(0, l.getOffset());
	}
	
	/**
	 * Tests that the offset of a Label put inside a method after an instruction.
	 */
	@Test
	public void testGetOffsetAfterOneInstruction() {
		Label l = new Label();
		ApplicationWriter aw = new ApplicationWriter();
		ClassVisitor cv = aw.visitClass(Opcodes.ACC_PUBLIC, "TestClass", null, null, null);
        MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "testMethod", "V", null, null);
        mv.visitCode();
        mv.visitInsn(Opcodes.INSN_RETURN_VOID);
        mv.visitLabel(l);
        assertEquals(2, l.getOffset());
	}
	
	/**
	 * Tests that the setting of an offset.
	 */
	@Test
	public void testSetOffset() {
		Label l = new Label();
		int offset = 142;
		l.setOffset(offset);
        assertEquals(offset, l.getOffset());
	}
	
	/**
	 * Tests that the getting of a line of a newly created Label.
	 */
	@Test
	public void testGetLine() {
		Label l = new Label();
        assertEquals(0, l.getLine());
	}
	
	/**
	 * Tests that the setting of a line of a newly created Label.
	 */
	@Test
	public void testSetLine() {
		Label l = new Label();
		int line = 189;
		l.setLine(line);
        assertEquals(line, l.getLine());
	}
	
	/**
	 * Tests that the emptiness of the referring Instructions of a newly created Label.
	 */
	@Test
	public void testReferringInstructionsEmpty() {
		Label l = new Label();
        assertEquals(0, l.getReferringInstructions().size());
	}
	
	/**
	 * Tests that the size after adding two referring Instructions of a newly created Label.
	 */
	@Test
	public void testAddReferringInstructions() {
		Label l = new Label();
		Instruction i1 = new InstructionFormat10X(Opcodes.INSN_NOP);
		Instruction i2 = new InstructionFormat10X(Opcodes.INSN_RETURN_VOID);
		l.addReferringInstruction(i1);
		l.addReferringInstruction(i2);
        assertEquals(2, l.getReferringInstructions().size());
	}
	
	/**
	 * Tests that the offset of a Label can't be retrieved if it hasn't been visited. 
	 */
	@Test(expected = IllegalStateException.class)
	public void testIllegalGetOffsetState() {
		Label l = new Label();
		l.getOffset();
	}
	
}
