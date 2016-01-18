package ir.ac.ut.widget;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Des {
	public static byte[] encrypt(byte[] message,byte[] keyArray){
		byte[] ans = new byte[8];
		try {
			SecretKeyFactory mySecretKeyFactory = SecretKeyFactory
					.getInstance("DES");

			// SecretKey myDesKey = keygenerator.generateKey();
			SecretKey myDesKey = mySecretKeyFactory.generateSecret(new DESKeySpec(keyArray));

			Cipher desCipher;

			// Create the cipher
			desCipher = Cipher.getInstance("DES/ECB/NoPadding");

			// Initialize the cipher for encryption
			desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

			// Encrypt the text
			ans = desCipher.doFinal(message);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return ans;
	}
}