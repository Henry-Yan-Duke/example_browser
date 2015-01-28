package features;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Favorites implements Feature {
    private Map<String, URL> myFavorites;
    private URL myCurrentURL;

    public Favorites () {
        myFavorites = new HashMap<String, URL>();
        myCurrentURL = null;
    }

    @Override
    public void handleURL (URL url) {
        myCurrentURL = url;
    }

    /**
     * Adds given URL to favorites collection with given name.
     */
    public void add (String name) {
        // just in case, might be called before a page is visited
        if (myCurrentURL != null) {
            myFavorites.put(name, myCurrentURL);
        }
    }

    /**
     * Returns URL from favorites associated with given name, null if none set.
     */
    public URL getFavoriteURL (String name) {
        return myFavorites.get(name);
    }
}
