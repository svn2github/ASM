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

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import util.AbstractTestCase;

/**
 * ASM Guide example test class.
 * 
 * @author Eric Bruneton
 */
public class BeanGeneratorTest extends AbstractTestCase {

  public void test() throws Exception {
    BeanGenerator cg = new BeanGenerator();
    PrintWriter pw = new PrintWriter(System.out, true);
    byte[] b = cg.generate(pw);
    Class<?> c = defineClass("pkg.Bean", b);
    checkClass(c);
  }

  protected void checkClass(Class<?> c) throws Exception {
    Object bean = c.newInstance();
    Method getF = c.getMethod("getF");
    Method setF = c.getMethod("setF", int.class);
    Method checkAndSetF = c.getMethod("checkAndSetF", int.class);
    setF.invoke(bean, 1);
    assertEquals(1, getF.invoke(bean));
    checkAndSetF.invoke(bean, 2);
    assertEquals(2, getF.invoke(bean));
    try {
      checkAndSetF.invoke(bean, -1);
      fail();
    } catch (InvocationTargetException ite) {
      Throwable t = ite.getTargetException();
      if (!(t instanceof IllegalArgumentException)) {
        fail();
      }
    }
  }
}
