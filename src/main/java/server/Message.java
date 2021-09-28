package server;

public class Message
{
    String receiver;
    String message;

    public Message(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
        System.out.println("new message created to " + receiver + message);
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
