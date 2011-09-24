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

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.PUTFIELD;

import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import ch3.sec2.RemoveGetFieldPutFieldAdapterTest;

/**
 * ASM Guide example test class.
 * 
 * @author Eric Bruneton
 */
public class RemoveGetFieldPutFieldTransformer2Test extends
    RemoveGetFieldPutFieldAdapterTest {

  public void test() {
    TraceMethodVisitor tmv = new TraceMethodVisitor(null, new Textifier());
    MethodNode mn = new MethodNode(0, null, null, null, null);
    mn.visitCode();
    mn.visitVarInsn(ALOAD, 0);
    mn.visitVarInsn(ALOAD, 0);
    mn.visitVarInsn(ALOAD, 0);
    mn.visitFieldInsn(GETFIELD, "C", "f", "I");
    mn.visitFieldInsn(PUTFIELD, "C", "f", "I");
    mn.visitFieldInsn(GETFIELD, "C", "f", "I");
    mn.visitInsn(IRETURN);
    mn.visitMaxs(0, 0);
    mn.visitEnd();
    new RemoveGetFieldPutFieldTransformer2(null).transform(mn);
    mn.accept(tmv);
    checkMethod(tmv);
  }

  @Override
  protected ClassVisitor getClassAdapter(final ClassVisitor next) {
    return new ClassNode(ASM4) {
      @Override
      public void visitEnd() {
        for (MethodNode mn : (List<MethodNode>) methods) {
          new RemoveGetFieldPutFieldTransformer2(null).transform(mn);
        }
        accept(next);
      }
    };
  }
}
