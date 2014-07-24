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
 * Log List of an "Hello World" application type.
 *  
 * @author Julien Névo
 */
public class LogListHelloWorld implements LogList {

	private Label l0FirstActivityInit = new Label();
	private Label l0FirstActivityOnCreate = new Label();
	private Label l1FirstActivityOnCreate = new Label();
	private Label l2FirstActivityOnCreate = new Label();
	
	private Label l0RAttrInit = new Label();
	private Label l0RDrawableInit = new Label();
	private Label l0RLayoutInit = new Label();
	private Label l0RStringInit = new Label();
	private Label l0RInit = new Label();

	/**
	 * Instantiates a new log list.
	 */
	public LogListHelloWorld() {
		l0FirstActivityInit.setOffset(0);
		
		l0FirstActivityOnCreate.setOffset(0);
		l1FirstActivityOnCreate.setOffset(6);
		l2FirstActivityOnCreate.setOffset(0x10);
		
		l0RAttrInit.setOffset(0);
		l0RDrawableInit.setOffset(0);
		l0RLayoutInit.setOffset(0);
		l0RStringInit.setOffset(0);
		l0RInit.setOffset(0);
	}

	
	@Override
	public String[] getClassesToParse() {
		return null;
	}

	@Override
	public String getDexFile() {
		return TestUtil.PATH_AND_FILENAME_HELLO_WORLD_DEX;
	}
	
	@Override
	public LogElement[] getLogElements() {
	
		return new LogElement[] {
			new LogElementApplicationVisit() 	// aw.visit();
			// Class FirstActivity
			, new LogElementApplicationVisitClass(ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null)	// cv = aw.visitClass(ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null);
			, new LogElementClassVisit(ACC_PUBLIC, "Lft/nevo/FirstActivity;", null, "Landroid/app/Activity;", null)
			, new LogElementClassVisitSource("FirstActivity.java", null) // cv.visitSource("FirstActivity.java", null);
			
			// Method <init>
			, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null)
			, new LogElementMethodVisitCode()
			, new LogElementMethodVisitMaxs(1, 0)
			, new LogElementMethodVisitLabel(l0FirstActivityInit)
			, new LogElementMethodVisitLineNumber(6, l0FirstActivityInit)
			, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Landroid/app/Activity;", "<init>", "V", new int[] { 0 })
			, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
			, new LogElementMethodVisitEnd()
			
			// Method onCreate - element 12.
			/*
			    mv = cv.visitMethod(ACC_PUBLIC, "onCreate", "VLandroid/os/Bundle;", null, null);
				mv.visitCode();
				mv.visitParameters(new String[] { "savedInstanceState" });
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(10, l0);
				mv.visitMethodInsn(INSN_INVOKE_SUPER, "Landroid/app/Activity;", "onCreate", "VLandroid/os/Bundle;", new int[] { 1, 2 });
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(11, l1);
				mv.visitVarInsn(INSN_CONST_HIGH16, 0, 2130903040);
				mv.visitMethodInsn(INSN_INVOKE_VIRTUAL, "Lft/nevo/FirstActivity;", "setContentView", "VI", new int[] { 1, 0 });
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(14, l2);
				mv.visitInsn(INSN_RETURN_VOID);
				mv.visitMaxs(3, 0);
				mv.visitEnd();
			 */
			, new LogElementClassVisitMethod(ACC_PUBLIC, "onCreate", "VLandroid/os/Bundle;", null, null)
			, new LogElementMethodVisitParameters(new String[] { "savedInstanceState" })
			, new LogElementMethodVisitCode()
			, new LogElementMethodVisitMaxs(3, 0)
			, new LogElementMethodVisitLabel(l0FirstActivityOnCreate)
			, new LogElementMethodVisitLineNumber(10, l0FirstActivityOnCreate)
			, new LogElementMethodVisitMethodInsn(INSN_INVOKE_SUPER, "Landroid/app/Activity;", "onCreate", "VLandroid/os/Bundle;", new int[] { 1, 2 })
			, new LogElementMethodVisitLabel(l1FirstActivityOnCreate)
			, new LogElementMethodVisitLineNumber(11, l1FirstActivityOnCreate)
			, new LogElementMethodVisitVarInsn(INSN_CONST_HIGH16, 0, 2130903040)
			, new LogElementMethodVisitMethodInsn(INSN_INVOKE_VIRTUAL, "Lft/nevo/FirstActivity;", "setContentView", "VI", new int[] { 1, 0 })
			, new LogElementMethodVisitLabel(l2FirstActivityOnCreate)
			, new LogElementMethodVisitLineNumber(14, l2FirstActivityOnCreate)
			, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
			, new LogElementMethodVisitEnd()
			
			, new LogElementClassVisitEnd()
			
			
			// Class R$attr - element 28
//			cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$attr;", null, "Ljava/lang/Object;", null);
//			cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$attr;", null, "Ljava/lang/Object;", null);
//			cv.visitSource("R.java", null);
//			cv.visitInnerClass("Lft/nevo/R$attr;", "Lft/nevo/R;", "attr", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
//			{
//				mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
//				mv.visitCode();
//				Label l0 = new Label();
//				mv.visitLabel(l0);
//				mv.visitLineNumber(11, l0);
//				mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
//				mv.visitInsn(INSN_RETURN_VOID);
//				mv.visitMaxs(1, 0);
//				mv.visitEnd();
//			}
//			cv.visitEnd();
			
			, new LogElementApplicationVisitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$attr;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisit(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$attr;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisitSource("R.java", null)
			, new LogElementClassVisitInnerClass("Lft/nevo/R$attr;", "Lft/nevo/R;", "attr", ACC_PUBLIC + ACC_STATIC + ACC_FINAL)
			
			// Method <init>
			, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null)
			, new LogElementMethodVisitCode()
			, new LogElementMethodVisitMaxs(1, 0)
			, new LogElementMethodVisitLabel(l0RAttrInit)
			, new LogElementMethodVisitLineNumber(11, l0RAttrInit)
			, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 })
			, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
			, new LogElementMethodVisitEnd()
			
			, new LogElementClassVisitEnd()
			
			
			// Class R$drawable - element 41
