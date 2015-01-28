package features;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class History implements Feature {
    private List<URL> myHistory;
    private int myCurrentIndex;

    public History () {
        myHistory = new ArrayList<URL>();
        myCurrentIndex = -1;
    }

    /**
     * Wipes all "next" history and appends given URL as the last item in history.
     */
    @Override
    public void handleURL (URL url) {
        if (hasNext()) {
            myHistory = myHistory.subList(0, myCurrentIndex + 1);
        }
        myHistory.add(url);
        myCurrentIndex++;
        System.out.println("History size = " + myHistory.size());
    }

    /**
     * Returns the URL currently being shown
     */
    public URL getCurrentURL () {
        return myHistory.get(myCurrentIndex);
    }

    /**
     * Returns the first page in next history, null if next history is empty.
     */
    public URL next () {
        if (hasNext()) {
            myCurrentIndex++;
            return myHistory.get(myCurrentIndex);
        }
        return null;
    }

    /**
     * Returns the first page in back history, null if back history is empty.
     */
    public URL back () {
        if (hasPrevious()) {
            myCurrentIndex--;
            return myHistory.get(myCurrentIndex);
        }
        return null;
    }

    /**
     * Returns true if there is a next URL available
     */
    public boolean hasNext () {
        return myCurrentIndex < (myHistory.size() - 1);
    }

    /**
     * Returns true if there is a previous URL available
     */
    public boolean hasPrevious () {
        return myCurrentIndex > 0;
    }
}
