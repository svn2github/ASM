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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;

import util.AbstractTestCase;

/**
 * ASM Guide example test class.
 * 
 * @author Eric Bruneton
 */
public class BeanGenerator3Test extends AbstractTestCase {

  public void test() throws Exception {
    Map<String, Type> fields = new HashMap<String, Type>();
    fields.put("f0", Type.BOOLEAN_TYPE);
    fields.put("f1", Type.INT_TYPE);
    fields.put("f2", Type.LONG_TYPE);
    fields.put("f3", Type.FLOAT_TYPE);
    fields.put("f4", Type.DOUBLE_TYPE);
    fields.put("f5", Type.getType("Ljava/lang/String;"));
    fields.put("f6", Type.getType("[I"));
    BeanGenerator3 bg = new BeanGenerator3("MyBean", fields);

    ClassWriter cw = new ClassWriter(0);
    CheckClassAdapter ca = new CheckClassAdapter(cw);
    bg.generate(ca);

    Class<?> c = defineClass("MyBean", cw.toByteArray());
    checkClass(c);
  }

  protected void checkClass(Class<?> c) throws Exception {
    Object bean = c.newInstance();
    Method getF0 = c.getMethod("getF0");
    Method setF0 = c.getMethod("setF0", boolean.class);
    setF0.invoke(bean, true);
    assertEquals(true, getF0.invoke(bean));

    Method getF1 = c.getMethod("getF1");
    Method setF1 = c.getMethod("setF1", int.class);
    setF1.invoke(bean, 1);
    assertEquals(1, getF1.invoke(bean));

    Method getF2 = c.getMethod("getF2");
    Method setF2 = c.getMethod("setF2", long.class);
    setF2.invoke(bean, 1L);
    assertEquals(1L, getF2.invoke(bean));

    Method getF3 = c.getMethod("getF3");
    Method setF3 = c.getMethod("setF3", float.class);
    setF3.invoke(bean, 1f);
    assertEquals(1f, getF3.invoke(bean));

    Method getF4 = c.getMethod("getF4");
    Method setF4 = c.getMethod("setF4", double.class);
    setF4.invoke(bean, 1d);
    assertEquals(1d, getF4.invoke(bean));

    Method getF5 = c.getMethod("getF5");
    Method setF5 = c.getMethod("setF5", String.class);
    setF5.invoke(bean, "1");
    assertEquals("1", getF5.invoke(bean));

    Method getF6 = c.getMethod("getF6");
    Method setF6 = c.getMethod("setF6", int[].class);
    setF6.invoke(bean, new Object[] { new int[] { 1 } });
    int[] v = (int[]) getF6.invoke(bean);
    assertNotNull(v);
    assertEquals(1, v.length);
    assertEquals(1, v[0]);
  }
}
