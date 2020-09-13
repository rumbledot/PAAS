import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
        import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


import java.io.IOException;

public class Main extends Application {

    private Stage window;
    private Scene scene;
    private DBConnector db;
    private int width = 1200;
    private int height = 600;
    private ImageView imageView;
    private Image logo;

    private Font headerFont = Font.font("Verdana", FontWeight.BOLD, 20);

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setResizable(false);
//        window.setFullScreen(true);
        window.setTitle("WE COME IN PEACE FOR PIZZA");
//        window.initStyle(StageStyle.UNDECORATED);
        Group loginRoot = getLogin();
        Scene login = new Scene(loginRoot,width,height);
        window.setScene(login);
        window.show();
    }

    /**
     * Creates login screen for start up
     * @return Group containing login view
     */
    private Group getLogin(){
        Text header = new Text("Login");
        header.setFont(headerFont);
        header.setFill(Color.WHITE);
        Text userLabel = new Text("User: ");
        Text passLabel = new Text("Password: ");
        userLabel.setFill(Color.WHITE);
        passLabel.setFill(Color.WHITE);
        TextField user = new TextField();
        PasswordField password = new PasswordField();
        password.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    String username = user.getText();
                    String pass = password.getText();
                    loginAction(username,pass);
                }
            }
        });
        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = user.getText();
                String pass = password.getText();
                loginAction(username,pass);
            }
        });
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        vBox.setPrefWidth(100);
        vBox.getChildren().addAll(header,userLabel,user,passLabel,password,loginBtn);
        vBox.setLayoutX((width/2)-50);
        vBox.setLayoutY(height*.6);
        Group group = new Group();
        Rectangle bg = new Rectangle(width+50,height+50,Color.BLACK);
        bg.setLayoutX(0);
        bg.setLayoutY(0);
        group.getChildren().add(bg);
        imageView = new ImageView();
        logo = new Image("file:web app/img/pizza.png");
        imageView.setImage(logo);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setLayoutX((width/2)-100);
        imageView.setLayoutY(100);
        group.getChildren().add(imageView);
        group.getChildren().add(vBox);
        return group;
    }

    /**
     * Attempt to connect to Database with login details provided
     * @param username Database username
     * @param pass Database password
     */
    private void loginAction(String username, String pass){
        loading();
        db = new DBConnector();
        if(db.connectDB(username,pass)){
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
                root = loader.load();
                MainWindow controller = loader.getController();
                controller.initData(db, window);

            } catch (IOException e) {
                e.printStackTrace();
            }
            scene = new Scene(root, window.getWidth(), window.getHeight());
            scene.getStylesheets().add("ProductionStyle.css");
            window.setScene(scene);
        }
        else{
            Dialog dialog = new Dialog();
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
            dialog.getDialogPane().setContent(new Text("Unable to login. Please try again"));
            dialog.showAndWait();
        }
    }

    public void loading(){
        System.out.println("Set up something to indicate loading");

    }

    public static void main(String[] args) {

        launch(args);

    }
}
