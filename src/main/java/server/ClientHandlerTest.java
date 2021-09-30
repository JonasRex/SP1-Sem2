package server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {

    CopyOnWriteArrayList<User> userList = new CopyOnWriteArrayList<>();
    BlockingQueue<Message> messages = new ArrayBlockingQueue<>(10);
    ClientHandler clientHandler = new ClientHandler(new Socket(), userList, messages);

    ClientHandlerTest() throws IOException {
    }
//
//    @Test
//    void testProtocol() {
//        userList.add(new User("eske", 0));
//        userList.get(0).isOnline = true;
//
//        assertEquals("eske",clientHandler.whoIsOnline() );
//    }

}