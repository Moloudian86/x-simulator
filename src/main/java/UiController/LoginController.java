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
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private UserService authService = UserService.getInstance();

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignUp;

    @FXML
    private TextField passwordLogin;

    @FXML
    private TextField usernameLogin;

    @FXML
    void btnLoginAction(ActionEvent event){


        String username = usernameLogin.getText();
        String password = passwordLogin.getText();
        if (!UserService.getInstance().fillAllFields(username, password)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Please select all fields");
            alert.show();
            return;
        }

        if (UserService.getInstance().AdminLogin(username,password)){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Main.setMainStage(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (UserService.getInstance().Login(username,password)){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Main.setMainStage(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Login Failed");
            alert.setContentText("Incorrect username or password!");
            alert.show();
        }
    }
    @FXML
    void btnSignUpAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signUp-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSignUp.getStyleClass().add("mainbutton");
        btnLogin.getStyleClass().add("mainbutton");
    }
}
