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

import org.objectweb.asmdex.TestUtil;
import org.objectweb.asmdex.logging.*;
import org.ow2.asmdex.structureCommon.Label;

/**
 * Log List of an application that tests the Annotations. Only the most important classes are parsed,
 * all the "boiler plate" (R, R$layout etc.) are skipped.
 *  
 * @author Julien Névo
 */
public class LogListAnnotationTests implements LogList {

	private Label l0FirstActivityInit = new Label();
	private Label l0FirstActivityMyMethod = new Label();
	
	private Label l0FirstActivityOnCreate = new Label();
	private Label l1FirstActivityOnCreate = new Label();
	private Label l2FirstActivityOnCreate = new Label();
	
	private Label l0MyEnumClInit = new Label();
	private Label l0MyEnumInit = new Label();
	private Label l0MyEnumValueOf = new Label();
	private Label l0MyEnumValue = new Label();
	
	/**
	 * Instantiates a new log list annotation tests.
	 */
	public LogListAnnotationTests() {
		l0FirstActivityInit.setOffset(0);
		l0FirstActivityMyMethod.setOffset(0);
		
		l0FirstActivityOnCreate.setOffset(0);
		l1FirstActivityOnCreate.setOffset(6);
		l2FirstActivityOnCreate.setOffset(0x10);
		
		l0MyEnumClInit.setOffset(8);
		l0MyEnumInit.setOffset(0);
		l0MyEnumValueOf.setOffset(0);
		l0MyEnumValue.setOffset(2);
	}
	
	@Override
	public String[] getClassesToParse() {
		return new String[] {
				"Lft/nevo/FirstActivity;",
				"Lft/nevo/FirstAnnotation;",
				"Lft/nevo/MyEnum;",
				"Lft/nevo/SecondAnnotation;",
				"Lft/nevo/ThirdAnnotation;"
		};
	}

	@Override
	public String getDexFile() {
		return TestUtil.PATH_AND_FILENAME_ANNOTATION_TESTS_DEX;
	}

