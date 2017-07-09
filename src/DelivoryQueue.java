/**
 * Sean McGlincy
 * Networking
 * Assignment 6
 * Distance Vector Routing Algorithm
 */

/**
 * Created by Sean on 7/4/2017.
 */
public class DelivoryQueue {
    private long delivoryTime;
    private Package rtpkt;

    public DelivoryQueue(long delivoryTime, Package rtpkt) {
        this.delivoryTime = delivoryTime;
        this.rtpkt = rtpkt;
    }

    public long getDelivoryTime() {
        return delivoryTime;
    }

    public Package getRtpkt() {
        return rtpkt;
    }
}
