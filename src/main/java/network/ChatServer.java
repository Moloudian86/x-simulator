package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatServer {
    private static final int port = 5000;
    public static Map<Integer, ClientHandler> onlineUsers = new ConcurrentHashMap<>();
    ServerSocket serverSocket;
    ExecutorService pool;
    private volatile boolean running = true;
    public void start(){
        try {
            serverSocket = new ServerSocket(port);
            pool = Executors.newFixedThreadPool(10);
            while (running){
                Socket socket = serverSocket.accept();
                ClientHandler handler = new  ClientHandler(socket);
                pool.execute(handler);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            pool.shutdown();
            if (!pool.awaitTermination(3, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