	@Override
	public LogElement[] getLogElements() {
		return new LogElement[] {
				new LogElementApplicationVisit() 	// aw.visit();
				
				// Class FirstActivity
				, new LogElementApplicationVisitClass(ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null)
				, new LogElementClassVisit(ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null)
				, new LogElementClassVisitSource("FirstActivity.java", null)
				
				// Method <init>
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null)
				, new LogElementMethodVisitCode()
				, new LogElementMethodVisitMaxs(1, 0)
				, new LogElementMethodVisitLabel(l0FirstActivityInit)
				, new LogElementMethodVisitLineNumber(7, l0FirstActivityInit)
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Landroid/app/Activity;", "<init>", "V", new int[] { 0 })
				, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
				, new LogElementMethodVisitEnd()
				
				
				// Method myMethod1
				, new LogElementClassVisitMethod(ACC_PUBLIC, "myMethod1", "V", null, null)

				, new LogElementMethodVisitAnnotation("Lft/nevo/FirstAnnotation;", true)
				, new LogElementAnnotationVisitArray("MyEnums")
				, new LogElementAnnotationVisitEnum(null, "Lft/nevo/MyEnum;", "CCC")
				, new LogElementAnnotationVisitEnum(null, "Lft/nevo/MyEnum;", "AAA")
				, new LogElementAnnotationVisitEnum(null, "Lft/nevo/MyEnum;", "FFF")
				, new LogElementAnnotationVisitEnd()
				
				// Element 19.
				, new LogElementAnnotationVisitClass("a", "Ljava/lang/Short;")
				, new LogElementAnnotationVisitArray("b")
				, new LogElementAnnotationVisitClass(null, "Ljava/lang/Short;")
				, new LogElementAnnotationVisitClass(null, "Ljava/lang/Short;")
				, new LogElementAnnotationVisitClass(null, "Ljava/lang/Short;")
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementAnnotationVisit("myAnnotatedInt", 45)
				, new LogElementAnnotationVisit("myBool", true)
				, new LogElementAnnotationVisit("myBools", new boolean[] { false, true, true, false })
				, new LogElementAnnotationVisit("myByte", Byte.valueOf((byte)5))
				, new LogElementAnnotationVisit("myBytes", new byte[] { -4, 5, 6 })
				, new LogElementAnnotationVisit("myChar", Character.valueOf((char)97))
				, new LogElementAnnotationVisit("myChars", new char[] { 97, 122, 121 })
				, new LogElementAnnotationVisit("myDouble", 45.0d)
				, new LogElementAnnotationVisit("myDoubles", new double[] { 1.0d, 440.0d, -5.0d })
				, new LogElementAnnotationVisit("myEmptyBools", new int[] {  })
				, new LogElementAnnotationVisitEnum("myEnum", "Lft/nevo/MyEnum;", "CCC")
				, new LogElementAnnotationVisit("myFloat", 3.0f)
				, new LogElementAnnotationVisit("myFloats", new float[] { 2.0f, 3.0f, 4.0f, -9.0f })
				, new LogElementAnnotationVisit("myInt", 5)
				
				, new LogElementAnnotationVisitAnnotation("myIntAnnotation", "Lft/nevo/ThirdAnnotation;")
				, new LogElementAnnotationVisit("a", 5)
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementAnnotationVisitArray("myIntAnnotations")
				, new LogElementAnnotationVisitAnnotation(null, "Lft/nevo/ThirdAnnotation;")
				, new LogElementAnnotationVisit("a", 2)
				, new LogElementAnnotationVisitEnd()
				, new LogElementAnnotationVisitAnnotation(null, "Lft/nevo/ThirdAnnotation;")
				, new LogElementAnnotationVisit("a", 5)
				, new LogElementAnnotationVisitEnd()
				, new LogElementAnnotationVisitAnnotation(null, "Lft/nevo/ThirdAnnotation;")
				, new LogElementAnnotationVisit("a", 7)
				, new LogElementAnnotationVisitEnd()
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementAnnotationVisit("myInts", new int[] { 1, -3, 4 })
				, new LogElementAnnotationVisit("myLong", 4L)
				, new LogElementAnnotationVisit("myLongs", new long[] { 4L, 20L, 60L, -60L })
				, new LogElementAnnotationVisit("myShort", Short.valueOf((short)8))
				, new LogElementAnnotationVisit("myShorts", new short[] { 3, -4, 5, 6 })
				, new LogElementAnnotationVisit("myString", "bonjour")
				
				, new LogElementAnnotationVisitArray("myStrings")
				, new LogElementAnnotationVisit(null, "zephyr")
				, new LogElementAnnotationVisit(null, "hello")
				, new LogElementAnnotationVisit(null, "goodbye")
				, new LogElementAnnotationVisitEnd()
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementMethodVisitAnnotation("Lft/nevo/SecondAnnotation;", true)
				, new LogElementAnnotationVisitArray("myStrings")
				, new LogElementAnnotationVisit(null, "aaa")
				, new LogElementAnnotationVisit(null, "zzz")
				, new LogElementAnnotationVisit(null, "eee")
				, new LogElementAnnotationVisitEnd()
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementMethodVisitCode()
				, new LogElementMethodVisitMaxs(1, 0)
				, new LogElementMethodVisitLabel(l0FirstActivityMyMethod)
				, new LogElementMethodVisitLineNumber(37, l0FirstActivityMyMethod)
				, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
				, new LogElementMethodVisitEnd()
				
				, new LogElementClassVisitMethod(ACC_PUBLIC, "onCreate", "VLandroid/os/Bundle;", null, null)
				, new LogElementMethodVisitParameters(new String[] { "savedInstanceState" })
				, new LogElementMethodVisitCode()
				, new LogElementMethodVisitMaxs(3, 0)
				, new LogElementMethodVisitLabel(l0FirstActivityOnCreate)
				, new LogElementMethodVisitLineNumber(11, l0FirstActivityOnCreate)
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_SUPER, "Landroid/app/Activity;", "onCreate", "VLandroid/os/Bundle;", new int[] { 1, 2 })
				, new LogElementMethodVisitLabel(l1FirstActivityOnCreate)
				, new LogElementMethodVisitLineNumber(12, l1FirstActivityOnCreate)
				, new LogElementMethodVisitVarInsn(INSN_CONST_HIGH16, 0, 2130903040)
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_VIRTUAL, "Lft/nevo/FirstActivity;", "setContentView", "VI", new int[] { 1, 0 })
				, new LogElementMethodVisitLabel(l2FirstActivityOnCreate)
				, new LogElementMethodVisitLineNumber(13, l2FirstActivityOnCreate)
				, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
				, new LogElementMethodVisitEnd()
				
				, new LogElementClassVisitEnd()
				
				
				
				// Class FirstAnnotation
				, new LogElementApplicationVisitClass(ACC_PUBLIC + ACC_INTERFACE + ACC_ABSTRACT + ACC_ANNOTATION, "Lft/nevo/FirstAnnotation;", null, "Ljava/lang/Object;", new String[] { "Ljava/lang/annotation/Annotation;" })
				, new LogElementClassVisit(ACC_PUBLIC + ACC_INTERFACE + ACC_ABSTRACT + ACC_ANNOTATION, "Lft/nevo/FirstAnnotation;", null, "Ljava/lang/Object;", new String[] { "Ljava/lang/annotation/Annotation;" })
				, new LogElementClassVisitSource("FirstAnnotation.java", null)
				
				, new LogElementClassVisitAnnotation("Ljava/lang/annotation/Retention;", true)
				, new LogElementAnnotationVisitEnum("value", "Ljava/lang/annotation/RetentionPolicy;", "RUNTIME")
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "MyEnums", "[Lft/nevo/MyEnum;", null, null)
				, new LogElementMethodVisitEnd()
				
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "a", "Ljava/lang/Class;", new String[] { "()", "Ljava/lang/Class", "<", "Ljava/lang/Short;", ">;" }, null)
				, new LogElementMethodVisitEnd()
				
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "b", "[Ljava/lang/Class;", new String[] { "()[", "Ljava/lang/Class", "<", "Ljava/lang/Short;", ">;" }, null)
				, new LogElementMethodVisitEnd()
				
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myAnnotatedInt", "I", null, null)
				, new LogElementMethodVisitAnnotation("Lft/nevo/SecondAnnotation;", true)
				, new LogElementAnnotationVisitArray("myStrings")
				, new LogElementAnnotationVisit(null, "zygomate")
				, new LogElementAnnotationVisit(null, "hello")
				, new LogElementAnnotationVisit(null, "bonjour")
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementMethodVisitEnd()
				
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myBool", "Z", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myBools", "[Z", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myByte", "B", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myBytes", "[B", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myChar", "C", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myChars", "[C", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myDouble", "D", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myDoubles", "[D", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myEmptyBools", "[Z", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myEnum", "Lft/nevo/MyEnum;", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myFloat", "F", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myFloatDefault", "F", null, null)
				, new LogElementMethodVisitAnnotationDefault()
				, new LogElementAnnotationVisit("myFloatDefault", 99.0f)
				, new LogElementAnnotationVisitEnd()
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myFloats", "[F", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myFloatsDefault", "[F", null, null)
				, new LogElementMethodVisitAnnotationDefault()
				, new LogElementAnnotationVisit("myFloatsDefault", new float[] { 1.0f, 2.0f, 3.0f })
				, new LogElementAnnotationVisitEnd()
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myInt", "I", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myIntAnnotation", "Lft/nevo/ThirdAnnotation;", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myIntAnnotations", "[Lft/nevo/ThirdAnnotation;", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myInts", "[I", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myLong", "J", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myLongs", "[J", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myShort", "S", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myShorts", "[S", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myString", "Ljava/lang/String;", null, null)
				, new LogElementMethodVisitEnd()
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myStrings", "[Ljava/lang/String;", null, null)
				, new LogElementMethodVisitEnd()
				
				, new LogElementClassVisitEnd()
				
				// Class MyEnum. Element 160.
				, new LogElementApplicationVisitClass(ACC_PUBLIC + ACC_FINAL + ACC_ENUM, "Lft/nevo/MyEnum;", new String[] { "Ljava/lang/Enum", "<", "Lft/nevo/MyEnum;", ">;" }, "Ljava/lang/Enum;", null)
				, new LogElementClassVisit(ACC_PUBLIC + ACC_FINAL + ACC_ENUM, "Lft/nevo/MyEnum;", new String[] { "Ljava/lang/Enum", "<", "Lft/nevo/MyEnum;", ">;" }, "Ljava/lang/Enum;", null)
				, new LogElementClassVisitSource("MyEnum.java", null)
				
				, new LogElementClassVisitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL + ACC_ENUM, "AAA", "Lft/nevo/MyEnum;", null, null)
				, new LogElementFieldVisitEnd()
				, new LogElementClassVisitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL + ACC_ENUM, "BBB", "Lft/nevo/MyEnum;", null, null)
				, new LogElementFieldVisitEnd()
				, new LogElementClassVisitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL + ACC_ENUM, "CCC", "Lft/nevo/MyEnum;", null, null)
				, new LogElementFieldVisitEnd()
				, new LogElementClassVisitField(ACC_PRIVATE + ACC_STATIC + ACC_FINAL + ACC_SYNTHETIC, "ENUM$VALUES", "[Lft/nevo/MyEnum;", null, null)
				, new LogElementFieldVisitEnd()
				, new LogElementClassVisitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL + ACC_ENUM, "FFF", "Lft/nevo/MyEnum;", null, null)
				, new LogElementFieldVisitEnd()

				// Static constructor
				, new LogElementClassVisitMethod(ACC_STATIC + ACC_CONSTRUCTOR, "<clinit>", "V", null, null)
				, new LogElementMethodVisitCode()
				, new LogElementMethodVisitMaxs(6, 0)
				, new LogElementMethodVisitVarInsn(INSN_CONST_4, 5, 3)
				, new LogElementMethodVisitVarInsn(INSN_CONST_4, 4, 2)
				, new LogElementMethodVisitVarInsn(INSN_CONST_4, 3, 1)
				, new LogElementMethodVisitVarInsn(INSN_CONST_4, 2, 0)
				, new LogElementMethodVisitLabel(l0MyEnumClInit)
				, new LogElementMethodVisitLineNumber(1, l0MyEnumClInit)
				
				, new LogElementMethodVisitTypeInsn(INSN_NEW_INSTANCE, 0, 0, 0, "Lft/nevo/MyEnum;")
				, new LogElementMethodVisitStringInsn(INSN_CONST_STRING, 1, "BBB")
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Lft/nevo/MyEnum;", "<init>", "VLjava/lang/String;I", new int[] { 0, 1, 2 })
				, new LogElementMethodVisitFieldInsn(INSN_SPUT_OBJECT, "Lft/nevo/MyEnum;", "BBB", "Lft/nevo/MyEnum;", 0, 0)
				, new LogElementMethodVisitTypeInsn(INSN_NEW_INSTANCE, 0, 0, 0, "Lft/nevo/MyEnum;")
				
				, new LogElementMethodVisitStringInsn(INSN_CONST_STRING, 1, "FFF")
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Lft/nevo/MyEnum;", "<init>", "VLjava/lang/String;I", new int[] { 0, 1, 3 })
				, new LogElementMethodVisitFieldInsn(INSN_SPUT_OBJECT, "Lft/nevo/MyEnum;", "FFF", "Lft/nevo/MyEnum;", 0, 0)
				, new LogElementMethodVisitTypeInsn(INSN_NEW_INSTANCE, 0, 0, 0, "Lft/nevo/MyEnum;")

				, new LogElementMethodVisitStringInsn(INSN_CONST_STRING, 1, "CCC")
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Lft/nevo/MyEnum;", "<init>", "VLjava/lang/String;I", new int[] { 0, 1, 4 })
				, new LogElementMethodVisitFieldInsn(INSN_SPUT_OBJECT, "Lft/nevo/MyEnum;", "CCC", "Lft/nevo/MyEnum;", 0, 0)
				, new LogElementMethodVisitTypeInsn(INSN_NEW_INSTANCE, 0, 0, 0, "Lft/nevo/MyEnum;")
				
				, new LogElementMethodVisitStringInsn(INSN_CONST_STRING, 1, "AAA")
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Lft/nevo/MyEnum;", "<init>", "VLjava/lang/String;I", new int[] { 0, 1, 5 })
				, new LogElementMethodVisitFieldInsn(INSN_SPUT_OBJECT, "Lft/nevo/MyEnum;", "AAA", "Lft/nevo/MyEnum;", 0, 0)
				, new LogElementMethodVisitVarInsn(INSN_CONST_4, 0, 4)
				, new LogElementMethodVisitTypeInsn(INSN_NEW_ARRAY, 0, 0, 0, "[Lft/nevo/MyEnum;")
				
				, new LogElementMethodVisitFieldInsn(INSN_SGET_OBJECT, "Lft/nevo/MyEnum;", "BBB", "Lft/nevo/MyEnum;", 1, 0)
				, new LogElementMethodVisitArrayOperationInsn(INSN_APUT_OBJECT, 1, 0, 2)
				, new LogElementMethodVisitFieldInsn(INSN_SGET_OBJECT, "Lft/nevo/MyEnum;", "FFF", "Lft/nevo/MyEnum;", 1, 0)
				, new LogElementMethodVisitArrayOperationInsn(INSN_APUT_OBJECT, 1, 0, 3)
				, new LogElementMethodVisitFieldInsn(INSN_SGET_OBJECT, "Lft/nevo/MyEnum;", "CCC", "Lft/nevo/MyEnum;", 1, 0)
				, new LogElementMethodVisitArrayOperationInsn(INSN_APUT_OBJECT, 1, 0, 4)
				, new LogElementMethodVisitFieldInsn(INSN_SGET_OBJECT, "Lft/nevo/MyEnum;", "AAA", "Lft/nevo/MyEnum;", 1, 0)
				, new LogElementMethodVisitArrayOperationInsn(INSN_APUT_OBJECT, 1, 0, 5)
				, new LogElementMethodVisitFieldInsn(INSN_SPUT_OBJECT, "Lft/nevo/MyEnum;", "ENUM$VALUES", "[Lft/nevo/MyEnum;", 0, 0)
				, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
				, new LogElementMethodVisitEnd()
				
				// Constructor.
				, new LogElementClassVisitMethod(ACC_PRIVATE + ACC_CONSTRUCTOR, "<init>", "VLjava/lang/String;I", null, null)
				, new LogElementMethodVisitParameters(new String[] { "", "" })
				, new LogElementMethodVisitCode()
				, new LogElementMethodVisitMaxs(3, 0)
				, new LogElementMethodVisitLabel(l0MyEnumInit)
				, new LogElementMethodVisitLineNumber(1, l0MyEnumInit)
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Enum;", "<init>", "VLjava/lang/String;I", new int[] { 0, 1, 2 })
				, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
				, new LogElementMethodVisitEnd()
				
				// Method valueOf.
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_STATIC, "valueOf", "Lft/nevo/MyEnum;Ljava/lang/String;", null, null)
				, new LogElementMethodVisitParameters(new String[] { "" })
				, new LogElementMethodVisitCode()
				, new LogElementMethodVisitMaxs(2, 0)
				, new LogElementMethodVisitLabel(l0MyEnumValueOf)
				, new LogElementMethodVisitLineNumber(1, l0MyEnumValueOf)
				, new LogElementMethodVisitTypeInsn(INSN_CONST_CLASS, 0, 0, 0, "Lft/nevo/MyEnum;")
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_STATIC, "Ljava/lang/Enum;", "valueOf", "Ljava/lang/Enum;Ljava/lang/Class;Ljava/lang/String;", new int[] { 0, 1 })
				, new LogElementMethodVisitIntInsn(INSN_MOVE_RESULT_OBJECT, 1)
				, new LogElementMethodVisitTypeInsn(INSN_CHECK_CAST, 0, 1, 0, "Lft/nevo/MyEnum;")
				, new LogElementMethodVisitIntInsn(INSN_RETURN_OBJECT, 1)
				, new LogElementMethodVisitEnd()
				
				// Method value.
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_STATIC, "values", "[Lft/nevo/MyEnum;", null, null)
				, new LogElementMethodVisitCode()
				, new LogElementMethodVisitMaxs(4, 0)
				, new LogElementMethodVisitVarInsn(INSN_CONST_4, 3, 0)
				, new LogElementMethodVisitLabel(l0MyEnumValue)
				, new LogElementMethodVisitLineNumber(1, l0MyEnumValue)
				, new LogElementMethodVisitFieldInsn(INSN_SGET_OBJECT, "Lft/nevo/MyEnum;", "ENUM$VALUES", "[Lft/nevo/MyEnum;", 0, 0)
				, new LogElementMethodVisitArrayLengthInsn(1, 0)
				, new LogElementMethodVisitTypeInsn(INSN_NEW_ARRAY, 2, 0, 1, "[Lft/nevo/MyEnum;")
				, new LogElementMethodVisitMethodInsn(INSN_INVOKE_STATIC, "Ljava/lang/System;", "arraycopy", "VLjava/lang/Object;ILjava/lang/Object;II", new int[] { 0, 3, 2, 3, 1 })
				, new LogElementMethodVisitIntInsn(INSN_RETURN_OBJECT, 2)
				, new LogElementMethodVisitEnd()
				
				, new LogElementClassVisitEnd()
				
				
				// Second Annotation Class.
				, new LogElementApplicationVisitClass(ACC_PUBLIC + ACC_INTERFACE + ACC_ABSTRACT + ACC_ANNOTATION, "Lft/nevo/SecondAnnotation;", null, "Ljava/lang/Object;", new String[] { "Ljava/lang/annotation/Annotation;" })
				, new LogElementClassVisit(ACC_PUBLIC + ACC_INTERFACE + ACC_ABSTRACT + ACC_ANNOTATION, "Lft/nevo/SecondAnnotation;", null, "Ljava/lang/Object;", new String[] { "Ljava/lang/annotation/Annotation;" })
				, new LogElementClassVisitSource("SecondAnnotation.java", null)
				, new LogElementClassVisitAnnotation("Ljava/lang/annotation/Retention;", true)
				, new LogElementAnnotationVisitEnum("value", "Ljava/lang/annotation/RetentionPolicy;", "RUNTIME")
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "myStrings", "[Ljava/lang/String;", null, null)
				, new LogElementMethodVisitEnd()

				, new LogElementClassVisitEnd()

				
				// Third Annotation Class.
				, new LogElementApplicationVisitClass(ACC_PUBLIC + ACC_INTERFACE + ACC_ABSTRACT + ACC_ANNOTATION, "Lft/nevo/ThirdAnnotation;", null, "Ljava/lang/Object;", new String[] { "Ljava/lang/annotation/Annotation;" })
				, new LogElementClassVisit(ACC_PUBLIC + ACC_INTERFACE + ACC_ABSTRACT + ACC_ANNOTATION, "Lft/nevo/ThirdAnnotation;", null, "Ljava/lang/Object;", new String[] { "Ljava/lang/annotation/Annotation;" })
				, new LogElementClassVisitSource("ThirdAnnotation.java", null)
				, new LogElementClassVisitAnnotation("Ljava/lang/annotation/Retention;", true)
				, new LogElementAnnotationVisitEnum("value", "Ljava/lang/annotation/RetentionPolicy;", "RUNTIME")
				, new LogElementAnnotationVisitEnd()
				
				, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_ABSTRACT, "a", "I", null, null)
				, new LogElementMethodVisitEnd()

				, new LogElementClassVisitEnd()

				, new LogElementApplicationVisitEnd()

		};
	}

}
