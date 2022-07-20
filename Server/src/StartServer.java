import java.io.IOException;


public class StartServer {
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		Server server = new Server(7777);
		server.waitingForClients();
	}
	
	
}
