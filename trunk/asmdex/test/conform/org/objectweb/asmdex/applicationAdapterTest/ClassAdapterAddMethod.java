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
package org.objectweb.asmdex.applicationAdapterTest;

import org.ow2.asmdex.ClassVisitor;
import org.ow2.asmdex.MethodVisitor;
import org.ow2.asmdex.Opcodes;

/**
 * Adapter Test that adds one "myMethod2" and make "onCreate" calls it.
 * 
 * @author Julien Névo
 */
public class ClassAdapterAddMethod extends ClassVisitor {

	private boolean isMethod2Created = false;
	
	/**
	 * Constructor
	 * @param cv
	 */
	public ClassAdapterAddMethod(ClassVisitor cv) {
		super(Opcodes.ASM4, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String[] signature,
			String[] exceptions) {
		
		// Create Method2, only once, whenever a method is found.
		if (!isMethod2Created) {
			MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "myMethod2", "V", null, null);
			if (mv != null) {
				mv.visitCode();
				mv.visitInsn(Opcodes.INSN_RETURN_VOID);
				mv.visitMaxs(1, 0);
				mv.visitEnd();
			}

			isMethod2Created = true;
		}
		
		// If "onCreate" is found, we create a new Method ("myMethod2"), and adds the call to
		// this one as well thanks to the next Adapter.
		if (name.equals("onCreate")) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			MethodAdapterAddMethod ma = new MethodAdapterAddMethod(mv);
			return ma;
		} else {
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}
}
