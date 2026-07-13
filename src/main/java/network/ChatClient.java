package network;

import UiController.Chat2Controller;
import UiController.ChatController;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatClient implements INetworkConnection {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean connected = false;

    private ClientListener listener;

    public void connect(Chat2Controller controller) {
        connect("localhost", 5000);
        listener = new ClientListener(input, controller);
        listener.start();
    }

    @Override
    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(NetworkPacket packet) {
        try {
            synchronized (output){
                output.writeObject(packet);
                output.flush();
            }

        } catch (Exception e) {
        }
    }

    @Override
    public void disconnect() {
        try {
            connected = false;
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }
}