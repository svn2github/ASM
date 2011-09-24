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

package ch4.sec2;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.V1_5;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class AddAnnotationAdapter extends ClassVisitor {

  private String annotationDesc;

  private boolean isAnnotationPresent;

  public AddAnnotationAdapter(ClassVisitor cv, String annotationDesc) {
    super(ASM4, cv);
    this.annotationDesc = annotationDesc;
  }

  public void visit(int version, int access, String name,
      String signature, String superName, String[] interfaces) {
    int v = (version & 0xFF) < V1_5 ? V1_5 : version;
    cv.visit(v, access, name, signature, superName, interfaces);
  }

  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    if (visible && desc.equals(annotationDesc)) {
      isAnnotationPresent = true;
    }
    return cv.visitAnnotation(desc, visible);
  }

  public void visitInnerClass(String name, String outerName,
      String innerName, int access) {
    addAnnotation();
    cv.visitInnerClass(name, outerName, innerName, access);
  }

  public FieldVisitor visitField(int access, String name, String desc,
      String signature, Object value) {
    addAnnotation();
    return cv.visitField(access, name, desc, signature, value);
  }

  public MethodVisitor visitMethod(int access, String name,
      String desc, String signature, String[] exceptions) {
    addAnnotation();
    return cv.visitMethod(access, name, desc, signature, exceptions);
  }

  public void visitEnd() {
    addAnnotation();
    cv.visitEnd();
  }

  private void addAnnotation() {
    if (!isAnnotationPresent) {
      AnnotationVisitor av = cv.visitAnnotation(annotationDesc, true);
      if (av != null) {
        av.visitEnd();
      }
      isAnnotationPresent = true;
    }
  }
}
