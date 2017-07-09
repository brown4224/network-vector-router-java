/**
 * Sean McGlincy
 * Networking
 * Assignment 6
 * Distance Vector Routing Algorithm
 */

/**
 * Created by Sean on 7/4/2017.
 */
public class Router {

    /**
     * True: each router prints after it updates.
     * False: only print from main function
     */
    private boolean printEachStep = false;

    private static int ID = 0;  // counter for router ID
    private final int INFINITY = 999;
    private int size = 4;
    private final int thisRouter = ID;

    // Arrays
    private int[] initialCost;
    private int[] minCost = new int[size];
    private int[][] distanceTable = new int[size][size];

    /**
     * Pass an int value for the connection cost.
     * If the connection doesn't exist.  Pass -1 or 999
     *
     * @param router0 Pass cost of connection.  -1 or 999 are no connection 'Infinity'
     * @param router1 Pass cost of connection.  -1 or 999 are no connection 'Infinity'
     * @param router2 Pass cost of connection.  -1 or 999 are no connection 'Infinity'
     * @param router3 Pass cost of connection.  -1 or 999 are no connection 'Infinity'
     */
    public Router(int router0, int router1, int router2, int router3) {
        ID++;
        if (router0 < 0)
            router0 = INFINITY;
        if (router1 < 0)
            router1 = INFINITY;
        if (router2 < 0)
            router2 = INFINITY;
        if (router3 < 0)
            router3 = INFINITY;

        initialCost = new int[]{router0, router1, router2, router3};

        // Intialize Arrays:  Set everything to INFINITY
        for (int i = 0; i < size; i++) {
            // 1-D arrays
            minCost[i] = INFINITY;

            // 2-D arrays
            for (int j = 0; j < size; j++) {
                distanceTable[i][j] = INFINITY;
            }
        }
    }

    public void rtinit() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {

                    // UPdate tables
                    distanceTable[i][j] = initialCost[i];
                    minCost[i] = initialCost[i];
                }
            }
        }
        if (printEachStep) {
            // Print table
            String str = "Initial table:  Router: " + thisRouter;
            toString(str);
        }
        // Send to other routers
        sendrt();
    }


    public void rtupdate(Package rtpkt) {
        int source = rtpkt.getSourceID();
        int[] pktCost = rtpkt.getMinCost();
        boolean flag = false;

        for (int i = 0; i < size; i++) {

            // Don't process this router   (i != thisRouter)
            // Don't process non-connections   (pktCost[i] < INFINITY)
            // Don't process the initial values  (i != source)
            if (i != thisRouter &&
                    pktCost[i] < INFINITY &&
                    i != source) {

                // Update distance table with packets cost + cost to get their
                int cost = distanceTable[source][source];
                distanceTable[i][source] = pktCost[i] + cost;

                // If mincost changed
                if (minCost[i] > distanceTable[i][source]) {
                    minCost[i] = distanceTable[i][source];
                    flag = true;
                }
            }
        }
        if (printEachStep) {
            toString("Updated Min Cost");
        }

        if (flag) {
            sendrt();
        }
    }

    // Takes the router number as ID
    public void linkCostChangeHandler(int connectedRouter, int newCost) {

        // Router can't update it's self
        if (thisRouter == connectedRouter) return;

        int update = newCost - distanceTable[connectedRouter][connectedRouter];
        // Update distance table
        for (int i = 0; i < size; i++) {
            if (newCost < INFINITY)
                distanceTable[i][connectedRouter] += update;
            else
                distanceTable[i][connectedRouter] = INFINITY;
        }
        distanceTable[connectedRouter][connectedRouter] = newCost;

        if (printEachStep) {
            String str = "Updating Table:  New value: " + newCost + " for link { " + thisRouter + ", " + connectedRouter + "}\n";
            toString(str);
        }
        sendrt();
    }

    /**
     * This function take the source node and mincost.
     * It cycles through all of the neighbors and sends a package for delivory
     * If the node is the current router.  We don't send
     *
     * @param source :  INT this.router
     * @param cost   :    INT [] this.mincost
     */
    private void sendrt() {
        resetMinCost();

        for (int i = 0; i < size; i++) {
            if (distanceTable[i][i] < INFINITY && i != thisRouter) {  // If there is connection and not the same router
                Main.toLayer2(new Package(thisRouter, i, minCost));
            }
        }
    }

    public void toString(String message) {
        int count = 0;
        System.out.printf("\n\n");
        System.out.printf("Message: " + message + "\n");
        System.out.printf("                via     \n");
        System.out.printf("Router %1d |    0    1     2    3 \n", thisRouter);
        System.out.printf(" --------|-----------------------\n");
        for (int i = 0; i < size; i++) {
            if (count != thisRouter) {
                System.out.printf("dest    %1d| %3d   %3d   %3d   %3d\n", i, distanceTable[i][0], distanceTable[i][1], distanceTable[i][2], distanceTable[i][3]);
            }
            count++;
        }
        System.out.printf("\n");
        System.out.printf("Router: " + thisRouter + " MinCost: [");
        for (int i = 0; i < size; i++) {
            System.out.printf("%3d, ", minCost[i]);
        }
        System.out.printf("]\n");
    }

    private void resetMinCost() {
        // reset and update min cost array
        minCost[0] = distanceTable[0][0];
        minCost[1] = distanceTable[1][1];
        minCost[2] = distanceTable[2][2];
        minCost[3] = distanceTable[3][3];
        for (int i = 0; i < size; i++) {
            if (minCost[0] > distanceTable[0][i])
                minCost[0] = distanceTable[0][i];
            if (minCost[1] > distanceTable[1][i])
                minCost[1] = distanceTable[1][i];
            if (minCost[2] > distanceTable[2][i])
                minCost[2] = distanceTable[2][i];
            if (minCost[3] > distanceTable[3][i])
                minCost[3] = distanceTable[3][i];
        }
    }
}
