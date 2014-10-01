import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.controlsfx.dialog.Dialogs;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 * A class used to display the viewer for a simple HTML browser.
 * 
 * See this tutorial for help on how to use the variety of components:
 * http://download.oracle.com/otndocs/products/javafx/2/samples/Ensemble/
 * 
 * @author Owen Astrachan
 * @author Marcin Dobosz
 * @author Yuzhang Han
 * @author Edwin Ward
 * @author Robert C. Duvall
 */
public class BrowserView {
    // constants
    public static final Dimension DEFAULT_SIZE = new Dimension(800, 600);
    public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
    public static final String PROTOCOL_PREFIX = "http://";
    public static final String BLANK = " ";

    // scene, needed to report back to Application
    private Scene myScene;
    // web page
    private WebView myPage;
    // information area
    private Label myStatus;
    // navigation
    private TextField myURLDisplay;
    private Button myBackButton;
    private Button myNextButton;
    private Button myHomeButton;
    // favorites
    private ComboBox<String> myFavorites;
    // get strings from resource file
    private ResourceBundle myResources;
    // the data
    private BrowserModel myModel;

    /**
     * Create a view of the given model of a web browser.
     */
    public BrowserView (BrowserModel model, String language) {
        myModel = model;
        // use resources for labels
        myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
        BorderPane root = new BorderPane();
        // must be first since other panels may refer to page
        root.setCenter(makePageDisplay());
        root.setTop(makeInputPanel());
        root.setBottom(makeInformationPanel());
        // control the navigation
        enableButtons();
        // create scene to hold UI
        myScene = new Scene(root, DEFAULT_SIZE.width, DEFAULT_SIZE.height);
    }

    /**
     * Display given URL.
     */
    public void showPage (String url) {
        try {
            if (url != null) {
                // check for a valid URL before updating model, view
                URL valid = new URL(completeURL(url));
                myModel.go(valid);
                update(valid);
            }
        } catch (MalformedURLException e) {
            showError("Could not load " + url);
        }
    }

    /**
     * Returns scene for this view so it can be added to stage.
     */
    public Scene getScene () {
        return myScene;
    }

    /**
     * Display given message as information in the GUI.
     */
    public void showStatus (String message) {
        myStatus.setText(message);
    }

    /**
     * Display given message as an error in the GUI.
     */
    public void showError (String message) {
        Dialogs.create().title(myResources.getString("ErrorTitle")).message(message).showError();
    }

    // move to the next URL in the history
    private void next () {
        update(myModel.next());
    }

    // move to the previous URL in the history
    private void back () {
        update(myModel.back());
    }

    // change current URL to the home page, if set
    private void home () {
        showPage(myModel.getHome().toString());
    }

    // change page to favorite choice
    private void showFavorite (String favorite) {
        showPage(myModel.getFavorite(favorite).toString());
    }

    // update just the view to display given URL
    private void update (URL url) {
        myPage.getEngine().load(url.toString());
        myURLDisplay.setText(url.toString());
        enableButtons();
    }

    // prompt user for name of favorite to add to collection
    private void addFavorite () {
        Optional<String> response = Dialogs.create()
                .title(myResources.getString("FavoritePromptTitle"))
                .message(myResources.getString("FavoritePrompt")).showTextInput("");
        // did user make a choice?
        if (response.isPresent()) {
            myModel.addFavorite(response.get());
            myFavorites.getItems().add(response.get());
        }
    }

    // deal with a potentially incomplete URL,
    // e.g., let user leave off initial protocol
    private String completeURL (String url) {
        if (!url.startsWith(PROTOCOL_PREFIX)) {
            return PROTOCOL_PREFIX + url;
        }
        return url;
    }

    // only enable buttons when useful to user
    private void enableButtons () {
        myBackButton.setDisable(!myModel.hasPrevious());
        myNextButton.setDisable(!myModel.hasNext());
        myHomeButton.setDisable(myModel.getHome() == null);
    }

    // convenience method to create HTML page display
    private Node makePageDisplay () {
        myPage = new WebView();
        // catch "browsing" events within web page
        myPage.getEngine().getLoadWorker().stateProperty().addListener(new LinkListener());
        return myPage;
    }

