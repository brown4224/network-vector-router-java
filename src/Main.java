import java.util.ArrayList;

/**
 * Created by Sean on 7/4/2017.
 */
public class Main {

    private static int INFINITY = 999;
    private static int size = 4;
    private static int [][] connectionCost = new int[size][size];  // Used by toLayer2
    private static Router router0;
    private static Router router1;
    private static Router router2;
    private static Router router3;

    //  Delivery queue
    private static ArrayList<DelivoryQueue> q = new ArrayList<DelivoryQueue>();

    // Link Change queue
    private static ArrayList<LinkUpdateQueue> updateQueue = new ArrayList<LinkUpdateQueue>();


    public static void main(String[] args) {

        //  Initialize Connection Cost
        //  -1 means not connected
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                connectionCost[i][j] = -1;
            }
        }

        // Add connections:  Used by to Layer 2
        // Router 0
        connectionCost[0][0] = 0;
        connectionCost[0][1] = 1;
        connectionCost[0][2] = 3;
        connectionCost[0][3] = 7;

        // Router 1
        connectionCost[1][0] = 1;
        connectionCost[1][1] = 0;
        connectionCost[1][2] = 1;

        // Router 2
        connectionCost[2][0] = 3;
        connectionCost[2][1] = 1;
        connectionCost[2][2] = 0;
        connectionCost[2][3] = 3;

        //Router 3
        connectionCost[3][0] = 7;
        connectionCost[3][2] = 2;
        connectionCost[3][3] = 0;

        //////////////////////////////////////////////////
        /////////////    CHANGE LINK HERE  ///////////////
        //////////////////////////////////////////////////
        /**
         * We will change some of the connections so that some
         * connections increase and some decrease.  Then we will change
         * them back to their original values and topography.
         *
         * There are numerous issues outside the scope of this assignment.
         * We will limit the values from 1 to INFINITY - 1
         *
         *
         * All values will be greater then 0
         * A router will not be allowed to update itself.
         */

        //  update Router 0: {0, 3} to value 2
        updateQueue.add(new LinkUpdateQueue(0,3,2));

//        //  update Router 1: {1, 0) to value 5
//        updateQueue.add((new LinkUpdateQueue(1,0,5)));
//
//        //  update Router 2: {2, 1} to Value 3
//        updateQueue.add(new LinkUpdateQueue(2,1,3));
//
//
//        // Change back
//        //  update Router 0: {0, 3} to value 7
//        updateQueue.add(new LinkUpdateQueue(0,3,7));
//
//        //  update Router 1: {1, 0) to value 1
//        updateQueue.add((new LinkUpdateQueue(1,0,1)));
//
//        //  update Router 2: {2, 1} to Value 1
//        updateQueue.add(new LinkUpdateQueue(2,1,1));
//




        // Start Program

         router0 = new Router(0, 1, 3, 7);
         router1 = new Router(1, 0, 1, INFINITY);
         router2 = new Router(3, 1, 0, 2);
         router3 = new Router(7, INFINITY, 2, 0);

        router0.rtinit();
        router1.rtinit();
        router2.rtinit();
        router3.rtinit();


        // Go through queue until all tables are updated
        boolean run = true;   // Simulation
        while (run){

            // Process list
            // See if message has needs to be delivered
            delivoryService();

            System.out.printf("\n\n");
            System.out.printf("#####################################\n");
            System.out.printf("###########Final Tables##############\n");
            System.out.printf("#####################################\n");
            router0.toString("Final Router 0");
            router1.toString("Final Router 1");
            router2.toString("Final Router 2");
            router3.toString("Final Router 3");
            System.out.printf("#####################################\n");
            System.out.printf("###########     END    ##############\n");
            System.out.printf("#####################################\n");


            //  Send a new VALUE to router
            if(updateQueue.size() > 0){
                LinkUpdateQueue newLink = updateQueue.remove(0);

                int value = newLink.getNewValue();
                int router = newLink.getRouter();
                int destRouter = newLink.getConnectedRouter();

                if(value > 0 && value < INFINITY && router != destRouter){

                    System.out.printf("\n\n");
                    System.out.printf("#####################################\n");
                    System.out.printf("Updating Router:  New value: " + value + " for link { " + router + ", " + destRouter + "}\n");
                    System.out.printf("#####################################\n");

                    connectionCost[router][destRouter] = value;
                    connectionCost[destRouter][router] = value;

                    switch (router){
                        case 0:
                            router0.linkCostChangeHandler(destRouter, value);
                            break;
                        case 1:
                            router1.linkCostChangeHandler(destRouter, value);
                            break;
                        case 2:
                            router2.linkCostChangeHandler(destRouter, value);
                            break;
                        case 3:
                            router3.linkCostChangeHandler(destRouter, value);
                            break;
                        default:
                            System.out.println("An error occurred when updating Router Link");
                            System.out.println("See Main function");
                            System.exit(1);
                    }

                }

            } else {
                // Program is done
                run = false;
            }


        }






    }
    private static void delivoryService(){
        while (q.size() > 0){
            for(int i =0; i < q.size(); i++){

                DelivoryQueue item = q.get(i);
                long delivoryTime = item.getDelivoryTime();

                if(System.currentTimeMillis() >= delivoryTime){
                    // Deliver Message
                    int dest = item.getRtpkt().getDestinationID();
                    switch (dest){
                        case 0:
                            router0.rtupdate(item.getRtpkt());
                            break;
                        case 1:
                            router1.rtupdate(item.getRtpkt());
                            break;
                        case 2:
                            router2.rtupdate(item.getRtpkt());
                            break;
                        case 3:
                            router3.rtupdate(item.getRtpkt());
                            break;
                    }

                    q.remove(i);  // Remove after item after delivory
                }
            }
        }
    }


    public static void toLayer2(Package rtpkt){
        int source = rtpkt.getSourceID();
        int destination = rtpkt.getDestinationID();

        if(source != destination){
            long clock = System.currentTimeMillis();
            long delay = connectionCost[source][destination];
            q.add(new DelivoryQueue(clock + delay, rtpkt ));

        }
    }
}
