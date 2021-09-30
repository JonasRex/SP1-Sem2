package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerHandler implements Runnable{
    private ServerSocket serverSocket;
    Scanner keyboard;
    CopyOnWriteArrayList<User> userList;
    BlockingQueue<Message> messages;

    public ServerHandler(CopyOnWriteArrayList<User> userList, BlockingQueue<Message> messages, ServerSocket serverSocket){
        this.userList = userList;
        this.messages = messages;
        this.serverSocket = serverSocket;
        keyboard = new Scanner(System.in);
    }
    private void protocol() throws InterruptedException, IOException {
        while (true){
            System.out.println("Write admin command: ");

            String[] strings = textSplitter(keyboard.nextLine());

            switch (strings[0].toLowerCase()) {
                case "online":
                    whoIsOnline(strings[1]);
                    break;

                case "message":
                    sendMessage(strings[1], strings[2]);

                    break;

                case "close":
                    System.out.println("Closing Server");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown action. Try again.");
            }

        }
    }

    private void whoIsOnline(String username) throws InterruptedException {
        String onlineList = "ONLINE#";

        for (User user : userList) {
            if(user.isOnline()){
                onlineList += user.getUserName() + ",";
            }

        }
        messages.put(new Message(username, onlineList));
    }

    private void sendMessage(String receiver, String msg) throws InterruptedException {
//        if (isConnected) {
            if (receiver.equals("*")) {
                messages.put(new Message("all", "Admin: " + msg));
            } else {
                for (User u : userList) {
                    if (receiver.equals(u.getUserName().toLowerCase())) {
                        messages.put(new Message(receiver, "Admin: " + msg));
                        break;
                    }
                }
            }
//        } else {
//            printWriter.println("You are not connected yet.");
//        }
    }

    @Override
    public void run() {
       while (true) {
           try {
               protocol();
           } catch (InterruptedException | IOException e) {
               e.printStackTrace();
           }


       }
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
}
