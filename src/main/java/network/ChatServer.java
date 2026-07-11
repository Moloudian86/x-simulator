package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int port = 5000;
    public static Map<Integer, ClientHandler> onlineUsers = new HashMap<>();
    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            ExecutorService pool = Executors.newFixedThreadPool(10);
            while (true){
                Socket socket = serverSocket.accept();
                ClientHandler handler = new  ClientHandler(socket);
                pool.execute(handler);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
