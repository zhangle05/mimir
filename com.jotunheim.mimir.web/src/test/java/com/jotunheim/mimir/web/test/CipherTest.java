/**
 * 
 */
package com.jotunheim.mimir.web.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.jotunheim.mimir.common.utils.CipherHelper;

/**
 * @author zhangle
 *
 */
public class CipherTest extends TestCase{

    @Test
    public void test() {
        String input = "supervisor-963d692f24c99cd9ba9a7c019a12ecf6";
        String encrypted = CipherHelper.encrypt(input);
        String decrypted = CipherHelper.decrypt(encrypted);
        assertEquals(input, decrypted);
    }
}
