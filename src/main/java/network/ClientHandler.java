package network;

import LogicController.MessageService;
import model.ChatMessage;
import model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private User currentUser;
    private volatile boolean running = true;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void stopHandler() {
        running = false;
        try {
            if (currentUser != null) {
                ChatServer.onlineUsers.remove(currentUser.getId());
            }
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            while (running){
                NetworkPacket packet = (NetworkPacket) input.readObject();
                handel(packet);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            stopHandler();
        }

    }

    public void handel(NetworkPacket packet){
        if (packet.getRequestType() == RequestType.REGISTER) {
            currentUser = (User) packet.getData();
            ChatServer.onlineUsers.put(currentUser.getId(), this);
        }
        else if (packet.getRequestType() == RequestType.SEND_MESSAGE){
            ChatMessage msg = (ChatMessage) packet.getData();
            MessageService.getInstance().sendMessage(msg.getSender(),msg.getReceiver(),msg.getContent());
            ClientHandler receiverHandler = ChatServer.onlineUsers.get(msg.getReceiver().getId());
            if (receiverHandler != null) {
                try {
                    NetworkPacket newMsg = new NetworkPacket(RequestType.NEW_MESSAGE, msg);
                    synchronized (receiverHandler.output){
                        receiverHandler.output.writeObject(newMsg);
                        receiverHandler.output.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else if (packet.getRequestType() == RequestType.GET_CONVERSATION){
            User[] users = (User[]) packet.getData();
            User u1 = users[0];
            User u2 = users[1];
            List<ChatMessage> messages = MessageService.getInstance().getConversation(u1,u2);
            NetworkPacket respons = new NetworkPacket(RequestType.GET_CONVERSATION, messages);
            try {
                synchronized (output){
                    output.writeObject(respons);
                    output.flush();
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        else if (packet.getRequestType() == RequestType.MARK_AS_SEEN) {
            User[] users = (User[]) packet.getData();
            MessageService.getInstance().markMessagesAsSeen(users[0], users[1]);
        }
        else if (packet.getRequestType() == RequestType.DISCONNECT) {
            try {
                stopHandler();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        else if (packet.getRequestType() == RequestType.DELETE_MESSAGE) {
            int messageId = (int) packet.getData();
            MessageService.getInstance().deleteMessage(messageId);
        }
        else if(packet.getRequestType() == RequestType.GET_ONLINE_USERS){
            try {
                List<Integer> onlineUsers = new ArrayList<>(ChatServer.onlineUsers.keySet());
                NetworkPacket response = new NetworkPacket(RequestType.GET_ONLINE_USERS, onlineUsers);
                synchronized (output){
                    output.writeObject(response);
                    output.flush();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }
}
