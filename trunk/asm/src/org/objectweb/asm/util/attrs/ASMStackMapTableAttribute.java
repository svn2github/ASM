/**
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2005 INRIA, France Telecom
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

import java.util.List;
import java.util.Map;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.attrs.StackMapTableAttribute;
import org.objectweb.asm.attrs.StackMapFrame;
import org.objectweb.asm.attrs.StackMapType;

/**
 * An {@link ASMifiable} {@link StackMapTableAttribute} sub class.
 *
 * @author Eugene Kuleshov
 */

public class ASMStackMapTableAttribute extends StackMapTableAttribute 
  implements ASMifiable 
{
  
  public ASMStackMapTableAttribute() {
    super();
  }
  
  public ASMStackMapTableAttribute( List frames) {
    super( frames);
  }

  protected Attribute read (ClassReader cr, int off,
    int len, char[] buf, int codeOff, Label[] labels) 
  {
    StackMapTableAttribute attr = 
      (StackMapTableAttribute)super.read(cr, off, len, buf, codeOff, labels);
    
    return new ASMStackMapTableAttribute( attr.getFrames());
  }

  public void asmify (StringBuffer buf, String varName, Map labelNames) {
    List frames = getFrames();
    buf.append("{\n");
    buf.append("org.objectweb.asm.attrs.StackMapTableAttribute ").append(varName).append("Attr");
    buf.append(" = new org.objectweb.asm.attrs.StackMapTableAttribute();\n");
    if (frames.size() > 0) {
      for (int i = 0; i < frames.size(); i++) {
        asmify((StackMapFrame)frames.get(i), buf, varName + "frame" + i, labelNames);
      }
    }
    buf.append(varName).append(".visitAttribute(").append(varName);
    buf.append("Attr);\n}\n");
  }

  void asmify (StackMapFrame f, StringBuffer buf, String varName, Map labelNames) {
    declareLabel(buf, labelNames, f.label);
    buf.append("{\n");
    
    buf.append("org.objectweb.asm.attrs.StackMapFrame ").append(varName).append( " = new org.objectweb.asm.attrs.StackMapFrame();\n");
    
    buf.append(varName).append(".label = ")
      .append(labelNames.get(f.label)).append(";\n");

    asmifyTypeInfo(buf, varName, labelNames, f.locals, "locals");
    asmifyTypeInfo(buf, varName, labelNames, f.stack, "stack");

    buf.append( "cvAttr.frames.add(").append(varName).append(");\n");
    buf.append("}\n");
  }

  void asmifyTypeInfo (StringBuffer buf, String varName, 
                     Map labelNames, List infos, String field) {
    if (infos.size() > 0) {
      buf.append("{\n");
      for (int i = 0; i < infos.size(); i++) {
        StackMapType typeInfo = (StackMapType)infos.get(i);
        String localName = varName + "Info" + i;
        int type = typeInfo.getType();
        buf.append("org.objectweb.asm.attrs.StackMapType ").append(localName)
          .append(" = org.objectweb.asm.attrs.StackMapType.getTypeInfo( StackMapType.ITEM_")
          .append(StackMapType.ITEM_NAMES[type]).append(");\n");

        switch (type) {
          case StackMapType.ITEM_Object:  //
            buf.append(localName).append(".setObject(\"")
              .append(typeInfo.getObject()).append("\");\n");
            break;

          case StackMapType.ITEM_Uninitialized:  //
            declareLabel(buf, labelNames, typeInfo.getLabel());
            buf.append(localName).append(".setLabel(")
              .append(labelNames.get(typeInfo.getLabel())).append(");\n");
            break;
        }
        buf.append(varName).append(".").append(field)
          .append(".add(").append(localName).append(");\n");
      }
      buf.append("}\n");
    }
  }

  static void declareLabel (StringBuffer buf, Map labelNames, Label l) {
    String name = (String)labelNames.get(l);
    if (name == null) {
      name = "l" + labelNames.size();
      labelNames.put(l, name);
      buf.append("Label ").append(name).append(" = new Label();\n");
    }
  }
}
