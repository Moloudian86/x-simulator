package UiController;

import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Database;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private Button btnPopularHashtags;

    @FXML
    private Button btnPopularPost;

    @FXML
    private Button btnPostsList;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnUsersList;

    @FXML
    private Label postListLabel;

    @FXML
    private Label userListLabel;

    @FXML
    void btnPopularHashtagsAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminCoice-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AdminChoiceController controller = fxmlLoader.getController();
            controller.setTitle("Popular Hashtags");
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPopularPostAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminCoice-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AdminChoiceController controller = fxmlLoader.getController();
            controller.setTitle("Popular Posts");
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPostsListAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminCoice-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AdminChoiceController controller = fxmlLoader.getController();
            controller.setTitle("Posts List");
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void btnReportsAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminCoice-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AdminChoiceController controller = fxmlLoader.getController();
            controller.setTitle("Reports");
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnUsersListAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminCoice-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AdminChoiceController controller = fxmlLoader.getController();
            controller.setTitle("Users List");
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnBackAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnPostsList.getStyleClass().add("btnPostList");
        btnReports.getStyleClass().add("btnReports");
        postListLabel.setText(String.valueOf(Database.getInstance().getPosts().size()));
        userListLabel.setText(String.valueOf(Database.getInstance().getUsers().size()));
    }
}
