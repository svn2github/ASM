/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000,2002,2003 INRIA, France Telecom
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

package org.objectweb.asm.tree.analysis;

/**
 * An immutable symbolic value for semantic interpretation of bytecode.
 * 
 * @author Eric Bruneton
 */

public interface Value {
  
  /**
   * Returns the size of this value in words.
   * 
   * @return either 1 or 2.
   */
  
  int getSize ();
  
  /**
   * Merges this value and the given value. The merge operation must return
   * a value that represents both values (for instance, if the two values are
   * two types, the merged value must be a common super type of the two types.
   * If the two values are integer intervals, the merged value must be an 
   * interval that contains the previous ones. Likewise for other types of 
   * values).  
   * 
   * @param value a value.
   * @return the merged value. If the merged value is equal to this value, this
   *      method <i>must</i> return <tt>this</tt>.
   */
  
  Value merge (Value value);
  
  /**
   * Compares this value with the given value.
   * 
   * @param value a value.
   * @return <tt>true</tt> if the values are equals, <tt>false</tt> otherwise.
   */
  
  boolean equals (Value value);
}
