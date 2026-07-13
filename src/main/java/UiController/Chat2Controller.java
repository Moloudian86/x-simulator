package UiController;

import LogicController.UserService;
import com.example.twitter.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.ChatMessage;
import model.MessageStatus;
import model.User;
import network.ChatClient;
import network.ChatServer;
import network.NetworkPacket;
import network.RequestType;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class Chat2Controller implements Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private ListView<VBox> listView;

    @FXML
    private ListView<VBox> listViewUsers;

    @FXML
    private Button sendMessage;

    @FXML
    private TextArea textArea;

    private User currentUser;
    private User receiver;
    private ChatClient client = new ChatClient();
    private Timeline timeline;


    private VBox createUserView(User user) {
        HBox userProfile = new HBox(10);
        ImageView profileView = PostviewBuilder.createProfileImageView(user);
        Button username = new Button(user.getUsername());
        username.getStyleClass().add("btnUsername");
        Circle online = new Circle(5);
        if(ChatServer.onlineUsers.containsKey(user.getId())){
            online.setStyle("-fx-fill: purple;");
        }
        else{
            online.setStyle("-fx-fill: gray;");
        }
        userProfile.getChildren().addAll(profileView,online,username);
        Label fullNameText = new Label("Full Name: ");
        Label fullName = new Label(user.getFullName());
        Label bioText = new Label("Bio: ");
        Label bio = new Label(user.getBio());

        HBox fullNameData = new HBox(10,fullNameText, fullName);
        HBox bioData = new HBox(10,bioText, bio);
        username.setOnAction(e -> {
            initChat(user);

        });
        VBox userVbox = new VBox(10,userProfile, fullNameData,bioData);
        userVbox.getStyleClass().add("box");
        return userVbox;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = UserService.getCurrentUser();
        client.connect(this);
        NetworkPacket registerPacket = new NetworkPacket(RequestType.REGISTER, currentUser);
        client.send(registerPacket);
        timeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            if (receiver != null) {
                loadMessages();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        List<User> users = UserService.getInstance().findAllUser();
        for (User u : users) {
            if (u.getId() == currentUser.getId())
                continue;

            if (u.isBlocked())
                continue;

            VBox userBox = createUserView(u);
            listViewUsers.getItems().add(userBox);
        }
    }

    public void initChat(User receiver) {
        this.receiver = receiver;
        listView.getItems().clear();
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
        Label nike = new Label("✔");
        Label nike2 = new Label("✔✔");

        content.setWrapText(true);
        Label time = new Label(msg.getSendTime().toString());
        VBox messageBox = new VBox(8, userProfile, content, time);
        if (msg.getSender().getId() == currentUser.getId()){
            if (msg.getStatus() == MessageStatus.SEEN ){
                messageBox.getChildren().add(nike2);
            }else {
                messageBox.getChildren().add(nike);
            }
        }

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
            if (timeline != null) {
                timeline.stop();
            }
            client.disconnect();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
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
