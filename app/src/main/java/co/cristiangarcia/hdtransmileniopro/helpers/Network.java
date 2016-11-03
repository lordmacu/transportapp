package co.cristiangarcia.hdtransmileniopro.helpers;

/**
 * Created by cristiangarcia on 26/10/16.
 */

public class Network {
    public int destination;
    public int origin;
    public double weight;

    public Network(int iOrigin, int iDestination, double dWeight) {
        this.origin = iOrigin;
        this.destination = iDestination;
        this.weight = dWeight;
    }
}