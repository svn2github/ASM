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

import org.objectweb.asmdex.AnnotationVisitor;
import org.objectweb.asmdex.FieldVisitor;
import org.objectweb.asmdex.logging.LogElementFieldVisitAnnotation;
import org.objectweb.asmdex.logging.LogElementFieldVisitEnd;
import org.objectweb.asmdex.logging.Logger;

/**
 * Field Visitor used to test the Reader.
 * 
 * @author Julien Névo
 */
public class FieldTestVisitor extends FieldVisitor {

	private Logger logger;
	
	/**
	 * Instantiates a new field test visitor.
	 *
	 * @param logger the logger
	 */
	public FieldTestVisitor(Logger logger) {
		this.logger = logger;
	}
	
	// ---------------------------------------------
	// Field Visitor
	// ---------------------------------------------
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		logger.foundElement(new LogElementFieldVisitAnnotation(desc, visible));
		return new AnnotationTestVisitor(logger);
	}

	@Override
	public void visitAttribute(Object attr) {
	}

	@Override
	public void visitEnd() {
		logger.foundElement(new LogElementFieldVisitEnd());
	}

}
