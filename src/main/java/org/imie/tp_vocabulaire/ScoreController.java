package org.imie.tp_vocabulaire;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import org.imie.model.Game;

/**
 * Controller for the score view of the WordQuest application.
 * Displays the user's final score, provides feedback messages,
 * and allows restarting the quiz.
 */
public class ScoreController {
    public Label scoreLabel;
    public Label adviceMessage;
    public Label score;
    public Label statsLabel;
    private MainApp app;
    private Game game;

    /**
     * Initializes the controller with references to the main application and game state.
     * Called by MainApp after the FXML is loaded.
     *
     * @param app  the main application instance for view navigation
     * @param game the Game instance containing quiz data and logic
     */
    public void setApp(MainApp app, Game game) {
        this.game = game;
        this.app = app;
    }
    /**
     * Converts the computed score to an integer percentage,
     * updates the score label, and sets the feedback message and color.
     *
     * @param score the calculated score as a Double
     */
    public void initScore(Double score) {
        int pct        = score.intValue();
        int total      = game.rows.size();
        long answered  = game.rows.stream()
                .filter(r -> r.get(1) != null && !r.get(1).isEmpty())
                .count();
        long unanswered = total - answered;

        scoreLabel.setText(pct + "%");

        statsLabel.setText(
                String.format("Vous avez répondu à %d des %d questions.%nNon répondues : %d",
                        answered, total, unanswered)
        );

        setAdviceMessageAndScoreColor(pct);
    }

    /**
     * Updates the advice message and changes the score text color
     * according to the score range.
     *
     * @param score the score as an integer percentage
     */
    public void setAdviceMessageAndScoreColor(int score) {
        if(score < 40) {
            this.adviceMessage.setText("Vous devez faire mieux.");
            this.scoreLabel.setStyle("-fx-text-fill: #dc3545;");
        } else if (score < 60) {
            this.adviceMessage.setText("Bon travail. Continuez à vous entrainer.");
            this.scoreLabel.setStyle("-fx-text-fill: #ffc107;");
        } else {
            this.adviceMessage.setText("Excellent travail. Bravo !");
            this.scoreLabel.setStyle("-fx-text-fill: #28a745;");
        }
    }

    /**
     * Event handler for the "Recommencer" button.
     * Resets the game state and returns to the quiz view.
     *
     * @param event the action event triggered by clicking the restart button
     * @throws Exception if the quiz view fails to load
     */
    public void onRestartClick(ActionEvent event) throws Exception {
        game.restart();
        app.showQuizView(game.l1, game.l2, game.rows);
    }

    public void onLoadFileClick(ActionEvent actionEvent) throws Exception {
        game.restart();
        app.showStartView();
    }
}
