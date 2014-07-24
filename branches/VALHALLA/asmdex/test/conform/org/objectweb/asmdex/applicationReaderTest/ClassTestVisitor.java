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

import org.objectweb.asmdex.logging.LogElementClassVisit;
import org.objectweb.asmdex.logging.LogElementClassVisitAnnotation;
import org.objectweb.asmdex.logging.LogElementClassVisitEnd;
import org.objectweb.asmdex.logging.LogElementClassVisitField;
import org.objectweb.asmdex.logging.LogElementClassVisitInnerClass;
import org.objectweb.asmdex.logging.LogElementClassVisitMemberClass;
import org.objectweb.asmdex.logging.LogElementClassVisitMethod;
import org.objectweb.asmdex.logging.LogElementClassVisitOuterClass;
import org.objectweb.asmdex.logging.LogElementClassVisitSource;
import org.objectweb.asmdex.logging.Logger;
import org.ow2.asmdex.AnnotationVisitor;
import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.FieldVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

/**
 * Class Visitor used to test the Reader.
 * 
 * @author Julien Névo
 */
public class ClassTestVisitor extends ClassVisitor {

	private Logger logger;
	
	/**
	 * Instantiates a new class test visitor.
	 *
	 * @param logger the logger
	 */
	public ClassTestVisitor(Logger logger) {
		super(Opcodes.ASM4);
		this.logger = logger;
	}
	
	
	// ---------------------------------------------
	// Class Visitor
	// ---------------------------------------------
	
	@Override
	public void visit(int version, int access, String name, String[] signature,
			String superName, String[] interfaces) {
		logger.foundElement(new LogElementClassVisit(access, name, signature, superName, interfaces));
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		logger.foundElement(new LogElementClassVisitAnnotation(desc, visible));
		return new AnnotationTestVisitor(logger);
	}

	@Override
	public void visitAttribute(Object attr) {
	}

	@Override
	public void visitEnd() {
		logger.foundElement(new LogElementClassVisitEnd());
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String[] signature, Object value) {
		logger.foundElement(new LogElementClassVisitField(access, name, desc, signature, value));
		return new FieldTestVisitor(logger);
	}

	@Override
	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
		logger.foundElement(new LogElementClassVisitInnerClass(name, outerName, innerName, access));
	}

	   @Override
	    public void visitMemberClass(String name, String outerName, String innerName) {
	        logger.foundElement(new LogElementClassVisitMemberClass(name, outerName, innerName));
	    }
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String[] signature, String[] exceptions) {
		logger.foundElement(new LogElementClassVisitMethod(access, name, desc, signature, exceptions));
		return new MethodTestVisitor(logger);
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
		logger.foundElement(new LogElementClassVisitOuterClass(owner, name, desc));
	}

	@Override
	public void visitSource(String source, String debug) {
		logger.foundElement(new LogElementClassVisitSource(source, debug));
	}

}
