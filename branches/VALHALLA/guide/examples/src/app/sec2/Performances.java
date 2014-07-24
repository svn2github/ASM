/***
 * ASM Guide
 * Copyright (c) 2007 Eric Bruneton, 2011 Google, 2011 Google
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

package app.sec2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.objectweb.asm.Opcodes.ASM4;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

import ch3.sec2.AddTimerAdapter;
import ch3.sec2.RemoveGetFieldPutFieldAdapter;
import ch6.sec1.AddTimerTransformer;
import ch6.sec1.RemoveGetFieldPutFieldTransformer;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class Performances {

  public static void main(String[] args) throws Exception {
    ZipFile zip = new ZipFile(args[0]);
    String clazz = args.length > 1 ? args[1] : null;

    List<byte[]> classes = new ArrayList<byte[]>();
    Enumeration<? extends ZipEntry> entries = zip.entries();
    while (entries.hasMoreElements()) {
      ZipEntry e = entries.nextElement();
      String s = e.getName();
      if (s.endsWith(".class")) {
        s = s.substring(0, s.length() - 6).replace('/', '.');
        if (clazz == null || s.indexOf(clazz) != -1) {
          InputStream is = zip.getInputStream(e);
          classes.add(new ClassReader(is).b);
        }
      }
    }

    long[] times = new long[15];
    for (int i = 0; i < times.length; ++i) {
      times[i] = Long.MAX_VALUE;
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        new ClassReader(b).accept(new EmptyVisitor(), 0);
      }
      t = System.currentTimeMillis() - t;
      times[0] = Math.min(t, times[0]);
      System.out.println("Time to deserialize " + classes.size()
          + " classes = " + t + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassWriter cw = new ClassWriter(0);
        new ClassReader(b).accept(cw, 0);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[1] = Math.min(t, times[1]);
      System.out.println("Time to deserialize and reserialize "
          + classes.size() + " classes = " + t + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassReader cr = new ClassReader(b);
        ClassWriter cw = new ClassWriter(cr, 0);
        cr.accept(cw, 0);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[2] = Math.min(t, times[2]);
      System.out.println("Time to deserialize and reserialize "
          + classes.size() + " classes with copyPool = " + t + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new AddTimerAdapter(cw);
        new ClassReader(b).accept(cv, 0);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[3] = Math.min(t, times[3]);
      System.out.println("Time to deserialize and reserialize "
          + classes.size() + " classes with AddTimerAdapter = " + t
          + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassReader cr = new ClassReader(b);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new AddTimerAdapter(cw);
        cr.accept(cv, 0);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[4] = Math.min(t, times[4]);
      System.out
          .println("Time to deserialize and reserialize "
              + classes.size()
              + " classes with AddTimerAdapter and copyPool = " + t
              + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassVisitor(ASM4, cw) {
          public MethodVisitor visitMethod(int access, String name,
              String desc, String signature, String[] exceptions) {
            return new RemoveGetFieldPutFieldAdapter(cv.visitMethod(
                access, name, desc, signature, exceptions));
          }
        };
        new ClassReader(b).accept(cv, 0);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[5] = Math.min(t, times[5]);
      System.out.println("Time to deserialize and reserialize "
          + classes.size()
          + " classes with RemoveGetFieldPutFieldAdapter = " + t
          + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassReader cr = new ClassReader(b);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(ASM4, cw) {
          public MethodVisitor visitMethod(int access, String name,
              String desc, String signature, String[] exceptions) {
            return new RemoveGetFieldPutFieldAdapter(cv.visitMethod(
                access, name, desc, signature, exceptions));
          }
        };
        cr.accept(cv, 0);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[6] = Math.min(t, times[6]);
      System.out
          .println("Time to deserialize and reserialize "
              + classes.size()
              + " classes with RemoveGetFieldPutFieldAdapter and copyPool = "
              + t + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        new ClassReader(b).accept(cw, 0);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[7] = Math.min(t, times[7]);
      System.out
          .println("Time to deserialize and reserialize "
              + classes.size() + " classes with computeMaxs = " + t
              + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      int errors = 0;
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        try {
          new ClassReader(b).accept(cw, 0);
        } catch (Throwable e) {
          ++errors;
        }
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[8] = Math.min(t, times[8]);
      System.out.println("Time to deserialize and reserialize "
          + classes.size() + " classes with computeFrames = " + t
          + " ms (" + errors + " errors)");
    }

    System.out.println();

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        new ClassReader(b).accept(new ClassNode(), 0);
      }
      t = System.currentTimeMillis() - t;
      times[9] = Math.min(t, times[9]);
      System.out.println("Time to deserialize " + classes.size()
          + " classes with tree package = " + t + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassWriter cw = new ClassWriter(0);
        ClassNode cn = new ClassNode();
        new ClassReader(b).accept(cn, 0);
        cn.accept(cw);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[10] = Math.min(t, times[10]);
      System.out.println("Time to deserialize and reserialize "
          + classes.size() + " classes with tree package = " + t
          + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassWriter cw = new ClassWriter(0);
        ClassNode cn = new ClassNode();
        new ClassReader(b).accept(cn, 0);
        new AddTimerTransformer(null).transform(cn);
        cn.accept(cw);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[11] = Math.min(t, times[11]);
      System.out.println("Time to deserialize and reserialize "
          + classes.size() + " classes with AddTimerTransformer = " + t
          + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size(); ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassWriter cw = new ClassWriter(0);
        ClassNode cn = new ClassNode();
        new ClassReader(b).accept(cn, 0);
        Iterator<MethodNode> it = cn.methods.iterator();
        while (it.hasNext()) {
          MethodNode mn = (MethodNode) it.next();
          new RemoveGetFieldPutFieldTransformer(null).transform(mn);
        }
        cn.accept(cw);
        cw.toByteArray();
      }
      t = System.currentTimeMillis() - t;
      times[12] = Math.min(t, times[12]);
      System.out.println("Time to deserialize and reserialize "
          + classes.size()
          + " classes with RemoveGetFieldPutFieldTransformer = " + t
          + " ms");
    }

    for (int i = 0; i < 10; ++i) {
      int errors = 0;
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size() / 10; ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassReader cr = new ClassReader(b);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        List<MethodNode> methods = cn.methods;
        for (int k = 0; k < methods.size(); ++k) {
          MethodNode method = (MethodNode) methods.get(k);
          if (method.instructions.size() > 0) {
            Analyzer<BasicValue> a = new Analyzer<BasicValue>(new BasicInterpreter());
            try {
              a.analyze(cn.name, method);
            } catch (Throwable th) {
              ++errors;
            }
          }
        }
      }
      t = System.currentTimeMillis() - t;
      times[13] = Math.min(t, times[13]);
      System.out.println("Time to analyze " + classes.size() / 10
          + " classes with BasicInterpreter = " + t + " ms (" + errors
          + " errors)");
    }

    for (int i = 0; i < 10; ++i) {
      int errors = 0;
      long t = System.currentTimeMillis();
      for (int j = 0; j < classes.size() / 10; ++j) {
        byte[] b = (byte[]) classes.get(j);
        ClassReader cr = new ClassReader(b);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        List<MethodNode> methods = cn.methods;
        for (int k = 0; k < methods.size(); ++k) {
          MethodNode method = (MethodNode) methods.get(k);
          if (method.instructions.size() > 0) {
            Analyzer<BasicValue> a = new Analyzer<BasicValue>(new SimpleVerifier());
            try {
              a.analyze(cn.name, method);
            } catch (Throwable th) {
              ++errors;
            }
          }
        }
      }
      t = System.currentTimeMillis() - t;
      times[14] = Math.min(t, times[14]);
      System.out.println("Time to analyze " + classes.size() / 10
          + " classes with SimpleVerifier = " + t + " ms (" + errors
          + " errors)");
    }

    for (int i = 0; i < times.length; ++i) {
      System.out.println(times[i]);
    }
    System.out.println();

    double base = 100.0 / times[1];
    double read = times[0] * base;
    double write = (times[1] - times[0]) * base;
    System.out.println("base = 100 (including " + read + " for R, "
        + write + " for W)");
    System.out.println("add time = " + times[3] * base + " (including "
        + read + " for R, " + (times[3] - times[1]) * base + " for T, "
        + write + " for W)");
    System.out.println("remove seq = " + times[5] * base
        + " (including " + read + " for R, " + (times[5] - times[1])
        * base + " for T, " + write + " for W)");
    System.out.println();
    System.out.println("base' = " + times[2] * base);
    System.out.println("add time' = " + times[4] * base);
    System.out.println("remove seq' = " + times[6] * base);
    System.out.println();
    System.out.println("compute maxs = " + times[7] * base
        + " (including " + read + " for R, " + (times[7] - times[1])
        * base + " for C, " + write + " for W)");
    System.out.println("compute frames = " + times[8] * base
        + " (including " + read + " for R, " + (times[8] - times[1])
        * base + " for C, " + write + " for W)");
    double tread = times[9] * base;
    double twrite = (times[10] - times[9]) * base;
    System.out.println();
    System.out.println("tree base = " + times[10] * base
        + " (including " + tread + " for R, " + twrite + " for W)");
    System.out.println("tree add time = " + times[11] * base
        + " (including " + tread + " for R, " + (times[11] - times[10])
        * base + " for T, " + twrite + " for W)");
    System.out.println("tree remove seq = " + times[12] * base
        + " (including " + tread + " for R, " + (times[12] - times[10])
        * base + " for T, " + twrite + " for W)");
    System.out.println("basic interpreter = " + times[13] * 10 * base
        + " (including " + tread + " for R, "
        + (times[13] * 10 - times[10]) * base + " for C, " + twrite
        + " for W)");
    System.out.println("simple verifier = " + times[14] * 10 * base
        + " (including " + tread + " for R, "
        + (times[14] * 10 - times[10]) * base + " for C, " + twrite
        + " for W)");
  }

  static class EmptyVisitor extends ClassVisitor {

    AnnotationVisitor av = new AnnotationVisitor(Opcodes.ASM4) {

      @Override
      public AnnotationVisitor visitAnnotation(String name, String desc) {
        return this;
      }

      @Override
      public AnnotationVisitor visitArray(String name) {
        return this;
      }
    };

    public EmptyVisitor() {
      super(ASM4);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return av;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
        String signature, Object value) {
      return new FieldVisitor(Opcodes.ASM4) {

        @Override
        public AnnotationVisitor visitAnnotation(String desc,
            boolean visible) {
          return av;
        }
      };
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
        String signature, String[] exceptions) {
      return new MethodVisitor(Opcodes.ASM4) {

        @Override
        public AnnotationVisitor visitAnnotationDefault() {
          return av;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc,
            boolean visible) {
          return av;
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(
            int parameter, String desc, boolean visible) {
          return av;
        }
      };
    }
  }
}
