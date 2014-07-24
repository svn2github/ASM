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

package ch8.sec2;

import static org.objectweb.asm.Opcodes.V1_5;

import java.util.ArrayList;
import java.util.Iterator;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import ch5.sec1.ClassTransformer;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class AddAnnotationTransformer extends ClassTransformer {

  private String annotationDesc;

  public AddAnnotationTransformer(ClassTransformer ct,
      String annotationDesc) {
    super(ct);
    this.annotationDesc = annotationDesc;
  }

  public void transform(ClassNode cn) {
    cn.version = (cn.version & 0xFF) < V1_5 ? V1_5 : cn.version;
    boolean isPresent = false;
    if (cn.visibleAnnotations != null) {
      Iterator<AnnotationNode> i = cn.visibleAnnotations.iterator();
      while (i.hasNext()) {
        AnnotationNode an = i.next();
        if (annotationDesc.equals(an.desc)) {
          isPresent = true;
          break;
        }
      }
    }
    if (!isPresent) {
      if (cn.visibleAnnotations == null) {
        cn.visibleAnnotations = new ArrayList<AnnotationNode>(5);
      }
      cn.visibleAnnotations.add(new AnnotationNode(annotationDesc));
    }
    super.transform(cn);
  }
}
