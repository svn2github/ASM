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

import java.lang.reflect.Array;

/**
 * LogElement for the Visit of an Annotation.
 * 
 * @author Julien Névo
 */
public class LogElementAnnotationVisit extends LogElement {

	/** The name. */
	protected String name;
	
	/** The value. */
	protected Object value;

	@Override
	public ElementType getType() {
		return ElementType.TYPE_ANNOTATION_VISIT;
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param value
	 */
	public LogElementAnnotationVisit(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public boolean isElementEqual(LogElement e) {
		LogElementAnnotationVisit a = (LogElementAnnotationVisit)e;
		// Name can be null.
		if ((name != null) && (a.name != null)) {
			if (!name.equals(a.name)) {
				return false;
			}
		}
		
		if (!value.getClass().isArray()) {
			return value.equals(a.value);
		} else {
			// If the object is an array, we have to compare all its elements.
			Class<? extends Object> type1 = value.getClass();
			Class<? extends Object> type2 = a.value.getClass();
			if (!type1.equals(type2)) {
				return false;
			}
				
			int length = Array.getLength(value);
			if (length != Array.getLength(a.value)) {
				return false;
			}
	        Class<? extends Object> componentType = type1.getComponentType();
	        if (!componentType.equals(type2.getComponentType())) {
				return false;
			}
	        
	        for (int i = 0; i < length; i++) {
	        	if (!Array.get(value, i).equals(Array.get(a.value, i))) {
	        		return false;
	        	}
	        }
			
			return true;
		}
		
	}
	

}
