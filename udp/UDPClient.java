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
import java.util.Timer;

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

		//Construct UDP client class and try to send messages
		UDPClient client = new UDPClient();
		long startTime = System.nanoTime();
		client.testLoop(serverAddr, recvPort, countTo);
		long endTime = System.nanoTime();

		System.err.println("Time elapsed (ms): " + ((endTime - startTime)/1000000));
	}

	public UDPClient() {
		//Initialise the UDP socket for sending data
		try {
			sendSoc = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("SocketException: " + e);
			System.exit(-1); 
		}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		//Send the messages to the server
		for(short tries = 0; tries < countTo; ++tries) {
			MessageInfo msg = new MessageInfo(countTo, tries+1);
			send(msg.toString().getBytes(), serverAddr, recvPort);
		}
	}

	private void send(byte[] payload, InetAddress destAddr, int destPort) {
		int					payloadSize;
		DatagramPacket		pkt;

		// Build the datagram packet and send it to the server
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
