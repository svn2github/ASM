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

import java.util.List;

import org.ow2.asmdex.structureCommon.Label;

/**
 * LogElement for the Visit of a LocalVariable Instruction of a Method, as a List.
 * 
 * @author Julien Névo
 */
public class LogElementMethodVisitLocalVariableList extends LogElement {

	/** The name. */
	protected String name;
	
	/** The description. */
	protected String desc;
	
	/** The signature. */
	protected String signature;
	
	/** The start label. */
	protected Label start;
	
	/** The ends labels. */
	protected List<Label> ends;
	
	/** The restarts labels. */
	protected List<Label> restarts;
	
	/** The index. */
	protected int index;
	
	/**
	 * Instantiates a new log element method visit local variable list.
	 *
	 * @param name the name
	 * @param desc the desc
	 * @param signature the signature
	 * @param start the start
	 * @param ends the ends
	 * @param restarts the restarts
	 * @param index the index
	 */
	public LogElementMethodVisitLocalVariableList(String name, String desc,
			String signature, Label start, List<Label> ends,
			List<Label> restarts, int index) {
		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.start = start;
		this.ends = ends;
		this.restarts = restarts;
		this.index = index;
	}

	@Override
	public ElementType getType() {
		return ElementType.TYPE_METHOD_VISIT_LOCAL_VARIABLE_LIST;
	}

	@Override
	public boolean isElementEqual(LogElement e) {
		LogElementMethodVisitLocalVariableList a = (LogElementMethodVisitLocalVariableList)e;
		
		if (ends == null) {
			if (a.ends != null) {
				return false;
			}
		} else if (!ends.equals(a.ends)) {
			return false;
		}

		if (restarts == null) {
			if (a.restarts != null) {
				return false;
			}
		} else if (!restarts.equals(a.restarts)) {
			return false;
		}

		if (signature == null) {
			if (a.signature != null) {
				return false;
			}
		} else if (!signature.equals(a.signature)) {
			return false;
		}
		
		return name.equals(a.name)
			&& desc.equals(a.desc)
			&& start.equals(a.start)
			&& (index == a.index);
	}

}
