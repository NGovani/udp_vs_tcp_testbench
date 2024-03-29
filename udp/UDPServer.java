/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private boolean[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize = 11;
		byte[]			pacData = new byte[pacSize];
		DatagramPacket 	pac = new DatagramPacket(pacData, pacSize);

		int i = 0;

		close = false;

		while(!close) {
			try {
				recvSoc.receive(pac);
				i = processMessage(pac);
			} catch(SocketTimeoutException e) {
				System.out.println("SocketTimeOut: " + e);
				if(totalMessages != -1)
					 break;
			} catch(IOException e) {
				System.out.println("IOException: " + e);
				System.exit(-1);
			}
			if (totalMessages != -1 && (i) >= totalMessages)
				close = true;		
		}

		int numRecieved = 0;

		System.out.println("Messages lost:");
		for(int count = 0; count < receivedMessages.length; ++count) {
			if (receivedMessages[count])
				numRecieved++;
			else
				System.out.println("Message: " + count);
		}
		System.out.println("Recieved " + numRecieved + " out of " + totalMessages + " : " + ((double)numRecieved/(double)totalMessages)*100 + "% found.");

		recvSoc.close();
	}

	public int processMessage(DatagramPacket data) {

		try{
			byte[] temp = data.getData();
			String msgInfo = new String(temp).trim();
			MessageInfo msg = new MessageInfo(msgInfo);
		// receipt of first message, initialise the receive buffer
			if (totalMessages == -1) {
		
				receivedMessages = new boolean[msg.totalMessages];
				totalMessages = msg.totalMessages;
			}

		// Log receipt of the message
			int msgNumber = msg.messageNum;
			receivedMessages[msgNumber-1] = true;

			return msgNumber;
		}
		catch(Exception e){
			System.err.println("Client exception: " + e.toString()); 
			e.printStackTrace(); 
			return 0;
		}
	}


	public UDPServer(int rp) {

		double timeOut = 4 ;

		// Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
			recvSoc.setSoTimeout((int)(timeOut*1000));
		} catch (SocketException e) {
			System.out.println("SocketException: " + e);
			System.exit(-1);
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

		//Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);
		server.run();
	}

}
