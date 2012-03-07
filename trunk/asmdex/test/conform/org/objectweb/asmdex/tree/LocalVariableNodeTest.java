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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.ow2.asmdex.structureCommon.Label;
import org.ow2.asmdex.tree.LabelNode;
import org.ow2.asmdex.tree.LocalVariableNode;

/**
 * Test Unit of a LocalVariableNode.
 * 
 * @author Julien Névo
 */
public class LocalVariableNodeTest {

	/**
	 * Test local variable node string string string label node label node int.
	 */
	@Test
	public void testLocalVariableNodeStringStringStringLabelNodeLabelNodeInt() {
		String name = "name";
		String desc = "desc";
		String signature = "signature";
		Label labelStart = new Label();
		labelStart.setOffset(10);
		Label labelEnd = new Label();
		labelEnd.setOffset(20);
		
		LabelNode start = new LabelNode(labelStart);
		LabelNode end = new LabelNode(labelEnd);
		
		List<LabelNode> ends = new ArrayList<LabelNode>(1);
		ends.add(end);
		
		int index = 123;
		
		LocalVariableNode n = new LocalVariableNode(name, desc, signature, start, end, index);
		
		assertEquals(name, n.name);
		assertEquals(desc, n.desc);
		assertEquals(signature, n.signature);
		assertEquals(index, n.index);
		assertEquals(start, n.start);
		assertEquals(ends, n.ends);
		assertNull(n.restarts);
	}

	/**
	 * Test local variable node string string string label node list of label node list of label node int.
	 */
	@Test
	public void testLocalVariableNodeStringStringStringLabelNodeListOfLabelNodeListOfLabelNodeInt() {
		String name = "name";
		String desc = "desc";
		String signature = "signature";
		Label labelStart = new Label();
		labelStart.setOffset(10);
		Label labelEnd = new Label();
		labelEnd.setOffset(20);
		Label labelRestart = new Label();
		labelRestart.setOffset(30);
		
		LabelNode start = new LabelNode(labelStart);
		LabelNode end = new LabelNode(labelEnd);
		LabelNode restart = new LabelNode(labelRestart);
		
		List<LabelNode> ends = new ArrayList<LabelNode>(1);
		ends.add(end);
		List<LabelNode> restarts = new ArrayList<LabelNode>(1);
		restarts.add(restart);
		
		int index = 123;
		
		LocalVariableNode n = new LocalVariableNode(name, desc, signature, start, ends, restarts, index);
		
		assertEquals(name, n.name);
		assertEquals(desc, n.desc);
		assertEquals(signature, n.signature);
		assertEquals(index, n.index);
		assertEquals(start, n.start);
		assertEquals(restarts, n.restarts);
		assertEquals(ends, n.ends);
		
	}

}
