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

package ch3.sec2;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.V1_5;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class ProxyGenerator {

  private static Type CLASS = Type.getType(Class.class);

  private static Type METHOD = Type
      .getType(java.lang.reflect.Method.class);

  private static Type PROXY = Type.getType(Proxy.class);

  private static Type HANDLER = Type.getType(InvocationHandler.class);

  private static Method CLINIT = Method.getMethod("void <clinit>()");

  private static Method INIT = Method
      .getMethod("void <init>(java.lang.reflect.InvocationHandler)");

  private static Method INVOKE = Method
      .getMethod("java.lang.Object invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])");

  private static Method GET_METHOD = Method
      .getMethod("java.lang.reflect.Method getMethod(java.lang.String, java.lang.Class[])");

  private Class<?> itf;

  public ProxyGenerator(Class<?> itf) {
    this.itf = itf;
  }

  public void generate(Type type, ClassVisitor cv) {
    cv.visit(V1_5, ACC_PUBLIC, type.getInternalName(), null, PROXY
        .getInternalName(), new String[] { Type.getType(itf)
        .getInternalName() });

    MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, INIT.getName(), INIT
        .getDescriptor(), null, null);
    if (mv != null) {
      GeneratorAdapter init = new GeneratorAdapter(ACC_PUBLIC, INIT, mv);
      init.visitCode();
      init.loadThis();
      init.loadArg(0);
      init.invokeConstructor(PROXY, INIT);
      init.returnValue();
      init.endMethod();
    }

    GeneratorAdapter clinit = null;
    mv = cv.visitMethod(ACC_STATIC, CLINIT.getName(), CLINIT
        .getDescriptor(), null, null);
    if (mv != null) {
      clinit = new GeneratorAdapter(ACC_STATIC, CLINIT, mv);
      clinit.visitCode();
    }

    for (int i = 0; i < itf.getMethods().length; ++i) {
      Method m = getMethod(itf.getMethods()[i]);

      mv = cv.visitMethod(ACC_PUBLIC, m.getName(), m.getDescriptor(),
          null, null);
      if (mv != null) {
        String field = "_M" + i;
        FieldVisitor fv = cv.visitField(ACC_PRIVATE + ACC_STATIC,
            field, METHOD.getDescriptor(), null, null);
        if (fv != null) {
          fv.visitEnd();
        }

        if (clinit != null) {
          Type[] formals = m.getArgumentTypes();
          clinit.push(Type.getType(itf));
          clinit.push(m.getName());
          clinit.push(formals.length);
          clinit.newArray(CLASS);
          for (int j = 0; j < formals.length; ++j) {
            clinit.dup();
            clinit.push(j);
            clinit.push(formals[j]);
            clinit.arrayStore(CLASS);
          }
          clinit.invokeVirtual(CLASS, GET_METHOD);
          clinit.putStatic(type, field, METHOD);
        }

        GeneratorAdapter ga = new GeneratorAdapter(ACC_PUBLIC, m, mv);
        ga.visitCode();
        ga.loadThis();
        ga.getField(PROXY, "h", HANDLER);
        ga.loadThis();
        ga.getStatic(type, field, METHOD);
        ga.loadArgArray();
        ga.invokeInterface(HANDLER, INVOKE);
        if (m.getReturnType() != Type.VOID_TYPE) {
          ga.unbox(m.getReturnType());
        }
        ga.returnValue();
        ga.endMethod();
      }
    }

    if (clinit != null) {
      clinit.returnValue();
      clinit.endMethod();
    }

    cv.visitEnd();
  }

  private static Method getMethod(java.lang.reflect.Method m) {
    Type returnType = Type.getType(m.getReturnType());
    Type[] argTypes = new Type[m.getParameterTypes().length];
    for (int i = 0; i < argTypes.length; ++i) {
      argTypes[i] = Type.getType(m.getParameterTypes()[i]);
    }
    return new Method(m.getName(), returnType, argTypes);
  }
}