//			cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$drawable;", null, "Ljava/lang/Object;", null);
//			cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$drawable;", null, "Ljava/lang/Object;", null);
//			cv.visitSource("R.java", null);
//			cv.visitInnerClass("Lft/nevo/R$drawable;", "Lft/nevo/R;", "drawable", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
//			{
//				fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "icon", "I", null, 2130837504);
//				fv.visitEnd();
//			}
//			{
//				mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
//				mv.visitCode();
//				Label l0 = new Label();
//				mv.visitLabel(l0);
//				mv.visitLineNumber(13, l0);
//				mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
//				mv.visitInsn(INSN_RETURN_VOID);
//				mv.visitMaxs(1, 0);
//				mv.visitEnd();
//			}
//			cv.visitEnd();
			
			, new LogElementApplicationVisitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$drawable;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisit(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$drawable;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisitSource("R.java", null)
			, new LogElementClassVisitInnerClass("Lft/nevo/R$drawable;", "Lft/nevo/R;", "drawable", ACC_PUBLIC + ACC_STATIC + ACC_FINAL)
			
			, new LogElementClassVisitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "icon", "I", null, 2130837504)
			, new LogElementFieldVisitEnd()
			
			// Method <init>
			, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null)
			, new LogElementMethodVisitCode()
			, new LogElementMethodVisitMaxs(1, 0)
			, new LogElementMethodVisitLabel(l0RDrawableInit)
			, new LogElementMethodVisitLineNumber(13, l0RDrawableInit)
			, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 })
			, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
			, new LogElementMethodVisitEnd()
			
			, new LogElementClassVisitEnd()
			
			
			// Class R$layout - element 56
			
//			cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$layout;", null, "Ljava/lang/Object;", null);
//			cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$layout;", null, "Ljava/lang/Object;", null);
//			cv.visitSource("R.java", null);
//			cv.visitInnerClass("Lft/nevo/R$layout;", "Lft/nevo/R;", "layout", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
//			{
//				fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "main", "I", null, 2130903040);
//				fv.visitEnd();
//			}
//			{
//				mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
//				mv.visitCode();
//				Label l0 = new Label();
//				mv.visitLabel(l0);
//				mv.visitLineNumber(16, l0);
//				mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
//				mv.visitInsn(INSN_RETURN_VOID);
//				mv.visitMaxs(1, 0);
//				mv.visitEnd();
//			}
//			cv.visitEnd();
			
			, new LogElementApplicationVisitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$layout;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisit(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$layout;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisitSource("R.java", null)
			, new LogElementClassVisitInnerClass("Lft/nevo/R$layout;", "Lft/nevo/R;", "layout", ACC_PUBLIC + ACC_STATIC + ACC_FINAL)
			
			, new LogElementClassVisitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "main", "I", null, 2130903040)
			, new LogElementFieldVisitEnd()
			
			// Method <init>
			, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null)
			, new LogElementMethodVisitCode()
			, new LogElementMethodVisitMaxs(1, 0)
			, new LogElementMethodVisitLabel(l0RLayoutInit)
			, new LogElementMethodVisitLineNumber(16, l0RLayoutInit)
			, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 })
			, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
			, new LogElementMethodVisitEnd()
			
			, new LogElementClassVisitEnd()
			
			
			
			
			// Class R$string - element 71
			
