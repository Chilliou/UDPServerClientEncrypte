import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Scanner;

public class ClientUDP
{
	private final static int PORT = 9000;
	private final static int taille = 1024;


	public static void main(String[] args) throws Exception
	{
		AsymmetricCryptography ac = new AsymmetricCryptography();
		PrivateKey privateKey = ac.getPrivate("../KeyPair/privateKey");

		Scanner sc = new Scanner(System.in);
		try
		{
			do
			{
				String sClient = sc.nextLine ();
				String sClientEncrypt= ac.encryptText (sClient,privateKey);

				byte[] arBuffer = sClientEncrypt.getBytes();
				DatagramSocket ds = new DatagramSocket();
				DatagramPacket dp = new DatagramPacket(arBuffer, arBuffer.length, InetAddress.getLocalHost(), ClientUDP.PORT);

				ds.send(dp);
				ds.receive(dp);

				String strRecu=new String(dp.getData(), 0, dp.getLength());

				System.out.println("Message " + strRecu);
				System.out.println("informations client :   ip = " + dp.getAddress().getHostAddress());
				System.out.println("informations client : port = " + dp.getPort());
			}while(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}