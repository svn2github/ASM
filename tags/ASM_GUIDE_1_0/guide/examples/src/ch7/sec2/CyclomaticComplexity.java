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

package ch7.sec2;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class CyclomaticComplexity {
  
  public int getCyclomaticComplexity(String owner, MethodNode mn)
      throws AnalyzerException {
    Analyzer a = new Analyzer(new BasicInterpreter()) {
      protected Frame newFrame(int nLocals, int nStack) {
        return new Node(nLocals, nStack);
      }

      protected Frame newFrame(Frame src) {
        return new Node(src);
      }

      protected void newControlFlowEdge(int src, int dst) {
        Node s = (Node) getFrames()[src];
        s.successors.add((Node) getFrames()[dst]);
      }
    };
    a.analyze(owner, mn);
    Frame[] frames = a.getFrames();
    int edges = 0;
    int nodes = 0;
    for (int i = 0; i < frames.length; ++i) {
      if (frames[i] != null) {
        edges += ((Node) frames[i]).successors.size();
        nodes += 1;
      }
    }
    return edges - nodes + 2;
  }
}

class Node extends Frame {

  Set<Node> successors = new HashSet<Node>();

  public Node(int nLocals, int nStack) {
    super(nLocals, nStack);
  }

  public Node(Frame src) {
    super(src);
  }
}
