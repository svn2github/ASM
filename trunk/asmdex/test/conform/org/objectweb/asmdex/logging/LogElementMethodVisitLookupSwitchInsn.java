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

import org.ow2.asmdex.structureCommon.Label;

/**
 * Class representing a Log Element for a Sparse Switch Instruction.
 * 
 * ASM requires a Default label in its specification, but we can only "guess" it in dex (like,
 * for example, suppose it is the next instruction). As we can't be sure, it is not
 * compared here.
 * 
 * @author Julien Névo
 */
public class LogElementMethodVisitLookupSwitchInsn extends LogElement {

	/** The register. */
	protected int register;
	
	/** The default label. */
	protected Label dflt;
	
	/** The keys. */
	protected int[] keys;
	
	/** The labels. */
	protected Label[] labels;
	
	/**
	 * Instantiates a new log element method visit lookup switch insn.
	 *
	 * @param register the register
	 * @param dflt the dflt
	 * @param keys the keys
	 * @param labels the labels
	 */
	public LogElementMethodVisitLookupSwitchInsn(int register, Label dflt,
			int[] keys, Label[] labels) {
		this.register = register;
		this.dflt = dflt;
		this.keys = keys;
		this.labels = labels;
	}

	@Override
	public ElementType getType() {
		return ElementType.TYPE_METHOD_VISIT_LOOK_UP_SWITCH_INSN;
	}

	@Override
	public boolean isElementEqual(LogElement e) {
		LogElementMethodVisitLookupSwitchInsn a = (LogElementMethodVisitLookupSwitchInsn)e;
		return (register == a.register)
			//&& dflt.equals(a.dflt)
			&& Arrays.equals(keys, a.keys)
			&& Arrays.equals(labels, a.labels);
	}

}
