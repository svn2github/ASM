/***
 * ASM tests
 * Copyright (c) 2002-2005 France Telecom
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
package org.objectweb.asm;

import java.lang.reflect.Method;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * Type unit tests.
 * 
 * @author Eric Bruneton
 */
public class TypeUnitTest extends TestCase implements Opcodes {

    public void testConstants() {
        assertEquals(Type.getType(Integer.TYPE), Type.INT_TYPE);
        assertEquals(Type.getType(Void.TYPE), Type.VOID_TYPE);
        assertEquals(Type.getType(Boolean.TYPE), Type.BOOLEAN_TYPE);
        assertEquals(Type.getType(Byte.TYPE), Type.BYTE_TYPE);
        assertEquals(Type.getType(Character.TYPE), Type.CHAR_TYPE);
        assertEquals(Type.getType(Short.TYPE), Type.SHORT_TYPE);
        assertEquals(Type.getType(Double.TYPE), Type.DOUBLE_TYPE);
        assertEquals(Type.getType(Float.TYPE), Type.FLOAT_TYPE);
        assertEquals(Type.getType(Long.TYPE), Type.LONG_TYPE);
    }

    public void testInternalName() {
        String s1 = Type.getType(TypeUnitTest.class).getInternalName();
        String s2 = Type.getInternalName(TypeUnitTest.class);
        assertEquals(s1, s2);
    }

    public void testMethodDescriptor() {
        for (int i = 0; i < Arrays.class.getMethods().length; ++i) {
            Method m = Arrays.class.getMethods()[i];
            Type[] args = Type.getArgumentTypes(m);
            Type r = Type.getReturnType(m);
            String d1 = Type.getMethodDescriptor(r, args);
            String d2 = Type.getMethodDescriptor(m);
            assertEquals(d1, d2);
        }
    }

    public void testGetOpcode() {
        Type object = Type.getType("Ljava/lang/Object;");
        assertEquals(Type.BOOLEAN_TYPE.getOpcode(IALOAD), BALOAD);
        assertEquals(Type.BYTE_TYPE.getOpcode(IALOAD), BALOAD);
        assertEquals(Type.CHAR_TYPE.getOpcode(IALOAD), CALOAD);
        assertEquals(Type.SHORT_TYPE.getOpcode(IALOAD), SALOAD);
        assertEquals(Type.INT_TYPE.getOpcode(IALOAD), IALOAD);
        assertEquals(Type.FLOAT_TYPE.getOpcode(IALOAD), FALOAD);
        assertEquals(Type.LONG_TYPE.getOpcode(IALOAD), LALOAD);
        assertEquals(Type.DOUBLE_TYPE.getOpcode(IALOAD), DALOAD);
        assertEquals(object.getOpcode(IALOAD), AALOAD);
        assertEquals(Type.BOOLEAN_TYPE.getOpcode(IADD), IADD);
        assertEquals(Type.BYTE_TYPE.getOpcode(IADD), IADD);
        assertEquals(Type.CHAR_TYPE.getOpcode(IADD), IADD);
        assertEquals(Type.SHORT_TYPE.getOpcode(IADD), IADD);
        assertEquals(Type.INT_TYPE.getOpcode(IADD), IADD);
        assertEquals(Type.FLOAT_TYPE.getOpcode(IADD), FADD);
        assertEquals(Type.LONG_TYPE.getOpcode(IADD), LADD);
        assertEquals(Type.DOUBLE_TYPE.getOpcode(IADD), DADD);
    }

    public void testHashcode() {
        Type.getType("Ljava/lang/Object;").hashCode();
    }
}
