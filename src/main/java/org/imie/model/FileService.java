package org.imie.model;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileService {
    @FXML
    private TableColumn<ObservableList<String>,String> colLangOne;
    @FXML private TableColumn<ObservableList<String>,String> colLangTwo;
    @FXML private TableView<ObservableList<String>> tableView;
    public static Language languageOne;
    public static Language languageTwo;

    /**
     * Opens the file explorer to let the user select a CSV file.
     *
     * @param owner the parent stage for the dialog
     * @return the selected File, or null if the user cancelled
     */
    public File chooseCsvFile(Stage owner) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Ouvrir un fichier CSV");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
        );
        return chooser.showOpenDialog(owner);
    }

    /**
     * Reads the given CSV file, splitting each line on semicolons.
     *
     * @param csvFile the CSV file to read
     * @return a list of rows, each row is a list of cell values
     * @throws IOException if an I/O error occurs while reading the file
     */
    public List<List<String>> readCsv(File csvFile) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length < 2) continue;
                rows.add(Arrays.asList(parts));
            }
        }
        return rows;
    }




    /**
     * Creates Language instances from the parsed CSV data.
     * The first row contains the two language names. Subsequent rows contain
     * a word in the first language and its list of similar words in the second.
     *
     * @param data the raw CSV data as a list of rows
     */
    public void instanceLanguages(List<List<String>> data) {
        if (data == null || data.size() < 2) {
            throw new IllegalArgumentException("Need at least a header and one data row");
        }

        boolean richFormat = data.get(1).size() > 2;

        List<List<String>> w1 = new ArrayList<>();
        List<List<String>> w2 = new ArrayList<>();

        String name1 = data.getFirst().get(0);
        String name2 = data.getFirst().get(1);

        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.size() < 2) continue;

            w1.add(Collections.singletonList(row.get(0)));

            if (richFormat) {
                w2.add(new ArrayList<>(row.subList(1, row.size())));
            } else {
                w2.add(Collections.singletonList(row.get(1)));
            }
        }

        languageOne = new Language(name1, w1);
        languageTwo = new Language(name2, w2);
    }

    /**
     * @return the instantiated Language for the first column
     */
    public Language getLanguageOne() { return languageOne; }
    /**
     * @return the instantiated Language for the second column
     */
    public Language getLanguageTwo() { return languageTwo; }
}
