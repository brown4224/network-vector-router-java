import java.util.ArrayList;

/**
 * Created by Sean on 7/4/2017.
 */
public class Main {

    private static int INFINITY = 999;
    private static long startTime;
    private static int size = 4;
    private static int [][] connectionCost = new int[size][size];  // Used by toLayer2
    private static Router router0;
    private static Router router1;
    private static Router router2;
    private static Router router3;

    //  Delivery queue
    private static ArrayList<DelivoryQueue> q = new ArrayList<DelivoryQueue>();


    public static void main(String[] args) {

        //  Initialize Connection Cost
        //  -1 means not connected
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                connectionCost[size][size] = -1;
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



        // Start Program
//        startTime = System.currentTimeMillis();

         router0 = new Router(0, 1, 3, 7);
         router1 = new Router(1, 0, 1, INFINITY);
         router2 = new Router(3, 1, 0, 2);
         router3 = new Router(7, INFINITY, 2, 0);

        router0.rtinit();
        router1.rtinit();
        router2.rtinit();
        router3.rtinit();


        // Go through queue until all tables are updated
        boolean run = true;
        while (run){
            if (q.size() <= 0){
                run = false;  // List empty, we are done
            } else {
                // Process list
                // See if message has needs to be delivered
                delivoryService();
            }
        }


        System.out.printf("\n\n");
        System.out.printf("#####################################\n");
        System.out.printf("###########Final Tables##############\n");
        System.out.printf("#####################################\n");
        router0.toString("Final Router 0");
        router1.toString("Final Router 1");
        router2.toString("Final Router 2");
        router3.toString("Final Router 3");



    }
    private static void delivoryService(){
        for(int i =0; i < q.size(); i++){

            DelivoryQueue item = q.get(i);
            long delivoryTime = item.getDelivoryTime();

            if(System.currentTimeMillis() <= delivoryTime){
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


    public static void toLayer2(Package rtpkt){
        // Copy
//        Package payload = new Package(rtpkt);
        int source = rtpkt.getSourceID();
        int destination = rtpkt.getDestinationID();

        if(source != destination){
            long clock = System.currentTimeMillis();
            long delay = connectionCost[source][destination];
            q.add(new DelivoryQueue(clock + delay, rtpkt ));

        }

        // Delay
//        long clock = System.currentTimeMillis();
//        while (clock < connectionCost[source][destination]){
//            clock = System.currentTimeMillis();
//        }

        // Deliver Package
        // Switch Statement
    }
}
