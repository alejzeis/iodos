import xyz.ajann.iodos.CryptoUtilKt;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class TestKeyPairGen {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("ECIES", "BC");

        KeyPair pair = CryptoUtilKt.generateECKeyPair();
        System.out.println(pair.getPrivate().getFormat());
        System.out.println(pair.getPublic().getFormat());

        cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
        byte[] encrypted = cipher.doFinal("Test String!".getBytes());
        System.out.println(new String(encrypted));


        System.out.println(new String(Base64.getEncoder().encode(pair.getPrivate().getEncoded())));
        System.out.println(new String(Base64.getEncoder().encode(pair.getPublic().getEncoded())));

        ECPrivateKey privateKey = CryptoUtilKt.loadPrivateKeyFromDisk("private.key");
        PrivateKey pk = KeyFactory.getInstance("EC","BC").generatePrivate(new PKCS8EncodedKeySpec(pair.getPrivate().getEncoded()));
        cipher = Cipher.getInstance("ECIES", "BC");
        cipher.init(Cipher.DECRYPT_MODE, pk);
        System.out.println(new String(cipher.doFinal(encrypted)));

    }
}
