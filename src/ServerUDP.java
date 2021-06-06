import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServerUDP
{
	final static int port = 9000;
	final static int taille = 1024;

	public static void main (String[] args) throws Exception
	{

		DatagramSocket ds = new DatagramSocket (port);


		System.out.println ("Lancement du serveur");
		while (true)
		{
			byte[] buf = new byte[taille];
			DatagramPacket dp = new DatagramPacket (buf, 1024);
			DatagramPacket envoi = new DatagramPacket (buf, 1024);
			ds.receive (dp);

			System.out.println ("informations client :   ip = " + dp.getAddress ().getHostAddress ());
			System.out.println ("informations client : port = " + dp.getPort ());

			String strRecu = new String (dp.getData (), 0, dp.getLength ());


			String m1 = "";


			byte[] arBuffer = m1.getBytes ();

			envoi = new DatagramPacket (arBuffer, arBuffer.length, dp.getAddress (), dp.getPort ());
			ds.send (envoi);


			System.out.println ("Information recu: " + strRecu);
			System.out.println ("Information envoyée : " + strRecu);

		}
	}
}
