package edu.caltech.cs2.textgenerator;

import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.Iterator;
public class NGram implements Iterable<String>, Comparable<NGram> {
    public static final String NO_SPACE_BEFORE = ",?!.-,:'";
    public static final String NO_SPACE_AFTER = "-'><=";
    public static final String REGEX_TO_FILTER = "”|\"|“|\\(|\\)|\\*";
    public static final String DELIMITER = "\\s+|\\s*\\b\\s*";
    private IDeque<String> data;
    private static final int HASHING_PRIME = 37;

    public static String normalize(String s) {
        return s.replaceAll(REGEX_TO_FILTER, "").strip();
    }

    public NGram(IDeque<String> x) {
        this.data = new LinkedDeque<>();
        for (String word : x) {
            this.data.add(word);
        }
    }

    public NGram(String data) {
        this(normalize(data).split(DELIMITER));
    }

    public NGram(String[] data) {
        this.data = new LinkedDeque<>();
        for (String s : data) {
            s = normalize(s);
            if (!s.isEmpty()) {
                this.data.addBack(s);
            }
        }
    }

    public NGram next(String word) {
        String[] data = new String[this.data.size()];
        Iterator<String> dataIterator = this.data.iterator();
        dataIterator.next();
        for (int i = 0; i < data.length - 1; i++) {
            data[i] = dataIterator.next();
        }
        data[data.length - 1] = word;
        return new NGram(data);
    }

    public String toString() {
        String result = "";
        String prev = "";
        for (String s : this.data) {
            result += ((NO_SPACE_AFTER.contains(prev) || NO_SPACE_BEFORE.contains(s) || result.isEmpty()) ? "" : " ") + s;
            prev = s;
        }
        return result.strip();
    }

    @Override
    public Iterator<String> iterator() {
        return this.data.iterator();
    }

    @Override
    public int compareTo(NGram other) {
        if (this.equals(other)) {
            return 0;
        }
        if (this.data.size() < other.data.size()) {
            return -1;
        } else if (this.data.size() > other.data.size()) {
            return 1;
        } else {
            Iterator thisNGram = this.iterator();
            Iterator otherNGram = other.iterator();

            while (thisNGram.hasNext() && otherNGram.hasNext()) {
                String next1 = (String) thisNGram.next();
                String next2 = (String) otherNGram.next();
                if (!next1.equals(next2)) {
//                    String next1 = (String) thisNGram.next();
//                    String next2 = (String) otherNGram.next();
                    return next1.compareTo(next2);
                }
            }
        }
        return 0;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NGram)) {
            return false;
        }
        NGram otherNGram = (NGram) o;
        //check if the two NGrams are of the same size
        if (this.data.size() == otherNGram.data.size()) {
            //make two iterators of the data's of the NGrams
            Iterator thisNgram = this.data.iterator();
            Iterator secondNGram = otherNGram.iterator();

            while (thisNgram.hasNext()) {
                //compare them
                Object element1 = thisNgram.next();
                Object element2 = secondNGram.next();
                if (!element1.equals(element2)) {
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Object word: this.data){
            String currWord = (String)word;
            hash = (HASHING_PRIME * hash) + currWord.hashCode();
        }
        return hash;
    }
}
//    public int hashCode() {
//        if (this.data != null){
//            IDeque NGramDeque = new LinkedDeque<>();
//            NGramDeque.addAll(this.data);
//            Object something = NGramDeque.removeFront();
//            Iterator thisNgram = this.data.iterator();
//            int count = 0;
//            int firstHash = 0;
//            //get the first hash
//            while (thisNgram.hasNext() && count != 1){
//                String firstOne = (String)thisNgram.next();
//                count ++;
//                firstHash = firstOne.hashCode();
//            }
//        return firstHash + (HASHING_PRIME * (hashCodeHelper(NGramDeque)));
//        }
//        return 0;
//    }
//
//    private int hashCodeHelper(IDeque theDeque){
//        if (theDeque.size() == 1){
//            for (Object word: theDeque){
//                String lastWord = (String)word;
//                int lastHash = lastWord.hashCode();
//                return HASHING_PRIME * lastHash;
//            }
//        }
//        if (!theDeque.isEmpty()) {
//            String currWord = (String) theDeque.removeFront();
//            int currHash = currWord.hashCode();
//            return currHash + HASHING_PRIME * (hashCodeHelper(theDeque));
//        }
//        return 0;
//    }
//}