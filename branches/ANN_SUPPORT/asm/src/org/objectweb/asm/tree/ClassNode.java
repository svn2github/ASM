/***
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

package org.objectweb.asm.tree;

import org.objectweb.asm.MemberVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.CodeVisitor;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A node that represents a class.
 * 
 * @author Eric Bruneton
 */

public class ClassNode extends MemberNode implements ClassVisitor {

  /**
   * The class version.
   */
  
  public int version;
  
  /**
   * The class's access flags (see {@link org.objectweb.asm.Constants}). This
   * field also indicates if the class is deprecated.
   */

  public int access;

  /**
   * The internal name of the class (see {@link
   * org.objectweb.asm.Type#getInternalName() getInternalName}).
   */

  public String name;

  /**
   * TODO.
   */
  
  public String signature;
  
  /**
   * The internal of name of the super class (see {@link
   * org.objectweb.asm.Type#getInternalName() getInternalName}). For interfaces,
   * the super class is {@link Object}. May be <tt>null</tt>, but only for the
   * {@link Object java.lang.Object} class.
   */

  public String superName;

  /**
   * The internal names of the class's interfaces (see {@link
   * org.objectweb.asm.Type#getInternalName() getInternalName}). This list is a
   * list of {@link String} objects.
   */

  public final List interfaces;

  /**
   * The name of the source file from which this class was compiled. May be
   * <tt>null</tt>.
   */

  public String sourceFile;

  /**
   * TODO.
   */
  
  public String sourceDebug;
  
  /**
   * TODO.
   */
  
  public String outerClass;
  
  /**
   * TODO.
   */
  
  public String outerMethod;
  
  /**
   * TODO.
   */
  
  public String outerMethodDesc;
  
  /**
   * Informations about the inner classes of this class. This list is a list of
   * {@link InnerClassNode InnerClassNode} objects.
   */

  public final List innerClasses;

  /**
   * The fields of this class. This list is a list of {@link FieldNode
   * FieldNode} objects.
   */

  public final List fields;

  /**
   * The methods of this class. This list is a list of {@link MethodNode
   * MethodNode} objects.
   */

  public final List methods;

  /**
   * Constructs a new {@link ClassNode ClassNode} object.
   *
   * @param version the class version.
   * @param access the class's access flags (see {@link
   *      org.objectweb.asm.Constants}). This parameter also indicates if the
   *      class is deprecated.
   * @param name the internal name of the class (see {@link
   *      org.objectweb.asm.Type#getInternalName() getInternalName}).
   * @param superName the internal of name of the super class (see {@link
   *      org.objectweb.asm.Type#getInternalName() getInternalName}). For
   *      interfaces, the super class is {@link Object}.
   * @param interfaces the internal names of the class's interfaces (see {@link
   *      org.objectweb.asm.Type#getInternalName() getInternalName}). May be
   *      <tt>null</tt>.
   * @param sourceFile the name of the source file from which this class was
   *      compiled. May be <tt>null</tt>.
   */

  public ClassNode () {
    this.interfaces = new ArrayList();
    this.innerClasses = new ArrayList();
    this.fields = new ArrayList();
    this.methods = new ArrayList();
    this.attrs = new ArrayList();
  }

  public void visit (
    final int version,
    final int access,
    final String name,
    final String signature,
    final String superName,
    final String[] interfaces)
  {
    this.version = version;
    this.access = access;
    this.name = name;
    this.signature = signature; 
    this.superName = superName;
    if (interfaces != null) {
      this.interfaces.addAll(Arrays.asList(interfaces));
    }
  }

  public void visitSource (final String file, final String debug) {
    sourceFile = file;
    sourceDebug = debug;
  }
  
  public void visitOuterClass (
    final String owner, 
    final String name, 
    final String desc) 
  {
    outerClass = owner;
    outerMethod = name;
    outerMethodDesc = desc;
  }
  
  public void visitInnerClass (
    final String name,
    final String outerName,
    final String innerName,
    final int access)
  {
    InnerClassNode icn = new InnerClassNode(name, outerName, innerName, access);
    innerClasses.add(icn);
  }

  public MemberVisitor visitField (
    final int access,
    final String name,
    final String desc,
    final String signature,
    final Object value)
  {
    FieldNode fn = new FieldNode(access, name, desc, signature, value);
    fields.add(fn);
    return fn;
  }

  public CodeVisitor visitMethod (
    final int access,
    final String name,
    final String desc,
    final String signature,
    final String[] exceptions)
  {
    MethodNode mn = new MethodNode(access, name, desc, signature, exceptions);
    methods.add(mn);
    return mn;
  }

  public void visitEnd () {
  }

  /**
   * Makes the given class visitor visit this class.
   *
   * @param cv a class visitor.
   */

  public void accept (final ClassVisitor cv) {
    // visits header
    String[] interfaces = new String[this.interfaces.size()];
    this.interfaces.toArray(interfaces);
    cv.visit(version, access, name, signature, superName, interfaces);
    // visits source
    if (sourceFile != null || sourceDebug != null) {
      cv.visitSource(sourceFile, sourceDebug);
    }
    // visits outer class
    if (outerClass != null) {
      cv.visitOuterClass(outerClass, outerMethod, outerMethodDesc);
    }
    // visits inner classes
    int i;
    for (i = 0; i < innerClasses.size(); ++i) {
      ((InnerClassNode)innerClasses.get(i)).accept(cv);
    }
    // visits fields
    for (i = 0; i < fields.size(); ++i) {
      ((FieldNode)fields.get(i)).accept(cv);
    }
    // visits methods
    for (i = 0; i < methods.size(); ++i) {
      ((MethodNode)methods.get(i)).accept(cv);
    }
    // visits attributes
    super.accept(cv);
    // visits end
    cv.visitEnd();
  }
}
