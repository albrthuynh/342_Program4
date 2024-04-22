
import java.util.*;

import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;


import static javafx.scene.paint.Color.rgb;

public class GuiClient extends Application{

	
	TextField c1;
	Button b1;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	Client clientConnection;
	TextField createUser = new TextField();
	
	ListView<String> listItems2;

	public ArrayList<int[]> clickedButtons = new ArrayList<>(); // List to track clicked buttons
	private int currentShipLength; // Length of the current ship being placed
	public ArrayList<Integer> availableShips = new ArrayList<>(); // List of available ship lengths



	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		clientConnection = new Client(data->{
				Platform.runLater(()->{listItems2.getItems().add(data.toString());
			});
		});
							
		clientConnection.start();

		listItems2 = new ListView<String>();
		
		c1 = new TextField();
		b1 = new Button("Send");
		b1.setOnAction(e->{clientConnection.send(c1.getText()); c1.clear();});
		
		sceneMap = new HashMap<String, Scene>();

//		sceneMap.put("client", welcomeScreen(primaryStage));
		welcomeScreen(primaryStage);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });


//		primaryStage.setScene(sceneMap.get("client"));
//		primaryStage.setTitle("Client");
		primaryStage.show();
		
	}

	public void welcomeScreen(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Welcome to YapaVerse!!!!"); //title for screen

		//create textMe image
		InputStream stream = new FileInputStream("src/Images/battleship.gif");
		Image image = new Image(stream);
		ImageView imageView = new ImageView();

		//sets the width and height of the image
		imageView.setImage(image);
		imageView.setFitWidth(325);
		imageView.setFitHeight(350);
		imageView.setPreserveRatio(false);

		//header text at top of screen
		Text header = new Text();
		header.setText("WELCOME TO BATTLESHIP");
		header.setFill(rgb(255,255,255)); //color of text
		header.setFont(Font.font("Rockwell", FontWeight.BOLD, 54));

		//creates shadow for text
		DropShadow dropShadow1 = new DropShadow();
		dropShadow1.setOffsetY(3.0f);
		dropShadow1.setColor(Color.color(0.01f, 0.01f, 0.01f));
		header.setEffect(dropShadow1);

		//vbox containing both headers
		VBox headerBox = new VBox(-10, header);
		//headerBox.setTranslateY(-10);
		headerBox.setTranslateX(-15);
		headerBox.setAlignment(Pos.CENTER);

		//textfield above join button to enter username
		createUser.setAlignment(Pos.CENTER);
		createUser.setMaxWidth(250);

		//text next to textfield
		Text createMessage = new Text("Create Username:");
		createMessage.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

		//vbox that contains textfield with text
		VBox userBox = new VBox(20,createMessage, createUser);
		userBox.setTranslateX(-18);
		userBox.setAlignment(Pos.CENTER);
		userBox.setTranslateY(140);

		//creating the join button that allows a user to join the server
		Button playWithAI = new Button("Play with AI");
		playWithAI.setStyle("-fx-background-radius: 40;");
		playWithAI.setPrefWidth(300);
		playWithAI.setPrefHeight(60);

		Button playWithUser = new Button("Play with User");
		playWithUser.setStyle("-fx-background-radius: 40;");
		playWithUser.setPrefWidth(300);
		playWithUser.setPrefHeight(60);

		//setting font for text on button
		playWithUser.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
		playWithUser.setTranslateY(160);
		playWithUser.setTranslateX(32);

		//setting font for text on button
		playWithAI.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
		playWithAI.setTranslateY(160);
		playWithAI.setTranslateX(32);

		//when the joinButton is clicked
		playWithUser.setOnAction(e->{
			String tempUserId = createUser.getText(); //grab text from textfield
            try {
                gameSetUpScreen(primaryStage);
            } catch (Exception ex) {
				ex.printStackTrace();
                throw new RuntimeException(ex);
            }
		});

		HBox buttonBox = new HBox(20, playWithUser, playWithAI);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setTranslateX(-60);


		//vbox that contains vbox with textfield/text and joinbutton
		VBox userNameBox = new VBox(10, userBox, buttonBox);
		userNameBox.setTranslateY(-240);

		VBox imageBox = new VBox(10, imageView);
		imageBox.setAlignment(Pos.CENTER);
		imageBox.setTranslateY(-60);

		imageBox.setTranslateX(-18);

		userNameBox.setAlignment(Pos.CENTER);

		//vbox that contains all objects on the screen
		VBox all = new VBox(80, headerBox, imageBox, userNameBox);

		//creating BorderPane to add everything
		BorderPane pane = new BorderPane();
		pane.setCenter(all);
		pane.setPadding(new Insets(30)); //makes sure stuff does not touch the border

		//adding a border color
		BorderWidths newBorderWidth = new BorderWidths(10);
		BorderStroke newBorderStroke = new BorderStroke(Color.LIGHTBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, newBorderWidth);
		Border newBorder = new Border(newBorderStroke);
		pane.setBorder(newBorder);

		//setting background color of screen
		pane.setStyle("-fx-background-color: rgb(98,170,237);");

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		//creating scene
		Scene mainScene = new Scene(pane, 500, 500);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	// game setup screen
	public void gameSetUpScreen(Stage primaryStage) throws Exception {
		Button done, placeShip;
		//create textMe image
		InputStream stream1 = new FileInputStream("src/Images/2ship.png");
		InputStream stream2 = new FileInputStream("src/Images/3shipPart1.png");
		InputStream stream3 = new FileInputStream("src/Images/3shipPart2.png");
		InputStream stream4 = new FileInputStream("src/Images/4ship.png");
		InputStream stream5 = new FileInputStream("src/Images/5ship.png");

		Image twoShip = new Image(stream1);
		Image threeShip = new Image(stream2);
		Image threeShip2 = new Image(stream3);
		Image fourShip = new Image(stream4);
		Image fiveShip = new Image(stream5);

		ImageView imageViewTwoShip = new ImageView();
		ImageView imageViewThreeShip = new ImageView();
		ImageView imageViewThreeShip2 = new ImageView();
		ImageView imageViewFourShip = new ImageView();
		ImageView imageViewFiveShip = new ImageView();

		//sets the width and height of the image
		imageViewTwoShip.setImage(twoShip);
		imageViewTwoShip.setFitWidth(250);
		imageViewTwoShip.setFitHeight(125);
		imageViewTwoShip.setPreserveRatio(false);

		imageViewThreeShip.setImage(threeShip);
		imageViewThreeShip.setFitWidth(250);
		imageViewThreeShip.setFitHeight(125);
		imageViewThreeShip.setPreserveRatio(false);

		imageViewThreeShip2.setImage(threeShip2);
		imageViewThreeShip2.setFitWidth(250);
		imageViewThreeShip2.setFitHeight(125);
		imageViewThreeShip2.setPreserveRatio(false);

		imageViewFourShip.setImage(fourShip);
		imageViewFourShip.setFitWidth(250);
		imageViewFourShip.setFitHeight(125);
		imageViewFourShip.setPreserveRatio(false);

		imageViewFiveShip.setImage(fiveShip);
		imageViewFiveShip.setFitWidth(250);
		imageViewFiveShip.setFitHeight(125);
		imageViewFiveShip.setPreserveRatio(false);

		imageViewTwoShip.setOnMouseClicked(e -> {

		});

		imageViewThreeShip.setOnMouseClicked(e -> {

		});

		imageViewThreeShip2.setOnMouseClicked(e -> {

		});

		imageViewFourShip.setOnMouseClicked(e -> {

		});

		imageViewFiveShip.setOnMouseClicked(e -> {

		});

		availableShips.add(5);
		availableShips.add(4);
		availableShips.add(3);
		availableShips.add(3);
		availableShips.add(2);

		Text selectShip = new Text("SELECT SHIP TO PLACE");
		VBox allShips = new VBox(10, selectShip,imageViewTwoShip, imageViewThreeShip, imageViewThreeShip2, imageViewFourShip, imageViewFiveShip);
		allShips.setTranslateX(80);
		done = new Button("DONE");
		placeShip = new Button("PLACE SHIP");

		done.setStyle("-fx-background-radius: 40;");
		done.setPrefWidth(220);
		done.setPrefHeight(60);

		placeShip.setStyle("-fx-background-radius: 40;");
		placeShip.setPrefWidth(220);
		placeShip.setPrefHeight(60);

		// Create a GridPane layout
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setTranslateY(-30);
		gridPane.setTranslateX(180);

		gridPane.setHgap(5);
		gridPane.setVgap(5);

		VBox buttonsBox = new VBox(20, done,placeShip);
		buttonsBox.setTranslateY(320);
		buttonsBox.setTranslateX(250);

		HBox all = new HBox(10, allShips, gridPane, buttonsBox);

		// Define the number of rows and columns (e.g., 10x10 for a standard Battleship board)
		int rows = 10;
		String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		int cols = 10;

		// Create a 10x10 grid of buttons
		for (int row = 0; row < letters.length; row++) {
			for (int col = 0; col < cols; col++) {
				// Create a new button
				Button button = new Button();

				// Set button text or other properties as needed
				button.setText(letters[row] + (col + 1)); // Set initial button text (e.g., "")

				// Set button size (optional, adjust as needed)
				button.setMinSize(60, 60);

				// Add the button to the grid
				gridPane.add(button, col, row);

				// Set an action for button click
				final int r = row;
				final int c = col;
				button.setOnAction(event -> {
					// Add the clicked button to the list
//					clickedButtons.add(new int[] { r, c });
//					checkValidShipPlacement(button, gridPane);


					// Handle button click event
					//button.setText("Ship Part"); // Mark the button as part of a ship


					// Check if the clicked buttons form a valid ship placement

				});
			}
		}


		// Create a scene with the gridPane
		BorderPane pane = new BorderPane();
		pane.setCenter(all);
		pane.setStyle("-fx-background-color: rgb(98,170,237);");

		done.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
                try {
                    gameScreen(primaryStage);
                } catch (Exception e) {
					e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
		});

		Scene gameSetUpScreen = new Scene(pane, 1500, 800);
		primaryStage.setMaximized(true);
		primaryStage.setScene(gameSetUpScreen);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	// waiting for player to connect screen
	public void waitingScreen (Stage primaryStage) throws Exception{
		Text title = new Text("Waiting for player to connect...");

		BorderPane pane = new BorderPane();
		pane.setCenter(title);

		Scene mainScene = new Scene(pane, 500, 500);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		primaryStage.show();
		//return new Scene(pane);
	}

	// game screen
	public void gameScreen (Stage primaryStage) throws Exception {
		Button attack, chooseSquare;
		attack = new Button("Attack");
		chooseSquare = new Button("Choose Square To Attack");
		int rows = 10;

		String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		int cols = 10;
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);

		// Create a 10x10 grid of buttons
		for (int row = 0; row < letters.length; row++) {
			for (int col = 0; col < cols; col++) {
				// Create a new button
				Button button = new Button();

				// Set button text or other properties as needed
				button.setText(letters[row] + (col + 1)); // Set initial button text (e.g., "")

				// Set button size (optional, adjust as needed)
				button.setMinSize(40, 40);

				// Add the button to the grid
				gridPane.add(button, col, row);

				// Set an action for button click
				final int r = row;
				final int c = col;
				button.setOnAction(event -> {
					// Add the clicked button to the list
//					clickedButtons.add(new int[] { r, c });
//					checkValidShipPlacement(button, gridPane);


					// Handle button click event
					//button.setText("Ship Part"); // Mark the button as part of a ship


					// Check if the clicked buttons form a valid ship placement

				});
			}
		}

		VBox gridVbox = new VBox(10, gridPane, chooseSquare);
		HBox all = new HBox(10, gridVbox, attack);

		BorderPane pane = new BorderPane();
		pane.setCenter(all);

		pane.setStyle("-fx-background-color: rgb(98,170,237);");


		// NOT CORRECT IMPLEMENTATION JUST SEETING WHAT IT LOOKS LIKE
		attack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				try{
					primaryStage.setMaximized(true);
					userLostScreen(primaryStage);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		//return new Scene(pane);
		Scene gameScene = new Scene(pane, 1500, 800);
		primaryStage.setScene(gameScene);
//		primaryStage.setMaximized(true);
//		primaryStage.setResizable(false);
		primaryStage.show();
	}


	// you lost screen
	public void userLostScreen (Stage primaryStage) {
		Button playAgain, goHome;
		Text userLostText;
		VBox all;

		userLostText = new Text("YOU LOST, battleship!");
		playAgain = new Button("Play Again");
		goHome = new Button("Go Home");
		all = new VBox(userLostText, playAgain, goHome);

		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-color: rgb(98,170,237);");
		pane.setCenter(all);


		Scene mainScene = new Scene(pane, 500, 500);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		primaryStage.show();

		goHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				try{
					primaryStage.setMaximized(true);
					welcomeScreen(primaryStage);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		// AGAIN THIS THING SHOULD NOT WORK!! JUST A PLACE HOLDER
		playAgain.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				try{
					primaryStage.setMaximized(true);
					userWonScreen(primaryStage);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		//return new Scene(pane);
	}

	// you won screen
	public void userWonScreen(Stage primaryStage) {
		Button playAgain, goHome;
		Text userWonText;
		VBox userChoiceButtons;

		playAgain = new Button("Play Again");
		goHome = new Button("Go Home");
		userWonText = new Text("YOU WON, battleship!");
		userChoiceButtons = new VBox(userWonText, playAgain, goHome);

		BorderPane pane = new BorderPane();
		pane.setCenter(userChoiceButtons);

		pane.setStyle("-fx-background-color: rgb(98,170,237);");
		Scene mainScene = new Scene(pane, 500, 500);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		primaryStage.show();


		//return new Scene(pane);
	}


//	public void sample(Stage primaryStage) {
//		availableShips.add(5);
//		availableShips.add(4);
//		availableShips.add(3);
//		availableShips.add(3);
//		availableShips.add(2);
//		// Create a GridPane layout
//		GridPane gridPane = new GridPane();
//
//		// Define the number of rows and columns (e.g., 10x10 for a standard Battleship board)
//		int rows = 10;
//		int cols = 10;
//
//		// Create a 10x10 grid of buttons
//		for (int row = 0; row < rows; row++) {
//			for (int col = 0; col < cols; col++) {
//				// Create a new button
//				Button button = new Button();
//
//				// Set button text or other properties as needed
//				button.setText(""); // Set initial button text (e.g., "")
//
//				// Set button size (optional, adjust as needed)
//				button.setMinSize(40, 40);
//
//				// Add the button to the grid
//				gridPane.add(button, col, row);
//
//				// Set an action for button click
//				final int r = row;
//				final int c = col;
//				button.setOnAction(event -> {
//					// Handle button click event
//					button.setText("Ship Part"); // Mark the button as part of a ship
//
//					// Add the clicked button to the list
//					clickedButtons.add(new int[] { r, c });
//
//					// Check if the clicked buttons form a valid ship placement
//					checkValidShipPlacement(button, gridPane);
//				});
//			}
//		}
//
//		// Create a scene with the gridPane
//		Scene scene = new Scene(gridPane, 400, 400);
//
//		// Set the scene on the stage
//		primaryStage.setScene(scene);
//
//		// Set stage title and show the stage
//		primaryStage.setTitle("Battleship Game");
//		primaryStage.show();
//	}

	private void checkValidShipPlacement(Button button, GridPane gridPane) {
		// Check if the list contains the current ship length
		if (clickedButtons.size() == currentShipLength) {
			// Check if the clicked buttons form a valid ship placement
			boolean isValid = checkShipPlacement();

			if (isValid) {
				// Valid ship placement
				System.out.println("Valid ship placement!");
				// Remove the placed ship from the available ships
				availableShips.remove(Integer.valueOf(currentShipLength));
				// Reset the clicked buttons list
				clickedButtons.clear();
				// Reset button texts (you may want to adjust this for your game)
				resetButtonTexts(gridPane);
			} else {
				// Invalid ship placement
				System.out.println("Invalid ship placement. Please try again.");
				// Reset the clicked buttons list
				clickedButtons.clear();
				// Reset button texts (you may want to adjust this for your game)
				resetButtonTexts(gridPane);
			}

			// Check if there are more available ships
			if (!availableShips.isEmpty()) {
				// Set the current ship length to the next available ship length
				currentShipLength = availableShips.get(0);
			} else {
				// No more ships available
				System.out.println("All ships placed!");
			}
		}
	}

	private boolean checkShipPlacement() {
		// Check if the clicked buttons form a straight line
		// Sort the list for easier checking
		clickedButtons.sort((a, b) -> {
			if (a[0] != b[0]) {
				return Integer.compare(a[0], b[0]);
			}
			return Integer.compare(a[1], b[1]);
		});

		// Check if the buttons form a valid horizontal ship placement
		boolean isHorizontal = true;
		for (int i = 1; i < currentShipLength; i++) {
			if (clickedButtons.get(i)[0] != clickedButtons.get(i - 1)[0] ||
					clickedButtons.get(i)[1] != clickedButtons.get(i - 1)[1] + 1) {
				System.out.println("Ships are NOT horizontal!\n");
				isHorizontal = false;
				break;
			}
		}
		if (isHorizontal) {
			System.out.println("Ships ARE horizontal!\n");
			return true;
		}

		// Check if the buttons form a valid vertical ship placement
		boolean isVertical = true;
		for (int i = 1; i < currentShipLength; i++) {
			if (clickedButtons.get(i)[0] != clickedButtons.get(i - 1)[0] + 1 ||
					clickedButtons.get(i)[1] != clickedButtons.get(i - 1)[1]) {
				isVertical = false;
				System.out.println("Ships are NOT vertical!\n");
				break;
			}
		}

		System.out.println("Ships ARE vertical!\n");
		return isVertical;
	}

	private void resetButtonTexts(GridPane gridPane) {
		// Reset the texts of the buttons in the gridPane
		gridPane.getChildren().forEach(node -> {
			if (node instanceof Button) {
				((Button) node).setText("");
			}
		});
	}
}



