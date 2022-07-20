import java.net.*;
import java.awt.BorderLayout;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Server {
	
	JFrame serverGui;
	JTextArea displayWindow;
	private ServerSocket serverSocket;
	private Socket socket;
	public Hashtable<Socket, ObjectOutputStream> outputStreams;
	public Hashtable<String, ObjectOutputStream> clients;
	
	//constructor
	public Server(int port) throws IOException{
		//Simple Gui for Server
		serverGui = new JFrame("Server");
		serverGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverGui.setSize(500, 500);
		displayWindow = new JTextArea();
		serverGui.add(new JScrollPane(displayWindow), BorderLayout.CENTER);
		serverGui.setVisible(true);

		
		outputStreams = new Hashtable<Socket, ObjectOutputStream>();
		clients = new Hashtable<String, ObjectOutputStream>();
		
		serverSocket = new ServerSocket(port);
		showMessage("Waiting for clients at " + serverSocket);
	}

	//Waiting for clients to connect
	public void waitingForClients() throws IOException, ClassNotFoundException{
		
		while (true){
			socket = serverSocket.accept();
			
			new ServerThread(this, socket);
		}
	}
	
	//displaying message on Server Gui
	public void showMessage(final String message) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(
				new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						displayWindow.append(message);
					}
					
				}
				);
	}
	
	//Sending a message to all the available clients
	public void sendToAll(Object data) throws IOException{
		
		for (Enumeration<ObjectOutputStream> e = getOutputStreams(); e.hasMoreElements(); ){
			//since we don't want server to remove one client and at the same time sending message to it
			synchronized (outputStreams){
				ObjectOutputStream tempOutput = e.nextElement();
				tempOutput.writeObject(data);
				tempOutput.flush();
			}
		}
	}

	//To get Output Stream of the available clients from the hash table
	private Enumeration<ObjectOutputStream> getOutputStreams() {
		// TODO Auto-generated method stub
		return outputStreams.elements();
	}
	
	//Sending private message
	public void sendPrivately(String username, String message) throws IOException {
		// TODO Auto-generated method stub
		ObjectOutputStream tempOutput = clients.get(username);
		tempOutput.writeObject(message);
		tempOutput.flush();
	}
	
	//Removing the client from the client hash table
		public void removeClient(String username) throws IOException{
			
			synchronized (clients){
				clients.remove(username);
				sendToAll("!" + clients.keySet());
			}
		}
	
	//Removing a connection from the outputStreams hash table and closing the socket
	public void removeConnection(Socket socket, String username) throws IOException{
		
		synchronized (outputStreams){
			outputStreams.remove(socket);
		}
		
		//Printing out the client along with the IP offline in the format of ReetAwwsum(123, 12, 21, 21) is offline
		showMessage("\n" + username + "(" + socket.getInetAddress().getHostAddress() + ") is offline");

	}
}












