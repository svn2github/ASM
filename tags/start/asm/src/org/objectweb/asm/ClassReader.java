/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (C) 2000 INRIA, France Telecom
 * Copyright (C) 2002 France Telecom
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Eric.Bruneton@rd.francetelecom.com
 *
 * Author: Eric Bruneton
 */

package org.objectweb.asm;

import java.io.InputStream;
import java.io.IOException;

/**
 * A Java class parser to make a {@link ClassVisitor ClassVisitor} visit an
 * existing class. This class parses a byte array conforming to the Java class
 * file format and calls the appropriate visit methods of a given class visitor
 * for each field, method and bytecode instruction encountered.
 */

public class ClassReader {

  /**
   * The class to be parsed.
   */

  private final byte[] b;

  /**
   * The start index of each constant pool item in {@link #b b}, plus one. The
   * one byte offset skips the constant pool item tag that indicates its type.
   */

  private int[] items;

  /**
   * The String objects corresponding to the CONSTANT_Utf8 items. This cache
   * avoids multiple parsing of a given CONSTANT_Utf8 constant pool item, which
   * GREATLY improves performances (by a factor 2 to 3). This caching strategy
   * could be extended to all constant pool items, but its benefit would not be
   * so great for these items (because they are much less expensive to parse
   * than CONSTANT_Utf8 items).
   */

  private String[] strings;

  /**
   * A common buffer used to parse strings in the constant pool. This common
   * buffer eliminates the need to create a new temporary buffer each time
   * readUTF8 is called, and therefore improves performances.
   */

  private char[] buf;

  /**
   * Start index of the class header information (access, name...) in {@link #b
   * b}.
   */

  private int header;

  /**
   * The type of instructions without any operand.
   */

  private final static int NOARG_INSN = 0;

  /**
   * The type of instructions with an signed byte operand.
   */

  private final static int SBYTE_INSN = 1;

  /**
   * The type of instructions with an signed short operand.
   */

  private final static int SHORT_INSN = 2;

  /**
   * The type of instructions with a local variable index operand.
   */

  private final static int VAR_INSN = 3;

  /**
   * The type of instructions with an implicit local variable index operand.
   */

  private final static int IMPLVAR_INSN = 4;

  /**
   * The type of instructions with a type descriptor argument.
   */

  private final static int TYPE_INSN = 5;

  /**
   * The type of field and method invocations instructions.
   */

  private final static int FIELDORMETH_INSN = 6;

  /**
   * The type of the INVOKEINTERFACE instruction.
   */

  private final static int ITFMETH_INSN = 7;

  /**
   * The type of instructions with a 2 bytes bytecode offset operand.
   */

  private final static int LABEL_INSN = 8;

  /**
   * The type of instructions with a 4 bytes bytecode offset operand.
   */

  private final static int LABELW_INSN = 9;

  /**
   * The type of the LDC instruction.
   */

  private final static int LDC_INSN = 10;

  /**
   * The type of the LDC_W and LDC2_W instructions.
   */

  private final static int LDCW_INSN = 11;

  /**
   * The type of the IINC instruction.
   */

  private final static int IINC_INSN = 12;

  /**
   * The type of the TABLESWITCH instruction.
   */

  private final static int TABL_INSN = 13;

  /**
   * The type of the LOOKUPSWITCH instruction.
   */

  private final static int LOOK_INSN = 14;

  /**
   * The type of the MULTIANEWARRAY instruction.
   */

  private final static int MANA_INSN = 15;

  /**
   * The type of the WIDE instruction.
   */

  private final static int WIDE_INSN = 16;

  /**
   * The instruction types of all JVM opcodes.
   */

  private static byte[] TYPE;

  // --------------------------------------------------------------------------
  // Static initializer
  // --------------------------------------------------------------------------

  /**
   * Computes the instruction types of JVM opcodes.
   */

