package org.imie.tp_vocabulaire;


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.imie.model.Game;
import org.imie.model.Language;

import java.util.Objects;

public class MainApp extends Application {
    /**
     * Primary stage (main window) of the application.
     */
    private Stage primaryStage;
    /**
     * Game instance that holds the current quiz state.
     * @deprecated ?
     */
    private Game game;

    /**
     * Called when the JavaFX application is launched.
     * Initializes the primary stage, sets the application icon,
     * shows the start view, and displays the window.
     *
     * @param stage the main stage provided by JavaFX
     * @throws Exception if the FXML cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        Image icon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("favicon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Word Quest - Jeu éducatif");
        showStartView();
        primaryStage.show();
    }

    /**
     * Loads and displays the start screen of the application.
     * This view contains a button to load a vocabulary file.
     *
     * @throws Exception if the FXML file cannot be loaded
     */
    public void showStartView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start-view.fxml"));
        Parent root = loader.load();
        StartController ctrl = loader.getController();
        ctrl.setApp(this);
        primaryStage.setScene(new Scene(root));
    }

    /**
     * Loads and displays the quiz view.
     * The quiz view shows a table of words and dropdowns for the user
     * to select translations.
     *
     * @param l1   the first language model (source words)
     * @param l2   the second language model (translations)
     * @param rows the table data containing word pairs and user selections
     * @throws Exception if the FXML file cannot be loaded
     */
    public void showQuizView(Language l1, Language l2, ObservableList<ObservableList<String>> rows) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz-view.fxml"));
        Parent root = loader.load();
        QuizController ctrl = loader.getController();
        ctrl.setApp(this);
        ctrl.initData(l1, l2, rows);
        primaryStage.setScene(new Scene(root));
    }
    /**
     * Loads and displays the result (score) view.
     * The result view shows the user's score and a button to restart.
     *
     * @param score the final score percentage to display
     * @param game  the Game instance containing the quiz data
     * @throws Exception if the FXML file cannot be loaded
     */
    public void showScoreView(Double score, Game game) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("score-view.fxml"));
        Parent root = loader.load();
        ScoreController ctrl = loader.getController();
        ctrl.setApp(this, game);
        ctrl.initScore(score);
        primaryStage.setScene(new Scene(root));
    }

}