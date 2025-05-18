package org.imie.tp_vocabulaire;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.imie.model.Game;
import org.imie.model.Language;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Controller for the quiz view of the WordQuest application.
 * Responsible for displaying a table of source words and shuffled translation options,
 * handling user selections, and validating the quiz to compute the score.
 */
public class QuizController {
    public Button btnValidate;
    private MainApp app;
    private Language l1, l2;
    private ObservableList<ObservableList<String>> rows;
    private List<List<String>> shuffledRows;
    private Game game;

    /**
     * Sets the main application reference for this controller.
     * Called by MainApp after loading the FXML.
     *
     * @param app the MainApp instance
     */
    public void setApp(MainApp app) {
        this.app = app;
    }

    /**
     * Initializes quiz data.
     * Shuffles translation options, populates the TableView with rows,
     * configures cell factories for dropdowns, and initializes game logic.
     * We detect whether the csv data corresponds to Daniel's way of doing things or mine
     *
     * @param l1   the source Language model
     * @param l2   the target Language model
     * @param rows the observable rows list for the quiz table
     */
    public void initData(Language l1, Language l2, ObservableList<ObservableList<String>> rows) {
        this.l1 = l1;
        this.l2 = l2;
        this.rows = rows;

        boolean simpleFormat = l2.getWords().stream()
                .allMatch(opts -> opts.size() == 1);

        if (simpleFormat) {
            // Daniel's way of doing things: every row has exactly one correct translation
            List<String> allTranslations = l2.getWords().stream()
                    .map(List::getFirst)
                    .toList();

            this.shuffledRows = rows.stream()
                    .map(r -> {
                        List<String> copy = new ArrayList<>(allTranslations);
                        Collections.shuffle(copy);
                        return copy;
                    })
                    .collect(Collectors.toList());

        } else {
            // My way of doing things: every row has many similar translations and only one is correct.
            this.shuffledRows = l2.getWords().stream()
                    .map(originalList -> {
                        List<String> copy = new ArrayList<>(originalList);
                        Collections.shuffle(copy);
                        return copy;
                    })
                    .collect(Collectors.toList());
        }

        tableView.getItems().clear();
        tableView.getItems().addAll(rows);
        populateTable(l1, l2);
        this.game = new Game(l1, l2, rows, tableView);
    }

    /**
     * Utility method to build the initial rows list for the quiz.
     * Each row starts with the source word and an empty selection.
     *
     * @param l1 the source Language model
     * @param l2 the target Language model
     * @return an ObservableList of rows for the TableView
     */
    public static ObservableList<ObservableList<String>> buildRows(Language l1, Language l2) {
        var result = javafx.collections.FXCollections.<ObservableList<String>>observableArrayList();
        var w1 = l1.getWords();
        var w2 = l2.getWords();
        int n = Math.min(w1.size(), w2.size());
        for (int i = 0; i < n; i++) {
            result.add(javafx.collections.FXCollections
                    .observableArrayList(w1.get(i).getFirst(), ""));
        }
        return result;
    }
    @FXML private TableColumn<ObservableList<String>,String> colLangOne;
    @FXML private TableColumn<ObservableList<String>,String> colLangTwo;
    @FXML private TableView<ObservableList<String>> tableView;



    /**
     * Configures the TableView columns and cell factories.
     * Binds the first column to display source words and sets up
     * the second column to show a ComboBox with shuffled options.
     */
    void populateTable(Language l1, Language l2) {
        colLangOne.setText(l1.getName());
        colLangTwo.setText(l2.getName());

        colLangOne.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFirst())
        );

        colLangTwo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(1))
        );
        colLangTwo.setCellFactory(col -> new TableCell<ObservableList<String>,String>() {
            private final ComboBox<String> combo = new ComboBox<>();

            {
                combo.valueProperty().addListener((obs, oldV, newV) -> {
                    int idx = getIndex();
                    if (idx >= 0 && idx < getTableView().getItems().size()) {
                        getTableView().getItems().get(idx).set(1, newV);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    int idx = getIndex();
                    List<String> options = shuffledRows.get(idx);
                    combo.getItems().setAll(options);
                    combo.setValue(item);
                    setGraphic(combo);
                }
            }
        });

        tableView.setEditable(true);
        colLangTwo.setEditable(true);
    }

    /**
     * Handler for the "Validate" button click.
     * Calculates the quiz score and transitions to the score view.
     *
     * @param event the action event from the button
     * @throws Exception if view loading fails
     */
    @FXML
    protected void onValidateQuizClick(ActionEvent event) throws Exception {
        Double score = game.calculRate();
        System.out.println(score);
        app.showScoreView(score, game);
    }
}