  static {
    int i;
    byte[] b = new byte[202];
    String s =
      "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEE" +
      "EEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAA" +
      "AAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHAFBFAAFFAAQPIIJJ";
    for (i = 0; i < b.length; ++i) {
      b[i] = (byte)(s.charAt(i) - 'A');
    }
    TYPE = b;

    /* code to generate the above string

    // SBYTE_INSN instructions
    b[Constants.NEWARRAY] = SBYTE_INSN;
    b[Constants.BIPUSH] = SBYTE_INSN;

    // SHORT_INSN instructions
    b[Constants.SIPUSH] = SHORT_INSN;

    // (IMPL)VAR_INSN instructions
    b[Constants.RET] = VAR_INSN;
    for (i = Constants.ILOAD; i <= Constants.ALOAD; ++i) {
      b[i] = VAR_INSN;
    }
    for (i = Constants.ISTORE; i <= Constants.ASTORE; ++i) {
      b[i] = VAR_INSN;
    }
    for (i = 26; i <= 45; ++i) { // ILOAD_0 to ALOAD_3
      b[i] = IMPLVAR_INSN;
    }
    for (i = 59; i <= 78; ++i) { // ISTORE_0 to ASTORE_3
      b[i] = IMPLVAR_INSN;
    }

    // TYPE_INSN instructions
    b[Constants.NEW] = TYPE_INSN;
    b[Constants.ANEWARRAY] = TYPE_INSN;
    b[Constants.CHECKCAST] = TYPE_INSN;
    b[Constants.INSTANCEOF] = TYPE_INSN;

    // (Set)FIELDORMETH_INSN instructions
    for (i = Constants.GETSTATIC; i <= Constants.INVOKESTATIC; ++i) {
      b[i] = FIELDORMETH_INSN;
    }
    b[Constants.INVOKEINTERFACE] = ITFMETH_INSN;

    // LABEL(W)_INSN instructions
    for (i = Constants.IFEQ; i <= Constants.JSR; ++i) {
      b[i] = LABEL_INSN;
    }
    b[Constants.IFNULL] = LABEL_INSN;
    b[Constants.IFNONNULL] = LABEL_INSN;
    b[200] = LABELW_INSN; // GOTO_W
    b[201] = LABELW_INSN; // JSR_W

    // LDC(_W) instructions
    b[Constants.LDC] = LDC_INSN;
    b[19] = LDCW_INSN; // LDC_W
    b[20] = LDCW_INSN; // LDC2_W

    // special instructions
    b[Constants.IINC] = IINC_INSN;
    b[Constants.TABLESWITCH] = TABL_INSN;
    b[Constants.LOOKUPSWITCH] = LOOK_INSN;
    b[Constants.MULTIANEWARRAY] = MANA_INSN;
    b[196] = WIDE_INSN; // WIDE

    for (i = 0; i < b.length; ++i) {
      System.err.print((char)('A' + b[i]));
    }
    System.err.println();
    */
  }

  // --------------------------------------------------------------------------
  // Constructors
  // --------------------------------------------------------------------------

  /**
   * Constructs a new {@link ClassReader ClassReader} object.
   *
   * @param b the bytecode of the class to be read.
   */

  public ClassReader (final byte[] b) {
    this.b = b;
    // parses the constant pool
    items = new int[readUnsignedShort(8)];
    strings = new String[items.length];
    int max = 0;
    int index = 10;
    for (int i = 1; i < items.length; ++i) {
      items[i] = index + 1;
      int tag = b[index];
      int size;
      switch (tag) {
        case ClassWriter.FIELD:
        case ClassWriter.METH:
        case ClassWriter.IMETH:
        case ClassWriter.INT:
        case ClassWriter.FLOAT:
        case ClassWriter.NAME_TYPE:
          size = 5;
          break;
        case ClassWriter.LONG:
        case ClassWriter.DOUBLE:
          size = 9;
          ++i;
          break;
        case ClassWriter.UTF8:
          size = 3 + readUnsignedShort(index + 1);
          max = (size > max ? size : max);
          break;
        //case ClassWriter.CLASS:
        //case ClassWriter.STR:
        default:
          size = 3;
          break;
      }
      index += size;
    }
    buf = new char[max];
    // the class header information starts just after the constant pool
    header = index;
  }

  /**
   * Constructs a new {@link ClassReader ClassReader} object.
   *
   * @param is an input stream from which to read the class.
   * @throws IOException if a problem occurs during reading.
   */

  public ClassReader (final InputStream is) throws IOException {
    this(readClass(is));
  }

  /**
   * Constructs a new {@link ClassReader ClassReader} object.
   *
   * @param name the fully qualified name of the class to be read.
   * @throws IOException if an exception occurs during reading.
   */

