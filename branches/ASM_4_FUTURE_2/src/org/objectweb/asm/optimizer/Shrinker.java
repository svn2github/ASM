/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2007 INRIA, France Telecom
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
package org.objectweb.asm.optimizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodType;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.SimpleRemapper;

/**
 * A class file shrinker utility.
 *
 * @author Eric Bruneton
 * @author Eugene Kuleshov
 */
public class Shrinker {

    static final HashMap<String, String> MAPPING = new HashMap<String, String>();

    public static void main(final String[] args) throws IOException {
        Properties properties = new Properties();
        int n = args.length - 1;
        for (int i = 0; i < n - 1; ++i) {
            properties.load(new FileInputStream(args[i]));
        } 
        
        for(Map.Entry<Object, Object> entry: properties.entrySet()) {
            MAPPING.put((String)entry.getKey(), (String)entry.getValue());
        }
        
        final Set<String> unused = new HashSet<String>(MAPPING.keySet());

        File f = new File(args[n - 1]);
        File d = new File(args[n]);

        optimize(f, d, new SimpleRemapper(MAPPING) {
            public String map(String key) {
                String s = super.map(key);
                if (s != null) {
                    unused.remove(key);
                }
                return s;
            }
        });

        Iterator<String> i = unused.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (!s.endsWith("/remove")) {
                System.out.println("INFO: unused mapping " + s);
            }
        }
    }

    static void optimize(final File f, final File d, final Remapper remapper)
            throws IOException
    {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; ++i) {
                optimize(files[i], d, remapper);
            }
        } else if (f.getName().endsWith(".class")) {
            ConstantPool cp = new ConstantPool();
            ClassReader cr = new ClassReader(new FileInputStream(f));
            ClassWriter cw = new ClassWriter(0);
            ClassConstantsCollector ccc = new ClassConstantsCollector(cw, cp);
            ClassOptimizer co = new ClassOptimizer(ccc, remapper);
            cr.accept(co, ClassReader.SKIP_DEBUG);

            Set<Constant> constants = new TreeSet<Constant>(new ConstantComparator());
            constants.addAll(cp.values());

            cr = new ClassReader(cw.toByteArray());
            cw = new ClassWriter(0);
            Iterator<Constant> i = constants.iterator();
            while (i.hasNext()) {
                Constant c = i.next();
                c.write(cw);
            }
            cr.accept(cw, ClassReader.SKIP_DEBUG);

            if (MAPPING.get(cr.getClassName() + "/remove") != null) {
                return;
            }
            String n = remapper.mapType(cr.getClassName());
            File g = new File(d, n + ".class");
            if (!g.exists() || g.lastModified() < f.lastModified()) {
                g.getParentFile().mkdirs();
                OutputStream os = new FileOutputStream(g);
                os.write(cw.toByteArray());
                os.close();
            }
        }
    }

    static class ConstantComparator implements Comparator<Constant> {

        public int compare(final Constant c1, final Constant c2) {
            int d = getSort(c1) - getSort(c2);
            if (d == 0) {
                switch (c1.type) {
                    case 'I':
                        return new Integer(c1.intVal).compareTo(new Integer(c2.intVal));
                    case 'J':
                        return new Long(c1.longVal).compareTo(new Long(c2.longVal));
                    case 'F':
                        return new Float(c1.floatVal).compareTo(new Float(c2.floatVal));
                    case 'D':
                        return new Double(c1.doubleVal).compareTo(new Double(c2.doubleVal));
                    case 's':
                    case 'S':
                    case 'C':
                    case 't':
                        return c1.strVal1.compareTo(c2.strVal1);
                    case 'T':
                        d = c1.strVal1.compareTo(c2.strVal1);
                        if (d == 0) {
                            d = c1.strVal2.compareTo(c2.strVal2);
                        }
                        break;
                    case 'y':
                        d = c1.strVal1.compareTo(c2.strVal1);
                        if (d == 0) {
                            d = c1.strVal2.compareTo(c2.strVal2);
                            if (d == 0) {
                                MethodHandle bsm1 = (MethodHandle)c1.objVal3;
                                MethodHandle bsm2 = (MethodHandle)c2.objVal3;
                                d = compareMHandle(bsm1, bsm2);
                                if (d == 0) {
                                    d = compareObjects(c1.objVals, c2.objVals);
                                }
                            }
                        }
                        break;

                    default:
                        d = c1.strVal1.compareTo(c2.strVal1);
                        if (d == 0) {
                            d = c1.strVal2.compareTo(c2.strVal2);
                            if (d == 0) {
                                d = ((String)c1.objVal3).compareTo((String)c2.objVal3);
                            }
                        }
                }
            }
            return d;
        }

        private static int compareMHandle(MethodHandle mh1, MethodHandle mh2) {
            int d = mh1.getTag() - mh2.getTag();
            if (d == 0) {
                d = mh1.getOwner().compareTo(mh2.getOwner());
                if (d == 0) {
                    d = mh1.getName().compareTo(mh2.getName());
                    if (d == 0) {
                        d = mh1.getDesc().compareTo(mh2.getDesc());
                    }
                }
            }
            return d;
        }

        private static int compareMethodType(MethodType mtype1, MethodType mtype2) {
            return mtype1.getDescriptor().compareTo(mtype2.getDescriptor());
        }

        private static int compareObjects(Object[] objVals1, Object[] objVals2)
        {
            int length = objVals1.length;
            int d = length - objVals2.length;
            if (d == 0) {
                for(int i=0; i<length; i++) {
                    Object objVal1 = objVals1[i];
                    Object objVal2 = objVals2[i];
                    d = objVal1.getClass().getName().compareTo(objVal2.getClass().getName());
                    if (d == 0) {
                        if (objVal1 instanceof Type) {
                            d = ((Type)objVal1).getDescriptor().compareTo(((Type)objVal2).getDescriptor());
                        } else if (objVal1 instanceof MethodType) {
                            d = compareMethodType((MethodType)objVal1,(MethodType)objVal2);
                        } else if (objVal1 instanceof MethodHandle) {
                            d = compareMHandle((MethodHandle)objVal1,(MethodHandle)objVal2);
                        } else {
                            d = ((Comparable)objVal1).compareTo(objVal2);
                        }
                    }

                    if (d != 0) {
                        return d;
                    }
                }
            }
            return 0;
        }

        private static int getSort(final Constant c) {
            switch (c.type) {
                case 'I':
                    return 0;
                case 'J':
                    return 1;
                case 'F':
                    return 2;
                case 'D':
                    return 3;
                case 's':
                    return 4;
                case 'S':
                    return 5;
                case 'C':
                    return 6;
                case 'T':
                    return 7;
                case 'G':
                    return 8;
                case 'M':
                    return 9;
                case 'N':
                    return 10;
                case 'y':
                    return 11;
                case 't':
                    return 12;
                default:
                    return 100 + c.type - 'h';
            }
        }
    }
}
