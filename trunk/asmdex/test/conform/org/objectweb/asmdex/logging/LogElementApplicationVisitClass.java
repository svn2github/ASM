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
package org.objectweb.asmdex.logging;

import java.util.Arrays;

/**
 * LogElement for the Visit of a Class of an Application.
 * 
 * @author Julien Névo
 */
public class LogElementApplicationVisitClass extends LogElement {

	/** The access flags. */
	protected int access;
	
	/** The name. */
	protected String name;
	
	/** The signature. */
	protected String[] signature;
	
	/** The name of super. */
	protected String superName;
	
	/** The interfaces. */
	protected String[] interfaces;
	
	@Override
	public ElementType getType() {
		return ElementType.TYPE_APPLICATION_VISIT_CLASS;
	}
	
	/**
	 * Constructor
	 * @param access
	 * @param name
	 * @param signature
	 * @param superName
	 * @param interfaces
	 */
	public LogElementApplicationVisitClass(int access, String name, String[] signature,
			String superName, String[] interfaces) {
		this.access = access;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		this.interfaces = interfaces;
	}
	
	@Override
	public boolean isElementEqual(LogElement e) {
		LogElementApplicationVisitClass a = (LogElementApplicationVisitClass)e;
		return (access == a.access) &&
			(name.equals(a.name)) &&
			(Arrays.equals(signature, a.signature)) &&
			(superName.equals(a.superName)) &&
			(Arrays.equals(interfaces, a.interfaces));
	}
}
