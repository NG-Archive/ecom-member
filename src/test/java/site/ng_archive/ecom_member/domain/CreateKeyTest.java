package site.ng_archive.ecom_member.domain;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class CreateKeyTest {

    @org.junit.jupiter.api.Test
    void 암호화키생성() {

        String plainText = "key";
        String key = "test";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());
        encryptor.setPassword(key);
        String encrypted = encryptor.encrypt(plainText);
        System.out.println(encrypted);
    }
}
