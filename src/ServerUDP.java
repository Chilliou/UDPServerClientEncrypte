import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.ArrayList;

public class ServerUDP
{
	final static int port = 9000;
	final static int taille = 1024;

	public static void main (String[] args) throws Exception
	{
		AsymmetricCryptography ac = new AsymmetricCryptography();
		PublicKey publicKey = ac.getPublic("../KeyPair/publicKey");

		DatagramSocket ds = new DatagramSocket (port);


		System.out.println ("Lancement du serveur");
		while (true)
		{
			byte[] buf = new byte[taille];
			DatagramPacket dp = new DatagramPacket (buf, 1024);
			DatagramPacket envoi = new DatagramPacket (buf, 1024);


			String bannière = "Bienvue sur le serveur de Chilliou";
			byte[] arBuffer = bannière.getBytes ();
			DatagramPacket bannièreDP = new DatagramPacket (arBuffer, arBuffer.length, dp.getAddress (), dp.getPort ());
			ds.send (bannièreDP);


			ds.receive (dp);

			System.out.println ("informations client :   ip = " + dp.getAddress ().getHostAddress ());
			System.out.println ("informations client : port = " + dp.getPort ());

			String strRecuEncrypt = new String (dp.getData (), 0, dp.getLength ());
			String strRecuDecrypt = ac.decryptText (strRecuEncrypt,publicKey);


			String m1 = "";


			arBuffer = m1.getBytes ();

			envoi = new DatagramPacket (arBuffer, arBuffer.length, dp.getAddress (), dp.getPort ());
			ds.send (envoi);


			System.out.println ("Information recu: " + strRecuDecrypt);
			System.out.println ("Information envoyée : " + strRecuDecrypt);

		}
	}
}
