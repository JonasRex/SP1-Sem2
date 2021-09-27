package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer
{
    private ServerSocket serverSocket;
    CopyOnWriteArrayList<ClientHandler> clientList;

    public void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port: " + port);
        clientList = new CopyOnWriteArrayList();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        while(true)
        {
            System.out.println("Waiting for a client..");
            Socket client = serverSocket.accept();
            System.out.println("New client connectet"); //TODO tilføj brugernavn
            ClientHandler clientHandler = new ClientHandler(client);
            clientList.add(clientHandler);
            executorService.execute(clientHandler);
        }
    }
}
