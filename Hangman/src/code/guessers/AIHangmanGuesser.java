package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AIHangmanGuesser implements IHangmanGuesser {
  private static String dictName = "data/scrabble.txt";

  @Override
  public char getGuess(String pattern, Set<Character> guesses) throws FileNotFoundException {
    SortedSet<String> words = new TreeSet<>();
    SortedSet<String> selectedWords = new TreeSet<>();
    //read the words from the data/scrabble.txt file.
    File AllWords = new File(dictName);
    Scanner line = new Scanner(AllWords);
    while (line.hasNextLine()){
      String currentWord = line.nextLine();
      //select words with the required length
      if (currentWord.length() == pattern.length()) {
        selectedWords.add(currentWord);
      }
    }
    for (String word: selectedWords){
      String orgWord = word;
      String sequence = "";
      for (int i = 0; i < pattern.length(); i++){
        if (pattern.charAt(i) == '-' && !guesses.contains(word.charAt(i))){
          word = word.replace(word.charAt(i), '-');
        }
      }
      if (word.equals(pattern)){
        words.add(orgWord);
      }
    }
    //create a set of all alphabetical letters
    char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    char freqLetter = '-';
    int mostFreq = 0;
    for (int i = 0; i < 26; i++) {
      char letter = alphabet[i];
      int occurrence = 0;
      if (!guesses.contains(letter)) {
        for (String element: words) {
          for (int j = 0; j < element.length(); j++){
            if (element.charAt(j) == letter){
              occurrence++;
              //element = element.replace(element.charAt(j), '-');.
            }
          }
        }
      }
      if (occurrence > mostFreq){
        mostFreq = occurrence;
        freqLetter = letter;
      }
    }
    return freqLetter;
  }
}
