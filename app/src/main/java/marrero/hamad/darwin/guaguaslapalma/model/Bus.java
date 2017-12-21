package marrero.hamad.darwin.guaguaslapalma.model;

/**
 * Created by Antonio on 10/11/2017.
 */

import com.esri.arcgisruntime.geometry.Point;

/**
 * A class representing one bus.  A bus keeps track of what route it is
 * following, where it is along the route, its current {@link Point} location
 * on the map, as well as its own unique graphic identifier, with which you can
 * refer to this specific graphic/taxi within the application.
 */
public class Bus {

    private BusRoute followingRoute;
    private int location;
    private int graphicIdentifier;
    private Point currentPosition;
    private boolean goSlow = false;
    private boolean stopAtJunction = false;
    private int countDown = 0; // used to slow taxi or stop it for a while

    public Bus(BusRoute route, int positionOnRoute, int graphicID) {
        followingRoute = route;
        graphicIdentifier = graphicID;
        setLocation(positionOnRoute);
    }

    public boolean MoveTaxi() {
        boolean haveWeMoved = false;

        // have we come to a slowdown point?
        int slowCountDown = 2;
        if (followingRoute.getSlowFlag().get(location) == 1) {
            // 2 ticks per move now
            goSlow = true;
            countDown = slowCountDown;

            // shift to the next location along the route
            location++;
        }

        // have we come to a stop point?
        if (followingRoute.getStopFlag().get(location) == 1) {
            // stop for a few ticks
            stopAtJunction = true;
            int stopCountDown = 200;
            countDown = stopCountDown;

            // shift to the next location along the route
            location++;
        }

        // if we are in countdown (slow or stop at junction), decrement count down
        if (countDown > 0) {
            countDown--;
        }

        // are we okay to move forward?
        if (countDown == 0) {
            //update the current position geometry
            currentPosition = new Point (getXPos(), getYPos());
            haveWeMoved = true;

            // were we in stop mode?
            if (stopAtJunction) {
                // move ahead at full speed
                stopAtJunction = false;
            }

            // were we in show mode?
            if (goSlow) {
                // keep going slow
                countDown = slowCountDown;
            }

            // increment the location making sure we have not dropped off the end of the of route
            if (location++ >= (followingRoute.getXPos().size()-1)) {
                // move back to the start of the route
                location = 0;
            }
        }

        return haveWeMoved;
    }

    private int getYPos() {
        return (followingRoute.getYPos().get(location)).intValue();
    }

    private int getXPos() {
        return (followingRoute.getXPos().get(location)).intValue();
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public int getLocation() {
        return location;
    }

    private void setLocation(int location) {
        this.location = location;
    }

    public int getGraphicIdentifier() {
        return graphicIdentifier;
    }

    public BusRoute getFollowingRoute() {
        return followingRoute;
    }

    public void setGraphicIdentifier(int graphicIdentifier) {
        this.graphicIdentifier = graphicIdentifier;
    }
}