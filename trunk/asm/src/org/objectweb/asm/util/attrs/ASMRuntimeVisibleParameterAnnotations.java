/**
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000,2002,2003 INRIA, France Telecom
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

package org.objectweb.asm.util.attrs;

import java.util.Map;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.attrs.RuntimeVisibleParameterAnnotations;

/**
 * An {@link ASMifiable} {@link RuntimeVisibleParameterAnnotations} sub class.
 *
 * @author Eugene Kuleshov
 */

public class ASMRuntimeVisibleParameterAnnotations 
  extends RuntimeVisibleParameterAnnotations 
  implements ASMifiable
{

  protected Attribute read (ClassReader cr, int off,
    int len, char[] buf, int codeOff, Label[] labels) 
  {
    RuntimeVisibleParameterAnnotations attr = 
      (RuntimeVisibleParameterAnnotations)super.read(
        cr, off, len, buf, codeOff, labels);
    
    ASMRuntimeVisibleParameterAnnotations result = new ASMRuntimeVisibleParameterAnnotations();
    result.parameters = attr.parameters;
    return result;
  }

  public void asmify (StringBuffer buf, String varName, Map labelNames) {
    buf.append("RuntimeVisibleParameterAnnotations ").append(varName)
      .append(" = new RuntimeVisibleParameterAnnotations();\n");
    ASMAnnotationDefaultAttribute.asmifyParameterAnnotations(buf, varName, parameters);
  }
}
