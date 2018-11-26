package com.bankserver.util;

import org.mindrot.jbcrypt.BCrypt;

public class Encryption {

    // Encrypt
    public static String hash(String text){
        return BCrypt.hashpw(text, BCrypt.gensalt());
    }

    // Check hashed texts
    public static boolean check(String text, String hashedText) {
        return BCrypt.checkpw(text, hashedText);
    }
}
