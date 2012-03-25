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

import java.util.List;

import org.objectweb.asmdex.logging.LogElementMethodVisitAnnotation;
import org.objectweb.asmdex.logging.LogElementMethodVisitAnnotationDefault;
import org.objectweb.asmdex.logging.LogElementMethodVisitArrayLengthInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitArrayOperationInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitCode;
import org.objectweb.asmdex.logging.LogElementMethodVisitEnd;
import org.objectweb.asmdex.logging.LogElementMethodVisitFieldInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitFillArrayDataInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitIntInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitJumpInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitLabel;
import org.objectweb.asmdex.logging.LogElementMethodVisitLineNumber;
import org.objectweb.asmdex.logging.LogElementMethodVisitLocalVariable;
import org.objectweb.asmdex.logging.LogElementMethodVisitLocalVariableList;
import org.objectweb.asmdex.logging.LogElementMethodVisitLookupSwitchInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitMaxs;
import org.objectweb.asmdex.logging.LogElementMethodVisitMethodInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitMultiANewArrayInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitOperationInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitParameterAnnotation;
import org.objectweb.asmdex.logging.LogElementMethodVisitParameters;
import org.objectweb.asmdex.logging.LogElementMethodVisitStringInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitTableSwitchInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitTryCatchBlock;
import org.objectweb.asmdex.logging.LogElementMethodVisitTypeInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitVarInsn;
import org.objectweb.asmdex.logging.LogElementMethodVisitVarLongInsn;
import org.objectweb.asmdex.logging.Logger;
import org.ow2.asmdex.AnnotationVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;
import org.ow2.asmdex.structureCommon.Label;

/**
 * Method Visitor used to test the Reader.
 * 
 * @author Julien Névo
 */
public class MethodTestVisitor extends MethodVisitor {

	private Logger logger;
	
	/**
	 * Instantiates a new method test visitor.
	 *
	 * @param logger the logger
	 */
	public MethodTestVisitor(Logger logger) {
		super(Opcodes.ASM4);
		this.logger = logger;
	}
	
	// ---------------------------------------------
	// Class Visitor
	// ---------------------------------------------
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		logger.foundElement(new LogElementMethodVisitAnnotation(desc, visible));
		return new AnnotationTestVisitor(logger);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		logger.foundElement(new LogElementMethodVisitAnnotationDefault());
		return new AnnotationTestVisitor(logger);
	}

	@Override
	public void visitArrayLengthInsn(int destinationRegister, int arrayReferenceBearing) {
		logger.foundElement(new LogElementMethodVisitArrayLengthInsn(destinationRegister, arrayReferenceBearing));
	}

	@Override
	public void visitArrayOperationInsn(int opcode, int valueRegister,
			int arrayRegister, int indexRegister) {
		logger.foundElement(new LogElementMethodVisitArrayOperationInsn(opcode, valueRegister,
				arrayRegister, indexRegister));
	}

	@Override
	public void visitAttribute(Object attr) {
	}

	@Override
	public void visitCode() {
		logger.foundElement(new LogElementMethodVisitCode());
	}

	@Override
	public void visitEnd() {
		logger.foundElement(new LogElementMethodVisitEnd());
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name,
			String desc, int valueRegister, int objectRegister) {
		logger.foundElement(new LogElementMethodVisitFieldInsn(opcode, owner, name, desc,
				valueRegister, objectRegister));
	}

	@Override
	public void visitFillArrayDataInsn(int arrayReference, Object[] arrayData) {
		logger.foundElement(new LogElementMethodVisitFillArrayDataInsn(arrayReference, arrayData));
	}

	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack,
			Object[] stack) {
	}

	@Override
	public void visitInsn(int opcode) {
		logger.foundElement(new LogElementMethodVisitInsn(opcode));
	}

	@Override
	public void visitIntInsn(int opcode, int register) {
		logger.foundElement(new LogElementMethodVisitIntInsn(opcode, register));
	}

	@Override
	public void visitJumpInsn(int opcode, Label label, int registerA, int registerB) {
		logger.foundElement(new LogElementMethodVisitJumpInsn(opcode, label, registerA, registerB));
	}

	@Override
	public void visitLabel(Label label) {
		logger.foundElement(new LogElementMethodVisitLabel(label));
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		logger.foundElement(new LogElementMethodVisitLineNumber(line, start));
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature,
			Label start, Label end, int index) {
		logger.foundElement(new LogElementMethodVisitLocalVariable(name, desc, signature,
				start, end, index));
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature,
			Label start, List<Label> ends, List<Label> restarts, int index) {
		logger.foundElement(new LogElementMethodVisitLocalVariableList(name, desc, signature,
				start, ends, restarts, index));
	}

	@Override
	public void visitLookupSwitchInsn(int register, Label dflt, int[] keys,
			Label[] labels) {
		logger.foundElement(new LogElementMethodVisitLookupSwitchInsn(register, dflt, keys,
				labels));
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		logger.foundElement(new LogElementMethodVisitMaxs(maxStack, maxLocals));
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, int[] arguments) {
		logger.foundElement(new LogElementMethodVisitMethodInsn(opcode, owner, name, desc, arguments));
	}

	@Override
	public void visitMultiANewArrayInsn(String desc, int[] registers) {
		logger.foundElement(new LogElementMethodVisitMultiANewArrayInsn(desc, registers));
	}

	@Override
	public void visitOperationInsn(int opcode, int destinationRegister,
			int firstSourceRegister, int secondSourceRegister, int value) {
		logger.foundElement(new LogElementMethodVisitOperationInsn(opcode, destinationRegister,
			firstSourceRegister, secondSourceRegister, value));
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter,
			String desc, boolean visible) {
		logger.foundElement(new LogElementMethodVisitParameterAnnotation(parameter, desc, visible));
		return new AnnotationTestVisitor(logger);
	}

	@Override
	public void visitParameters(String[] parameters) {
		logger.foundElement(new LogElementMethodVisitParameters(parameters));
	}

	@Override
	public void visitStringInsn(int opcode, int destinationRegister,
			String string) {
		logger.foundElement(new LogElementMethodVisitStringInsn(opcode, destinationRegister, string));
	}

	@Override
	public void visitTableSwitchInsn(int register, int min, int max,
			Label dflt, Label[] labels) {
		logger.foundElement(new LogElementMethodVisitTableSwitchInsn(register, min, max, dflt, labels));
	}

	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler,
			String type) {
		logger.foundElement(new LogElementMethodVisitTryCatchBlock(start, end, handler, type));
	}

	@Override
	public void visitTypeInsn(int opcode, int destinationRegister,
			int referenceBearingRegister, int sizeRegister, String type) {
		logger.foundElement(new LogElementMethodVisitTypeInsn(opcode, destinationRegister,
				referenceBearingRegister, sizeRegister, type));
	}

	@Override
	public void visitVarInsn(int opcode, int destinationRegister, int var) {
		logger.foundElement(new LogElementMethodVisitVarInsn(opcode, destinationRegister, var));
	}

	@Override
	public void visitVarInsn(int opcode, int destinationRegister, long var) {
		logger.foundElement(new LogElementMethodVisitVarLongInsn(opcode, destinationRegister, var));
	}

}
