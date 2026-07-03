package UiController;

import LogicController.UserService;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {



    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignUp;

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

    @FXML
    private Button btnEye;

    private boolean eye = false;



    private void setError(TextField field){
        field.getStyleClass().add("problemfield");
    }

    private void clearError(TextField field){
        field.getStyleClass().remove("problemfield");
    }


    @FXML
    void btnLoginAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSignUpAction(ActionEvent event) {
        clearError(fullNameSignUp);
        clearError(usernameSignUp);
        clearError(emailSignUp);
        clearError(phoneSignUp);
        clearError(passwordSignUp);
        clearError(confirmPasswprdSignUp);
        boolean valid = true;
        String fullName = fullNameSignUp.getText();
        String username = usernameSignUp.getText();
        String email = emailSignUp.getText();
        String phone = phoneSignUp.getText();
        String password = passwordSignUp.getText();
        String confirmPassword = confirmPasswprdSignUp.getText();


        if (!UserService.getInstance().fillAllFields
                (fullName,username,email,phone, password,confirmPassword)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Please select all fields");
            alert.show();
            return;
        }

        if (!UserService.getInstance().checkPassword(password,confirmPassword)){
            setError(confirmPasswprdSignUp);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Incorrect confirm password");
            alert.show();
            return;
        }else {
            clearError(confirmPasswprdSignUp);
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

        boolean authentication = UserService.getInstance().signUp(fullName,username,email,phone, password);
        if (authentication){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hashtag-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Main.setMainStage(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Sign Up Error");
            alert.setContentText("Username, email or phone already exists");
            alert.show();
        }

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
            imageView.setPreserveRatio(true);

            btnEye.setGraphic(imageView);
            eye = true;
        }else {
            passwordSignUp.setVisible(true);
            passwordVisible.setVisible(false);
            passwordSignUp.setText(passwordVisible.getText());
            Image image = new Image(getClass().getResourceAsStream("/img/eye_96px.png"));
            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setPreserveRatio(true);

            btnEye.setGraphic(imageView);
            eye = false;
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(getClass().getResourceAsStream("/img/eye_96px.png"));
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        imageView.setPreserveRatio(true);

        btnEye.setGraphic(imageView);
        passwordVisible.setVisible(false);
        btnSignUp.getStyleClass().add("mainbutton");
        btnLogin.getStyleClass().add("mainbutton");
    }
}
