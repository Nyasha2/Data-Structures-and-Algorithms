package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.TrieMap;
import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.HashSet;


public class TrieMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static ITrieMap<String, IDeque<String>, IDeque<String>> titles = new TrieMap<>((IDeque<String> s) -> s);

    public static void populateTitles() {
        titles.clear();
        for (String name: ID_MAP.keySet()){
            String[] titleWords = name.toLowerCase().split(" ");
            for (int i = 0; i < titleWords.length; i++) {
                IDeque<String> suffixDeque = new ArrayDeque<>();
                IDeque<String> movieNameDeque = new ArrayDeque<>();
                movieNameDeque.add(name);
                for (int j = i; j < titleWords.length; j++) {
                    suffixDeque.add(titleWords[j]);
                }
                if (titles.containsKey(suffixDeque)){
                    IDeque<String> toAdd = new ArrayDeque<>();
                    toAdd.addAll(titles.get(suffixDeque));
                    toAdd.add(name);
                    titles.put(suffixDeque, toAdd);
                }
                else{
                    titles.put(suffixDeque, movieNameDeque);
                }

            }

        }
    }

    public static IDeque<String> complete(String term) {
        String[] terms = term.toLowerCase().split(" ");
        HashSet<String> movieTitles = new HashSet<>();
        IDeque<String> toReturn = new ArrayDeque<>();
        IDeque<String> termsDeque = new ArrayDeque<>();
        for (int i = 0; i < terms.length; i++) {
            termsDeque.add(terms[i]);
        }

        ICollection<IDeque<String>> values;
        values = titles.getCompletions(termsDeque);
        for (IDeque<String> map: values){
            for (String s: map){
                movieTitles.add(s);
            }
        }
        for (String movie: movieTitles){
            toReturn.add(movie);
        }
        return toReturn;
    }
}
