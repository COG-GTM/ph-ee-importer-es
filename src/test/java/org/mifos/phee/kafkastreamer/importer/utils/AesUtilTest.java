package org.mifos.phee.kafkastreamer.importer.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.junit.Test;

public class AesUtilTest {

    private static String encryptWithFreshKey(String plaintext, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, AesUtil.getSecretKey(key));
        return AesUtil.base64Encode(cipher.doFinal(plaintext.getBytes()));
    }

    @Test
    public void encryptMatchesUncachedDerivation() throws Exception {
        String key = AesUtil.generateSecretKey();
        for (String plaintext : new String[] { "2", "27", "277102039", "27710305999" }) {
            assertEquals(encryptWithFreshKey(plaintext, key), AesUtil.encrypt(plaintext, key));
            assertEquals(AesUtil.encrypt(plaintext, key), AesUtil.encrypt(plaintext, key));
        }
    }

    @Test
    public void decryptRoundTrips() throws Exception {
        String key = AesUtil.generateSecretKey();
        assertEquals("277102039", AesUtil.decrypt(AesUtil.encrypt("277102039", key), key));
    }

    @Test
    public void derivedKeyIsCachedPerKey() throws Exception {
        String key = AesUtil.generateSecretKey();
        String otherKey = AesUtil.generateSecretKey();
        SecretKey first = AesUtil.getCachedSecretKey(key);
        assertSame(first, AesUtil.getCachedSecretKey(key));
        assertEquals(AesUtil.getSecretKey(key), first);
        assertNotEquals(first, AesUtil.getCachedSecretKey(otherKey));
        assertNotEquals(AesUtil.encrypt("277102039", key), AesUtil.encrypt("277102039", otherKey));
    }
}
