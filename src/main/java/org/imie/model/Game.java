package org.imie.model;


import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.List;

public class Game {
    public final ObservableList<ObservableList<String>> rows;
    public final Language l1;
    public final Language l2;
    private final TableView<ObservableList<String>> tableView;

    /**
     * Constructs a new Game instance bound to the given data and view.
     *
     * @param l1        the language model to traduce
     * @param l2        the language to be translated
     * @param rows      the quiz data rows
     * @param tableView the TableView showing the quiz
     */
    public Game(Language l1, Language l2, ObservableList<ObservableList<String>> rows, TableView<ObservableList<String>> tableView) {
        this.l1   = l1;
        this.l2   = l2;
        this.rows = rows;
        this.tableView = tableView;
    }

    /**
     * Calculates the percentage of correct answers.
     * Compares each user selection (column index 1) to the correct translation
     * (first entry in the corresponding list from l2.getWords()).
     *
     * @return the score as a percentage between 0.0 and 100.0
     */    public Double calculRate() {
        int bonnes = 0;
        int n = Math.min(rows.size(), l2.getWords().size());
        for (int i = 0; i < n; i++) {
            String choix   = rows.get(i).get(1);
            String correct = l2.getWords().get(i).getFirst();
            System.out.println("i : " + i + ", choix : " + choix + ", correct : " + correct+ "rows.size : " + rows.size());
            if (correct.equals(choix)) {
                bonnes++;
            }
        }
        return 100.0 * bonnes / rows.size();
    }

    /**
     * Resets all user selections in the quiz back to empty strings.
     * Iterates over each row and clears the second column (selection).
     */
    public void restart() {
        for (ObservableList<String> row : rows) {
            row.set(1, "");
        }
    }
}
