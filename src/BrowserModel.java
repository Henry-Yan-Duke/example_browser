import java.net.URL;
import features.*;


/**
 * This represents the heart of the browser: the collections that organize all
 * the URLs into useful structures.
 * 
 * @author Edwin Ward
 * @author Robert C. Duvall
 */
public class BrowserModel {
    private Home myHome;
    private History myHistory;
    private Favorites myFavorites;

    /**
     * Creates an empty model.
     */
    public BrowserModel () {
        myHome = new Home();
        myHistory = new History();
        myFavorites = new Favorites();
    }

    /**
     * Returns the first page in next history, null if next history is empty.
     */
    public URL next () {
        return myHistory.next();
    }

    /**
     * Returns the first page in back history, null if back history is empty.
     */
    public URL back () {
        return myHistory.back();
    }

    /**
     * Returns true if there is a next URL available
     */
    public boolean hasNext () {
        return myHistory.hasNext();
    }

    /**
     * Returns true if there is a previous URL available
     */
    public boolean hasPrevious () {
        return myHistory.hasPrevious();
    }

    /**
     * Changes current page to given URL, removing next history.
     */
    public void go (URL url) {
        myHistory.add(url);
    }

    /**
     * Returns URL of the current home page or null if none is set.
     */
    public URL getHome () {
        return myHome.getURL();
    }

    /**
     * Sets current home page to the current URL being viewed.
     */
    public void setHome () {
        myHome.setHome(myHistory.getCurrentURL());
    }

    /**
     * Adds current URL being viewed to favorites collection with given name.
     */
    public void addFavorite (String name) {
        myFavorites.add(name, myHistory.getCurrentURL());
    }

    /**
     * Returns URL from favorites associated with given name, null if none set.
     */
    public URL getFavorite (String name) {
        return myFavorites.getFavoriteURL(name);
    }
}
