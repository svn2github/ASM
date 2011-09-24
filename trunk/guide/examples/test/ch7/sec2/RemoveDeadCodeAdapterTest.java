/***
 * ASM Guide
 * Copyright (c) 2007 Eric Bruneton, 2011 Google
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
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.IFLT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import util.AbstractTestCase;

/**
 * ASM Guide example test class.
 * 
 * @author Eric Bruneton
 */
public class RemoveDeadCodeAdapterTest extends AbstractTestCase {

  public void test() {
    TraceMethodVisitor tmv = new TraceMethodVisitor(null, new Textifier());
    RemoveDeadCodeAdapter mv = new RemoveDeadCodeAdapter("pkg/Bean",
        ACC_PUBLIC, "checkAndSetF", "(I)V", tmv);
    mv.visitCode();
    mv.visitVarInsn(ILOAD, 1);
    Label label = new Label();
    mv.visitJumpInsn(IFLT, label);
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ILOAD, 1);
    mv.visitFieldInsn(PUTFIELD, "pkg/Bean", "f", "I");
    Label end = new Label();
    mv.visitInsn(RETURN);
    mv.visitLabel(label);
    mv.visitFrame(F_SAME, 0, null, 0, null);
    mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL,
        "java/lang/IllegalArgumentException", "<init>", "()V");
    mv.visitInsn(ATHROW);
    mv.visitLabel(end);
    mv.visitFrame(F_SAME, 0, null, 0, null);
    mv.visitInsn(RETURN);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
    checkMethod(tmv);
  }

  protected void checkMethod(TraceMethodVisitor tmv) {
    TraceMethodVisitor mv = new TraceMethodVisitor(null, new Textifier());
    mv.visitCode();
    mv.visitVarInsn(ILOAD, 1);
    Label label = new Label();
    mv.visitJumpInsn(IFLT, label);
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ILOAD, 1);
    mv.visitFieldInsn(PUTFIELD, "pkg/Bean", "f", "I");
    mv.visitInsn(RETURN);
    mv.visitLabel(label);
    mv.visitFrame(F_SAME, 0, null, 0, null);
    mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL,
        "java/lang/IllegalArgumentException", "<init>", "()V");
    mv.visitInsn(ATHROW);
    mv.visitLabel(new Label());
    mv.visitMaxs(2, 2);
    mv.visitEnd();
    assertEquals(mv, tmv);
  }

  @Override
  protected ClassVisitor getClassAdapter(final ClassVisitor cv) {
    return new ClassVisitor(ASM4, cv) {
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
        return new RemoveDeadCodeAdapter(owner, access, name, desc, cv
            .visitMethod(access, name, desc, signature, exceptions));
      }
    };
  }
}
