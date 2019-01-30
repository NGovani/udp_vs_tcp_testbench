/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private boolean[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize = 4;
		byte[]			pacData = new byte[pacSize];
		DatagramPacket 	pac = new DatagramPacket(pacData, pacSize);

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever

		int i = 0;

		close = false;

		while(!close) {
			try {
				recvSoc.receive(pac);
				i = processMessage(pac);
				System.out.println(i);
			} catch(SocketTimeoutException e) {
				System.out.println("SocketTimeOut: " + e);
				++i;
			} catch(IOException e) {
				System.out.println("IOException: " + e);
				System.exit(-1);
			}
			if (totalMessages != -1 && i >= totalMessages)
				close = true;		
		}

		int numRecieved = 0;

		System.out.println("Messages lost:");
		for(int count = 0; count < receivedMessages.length; ++count) {
			if (receivedMessages[count])
				numRecieved++;
			else
				System.out.println(count);
		}
		System.out.println("Recieved " + numRecieved + " out of " + totalMessages + " : " + ((double)numRecieved/(double)totalMessages)*100 + "% found.");

		recvSoc.close();
	}

	public int processMessage(DatagramPacket data) {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (totalMessages == -1) {
			totalMessages = (uint)((data.getData()[0] << 8) | (data.getData()[1]));
			receivedMessages = new boolean[totalMessages];
		}

		// TO-DO: Log receipt of the message
		int msg = (int)((data.getData()[2] << 8) | (data.getData()[3]));
		receivedMessages[msg] = true;

		return msg;
	}


	public UDPServer(int rp) {

		int timeOut = 5;

		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
			recvSoc.setSoTimeout(timeOut*1000);
		} catch (SocketException e) {
			System.out.println("SocketException: " + e);
			exit(-1);
		}

		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);
		server.run();
	}

}
