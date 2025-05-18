package org.imie.model;

import java.util.List;

public class Language {
    public final String modelName;
    public final List<List<String>> modelWords;

    /**
     * Constructs a Language model with the given name and word groups.
     *
     * @param languageName  the display name of this language
     * @param languageWords a list of word lists; each sublist contains words related to a source term
     */
    public Language(String languageName, List<List<String>> languageWords) {
        modelName = languageName;
        modelWords = languageWords;
        System.out.println(modelName);
        System.out.println(modelWords);
    }
    /**
     * Gets the display name of this language.
     *
     * @return the language name
     */
    public String getName() { return modelName; }

    /**
     * Gets the word groups for this language.
     *
     * @return the list of word lists
     */
    public List<List<String>> getWords() { return modelWords; }
}
