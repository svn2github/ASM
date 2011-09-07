package de.loskutov.bco.asm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import de.loskutov.bco.preferences.BCOConstants;

/**
 * @author Eric Bruneton
 */

public class CommentedClassVisitor extends Textifier implements ICommentedClassVisitor {

    protected final boolean raw;
    protected final boolean showLines;
    protected final boolean showLocals;
    protected final boolean showStackMap;
    protected final boolean showHex;
    private final DecompilerOptions options;

    private DecompiledMethod currMethod;
    private String className;
    private String javaVersion;
    private int accessFlags;
    private Textifier dummyAnnVisitor;
    private final ClassNode classNode;

    private LabelNode currentLabel;

    private int currentInsn;

    public CommentedClassVisitor(ClassNode classNode, final DecompilerOptions options) {
        super(Opcodes.ASM4);
        this.classNode = classNode;
        this.options = options;
        raw = !options.modes.get(BCOConstants.F_SHOW_RAW_BYTECODE);
        showLines = options.modes.get(BCOConstants.F_SHOW_LINE_INFO);
        showLocals = options.modes.get(BCOConstants.F_SHOW_VARIABLES);
        showStackMap = options.modes.get(BCOConstants.F_SHOW_STACKMAP);
        showHex = options.modes.get(BCOConstants.F_SHOW_HEX_VALUES);
    }

    private boolean decompilingEntireClass() {
        return options.methodFilter == null && options.fieldFilter == null;
    }

    @Override
    public void visit(int version, int access, String name, String signature,
        String superName, String[] interfaces) {
        if(decompilingEntireClass()) {
            super.visit(version, access, name, signature, superName, interfaces);
        }
        this.className = name;
        int major = version & 0xFFFF;
        //int minor = version >>> 16;
        // 1.1 is 45, 1.2 is 46 etc.
        int javaV = major % 44;
        if (javaV > 0 && javaV < 10) {
            javaVersion = "1." + javaV;
        }
        this.accessFlags = access;
    }

    @Override
    public Textifier visitClassAnnotation(String desc, boolean visible) {
        if (decompilingEntireClass()) {
            return super.visitClassAnnotation(desc, visible);
        }
        return getDummyVisitor();
    }

    @Override
    public void visitClassAttribute(Attribute attr) {
        if (decompilingEntireClass()) {
            super.visitClassAttribute(attr);
        }
    }

    @Override
    public void visitClassEnd() {
        if (decompilingEntireClass()) {
            super.visitClassEnd();
        }
    }

