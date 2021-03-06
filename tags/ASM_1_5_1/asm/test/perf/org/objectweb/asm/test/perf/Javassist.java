/***
 * ASM performance test: measures the performances of asm package
 * Copyright (c) 2002,2003 France Telecom
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

package org.objectweb.asm.test.perf;

import java.io.InputStream;
import java.lang.reflect.Modifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

/**
 * @author Eric Bruneton
 */

public class Javassist extends ALL {

  public static void main (final String args[]) throws Exception {
    System.out.println("Javassist PERFORMANCES\n");
    new Javassist().perfs(args);
  }

  ClassPool pool;
  
  public Javassist () {
    pool = new ClassPool(null);
  }
  
  ALL newInstance () {
    return new Javassist();
  }

  byte[] nullAdaptClass (final InputStream is, final String name)
    throws Exception
  {
    CtClass cc = pool.makeClass(is);
    CtMethod[] ms = cc.getDeclaredMethods();
    for (int j = 0; j < ms.length; ++j) {
      if (skipDebug) {
        // is there a mean to remove the debug attributes?
      }
      if (compute) {
        // is there a mean to force recomputation of maxStack and maxLocals?
      }
    }
    return cc.toBytecode();
  }

  byte[] counterAdaptClass (final InputStream is, final String name)
    throws Exception
  {
    CtClass cc = pool.makeClass(is);
    if (!cc.isInterface()) {
      cc.addField(new CtField(CtClass.intType, "_counter", cc));
    }
    CtMethod[] ms = cc.getDeclaredMethods();
    for (int j = 0; j < ms.length; ++j) {
      CtMethod m = ms[j];
      int modifiers = m.getModifiers();
      if (!Modifier.isStatic(modifiers) && !Modifier.isAbstract(modifiers) && !Modifier.isNative(modifiers)) {
        if (!m.isEmpty()) {
          MethodInfo info = m.getMethodInfo();
          Bytecode bc = new Bytecode(info.getConstPool(), 1, 0);
          bc.addAload(0);
          bc.addAload(0);
          bc.addGetfield(cc, "_counter", "I");
          bc.add(Bytecode.ICONST_1);
          bc.add(Bytecode.IADD);
          bc.addPutfield(cc, "_counter", "I");
          CodeIterator iter = info.getCodeAttribute().iterator();
          iter.begin();
          iter.insert(bc.get());
        }
      }
    }
    return cc.toBytecode();
  }
}
