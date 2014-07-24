
package org.objectweb.asmdex;

import org.junit.Test;
import org.ow2.asmdex.AnnotationVisitor;
import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.ApplicationVisitor;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.Opcodes;

/**
 * @author panxiaobo - Bug #316374
 *
 */
public class NullAnnotationVisitorTest {
    /**
     * The test.
     */
    @Test
    public void test() {
        ApplicationWriter aw = new ApplicationWriter();
        ClassVisitor cv = aw.visitClass(0, "a", null, "java/lang/Object", null);
        AnnotationVisitor av = cv.visitAnnotation("LAnno;", true);
        av.visit("a", 1);
        AnnotationVisitor av2 = av.visitArray("btheArray");
        av2.visit(null, "a1");
        av2.visit(null, "a2");
        av2.visitEnd();
        av.visit("c", "c");
        av.visitEnd();
        cv.visitEnd();
        aw.visitEnd();
        ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, aw.toByteArray());
        ar.accept(new ApplicationVisitor(Opcodes.ASM4) {
            @Override
            public ClassVisitor visitClass(int access, String name, String[] signature,
                    String superName, String[] interfaces) {
                return new ClassVisitor(Opcodes.ASM4) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                        return new AnnotationVisitor(Opcodes.ASM4) {
                            @Override
                            public AnnotationVisitor visitArray(String name) {
                                return null; // skip the array
                            }
                        };
                    }
                };
            }
        }, 0);

    }
}
