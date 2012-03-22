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
 * LogElement for the Visit of a Field Instruction of a Method.
 * 
 * @author Julien Névo
 */
public class LogElementMethodVisitFieldInsn extends LogElement {

	/** The opcode. */
	protected int opcode;
	
	/** The owner. */
	protected String owner;
	
	/** The name. */
	protected String name;
	
	/** The desc. */
	protected String desc;
	
	/** The value register. */
	protected int valueRegister;
	
	/** The object register. */
	protected int objectRegister;
	
	/**
	 * Constructor
	 * @param opcode
	 * @param owner
	 * @param name
	 * @param desc
	 * @param valueRegister
	 * @param objectRegister
	 */
	public LogElementMethodVisitFieldInsn(int opcode, String owner,
			String name, String desc, int valueRegister, int objectRegister) {
		this.opcode = opcode;
		this.owner = owner;
		this.name = name;
		this.desc = desc;
		this.valueRegister = valueRegister;
		this.objectRegister = objectRegister;
	}

	@Override
	public ElementType getType() {
		return ElementType.TYPE_METHOD_VISIT_FIELD_INSN;
	}

	@Override
	public boolean isElementEqual(LogElement e) {
		LogElementMethodVisitFieldInsn a = (LogElementMethodVisitFieldInsn)e;
		return (opcode == a.opcode)
			&& (valueRegister == a.valueRegister)
			&& (objectRegister == a.objectRegister)
			&& (owner.equals(a.owner))
			&& (name.equals(a.name))
			&& (desc.equals(a.desc));
	}

}
