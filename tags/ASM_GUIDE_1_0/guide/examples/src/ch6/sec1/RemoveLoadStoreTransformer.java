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

package ch6.sec1;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.ISTORE;

import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class RemoveLoadStoreTransformer extends MethodTransformer {

  public RemoveLoadStoreTransformer(MethodTransformer mt) {
    super(mt);
  }

  public void transform(MethodNode mn) {
    InsnList insns = mn.instructions;
    Iterator i = insns.iterator();
    while (i.hasNext()) {
      AbstractInsnNode i1 = (AbstractInsnNode) i.next();
      if (i1 instanceof VarInsnNode) {
        VarInsnNode v1 = (VarInsnNode) i1;
        int op1 = v1.getOpcode();
        if (op1 >= ILOAD && op1 <= ALOAD) {
          AbstractInsnNode i2 = getNext(i);
          if (i2 instanceof VarInsnNode) {
            VarInsnNode v2 = (VarInsnNode) i2;
            int op2 = v2.getOpcode();
            if (op2 - ISTORE == op1 - ILOAD && v2.var == v1.var) {
              insns.remove(i1);
              insns.remove(i2);
            }
          }
        }
      }
    }
    super.transform(mn);
  }

  private static AbstractInsnNode getNext(Iterator i) {
    while (i.hasNext()) {
      AbstractInsnNode in = (AbstractInsnNode) i.next();
      if (!(in instanceof LineNumberNode)) {
        return in;
      }
    }
    return null;
  }
}
