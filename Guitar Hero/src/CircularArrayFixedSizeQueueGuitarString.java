package edu.caltech.cs2.project03;

import edu.caltech.cs2.datastructures.CircularArrayFixedSizeQueue;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Random;


public class CircularArrayFixedSizeQueueGuitarString {
    private IFixedSizeQueue<Double> data;

    private static final int SAMPLING_RATE = 44100;
    private static final double DECAY_FACTOR = 0.996;

    private static final Random RANDOMIZER = new Random();
    public CircularArrayFixedSizeQueueGuitarString(double frequency) {
        int capacity = (int)Math.ceil(SAMPLING_RATE / frequency);
        this.data = new CircularArrayFixedSizeQueue(capacity);
        while (!this.data.isFull()){
            this.data.enqueue(0.0);
        }
    }

    public int length() {
        return this.data.size();
    }

    public void pluck() {
        this.data.clear();
        while (!this.data.isFull()){
            double value = RANDOMIZER.nextDouble(-0.5, 0.5);
            this.data.enqueue(value);
        }
    }

    public void tic() {
        double originalValue = this.data.peek();
        this.data.dequeue();
        double newValue = this.data.peek();
        double toBeAdded = DECAY_FACTOR * 0.5 * (newValue + originalValue);
        this.data.enqueue(toBeAdded);

    }

    public double sample() {
        return this.data.peek();
    }

}