    @Override
    public Textifier visitField(int access, String name, String desc,
        String signature, Object value) {
        if (options.methodFilter != null) {
            return getDummyVisitor();
        }
        if (options.fieldFilter != null && !name.equals(options.fieldFilter)) {
            return getDummyVisitor();
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitInnerClass(String name, String outerName,
        String innerName, int access) {
        if (decompilingEntireClass()) {
            super.visitInnerClass(name, outerName, innerName, access);
        }
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        if (decompilingEntireClass()) {
            super.visitOuterClass(owner, name, desc);
        }
    }

    @Override
    public void visitSource(String file, String debug) {
        if (decompilingEntireClass()) {
            super.visitSource(file, debug);
        }
    }

    @Override
    public Textifier visitMethod(int access, String name, String desc,
        String signature, String[] exceptions) {
        if(options.fieldFilter != null || options.methodFilter != null && !options.methodFilter.equals(name + desc)) {
            return getDummyVisitor();
        }

        MethodNode meth = null;
        List<String> exList = Arrays.asList(exceptions);
        for (MethodNode mn : classNode.methods) {
            if(mn.name.equals(name) && mn.desc.equals(desc) && mn.exceptions.equals(exList)) {
                meth = mn;
                break;
            }
        }
        assert meth != null;

        currMethod = new DecompiledMethod(className, new HashMap(), meth, options, access);
        Textifier textifier = super.visitMethod(access, name, desc, signature, exceptions);
        TraceMethodVisitor tm = new TraceMethodVisitor(textifier);
        meth.accept(tm);

        Object methodtext = text.remove(text.size() - 1);
        currMethod.setText((List) methodtext);
        text.add(currMethod);
        return textifier;
    }

    @Override
    protected void appendDescriptor(final int type, final String desc) {
        appendDescriptor(buf, type, desc, raw);
    }

    protected void appendDescriptor(final StringBuffer buf1, final int type,
        final String desc, final boolean raw1) {
        if (desc == null) {
            return;
        }
        if (raw1) {
            if (type == CLASS_SIGNATURE || type == FIELD_SIGNATURE
                || type == METHOD_SIGNATURE) {
                buf1.append("// signature ").append(desc).append('\n');
            } else {
                buf1.append(desc);
            }
        } else {
            switch (type) {
                case INTERNAL_NAME :
                    buf1.append(eatPackageNames(desc, '/'));
                    break;
                case FIELD_DESCRIPTOR :
                    if ("T".equals(desc)) {
                        buf1.append("top");
                    } else if ("N".equals(desc)) {
                        buf1.append("null");
                    } else if ("U".equals(desc)) {
                        buf1.append("uninitialized_this");
                    } else {
                        buf1.append(getSimpleName(Type.getType(desc)));
                    }
                    break;
                case METHOD_DESCRIPTOR :
                    Type[] args = Type.getArgumentTypes(desc);
                    Type res = Type.getReturnType(desc);
                    buf1.append('(');
                    for (int i = 0; i < args.length; ++i) {
                        if (i > 0) {
                            buf1.append(',');
                        }
                        buf1.append(getSimpleName(args[i]));
                    }
                    buf1.append(") : ");
                    buf1.append(getSimpleName(res));
                    break;

                case METHOD_SIGNATURE :
                case FIELD_SIGNATURE :
                    // fine tuning of identation - we have two tabs in this case
                    if (buf.lastIndexOf(tab) == buf.length() - tab.length()) {
                        buf.delete(buf.lastIndexOf(tab), buf.length());
                    }
                    break;

                case CLASS_SIGNATURE :
                    // ignore - show only in "raw" mode
                    break;
                case TYPE_DECLARATION :
                    buf1.append(eatPackageNames(desc, '.'));
                    break;
                case CLASS_DECLARATION :
                    buf1.append(eatPackageNames(desc, '.'));
                    break;
                case PARAMETERS_DECLARATION :
                    buf1.append(eatPackageNames(desc, '.'));
                    break;
                default :
                    buf1.append(desc);
            }
        }
    }

    /**
     * @param t
     * @return simply class name without any package/outer class information
     */
    public static String getSimpleName(Type t) {
        String name = t.getClassName();
        return eatPackageNames(name, '.');
    }

    /**
     * @param name Java type name(s).
     * @return simply class name(s) without any package/outer class information, but with
     * "generics" information from given name parameter.
     */
    private static String eatPackageNames(String name, char separator) {
        int lastPoint = name.lastIndexOf(separator);
        if (lastPoint < 0) {
            return name;
        }
        StringBuffer sb = new StringBuffer(name);
        do {
            int start = getPackageStartIndex(sb, separator, lastPoint);
            sb.delete(start, lastPoint + 1);
            lastPoint = lastIndexOf(sb, separator, start);
        } while (lastPoint > 0);

        return sb.toString();
    }

    private static int lastIndexOf(StringBuffer chars, char c, int lastPoint) {
        for (int i = lastPoint - 1; i > 0; i--) {
            if (chars.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    private static int getPackageStartIndex(StringBuffer chars, char c,
        int firstPoint) {
        for (int i = firstPoint - 1; i >= 0; i--) {
            char curr = chars.charAt(i);
            if (curr != c && !Character.isJavaIdentifierPart(curr)) {
                return i + 1;
            }
        }
        return 0;
    }


    /**
     * control chars names
     */
    private static final String[] CHAR_NAMES = {"NUL", "SOH", "STX", "ETX",
        "EOT", "ENQ", "ACK", "BEL", "BS", "HT", "LF", "VT", "FF", "CR", "SO",
        "SI", "DLE", "DC1", "DC2", "DC3", "DC4", "NAK", "SYN", "ETB", "CAN",
        "EM", "SUB", "ESC", "FS", "GS", "RS", "US", // "Sp"
    };

    private Index getIndex(Label label) {
        Index index;
        for (int i = 0; i < text.size(); i++) {
            Object o = text.get(i);
            if (o instanceof Index) {
                index = (Index) o;
                if (index.labelNode != null
                    && index.labelNode.getLabel() == label) {
                    return index;
                }
            }
        }
        return null;
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local,
        int nStack, Object[] stack) {
        if (showStackMap) {
            addIndex(-1);
            super.visitFrame(type, nLocal, local, nStack, stack);
        }
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner,
        final String name, final String desc) {
        addIndex(opcode);
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(INTERNAL_NAME, owner);
        buf.append('.').append(name);
        appendDescriptor(METHOD_DESCRIPTOR, desc);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        addIndex(opcode);
        text.add(tab2 + OPCODES[opcode] + " " + var);
        if (!raw) {
            text.add(new Integer(var));
        }
        text.add("\n");
    }

    @Override
    public void visitLabel(Label label) {
        addIndex(-1);
        buf.setLength(0);
        buf.append(ltab);
        appendLabel(label);
        Index index = getIndex(label);
        if (index != null) {
            buf.append(" (").append(index.insn).append(")");
        }
        buf.append('\n');
        text.add(buf.toString());
        InsnList instructions = currMethod.meth.instructions;
        LabelNode currLabel = null;
        for (int i = 0; i < instructions.size(); i++) {
            AbstractInsnNode insnNode = instructions.get(i);
            if(insnNode instanceof LabelNode) {
                LabelNode labelNode = (LabelNode) insnNode;
                if(labelNode.getLabel() == label) {
                    currLabel = labelNode;
                }
            }
        }
        setCurrentLabel(currLabel);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm,
        Object... bsmArgs) {
        addIndex(Opcodes.INVOKEDYNAMIC);
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        addIndex(Opcodes.IINC);
        text.add(tab2 + "IINC " + var);
        if (!raw) {
            text.add(new Integer(var));
        }
        text.add(" " + increment + "\n");
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        addIndex(opcode);
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ').append(
            opcode == Opcodes.NEWARRAY
            ? TYPES[operand]
                : formatValue(operand)).append('\n');
        text.add(buf.toString());
    }

    private String formatValue(int operand) {
        if (showHex) {
            String intStr = Integer.toHexString(operand).toUpperCase();
            return intStr + getAsCharComment(operand);
        }
        return Integer.toString(operand);
    }

    /**
     * @param value
     * @return char value from int, together with char name if it is a control char,
     * or an empty string
     */
    private static String getAsCharComment(int value) {
        if (Character.MAX_VALUE < value || Character.MIN_VALUE > value) {
            return "";
        }
        StringBuffer sb = new StringBuffer("    // '");
        switch (value) {
            case '\t' :
                sb.append("\\t");
                break;
            case '\r' :
                sb.append("\\r");
                break;
            case '\n' :
                sb.append("\\n");
                break;
            case '\f' :
                sb.append("\\f");
                break;
            default :
                sb.append((char) value);
                break;
        }

        if (value >= CHAR_NAMES.length) {
            if (value == 127) {
                return sb.append("' (DEL)").toString();
            }
            return sb.append("'").toString();
        }
        return sb.append("' (").append(CHAR_NAMES[value]).append(")")
            .toString();
    }

    private String formatValue(Object operand) {
        if (operand == null) {
            return "null";
        }
        if (showHex) {
            if (operand instanceof Integer) {
                String intStr = Integer.toHexString(
                    ((Integer) operand).intValue()).toUpperCase();
                return intStr
                    + getAsCharComment(((Integer) operand).intValue());
            } else if (operand instanceof Long) {
                return Long.toHexString(((Long) operand).longValue())
                    .toUpperCase();
            } else if (operand instanceof Double) {
                return Double.toHexString(((Double) operand).doubleValue());
            } else if (operand instanceof Float) {
                return Float.toHexString(((Float) operand).floatValue());
            }
        }
        return operand.toString();
    }

    @Override
    public void visitLocalVariable(final String name, final String desc,
        final String signature, final Label start, final Label end,
        final int index) {
        if (showLocals) {
            super.visitLocalVariable(
                name, desc, signature, start, end, index);
        }
    }

    @Override
    public void visitLdcInsn(final Object cst) {
        addIndex(Opcodes.LDC);
        buf.setLength(0);
        buf.append(tab2).append("LDC ");
        if (cst instanceof String) {
            Printer.appendString(buf, (String) cst);
        } else if (cst instanceof Type) {
            buf.append(((Type) cst).getDescriptor() + ".class");
        } else {
            buf.append(formatValue(cst));
        }
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        if (showLocals) {
            super.visitMaxs(maxStack, maxLocals);
        }
    }

    @Override
    public void visitInsn(final int opcode) {
        addIndex(opcode);
        super.visitInsn(opcode);
    }

    @Override
    public void visitTypeInsn(final int opcode, final String desc) {
        addIndex(opcode);
        super.visitTypeInsn(opcode, desc);
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner1,
        final String name, final String desc) {
        addIndex(opcode);
        super.visitFieldInsn(opcode, owner1, name, desc);
    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        addIndex(opcode);
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitTableSwitchInsn(final int min, final int max,
        final Label dflt, final Label... labels) {
        addIndex(Opcodes.TABLESWITCH);
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys,
        final Label[] labels) {
        addIndex(Opcodes.LOOKUPSWITCH);
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        addIndex(Opcodes.MULTIANEWARRAY);
        super.visitMultiANewArrayInsn(desc, dims);
    }


    @Override
    public void visitLineNumber(final int line, final Label start) {
        if (showLines) {
            addIndex(-1);
            currMethod.addLineNumber(start, new Integer(line));
            super.visitLineNumber(line, start);
        }
    }

    private void addIndex(final int opcode) {
        text.add(new Index(currentLabel, currentInsn++, opcode));
    }

    void setCurrentLabel(LabelNode currentLabel) {
        this.currentLabel = currentLabel;
    }

    @Override
    protected Textifier createTextifier() {
        CommentedClassVisitor classVisitor = new CommentedClassVisitor(classNode, options);
        classVisitor.currMethod = currMethod;
        return classVisitor;
    }

    @Override
    public DecompiledClassInfo getClassInfo() {
        return new DecompiledClassInfo(javaVersion, accessFlags);
    }

    private Textifier getDummyVisitor(){
        if (dummyAnnVisitor == null) {
            dummyAnnVisitor = new Textifier(Opcodes.ASM4) {
                @Override
                public void visitAnnotationEnd() {
                    text.clear();
                }

                @Override
                public void visitClassEnd() {
                    text.clear();
                }

                @Override
                public void visitFieldEnd() {
                    text.clear();
                }

                @Override
                public void visitMethodEnd() {
                    text.clear();
                }
            };
        }
        return dummyAnnVisitor;
    }
}
