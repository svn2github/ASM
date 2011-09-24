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

package util;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_1;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import junit.framework.TestCase;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.TraceMethodVisitor;

/**
 * ASM Guide example test class.
 * 
 * @author Eric Bruneton
 */
public class AbstractTestCase extends TestCase {

  private TestClassLoader LOADER = new TestClassLoader();

  public void tests() throws Exception {
    if (getClassAdapter(null) != null) {
      String file = System.getProperty("java.home") + "/lib/rt.jar";
      ZipFile zip = new ZipFile(file);
      Enumeration<? extends ZipEntry> entries = zip.entries();
      while (entries.hasMoreElements()) {
        ZipEntry e = (ZipEntry) entries.nextElement();
        String n = e.getName();
        if (n.endsWith(".class")) {
          n = n.substring(0, n.length() - 6).replace('/', '.');
          InputStream is = zip.getInputStream(e);
          ClassReader cr = new ClassReader(is);
          if ((cr.readInt(4) & 0xFF) < Opcodes.V1_6) {
            try {
              ClassWriter cw = new ClassWriter(
                  ClassWriter.COMPUTE_FRAMES);
              cr.accept(cw, 0);
              cr = new ClassReader(cw.toByteArray());
            } catch (Throwable ignored) {
              continue;
            }
          }
          ClassWriter cw = new ClassWriter(0);
          ClassVisitor cv = getClassAdapter(cw);
          try {
            cr.accept(cv, ClassReader.EXPAND_FRAMES);
          } catch (UnsatisfiedLinkError ignored) {
          } catch (NoClassDefFoundError ignored) {
          }
          byte[] b = cw.toByteArray();
          try {
            new TestClassLoader().defineClass(n, b);
          } catch (ClassFormatError cfe) {
            cfe.printStackTrace();
            fail();
          } catch (Throwable t) {
          }
        }
      }
    }
  }

  protected ClassVisitor getClassAdapter(ClassVisitor cv) {
    return null;
  }

  protected void generateBasicClass(ClassVisitor cv) {
    FieldVisitor fv;
    MethodVisitor mv;
    cv.visit(V1_1, ACC_PUBLIC, "C", null, "java/lang/Object", null);
    cv.visitSource("C.java", null);
    fv = cv.visitField(ACC_PUBLIC, "f", "I", null, null);
    if (fv != null) {
      fv.visitEnd();
    }
    mv = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
    if (mv != null) {
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>",
          "()V");
      mv.visitInsn(RETURN);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    }
    mv = cv.visitMethod(ACC_PUBLIC, "m", "()V", null, null);
    if (mv != null) {
      mv.visitCode();
      mv.visitLdcInsn(new Long(100));
      mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "sleep",
          "(J)V");
      mv.visitInsn(RETURN);
      mv.visitMaxs(3, 1);
      mv.visitEnd();
    }
    cv.visitEnd();
  }

  protected ClassNode generateBasicClass() {
    ClassNode cn = new ClassNode();
    generateBasicClass(cn);
    return cn;
  }

  protected Class<?> defineClass(String name, byte[] b) {
    return LOADER.defineClass(name, b);
  }

  protected void assertEquals(TraceMethodVisitor expected,
      TraceMethodVisitor actual) {
    assertEquals(getText(expected), getText(actual));
  }

  private static String getText(TraceMethodVisitor mv) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mv.p.text.size(); i++) {
      sb.append(mv.p.text.get(i));
    }
    return sb.toString();
  }

  static class TestClassLoader extends ClassLoader {

    public Class<?> defineClass(String name, byte[] b) {
      return defineClass(name, b, 0, b.length);
    }
  }
}
