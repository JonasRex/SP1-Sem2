package server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dispatcher implements Runnable
{
    BlockingQueue<Message> messages;
    CopyOnWriteArrayList<ClientHandler> clientList;

    public Dispatcher(BlockingQueue<Message> queue, CopyOnWriteArrayList<ClientHandler> clientList) {
        this.messages = queue;
        this.clientList = clientList;
    }

    @Override
    public void run()
    {
        String outmsg = "";
        String receiver = "";

        while(true)
        {
            try {
                Message newMessage = messages.take();
                outmsg = newMessage.getMessage();
                receiver = newMessage.getReceiver();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(receiver.equals("all"))
            {
                for (ClientHandler ch : clientList)
                {
                    ch.getPrintWriter().println(outmsg);
                }
            }
            else
            {
                for (ClientHandler ch : clientList)
                {
                    String userName = ch.getUser().getUserName().toLowerCase();
                    //System.out.println(userName + " " + receiver);
                    if(receiver.equals(userName))
                    {
                        ch.getPrintWriter().println(outmsg);
                        break;
                    }
                }
            }
        }
    }
}

