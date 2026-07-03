package UiController;

import LogicController.UserService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;

import java.io.IOException;

public class FollowersAndFollowing {

    @FXML
    private Button btnBack;

    @FXML
    private ListView<VBox> listView;

    private User profileUser;

    @FXML
    void btnBackAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("profile-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ProfileController controller = fxmlLoader.getController();
            controller.loadProfile(profileUser);
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFollowers(User user){
        this.profileUser = user;
        listView.getItems().clear();
        for (Integer userIds : user.getFollowers()){
            VBox userBox = createUserView(UserService.getInstance().getUserById(userIds));
            listView.getItems().add(userBox);
        }
    }

    public void showFollowing(User user){
        this.profileUser = user;
        listView.getItems().clear();
        for (Integer userIds : user.getFollowing()){
            VBox userBox = createUserView(UserService.getInstance().getUserById(userIds));
            listView.getItems().add(userBox);
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
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("profile-view.fxml"));
                Scene scene = new Scene(loader.load());
                ProfileController controller = loader.getController();
                controller.loadProfile(user);
                Main.setMainStage(scene);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        VBox userVbox = new VBox(10,username,fullNameData,bioData);
        userVbox.getStyleClass().add("box");
        return userVbox;

    }

}
