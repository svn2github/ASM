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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;

import util.AbstractTestCase;

/**
 * ASM Guide example test class.
 * 
 * @author Eric Bruneton
 */
public class ProxyGeneratorTest extends AbstractTestCase {

  private Object p;

  private Method m;

  private Object[] args;

  public void test() throws Throwable {
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    CheckClassAdapter ca = new CheckClassAdapter(cw, false);
    ProxyGenerator pg = new ProxyGenerator(Comparable.class);
    pg.generate(Type.getType("LC;"), ca);

    InvocationHandler handler = new InvocationHandler() {
      public Object invoke(Object proxy, Method method, Object[] args)
          throws Throwable {
        ProxyGeneratorTest.this.p = proxy;
        ProxyGeneratorTest.this.m = method;
        ProxyGeneratorTest.this.args = args;
        return 1;
      }
    };

    @SuppressWarnings("unchecked")
	Class< Comparable<Integer> > c = (Class< Comparable<Integer> >) defineClass("C", cw.toByteArray());
    Constructor< Comparable<Integer> > ct = c.getConstructor(InvocationHandler.class);
    Comparable<Integer> o = ct.newInstance(handler);
    int result = o.compareTo(new Integer(123));

    assertEquals(o, p);
    assertEquals(Comparable.class.getMethod("compareTo", Object.class),
        m);
    assertNotNull(args);
    assertEquals(1, args.length);
    assertEquals(new Integer(123), args[0]);
    assertEquals(1, result);
  }
}