//			cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$string;", null, "Ljava/lang/Object;", null);
//			cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$string;", null, "Ljava/lang/Object;", null);
//			cv.visitSource("R.java", null);
//			cv.visitInnerClass("Lft/nevo/R$string;", "Lft/nevo/R;", "string", ACC_PUBLIC + ACC_STATIC + ACC_FINAL);
//			{
//				fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "app_name", "I", null, 2130968577);
//				fv.visitEnd();
//			}
//			{
//				fv = cv.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "hello", "I", null, 2130968576);
//				fv.visitEnd();
//			}
//			{
//				mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
//				mv.visitCode();
//				Label l0 = new Label();
//				mv.visitLabel(l0);
//				mv.visitLineNumber(19, l0);
//				mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
//				mv.visitInsn(INSN_RETURN_VOID);
//				mv.visitMaxs(1, 0);
//				mv.visitEnd();
//			}
//			cv.visitEnd();
			
			, new LogElementApplicationVisitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$string;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisit(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R$string;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisitSource("R.java", null)
			, new LogElementClassVisitInnerClass("Lft/nevo/R$string;", "Lft/nevo/R;", "string", ACC_PUBLIC + ACC_STATIC + ACC_FINAL)
			
			, new LogElementClassVisitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "app_name", "I", null, 2130968577)
			, new LogElementFieldVisitEnd()
			, new LogElementClassVisitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "hello", "I", null, 2130968576)
			, new LogElementFieldVisitEnd()
			
			// Method <init>
			, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null)
			, new LogElementMethodVisitCode()
			, new LogElementMethodVisitMaxs(1, 0)
			, new LogElementMethodVisitLabel(l0RStringInit)
			, new LogElementMethodVisitLineNumber(19, l0RStringInit)
			, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 })
			, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
			, new LogElementMethodVisitEnd()
			
			, new LogElementClassVisitEnd()
			
			
			
			// Class R
			
//			cv = aw.visitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R;", null, "Ljava/lang/Object;", null);
//			cv.visit(0, ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R;", null, "Ljava/lang/Object;", null);
//			cv.visitSource("R.java", null);
//			cv.visitInnerClass("Lft/nevo/R$attr;", "Lft/nevo/R;", "attr", ACC_PUBLIC + ACC_FINAL);
//			cv.visitInnerClass("Lft/nevo/R$drawable;", "Lft/nevo/R;", "drawable", ACC_PUBLIC + ACC_FINAL);
//			cv.visitInnerClass("Lft/nevo/R$layout;", "Lft/nevo/R;", "layout", ACC_PUBLIC + ACC_FINAL);
//			cv.visitInnerClass("Lft/nevo/R$string;", "Lft/nevo/R;", "string", ACC_PUBLIC + ACC_FINAL);
//			{
//				mv = cv.visitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null);
//				mv.visitCode();
//				Label l0 = new Label();
//				mv.visitLabel(l0);
//				mv.visitLineNumber(10, l0);
//				mv.visitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 });
//				mv.visitInsn(INSN_RETURN_VOID);
//				mv.visitMaxs(1, 0);
//				mv.visitEnd();
//			}
//			cv.visitEnd();
			
			, new LogElementApplicationVisitClass(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisit(ACC_PUBLIC + ACC_FINAL, "Lft/nevo/R;", null, "Ljava/lang/Object;", null)
			, new LogElementClassVisitSource("R.java", null)
			, new LogElementClassVisitMemberClass("Lft/nevo/R$attr;", "Lft/nevo/R;", "attr")
			, new LogElementClassVisitMemberClass("Lft/nevo/R$drawable;", "Lft/nevo/R;", "drawable")
			, new LogElementClassVisitMemberClass("Lft/nevo/R$layout;", "Lft/nevo/R;", "layout")
			, new LogElementClassVisitMemberClass("Lft/nevo/R$string;", "Lft/nevo/R;", "string")
			
			// Method <init>
			, new LogElementClassVisitMethod(ACC_PUBLIC + ACC_CONSTRUCTOR, "<init>", "V", null, null)
			, new LogElementMethodVisitCode()
			, new LogElementMethodVisitMaxs(1, 0)
			, new LogElementMethodVisitLabel(l0RInit)
			, new LogElementMethodVisitLineNumber(10, l0RInit)
			, new LogElementMethodVisitMethodInsn(INSN_INVOKE_DIRECT, "Ljava/lang/Object;", "<init>", "V", new int[] { 0 })
			, new LogElementMethodVisitInsn(INSN_RETURN_VOID)
			, new LogElementMethodVisitEnd()
			
			, new LogElementClassVisitEnd()
			
			
			
			
			, new LogElementApplicationVisitEnd()
		};
			
	}

}
