package de.loskutov.bco.asm;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.AbstractVisitor;

import de.loskutov.bco.preferences.BCOConstants;

/**
 * @author Eric Bruneton
 */

public class DecompilerClassVisitor extends ClassAdapter {

    private final String fieldFilter;

    private final String methodFilter;

    private String name;

    private final List methods;

    private AnnotationVisitor dummyAnnVisitor;

    private String javaVersion;

    private final BitSet modes;

    public DecompilerClassVisitor(final ClassVisitor cv, final String field,
        final String method, final BitSet modes) {
        super(cv);
        this.fieldFilter = field;
        this.methodFilter = method;
        this.modes = modes;
        this.methods = new ArrayList();
    }

    public static DecompiledClass getDecompiledClass(final InputStream is,
        final String field, final String method, final BitSet modes, final ClassLoader cl)
        throws IOException, UnsupportedClassVersionError {
        ClassReader cr = new ClassReader(is);
        int crFlags = 0;
        if(modes.get(BCOConstants.F_EXPAND_STACKMAP)) {
            crFlags |= ClassReader.EXPAND_FRAMES;
        }
        ClassVisitor cv;
        if (modes.get(BCOConstants.F_SHOW_ASMIFIER_CODE)) {
            cv = new CommentedASMifierClassVisitor(modes);
        } else {
            cv = new CommentedClassVisitor(modes);
        }
        DecompilerClassVisitor dcv = new DecompilerClassVisitor(
            cv, field, method, modes);
        cr.accept(dcv, crFlags);
        return dcv.getResult(cl);
    }

    public DecompiledClass getResult(final ClassLoader cl) {
        List text = new ArrayList();
        formatText(((AbstractVisitor) cv).getText(), new StringBuffer(), text, cl);
        while (text.size() > 0 && text.get(0).equals("\n")) {
            text.remove(0);
        }
        DecompiledClass dc = new DecompiledClass(text);
        dc.setAttribute("java.version", javaVersion);
        return dc;
    }

    private void formatText(final List input, StringBuffer line, final List result,
        final ClassLoader cl) {
        for (int i = 0; i < input.size(); ++i) {
            Object o = input.get(i);
            if (o instanceof List) {
                formatText((List) o, line, result, cl);
            } else if (o instanceof DecompilerMethodVisitor) {
                result.add(((DecompilerMethodVisitor) o).getResult(cl));
            } else {
                String s = o.toString();
                int p;
                do {
                    p = s.indexOf('\n');
                    if (p == -1) {
                        line.append(s);
                    } else {
                        result.add(line.toString() + s.substring(0, p + 1));
                        s = s.substring(p + 1);
                        line.setLength(0);
                    }
                } while (p != -1);
            }
        }
    }

    public void visit(final int version, final int access, final String name1,
        final String signature, final String superName,
        final String[] interfaces) {
        if (methodFilter == null && fieldFilter == null) {
            super
                .visit(version, access, name1, signature, superName, interfaces);
        }
        this.name = name1;
        int major = version & 0xFFFF;
        //int minor = version >>> 16;
        // 1.1 is 45, 1.2 is 46 etc.
        int javaV = major % 44;
        if (javaV > 0 && javaV < 10) {
            javaVersion = "1." + javaV; //$NON-NLS-1$
        }
    }

    public void visitSource(final String source, final String debug) {
        if (methodFilter == null && fieldFilter == null) {
            super.visitSource(source, debug);
        }
    }

    public void visitOuterClass(final String owner, final String name1,
        final String desc) {
        if (methodFilter == null && fieldFilter == null) {
            super.visitOuterClass(owner, name1, desc);
        }
    }

    public AnnotationVisitor visitAnnotation(final String desc,
        final boolean visible) {
        if (methodFilter == null && fieldFilter == null) {
            return super.visitAnnotation(desc, visible);
        }
        return getDummyAnnotationVisitor();
    }

    public void visitAttribute(final Attribute attr) {
        if (methodFilter == null && fieldFilter == null) {
            super.visitAttribute(attr);
        }
    }

    public void visitInnerClass(final String name1, final String outerName,
        final String innerName, final int access) {
        if (methodFilter == null && fieldFilter == null) {
            super.visitInnerClass(name1, outerName, innerName, access);
        }
    }

    public FieldVisitor visitField(final int access, final String name1,
        final String desc, final String signature, final Object value) {
        if (methodFilter != null) {
            return null;
        }
        if (fieldFilter != null && !name1.equals(fieldFilter)) {
            return null;
        }
        return super.visitField(access, name1, desc, signature, value);
    }

    public MethodVisitor visitMethod(final int access, final String name1,
        final String desc, final String signature, final String[] exceptions) {
        if (fieldFilter != null) {
            return null;
        }
        if (methodFilter != null && !(name1 + desc).equals(methodFilter)) {
            return null;
        }
        MethodNode meth = null;
        if (modes.get(BCOConstants.F_SHOW_ANALYZER)) {
            meth = new MethodNode(access, name1, desc, signature, exceptions);
        }
        List text = ((AbstractVisitor) cv).getText();
        int size = text.size();
        MethodVisitor mv = cv.visitMethod(
            access, name1, desc, signature, exceptions);
        mv = new DecompilerMethodVisitor(this.name, meth, mv, modes);
        methods.add(mv);
        for (int i = size; i < text.size(); ++i) {
            if (text.get(i) instanceof List) {
                text.set(i, mv);
            }
        }
        return mv;
    }

    public void visitEnd() {
        if (methodFilter == null && fieldFilter == null) {
            super.visitEnd();
        }
    }

    private AnnotationVisitor getDummyAnnotationVisitor(){
        if (dummyAnnVisitor == null) {
            dummyAnnVisitor = new AnnotationVisitor() {
                public void visit(String n, Object value) {
                    /* empty */
                }
                public void visitEnum(String n, String desc, String value) {
                    /* empty */
                }
                public AnnotationVisitor visitAnnotation(String n, String desc) {
                    return this;
                }
                public AnnotationVisitor visitArray(String n) {
                    return this;
                }
                public void visitEnd() {
                    /* empty*/
                }
            };
        }
        return dummyAnnVisitor;
    }
}
