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
package org.objectweb.asmdex.tree;

import static org.junit.Assert.*;

import org.junit.Test;
import org.objectweb.asmdex.Opcodes;
import org.objectweb.asmdex.tree.AbstractInsnNode;
import org.objectweb.asmdex.tree.ArrayLengthInsnNode;

/**
 * Test Unit of an ArrayLengthInsnNode.
 * 
 * @author Julien Névo
 */
public class ArrayLengthInsnNodeTest {

	/**
	 * Test array length insn node.
	 */
	@Test
	public void testArrayLengthInsnNode() {
		int destinationRegister = 1;
		int arrayReferenceBearing = 255;
		ArrayLengthInsnNode n = new ArrayLengthInsnNode(destinationRegister, arrayReferenceBearing);
		
		assertEquals(Opcodes.INSN_ARRAY_LENGTH, n.getOpcode());
		assertEquals(destinationRegister, n.destinationRegister);
		assertEquals(arrayReferenceBearing, n.arrayReferenceBearing);
		assertEquals(AbstractInsnNode.ARRAY_LENGTH_INSN, n.getType());
	}

}
