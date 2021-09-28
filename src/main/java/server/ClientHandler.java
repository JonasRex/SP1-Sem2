package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable
{
    Socket client;
    PrintWriter printWriter;
    Scanner scanner;
    boolean isRunning;
    User user;
    ArrayList<User> userList;

    public ClientHandler(Socket client, ArrayList<User> userList) throws IOException {
        this.client = client;
        this.printWriter = new PrintWriter(client.getOutputStream(),true);
        this.scanner = new Scanner(client.getInputStream());
        this.user = null;
        this.isRunning = true;
        this.userList = userList;
    }

    public void protocol() throws IOException {
        String message = "";
        printWriter.println("Welcome to the chatroom");
        while(!message.equalsIgnoreCase("close") && isRunning)
        {
            message = scanner.nextLine();
            String action = "";
            if(message.contains("#"))
            {
                String[] strings = message.split("#");
                action = strings[0];
                if(strings[1] != null)
                {
                    message = strings[1];
                }
            }
            switch (action.toLowerCase())
            {
                case "connect":
                    connectUser(message);
                    break;

                case "close":
                    printWriter.println("Goodbye");
                    isRunning = false;
                    break;
            }
        }
        client.close();
        isRunning = false;
    }

    @Override
    public void run()
    {
        while(isRunning)
        {
            try {
                this.protocol();
            } catch (IOException e) {
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
