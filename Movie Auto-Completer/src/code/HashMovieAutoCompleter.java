package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.interfaces.IDeque;


import java.util.HashMap;
import java.util.Map;


public class HashMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static Map<String, IDeque<String>> titles = new HashMap<>();
    private static AbstractMovieAutoCompleter Movie_IDs;

    public static void populateTitles() {
        titles.clear();
        Map<String, String> Movies = Movie_IDs.getIDMap();
        for (Map.Entry<String, String> entry : Movies.entrySet()) {
            String movieName = entry.getValue();
            titles.put(movieName, new LinkedDeque<>());
            String[] titleWords = movieName.split(" ");
            for (int i = 0; i < titleWords.length; i++) {
                String suffix = titleWords[i];
                for (int j = i + 1; j < titleWords.length; j++) {
                    suffix += " " + titleWords[j];
                }
                titles.get(movieName).add(suffix);
            }
//            titles.get(movieName).addBack(movieName);
//            for (int i = 0; i < movieName.length(); i++) {
//                if (movieName.charAt(i) == ' ') {
//                    String suffix = movieName.substring(i + 1);
//                    titles.get(movieName).addBack(suffix);
//                    movieName = suffix;
//                }
//            }
        }
    }

    public static IDeque<String> complete(String term) {
        IDeque<String> matches = new ArrayDeque<>();
        Map<String, String> movieNames = Movie_IDs.getIDMap();
        for (Map.Entry<String, String> entry : movieNames.entrySet()) {
            String name = " " + entry.getKey().toLowerCase() + " ";
            if (name.contains(" " + term.toLowerCase() + " ")) {
                matches.add(entry.getKey());
            }
        }
        return matches;
    }
}