/*
 * Copyright area
 */

package org.objectweb.asm.commons;

import java.io.FileInputStream;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.ASMifierClassVisitor;


public class GASMifierClassVisitor extends ASMifierClassVisitor {

  /**
   * Prints the ASM source code to generate the given class to the standard
   * output.
   * <p>
   * Usage: ASMifierClassVisitor [-debug]
   * &lt;fully qualified class name or class file name&gt;
   *
   * @param args the command line arguments.
   *
   * @throws Exception if the class cannot be found, or if an IO exception
   *      occurs.
   */

  public static void main (final String[] args) throws Exception {
    int i = 0;
    boolean skipDebug = true;

    boolean ok = true;
    if (args.length < 1 || args.length > 2) {
      ok = false;
    }
    if (ok && args[0].equals("-debug")) {
      i = 1;
      skipDebug = false;
      if (args.length != 2) {
        ok = false;
      }
    }
    if (!ok) {
      System.err.println("Prints the ASM code to generate the given class.");
      System.err.println("Usage: GASMifierClassVisitor [-debug] " +
                         "<fully qualified class name or class file name>");
      System.exit(-1);
    }
    ClassReader cr;
    if (args[i].endsWith(".class")) {
      cr = new ClassReader(new FileInputStream(args[i]));
    } else {
      cr = new ClassReader(args[i]);
    }
    cr.accept(new GASMifierClassVisitor(
      new PrintWriter(System.out)), getDefaultAttributes(), skipDebug);
  }
  
  public GASMifierClassVisitor (PrintWriter pw) {
    super(pw);
  }

  public void visit (
      final int version,
      final int access,
      final String name,
      final String signature,
      final String superName,
      final String[] interfaces)
  {
    super.visit(version, access, name, signature, superName, interfaces);
    int n;
    if (name.lastIndexOf('/') != -1) {
      n = 1;
    } else {
      n = 0;
    }
    text.set(n+3, "ClassWriter cw = new ClassWriter(true);\n");
    text.set(n+5, "GeneratorAdapter mg;\n");
    text.add(n+1, "import org.objectweb.asm.commons.*;\n");
  }
  
  public MethodVisitor visitMethod (
    final int access,
    final String name,
    final String desc,
    final String signature,
    final String[] exceptions)
  {
    buf.setLength(0);
    buf.append("{\n");
    buf.append("mg = new GeneratorAdapter(");
    buf.append(access);
    buf.append(", ");
    buf.append(GASMifierMethodVisitor.getMethod(name, desc));
    buf.append(", ");
    if (signature == null) {
      buf.append("null");
    } else {
      buf.append('"').append(signature).append('"');
    }
    buf.append(", ");
    if (exceptions != null && exceptions.length > 0) {
      buf.append("new Type[] {");
      for (int i = 0; i < exceptions.length; ++i) {
        buf.append(i == 0 ? " " : ", ");
        buf.append(GASMifierMethodVisitor.getType(exceptions[i]));
      }
      buf.append(" }");
    } else {
      buf.append("null");
    }
    buf.append(", cw);\n");
    text.add(buf.toString());
    GASMifierMethodVisitor acv = new GASMifierMethodVisitor(access, desc);
    text.add(acv.getText());
    text.add("}\n");
    return new LocalVariablesSorter(access, desc, acv);
  }

}
