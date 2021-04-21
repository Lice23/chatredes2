package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        /*

        Button button = new Button("Hey what's up");
        button.setOnAction(e -> System.out.println("Nothin' much, how about you?"));

        GridPane layout = new GridPane();
        layout.getChildren().add(button);

        ScrollPane sp = new ScrollPane();

        Scene scene = new Scene(layout, 300, 300);

        primaryStage.setScene(scene);*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
