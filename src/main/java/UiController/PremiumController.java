package UiController;

import LogicController.PremiumService;
import LogicController.UserService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PremiumController implements Initializable {

    @FXML
    private Button btnBlueSubscription;


    @FXML
    private TextField textFieldDollarAmount;

    @FXML
    private Button btnGoldSubscription;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnBuyToken;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnPorofile;

    @FXML
    private Button btnPremium;

    @FXML
    private Button btnSearch;

    @FXML
    private Label creditLabel;

    @FXML
    private Label tokenLabel;

    private User currentUser = UserService.getCurrentUser();

    @FXML
    void btnBlueSubscriptionAction(ActionEvent event) {
        boolean result = PremiumService.getInstance().buyBlueSubscription(currentUser);
        if (result){
            currentUser = UserService.getCurrentUser();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Subscription Successfully");
            alert.setContentText("Blue subscription was activated successfully. 3000 tokens were added to your account.");
            alert.show();
            tokenLabel.setText(String.valueOf(currentUser.getToken()));
            creditLabel.setText(String.valueOf(currentUser.getCredit()));
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Subscription Unsuccessfully");
            alert.setContentText("Blue subscription was activated Unsuccessfully.");
            alert.show();
        }

    }

    @FXML
    void btnGoldSubscriptionAction(ActionEvent event) {
        boolean result = PremiumService.getInstance().buyGoldSubscription(currentUser);
        if (result){
            currentUser = UserService.getCurrentUser();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Subscription Successfully");
            alert.setContentText("Gold subscription was activated successfully. 3000 tokens were added to your account.");
            alert.show();
            tokenLabel.setText(String.valueOf(currentUser.getToken()));
            creditLabel.setText(String.valueOf(currentUser.getCredit()));
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Subscription Unsuccessfully");
            alert.setContentText("Gold subscription was activated Unsuccessfully.");
            alert.show();
        }
    }

    @FXML
    void btnBuyTokenAction(ActionEvent event) {
         int dollarAmount = Integer.parseInt(textFieldDollarAmount.getText());
         boolean result = PremiumService.getInstance().buyToken(currentUser,dollarAmount);
         if (result){
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
             alert.setHeaderText("Buy Token");
             alert.setContentText("Buy Token Successfully");
             alert.show();
             tokenLabel.setText(String.valueOf(currentUser.getToken()));
             creditLabel.setText(String.valueOf(currentUser.getCredit()));
         }else{
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setHeaderText("Buy Token");
             alert.setContentText("Buy Token Unsuccessfully");
             alert.show();
         }
    }



    private void setActive(Button button){

        btnHome.getStyleClass().remove("mainButton");
        btnSearch.getStyleClass().remove("mainButton");
        btnPorofile.getStyleClass().remove("mainButton");
        btnNew.getStyleClass().remove("mainButton");


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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image homeImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/home.png"));
        ImageView homeView = new ImageView(homeImg);
        homeView.setFitWidth(20);
        homeView.setFitHeight(20);
        btnHome.setGraphic(homeView);

        Image searchImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/search.png"));
        ImageView searchView = new ImageView(searchImg);
        searchView.setFitWidth(20);
        searchView.setFitHeight(20);
        btnSearch.setGraphic(searchView);

        Image profileImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/supervisedusercircle.png"));
        ImageView profileView = new ImageView(profileImg);
        profileView.setFitWidth(20);
        profileView.setFitHeight(20);
        btnPorofile.setGraphic(profileView);

        Image newPostImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/edit_graph_report_512px.png"));
        ImageView newPostView = new ImageView(newPostImg);
        newPostView.setFitWidth(20);
        newPostView.setFitHeight(20);
        btnNew.setGraphic(newPostView);

        Image premiumImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/approval_480px.png"));
        ImageView premiumView = new ImageView(premiumImg);
        premiumView.setFitWidth(20);
        premiumView.setFitHeight(20);
        btnPremium.setGraphic(premiumView);

        Image logOutImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/logout_144px.png"));
        ImageView logOutView = new ImageView(logOutImg);
        logOutView.setFitWidth(20);
        logOutView.setFitHeight(20);
        btnLogOut.setGraphic(logOutView);


        setActive(btnPremium);
        this.currentUser = UserService.getCurrentUser();
        creditLabel.setText(String.valueOf(currentUser.getCredit()));
        tokenLabel.setText(String.valueOf(currentUser.getToken()));
    }
}
