/**
 * Sean McGlincy
 * Networking
 * Assignment 6
 * Distance Vector Routing Algorithm
 */


/**
 * Created by Sean on 7/4/2017.
 */
public class Package {
    private int sourceID;
    private int destinationID;
    private int[] minCost = new int[4];

    // New Package
    public Package(int sourceID, int destinationID, int[] minCost) {
        this.sourceID = sourceID;
        this.destinationID = destinationID;
        this.minCost = minCost;
    }

    // Copy Existing Package
    public Package(Package copy) {
        this.sourceID = copy.sourceID;
        this.destinationID = copy.destinationID;
        this.minCost = copy.minCost;
    }

    public int getSourceID() {
        return sourceID;
    }

    public int getDestinationID() {
        return destinationID;
    }

    public int[] getMinCost() {
        return minCost;
    }
}
