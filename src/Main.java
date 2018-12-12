
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String args[]) throws Exception {

        ServerSocket Server = new ServerSocket(5000, 10, InetAddress.getByName("127.0.0.1"));
        System.out.println("TCPServer Waiting for client on port 5000");

        while (true) {
            try {
                Socket connected = Server.accept();
                (new MyServer(connected)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
