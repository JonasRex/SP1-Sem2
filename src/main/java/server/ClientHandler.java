package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable
{
    Socket client;
    PrintWriter printWriter;
    Scanner scanner;
    boolean isRunning;

    public ClientHandler(Socket client)
    {
        this.client = client;
    }

    @Override
    public void run() {

    }
}
