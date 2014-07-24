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

/**
 * Abstract class of a Log Element. Each element is stored and can be compared to others, so that
 * we can know if we produced the expected output. This is useful to test the ApplicationReader.
 * 
 * @author Julien Névo
 */
public abstract class LogElement {

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public abstract ElementType getType();
	
	/**
	 * Checks if is element equal.
	 *
	 * @param e the e
	 * @return true, if elements are equal
	 */
	public abstract boolean isElementEqual(LogElement e);

	@Override
	public int hashCode() {
		  assert false : "hashCode not designed";
		  return 42; // Arbitrary constant. 
		}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof LogElement) {
			LogElement e = (LogElement)obj;
			
			if (getType() == e.getType()) {
				return e.isElementEqual(this);
			}
		}
				
		return false;
	}
	
	/**
	 * @author Julien Nevo
	 * Elements types
	 */
	public enum ElementType {
		// Application elements.
		/**
		 * application visit
		 */
		TYPE_APPLICATION_VISIT,
		/**
		 * visit class of app
		 */
		TYPE_APPLICATION_VISIT_CLASS,
		/**
		 * application visit finished
		 */
		TYPE_APPLICATION_VISIT_END,
		
		// Class elements.
		/**
		 * class visit
		 */
		TYPE_CLASS_VISIT,
		/**
		 * annotation of class visit
		 */
		TYPE_CLASS_VISIT_ANNOTATION,
		/**
		 * class visit finished
		 */
		TYPE_CLASS_VISIT_END,
		/**
		 * field of class visit
		 */
		TYPE_CLASS_VISIT_FIELD,
		/**
		 * inner class  visit
		 */
		TYPE_CLASS_VISIT_INNER_CLASS,
        /**
         * member class  visit
         */
        TYPE_CLASS_VISIT_MEMBER_CLASS,
		/**
		 * method visit
		 */
		TYPE_CLASS_VISIT_METHOD,
		/**
		 * outer class visit
		 */
		TYPE_CLASS_VISIT_OUTER_CLASS,
		/**
		 * source info visit
		 */
		TYPE_CLASS_VISIT_SOURCE,
		
		// Field elements.
		/**
		 * annotation of field visit
		 */
		TYPE_FIELD_VISIT_ANNOTATION,
		/**
		 * field visit finished
		 */
		TYPE_FIELD_VISIT_END,
		
		// Annotation elements.
		/**
		 * annotation visit
		 */
		TYPE_ANNOTATION_VISIT,
		/**
		 * annotation of annotation visit
		 */
		TYPE_ANNOTATION_VISIT_ANNOTATION,
		/**
		 * array of annotation visit
		 */
		TYPE_ANNOTATION_VISIT_ARRAY,
		/**
		 * annotation visit finished
		 */
		TYPE_ANNOTATION_VISIT_END,
		/**
		 * enum of annotation visit
		 */
		TYPE_ANNOTATION_VISIT_ENUM,
		/**
		 * class in annotation visit
		 */
		TYPE_ANNOTATION_VISIT_CLASS,
		
		// Method elements.
		/**
		 * annotation of method visit
		 */
		TYPE_METHOD_VISIT_ANNOTATION,
		/**
		 * default annotation of method visit
		 */
		TYPE_METHOD_VISIT_ANNOTATION_DEFAULT,
		/**
		 * array length visit
		 */
		TYPE_METHOD_VISIT_ARRAY_LENGTH_INSN,
		/**
		 * array op visit
		 */
		TYPE_METHOD_VISIT_ARRAY_OPERATION_INSN,
		/**
		 * code visit
		 */
		TYPE_METHOD_VISIT_CODE,
		/**
		 * visit of method finished
		 */
		TYPE_METHOD_VISIT_END,
		/**
		 * visit field instruction
		 */
		TYPE_METHOD_VISIT_FIELD_INSN,
		/**
		 * visit fill data array
		 */
		TYPE_METHOD_VISIT_FILL_DATA_ARRAY_INSN,
		/**
		 * visit instruction
		 */
		TYPE_METHOD_VISIT_INSN,
		/**
		 * visit int instruction
		 */
		TYPE_METHOD_VISIT_INT_INSN,
		/**
		 * visit jump instrution
		 */
		TYPE_METHOD_VISIT_JUMP_INSN,
		/**
		 * visit label
		 */
		TYPE_METHOD_VISIT_LABEL,
		/**
		 * visit line number
		 */
		TYPE_METHOD_VISIT_LINE_NUMBER_INSN,
		/**
		 * visit local variable
		 */
		TYPE_METHOD_VISIT_LOCAL_VARIABLE,
		/**
		 * visit local variable list
		 */
		TYPE_METHOD_VISIT_LOCAL_VARIABLE_LIST,
		/**
		 * visit lookup swicth
		 */
		TYPE_METHOD_VISIT_LOOK_UP_SWITCH_INSN,
		/**
		 * visit max stack
		 */
		TYPE_METHOD_VISIT_MAXS,
		/**
		 * visit method instruction
		 */
		TYPE_METHOD_VISIT_METHOD_INSN,
		/**
		 * visit multi new array
		 */
		TYPE_METHOD_VISIT_MULTI_A_NEW_ARRAY_INSN,
		/**
		 * visit operation
		 */
		TYPE_METHOD_VISIT_OPERATION_INSN,
		/**
		 * visit operation with one parameter
		 */
		TYPE_METHOD_VISIT_PARAMETER_OPERATION,
		/**
		 * visit operation with parameters
		 */
		TYPE_METHOD_VISIT_PARAMETERS,
		/**
		 * visit string instruction
		 */
		TYPE_METHOD_VISIT_STRING_INSN,
		/**
		 * visit table switch
		 */
		TYPE_METHOD_VISIT_TABLE_SWITCH_INSN,
		/**
		 * visit try catch block
		 */
		TYPE_METHOD_VISIT_TRY_CATCH_BLOCK,
		/**
		 * visit type instruction
		 */
		TYPE_METHOD_VISIT_TYPE_INSN,
		/**
		 * visit var instruction
		 */
		TYPE_METHOD_VISIT_VAR_INSN,
		/**
		 * visit long var instruction
		 */
		TYPE_METHOD_VISIT_VAR_LONG_INSN,
	};
	
}
