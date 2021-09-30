package server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class ChatServer {
    //Call server with arguments like this: 8088
    public static void main(String[] args) throws IOException {
        int port = 8088;
        EchoServer echoServer = new EchoServer();
        try {
            echoServer.startServer(port);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
