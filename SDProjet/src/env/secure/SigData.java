package env.secure;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Random;

public class SigData {
	
	PrivateKey priv;
	String data;
	
	public SigData(PrivateKey priv, String data) {
		this.priv = priv;
		this.data = data;
	}
	
	public int SignData() throws InvalidKeyException {
		/* Sign the data from the file */
    	Signature dsa = null;
		try {
			dsa = Signature.getInstance("SHA1withDSA", "SUN");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	dsa.initSign(priv);
    	
    	/* Recupere le fichier donne en argument du programme */
    	InputStream fis = null;
		fis = new ByteArrayInputStream(data.getBytes());
    	BufferedInputStream bufin = new BufferedInputStream(fis);
    	byte[] buffer = new byte[1024];
    	int len;
    	try {
			while ((len = bufin.read(buffer)) >= 0) {
			    dsa.update(buffer, 0, len);
			}
		} catch (SignatureException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
    	try {
			bufin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	byte[] realSig = null;
		try {
			realSig = dsa.sign();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	/* save the signature in a string */
    	FileOutputStream sigfos = null;
    	Random rand = new Random();
    	int id = rand.nextInt();
    	File f = new File("signatures");
		try {
			if (f.exists()) {
				sigfos = new FileOutputStream("signatures/" + id + ".sig");
			} else {
				f.mkdir();
				sigfos = new FileOutputStream("signatures/" + id + ".sig");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			sigfos.write(realSig);
			sigfos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return id;
	}

}
