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

import static org.objectweb.asm.Opcodes.*;

import java.util.Iterator;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import util.AbstractTestCase;

/**
 * ASM Guide example test class.
 * 
 * @author Eric Bruneton
 */
public class CyclomaticComplexityTest extends AbstractTestCase {

  public void test() throws AnalyzerException {
    MethodNode mn = new MethodNode(ACC_PUBLIC, "checkAndSetF", "(I)V",
        null, null);
    InsnList il = mn.instructions;
    il.add(new VarInsnNode(ILOAD, 1));
    LabelNode label = new LabelNode();
    il.add(new JumpInsnNode(IFLT, label));
    il.add(new VarInsnNode(ALOAD, 0));
    il.add(new VarInsnNode(ILOAD, 1));
    il.add(new FieldInsnNode(PUTFIELD, "pkg/Bean", "f", "I"));
    LabelNode end = new LabelNode();
    il.add(new JumpInsnNode(GOTO, end));
    il.add(label);
    il.add(new FrameNode(F_SAME, 0, null, 0, null));
    il.add(new TypeInsnNode(NEW, "java/lang/IllegalArgumentException"));
    il.add(new InsnNode(DUP));
    il.add(new MethodInsnNode(INVOKESPECIAL,
        "java/lang/IllegalArgumentException", "<init>", "()V"));
    il.add(new InsnNode(ATHROW));
    il.add(end);
    il.add(new FrameNode(F_SAME, 0, null, 0, null));
    il.add(new InsnNode(RETURN));
    mn.maxStack = 2;
    mn.maxLocals = 2;
    CyclomaticComplexity cc = new CyclomaticComplexity();
    assert (cc.getCyclomaticComplexity("pkg/Bean", mn) == 1);
  }
  
  @Override
  protected ClassVisitor getClassAdapter(final ClassVisitor next) {
    return new ClassNode(ASM4) {
      public void visitEnd() {
        Iterator<MethodNode> i = methods.iterator();
        while (i.hasNext()) {
          MethodNode mn = i.next();
          try {
            CyclomaticComplexity cc = new CyclomaticComplexity();
            assert (cc.getCyclomaticComplexity(name, mn) > 0);
          } catch (AnalyzerException ignored) {
          }
        }
        accept(next);
      }
    };
  }
}
