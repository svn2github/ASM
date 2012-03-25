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

import org.objectweb.asmdex.logging.LogElementAnnotationVisit;
import org.objectweb.asmdex.logging.LogElementAnnotationVisitAnnotation;
import org.objectweb.asmdex.logging.LogElementAnnotationVisitArray;
import org.objectweb.asmdex.logging.LogElementAnnotationVisitEnd;
import org.objectweb.asmdex.logging.LogElementAnnotationVisitEnum;
import org.objectweb.asmdex.logging.LogElementAnnotationVisitClass;
import org.objectweb.asmdex.logging.Logger;
import org.ow2.asmdex.AnnotationVisitor;
import org.ow2.asmdex.Opcodes;

/**
 * Annotation Visitor used to test the Reader.
 * 
 * @author Julien Névo
 */
public class AnnotationTestVisitor extends AnnotationVisitor {

	private Logger logger;
	
	/**
	 * Instantiates a new annotation test visitor.
	 *
	 * @param logger the logger
	 */
	public AnnotationTestVisitor(Logger logger) {
		super(Opcodes.ASM4);
		this.logger = logger;
	}
	
	// ---------------------------------------------
	// Annotation Visitor
	// ---------------------------------------------
	
	@Override
	public void visit(String name, Object value) {
		logger.foundElement(new LogElementAnnotationVisit(name, value));
	}

	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc) {
		logger.foundElement(new LogElementAnnotationVisitAnnotation(name, desc));
		return new AnnotationTestVisitor(logger);
	}

	@Override
	public AnnotationVisitor visitArray(String name) {
		logger.foundElement(new LogElementAnnotationVisitArray(name));
		return new AnnotationTestVisitor(logger);
	}

	@Override
	public void visitEnd() {
		logger.foundElement(new LogElementAnnotationVisitEnd());
	}

	@Override
	public void visitEnum(String name, String desc, String value) {
		logger.foundElement(new LogElementAnnotationVisitEnum(name, desc, value));
	}

	@Override
	public void visitClass(String annotationName, String className) {
		logger.foundElement(new LogElementAnnotationVisitClass(annotationName, className));
		
	}

}
