/**
 * Created by Sean on 7/6/2017.
 */
public class LinkUpdateQueue {
    private int router;
    private int ConnectedRouter;
    private int newValue;

    public LinkUpdateQueue(int router, int connectedRouter, int newValue) {
        this.router = router;
        ConnectedRouter = connectedRouter;
        this.newValue = newValue;
    }

    public int getRouter() {
        return router;
    }

    public int getConnectedRouter() {
        return ConnectedRouter;
    }

    public int getNewValue() {
        return newValue;
    }
}
