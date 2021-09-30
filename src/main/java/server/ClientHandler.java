package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler implements Runnable {
    // All shared lists
    CopyOnWriteArrayList<User> userList;
    BlockingQueue<Message> messages;

    // Shared tools
    Socket client;
    PrintWriter printWriter;
    Scanner scanner;

    // All booleans
    boolean isRunning;
    boolean isConnected;
    boolean closed;

    User user;


    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public ClientHandler(Socket client, CopyOnWriteArrayList<User> userList, BlockingQueue<Message> messages) throws IOException {
        this.client = client;
        this.printWriter = new PrintWriter(client.getOutputStream(), true);
        this.scanner = new Scanner(client.getInputStream());
        this.user = new User("", 0);

        this.isRunning = true; // This controls the Run methods while loop;
        this.isConnected = false; // This checks if user is connected, and makes sure you can send messages before connected.
        this.closed = false;  // This controls the protocol while loop

        this.userList = userList;
        this.messages = messages;

    }

    public void protocol() throws IOException, InterruptedException {

        printWriter.println(Thread.currentThread().getName() + ": " +"Welcome to the chatroom");

        while (!closed) {
            String[] strings = textSplitter(scanner.nextLine());

            // strings[0]#strings[1]#strings[2]
            // strings[0] = action, strings[1] = message1, strings[2] = message2

            switch (strings[0].toLowerCase()) {
                case "connect":
                    connectUser(strings[1]);
                    break;

                case "send":
                    sendMessage(strings[1], strings[2]);
                    break;

                case "close":
                    closed = true;
                    System.out.println("CLOSE#0: Normal close");
                    break;
                default:
                    printWriter.println("Unknown action. Try again.");
            }

        }
        closingConnection();

    }

    private String[] textSplitter(String message){
        String[] strings = new String[3];

        String[] splitter = message.split("#");

        strings[0] = splitter[0];

        if (splitter.length > 1) {
            strings[1] = splitter[1].toLowerCase();
        }

        if (splitter.length > 2) {
            strings[2] = splitter[2];
        }


        return strings;
    }

    private void connectUser(String userName) throws IOException, InterruptedException {

        boolean found = false;
        for (User u : userList) {
            String tmp = u.getUserName().toLowerCase();
            if (tmp.equals(userName.toLowerCase()) && !u.isOnline()) {
                printWriter.println(u.getUserName() + " is now connected to the chatroom");
                isConnected = true;
                found = true;
                this.user = u;
                u.setOnline(true);
                messages.put(whoIsOnline("all"));
                break;
            }
        }
        if (!found) {
            printWriter.println("User " + userName + " not found or is already online");
            System.out.println("CLOSE#2: User not found");
            client.close();
            isRunning = false;
            closed = true;


        }
    }

    private void sendMessage(String receiver, String msg) throws InterruptedException {
        if (isConnected) {
            if (receiver.equals("*")) {
                messages.put(new Message("all", user.getUserName() + ": " + msg));
            } else {
                for (User u : userList) {
                    if (receiver.equals(u.getUserName().toLowerCase())) {


                        messages.put(new Message(receiver, user.getUserName() + ": " + msg));
                        break;
                    }
                }
            }
        } else {
            printWriter.println("You are not connected yet.");
        }
    }

    private void closingConnection() throws InterruptedException, IOException {
        printWriter.println("Goodbye");
        isRunning = false;
        if (user != null)
            user.setOnline(false);
        messages.put(whoIsOnline("all"));

        printWriter.close();
        scanner.close();
        client.close();
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                this.protocol();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Message whoIsOnline(String username) throws InterruptedException {
        String onlineList = "User currently online: ";

        for (User user : userList) {
            if(user.isOnline()){

                    onlineList += user.getUserName() + ",";

            }

        }
        return new Message(username, onlineList);

    }

    public User getUser() {
        return user;
    }
}
