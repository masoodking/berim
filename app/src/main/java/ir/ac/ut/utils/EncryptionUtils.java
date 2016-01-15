package ir.ac.ut.utils;

import ir.ac.ut.widget.TripleDES;

/**
 * Created by saeed on 1/16/2016.
 */
public class EncryptionUtils {
    static TripleDES tripleDES;
    static String key = "SYqLRdNF93j3KCuFPdbbvmVe";
    public EncryptionUtils(){
        tripleDES = new TripleDES();
    }

    public static String encrypt(String messege){
        byte[] coded = tripleDES.encrypt(messege.getBytes(),key.getBytes());
        return new String(coded);
    }

    public static String decrypt(String messege){
        byte[] coded = messege.getBytes();
        byte[] decrypted = tripleDES.decrypt(coded,key.getBytes());
        return new String(decrypted);
    }
}
