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

import static org.objectweb.asm.Opcodes.ARRAYLENGTH;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.MONITORENTER;
import static org.objectweb.asm.Opcodes.MONITOREXIT;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.tree.analysis.BasicValue.REFERENCE_VALUE;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Value;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class NullDereferenceAnalyzer {

  public List<AbstractInsnNode> findNullDereferences(String owner,
      MethodNode mn) throws AnalyzerException {
    List<AbstractInsnNode> result = new ArrayList<AbstractInsnNode>();
    Analyzer<BasicValue> a = new Analyzer<BasicValue>(new IsNullInterpreter());
    a.analyze(owner, mn);
    Frame<BasicValue>[] frames = a.getFrames();
    AbstractInsnNode[] insns = mn.instructions.toArray();
    for (int i = 0; i < insns.length; ++i) {
      AbstractInsnNode insn = insns[i];
      if (frames[i] != null) {
        Value v = getTarget(insn, frames[i]);
        if (v == IsNullInterpreter.NULL
            || v == IsNullInterpreter.MAYBENULL) {
          result.add(insn);
        }
      }
    }
    return result;
  }

  private static BasicValue getTarget(AbstractInsnNode insn, Frame<BasicValue> f) {
    switch (insn.getOpcode()) {
    case GETFIELD:
    case ARRAYLENGTH:
    case MONITORENTER:
    case MONITOREXIT:
      return getStackValue(f, 0);
    case PUTFIELD:
      return getStackValue(f, 1);
    case INVOKEVIRTUAL:
    case INVOKESPECIAL:
    case INVOKEINTERFACE:
      String desc = ((MethodInsnNode) insn).desc;
      return getStackValue(f, Type.getArgumentTypes(desc).length);
    }
    return null;
  }

  private static BasicValue getStackValue(Frame<BasicValue> f, int index) {
    int top = f.getStackSize() - 1;
    return index <= top ? f.getStack(top - index) : null;
  }
}

class IsNullInterpreter extends BasicInterpreter {

  public final static BasicValue NULL = new BasicValue(null);

  public final static BasicValue MAYBENULL = new BasicValue(null);

  public IsNullInterpreter() {
	  super(ASM4);
  }
  
  @Override
  public BasicValue newOperation(AbstractInsnNode insn) 
    throws AnalyzerException 
  {
    if (insn.getOpcode() == ACONST_NULL) {
      return NULL;
    }
    return super.newOperation(insn);
  }

  @Override
  public BasicValue merge(BasicValue v, BasicValue w) {
    if (isRef(v) && isRef(w) && v != w) {
      return MAYBENULL;
    }
    return super.merge(v, w);
  }

  private boolean isRef(Value v) {
    return v == REFERENCE_VALUE || v == NULL || v == MAYBENULL;
  }
}
