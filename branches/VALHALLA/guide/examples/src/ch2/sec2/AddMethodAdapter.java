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

package ch2.sec2;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM4;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class AddMethodAdapter extends ClassVisitor {

  private boolean isInterface;

  private boolean isMethodPresent;

  public AddMethodAdapter(ClassVisitor cv) {
    super(ASM4, cv);
  }

  @Override
  public void visit(int version, int access, String name,
      String signature, String superName, String[] interfaces) {
    cv.visit(version, access, name, signature, superName, interfaces);
    isInterface = (access & ACC_INTERFACE) != 0;
  }

  public MethodVisitor visitMethod(int access, String name,
      String desc, String signature, String[] exceptions) {
    if (name.equals("getThis") && desc.equals("()Ljava/lang/Object;")) {
      isMethodPresent = true;
    }
    return cv.visitMethod(access, name, desc, signature, exceptions);
  }

  public void visitEnd() {
    if (!isMethodPresent && !isInterface) {
      MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "getThis",
          "()Ljava/lang/Object;", null, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitInsn(ARETURN);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    }
    cv.visitEnd();
  }
}
