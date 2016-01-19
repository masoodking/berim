package ir.ac.ut.utils;

import android.util.Log;

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
        String t = messege;
        while(t.length()%8!=0)
            t=t+" ";
        byte[] coded = tripleDES.encrypt(stringToByteArray(t),stringToByteArray(key));
        return byteArrayToString(coded);
    }

    public static String decrypt(String messege){
        byte[] coded = stringToByteArray(messege);
        byte[] decrypted = tripleDES.decrypt(coded, stringToByteArray(key));
        return byteArrayToString(decrypted);
    }

    public static String byteArrayToString (byte[] input){
        String res = "";
        for(int i=0;i<input.length;i++){
            res = res+(char) input[i];
        }
        Log.d("byte to string", res);
        return res;
    }

    public static byte[] stringToByteArray(String s){
        byte [] res = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            res[i] = (byte) s.charAt(i);
        }
        return res;
    }
}
