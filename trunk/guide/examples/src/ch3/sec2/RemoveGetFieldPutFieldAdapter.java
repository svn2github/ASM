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

package ch3.sec2;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.PUTFIELD;

import org.objectweb.asm.MethodVisitor;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class RemoveGetFieldPutFieldAdapter extends PatternMethodAdapter {

  private final static int SEEN_ALOAD_0 = 1;

  private final static int SEEN_ALOAD_0ALOAD_0 = 2;

  private final static int SEEN_ALOAD_0ALOAD_0GETFIELD = 3;

  private String fieldOwner;

  private String fieldName;

  private String fieldDesc;

  public RemoveGetFieldPutFieldAdapter(MethodVisitor mv) {
    super(mv);
  }

  public void visitVarInsn(int opcode, int var) {
    switch (state) {
    case SEEN_NOTHING: // S0 -> S1
      if (opcode == ALOAD && var == 0) {
        state = SEEN_ALOAD_0;
        return;
      }
      break;
    case SEEN_ALOAD_0: // S1 -> S2
      if (opcode == ALOAD && var == 0) {
        state = SEEN_ALOAD_0ALOAD_0;
        return;
      }
      break;
    case SEEN_ALOAD_0ALOAD_0: // S2 -> S2
      if (opcode == ALOAD && var == 0) {
        mv.visitVarInsn(ALOAD, 0);
        return;
      }
      break;
    }
    visitInsn();
    mv.visitVarInsn(opcode, var);
  }

  public void visitFieldInsn(int opcode, String owner, String name,
      String desc) {
    switch (state) {
    case SEEN_ALOAD_0ALOAD_0: // S2 -> S3
      if (opcode == GETFIELD) {
        state = SEEN_ALOAD_0ALOAD_0GETFIELD;
        fieldOwner = owner;
        fieldName = name;
        fieldDesc = desc;
        return;
      }
      break;
    case SEEN_ALOAD_0ALOAD_0GETFIELD: // S3 -> S0
      if (opcode == PUTFIELD && name.equals(fieldName)) {
        state = SEEN_NOTHING;
        return;
      }
      break;
    }
    visitInsn();
    mv.visitFieldInsn(opcode, owner, name, desc);
  }

  protected void visitInsn() {
    switch (state) {
    case SEEN_ALOAD_0: // S1 -> S0
      mv.visitVarInsn(ALOAD, 0);
      break;
    case SEEN_ALOAD_0ALOAD_0: // S2 -> S0
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 0);
      break;
    case SEEN_ALOAD_0ALOAD_0GETFIELD: // S3 -> S0
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, fieldOwner, fieldName, fieldDesc);
      break;
    }
    state = SEEN_NOTHING;
  }
}
