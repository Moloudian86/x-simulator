package UiController;

import LogicController.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.ChatMessage;
import model.User;
import LogicController.MessageService;

import java.util.List;

public class ChatController {

    @FXML
    private Button btnBack;

    @FXML
    private ListView<VBox> listView;

    @FXML
    private TextArea textArea;

    private final MessageService messageService = new MessageService();

    private User currentUser;
    private User receiver;

    public void initChat(User receiver) {
        this.currentUser = UserService.getCurrentUser();
        this.receiver = receiver;
        loadMessages();
        messageService.markMessagesAsSeen(currentUser, receiver);
    }

    private void loadMessages() {
        listView.getItems().clear();
        List<ChatMessage> messages = messageService.getConversation(currentUser, receiver);
        for (ChatMessage msg : messages) {
            VBox box = createMessageView(msg);
            listView.getItems().add(box);
        }
    }

    @FXML
    void sendMessageAction(ActionEvent event) {
        String text = textArea.getText().trim();
        if (text.isEmpty()) return;
        ChatMessage msg = messageService.sendMessage(currentUser, receiver, text);
        if (msg != null) {
            VBox box = createMessageView(msg);
            listView.getItems().add(box);
            textArea.clear();
        }
    }

    private VBox createMessageView(ChatMessage msg) {
        Label username = new Label(msg.getSender().getUsername());
        Label content = new Label(msg.getContent());
        Label time = new Label(msg.getSendTime() != null ? msg.getSendTime().toString() : "");
        VBox box = new VBox(5, username, content, time);

        if (msg.getSender().getId() == currentUser.getId()) {
            box.setStyle("-fx-background-color: #1DA1F2; -fx-padding: 10;");
        } else {
            box.setStyle("-fx-background-color: lightgray; -fx-padding: 10;");
        }
        return box;
    }

    @FXML
    void btnBackAction(ActionEvent event) {

    }
}