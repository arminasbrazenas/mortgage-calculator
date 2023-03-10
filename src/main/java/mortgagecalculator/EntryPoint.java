package mortgagecalculator;

import mortgagecalculator.controllers.MainViewController;
import mortgagecalculator.utils.Localization;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EntryPoint extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"), Localization.getBundle("lt"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 640);

        MainViewController mainViewController = loader.getController();
        mainViewController.stage = stage;

        stage.setTitle(Localization.getMessage("mortgageCalculator"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}