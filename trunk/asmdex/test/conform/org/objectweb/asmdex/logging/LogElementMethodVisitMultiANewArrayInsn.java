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
 * LogElement for the Visit of a MultiANewArray Instruction of a Method.
 * 
 * @author Julien Névo
 */
public class LogElementMethodVisitMultiANewArrayInsn extends LogElement {

	/** The description. */
	protected String desc;
	
	/** The registers. */
	protected int[] registers;
	
	/**
	 * Instantiates a new log element method visit multi a new array insn.
	 *
	 * @param desc the desc
	 * @param registers the registers
	 */
	public LogElementMethodVisitMultiANewArrayInsn(String desc, int[] registers) {
		this.desc = desc;
		this.registers = registers;
	}

	@Override
	public ElementType getType() {
		return ElementType.TYPE_METHOD_VISIT_MULTI_A_NEW_ARRAY_INSN;
	}

	@Override
	public boolean isElementEqual(LogElement e) {
		LogElementMethodVisitMultiANewArrayInsn a = (LogElementMethodVisitMultiANewArrayInsn)e;
		return desc.equals(a.desc)
			&& Arrays.equals(registers, a.registers);
	}

}
