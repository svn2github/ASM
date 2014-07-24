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

import java.util.HashSet;
import java.util.Set;

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
public class CyclomaticComplexity {
  
  public int getCyclomaticComplexity(String owner, MethodNode mn)
      throws AnalyzerException {
    Analyzer<BasicValue> a = new Analyzer<BasicValue>(new BasicInterpreter()) {
      protected Frame<BasicValue> newFrame(int nLocals, int nStack) {
        return new Node<BasicValue>(nLocals, nStack);
      }

      @Override
      protected Frame<BasicValue> newFrame(Frame<? extends BasicValue> src) {
        return new Node<BasicValue>(src);
      }

      @Override
      protected void newControlFlowEdge(int src, int dst) {
        Node<BasicValue> s = (Node<BasicValue>) getFrames()[src];
        s.successors.add((Node<BasicValue>) getFrames()[dst]);
      }
    };
    a.analyze(owner, mn);
    Frame<BasicValue>[] frames = a.getFrames();
    int edges = 0;
    int nodes = 0;
    for (int i = 0; i < frames.length; ++i) {
      if (frames[i] != null) {
        edges += ((Node<BasicValue>) frames[i]).successors.size();
        nodes += 1;
      }
    }
    return edges - nodes + 2;
  }
}

class Node<V extends Value> extends Frame<V> {

  Set< Node<V> > successors = new HashSet< Node<V> >();

  public Node(int nLocals, int nStack) {
    super(nLocals, nStack);
  }

  public Node(Frame<? extends V> src) {
    super(src);
  }
}
