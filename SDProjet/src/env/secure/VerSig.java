package env.secure;

import java.io.*;
import java.security.*;
import java.security.spec.*;

public class VerSig {
	File publicKey;
	int id;
	String data;
	
	public VerSig(File publicKey, int id, String data) {
		this.publicKey = publicKey;
		this.id = id;
		this.data = data;
	}
	
	public boolean ValidSig() {
		/* Verify a DSA signature */

        try {
        	/* Reading encoded public bytes */
        	FileInputStream keyfis = new FileInputStream(this.publicKey);
        	byte[] encKey = new byte[keyfis.available()];  
        	keyfis.read(encKey);
        	keyfis.close();
        	
        	/* Generating public key */
        	X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        	KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        	
        	PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        	
        	FileInputStream sigfis = new FileInputStream("signatures/" + this.id + ".sig");
        	byte[] sigToVerify = new byte[sigfis.available()]; 
        	sigfis.read(sigToVerify);
        	sigfis.close();
        	
        	/* Verifying the signature */
        	Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        	sig.initVerify(pubKey);
        	
        	InputStream datafis = new ByteArrayInputStream(this.data.getBytes());
        	BufferedInputStream bufin = new BufferedInputStream(datafis);

        	byte[] buffer = new byte[1024];
        	int len;
        	while (bufin.available() != 0) {
        	    len = bufin.read(buffer);
        	    sig.update(buffer, 0, len);
        	};

        	bufin.close();
        	
        	boolean verifies = sig.verify(sigToVerify);
        	//System.out.println("La validation a donnee : " + verifies + " pour le message :  " + this.data);
        	return verifies;

        	//System.out.println("signature verifies: " + verifies);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
		return false;		
	}
	
	public void deleteSig() {
		File file = new File("signatures/" + this.id + ".sig");
		if(file.delete()){
			//System.out.println(file.getName() + " is deleted!");
		} else{
			//System.out.println("Delete operation is failed.");
		}
	}
}