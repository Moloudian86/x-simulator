package UiController;

import LogicController.UserService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MessageController implements Initializable {


    @FXML
    private Button btnHome;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnPorofile;

    @FXML
    private Button btnSearch;


    @FXML
    private Button btnPremium;

    @FXML
    private Button btnMessages;

    @FXML
    private ListView<VBox> listViewUsers;

    private void setActive(Button button){
        btnHome.getStyleClass().remove("mainButton");
        btnSearch.getStyleClass().remove("mainButton");
        btnPorofile.getStyleClass().remove("mainButton");
        btnNew.getStyleClass().remove("mainButton");
        btnLogOut.getStyleClass().remove("mainButton");
        button.getStyleClass().add("mainButton");
    }

    @FXML
    void btnHomeAction(ActionEvent event) {
        setActive(btnHome);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSearchAction(ActionEvent event) {
        setActive(btnSearch);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("search.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPorofileAction(ActionEvent event) {
        setActive(btnPorofile);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("profile-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ProfileController controller = fxmlLoader.getController();
            controller.loadProfile(UserService.getCurrentUser());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnNewAction(ActionEvent event) {
        setActive(btnNew);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("newPost-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPremiumAction(ActionEvent event) {
        setActive(btnPremium);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("premium-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnMessagesAction(ActionEvent event) {
        setActive(btnMessages);
    }


    @FXML
    void btnLogOutAction(ActionEvent event) {
        try {
            UserService.setCurrentUser(null);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private VBox createUserView(User user) {
        Button username = new Button(user.getUsername());
        username.getStyleClass().add("btnUsername");
        Label fullNameText = new Label("Full Name: ");
        Label fullName = new Label(user.getFullName());
        Label bioText = new Label("Bio: ");
        Label bio = new Label(user.getBio());

        HBox fullNameData = new HBox(10,fullNameText, fullName);
        HBox bioData = new HBox(10,bioText, bio);
        username.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("chat-view.fxml"));
                Scene scene = new Scene(loader.load());
                ChatController controller = loader.getController();
                controller.initChat(user);

                Main.setMainStage(scene);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        VBox userVbox = new VBox(10,username,fullNameData,bioData);
        userVbox.getStyleClass().add("box");
        return userVbox;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setActive(btnMessages);
        List<User> users = UserService.getInstance().findAllUser();
        for (User u : users){
            if (u.getId() == UserService.getCurrentUser().getId()){
                continue;
            }
            if (u.isBlocked()){
                continue;
            }
            VBox userBox = createUserView(u);
            listViewUsers.getItems().add(userBox);
        }
    }
}
