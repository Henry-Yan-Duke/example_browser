package features;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Favorites {
    private Map<String, URL> myFavorites;

    public Favorites () {
        myFavorites = new HashMap<String, URL>();
    }

    /**
     * Adds given URL to favorites collection with given name.
     */
    public void add (String name, URL url) {
        // just in case, might be called before a page is visited
        if (url != null) {
            myFavorites.put(name, url);
        }
    }

    /**
     * Returns URL from favorites associated with given name, null if none set.
     */
    public URL getFavoriteURL (String name) {
        return myFavorites.get(name);
    }
}
