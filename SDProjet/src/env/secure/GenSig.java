package env.secure;

import java.io.*;
import java.security.*;

/**
 * 
 * @author Idryss Bourdier
 * Génère la signature d'une commande
 * 
 */

public class GenSig {
	private KeyPair pair;
	PrivateKey priv;
	PublicKey pub;
	
	public PrivateKey getPrivate() {
		return this.priv;
	}
	
	public void genKeyPair(String name) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
    	SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
    	keyGen.initialize(1024, random);
    	
    	this.pair = keyGen.generateKeyPair();
    	this.priv = this.pair.getPrivate();
    	this.pub = this.pair.getPublic();
    	
    	/* save the private key in a file */
    	byte[] key = this.priv.getEncoded();
    	FileOutputStream keyfos = new FileOutputStream("private/" + name + "privKey.priv");
    	keyfos.write(key);
    	keyfos.close();
    	
    	/* save the public key in a file */
    	byte[] key_pub = this.pub.getEncoded();
    	FileOutputStream keyfos_pub = new FileOutputStream("public/" + name + "pubKey.pub");
    	keyfos_pub.write(key_pub);
    	keyfos_pub.close();
	}

    public static void main(String[] args) {
//
//        /* Generate a DSA signature */
//
//        if (args.length != 1) {
//            System.out.println("Error : Usage: GenSig nameOfFileToSign");
//        } else try {
//        	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
//        	SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
//        	keyGen.initialize(1024, random);
//        	
//        	KeyPair pair = keyGen.generateKeyPair();
//        	PrivateKey priv = pair.getPrivate();
//        	PublicKey pub = pair.getPublic();
//        	
//        	/* Sign the data from the file */
//        	Signature dsa = Signature.getInstance("SHA1withDSA", "SUN"); 
//        	dsa.initSign(priv);
//        	
//        	/* Recupere le fichier donne en argument du programme */
//        	FileInputStream fis = new FileInputStream(args[0]);
//        	BufferedInputStream bufin = new BufferedInputStream(fis);
//        	byte[] buffer = new byte[1024];
//        	int len;
//        	while ((len = bufin.read(buffer)) >= 0) {
//        	    dsa.update(buffer, 0, len);
//        	};
//        	bufin.close();
//        	
//        	byte[] realSig = dsa.sign();
//        	
//        	/* save the signature in a file */
//        	FileOutputStream sigfos = new FileOutputStream("signature");
//        	sigfos.write(realSig);
//        	sigfos.close();
//        	
//        	/* save the public key in a file */
//        	byte[] key = pub.getEncoded();
//        	FileOutputStream keyfos = new FileOutputStream("pubKey.pub");
//        	keyfos.write(key);
//        	keyfos.close();
//        } catch (Exception e) {
//            System.err.println("Caught exception " + e.toString());
//        }
//    }
}}