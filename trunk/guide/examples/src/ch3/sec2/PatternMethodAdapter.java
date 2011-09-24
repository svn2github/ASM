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

import static org.objectweb.asm.Opcodes.ASM4;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public abstract class PatternMethodAdapter extends MethodVisitor {

  protected final static int SEEN_NOTHING = 0;

  protected int state;

  public PatternMethodAdapter(MethodVisitor mv) {
    super(ASM4, mv);
  }

  public void visitFrame(int type, int nLocal, Object[] local,
      int nStack, Object[] stack) {
    visitInsn();
    mv.visitFrame(type, nLocal, local, nStack, stack);
  }

  public void visitInsn(int opcode) {
    visitInsn();
    mv.visitInsn(opcode);
  }

  public void visitIntInsn(int opcode, int operand) {
    visitInsn();
    mv.visitIntInsn(opcode, operand);
  }

  public void visitVarInsn(int opcode, int var) {
    visitInsn();
    mv.visitVarInsn(opcode, var);
  }

  public void visitTypeInsn(int opcode, String desc) {
    visitInsn();
    mv.visitTypeInsn(opcode, desc);
  }

  public void visitFieldInsn(int opcode, String owner, String name,
      String desc) {
    visitInsn();
    mv.visitFieldInsn(opcode, owner, name, desc);
  }

  public void visitMethodInsn(int opcode, String owner, String name,
      String desc) {
    visitInsn();
    mv.visitMethodInsn(opcode, owner, name, desc);
  }

  public void visitJumpInsn(int opcode, Label label) {
    visitInsn();
    mv.visitJumpInsn(opcode, label);
  }

  public void visitLabel(Label label) {
    visitInsn();
    mv.visitLabel(label);
  }

  public void visitLdcInsn(Object cst) {
    visitInsn();
    mv.visitLdcInsn(cst);
  }

  public void visitIincInsn(int var, int increment) {
    visitInsn();
    mv.visitIincInsn(var, increment);
  }

  public void visitTableSwitchInsn(int min, int max, Label dflt,
      Label... labels) {
    visitInsn();
    mv.visitTableSwitchInsn(min, max, dflt, labels);
  }

  public void visitLookupSwitchInsn(Label dflt, int keys[],
      Label labels[]) {
    visitInsn();
    mv.visitLookupSwitchInsn(dflt, keys, labels);
  }

  public void visitMultiANewArrayInsn(String desc, int dims) {
    visitInsn();
    mv.visitMultiANewArrayInsn(desc, dims);
  }

  public void visitMaxs(int maxStack, int maxLocals) {
    visitInsn();
    mv.visitMaxs(maxStack, maxLocals);
  }

  protected abstract void visitInsn();
}
