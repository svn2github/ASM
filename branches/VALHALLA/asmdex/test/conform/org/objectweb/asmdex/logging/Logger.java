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

import static org.junit.Assert.*;

/**
 * This class allows to log elements and compare them with a list coming from the Reader, reading
 * an original dex file. We can this way make sure that what the Reader produces exactly what we
 * expect.
 * 
 * @author Julien Névo
 */
public class Logger {

	private int index = 0;
	
	/**
	 * Elements that we have to be equal with.
	 */
	private LogElement[] loggedElements; 
	
	/**
	 * Constructor
	 * @param logList
	 */
	public Logger(LogList logList) {
		this.loggedElements = logList.getLogElements();
	}
	
	/**
	 * Indicates than an element has been visited. Performs a check as to its validity.
	 * @param element the newly visited element.
	 */
	public void foundElement(LogElement element) {
		assertTrue("Not enough elements in the log !", index < loggedElements.length);
		assertTrue("Element found (number " + index + ") not matching !", element.equals(loggedElements[index]));
		index++;
	}
	
	/**
	 * Indicates that the visit is finished. Performs a check of the number of elements found.
	 */
	public void logEnd() {
		assertEquals("Not the same amount of elements !", index, loggedElements.length);
	}

}
