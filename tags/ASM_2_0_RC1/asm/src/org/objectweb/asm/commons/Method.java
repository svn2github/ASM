/***
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

package org.objectweb.asm.commons;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Type;

/**
 * A named method descriptor.

 * @author Juozas Baliuka
 * @author Chris Nokleberg
 * @author Eric Bruneton
 */

public class Method {
  
  /**
   * The method name.
   */

  private final String name;

  /**
   * The method descriptor.
   */

  private final String desc;

  /**
   * Maps primitive Java type names to their descriptors.
   */

  private final static Map DESCRIPTORS;
  
  static {
    DESCRIPTORS = new HashMap();
    DESCRIPTORS.put("void", "V");
    DESCRIPTORS.put("byte", "B");
    DESCRIPTORS.put("char", "C");
    DESCRIPTORS.put("double", "D");
    DESCRIPTORS.put("float", "F");
    DESCRIPTORS.put("int", "I");
    DESCRIPTORS.put("long", "J");
    DESCRIPTORS.put("short", "S");
    DESCRIPTORS.put("boolean", "Z");
  }
  
  /**
   * Creates a new {@link Method}.
   *
   * @param name the method's name.
   * @param desc the method's descriptor.
   */

  public Method (final String name, final String desc) {
    this.name = name;
    this.desc = desc;
  }

  /**
   * Creates a new {@link Method}.
   *
   * @param name the method's name.
   * @param returnType the method's return type.
   * @param argumentTypes the method's argument types.
   */

  public Method (
    final String name,
    final Type returnType,
    final Type[] argumentTypes)
  {
    this(name, Type.getMethodDescriptor(returnType, argumentTypes));
  }

  /**
   * Returns a {@link Method} corresponding to the given Java method
   * declaration.
   *
   * @param method a Java method declaration, without argument names, of the
   *     form "returnType name (argumentType1, ... argumentTypeN)", where the
   *     types are in plain Java (e.g. "int", "float", "java.util.List", ...).
   * @return a {@link Method} corresponding to the given Java method
   *     declaration.
   */

  public static Method getMethod (final String method) {
    int space = method.indexOf(' ');
    int start = method.indexOf('(', space) + 1;
    int end = method.indexOf(')', start);
    String returnType = method.substring(0, space);
    String methodName = method.substring(space + 1, start - 1).trim();
    StringBuffer sb = new StringBuffer();
    sb.append('(');
    int p;
    do {
      p = method.indexOf(',', start);
      if (p == -1) {
        map(method.substring(start, end).trim());
      } else {
        map(method.substring(start, p).trim());
        start = p + 1;
      }
    } while (p != -1);
    sb.append(')');
    sb.append(map(returnType));
    return new Method(methodName, sb.toString());
  }

  private static String map (final String type) {
    if (type.equals("")) {
      return type;
    }
    String desc = (String)DESCRIPTORS.get(type);
    if (desc != null) {
      return desc;
    } else if (type.indexOf('.') < 0) {
      return map("java.lang." + type);
    } else {
      StringBuffer sb = new StringBuffer();
      int index = 0;
      while ((index = type.indexOf("[]", index) + 1) > 0) {
        sb.append('[');
      }
      String elementType = type.substring(0, type.length() - sb.length() * 2);
      sb.append('L').append(elementType.replace('.', '/')).append(';');
      return sb.toString();
    }
  }

  /**
   * Returns the name of the method described by this object.
   *
   * @return the name of the method described by this object.
   */

  public String getName () {
    return name;
  }

  /**
   * Returns the descriptor of the method described by this object.
   *
   * @return the descriptor of the method described by this object.
   */

  public String getDescriptor () {
    return desc;
  }

  /**
   * Returns the return type of the method described by this object.
   *
   * @return the return type of the method described by this object.
   */

  public Type getReturnType () {
    return Type.getReturnType(desc);
  }

  /**
   * Returns the argument types of the method described by this object.
   *
   * @return the argument types of the method described by this object.
   */

  public Type[] getArgumentTypes () {
    return Type.getArgumentTypes(desc);
  }

  public String toString () {
    return name + desc;
  }

  public boolean equals (final Object o) {
    if (!(o instanceof Method)) {
      return false;
    }
    Method other = (Method)o;
    return name.equals(other.name) && desc.equals(other.desc);
  }

  public int hashCode () {
    return name.hashCode() ^ desc.hashCode();
  }
}