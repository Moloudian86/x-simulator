package UiController;

import LogicController.UserService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import model.User;

import java.io.File;
import java.io.IOException;

public class AddProfileImage {

    @FXML
    private Button btnAddMedia;

    @FXML
    private Label fileLable;
    private File selectedFile;

    @FXML
    void btnAddMediaAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        //فایل هایی که میتونه انتخاب کنه
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Media Files", "*.png", "*.jpg", "*.jpeg","*.cr2", "*.CR2"));

        //انتخاب فایل
        selectedFile = fileChooser.showOpenDialog(btnAddMedia.getScene().getWindow());
        if(selectedFile != null){
            fileLable.setText(selectedFile.getAbsolutePath());
        }
    }


    @FXML
    void btnBackAction(ActionEvent event) {
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
    void btnSetProfileAction(ActionEvent event) {
        if (selectedFile != null) {
            User user = UserService.getCurrentUser();
            user.setProfileImage(selectedFile.getAbsolutePath());
        }
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

}
