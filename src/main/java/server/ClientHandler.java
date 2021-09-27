package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable
{
    Socket client;
    PrintWriter printWriter;
    Scanner scanner;
    boolean isRunning;
    User user;

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        this.printWriter = new PrintWriter(client.getOutputStream(),true);
        this.scanner = new Scanner(client.getInputStream());
        this.user = null;
        this.isRunning = true;
    }

    public void protocol()
    {
        String message = "";
        printWriter.println("Welcome to the chatroom");
        while(!message.equals("CLOSE"))
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
    }

    @Override
    public void run()
    {
        while(isRunning)
        {
            this.protocol();
        }
    }

    public void connectUser(String userName)
    {
        user = new User(userName);
        printWriter.println(userName + " is now connected to the chatroom");
    }

    public User getUser() {
        return user;
    }
}
