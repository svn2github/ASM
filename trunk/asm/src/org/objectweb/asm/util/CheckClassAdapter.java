/***
 * Julia: France Telecom's implementation of the Fractal API
 * Copyright (C) 2001-2002 France Telecom R&D
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Eric.Bruneton@rd.francetelecom.com
 *
 * Author: Eric Bruneton
 */

package org.objectweb.asm.util;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.CodeVisitor;
import org.objectweb.asm.CodeAdapter;
import org.objectweb.asm.Constants;

/**
 * A {@link ClassAdapter ClssAdapter} that checks that its methods are properly
 * used. More precisely this class adapter checks each method call individually,
 * based <i>only</i> on its arguments, but does <i>not</i> check the
 * <i>sequence</i> of method calls. For example, the invalid sequence
 * <tt>visitField(ACC_PUBLIC, "i", "I", null)</tt> <tt>visitField(ACC_PUBLIC,
 * "i", "D", null)</tt> will <i>not</i> be detected by this class adapter.
 */

public class CheckClassAdapter extends ClassAdapter {

  /**
   * <tt>true</tt> if the visit method has been called.
   */

  private boolean start;

  /**
   * <tt>true</tt> if the visitEnd method has been called.
   */

  private boolean end;

  /**
   * Constructs a new {@link CheckClassAdapter CheckClassAdapter} object.
   *
   * @param cv the class visitor to which this adapter must delegate calls.
   */

  public CheckClassAdapter (final ClassVisitor cv) {
    super(cv);
  }

  public void visit (
    final int access,
    final String name,
    final String superName,
    final String[] interfaces,
    final String sourceFile)
  {
    if (start) {
      throw new IllegalStateException("visit must be called only once");
    } else {
      start = true;
    }
    checkState();
    checkAccess(access, 1 + 2 + 4 + 16 + 512 + 1024 + 32 + 65536 + 131072);
    CheckCodeAdapter.checkInternalName(name, "class name");
    CheckCodeAdapter.checkInternalName(superName, "super class name");
    if (interfaces != null) {
      for (int i = 0; i < interfaces.length; ++i) {
        CheckCodeAdapter.checkInternalName(
          interfaces[i], "interface name at index " + i);
      }
    }
    cv.visit(access, name, superName, interfaces, sourceFile);
  }

  public void visitInnerClass (
    final String name,
    final String outerName,
    final String innerName,
    final int access)
  {
    checkState();
    CheckCodeAdapter.checkInternalName(name, "class name");
    if (outerName != null) {
      CheckCodeAdapter.checkInternalName(outerName, "outer class name");
    }
    if (innerName != null) {
      CheckCodeAdapter.checkIdentifier(innerName, "inner class name");
    }
    checkAccess(access, 1 + 2 + 4 + 8 + 16 + 512 + 1024 + 32);
    cv.visitInnerClass(name, outerName, innerName, access);
  }

  public void visitField (
    final int access,
    final String name,
    final String desc,
    final Object value)
  {
    checkState();
    checkAccess(access, 1 + 2 + 4 + 8 + 16 + 64 + 128 + 65536 + 131072);
    CheckCodeAdapter.checkIdentifier(name, "field name");
    CheckCodeAdapter.checkDesc(desc, false);
    if (value != null) {
      CheckCodeAdapter.checkConstant(value);
    }
    cv.visitField(access, name, desc, value);
  }

  public CodeVisitor visitMethod (
    final int access,
    final String name,
    final String desc,
    final String[] exceptions)
  {
    checkState();
    checkAccess(
      access, 1 + 2 + 4 + 8 + 16 + 32 + 256 + 1024 + 2048 + 65536 + 131072);
    CheckCodeAdapter.checkMethodIdentifier(name, "method name");
    CheckCodeAdapter.checkMethodDesc(desc);
    if (exceptions != null) {
      for (int i = 0; i < exceptions.length; ++i) {
        CheckCodeAdapter.checkInternalName(
          exceptions[i], "exception name at index " + i);
      }
    }
    return new CheckCodeAdapter(cv.visitMethod(access, name, desc, exceptions));
  }

  public void visitEnd () {
    checkState();
    end = true;
    cv.visitEnd();
  }

  // ---------------------------------------------------------------------------

  /**
   * Checks that the visit method has been called and that visitEnd has not been
   * called.
   */

  private void checkState () {
    if (!start) {
      throw new IllegalStateException(
        "Cannot visit member before visit has been called.");
    }
    if (end) {
      throw new IllegalStateException(
        "Cannot visit member after visitEnd has been called.");
    }
  }

  /**
   * Checks that the given access flags do not contain invalid flags. This
   * method also checks that mutually incompatible flags are not set
   * simultaneously.
   *
   * @param access the access flags to be checked
   * @param possibleAccess the valid access flags.
   */

  static void checkAccess (final int access, final int possibleAccess) {
    if ((access & ~possibleAccess) != 0) {
      throw new IllegalArgumentException("Invalid access flags: " + access);
    }
    int pub = ((access & Constants.ACC_PUBLIC) != 0 ? 1 : 0);
    int pri = ((access & Constants.ACC_PRIVATE) != 0 ? 1 : 0);
    int pro = ((access & Constants.ACC_PROTECTED) != 0 ? 1 : 0);
    if (pub + pri + pro > 1) {
      throw new IllegalArgumentException(
        "public private and protected are mutually exclusive: " + access);
    }
    int fin = ((access & Constants.ACC_FINAL) != 0 ? 1 : 0);
    int abs = ((access & Constants.ACC_ABSTRACT) != 0 ? 1 : 0);
    if (fin + abs > 1) {
      throw new IllegalArgumentException(
        "final and abstract are mutually exclusive: " + access);
    }
  }
}
