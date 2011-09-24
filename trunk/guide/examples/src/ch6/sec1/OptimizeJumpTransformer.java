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

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class OptimizeJumpTransformer extends MethodTransformer {

  public OptimizeJumpTransformer(MethodTransformer mt) {
    super(mt);
  }

  public void transform(MethodNode mn) {
    InsnList insns = mn.instructions;
    Iterator<AbstractInsnNode> i = insns.iterator();
    while (i.hasNext()) {
      AbstractInsnNode in = i.next();
      if (in instanceof JumpInsnNode) {
        LabelNode label = ((JumpInsnNode) in).label;
        AbstractInsnNode target;
        // while target == goto l, replace label with l
        while (true) {
          target = label;
          while (target != null && target.getOpcode() < 0) {
            target = target.getNext();
          }
          if (target != null && target.getOpcode() == GOTO) {
            label = ((JumpInsnNode) target).label;
          } else {
            break;
          }
        }
        // update target
        ((JumpInsnNode) in).label = label;
        // if possible, replace jump with target instruction
        if (in.getOpcode() == GOTO && target != null) {
          int op = target.getOpcode();
          if ((op >= IRETURN && op <= RETURN) || op == ATHROW) {
            // replace 'in' with clone of 'target'
            insns.set(in, target.clone(null));
          }
        }
      }
    }
    super.transform(mn);
  }
}
