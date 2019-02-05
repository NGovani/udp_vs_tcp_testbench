/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;

	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		int			recvPort;
		int 		countTo;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);


		// TO-DO: Construct UDP client class and try to send messages
		UDPClient client = new UDPClient();
		client.testLoop(serverAddr, recvPort, countTo);
	}

	public UDPClient() {
		// TO-DO: Initialise the UDP socket for sending data
		try {
			sendSoc = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("SocketException: " + e);
			System.exit(-1); 
		}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		// TO-DO: Send the messages to the server
		for(short tries = 0; tries < countTo; ++tries) {
			//byte[] msg = new byte[8];
			/*msg[3] = (byte)(tries & 0x00FF);
			msg[2] = (byte)((tries >> 8) & 0x00FF);
			msg[1] = (byte)(countTo & 0x000000FF);
			msg[0] = (byte)((countTo >> 8) & 0x000000FF);
			*/
			MessageInfo msg = new MessageInfo(countTo, tries);
			send(msg.toString().getBytes(), serverAddr, recvPort);
			System.err.println(msg.toString().getBytes());
			//try{Thread.sleep(0, 5);}catch(InterruptedException ex){}
		}
	}

	private void send(byte[] payload, InetAddress destAddr, int destPort) {
		int					payloadSize;
		DatagramPacket		pkt;

		// TO-DO: build the datagram packet and send it to the server
		try {
			payloadSize = payload.length;
			pkt = new DatagramPacket(payload, payloadSize, destAddr, destPort);
			sendSoc.send(pkt);
		} catch (IOException e) {
			System.out.println("IOException: " + e);
			System.exit(-1);
		}
	}
}
