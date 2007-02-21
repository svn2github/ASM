/***
 * ASM Guide
 * Copyright (c) 2007 Eric Bruneton
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

package ch7.sec2;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.ISTORE;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.TraceMethodVisitor;

import util.AbstractTestCase;

/**
 * ASM Guide example test class.
 * 
 * @author Eric Bruneton
 */
public class BasicVerifierAdapterTest extends AbstractTestCase {

  public void test() {
    TraceMethodVisitor tmv = new TraceMethodVisitor(null);
    MethodVisitor mv = new BasicVerifierAdapter("C", ACC_PUBLIC, "m",
        "(I)I", tmv);
    mv.visitCode();
    mv.visitVarInsn(ILOAD, 1);
    mv.visitInsn(ICONST_0);
    mv.visitInsn(IADD);
    mv.visitInsn(IRETURN);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
  }

  public void testError() {
    TraceMethodVisitor tmv = new TraceMethodVisitor(null);
    MethodVisitor mv = new BasicVerifierAdapter("C", ACC_PUBLIC, "m",
        "(I)LC;", tmv);
    mv.visitCode();
    mv.visitVarInsn(ILOAD, 1);
    mv.visitVarInsn(ISTORE, 1);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitInsn(ARETURN);
    mv.visitMaxs(1, 2);
    try {
      mv.visitEnd();
      fail();
    } catch (RuntimeException e) {
    }
  }

  @Override
  protected ClassVisitor getClassAdapter(final ClassVisitor cv) {
    return new ClassAdapter(cv) {
      private String owner;

      @Override
      public void visit(int version, int access, String name,
          String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName,
            interfaces);
        owner = name;
      }

      @Override
      public MethodVisitor visitMethod(int access, String name,
          String desc, String signature, String[] exceptions) {
        return new BasicVerifierAdapter(owner, access, name, desc, cv
            .visitMethod(access, name, desc, signature, exceptions));
      }
    };
  }
}
