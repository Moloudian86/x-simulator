package UiController;

import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.*;
import LogicController.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    private boolean eye = false;


    @FXML
    private TextField bio;

    @FXML
    private Button btnEye;

    @FXML
    private Button btnSave;

    @FXML
    private PasswordField confirmPasswprdSignUp;

    @FXML
    private TextField emailSignUp;

    @FXML
    private TextField fullNameSignUp;

    @FXML
    private PasswordField passwordSignUp;

    @FXML
    private TextField passwordVisible;

    @FXML
    private TextField phoneSignUp;

    @FXML
    private TextField usernameSignUp;

    boolean valid = true;


    private void setError(TextField field){
        field.getStyleClass().add("problemfield");
    }

    private void clearError(TextField field){
        field.getStyleClass().remove("problemfield");
    }

    @FXML
    void btnEyeAction(ActionEvent event) {
        if (!eye){
            passwordVisible.setVisible(true);
            passwordSignUp.setVisible(false);
            passwordVisible.setText(passwordSignUp.getText());
            Image image = new Image(getClass().getResourceAsStream("/img/no-eye.png"));
            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setPreserveRatio(true);

            btnEye.setGraphic(imageView);
            eye = true;
        }else {
            passwordSignUp.setVisible(true);
            passwordVisible.setVisible(false);
            passwordSignUp.setText(passwordVisible.getText());
            Image image = new Image(getClass().getResourceAsStream("/img/eye.png"));
            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setPreserveRatio(true);

            btnEye.setGraphic(imageView);
            eye = false;
        }
    }


    public void loadUser(User user){
        usernameSignUp.setText(user.getUsername());
        emailSignUp.setText(user.getEmail());
        fullNameSignUp.setText(user.getFullName());
        phoneSignUp.setText(user.getPhone());
        passwordSignUp.setText(user.getPassword());
        bio.setText(user.getBio());

    }

    @FXML
    void btnSaveAction(ActionEvent event) {

        User user = UserService.getCurrentUser();

        String fullName = fullNameSignUp.getText();
        String username = usernameSignUp.getText();
        String bio1 = bio.getText();
        String email = emailSignUp.getText();
        String phone = phoneSignUp.getText();
        String password;
        if(passwordSignUp.isVisible()){
            password = passwordSignUp.getText();
        }else{
            password = passwordVisible.getText();
        }

//        if (!UserService.getInstance().regexFullName(fullName)){
//            setError(fullNameSignUp);
//            valid = false;
//        }
//        if (!UserService.getInstance().regexUsername(username)){
//            setError(usernameSignUp);
//            valid = false;
//        }
//
//        if (!UserService.getInstance().regexEmail(email)){
//            setError(emailSignUp);
//            valid = false;
//        }
//        if (!UserService.getInstance().regexPhone(phone)){
//            setError(phoneSignUp);
//            valid = false;
//        }
//
//        if (!UserService.getInstance().regexPassword(password)){
//            setError(passwordSignUp);
//            valid = false;
//        }
//        if (!valid){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText("Error");
//            alert.setContentText("Please correct highlighted fields");
//            alert.show();
//            return;
//        }



        boolean result = UserService.getInstance().editProfile(fullName,username,email,phone,password,user,bio1);

        if (!result){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Sign Up Error");
            alert.setContentText("Username, email or phone already exists");
            alert.show();
            return;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(getClass().getResourceAsStream("/img/eye.png"));
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        imageView.setPreserveRatio(true);

        btnEye.setGraphic(imageView);
        passwordVisible.setVisible(false);



    }
}