  public ClassReader (final String name) throws IOException {
    this(ClassLoader.getSystemResourceAsStream(
      name.replace('.','/') + ".class"));
  }

  /**
   * Reads the bytecode of a class.
   *
   * @param is an input stream from which to read the class.
   * @return the bytecode read from the given input stream.
   * @throws IOException if a problem occurs during reading.
   */

  private static byte[] readClass (final InputStream is) throws IOException {
    if (is == null) {
      throw new IOException("Class not found");
    }
    byte[] b = new byte[is.available()];
    int len = 0;
    while (true) {
      int n = is.read(b, len, b.length - len);
      if (n == -1) {
        if (len < b.length) {
          byte[] c = new byte[len];
          System.arraycopy(b, 0, c, 0, len);
          b = c;
        }
        return b;
      } else {
        len += n;
        if (len == b.length) {
          byte[] c = new byte[b.length + 1000];
          System.arraycopy(b, 0, c, 0, len);
          b = c;
        }
      }
    }
  }

  // --------------------------------------------------------------------------
  // Public methods
  // --------------------------------------------------------------------------

  /**
   * Makes the given visitor visit the Java class of this {@link ClassReader
   * ClassReader}. This class is the one specified in the constructor (see
   * {@link #ClassReader ClassReader}).
   *
   * @param classVisitor the visitor that must visit this class.
   * @param skipDebug <tt>true</tt> if the debug information of the class must
   *      not be visited. In this case the {@link CodeVisitor#visitLocalVariable
   *      visitLocalVariable} and {@link CodeVisitor#visitLineNumber} methods
   *      will not be called.
   */

