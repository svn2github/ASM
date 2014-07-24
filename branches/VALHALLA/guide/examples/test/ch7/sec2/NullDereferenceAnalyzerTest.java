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
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.MONITORENTER;
import static org.objectweb.asm.Opcodes.MONITOREXIT;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import util.AbstractTestCase;

/**
 * ASM Guide example test class.
 * 
 * @author Eric Bruneton
 */
public class NullDereferenceAnalyzerTest extends AbstractTestCase {

  public void test() throws Exception {
    MethodNode mn = new MethodNode(ACC_PUBLIC, "m", "(ZZ)V", null, null);
    mn.visitCode();
    Label l0 = new Label();
    Label l1 = new Label();
    Label l2 = new Label();
    Label l3 = new Label();
    Label l4 = new Label();
    Label l5 = new Label();
    Label l6 = new Label();
    mn.visitTryCatchBlock(l0, l1, l2, null);
    mn.visitTryCatchBlock(l2, l3, l2, null);
    mn.visitInsn(ACONST_NULL); // insn 0
    mn.visitVarInsn(ASTORE, 3);
    mn.visitInsn(ACONST_NULL); // insn 2
    mn.visitVarInsn(ASTORE, 4);
    mn.visitTypeInsn(NEW, "C");
    mn.visitInsn(DUP);
    mn.visitMethodInsn(INVOKESPECIAL, "C", "<init>", "()V");
    mn.visitVarInsn(ASTORE, 5);
    mn.visitVarInsn(ILOAD, 1);
    mn.visitJumpInsn(IFEQ, l4);
    mn.visitTypeInsn(NEW, "java/lang/Integer"); // insn
    // 10
    mn.visitInsn(DUP);
    mn.visitInsn(ICONST_1);
    mn.visitMethodInsn(INVOKESPECIAL, "java/lang/Integer", "<init>",
        "(I)V");
    mn.visitVarInsn(ASTORE, 3);
    mn.visitTypeInsn(NEW, "C"); // insn 15
    mn.visitInsn(DUP);
    mn.visitMethodInsn(INVOKESPECIAL, "C", "<init>", "()V");
    mn.visitVarInsn(ASTORE, 4);
    mn.visitLabel(l4);
    mn.visitVarInsn(ILOAD, 2);
    mn.visitJumpInsn(IFEQ, l5);
    mn.visitTypeInsn(NEW, "java/lang/Integer"); // insn
    // 22
    mn.visitInsn(DUP);
    mn.visitInsn(ICONST_1);
    mn.visitMethodInsn(INVOKESPECIAL, "java/lang/Integer", "<init>",
        "(I)V");
    mn.visitVarInsn(ASTORE, 3);
    mn.visitTypeInsn(NEW, "C"); // insn 27
    mn.visitInsn(DUP);
    mn.visitMethodInsn(INVOKESPECIAL, "C", "<init>", "()V");
    mn.visitVarInsn(ASTORE, 4);
    mn.visitLabel(l5);
    mn.visitVarInsn(ALOAD, 4);
    mn.visitInsn(DUP);
    mn.visitVarInsn(ASTORE, 6);
    mn.visitInsn(MONITORENTER); // insn 35
    mn.visitLabel(l0);
    mn.visitVarInsn(ALOAD, 4);
    mn.visitVarInsn(ALOAD, 4);
    mn.visitFieldInsn(GETFIELD, "C", "i", "I"); // insn
    // 39
    mn.visitVarInsn(ALOAD, 5);
    mn.visitFieldInsn(GETFIELD, "C", "i", "I");
    mn.visitInsn(IADD);
    mn.visitVarInsn(ALOAD, 3);
    mn.visitMethodInsn(INVOKEVIRTUAL, // insn 44
        "java/lang/Integer", "intValue", "()I");
    mn.visitInsn(IADD);
    mn.visitFieldInsn(PUTFIELD, "C", "i", "I"); // insn
    // 46
    mn.visitVarInsn(ALOAD, 6);
    mn.visitInsn(MONITOREXIT); // insn 48
    mn.visitLabel(l1);
    mn.visitJumpInsn(GOTO, l6);
    mn.visitLabel(l2);
    mn.visitVarInsn(ALOAD, 6);
    mn.visitInsn(MONITOREXIT); // insn 53
    mn.visitLabel(l3);
    mn.visitInsn(ATHROW);
    mn.visitLabel(l6);
    mn.visitInsn(RETURN);
    mn.visitMaxs(3, 7);
    mn.visitEnd();

    List<AbstractInsnNode> occurences;
    occurences = new NullDereferenceAnalyzer().findNullDereferences(
        "D", mn);
    assertEquals(6, occurences.size());
    assertTrue(occurences.contains(mn.instructions.get(35)));
    assertTrue(occurences.contains(mn.instructions.get(39)));
    assertTrue(occurences.contains(mn.instructions.get(44)));
    assertTrue(occurences.contains(mn.instructions.get(46)));
    assertTrue(occurences.contains(mn.instructions.get(48)));
    assertTrue(occurences.contains(mn.instructions.get(53)));
  }

  @Override
  protected ClassVisitor getClassAdapter(final ClassVisitor next) {
    return new ClassNode(ASM4) {
      @Override
      public void visitEnd() {
        for (MethodNode mn : (List<MethodNode>) methods) {
          try {
            new NullDereferenceAnalyzer()
                .findNullDereferences(name, mn);
          } catch (AnalyzerException e) {
          }
        }
        accept(next);
      }
    };
  }

  // source code used to get above ASMified code

  public void m(boolean a, boolean b) {
    Integer i = null;
    C c = null;
    C d = new C();
    if (a) {
      i = new Integer(1);
      c = new C();
    }
    if (b) {
      i = new Integer(1);
      c = new C();
    }
    synchronized (c) { // c may be null
      c.i = c.i + d.i + i.intValue(); // c and i may
      // be null, but
      // not d
    }
  }

  static class C {
    int i;
  }
}
