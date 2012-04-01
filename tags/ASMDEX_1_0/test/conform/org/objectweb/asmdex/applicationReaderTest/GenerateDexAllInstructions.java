/* Software Name : AsmDex
 * Version : 1.0
 *
 * Copyright © 2012 France Télécom
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
package org.objectweb.asmdex.applicationReaderTest;

import java.io.IOException;

import org.objectweb.asmdex.TestUtil;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

/**
 * Class that generates a dex file containing all the dalvik possible instructions, thanks
 * to the ApplicationWriter. The generated file hasn't the necessary classes to make it run
 * on hardware, it only consists in a class having one method having all the instructions.
 * <br/><br/>
 * As it uses the Writer, some register combinations can't be obtained (like 0 for a
 * CONST/16 as it will be optimized into a CONST).
 * <br/><br/>
 * Logically, we shouldn't use the Writer to test the Reader, and we don't do that in the
 * other tests (they use dex files compiled by dx). But getting all the instructions only
 * through a Java code would be too complicated, and wouldn't be as accurate.
 * 
 * @author Julien Névo
 */
public class GenerateDexAllInstructions implements Opcodes {

	/** The path name to generated dex file */
	public static final String PATH_AND_FILENAME_GENERATED_DEX_FILE =
		TestUtil.PATH_FOLDER_TESTCASE + TestUtil.FULL_TEST_SUBFOLDER + "generatedInsns.dex";
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		generateInstructions();
	}

	/**
	 * Generate instructions.
	 */
	public static void generateInstructions() {
		
		ApplicationWriter aw = new ApplicationWriter();
		Label label1 = new Label();
		Label label2 = new Label();
		Label label3 = new Label();
		Label label4 = new Label();
		Label label5 = new Label();
		Label label6 = new Label();
		Label label7 = new Label();
		Label label8 = new Label();
		Label label9 = new Label();
		Label label10 = new Label();
		Label label11 = new Label();
		Label label12 = new Label();

		aw.visit();
		
		ClassVisitor cw = aw.visitClass(ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null);
		cw.visit(0, ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null);
		
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "myMethod2", "V", null, null);
			mv.visitEnd();
		}
		
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "myMethod3", "VII", null, null);
			mv.visitEnd();
		}
		
		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "myMethod4", "VIII", null, null);
			mv.visitEnd();
		}

		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "myMethod5", "VIIII", null, null);
			mv.visitEnd();
		}

		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "myMethod6", "VIIIII", null, null);
			mv.visitEnd();
		}

		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "myMethod7", "VIIIIII", null, null);
			mv.visitEnd();
		}

		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "myMethod8", "VIIIIIII", null, null);
			mv.visitEnd();
		}

		{
			MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "myMethod9", "VIIIIIIII", null, null);
			mv.visitEnd();
		}

		
		
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "myMethod1", "V", null, null);
		mv.visitCode();
		
		// 0x00
		mv.visitInsn(INSN_NOP);
		// 0x01
		mv.visitVarInsn(INSN_MOVE, 0, 15);
		mv.visitVarInsn(INSN_MOVE, 15, 1);
		mv.visitVarInsn(INSN_MOVE, 7, 8);
		// 0x02
		mv.visitVarInsn(INSN_MOVE_FROM16, 0, 65535);
		mv.visitVarInsn(INSN_MOVE_FROM16, 255, 0);
		mv.visitVarInsn(INSN_MOVE_FROM16, 129, 32907);
		// 0x03
		mv.visitVarInsn(INSN_MOVE_16, 256, 65535);
		mv.visitVarInsn(INSN_MOVE_16, 65535, 0);
		mv.visitVarInsn(INSN_MOVE_16, 1234, 50468);
		// 0x04
		mv.visitVarInsn(INSN_MOVE_WIDE, 0, 15);
		mv.visitVarInsn(INSN_MOVE_WIDE, 15, 0);
		mv.visitVarInsn(INSN_MOVE_WIDE, 8, 7);
		// 0x05
		mv.visitVarInsn(INSN_MOVE_WIDE_FROM16, 0, 16);
		mv.visitVarInsn(INSN_MOVE_WIDE_FROM16, 255, 0);
		mv.visitVarInsn(INSN_MOVE_WIDE_FROM16, 128, 65535);
		// 0x06
		mv.visitVarInsn(INSN_MOVE_WIDE_16, 256, 65535);
		mv.visitVarInsn(INSN_MOVE_WIDE_16, 65535, 0);
		mv.visitVarInsn(INSN_MOVE_WIDE_16, 12437, 54970);
		// 0x07
		mv.visitVarInsn(INSN_MOVE_OBJECT, 0, 15);
		mv.visitVarInsn(INSN_MOVE_OBJECT, 15, 0);
		mv.visitVarInsn(INSN_MOVE_OBJECT, 10, 4);
		// 0x08
		mv.visitVarInsn(INSN_MOVE_OBJECT_FROM16, 0, 65535);
		mv.visitVarInsn(INSN_MOVE_OBJECT_FROM16, 255, 0);
		mv.visitVarInsn(INSN_MOVE_OBJECT_FROM16, 128, 12898);
		// 0x09
		mv.visitVarInsn(INSN_MOVE_OBJECT_16, 256, 65535);
		mv.visitVarInsn(INSN_MOVE_OBJECT_16, 65535, 0);
		mv.visitVarInsn(INSN_MOVE_OBJECT_16, 15679, 30189);
		// 0x0a
		mv.visitIntInsn(INSN_MOVE_RESULT, 0);
		mv.visitIntInsn(INSN_MOVE_RESULT, 255);
		mv.visitIntInsn(INSN_MOVE_RESULT, 130);
		// 0x0b
		mv.visitIntInsn(INSN_MOVE_RESULT_WIDE, 0);
		mv.visitIntInsn(INSN_MOVE_RESULT_WIDE, 255);
		mv.visitIntInsn(INSN_MOVE_RESULT_WIDE, 80);
		// 0x0c
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 0);
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 255);
		mv.visitIntInsn(INSN_MOVE_RESULT_OBJECT, 170);
		// 0x0d
		mv.visitIntInsn(INSN_MOVE_EXCEPTION, 0);
		mv.visitIntInsn(INSN_MOVE_EXCEPTION, 255);
		mv.visitIntInsn(INSN_MOVE_EXCEPTION, 60);
		// 0x0e
		mv.visitInsn(INSN_RETURN_VOID);
		// 0x0f
		mv.visitIntInsn(INSN_RETURN, 0);
		mv.visitIntInsn(INSN_RETURN, 255);
		mv.visitIntInsn(INSN_RETURN, 100);
		// 0x10
		mv.visitIntInsn(INSN_RETURN_WIDE, 0);
		mv.visitIntInsn(INSN_RETURN_WIDE, 255);
		mv.visitIntInsn(INSN_RETURN_WIDE, 70);
		// 0x11
		mv.visitIntInsn(INSN_RETURN_OBJECT, 0);
		mv.visitIntInsn(INSN_RETURN_OBJECT, 255);
		mv.visitIntInsn(INSN_RETURN_OBJECT, 70);
		// 0x12
		mv.visitVarInsn(INSN_CONST_4, 0, 7);
		mv.visitVarInsn(INSN_CONST_4, 15, 0);
		mv.visitVarInsn(INSN_CONST_4, 7, -8);
		// 0x13
		mv.visitVarInsn(INSN_CONST_16, 0, 16);
		mv.visitVarInsn(INSN_CONST_16, 16, 0);
		mv.visitVarInsn(INSN_CONST_16, 15, -32768);
		mv.visitVarInsn(INSN_CONST_16, 7, 32767);
		// 0x14
		mv.visitVarInsn(INSN_CONST, 0, 32768);
		mv.visitVarInsn(INSN_CONST, 255, Integer.MAX_VALUE);
		mv.visitVarInsn(INSN_CONST, 127, Integer.MIN_VALUE + 1); // +1 to prevent the optimization to CONST/HIGH16
		// 0x15
		mv.visitVarInsn(INSN_CONST_HIGH16, 255, 0x7fff0000);
		mv.visitVarInsn(INSN_CONST_HIGH16, 127, 0x12340000);
		mv.visitVarInsn(INSN_CONST_HIGH16, 0, Integer.MIN_VALUE);
		// 0x16
		mv.visitVarInsn(INSN_CONST_WIDE_16, 0, -32768);
		mv.visitVarInsn(INSN_CONST_WIDE_16, 255, 32767);
		mv.visitVarInsn(INSN_CONST_WIDE_16, 128, 0);
		// 0x17
		mv.visitVarInsn(INSN_CONST_WIDE_32, 0, -32769);
		mv.visitVarInsn(INSN_CONST_WIDE_32, 255, 32768);
		mv.visitVarInsn(INSN_CONST_WIDE_32, 147, Integer.MIN_VALUE);
		mv.visitVarInsn(INSN_CONST_WIDE_32, 240, Integer.MAX_VALUE);
		mv.visitVarInsn(INSN_CONST_WIDE_32, 131, 4898789);
		// 0x18
		mv.visitVarInsn(INSN_CONST_WIDE, 0, 0x4545454545L);
		mv.visitVarInsn(INSN_CONST_WIDE, 255, -0x275134515L);
		mv.visitVarInsn(INSN_CONST_WIDE, 45, Long.MAX_VALUE);
		mv.visitVarInsn(INSN_CONST_WIDE, 241, Long.MIN_VALUE + 1); // +1 to prevent the optimization to CONST-WIDE/HIGH16
		// 0x19
		mv.visitVarInsn(INSN_CONST_WIDE_HIGH16, 0, 0x7fff000000000000L);
		mv.visitVarInsn(INSN_CONST_WIDE_HIGH16, 255, Long.MIN_VALUE);
		mv.visitVarInsn(INSN_CONST_WIDE_HIGH16, 61, 0x1234000000000000L);
		// 0x1a
		mv.visitStringInsn(INSN_CONST_STRING, 0, "myString1");
		mv.visitStringInsn(INSN_CONST_STRING, 255, "myString1");
		mv.visitStringInsn(INSN_CONST_STRING, 123, "myString1");
		// 0x1b
		mv.visitStringInsn(INSN_CONST_STRING_JUMBO, 0, "myString2");
		mv.visitStringInsn(INSN_CONST_STRING_JUMBO, 255, "myString2");
		mv.visitStringInsn(INSN_CONST_STRING_JUMBO, 178, "myString2");
		// 0x1c
		mv.visitTypeInsn(INSN_CONST_CLASS, 0, 0, 0, "myType1");
		mv.visitTypeInsn(INSN_CONST_CLASS, 255, 0, 0, "myType1");
		mv.visitTypeInsn(INSN_CONST_CLASS, 45, 0, 0, "myType1");
		// 0x1d
		mv.visitIntInsn(INSN_MONITOR_ENTER, 0);
		mv.visitIntInsn(INSN_MONITOR_ENTER, 255);
		mv.visitIntInsn(INSN_MONITOR_ENTER, 172);
		// 0x1e
		mv.visitIntInsn(INSN_MONITOR_EXIT, 0);
		mv.visitIntInsn(INSN_MONITOR_EXIT, 255);
		mv.visitIntInsn(INSN_MONITOR_EXIT, 63);
		// 0x1f
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 0, 0, "myType2");
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 255, 0, "myType2");
		mv.visitTypeInsn(INSN_CHECK_CAST, 0, 179, 0, "myType2");
		// 0x20
		mv.visitTypeInsn(INSN_INSTANCE_OF, 0, 15, 0, "myType3");
		mv.visitTypeInsn(INSN_INSTANCE_OF, 15, 0, 0, "myType3");
		mv.visitTypeInsn(INSN_INSTANCE_OF, 3, 10, 0, "myType3");
		// 0x21
		mv.visitArrayLengthInsn(0, 15);
		mv.visitArrayLengthInsn(15, 0);
		mv.visitArrayLengthInsn(10, 3);
		// 0x22
		mv.visitTypeInsn(INSN_NEW_INSTANCE, 0, 0, 0, "Landroid/app/Activity;");
		mv.visitTypeInsn(INSN_NEW_INSTANCE, 255, 0, 0, "Landroid/app/Activity;");
		mv.visitTypeInsn(INSN_NEW_INSTANCE, 204, 0, 0, "Landroid/app/Activity;");
		// 0x23
		mv.visitTypeInsn(INSN_NEW_ARRAY, 0, 0, 6, "[B");
		mv.visitTypeInsn(INSN_NEW_ARRAY, 15, 0, 5, "[C");
		mv.visitTypeInsn(INSN_NEW_ARRAY, 10, 0, 7, "[S");
		mv.visitTypeInsn(INSN_NEW_ARRAY, 7, 0, 7, "[I");
		mv.visitTypeInsn(INSN_NEW_ARRAY, 5, 0, 7, "[F");
		mv.visitTypeInsn(INSN_NEW_ARRAY, 2, 0, 8, "[J");
		mv.visitTypeInsn(INSN_NEW_ARRAY, 1, 0, 7, "[D");
		mv.visitTypeInsn(INSN_NEW_ARRAY, 3, 0, 6, "[Z");
		// 0x24
		mv.visitMultiANewArrayInsn("[B", new int[] { 4 });
		mv.visitMultiANewArrayInsn("[I", new int[] { 0, 15 });
		mv.visitMultiANewArrayInsn("[F", new int[] { 0, 1, 15 });
		mv.visitMultiANewArrayInsn("[S", new int[] { 0, 15, 2, 7 });
		mv.visitMultiANewArrayInsn("[C", new int[] { 0, 4, 13, 10, 3 });
		// 0x25
		mv.visitMultiANewArrayInsn("[B", new int[] { 0, 1, 2, 3, 4, 5 });
		mv.visitMultiANewArrayInsn("[I", new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });
		// 0x26
		mv.visitFillArrayDataInsn(0, new Byte[] { 0, -2, -7, 10, Byte.MAX_VALUE, Byte.MIN_VALUE });
		mv.visitFillArrayDataInsn(15, new Character[] { 1, 17, 91, Character.MAX_VALUE, Character.MIN_VALUE });
		mv.visitFillArrayDataInsn(10, new Short[] { 0, 5, -6, -170, 491, Short.MAX_VALUE, Short.MIN_VALUE });
		mv.visitFillArrayDataInsn(7, new Integer[] { 0, -20, -156, 7, 156, Integer.MAX_VALUE, Integer.MIN_VALUE });
		mv.visitFillArrayDataInsn(5, new Float[] { 0f, 1f, -4f, 99f, -150f, Float.MAX_VALUE, Float.MIN_VALUE });
		mv.visitFillArrayDataInsn(2, new Long[] { 0L, 1L, -5L, 150L, -1500L, 5679L, Long.MAX_VALUE, Long.MIN_VALUE });
		mv.visitFillArrayDataInsn(1, new Double[] { 0d, 1d, -5.5d, 897.5d, -9090.45d, Double.MAX_VALUE, Double.MIN_VALUE });
		mv.visitFillArrayDataInsn(3, new Boolean[] { false, true, true, true, false, true });
		
		mv.visitLabel(label1);
		// 0x27
		mv.visitIntInsn(INSN_THROW, 0);
		mv.visitIntInsn(INSN_THROW, 255);
		mv.visitIntInsn(INSN_THROW, 199);
		// 0x28
		mv.visitJumpInsn(INSN_GOTO, label2, 0, 0);
		mv.visitJumpInsn(INSN_GOTO, label1, 0, 0);
		// 0x29
		mv.visitJumpInsn(INSN_GOTO_16, label2, 0, 0);
		mv.visitJumpInsn(INSN_GOTO_16, label1, 0, 0);
		// 0x2a
		mv.visitJumpInsn(INSN_GOTO_32, label2, 0, 0);
		mv.visitJumpInsn(INSN_GOTO_32, label1, 0, 0);
		mv.visitLabel(label2);

		// 0x2b and 0x2c are below.
		
		// 0x2d -> 0x31
		for (int opcode = INSN_CMPL_FLOAT; opcode <= INSN_CMP_LONG; opcode++) {
			mv.visitOperationInsn(opcode, 0, 255, 50, 0);
			mv.visitOperationInsn(opcode, 255, 184, 0, 0);
			mv.visitOperationInsn(opcode, 56, 0, 255, 0);
		}
		
		// 0x32 -> 0x3d
		for (int opcode = INSN_IF_EQ; opcode <= INSN_IF_LEZ; opcode++) {
			mv.visitJumpInsn(opcode, label1, 0, 15);
			mv.visitJumpInsn(opcode, label2, 15, 0);
			mv.visitJumpInsn(opcode, label11, 12, 7);
			mv.visitJumpInsn(opcode, label12, 1, 13);
		}
		
		// 0x3e - 0x43 (unused)
		
		mv.visitLabel(label11);
		
		// 0x44 -> 0x51
		for (int opcode = INSN_AGET; opcode <= INSN_APUT_SHORT; opcode++) {
			mv.visitArrayOperationInsn(opcode, 0, 255, 10);
			mv.visitArrayOperationInsn(opcode, 255, 240, 0);
			mv.visitArrayOperationInsn(opcode, 189, 0, 255);
			
		}
		
		// 0x52 -> 0x5f
		for (int opcode = INSN_IGET; opcode <= INSN_IPUT_SHORT; opcode++) {
			mv.visitFieldInsn(opcode, "Lft/nevo/FirstActivity;", "field1", "I", 0, 15);
			mv.visitFieldInsn(opcode, "Lft/nevo/FirstActivity;", "field1", "S", 15, 0);
			mv.visitFieldInsn(opcode, "Lft/nevo/FirstActivity;", "field1", "J", 3, 9);
		}
		
		mv.visitLabel(label12);
		
		// 0x60 -> 0x6d
		for (int opcode = INSN_SGET; opcode <= INSN_SPUT_SHORT; opcode++) {
			mv.visitFieldInsn(opcode, "Lft/nevo/FirstActivity;", "field1", "I", 0, 0);
			mv.visitFieldInsn(opcode, "Lft/nevo/FirstActivity;", "field1", "S", 15, 0);
			mv.visitFieldInsn(opcode, "Lft/nevo/FirstActivity;", "field1", "J", 8, 0);
		}
	
		// 0x6e -> 0x72
		for (int opcode = INSN_INVOKE_VIRTUAL; opcode <= INSN_INVOKE_INTERFACE; opcode++) {
			if (opcode != INSN_INVOKE_STATIC) {
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod1", "V", new int[] {0} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod2", "VI", new int[] {0, 15} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod3", "VII", new int[] {0, 15, 2} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod4", "VIII", new int[] {0, 15, 2, 6} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod5", "VIIII", new int[] {0, 15, 2, 6, 9} );
			} else {
				// Invoke Static requires one less parameter (no "this").
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod1", "V", new int[] {} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod2", "VI", new int[] {15} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod3", "VII", new int[] {15, 2} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod4", "VIII", new int[] {15, 2, 6} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod5", "VIIII", new int[] {15, 2, 6, 9} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod6", "VIIIII", new int[] {0, 15, 2, 6, 9} );
			}
			
		}
		
		// 0x73 (unused)
		
		// 0x74 -> 0x78
		for (int opcode = INSN_INVOKE_VIRTUAL_RANGE; opcode <= INSN_INVOKE_INTERFACE_RANGE; opcode++) {
			if (opcode != INSN_INVOKE_STATIC_RANGE) {
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod1", "V", new int[] {0} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod4", "VIII", new int[] {65532, 65533, 65534, 65535} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod9", "VIIIIIIII", new int[] {50, 51, 52, 53, 54, 55, 56, 57, 58} );
			} else {
				// Invoke Static Range requires one less parameter (no "this").
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod1", "V", new int[] {} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod4", "VIII", new int[] {65532, 65533, 65534} );
				mv.visitMethodInsn(opcode,  "Lft/nevo/FirstActivity;", "myMethod9", "VIIIIIIII", new int[] {50, 51, 52, 53, 54, 55, 56, 57} );
			}
		}
		
		// 0x7b -> 0x8f
		for (int opcode = INSN_NEG_INT; opcode <= INSN_INT_TO_SHORT; opcode++) {
			mv.visitOperationInsn(opcode, 0, 15, 0, 0);
			mv.visitOperationInsn(opcode, 15, 0, 0, 0);
			mv.visitOperationInsn(opcode, 7, 8, 0, 0);
		}
		
		// 0x90 -> 0xaf
		for (int opcode = INSN_ADD_INT; opcode <= INSN_REM_DOUBLE; opcode++) {
			mv.visitOperationInsn(opcode, 0, 255, 181, 0);
			mv.visitOperationInsn(opcode, 255, 52, 0, 0);
			mv.visitOperationInsn(opcode, 15, 0, 255, 0);
		}
		
		// 0xb0 -> 0xcf
		for (int opcode = INSN_ADD_INT_2ADDR; opcode <= INSN_REM_DOUBLE_2ADDR; opcode++) {
			mv.visitOperationInsn(opcode, 0, 0, 15, 0);
			mv.visitOperationInsn(opcode, 15, 15, 0, 0);
			mv.visitOperationInsn(opcode, 7, 7, 4, 0);
		}

		// 0xd0 -> 0xd7
		for (int opcode = INSN_ADD_INT_LIT16; opcode <= INSN_XOR_INT_LIT16; opcode++) {
			mv.visitOperationInsn(opcode, 0, 15, 0, -32768);
			mv.visitOperationInsn(opcode, 15, 0, 0, 32767);
			mv.visitOperationInsn(opcode, 11, 7, 0, 142);
			mv.visitOperationInsn(opcode, 3, 9, 0, -15);
		}

		// 0xd8 -> 0xe2
		for (int opcode = INSN_ADD_INT_LIT8; opcode <= INSN_USHR_INT_LIT8; opcode++) {
			mv.visitOperationInsn(opcode, 0, 255, 0, -128);
			mv.visitOperationInsn(opcode, 255, 0, 0, 127);
			mv.visitOperationInsn(opcode, 201, 9, 0, 110);
			mv.visitOperationInsn(opcode, 4, 189, 0, -45);
		}

		// 0xe3 -> 0xff (unused).

		// 0x2b
		mv.visitTableSwitchInsn(255, 5, 7, label6, new Label[] { label3, label4, label5 });		
		mv.visitLabel(label3);
		mv.visitIntInsn(INSN_THROW, 0);
		mv.visitJumpInsn(INSN_GOTO, label6, 0, 0);
		mv.visitLabel(label4);
		mv.visitIntInsn(INSN_THROW, 1);
		mv.visitJumpInsn(INSN_GOTO, label6, 0, 0);
		mv.visitLabel(label5);
		mv.visitIntInsn(INSN_THROW, 2);
		mv.visitJumpInsn(INSN_GOTO, label6, 0, 0);
		mv.visitLabel(label6);
		
		// 0x2c
		mv.visitLookupSwitchInsn(255, label10, new int[] { 2, 10, 3000 }, new Label[] { label7, label8, label9 });
		mv.visitLabel(label7);
		mv.visitIntInsn(INSN_THROW, 0);
		mv.visitJumpInsn(INSN_GOTO, label10, 0, 0);
		mv.visitLabel(label8);
		mv.visitIntInsn(INSN_THROW, 1);
		mv.visitJumpInsn(INSN_GOTO, label10, 0, 0);
		mv.visitLabel(label9);
		mv.visitIntInsn(INSN_THROW, 2);
		mv.visitJumpInsn(INSN_GOTO, label10, 0, 0);
		mv.visitLabel(label10);
		
		mv.visitInsn(INSN_RETURN_VOID);
		
		
		mv.visitMaxs(30, 0);
		mv.visitEnd();
		
		cw.visitEnd();
		
		aw.visitEnd();
		
		byte[] bytes = aw.toByteArray();
		try { TestUtil.createFileFromByteArray(bytes, PATH_AND_FILENAME_GENERATED_DEX_FILE);
		} catch (IOException e) { e.printStackTrace(); }
		
	}
	
	
}
