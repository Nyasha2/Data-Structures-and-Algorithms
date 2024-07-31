package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;


public class RandomHangmanChooser implements IHangmanChooser {
  private int GuessesRemaining;
  private SortedSet<Character> Guesses;
  private final String Word;
  private static final Random selector = new Random();


  public RandomHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
    this.GuessesRemaining = maxGuesses;
    this.Guesses = new TreeSet<>();

    //create a tree set to store words in
    SortedSet<String> selectedWords = new TreeSet<>();

    //check if word length is not negative
    if (wordLength < 1 || maxGuesses < 1){
      throw new IllegalArgumentException();
    }
    else{
      //read the words from the data/scrabble.txt file.
      File AllWords = new File("data/scrabble.txt");
      Scanner line = new Scanner(AllWords);
      while (line.hasNextLine()){
        String currentWord = line.nextLine();
        //select words with the required length
        if (currentWord.length() == (wordLength)) {
          selectedWords.add(currentWord);
        }
      }
      //check if the set of selected words is not empty
      if (selectedWords.isEmpty()) {
        throw new IllegalStateException();
      }
      //if not, randomly select one word from the set
      else {
        String tempWord = "";
        //get a random integer between 0 and the size of the selectedWords set..
        int location = selector.nextInt(selectedWords.size());
        int counter = 0;
        for (String word : selectedWords) {
          if (counter == location) {
            tempWord = word;
            break;
          }
          else {
            counter++;
          }
        }
        this.Word = tempWord;
      }
    }
  }    

  @Override
  public int makeGuess(char letter) {
    //seeing if the guess is in lower case
    if (!Character.isLowerCase((letter))) {
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
    //getting the number of occurrences of the letter
    else {
      this.Guesses.add(letter);
      int occurrence = 0;
      for (int i = 0; i < this.Word.length(); i++) {
        if (letter == this.Word.charAt(i)) {
          occurrence++;
        }
      }
      if (occurrence == 0){
        this.GuessesRemaining--;
      }
      return occurrence;
    }
  }

  @Override
  public boolean isGameOver() {
    return this.GuessesRemaining == 0 || this.Word.equals(this.getPattern());
  }

  @Override
  public String getPattern() {
    String word = this.Word;
    SortedSet<Character> Guesses = this.getGuesses();
    for (int i = 0; i < this.Word.length(); i++){
      if (!Guesses.contains(word.charAt(i))){
        word = word.replace(word.charAt(i), '-');
      }
    }
    return word;
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
    return this.Word;
  }
}