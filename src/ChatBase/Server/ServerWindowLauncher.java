package ChatBase.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Para executar, é necessário adicionar as seguintes opções de VM:
 * --module-path "caminho/para/javafxsdk" --add-modules javafx.controls,javafx.fxml
 * */
public class ServerWindowLauncher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));
        window.setTitle("Servidor");
        window.setScene(new Scene(root, 1280, 720));
        window.show();
    }
}
