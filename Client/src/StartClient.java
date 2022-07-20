import java.io.*;
import java.net.UnknownHostException;
public class StartClient {
	public static void main(String[] args) throws UnknownHostException, IOException{
		Client.BuildMainWindow();
		Client.Initialize();
		Client.BuildLogInWindow();
}
}
