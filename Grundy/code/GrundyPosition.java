package edu.caltech.cs2.lab05;

import java.util.*;

public class GrundyPosition {
    /*
     * Stores a mapping from the height of a pile to how many of those piles exist.
     * Does not include piles of size less than three.
     */
    private static HashMap<SortedMap<Integer, Integer>, Boolean> memoizeCache = new HashMap<>();
    private SortedMap<Integer, Integer> heapCounts;

    /**
     * Initializes a GrundyPosition with a single heap of height heapHeight.
     **/
    public GrundyPosition(int heapHeight) {
        this.heapCounts = new TreeMap<>();
        this.heapCounts.put(heapHeight, 1);
    }

    public GrundyPosition(TreeMap<Integer, Integer> heapCounts){
        this.heapCounts = heapCounts;
    }

    /**
     * Returns a list of legal GrundyPositions that a single move of Grundy's Game
     * can get to.
     **/
    public List<GrundyPosition> getMoves() {
        List<GrundyPosition> toReturn = new ArrayList<>();
        for (Integer heapHeight : this.heapCounts.keySet()){ // iterates over heapHeights
                for (int pile1Height = 1; pile1Height <= heapHeight/2; pile1Height++) {
                    //do a split
                    TreeMap<Integer, Integer> heapCountsCopy = new TreeMap<>(this.heapCounts);
                    heapCountsCopy.put(heapHeight, heapCountsCopy.get(heapHeight) - 1);
                    if (heapCountsCopy.get(heapHeight) == 0) heapCountsCopy.remove(heapHeight);
                    int pile2Height = heapHeight - pile1Height;
                    if (pile1Height != pile2Height) {
                        if (pile1Height > 2)
                            heapCountsCopy.put(pile1Height, heapCountsCopy.getOrDefault(pile1Height, 0) + 1);
                        if (pile2Height > 2)
                            heapCountsCopy.put(pile2Height, heapCountsCopy.getOrDefault(pile2Height, 0) + 1);
                        toReturn.add(new GrundyPosition(heapCountsCopy));
                    }
                }
        }
        return toReturn;
    }

    public boolean isTerminalPosition() {
       return this.heapCounts.isEmpty();
    }

    public boolean isPPosition() {
        if (memoizeCache.containsKey(this.heapCounts)){
            return memoizeCache.get(this.heapCounts);
        }
        for (GrundyPosition position: this.getMoves()){
            if (!position.isNPosition()) {
                memoizeCache.put(position.heapCounts, true);
                return false;
            }
            memoizeCache.put(position.heapCounts, false);
        }
        return true;
    }

    public boolean isNPosition()  {
        if (memoizeCache.containsKey(this.heapCounts)){
            return  !memoizeCache.get(this.heapCounts);
        }
        for (GrundyPosition position: this.getMoves()){
            if (position.isPPosition()){
                memoizeCache.put(position.heapCounts, true);
                return true;
            }
            memoizeCache.put(position.heapCounts, false);
        }
        return false;
    }

    /**
     * Ignore everything below this point.
     **/

    @Override
    public int hashCode() {
       return this.heapCounts.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GrundyPosition)) {
            return false;
        }
        return this.heapCounts.equals(((GrundyPosition) o).heapCounts);
    }

    @Override
    public String toString() {
        return this.heapCounts.toString();
    }
}