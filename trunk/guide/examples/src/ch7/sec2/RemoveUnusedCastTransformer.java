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

import static org.objectweb.asm.Opcodes.CHECKCAST;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

import ch6.sec1.MethodTransformer;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class RemoveUnusedCastTransformer extends MethodTransformer {

  String owner;

  public RemoveUnusedCastTransformer(String owner, MethodTransformer mt) {
    super(mt);
    this.owner = owner;
  }

  public void transform(MethodNode mn) {
    Analyzer<BasicValue> a = new Analyzer<BasicValue>(new SimpleVerifier());
    try {
      a.analyze(owner, mn);
      Frame<BasicValue>[] frames = a.getFrames();
      AbstractInsnNode[] insns = mn.instructions.toArray();
      for (int i = 0; i < insns.length; ++i) {
        AbstractInsnNode insn = insns[i];
        if (insn.getOpcode() == CHECKCAST) {
          Frame<BasicValue> f = frames[i];
          if (f != null && f.getStackSize() > 0) {
            Object operand = f.getStack(f.getStackSize() - 1);
            Class<?> to, from;
            try {
              to = getClass(((TypeInsnNode) insn).desc);
              from = getClass(((BasicValue) operand).getType());
            } catch (ClassNotFoundException t) {
              continue;
            }
            if (to.isAssignableFrom(from)) {
              mn.instructions.remove(insn);
            }
          }
        }
      }
    } catch (AnalyzerException ignored) {
    }
    super.transform(mn);
  }

  private Class<?> getClass(String desc)
      throws ClassNotFoundException {
	ClassLoader classLoader = getClass().getClassLoader();
	return Class.forName(desc.replace('/', '.'), false, classLoader);
  }

  private Class<?> getClass(Type t) throws ClassNotFoundException {
    if (t.getSort() == Type.OBJECT) {
      return getClass(t.getInternalName());
    }
    return getClass(t.getDescriptor());
  }
}
