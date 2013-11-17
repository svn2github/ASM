package org.objectweb.asmdex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.ow2.asmdex.ApplicationReader;
import org.ow2.asmdex.ApplicationWriter;
import org.ow2.asmdex.Opcodes;

import java.io.IOException;

/**
 * The Class ApplicationWriterBasicTest.
 */
public class ApplicationWriterBasicTest {
    /**
     * Tests the visit method, but it does nothing.
     */
    @Test
    public void testVisit() {
        ApplicationWriter aw = new ApplicationWriter();
        aw.visit();
    }

    /**
     * Tests the visitClass method, but it does nothing.
     */
    @Test
    public void testVisitClass() {
        ApplicationWriter aw = new ApplicationWriter();
        aw.visitClass(0, "class", null, null, null);
    }

    /**
     * Tests that a newly created Writer doesn't provide a byte array.
     */
    @Test
    public void testToByteArrayNull() {
        ApplicationWriter aw = new ApplicationWriter();
        assertNull(aw.toByteArray());
    }

    /**
     * Tests that a newly created Writer provides a byte array.
     */
    @Test
    public void testGetConstantPool() {
        ApplicationWriter aw = new ApplicationWriter();
        assertNotNull(aw.getConstantPool());
    }

    /**
     * Tests the visitEnd method, which generated the dex file, it should only contain the header and
     * the Map at the end.
     */
    @Test
    public void testVisitEndAlmostEmpty() {
        ApplicationWriter aw = new ApplicationWriter();
        aw.visitEnd();
        byte[] bytes = aw.toByteArray();
        assertEquals(0x70 + 0x1c, bytes.length);
    }
    
    /**
     * Tests that a newly created Writer doesn't provide an Application Reader unless it is given to it.
     */
    @Test
    public void testGetApplicationReaderNull() {
        ApplicationWriter aw = new ApplicationWriter();
        assertNull(aw.getApplicationReader());
    }
    
    /**
     * Tests that a newly created Writer provides an Application Reader if it is given to it.
     * @throws IOException 
     */
    @Test
    public void testGetApplicationReader() throws IOException {
        ApplicationReader ar = new ApplicationReader(Opcodes.ASM4, TestUtil.PATH_AND_FILENAME_HELLO_WORLD_DEX);
        ApplicationWriter aw = new ApplicationWriter(ar);
        assertNotNull(aw.getApplicationReader());
    }

}
