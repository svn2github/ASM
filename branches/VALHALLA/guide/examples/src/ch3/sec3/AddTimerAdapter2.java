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

package ch3.sec3;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class AddTimerAdapter2 extends ClassVisitor {

  private String owner;

  private boolean isInterface;

  public AddTimerAdapter2(ClassVisitor cv) {
    super(ASM4, cv);
  }

  public void visit(int version, int access, String name,
      String signature, String superName, String[] interfaces) {
    cv.visit(version, access, name, signature, superName, interfaces);
    owner = name;
    isInterface = (access & ACC_INTERFACE) != 0;
  }

  public MethodVisitor visitMethod(int access, String name,
      String desc, String signature, String[] exceptions) {
    MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
        exceptions);
    if (!isInterface && mv != null && !name.equals("<init>")) {
      mv = new AddTimerMethodAdapter2(owner, access, name, desc, mv);
    }
    return mv;
  }

  public void visitEnd() {
    if (!isInterface) {
      FieldVisitor fv = cv.visitField(ACC_PUBLIC + ACC_STATIC, "timer",
          "J", null, null);
      if (fv != null) {
        fv.visitEnd();
      }
      cv.visitEnd();
    }
  }

  class AddTimerMethodAdapter2 extends AnalyzerAdapter {

    private int maxStack;

    public AddTimerMethodAdapter2(String owner, int access,
        String name, String desc, MethodVisitor mv) {
      super(ASM4, owner, access, name, desc, mv);
    }

    public void visitCode() {
      super.visitCode();
      mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
      mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
          "currentTimeMillis", "()J");
      mv.visitInsn(LSUB);
      mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
      maxStack = 4;
    }

    public void visitInsn(int opcode) {
      if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
        mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
            "currentTimeMillis", "()J");
        mv.visitInsn(LADD);
        mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
        maxStack = Math.max(maxStack, stack.size() + 4);
      }
      super.visitInsn(opcode);
    }

    public void visitMaxs(int maxStack, int maxLocals) {
      super.visitMaxs(Math.max(this.maxStack, maxStack), maxLocals);
    }
  }
}
