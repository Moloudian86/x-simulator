package UiController;

import LogicController.UserService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.ChatMessage;
import model.MessageStatus;
import model.User;
import LogicController.MessageService;
import network.ChatClient;
import network.NetworkPacket;
import network.RequestType;

import javafx.scene.image.ImageView;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import javafx.geometry.Pos;

public class ChatController {

    @FXML
    private Button btnBack;

    @FXML
    private ListView<VBox> listView;

    @FXML
    private TextArea textArea;



    private User currentUser;
    private User receiver;
    private ChatClient client = new ChatClient();

    public void initChat(User receiver) {
        this.currentUser = UserService.getCurrentUser();
        this.receiver = receiver;
        client.connect(this);
        NetworkPacket registerPacket = new NetworkPacket(RequestType.REGISTER, currentUser);
        client.send(registerPacket);
        loadMessages();
        User[] users = {currentUser, receiver};
        NetworkPacket packet = new NetworkPacket(RequestType.MARK_AS_SEEN, users);
        client.send(packet);
    }
    private void loadMessages() {
        User[] users = {currentUser, receiver};
        NetworkPacket packet = new NetworkPacket(RequestType.GET_CONVERSATION, users);
        client.send(packet);
    }

    @FXML
    void sendMessageAction(ActionEvent event) {
        String text = textArea.getText().trim();
        if (text.isEmpty()) return;
        ChatMessage msg = new ChatMessage(currentUser, receiver, text);
        msg.setSendTime(new Timestamp(System.currentTimeMillis()));
        NetworkPacket packet = new NetworkPacket(RequestType.SEND_MESSAGE, msg);
        client.send(packet);
        VBox box = createMessageView(msg);
        listView.getItems().add(box);
        textArea.clear();

    }

    public void showMessages(List<ChatMessage> messages) {
        listView.getItems().clear();
        for (ChatMessage msg : messages) {
            VBox box = createMessageView(msg);
            listView.getItems().add(box);
        }
    }

    public VBox createMessageView(ChatMessage msg) {
        HBox userProfile = new HBox(10);
        ImageView profileView = PostviewBuilder.createProfileImageView(msg.getSender());
        Button username = new Button(msg.getSender().getUsername());
        username.getStyleClass().add("btnUsername");
        username.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("profile-view.fxml"));
                Scene scene = new Scene(loader.load());

                ProfileController controller = loader.getController();
                controller.loadProfile(msg.getSender());

                Main.setMainStage(scene);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        userProfile.getChildren().addAll(profileView, username);
        Label content = new Label(msg.getContent());


        content.setWrapText(true);
        Label time = new Label(msg.getSendTime().toString());
        VBox messageBox = new VBox(8, userProfile, content, time);


        messageBox.setPrefWidth(300);
        messageBox.setMaxWidth(300);


        if (msg.getSender().getId() == currentUser.getId()) {
            messageBox.setStyle(" -fx-background-color:#1DA1F2; -fx-background-radius:15; -fx-padding:10; ");
            messageBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageBox.setStyle(" -fx-background-color:#DDDDDD; -fx-background-radius:15; -fx-padding:10;");
            messageBox.setAlignment(Pos.CENTER_LEFT);
        }

        HBox container = new HBox();

        if (msg.getSender().getId() == currentUser.getId()) {
            container.setAlignment(Pos.CENTER_RIGHT);
        } else {
            container.setAlignment(Pos.CENTER_LEFT);
        }
        container.getChildren().add(messageBox);

        VBox root = new VBox(container);

        return root;
    }
    @FXML
    void btnBackAction(ActionEvent event) {
        try {
            NetworkPacket packet = new NetworkPacket(RequestType.DISCONNECT, null);
            client.send(packet);
            client.disconnect();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("message-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ListView<VBox> getListView() {
        return listView;
    }
}