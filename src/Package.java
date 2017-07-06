/**
 * Created by Sean on 7/4/2017.
 */
public class Package {
    private int sourceID;
    private int destinationID;
    private int connectionCost;
    private int [] minCost = new int [4];
    private int [] ttl;  // Flag to update occurred

    // New Package
    public Package(int sourceID, int destinationID, int[] minCost, int connectionCost, int ttl) {
        this.sourceID = sourceID;
        this.destinationID = destinationID;
        this.minCost = minCost;
        this.connectionCost = connectionCost;
        this.ttl = ttl;
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

    public int getConnectionCost() {
        return connectionCost;
    }

    public void setConnectionCost(int connectionCost) {
        this.connectionCost = connectionCost;
    }
}
