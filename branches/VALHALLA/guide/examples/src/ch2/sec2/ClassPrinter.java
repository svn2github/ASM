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

package ch2.sec2;

import static org.objectweb.asm.Opcodes.ASM4;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class ClassPrinter extends ClassVisitor {

  public ClassPrinter() {
	super(ASM4);
  }

  public void visit(int version, int access, String name,
      String signature, String superName, String[] interfaces) {
    System.out.println(name + " extends " + superName + " {");
  }

  public void visitSource(String source, String debug) {
  }

  public void visitOuterClass(String owner, String name, String desc) {
  }

  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    return null;
  }

  public void visitAttribute(Attribute attr) {
  }

  public void visitInnerClass(String name, String outerName,
      String innerName, int access) {
  }

  public FieldVisitor visitField(int access, String name, String desc,
      String signature, Object value) {
    System.out.println("    " + desc + " " + name);
    return null;
  }

  public MethodVisitor visitMethod(int access, String name,
      String desc, String signature, String[] exceptions) {
    System.out.println("    " + name + desc);
    return null;
  }

  public void visitEnd() {
    System.out.println("}");
  }
}
