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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.tree.ClassNode;
import org.ow2.asmdex.tree.FieldNode;
import org.ow2.asmdex.tree.InnerClassNode;
import org.ow2.asmdex.tree.MethodNode;

/**
 * Test Unit of a ClassNode.
 * 
 * @author Julien Névo
 */
public class ClassNodeTest {

	private ClassNode cn = new ClassNode(Opcodes.ASM4);
	
	/**
	 * Test visit.
	 */
	@Test
	public void testVisit() {
		// Does nothing.
		cn.visit(0, 0, "name", null, "superName", null);
		assertTrue(true);
	}

	/**
	 * Test visit field.
	 */
	@Test
	public void testVisitField() {
		int access = Opcodes.ACC_PUBLIC;
		String name = "name";
		String desc = "I";
		String[] signature = new String[] { "sig1", "sig2" };
		Object value = 120;
		cn.visitField(access, name, desc, signature, value);
		FieldNode fn = cn.fields.get(0);
		assertEquals(access, fn.access);
		assertEquals(name, fn.name);
		assertEquals(desc, fn.desc);
		assertArrayEquals(signature, fn.signature);
		assertEquals(value, fn.value);
	}

	/**
	 * Test visit inner class.
	 */
	@Test
	public void testVisitInnerClass() {
		String name = "name";
		String outerName = "outerName";
		String innerName = "innerName";
		int access = Opcodes.ACC_PUBLIC;
		cn.visitInnerClass(name, outerName, innerName, access);
		
		InnerClassNode icn = cn.innerClasses.get(0);
		assertEquals(name, icn.name);
		assertEquals(outerName, icn.outerName);
		assertEquals(innerName, icn.innerName);
		assertEquals(access, icn.access);
	}

	/**
	 * Test visit method.
	 */
	@Test
	public void testVisitMethod() {
		int access = Opcodes.ACC_PUBLIC;
		String name = "name";
		String desc = "I";
		String[] signature = new String[] { "sig1", "sig2" };
		String[] exceptions = new String[] { "exc1", "exc2" };
		cn.visitMethod(access, name, desc, signature, exceptions);
		
		MethodNode mn = cn.methods.get(0);
		assertEquals(access, mn.access);
		assertEquals(name, mn.name);
		assertEquals(desc, mn.desc);
		assertArrayEquals(signature, mn.signature);
		assertArrayEquals(exceptions, mn.exceptions.toArray());
	}

	/**
	 * Test visit outer class.
	 */
	@Test
	public void testVisitOuterClass() {
		String owner = "owner";
		String name = "name";
		String desc = "I";
		cn.visitOuterClass(owner, name, desc);
		
		assertEquals(owner, cn.outerClass);
		assertEquals(name, cn.outerMethod);
		assertEquals(desc, cn.outerMethodDesc);
	}

	/**
	 * Test visit source.
	 */
	@Test
	public void testVisitSource() {
		String source = "sourceFile";
		cn.visitSource(source, null);
		
		assertEquals(source, cn.sourceFile);
	}

}
