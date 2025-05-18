package org.imie.tp_vocabulaire;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.imie.model.FileService;
import org.imie.model.Language;

import java.io.File;
import java.util.List;

/**
 * Controller for the start view of the WordQuest application.
 * Handles user interactions on the welcome screen, including
 * the animated title and the "Load File" button to begin a quiz.
 */
public class StartController {
    private MainApp app;
    private final FileService fileService = new FileService();

    /**
     * Called by MainApp just after the FXML is loaded.
     * Stores a reference to the main application to allow view changes.
     *
     * @param app the main application instance
     */    public void setApp(MainApp app) {
        this.app = app;
    }

    @FXML public Region logoImage;
    @FXML public Button loadButton;
    @FXML private Label gameTitle;
    /**
     * Initializes the start view.
     * Sets up a continuous rotation animation on the game title.
     * This method is automatically called by the JavaFX framework
     * after FXML loading.
     */
    @FXML
    public void initialize() {
        gameTitle.setCache(true);
        gameTitle.setCacheHint(CacheHint.SPEED);

        logoImage.setOpacity(0);
        logoImage.setScaleX(0.5);
        logoImage.setScaleY(0.5);
        gameTitle.setOpacity(0);
        gameTitle.setTranslateY(-30);
        loadButton.setOpacity(0);
        loadButton.setTranslateY(30);
        PauseTransition delay = getPauseTransition();
        delay.play();
        TranslateTransition titleSlideIn = new TranslateTransition(Duration.seconds(0.8), gameTitle);
        titleSlideIn.setFromY(-50); titleSlideIn.setToY(0);
        titleSlideIn.setInterpolator(Interpolator.EASE_OUT);


        SequentialTransition intro = new SequentialTransition(
                new SequentialTransition(titleSlideIn)
        );
        intro.play();
        ScaleTransition logoPulse = new ScaleTransition(Duration.seconds(1), logoImage);
        logoPulse.setFromX(1); logoPulse.setFromY(1);
        logoPulse.setToX(1.05); logoPulse.setToY(1.05);
        logoPulse.setCycleCount(Animation.INDEFINITE);
        logoPulse.setAutoReverse(true);

        RotateTransition titleSwing = new RotateTransition(Duration.seconds(1), gameTitle);
        titleSwing.setFromAngle(-2); titleSwing.setToAngle(2);
        titleSwing.setCycleCount(Animation.INDEFINITE);
        titleSwing.setAutoReverse(true);

        logoPulse.play();
        titleSwing.play();
    }

    private PauseTransition getPauseTransition() {
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(e -> {

            FadeTransition fadeLogo  = fadeIn(logoImage);
            ScaleTransition scaleLogo = new ScaleTransition(Duration.seconds(1), logoImage);
            scaleLogo.setFromX(0.5); scaleLogo.setFromY(0.5);
            scaleLogo.setToX(1);     scaleLogo.setToY(1);
            ParallelTransition logoIntro = new ParallelTransition(logoImage, fadeLogo, scaleLogo);

            FadeTransition fadeTitle  = fadeIn(gameTitle);
            ParallelTransition titleIntro = getParallelTransition(fadeTitle);
            FadeTransition fadeBtn    = fadeIn(loadButton);
            SequentialTransition intro = getSequentialTransition(fadeBtn, logoIntro, titleIntro);

            intro.play();
        });
        return delay;
    }

    private ParallelTransition getParallelTransition(FadeTransition fadeTitle) {
        TranslateTransition slideTitle = new TranslateTransition(Duration.seconds(0.8), gameTitle);
        slideTitle.setFromY(-30);
        slideTitle.setToY(0);
        ScaleTransition popTitle = new ScaleTransition(Duration.seconds(0.4), gameTitle);
        popTitle.setFromX(1);
        popTitle.setFromY(1);
        popTitle.setToX(1.1);
        popTitle.setToY(1.1);
        popTitle.setCycleCount(2);
        popTitle.setAutoReverse(true);
        return new ParallelTransition(gameTitle, fadeTitle, slideTitle, popTitle);
    }

    private SequentialTransition getSequentialTransition(FadeTransition fadeBtn, ParallelTransition logoIntro, ParallelTransition titleIntro) {
        SequentialTransition intro = getTransition(fadeBtn, logoIntro, titleIntro);
        intro.setInterpolator(Interpolator.EASE_OUT);

        intro.setOnFinished(ev -> {
            // pulse logo
            ScaleTransition logoPulse = new ScaleTransition(Duration.seconds(2), logoImage);
            logoPulse.setFromX(1); logoPulse.setFromY(1);
            logoPulse.setToX(1.05); logoPulse.setToY(1.05);
            logoPulse.setCycleCount(Animation.INDEFINITE);
            logoPulse.setAutoReverse(true);
            logoPulse.play();

            // swing titre
            RotateTransition titleSwing = new RotateTransition(Duration.seconds(4), gameTitle);
            titleSwing.setFromAngle(-3); titleSwing.setToAngle(3);
            titleSwing.setCycleCount(Animation.INDEFINITE);
            titleSwing.setAutoReverse(true);
            titleSwing.play();
        });
        return intro;
    }

    private SequentialTransition getTransition(FadeTransition fadeBtn, ParallelTransition logoIntro, ParallelTransition titleIntro) {
        TranslateTransition slideBtn = new TranslateTransition(Duration.seconds(0.8), loadButton);
        slideBtn.setFromY(30);
        slideBtn.setToY(0);
        ScaleTransition scaleBtn = new ScaleTransition(Duration.seconds(0.6), loadButton);
        scaleBtn.setFromX(0.8);
        scaleBtn.setFromY(0.8);
        scaleBtn.setToX(1);
        scaleBtn.setToY(1);
        ParallelTransition btnIntro = new ParallelTransition(loadButton, fadeBtn, slideBtn, scaleBtn);

        return new SequentialTransition(logoIntro, titleIntro, btnIntro);
    }

    /** utilitaire pour créer un fade-in de durée donnée */
    private FadeTransition fadeIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.seconds(1.0), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        return ft;
    }

    /**
     * Event handler for the "Load File" button.
     * Opens a FileChooser dialog to select a CSV file,
     * reads the data, creates Language models, builds quiz rows,
     * and transitions to the quiz view.
     *
     * @param event the action event triggered by clicking the button
     * @throws Exception if file reading or view loading fails
     */
    @FXML
    protected void onLoadFileClick(ActionEvent event) throws Exception {
        Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        File csv = fileService.chooseCsvFile(stage);
        if (csv == null) return;
        List<List<String>> data = fileService.readCsv(csv);
        fileService.instanceLanguages(data);
        Language l1 = fileService.getLanguageOne();
        Language l2 = fileService.getLanguageTwo();
        var rows = QuizController.buildRows(l1, l2);
        app.showQuizView(l1, l2, rows);
    }
}