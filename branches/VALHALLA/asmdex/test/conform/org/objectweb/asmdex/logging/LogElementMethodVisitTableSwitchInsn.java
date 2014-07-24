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
 * LogElement for the Visit of a TableSwitch Instruction of a Method.
 * 
 * @author Julien Névo
 */
import java.util.Arrays;

import org.ow2.asmdex.structureCommon.Label;

/**
 * Class representing a Log Element for a Packed Switch Instruction.
 * 
 * ASM requires a Default label in its specification, but we can only "guess" it in dex (like,
 * for example, suppose it is the next instruction). As we can't be sure, it is not
 * compared here.
 * 
 * @author Julien Névo
 */
public class LogElementMethodVisitTableSwitchInsn extends LogElement {

	/** The register. */
	protected int register;
	
	/** The min. */
	protected int min;
	
	/** The max. */
	protected int max;
	
	/** The default. */
	protected Label dflt;
	
	/** The labels. */
	protected Label[] labels;
	
	/**
	 * Constructor
	 * @param register
	 * @param min
	 * @param max
	 * @param dflt
	 * @param labels
	 */
	public LogElementMethodVisitTableSwitchInsn(int register, int min, int max,
			Label dflt, Label[] labels) {
		this.register = register;
		this.min = min;
		this.max = max;
		this.dflt = dflt;
		this.labels = labels;
	}

	@Override
	public ElementType getType() {
		return ElementType.TYPE_METHOD_VISIT_TABLE_SWITCH_INSN;
	}

	@Override
	public boolean isElementEqual(LogElement e) {
		LogElementMethodVisitTableSwitchInsn a = (LogElementMethodVisitTableSwitchInsn)e;
		return (register == a.register)
			&& (min == a.min)
			&& (max == a.max)
			&& Arrays.equals(labels, a.labels);
	}

}