  public void accept (
    final ClassVisitor classVisitor,
    final boolean skipDebug)
  {
    byte[] b = this.b;  // the bytecode array
    int i, j, k;        // loop variables
    int u, v, w;        // indexes in b

		// visits the header
    u = header;
    int access = readUnsignedShort(u);
    String className = readClass(u + 2);
    String superClassName = readClass(u + 4);
    String[] implementedItfs = new String[readUnsignedShort(u + 6)];
    String sourceFile = null;
    w = 0;
    u += 8;
    for (i = 0; i < implementedItfs.length; ++i) {
      implementedItfs[i] = readClass(u); u += 2;
    }
    // skips fields and methods
    v = u;
    i = readUnsignedShort(v); v += 2;
    for ( ; i > 0; --i) {
      j = readUnsignedShort(v + 6);
      v += 8;
      for ( ; j > 0; --j) {
        v += 6 + readInt(v + 2);
      }
    }
    i = readUnsignedShort(v); v += 2;
    for ( ; i > 0; --i) {
      j = readUnsignedShort(v + 6);
      v += 8;
      for ( ; j > 0; --j) {
        v += 6 + readInt(v + 2);
      }
    }
    // reads the class's attributes
    i = readUnsignedShort(v); v += 2;
    for ( ; i > 0; --i) {
      String attrName = readUTF8(v);
      if (attrName.equals("SourceFile")) {
        sourceFile = readUTF8(v + 6);
      } else if (attrName.equals("Deprecated")) {
        access |= Constants.ACC_DEPRECATED;
      } else if (attrName.equals("InnerClasses")) {
        w = v + 6;
      }
      v += 6 + readInt(v + 2);
    }
    // calls the visit method
    classVisitor.visit(
      access, className, superClassName, implementedItfs, sourceFile);

    // visits the inner classes info
    if (w != 0) {
      i = readUnsignedShort(w); w += 2;
      for ( ; i > 0; --i) {
        classVisitor.visitInnerClass(
          readUnsignedShort(w) == 0 ? null : readClass(w),
          readUnsignedShort(w + 2) == 0 ? null : readClass(w + 2),
          readUnsignedShort(w + 4) == 0 ? null : readUTF8(w + 4),
          readUnsignedShort(w + 6));
        w += 8;
      }
    }

    // visits the fields
    i = readUnsignedShort(u); u += 2;
    for ( ; i > 0; --i) {
      access = readUnsignedShort(u);
      String fieldName = readUTF8(u + 2);
      String fieldDesc = readUTF8(u + 4);
      // visits the field's attributes and looks for a ConstantValue attribute
      int fieldValueItem = 0;
      j = readUnsignedShort(u + 6);
      u += 8;
      for ( ; j > 0; --j) {
        String attrName = readUTF8(u);
        if (attrName.equals("ConstantValue")) {
          fieldValueItem = readUnsignedShort(u + 6);
        } else if (attrName.equals("Synthetic")) {
          access |= Constants.ACC_SYNTHETIC;
        } else if (attrName.equals("Deprecated")) {
          access |= Constants.ACC_DEPRECATED;
        }
        u += 6 + readInt(u + 2);
      }
      // reads the field's value, if any
      Object value = (fieldValueItem == 0 ? null : readConst(fieldValueItem));
      // visits the field
      classVisitor.visitField(access, fieldName, fieldDesc, value);
    }

    i = readUnsignedShort(u); u += 2;
    for ( ; i > 0; --i) {
      access = readUnsignedShort(u);
      String methName = readUTF8(u + 2);
      String methDesc = readUTF8(u + 4);
      // looks for Code and Exceptions attributes
      j = readUnsignedShort(u + 6);
      u += 8;
      v = 0;
      w = 0;
      for ( ; j > 0; --j) {
        String attrName = readUTF8(u); u += 2;
        int attrSize = readInt(u); u += 4;
        if (attrName.equals("Code")) {
          v = u;
        } else if (attrName.equals("Exceptions")) {
          w = u;
        } else if (attrName.equals("Synthetic")) {
          access |= Constants.ACC_SYNTHETIC;
        } else if (attrName.equals("Deprecated")) {
          access |= Constants.ACC_DEPRECATED;
        }
        u += attrSize;
      }
      // reads declared exceptions
      String[] exceptions;
      if (w == 0) {
        exceptions = null;
      } else {
        exceptions = new String[readUnsignedShort(w)]; w += 2;
        for (j = 0; j < exceptions.length; ++j) {
          exceptions[j] = readClass(w); w += 2;
        }
      }

      // visits the method's code, if any
      CodeVisitor cv;
      cv = classVisitor.visitMethod(access, methName, methDesc, exceptions);
      if (cv != null && v != 0) {
        int maxStack = readUnsignedShort(v);
        int maxLocals = readUnsignedShort(v + 2);
        int codeLength = readInt(v + 4);
        v += 8;

        int codeStart = v;
        int codeEnd = v + codeLength;

        // 1st phase: finds the labels
        int label;
        Label[] labels = new Label[codeLength + 1];
        while (v < codeEnd) {
          int opcode = b[v] & 0xFF;
          switch (TYPE[opcode]) {
            case NOARG_INSN:
            case IMPLVAR_INSN:
              v += 1;
              break;
            case LABEL_INSN:
              label = v - codeStart + readShort(v + 1);
              if (labels[label] == null) {
                labels[label] = new Label();
              }
              v += 3;
              break;
            case LABELW_INSN:
              label = v - codeStart + readInt(v + 1);
              if (labels[label] == null) {
                labels[label] = new Label();
              }
              v += 5;
              break;
            case WIDE_INSN:
              opcode = b[v + 1] & 0xFF;
              if (opcode == Constants.IINC) {
                v += 6;
              } else {
                v += 4;
              }
              break;
            case TABL_INSN:
              // skips 0 to 3 padding bytes
              w = v - codeStart;
              v = v + 4 - (w & 3);
              // reads instruction
              label = w + readInt(v); v += 4;
              if (labels[label] == null) {
                labels[label] = new Label();
              }
              j = readInt(v); v += 4;
              j = readInt(v) - j + 1; v += 4;
              for ( ; j > 0; --j) {
                label = w + readInt(v); v += 4;
                if (labels[label] == null) {
                  labels[label] = new Label();
                }
              }
              break;
            case LOOK_INSN:
              // skips 0 to 3 padding bytes
              w = v - codeStart;
              v = v + 4 - (w & 3);
              // reads instruction
              label = w + readInt(v); v += 4;
              if (labels[label] == null) {
                labels[label] = new Label();
              }
              j = readInt(v); v += 4;
              for ( ; j > 0; --j) {
                v += 4; // skips key
                label = w + readInt(v); v += 4;
                if (labels[label] == null) {
                  labels[label] = new Label();
                }
              }
              break;
            case VAR_INSN:
            case SBYTE_INSN:
            case LDC_INSN:
              v += 2;
              break;
            case SHORT_INSN:
            case LDCW_INSN:
            case FIELDORMETH_INSN:
            case TYPE_INSN:
            case IINC_INSN:
              v += 3;
              break;
            case ITFMETH_INSN:
              v += 5;
              break;
            // case MANA_INSN:
            default:
              v += 4;
              break;
          }
        }
        // parses the try catch entries
        j = readUnsignedShort(v); v += 2;
        for ( ; j > 0; --j) {
          label = readUnsignedShort(v);
          if (labels[label] == null) {
            labels[label] = new Label();
          }
          label = readUnsignedShort(v + 2);
          if (labels[label] == null) {
            labels[label] = new Label();
          }
          label = readUnsignedShort(v + 4);
          if (labels[label] == null) {
            labels[label] = new Label();
          }
          v += 8;
        }
        if (!skipDebug) {
          // parses the local variable and line number tables
          j = readUnsignedShort(v); v += 2;
          for ( ; j > 0; --j) {
            String attrName = readUTF8(v);
            if (attrName.equals("LocalVariableTable")) {
              k = readUnsignedShort(v + 6);
              w = v + 8;
              for ( ; k > 0; --k) {
                label = readUnsignedShort(w);
                if (labels[label] == null) {
                  labels[label] = new Label();
                }
                label += readUnsignedShort(w + 2);
                if (labels[label] == null) {
                  labels[label] = new Label();
                }
                w += 10;
              }
            } else if (attrName.equals("LineNumberTable")) {
              k = readUnsignedShort(v + 6);
              w = v + 8;
              for ( ; k > 0; --k) {
                label = readUnsignedShort(w);
                if (labels[label] == null) {
                  labels[label] = new Label();
                }
                w += 4;
              }
            }
            v += 6 + readInt(v + 2);
          }
        }

        // 2nd phase: visits each instruction
        v = codeStart;
        Label l;
        while (v < codeEnd) {
          w = v - codeStart;
          l = labels[w];
          if (l != null) {
            cv.visitLabel(l);
          }
          int opcode = b[v] & 0xFF;
          switch (TYPE[opcode]) {
            case NOARG_INSN:
              cv.visitInsn(opcode);
              v += 1;
              break;
            case IMPLVAR_INSN:
              if (opcode > Constants.ISTORE) {
                opcode -= 59; //ISTORE_0
                cv.visitVarInsn(Constants.ISTORE + (opcode >> 2), opcode & 0x3);
              } else {
                opcode -= 26; //ILOAD_0
                cv.visitVarInsn(Constants.ILOAD + (opcode >> 2), opcode & 0x3);
              }
              v += 1;
              break;
            case LABEL_INSN:
              cv.visitJumpInsn(opcode, labels[w + readShort(v + 1)]);
              v += 3;
              break;
            case LABELW_INSN:
              cv.visitJumpInsn(opcode, labels[w + readInt(v + 1)]);
              v += 5;
              break;
            case WIDE_INSN:
              opcode = b[v + 1] & 0xFF;
              if (opcode == Constants.IINC) {
                cv.visitIincInsn(readUnsignedShort(v + 2), readShort(v + 4));
                v += 6;
              } else {
                cv.visitVarInsn(opcode, readUnsignedShort(v + 2));
                v += 4;
              }
              break;
            case TABL_INSN:
              // skips 0 to 3 padding bytes
              v = v + 4 - (w & 3);
              // reads instruction
              label = w + readInt(v); v += 4;
              int min = readInt(v); v += 4;
              int max = readInt(v); v += 4;
              Label[] table = new Label[max - min + 1];
              for (j = 0; j < table.length; ++j) {
                table[j] = labels[w + readInt(v)];
                v += 4;
              }
              cv.visitTableSwitchInsn(min, max, labels[label], table);
              break;
            case LOOK_INSN:
              // skips 0 to 3 padding bytes
              v = v + 4 - (w & 3);
              // reads instruction
              label = w + readInt(v); v += 4;
              j = readInt(v); v += 4;
              int[] keys = new int[j];
              Label[] values = new Label[j];
              for (j = 0; j < keys.length; ++j) {
                keys[j] = readInt(v); v += 4;
                values[j] = labels[w + readInt(v)]; v += 4;
              }
              cv.visitLookupSwitchInsn(labels[label], keys, values);
              break;
            case VAR_INSN:
              cv.visitVarInsn(opcode, b[v + 1] & 0xFF);
              v += 2;
              break;
            case SBYTE_INSN:
              cv.visitIntInsn(opcode, b[v + 1]);
              v += 2;
              break;
            case SHORT_INSN:
              cv.visitIntInsn(opcode, readShort(v + 1));
              v += 3;
              break;
            case LDC_INSN:
              cv.visitLdcInsn(readConst(b[v + 1] & 0xFF));
              v += 2;
              break;
            case LDCW_INSN:
              cv.visitLdcInsn(readConst(readUnsignedShort(v + 1)));
              v += 3;
              break;
            case FIELDORMETH_INSN:
            case ITFMETH_INSN:
              int cpIndex = items[readUnsignedShort(v + 1)];
              String iowner = readClass(cpIndex);
              cpIndex = items[readUnsignedShort(cpIndex + 2)];
              String iname = readUTF8(cpIndex);
              String idesc = readUTF8(cpIndex + 2);
              if (opcode < Constants.INVOKEVIRTUAL) {
								cv.visitFieldInsn(opcode, iowner, iname, idesc);
							} else {
								cv.visitMethodInsn(opcode, iowner, iname, idesc);
							}
              if (opcode == Constants.INVOKEINTERFACE) {
                v += 5;
              } else {
                v += 3;
              }
              break;
            case TYPE_INSN:
              cv.visitTypeInsn(opcode, readClass(v + 1));
              v += 3;
              break;
            case IINC_INSN:
              cv.visitIincInsn(b[v + 1] & 0xFF, b[v + 2]);
              v += 3;
              break;
            // case MANA_INSN:
            default:
              cv.visitMultiANewArrayInsn(readClass(v + 1), b[v + 3] & 0xFF);
              v += 4;
              break;
          }
        }
        l = labels[codeEnd - codeStart];
        if (l != null) {
          cv.visitLabel(l);
        }
        // visits the try catch entries
        j = readUnsignedShort(v); v += 2;
        for ( ; j > 0; --j) {
          Label start = labels[readUnsignedShort(v)];
          Label end = labels[readUnsignedShort(v + 2)];
          Label handler = labels[readUnsignedShort(v + 4)];
          int type = readUnsignedShort(v + 6);
          if (type == 0) {
            cv.visitTryCatchBlock(start, end, handler, null);
          } else {
            cv.visitTryCatchBlock(start, end, handler, readUTF8(items[type]));
          }
          v += 8;
        }
        if (!skipDebug) {
          // visits the local variable and line number tables
          j = readUnsignedShort(v); v += 2;
          for ( ; j > 0; --j) {
            String attrName = readUTF8(v);
            if (attrName.equals("LocalVariableTable")) {
              k = readUnsignedShort(v + 6);
              w = v + 8;
              for ( ; k > 0; --k) {
                label = readUnsignedShort(w);
                Label start = labels[label];
                label += readUnsignedShort(w + 2);
                Label end = labels[label];
                cv.visitLocalVariable(
                  readUTF8(w + 4),
                  readUTF8(w + 6),
                  start,
                  end,
                  readUnsignedShort(w + 8));
                w += 10;
              }
            } else if (attrName.equals("LineNumberTable")) {
              k = readUnsignedShort(v + 6);
              w = v + 8;
              for ( ; k > 0; --k) {
                cv.visitLineNumber(
                  readUnsignedShort(w + 2),
                  labels[readUnsignedShort(w)]);
                w += 4;
              }
            }
            v += 6 + readInt(v + 2);
          }
        }
        // visits the max stack and max locals values
        cv.visitMaxs(maxStack, maxLocals);
      }
    }
  }

