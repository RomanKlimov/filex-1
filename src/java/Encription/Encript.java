package Encription;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class Encript {

    public String encript(String pass) throws NoSuchAlgorithmException {
        StringBuffer code = new StringBuffer();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte bytes[] = pass.getBytes();
        byte digest[] = messageDigest.digest(bytes);
        for (int i = 0; i < digest.length; ++i) {
            code.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1));
        }
        String md5 = code.toString();
        return md5;
    }
}
