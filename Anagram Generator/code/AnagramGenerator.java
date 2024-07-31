package edu.caltech.cs2.lab04;

import java.util.ArrayList;
import java.util.List;

public class AnagramGenerator {
    public static void printPhrases(String phrase, List<String> dictionary) {
        LetterBag phraseBag = new LetterBag(phrase);
        printPhrases(phraseBag, dictionary, new ArrayList<>());
    }

    private static void printPhrases(LetterBag refBag, List<String> dictionary, List<String> accumulator){
        if (refBag.isEmpty()){
            System.out.println(accumulator);
        }
        else{
            for (String word: dictionary){
                LetterBag wordBag = new LetterBag(word);
                LetterBag differenceBag = refBag.subtract(wordBag);
                if (differenceBag != null){
                    List<String> accCopy = new ArrayList<>(accumulator);
                    accCopy.add(word);
                    printPhrases(differenceBag, dictionary, accCopy);
                }
            }
        }
    }

    public static void printWords(String word, List<String> dictionary) {
        LetterBag refBag = new LetterBag(word);
        for (String entry : dictionary){
            LetterBag entryBag = new LetterBag(entry);
            LetterBag differenceBag = refBag.subtract(entryBag);
            if (differenceBag!= null && differenceBag.equals(refBag.subtract(refBag))){
                System.out.println(entry);
            }
        }
    }
}
