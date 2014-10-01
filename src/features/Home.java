package features;

import java.net.URL;


public class Home {
    private URL myHome;

    public Home () {
        myHome = null;
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
    public void setHome (URL newHome) {
        if (newHome != null) {
            myHome = newHome;
        }
    }
}
