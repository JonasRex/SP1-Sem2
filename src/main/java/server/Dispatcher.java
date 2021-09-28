package server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dispatcher implements Runnable
{

    BlockingQueue<String> messages;
    CopyOnWriteArrayList<ClientHandler> clientList;

    public Dispatcher(BlockingQueue<String> queue, CopyOnWriteArrayList<ClientHandler> clientList) {
        this.messages = queue;
        this.clientList = clientList;
    }

    @Override
    public void run()
    {
        String outmsg = "";

        while(true)
        {
            try {
                outmsg = messages.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (ClientHandler ch : clientList)
            {
                ch.getPrintWriter().println(outmsg);
            }
        }
    }
}

