import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lib.CacheDAO;
import model.Cache;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main function. loads login pane
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Cache main = new Cache();
        CacheDAO.loadData(main);

        try {
            ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
            Locale local = Locale.getDefault();
            main.updateLocale(local);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("res/login.fxml"));
        LoginController controller = new LoginController(main);
        loader.setController(controller);
        primaryStage.setTitle("C195");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }

    /**
     * Main function. calls start
     * @param args argument
     */
    public static void main(String[] args) {
        launch(args);
    }
}
