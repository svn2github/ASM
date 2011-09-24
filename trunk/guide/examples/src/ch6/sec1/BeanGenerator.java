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

package ch6.sec1;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFLT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;

import java.io.PrintWriter;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class BeanGenerator {

  public byte[] generate(PrintWriter printWriter) {
    ClassNode cn = new ClassNode();
    cn.version = V1_6;
    cn.access = ACC_PUBLIC;
    cn.name = "pkg/Bean";
    cn.superName = "java/lang/Object";
    cn.sourceDebug = "Bean.java";
    cn.fields.add(new FieldNode(ACC_PRIVATE, "f", "I", null, null));
    {
      MethodNode mn = new MethodNode(ACC_PUBLIC, "<init>", "()V", null,
          null);
      cn.methods.add(mn);
      InsnList il = mn.instructions;
      il.add(new VarInsnNode(ALOAD, 0));
      il.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/Object",
          "<init>", "()V"));
      il.add(new InsnNode(RETURN));
      mn.maxStack = 1;
      mn.maxLocals = 1;
    }
    {
      MethodNode mn = new MethodNode(ACC_PUBLIC, "getF", "()I", null,
          null);
      cn.methods.add(mn);
      InsnList il = mn.instructions;
      il.add(new VarInsnNode(ALOAD, 0));
      il.add(new FieldInsnNode(GETFIELD, "pkg/Bean", "f", "I"));
      il.add(new InsnNode(IRETURN));
      mn.maxStack = 1;
      mn.maxLocals = 1;
    }
    {
      MethodNode mn = new MethodNode(ACC_PUBLIC, "setF", "(I)V", null,
          null);
      cn.methods.add(mn);
      InsnList il = mn.instructions;
      il.add(new VarInsnNode(ALOAD, 0));
      il.add(new VarInsnNode(ILOAD, 1));
      il.add(new FieldInsnNode(PUTFIELD, "pkg/Bean", "f", "I"));
      il.add(new InsnNode(RETURN));
      mn.maxStack = 2;
      mn.maxLocals = 2;
    }
    MethodNode mn = new MethodNode(ACC_PUBLIC, "checkAndSetF", "(I)V",
        null, null);
    cn.methods.add(mn);
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
    ClassWriter cw = new ClassWriter(0);
    cn.accept(cw);
    return cw.toByteArray();
  }
}
