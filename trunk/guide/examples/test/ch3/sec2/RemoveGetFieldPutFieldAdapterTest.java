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

package ch3.sec2;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.PUTFIELD;

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
public class RemoveGetFieldPutFieldAdapterTest extends AbstractTestCase {

  public void test() {
    TraceMethodVisitor tmv = new TraceMethodVisitor(null);
    MethodVisitor mv = new RemoveGetFieldPutFieldAdapter(tmv);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(GETFIELD, "C", "f", "I");
    mv.visitFieldInsn(PUTFIELD, "C", "f", "I");
    mv.visitFieldInsn(GETFIELD, "C", "f", "I");
    mv.visitInsn(IRETURN);
    mv.visitMaxs(0, 0);
    mv.visitEnd();
    checkMethod(tmv);
  }

  protected void checkMethod(TraceMethodVisitor tmv) {
    TraceMethodVisitor mv = new TraceMethodVisitor(null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(GETFIELD, "C", "f", "I");
    mv.visitInsn(IRETURN);
    mv.visitMaxs(0, 0);
    mv.visitEnd();
    assertEquals(mv, tmv);
  }

  @Override
  protected ClassVisitor getClassAdapter(ClassVisitor cv) {
    return new ClassAdapter(cv) {
      @Override
      public MethodVisitor visitMethod(int access, String name,
          String desc, String signature, String[] exceptions) {
        return new RemoveGetFieldPutFieldAdapter(cv.visitMethod(access,
            name, desc, signature, exceptions));
      }
    };
  }
}