  // --------------------------------------------------------------------------
  // Utility methods: low level parsing
  // --------------------------------------------------------------------------

  /**
   * Reads an unsigned short value in {@link #b b}.
   *
   * @param index the start index of the value to be read in {@link #b b}.
   * @return the read value.
   */

  private int readUnsignedShort (final int index) {
    byte[] b = this.b;
    return ((b[index] & 0xFF) << 8) | (b[index + 1] & 0xFF);
  }

  /**
   * Reads a signed short value in {@link #b b}.
   *
   * @param index the start index of the value to be read in {@link #b b}.
   * @return the read value.
   */

  private short readShort (final int index) {
    byte[] b = this.b;
    return (short)(((b[index] & 0xFF) << 8) | (b[index + 1] & 0xFF));
  }

  /**
   * Reads a signed int value in {@link #b b}.
   *
   * @param index the start index of the value to be read in {@link #b b}.
   * @return the read value.
   */

  private int readInt (final int index) {
    byte[] b = this.b;
    return ((b[index] & 0xFF) << 24) |
           ((b[index + 1] & 0xFF) << 16) |
           ((b[index + 2] & 0xFF) << 8) |
           (b[index + 3] & 0xFF);
  }

  /**
   * Reads a signed long value in {@link #b b}.
   *
   * @param index the start index of the value to be read in {@link #b b}.
   * @return the read value.
   */

