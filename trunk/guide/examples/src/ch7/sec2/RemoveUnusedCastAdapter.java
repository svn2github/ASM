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

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.CHECKCAST;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class RemoveUnusedCastAdapter extends MethodVisitor {

  public AnalyzerAdapter aa;

  public RemoveUnusedCastAdapter(MethodVisitor mv) {
    super(ASM4, mv);
  }

  public void visitTypeInsn(int opcode, String desc) {
    if (opcode == CHECKCAST) {
      Class<?> to = getClass(desc);
      if (aa.stack != null && aa.stack.size() > 0) {
        Object operand = aa.stack.get(aa.stack.size() - 1);
        if (operand instanceof String) {
          Class<?> from = getClass((String) operand);
          if (to.isAssignableFrom(from)) {
            return;
          }
        }
      }
    }
    mv.visitTypeInsn(opcode, desc);
  }

  private Class<?> getClass(String desc) {
	ClassLoader classLoader = getClass().getClassLoader();
	try {
      return Class.forName(desc.replace('/', '.'), false, classLoader);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e.toString());
    }
  }
}
