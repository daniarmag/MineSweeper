package mines;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*A class that implements the entire MineSweeper game. */
public class MineSweeperMain extends Application 
{
	private int height = 10, width = 10, mines = 10, tempMines;
	private MineSweeperController controller;
	//Object of type Mines that handles the logic and data of the MineSweeper game.
	private Mines m;
	//GridPane that holds the game's buttons.
	private GridPane grid = new GridPane();
	//Images used for the game's buttons.
	private Image buttonP = new Image(getClass().getResourceAsStream("/mines/button.png"), 20, 20, false, false);
	private Image mineP = new Image(getClass().getResourceAsStream("/mines/bomb.png"), 20,20, false, false);
	private Image flagP = new Image(getClass().getResourceAsStream("/mines/flag.png"), 20, 20, false, false);
	private BackgroundSize backgroundSize = new BackgroundSize(1000,1000,true,true,true,true);
	private BackgroundImage picture = new BackgroundImage(new Image(new File("/mines/background.png").toString()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);

	/*A main method that runs the game. */
	public static void main(String[] args) 
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) 
	{
		HBox root = new HBox();
		//Loader loads the FXML file which holds the game's UI.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("MineSweeperGame.fxml"));
		try 
		{
			root = loader.load();
			root.setBackground(new Background(picture));
			//Gets the controller for the FXML file.
			controller = loader.getController();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		//Sets the default values for the game's size and number of mines.
		controller.getheight().setText(String.valueOf(height));
		controller.getWidth().setText(String.valueOf(width));
		controller.getMines().setText((String.valueOf(mines)));
		//Initializes the Mines object.
		m = new Mines(height, width, mines);
		createResetButton();
		createGrid();
		root.getChildren().add(grid);
		Scene s = new Scene(root);
		stage.setScene(s);
		stage.setTitle("MinesSweeper");
		stage.sizeToScene();
		stage.show();
	}
	
	/*Creates the grid and its buttons. */
	private void createGrid()
	{
		int i, j;
		//Clear the grid.
		grid.getChildren().clear();
		grid.setAlignment(Pos.CENTER);
		for (i = 0; i < height; i++) 
		{
			for (j = 0; j < width; j++) 
			{
				Button b = new Button();
				//Set button size.
			    b.setPrefSize(40, 40);
				setPictures(b, i, j);
				//Set button click event with the help of the object buttonPressed.
				b.setOnMouseClicked(new buttonPressed(i, j));
				grid.add(b, j, i);
			}
		}	
	}
	
	/*An inner class that handles the button press event. */
	class buttonPressed implements EventHandler<MouseEvent> 
	{
		private int x;
		private int y;

		//A constructor.
		private buttonPressed(int x, int y) 
		{
			this.x = x;
			this.y = y;
		}

		@Override
		public void handle(MouseEvent event) 
		{
			//Left-click.
			if (event.getButton() == MouseButton.PRIMARY) 
			{
				//User lost.
				if (!m.open(x, y)) 
				{
					m.setShowAll(true); 
					JOptionPane.showMessageDialog(null, "Game over! You lose!", "Game Over", JOptionPane.ERROR_MESSAGE);

				}
				//User won.
				else if (m.isDone()) 
				{
					m.setShowAll(true); 
					JOptionPane.showMessageDialog(null, "Congratulations! You win!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

				}
			} 
			//Right-click.
			else if (event.getButton() == MouseButton.SECONDARY) 
				m.toggleFlag(x, y); 
			createGrid();
		}
	}	
	
	/*Sets the picture for all the possible grid buttons. */
	private void setPictures(Button b, int x, int y) 
	{
		String check;
        check = m.get(x, y);
        //Specifically for closed buttons.
        if(check == ".")
        	b.setGraphic(new ImageView(buttonP));
        //Specifically for mine buttons.
        else if(check == "X")
        	b.setGraphic(new ImageView(mineP));
        //Specifically for flagged buttons.
        else if(check == "F")	
        	b.setGraphic(new ImageView(flagP));
        else
        {
        	//Displayed the number of the mines around this position.
        	b.setText(m.get(x, y));
        }
	}
	
	/*Creates and sets the action for the reset button. */
	public void createResetButton() 
	{
		//Get the reset button from the controller.
		Button b = controller.resetButton();
		b.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event) 
			{
				resetText();
				//Re-initialize the Mines object.
				m = new Mines(height, width, mines);
				createGrid();
			}
		});
	}
	 
	/* Reads values from TextFields. */
	public void resetText() {
	    try {
	        String heightText = controller.getheight().getText().trim();
	        String widthText = controller.getWidth().getText().trim();
	        String minesText = controller.getMines().getText().trim();

	        height = Math.min(Integer.parseInt(heightText), 20);
	        width = Math.min(Integer.parseInt(widthText), 20);
	        tempMines = Integer.parseInt(minesText);
	        //Find the minimum number of mines between the user input mines and total buttons and of the board.
	        mines = Math.min(height * width, tempMines);
	    } catch (NumberFormatException e)
	    {
	        //Input contains non-integer characters
	        //Display an error message to the user
	        JOptionPane.showMessageDialog(null, "Please enter only integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
}	