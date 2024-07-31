package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanChooser implements IHangmanChooser {
  private int GuessesRemaining;
  private SortedSet<Character> Guesses;
  private SortedSet<String> Words;
  private String Pattern;
  private static final Random selector = new Random();

  public EvilHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
    this.GuessesRemaining = maxGuesses;
    this.Guesses = new TreeSet<>();
    String pattern = "";

    //create a tree set to store words in
    SortedSet<String> selectedWords = new TreeSet<>();

    //check if word length is not negative
    if (wordLength < 1 || maxGuesses < 1){
      throw new IllegalArgumentException();
    }
    else {
      //read the words from the data/scrabble.txt file.
      File AllWords = new File("data/scrabble.txt");
      Scanner line = new Scanner(AllWords);
      while (line.hasNextLine()) {
        String currentWord = line.nextLine();
        //select words with the required length
        if (currentWord.length() == (wordLength)) {
          selectedWords.add(currentWord);
        }
      }
      //check if the set of selected words is not empty
      if (selectedWords.isEmpty()) {
        throw new IllegalStateException();
      } else {
        this.Words = selectedWords;
      }
      for (int i = 0; i < wordLength; i++ )
      {
        pattern += '-';
      }
      this.Pattern = pattern;
    }
  }


  @Override
  public int makeGuess(char letter) {
    //seeing if the guess is in lower case
    if (!Character.isLowerCase(letter)) {
      throw new IllegalArgumentException();
    }
    //getting the sorted list of all the guesses that have been made
    SortedSet<Character> Guesses = this.getGuesses();

    //checking if the letter has not been guessed before
    for (Character i : Guesses) {
      if (letter == i) {
        throw new IllegalArgumentException();
      }
    }
    //checking if the number of remaining guesses is at least one.
    if (this.GuessesRemaining < 1) {
      throw new IllegalStateException();
    }
    else {
      //create the occurrence variable
      int occurrence = 0;
      //Add the letter to the set of Guesses
      this.Guesses.add(letter);
      //create a hashmap of tree sets
      Map<String, SortedSet<String>> Family = new TreeMap<>();
      String pattern = "";
      //iterate over the set of selected words
      for (String word : this.Words) {
        pattern = word;
        //for each word, see if the guess is one of the letters and create a pattern
        for (int i = 0; i < word.length(); i++) {
          if (!Guesses.contains(word.charAt(i))) {
            pattern = pattern.replace(word.charAt(i), '-');
          }
        }
        //use the patterns  as the keys, and store the words with the same pattern in the set value
        if (Family.containsKey(pattern)) {
          Family.get(pattern).add(word);
        } else {
          Family.put(pattern, new TreeSet<>());
          Family.get(pattern).add(word);
        }
      }

        //iterate over the families map to determine which one has the most members
      int maxSize = 0;
      String finalPattern = "";
      for (String key: Family.keySet()){
        if (Family.get(key).size() > maxSize){
          maxSize = Family.get(key).size();
          finalPattern = key;
        }

      }

      this.Pattern = finalPattern;
      this.Words = Family.get(finalPattern);



        //find the occurences of the guessed letter in the chosen family.

      for (String element: this.Words) {
        int appearance = 0;
        for (int i = 0; i < element.length(); i++) {
          if (letter == element.charAt(i)) {
            appearance++;
          }
        }
        occurrence = appearance;
      }
      if (occurrence == 0){
        this.GuessesRemaining--;
      }
      return occurrence;
    }
  }


  @Override
  public boolean isGameOver() {
    //see if the words in this.Words are equal to the pattern.
    int isOver = 0;
    for (String element: this.Words){
      if (element.equals(this.getPattern())){
        isOver = 1;
        break;
      }
    }
    return this.GuessesRemaining == 0 || isOver == 1;
  }

  @Override
  public String getPattern() {
   return this.Pattern;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return this.Guesses;
  }

  @Override
  public int getGuessesRemaining() {
    return this.GuessesRemaining;
  }

  @Override
  public String getWord() {
    this.GuessesRemaining = 0;
    int location = selector.nextInt(this.Words.size());
    int counter = 0;
    String chosenWord = "";
    for (String word : this.Words) {
      if (counter == location) {
        chosenWord = word;
        break;
      }
      else {
        counter++;
      }
    }
    return chosenWord;
  }
}