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

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ch5.sec1.ClassTransformer;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class AddTimerTransformer extends ClassTransformer {

  public AddTimerTransformer(ClassTransformer ct) {
    super(ct);
  }

  public void transform(ClassNode cn) {
    if ((cn.access & ACC_INTERFACE) == 0) {
      for (MethodNode mn : (List<MethodNode>) cn.methods) {
        if ("<init>".equals(mn.name) || "<clinit>".equals(mn.name)) {
          continue;
        }
        InsnList insns = mn.instructions;
        if (insns.size() == 0) {
          continue;
        }
        Iterator<AbstractInsnNode> j = insns.iterator();
        while (j.hasNext()) {
          AbstractInsnNode in = j.next();
          int op = in.getOpcode();
          if ((op >= IRETURN && op <= RETURN) || op == ATHROW) {
            InsnList il = new InsnList();
            il.add(new FieldInsnNode(GETSTATIC, cn.name, "timer", "J"));
            il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System",
                "currentTimeMillis", "()J"));
            il.add(new InsnNode(LADD));
            il.add(new FieldInsnNode(PUTSTATIC, cn.name, "timer", "J"));
            if (in.getPrevious() == null) {
              continue;
            }
            insns.insert(in.getPrevious(), il);
          }
        }
        InsnList il = new InsnList();
        il.add(new FieldInsnNode(GETSTATIC, cn.name, "timer", "J"));
        il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System",
            "currentTimeMillis", "()J"));
        il.add(new InsnNode(LSUB));
        il.add(new FieldInsnNode(PUTSTATIC, cn.name, "timer", "J"));
        insns.insert(il);

        mn.maxStack += 4;
      }
      int acc = ACC_PUBLIC + ACC_STATIC;
      cn.fields.add(new FieldNode(acc, "timer", "J", null, null));
    }
    super.transform(cn);
  }
}