  private long readLong (final int index) {
    long l1 = readInt(index);
    long l0 = readInt(index + 4) & 0xFFFFFFFFL;
    return (l1 << 32) | l0;
  }

  /**
   * Reads a CONSTANT_Utf8 constant pool item in {@link #b b}.
   *
   * @param index the start index of an unsigned short value in {@link #b b},
   *      whose value is the index of an CONSTANT_Utf8 constant pool item.
   * @return the String corresponding to the specified CONSTANT_Utf8 item.
   */

  private String readUTF8 (int index) {
    // consults cache
    int item = readUnsignedShort(index);
    String s = strings[item];
    if (s != null) {
      return s;
    }
    // computes the start index of the CONSTANT_Utf8 item in b
    index = items[item];
    // reads the length of the string (in bytes, not characters)
    int utfLen = readUnsignedShort(index);
    index += 2;
    // parses the string bytes
    int endIndex = index + utfLen;
    byte[] b = this.b;
    int strLen = 0;
    int c, d, e;
    while (index < endIndex) {
      c = b[index++] & 0xFF;
      switch (c >> 4) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
          // 0xxxxxxx
          buf[strLen++] = (char)c;
          break;
        case 12:
        case 13:
          // 110x xxxx   10xx xxxx
          d = b[index++];
          buf[strLen++] = (char)(((c & 0x1F) << 6) | (d & 0x3F));
          break;
        default:
          // 1110 xxxx  10xx xxxx  10xx xxxx
          d = b[index++];
          e = b[index++];
          buf[strLen++] =
            (char)(((c & 0x0F) << 12) | ((d & 0x3F) << 6) | (e & 0x3F));
          break;
      }
    }
    s = new String(buf, 0, strLen);
    strings[item] = s;
    return s;
  }

  /**
   * Reads a CONSTANT_Class constant pool item in {@link #b b}.
   *
   * @param index the start index of an unsigned short value in {@link #b b},
   *      whose value is the index of a CONSTANT_Class constant pool item.
   * @return the String corresponding to the CONSTANT_Utf8 corresponding to the
   *      specified CONSTANT_Class item.
   */

  private String readClass (final int index) {
    // computes the start index of the CONSTANT_Class item in b
    // and reads the CONSTANT_Utf8 item designated by
    // the first two bytes of this CONSTANT_Class item
    return readUTF8(items[readUnsignedShort(index)]);
  }

  /**
   * Reads a numeric or string constant pool item in {@link #b b}.
   *
   * @param item the index of a constant pool item.
   * @return the {@link java.lang.Integer Integer}, {@link java.lang.Float
   *      Float}, {@link java.lang.Long Long}, {@link java.lang.Double Double}
   *      or {@link String String} corresponding to the given constant pool
   *      item.
   */

  private Object readConst (final int item) {
    int index = items[item];
    switch (b[index - 1]) {
      case ClassWriter.INT:
        return new Integer(readInt(index));
      case ClassWriter.FLOAT:
        return new Float(Float.intBitsToFloat(readInt(index)));
      case ClassWriter.LONG:
        return new Long(readLong(index));
      case ClassWriter.DOUBLE:
        return new Double(Double.longBitsToDouble(readLong(index)));
      //case ClassWriter.STR:
      default:
        return readUTF8(index);
    }
  }
}
