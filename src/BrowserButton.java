import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * Implemented so that buttons can be systematically checked to see if they are enabled
 * Allows one to only have to add one method to BrowserView to create new browser functionality
 * Plus one call to create/add the button to the appropriate panel
 * Not necessary to implement a new class unless the button will sometimes be disabled
 * 
 * Obvious tradeoffs are that we have a bunch of short classes now, and pass the model around in a protected variable.
 * All this code already had access to the Model before, though.
 * @author Teddy Ward
 */
public class BrowserButton extends Button {	
	protected BrowserModel ourModel;

    // makes a button using either an image or a label
	public BrowserButton (String label, EventHandler<ActionEvent> handler) {
	    if (label.endsWith("gif")) {
	        setGraphic(new ImageView(
	            new Image(getClass().getResourceAsStream(DEFAULT_RESOURCE_PACKAGE + label))));
	    } else {
	        setText(label);
	    }
	    setOnAction(handler);
	}

	public boolean checkDisabled() {
		return false;
	}
}
