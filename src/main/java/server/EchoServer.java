package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EchoServer {
    private ServerSocket serverSocket;
    CopyOnWriteArrayList<ClientHandler> clientList;
    CopyOnWriteArrayList<User> userList;
    Dispatcher dispatcher;
    BlockingQueue<Message> messages;

    public void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port: " + port);
        clientList = new CopyOnWriteArrayList<>();
        messages = new ArrayBlockingQueue<>(10);
        userList = new CopyOnWriteArrayList<>();
        addUsersToList();
        ExecutorService executorService = Executors.newFixedThreadPool(100); //TODO: virkede ikke med kun 10 tråde, når man har plus 4 bruger på.
        dispatcher = new Dispatcher(messages, clientList);

        while (true) {
            System.out.println("Waiting for a client..");
            Socket client = serverSocket.accept();
            System.out.println("New client connectet"); //TODO tilføj brugernavn
            ClientHandler clientHandler = new ClientHandler(client, userList, messages, clientList);
            clientList.add(clientHandler);
            executorService.execute(clientHandler);
            executorService.execute(dispatcher);
        }
    }

    private void addUsersToList() {
        userList.add(new User("Eske", 0));
        userList.add(new User("Jonas", 1));
        userList.add(new User("Lars", 2));
        userList.add(new User("Bjarne", 3));
        userList.add(new User("Thor", 4));
    }
}
