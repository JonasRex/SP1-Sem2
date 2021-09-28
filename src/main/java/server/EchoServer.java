package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer
{
    private ServerSocket serverSocket;
    CopyOnWriteArrayList<ClientHandler> clientList;
    ArrayList<User> userList;

    public void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port: " + port);
        clientList = new CopyOnWriteArrayList();
        userList = new ArrayList<>();
        addUsersToList();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        while(true)
        {
            System.out.println("Waiting for a client..");
            Socket client = serverSocket.accept();
            System.out.println("New client connectet"); //TODO tilføj brugernavn
            ClientHandler clientHandler = new ClientHandler(client, userList);
            clientList.add(clientHandler);
            executorService.execute(clientHandler);
        }
    }

    private void addUsersToList()
    {
        userList.add(new User("Eske", 0));
        userList.add(new User("Jonas", 1));
        userList.add(new User("Lars", 2));
        userList.add(new User("Bjarne", 3));
        userList.add(new User("Thor", 4));
    }
}
