package mines;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/* A controller for the FXML, has 2 text areas and a button. */
public class MineSweeperController 
{

    @FXML
    private TextArea heightText;

    @FXML
    private TextArea minesText;

    @FXML
    private Button resButton;

    @FXML
    private TextArea widthText;
    
    //Returns the reset button.
    public Button resetButton()
    {
    	return resButton;
    }
    
    //Returns the width text area.
    public TextArea getWidth()
    {
    	return widthText;
    }
    
    //Returns the mines text area.
    public TextArea getMines()
    {
    	return minesText;
    }
    
    //Returns the height text area.
    public TextArea getheight()
    {
    	return heightText;
    }

}
