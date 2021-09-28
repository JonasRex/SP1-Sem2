package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler implements Runnable
{
    CopyOnWriteArrayList<ClientHandler> clientList;
    Socket client;
    PrintWriter printWriter;
    Scanner scanner;
    boolean isRunning;
    User user;
    CopyOnWriteArrayList<User> userList;
    BlockingQueue<Message> messages;

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public ClientHandler(Socket client, CopyOnWriteArrayList<User> userList, BlockingQueue<Message> messages, CopyOnWriteArrayList<ClientHandler> clientList) throws IOException {
        this.client = client;
        this.printWriter = new PrintWriter(client.getOutputStream(),true);
        this.scanner = new Scanner(client.getInputStream());
        this.user = new User("",0);
        this.isRunning = true;
        this.userList = userList;
        this.messages = messages;
        this.clientList = clientList;
    }

    public void protocol() throws IOException, InterruptedException {
        String message1 = "";
        printWriter.println("Welcome to the chatroom");
        while(!message1.equalsIgnoreCase("close") && isRunning)
        {
            message1 = scanner.nextLine();
            String action = "";
            String message2 = "";
            if(message1.contains("#"))
            {
                String[] splitter = message1.split("#");
                String[] strings = new String[3];
                strings[0] = splitter[0];

                if(splitter.length > 1)
                {
                    strings[1] = splitter[1];
                }

                if(splitter.length > 2)
                {
                    strings[2] = splitter[2];
                }

                action = strings[0];

                if(strings[1] != null)
                {
                    message1 = strings[1];
                }

                if(strings[2] != null)
                {
                    message2 = strings[2];
                }
            }

            switch (action.toLowerCase())
            {
                case "connect":
                    connectUser(message1);
                    break;

                case "send":
                    sendMessage(message1, message2);
                    break;

                case "close":
                    printWriter.println("Goodbye");
                    isRunning = false;
                    user.setOnline(false);
                    break;
            }
        }
        client.close();
        isRunning = false;
        if(user != null)
            user.setOnline(false);
    }

    private void sendMessage(String message1, String message2) throws InterruptedException {
        if(message1.equals("*"))
        {
            messages.put(new Message("all",user.getUserName() + ": " + message2));
        }
        else
        {
            for (User u: userList)
            {
                if(message1.equals(u.getUserName().toLowerCase()))
                {
                    messages.put(new Message(message1,user.getUserName() + ": " + message2));
                    break;
                }
            }
        }
    }

    @Override
    public void run()
    {
        while(isRunning)
        {
            try {
                this.protocol();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void connectUser(String userName) throws IOException {

        boolean found = false;
        for (User u : userList)
        {
            String tmp = u.getUserName().toLowerCase();
            if(tmp.equals(userName.toLowerCase()) && !u.isOnline())
            {
                printWriter.println(userName + " is now connected to the chatroom");
                found = true;
                this.user = u;
                u.setOnline(true);
                break;
            }
        }
        if(!found)
        {
            printWriter.println("User " + userName + " not found or is already online");
            client.close();
            isRunning = false;

        }
    }

    public User getUser() {
        return user;
    }
}
