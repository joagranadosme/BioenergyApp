package com.procesosyoperaciones.bioenergy;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Jonathan on 9/9/2017.
 */

public class Cipher {

    public static String hash(String password) throws Exception {

        MessageDigest md = null;
        byte[] hash = null;
        md = MessageDigest.getInstance("SHA-512");
        hash = md.digest(password.getBytes("UTF-8"));

        return convertToHex(hash);

    }

    private static String convertToHex(byte[] raw) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();

    }

}
