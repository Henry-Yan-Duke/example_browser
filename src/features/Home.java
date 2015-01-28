package features;

import java.net.URL;


public class Home implements Feature {
    private URL myHome;
    private URL myCurrentURL;

    public Home () {
        myHome = null;
        myCurrentURL = null;
    }

    @Override
    public void handleURL (URL url) {
        myCurrentURL = url;
    }

    /**
     * Returns URL of the current home page or null if none is set.
     */
    public URL getURL () {
        return myHome;
    }

    /**
     * Sets current home page to the current URL being viewed.
     */
    public void setHome () {
        if (myCurrentURL != null) {
            myHome = myCurrentURL;
        }
    }
}
