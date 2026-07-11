package network;

import UiController.ChatController;
import javafx.scene.layout.VBox;
import model.ChatMessage;

import java.io.ObjectInputStream;
import java.util.List;
import javafx.application.*;

public class ClientListener extends Thread{
    private ObjectInputStream input;
    private ChatController controller;

    public ClientListener(ObjectInputStream input, ChatController controller){
        this.input = input;
        this.controller = controller;
    }
    @Override
    public void run() {
        try {
            while (true){
                NetworkPacket packet = (NetworkPacket) input.readObject();
                if (packet.getRequestType() == RequestType.GET_CONVERSATION){
                    List<ChatMessage> messages = (List<ChatMessage>) packet.getData();
                    Platform.runLater(() -> {controller.showMessages(messages);});
                }
                if (packet.getRequestType() == RequestType.NEW_MESSAGE) {
                    ChatMessage msg = (ChatMessage) packet.getData();
                    Platform.runLater(() -> {
                        VBox box = controller.createMessageView(msg);
                        controller.getListView().getItems().add(box);
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
