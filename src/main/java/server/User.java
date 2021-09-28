package server;

public class User
{
    String userName;
    int id;
    boolean isOnline;

    public User(String userName, int id)
    {
        this.userName = userName;
        this.id = id;
        this.isOnline = false;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