    // organize user's options for controlling/giving input to model
    private Node makeInputPanel () {
        VBox result = new VBox();
        result.getChildren().addAll(makeNavigationPanel(), makePreferencesPanel());
        return result;
    }

    // make the panel where "would-be" clicked URL is displayed
    private Node makeInformationPanel () {
        // BLANK must be non-empty or status label will not be displayed in GUI
        myStatus = new Label(BLANK);
        return myStatus;
    }

    // make user-entered URL/text field and back/next buttons
    private Node makeNavigationPanel () {
        HBox result = new HBox();

        myBackButton = makeButton("BackCommand", event -> back());
        result.getChildren().add(myBackButton);

        myNextButton = makeButton("NextCommand", event -> next());
        result.getChildren().add(myNextButton);

        myHomeButton = makeButton("HomeCommand", event -> home());
        result.getChildren().add(myHomeButton);

        // if user presses button or enter in text field, load/show the URL
        EventHandler<ActionEvent> showPage = event -> showPage(myURLDisplay.getText());
        result.getChildren().add(makeButton("GoCommand", showPage));
        myURLDisplay = makeInputField(50, showPage);
        result.getChildren().add(myURLDisplay);

        return result;
    }

    // make buttons for setting favorites/home URLs
    private Node makePreferencesPanel () {
        HBox result = new HBox();

        result.getChildren().add(makeButton("AddFavoriteCommand", event -> addFavorite()));

        myFavorites = new ComboBox<String>();
        myFavorites.setPromptText(myResources.getString("FavoriteFirstItem"));
        myFavorites.valueProperty().addListener( (o, s1, s2) -> showFavorite(s2));
        result.getChildren().add(myFavorites);

        result.getChildren().add(makeButton("SetHomeCommand", event -> {
            myModel.setHome();
            enableButtons();
        }));

        return result;
    }

    // makes a button using either an image or a label
    private Button makeButton (String property, EventHandler<ActionEvent> handler) {
        Button result = new Button();
        String label = myResources.getString(property);
        if (label.endsWith("gif")) {
            result.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream(DEFAULT_RESOURCE_PACKAGE + label))));
        } else {
            result.setText(label);
        }
        result.setOnAction(handler);
        return result;
    }

    // make text field for input
    private TextField makeInputField (int width, EventHandler<ActionEvent> handler) {
        TextField result = new TextField();
        result.setPrefColumnCount(width);
        result.setOnAction(handler);
        return result;
    }

    /**
     * Inner class to deal with link-clicks and mouse-overs Mostly taken from
     * http://blogs.kiyut.com/tonny/2013/07/30/javafx-webview-addhyperlinklistener/
     */
    private class LinkListener implements ChangeListener<State> {
        public static final String EVENT_CLICK = "click";
        public static final String EVENT_MOUSEOVER = "mouseover";
        public static final String EVENT_MOUSEOUT = "mouseout";

        @Override
        public void changed (ObservableValue<? extends State> ov, State oldState, State newState) {
            if (newState == Worker.State.SUCCEEDED) {
                EventListener listener = event -> {
                    final String href = ((Element)event.getTarget()).getAttribute("href");
                    if (href != null) {
                        String domEventType = event.getType();
                        if (domEventType.equals(EVENT_CLICK)) {
                            showPage(href);
                        } else if (domEventType.equals(EVENT_MOUSEOVER)) {
                            showStatus(href);
                        } else if (domEventType.equals(EVENT_MOUSEOUT)) {
                            showStatus(BLANK);
                        }
                    }
                };
                Document doc = myPage.getEngine().getDocument();
                NodeList nodes = doc.getElementsByTagName("a");
                for (int i = 0; i < nodes.getLength(); i++) {
                    EventTarget node = (EventTarget) nodes.item(i);
                    node.addEventListener(EVENT_CLICK, listener, false);
                    node.addEventListener(EVENT_MOUSEOVER, listener, false);
                    node.addEventListener(EVENT_MOUSEOUT, listener, false);
                }
            }
        }
    };
}
