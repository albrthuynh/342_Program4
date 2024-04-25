
import java.lang.reflect.Array;
import java.util.*;

import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;


import static javafx.scene.paint.Color.rgb;


public class GuiClient extends Application{
	TextField c1;
	Button b1;
	Boolean isHorizontal = false, isVertical = false;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	Client clientConnection;
	TextField createUser = new TextField();
	
	ListView<String> listItems2;
	ArrayList<Button> setShipButtons;

	ArrayList<Button> computerCoords = new ArrayList<>();
	ArrayList<Button> playerCoords = new ArrayList<>();

	GridPane computerCoordsGrid = new GridPane();
	GridPane playerCoordsGrid = new GridPane();

	BattleShipGame game = new BattleShipGame();
	Text popUpMessage = new Text("");

	Integer shipPlacedCount = 0;

	Button done = new Button();

	Button vertical = new Button("PLACE VERTICAL");
	Button horizontal = new Button("PLACE HORIZONTAL");

	public ArrayList<int[]> clickedButtons = new ArrayList<>(); // List to track clicked buttons
	private int currentShipLength; // Length of the current ship being placed
	public ArrayList<Integer> availableShips = new ArrayList<>(); // List of available ship lengths
	Button twoShipButton, threeShipButton, threeShip2Button, fourShipButton, fiveShipButton;

	public ArrayList<String> playerHits = new ArrayList<>(); //keeping track of all the buttons the player clicked when attacking the computer
	public ArrayList<String> computerHits = new ArrayList<>(); //keeping track of all the buttons the computer clicked when attacking the player


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
		//gameScreen (primaryStage);
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
		primaryStage.setTitle("Welcome to Battleship!!!!"); //title for screen

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
				setUpScreen(primaryStage);
            } catch (Exception ex) {
				ex.printStackTrace();
                throw new RuntimeException(ex);
            }
		});

		playWithAI.setOnAction(e->{
			game.versusComputer = true;
			try {
				setUpScreen(primaryStage);
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
		Scene mainScene = new Scene(pane, 1450, 800);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	// game setup screen
	public void setUpScreen(Stage primaryStage) throws Exception {

		twoShipButton = new Button("Select");

		twoShipButton.setStyle("-fx-background-radius: 40;");
		twoShipButton.setPrefWidth(120);
		twoShipButton.setPrefHeight(50);

		threeShipButton = new Button("Select");
		threeShipButton.setStyle("-fx-background-radius: 40;");
		threeShipButton.setPrefWidth(120);
		threeShipButton.setPrefHeight(50);

		threeShip2Button = new Button("Select");
		threeShip2Button.setStyle("-fx-background-radius: 40;");
		threeShip2Button.setPrefWidth(120);
		threeShip2Button.setPrefHeight(50);

		fourShipButton = new Button("Select");
		fourShipButton.setStyle("-fx-background-radius: 40;");
		fourShipButton.setPrefWidth(120);
		fourShipButton.setPrefHeight(50);

		fiveShipButton = new Button("Select");
		fiveShipButton.setStyle("-fx-background-radius: 40;");
		fiveShipButton.setPrefWidth(120);
		fiveShipButton.setPrefHeight(50);
		int setUpShips = 0;

		//create textMe image
		InputStream stream1 = new FileInputStream("src/Images/2ship.png");
		InputStream stream2 = new FileInputStream("src/Images/3shipPart1.png");
		InputStream stream3 = new FileInputStream("src/Images/3shipPart2.png");
		InputStream stream4 = new FileInputStream("src/Images/4ship.png");
		InputStream stream5 = new FileInputStream("src/Images/5ship.png");

		Image twoShipImage = new Image(stream1);
		Image threeShipImage = new Image(stream2);
		Image threeShip2Image = new Image(stream3);
		Image fourShipImage = new Image(stream4);
		Image fiveShipImage = new Image(stream5);

		ImageView imageViewTwoShip = new ImageView();
		ImageView imageViewThreeShip = new ImageView();
		ImageView imageViewThreeShip2 = new ImageView();
		ImageView imageViewFourShip = new ImageView();
		ImageView imageViewFiveShip = new ImageView();

		//sets the width and height of the image
		imageViewTwoShip.setImage(twoShipImage);
		imageViewTwoShip.setFitWidth(250);
		imageViewTwoShip.setFitHeight(125);
		imageViewTwoShip.setPreserveRatio(false);

		imageViewThreeShip.setImage(threeShipImage);
		imageViewThreeShip.setFitWidth(250);
		imageViewThreeShip.setFitHeight(125);
		imageViewThreeShip.setPreserveRatio(false);

		imageViewThreeShip2.setImage(threeShip2Image);
		imageViewThreeShip2.setFitWidth(250);
		imageViewThreeShip2.setFitHeight(125);
		imageViewThreeShip2.setPreserveRatio(false);

		imageViewFourShip.setImage(fourShipImage);
		imageViewFourShip.setFitWidth(250);
		imageViewFourShip.setFitHeight(125);
		imageViewFourShip.setPreserveRatio(false);

		imageViewFiveShip.setImage(fiveShipImage);
		imageViewFiveShip.setFitWidth(250);
		imageViewFiveShip.setFitHeight(125);
		imageViewFiveShip.setPreserveRatio(false);

		availableShips.add(5);
		availableShips.add(4);
		availableShips.add(3);
		availableShips.add(3);
		availableShips.add(2);

		Text shipOrientation = new Text("SELECT SHIP ORIENTATION");
		shipOrientation.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
		shipOrientation.setTranslateX(-30);
		shipOrientation.setUnderline(true);


		horizontal.setDisable(true);
		horizontal.setStyle("-fx-background-radius: 40;");
		horizontal.setPrefWidth(220);
		horizontal.setPrefHeight(60);


		vertical.setDisable(true);
		vertical.setStyle("-fx-background-radius: 40;");
		vertical.setPrefWidth(220);
		vertical.setPrefHeight(60);

		Text selectShip = new Text("SELECT SHIP TO PLACE");
		selectShip.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
		selectShip.setTranslateX(80);
		selectShip.setUnderline(true);

		twoShipButton.setTranslateX(25);
		twoShipButton.setTranslateY(67);

		threeShipButton.setTranslateX(25);
		threeShipButton.setTranslateY(55);

		threeShip2Button.setTranslateX(25);
		threeShip2Button.setTranslateY(65);

		fourShipButton.setTranslateX(25);
		fourShipButton.setTranslateY(65);

		fiveShipButton.setTranslateX(25);
		fiveShipButton.setTranslateY(65);

		HBox twoShipBox = new HBox(35,twoShipButton,imageViewTwoShip);
		HBox threeShipBox = new HBox(35,threeShipButton, imageViewThreeShip);
		HBox threeShip2Box = new HBox(35,threeShip2Button, imageViewThreeShip2);
		HBox fourShipBox = new HBox(35,fourShipButton, imageViewFourShip);
		HBox fiveShipBox = new HBox(35,fiveShipButton, imageViewFiveShip);

		VBox allShips = new VBox(10, selectShip,twoShipBox, threeShipBox, threeShip2Box, fourShipBox, fiveShipBox);
		allShips.setTranslateX(20);

		allShips.setTranslateY(30);
		done = new Button("DONE");
//		placeShip = new Button("PLACE SHIP");

		done.setStyle("-fx-background-radius: 40;");
		done.setPrefWidth(220);
		done.setPrefHeight(60);

//		placeShip.setStyle("-fx-background-radius: 40;");
//		placeShip.setPrefWidth(220);
//		placeShip.setPrefHeight(60);
//		placeShip.setDisable(true);
		done.setDisable(true);

		// Create a GridPane layout
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setTranslateY(-30);
		gridPane.setTranslateX(180);

		gridPane.setHgap(5);
		gridPane.setVgap(5);

		popUpMessage.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
		popUpMessage.setFill(Color.RED);

		VBox buttonsBox = new VBox(20, shipOrientation, horizontal, vertical, done, popUpMessage);
		buttonsBox.setTranslateY(30);
		buttonsBox.setTranslateX(240);

		setShipButtons = new ArrayList<>();

		HBox rightSide = new HBox(-10, gridPane, buttonsBox);
		rightSide.setTranslateX(-125);

		HBox all = new HBox(5, allShips, rightSide);
		populateGrid(setShipButtons, gridPane, 60);

		setDisableGridPane("disable");
		twoShipButton.setOnMouseClicked(e -> {
//			AtomicInteger keepCount = new AtomicInteger();
			horizontal.setDisable(false);
			vertical.setDisable(false);

			//disable other ships so user cannot undo their choice
			threeShipButton.setDisable(true);
			threeShip2Button.setDisable(true);
			fourShipButton.setDisable(true);
			fiveShipButton.setDisable(true);
			setDisableGridPane("disable");

			horizontal.setOnMouseClicked(t -> {
				disableShipCoords();

				game.currentPlayer.twoShip.isHorizontal = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);

				//if(keepCount.equals(0)) {
				searchGridButtons(game.currentPlayer.twoShip, game.currentPlayer);

				//keepCount.addAndGet(1);
				//}
//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				System.out.println("checkng string" + checkCoord+"\n");
//				game.checkFirstClick(checkCoord, game.currentPlayer.twoShip);
//				tempDisableButtons(game.currentPlayer.twoShip, game.currentPlayer, checkCoord);
			});

			vertical.setOnMouseClicked(d -> {
				disableShipCoords();

				game.currentPlayer.twoShip.isVertical = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);

				searchGridButtons(game.currentPlayer.twoShip, game.currentPlayer);
//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				game.checkFirstClick(checkCoord, game.currentPlayer.twoShip);
//				tempDisableButtons(game.currentPlayer.twoShip, game.currentPlayer, checkCoord);
			});

		});

		threeShipButton.setOnMouseClicked(e -> {
			horizontal.setDisable(false);
			vertical.setDisable(false);

			threeShip2Button.setDisable(true);
			twoShipButton.setDisable(true);
			fourShipButton.setDisable(true);
			fiveShipButton.setDisable(true);
			setDisableGridPane("disable");

			horizontal.setOnMouseClicked(t -> {
				//System.out.prdintln("inside three ship button horizontal click\n\n");
				disableShipCoords();

				game.currentPlayer.threeShip.isHorizontal = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);

				searchGridButtons(game.currentPlayer.threeShip, game.currentPlayer);
//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				game.checkFirstClick(checkCoord, game.currentPlayer.threeShip);
//				tempDisableButtons(game.currentPlayer.threeShip, game.currentPlayer, checkCoord);
			});

			vertical.setOnMouseClicked(d -> {
				disableShipCoords();

				game.currentPlayer.threeShip.isVertical = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);

				searchGridButtons(game.currentPlayer.threeShip, game.currentPlayer);
//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				game.checkFirstClick(checkCoord, game.currentPlayer.threeShip);
//				tempDisableButtons(game.currentPlayer.threeShip, game.currentPlayer, checkCoord);
			});
		});

		threeShip2Button.setOnMouseClicked(e -> {
			horizontal.setDisable(false);
			vertical.setDisable(false);

			twoShipButton.setDisable(true);
			threeShipButton.setDisable(true);
			fourShipButton.setDisable(true);
			fiveShipButton.setDisable(true);
			setDisableGridPane("disable");

			horizontal.setOnMouseClicked(t -> {
				disableShipCoords();

				game.currentPlayer.threeShip2.isHorizontal = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);
				searchGridButtons(game.currentPlayer.threeShip2, game.currentPlayer);

//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				game.checkFirstClick(checkCoord, game.currentPlayer.threeShip2);
//				tempDisableButtons(game.currentPlayer.threeShip2, game.currentPlayer, checkCoord);
			});

			vertical.setOnMouseClicked(d -> {
				disableShipCoords();

				game.currentPlayer.threeShip2.isVertical = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);

				searchGridButtons(game.currentPlayer.threeShip2, game.currentPlayer);

//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				game.checkFirstClick(checkCoord, game.currentPlayer.threeShip2);
//				tempDisableButtons(game.currentPlayer.threeShip2, game.currentPlayer, checkCoord);
			});

		});

		fourShipButton.setOnMouseClicked(e -> {
			horizontal.setDisable(false);
			vertical.setDisable(false);

			twoShipButton.setDisable(true);
			threeShipButton.setDisable(true);
			threeShip2Button.setDisable(true);
			fiveShipButton.setDisable(true);
			setDisableGridPane("disable");

			horizontal.setOnMouseClicked(t -> {
				disableShipCoords();

				game.currentPlayer.fourShip.isHorizontal = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);
				searchGridButtons(game.currentPlayer.fourShip, game.currentPlayer);

//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				game.checkFirstClick(checkCoord, game.currentPlayer.fourShip);
//				tempDisableButtons(game.currentPlayer.fourShip, game.currentPlayer, checkCoord);
			});

			vertical.setOnMouseClicked(d -> {
				disableShipCoords();


				game.currentPlayer.fourShip.isVertical = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);
				searchGridButtons(game.currentPlayer.fourShip, game.currentPlayer);

				//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				game.checkFirstClick(checkCoord, game.currentPlayer.fourShip);
//				tempDisableButtons(game.currentPlayer.fourShip, game.currentPlayer, checkCoord);
			});

		});

		fiveShipButton.setOnMouseClicked(e -> {
			horizontal.setDisable(false);
			vertical.setDisable(false);

			twoShipButton.setDisable(true);
			threeShipButton.setDisable(true);
			threeShip2Button.setDisable(true);
			fourShipButton.setDisable(true);
			setDisableGridPane("disable");

			horizontal.setOnMouseClicked(t -> {
				disableShipCoords();


				game.currentPlayer.fiveShip.isHorizontal = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);
				searchGridButtons(game.currentPlayer.fiveShip, game.currentPlayer);
//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				game.checkFirstClick(checkCoord, game.currentPlayer.fiveShip);
//				tempDisableButtons(game.currentPlayer.fiveShip, game.currentPlayer, checkCoord);
			});

			vertical.setOnMouseClicked(d -> {
				disableShipCoords();

				game.currentPlayer.fiveShip.isVertical = true;
				setDisableGridPane("enable");
				horizontal.setDisable(true);
				vertical.setDisable(true);
				searchGridButtons(game.currentPlayer.fiveShip, game.currentPlayer);
//				Button checkButton = searchGridButtons().get(); //set regular button equal to atomic button
//				String checkCoord = checkButton.getText();
//				game.checkFirstClick(checkCoord, game.currentPlayer.fiveShip);
//				tempDisableButtons(game.currentPlayer.fiveShip, game.currentPlayer, checkCoord);
			});
		});


		// Create a scene with the gridPane
		BorderPane pane = new BorderPane();
		pane.setCenter(all);
		pane.setStyle("-fx-background-color: rgb(98,170,237);");

		done.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
                try {
					if(game.versusComputer){
						game.createBoardForComputer();
					}
                    gameScreen(primaryStage);
                } catch (Exception e) {
					e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
		});

		Scene setUpScreen = new Scene(pane, 1450, 800);
		primaryStage.setMaximized(true);
		primaryStage.setScene(setUpScreen);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

//	public void tempDisableButtons(Ship ship, Player player, String coord){
//		String col = coord.substring(1); //number in the coordinate
//		char row =  coord.charAt(0);
//		String rowStr = coord.substring(0,1);
//		player.allCoordinates.add(coord);
//
//
//		if (ship.isVertical) {
//			//if they can place both above and below, disable everything not in that column
//			if(player.above && player.below){
//
//				for (Button tempButton: gridButtons){
//					//if it is not in that row, then disable the button
//					if(tempButton.getText().equals(coord)){
//						tempButton.setDisable(true);
//					}
//					if(!tempButton.getText().contains(col)){
//						tempButton.setDisable(true);
//					}
//				}
//			}
//
//			//if they can place their coord above but not below
//			else if(player.above){
//				for (Button tempButton: gridButtons){
//					if(tempButton.getText().equals(coord)){
//						tempButton.setDisable(true);
//					}
//					//if it is not in that row, then disable the button (checks if the button does not contain the row and if the letter is greater, disable the button)
//					if(!tempButton.getText().contains(col) || (tempButton.getText().charAt(0) > row )){
//						tempButton.setDisable(true);
//					}
//				}
//			}
//			//if they can place only below and not above
//			else {
//				for (Button tempButton: gridButtons){
//					if(tempButton.getText().equals(coord)){
//						tempButton.setDisable(true);
//					}
//					//if it is not in that row, then disable the button (checks if the button does not contain the row, and if the letter is greater)
//					if(!tempButton.getText().contains(col) || (tempButton.getText().charAt(0) < row )){
//						tempButton.setDisable(true);
//					}
//				}
//			}
//		}
//		//check if horizontal
//		else{
//			//if they can place left and right disable everything not in the row
//			if(player.right && player.left){
//				for (Button tempButton: gridButtons){
//					if(tempButton.getText().equals(coord)){
//						tempButton.setDisable(true);
//					}
//					//if it is not in that row, then disable the button
//					if (!tempButton.getText().contains(rowStr)) {
//						tempButton.setDisable(true);
//					}
//				}
//			}
//			// if they can place in the coord to the right but not left
//			else if(player.right){
//				for (Button tempButton: gridButtons){
//					if(tempButton.getText().equals(coord)){
//						tempButton.setDisable(true);
//					}
//					//if it is not in that row, then disable the button
//					if (!tempButton.getText().contains(rowStr) || (Integer.parseInt(tempButton.getText().substring(1)) < Integer.parseInt(col))) {
//						tempButton.setDisable(true);
//					}
//				}
//			}
//			//if they can place the coord to the left but not right
//			else {
//				for (Button tempButton: gridButtons){
//					if(tempButton.getText().equals(coord)){
//						tempButton.setDisable(true);
//					}
//					//if it is not in that row, then disable the button
//					if (!tempButton.getText().contains(rowStr) || (Integer.parseInt(tempButton.getText().substring(1)) > Integer.parseInt(col))) {
//						tempButton.setDisable(true);
//					}
//				}
//			}
//
//		}
//	}

	public void searchGridButtons(Ship ship, Player player){
		//AtomicReference<Integer> keepCount = new AtomicReference<>(0);
		for (Button tempButton : setShipButtons) {
			//System.out.println("inside for loop\n");
			tempButton.setOnAction(d -> {
				game.checkFirstClick(tempButton.getText(), ship, false);
				System.out.print("This is the tempButton.getText() inside searchGridButtons " + tempButton.getText() + "\n");

				if (game.isShipFilled && shipPlacedCount != 5) {
					ship.getCoordinates().add(tempButton.getText());
					game.currentPlayer.allCoordinates.add(tempButton.getText());

					popUpMessage.setText("Ship Placed!\nSelect A New Ship!");
					popUpMessage.setTextAlignment(TextAlignment.CENTER);
					shipPlacedCount++;
					disableShipCoords();
					setDisableGridPane("disable");

					// 5 checks for each ship to check whether they have been placed or not
					if (!game.currentPlayer.twoShip.getCoordinates().isEmpty()){
						twoShipButton.setDisable(true);
					}
					else{
						twoShipButton.setDisable(false);
					}

					if (!game.currentPlayer.threeShip.getCoordinates().isEmpty()){
						threeShipButton.setDisable(true);
					}
					else {
						threeShipButton.setDisable(false);
					}

					if (!game.currentPlayer.threeShip2.getCoordinates().isEmpty()){
						threeShip2Button.setDisable(true);
					}
					else {
						threeShip2Button.setDisable(false);
					}

					if (!game.currentPlayer.fourShip.getCoordinates().isEmpty()){
						fourShipButton.setDisable(true);
					}
					else {
						fourShipButton.setDisable(false);
					}

					if (!game.currentPlayer.fiveShip.getCoordinates().isEmpty()){
						fiveShipButton.setDisable(true);
					}
					else {
						fiveShipButton.setDisable(false);
					}
				}

				if (game.isShipFilled && shipPlacedCount == 5) {
					//call method to disable buttons
					popUpMessage.setText("All Ships Placed!\n Press Done");
					popUpMessage.setTextAlignment(TextAlignment.CENTER);
					disableShipCoords();
					done.setDisable(false);
				}

				if (!game.isShipFilled) {
					System.out.println("inside try again pick new coordinate\n");
					popUpMessage.setText("Try Again! Pick New Coordinate");
					setDisableGridPane("disable");
					vertical.setDisable(false);
					horizontal.setDisable(false);
				}
			});

		}
	}

	public void disableShipCoords(){
		System.out.println("inside disable ship coords");
		// disable the ship coordinates, so it cannot be reselected

//		game.currentPlayer.allCoordinates.add("F4");

		System.out.println("AllCoordinates inside disableShipCoords is: " + game.currentPlayer.allCoordinates);

		for (Button button : setShipButtons) {
			for (int i = 0; i < game.currentPlayer.allCoordinates.size(); i++) {
				//System.out.println("ship coordinates: " + ship.getCoordinates().get(i)+"\n");

//				System.out.println("this is the button.getText(): " + button.getText());

				if (button.getText().equals(game.currentPlayer.allCoordinates.get(i))) {
					button.setStyle("-fx-background-color: green; -fx-text-fill:black;");
					System.out.println("inside found ship coord");
					System.out.println("here is the button we are disabling: " + button.getText());
					button.setDisable(true);
				}
			}
		}
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
		Text headerAI = new Text ("AI MAP");
		headerAI.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
		//headerAI.setTranslateX(-30);
		headerAI.setUnderline(true);

		Text headerYourMap = new Text ("YOUR MAP");
		headerYourMap.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
		//headerYourMap.setTranslateX(-30);
		headerYourMap.setUnderline(true);

		Text popUpMessage = new Text ("");
		popUpMessage.setFill(Color.RED);
		popUpMessage.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

		Text message = new Text("Choose coordinate\nto attack");
		Text redMessage = new Text("= Miss");
		Text greenMessage = new Text("= Hit");

		Button redButton = new Button();
		redButton.setDisable(true);
		redButton.setStyle("-fx-background-color: red; -fx-text-fill:black;");
		//redButton.setStyle("-fx-background-radius: 40;");
		redButton.setPrefWidth(40);
		redButton.setPrefHeight(40);

		Button greenButton = new Button();
		greenButton.setDisable(true);
		greenButton.setStyle("-fx-background-color: green; -fx-text-fill:black;");
		//greenButton.setStyle("-fx-background-radius: 40;");
		greenButton.setPrefWidth(40);
		greenButton.setPrefHeight(40);

//		GridPane computerCoordsGrid = new GridPane();

//		computerCoordsGrid.setTranslateY(-30);
//		computerCoordsGrid.setTranslateX(180);
		computerCoordsGrid.setAlignment(Pos.CENTER);
		computerCoordsGrid.setHgap(5);
		computerCoordsGrid.setVgap(5);

		playerCoordsGrid.setAlignment(Pos.CENTER);
		playerCoordsGrid.setHgap(5);
		playerCoordsGrid.setVgap(5);

		populateGrid(computerCoords, computerCoordsGrid, 45);
		populateGrid(playerCoords, playerCoordsGrid,45);

		Button attackComputer = new Button("Attack AI");
		attackComputer.setStyle("-fx-background-radius: 40;");
		attackComputer.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
		attackComputer.setPrefWidth(220);
		attackComputer.setPrefHeight(60);
		attackComputer.setDisable(true);

		redMessage.setFont(Font.font("Courier New", FontWeight.NORMAL, 24));
		greenMessage.setFont(Font.font("Courier New", FontWeight.NORMAL, 24));
		HBox redKey = new HBox(8,redButton, redMessage );
		HBox greenKey = new HBox(8,greenButton, greenMessage);
		redKey.setAlignment(Pos.CENTER);
		greenKey.setAlignment(Pos.CENTER);
		VBox key = new VBox(15, redKey, greenKey);
		greenKey.setTranslateX(-6);
		message.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
		message.setTextAlignment(TextAlignment.CENTER);

		VBox keyAndMessage = new VBox(20, message,key, popUpMessage);
		VBox computerGridVbox = new VBox(20, headerAI, computerCoordsGrid, attackComputer);
		VBox playerGridVbox = new VBox(20, headerYourMap, playerCoordsGrid);

		computerGridVbox.setAlignment(Pos.CENTER);
		playerGridVbox.setAlignment(Pos.CENTER);

		keyAndMessage.setTranslateY(130);
		keyAndMessage.setTranslateX(-10);

		HBox all = new HBox(15, computerGridVbox, keyAndMessage, playerGridVbox);


		computerGridVbox.setTranslateX(-30);
		playerGridVbox.setTranslateX(20);
		playerGridVbox.setTranslateY(-40);

		all.setTranslateX(90);
		all.setTranslateY(-10);

		BorderPane pane = new BorderPane();
		pane.setCenter(all);

		pane.setStyle("-fx-background-color: rgb(98,170,237);");

		for (Button button : computerCoords) {
			button.setOnAction(e->{
				playerHits.add(button.getText());
				attackComputer.setDisable(false);

				button.setStyle("-fx-background-color: blue; -fx-text-fill:black;");
				blackOutGrid("disable", playerHits, computerCoords);

				attackComputer.setOnAction(d->{
					String coordinateChosen = button.getText();
					System.out.println("this is the button.getText() before the checkAttack: " + button.getText());
					//if it returns true, let them play again and change coordinate to green
					if (game.checkAttack(coordinateChosen, false)) {
						button.setStyle("-fx-background-color: green; -fx-text-fill:black;");
						button.setDisable(true);
						popUpMessage.setText("You hit a ship!\nChoose a new coordinate");
						popUpMessage.setTextAlignment(TextAlignment.CENTER);
						blackOutGrid("disable", computerHits, playerCoords);
						blackOutGrid("enable", playerHits, computerCoords);

						System.out.println("here is the twoShip hitCount: " + game.computerPlayer.twoShip.hitCount);

						if (game.computerPlayer.twoShip.hitCount == game.computerPlayer.twoShip.shipLength &&
								!game.computerPlayer.twoShip.shipAlreadySunk){
							System.out.println("YOU SUNK 2 SHIP\n\n");
							game.computerPlayer.twoShip.shipAlreadySunk = true;
							//alert message
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("You sunk their 2 ship!");
							alert.setContentText("Your ship has sunk, please continue choosing new coordinates to sink another");
							alert.showAndWait();
						}

						if (game.computerPlayer.threeShip.hitCount == game.computerPlayer.threeShip.shipLength &&
								!game.computerPlayer.threeShip.shipAlreadySunk){
							System.out.println("YOU SUNK 3 SHIP\n\n");
							game.computerPlayer.threeShip.shipAlreadySunk = true;
							//alert message
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("You sunk their 3 ship!");
							alert.setContentText("Your ship has sunk, please continue choosing new coordinates to sink another");
							alert.showAndWait();
						}

						if (game.computerPlayer.threeShip2.hitCount == game.computerPlayer.threeShip2.shipLength &&
								!game.computerPlayer.threeShip2.shipAlreadySunk){
							game.computerPlayer.threeShip2.shipAlreadySunk = true;
							System.out.println("YOU SUNK 3.2 SHIP\n\n");
							//alert message
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("You sunk their 3 ship!");
							alert.setContentText("Your ship has sunk, please continue choosing new coordinates to sink another");
							alert.showAndWait();
						}

						if (game.computerPlayer.fourShip.hitCount == game.computerPlayer.fourShip.shipLength &&
								!game.computerPlayer.fourShip.shipAlreadySunk){
							System.out.println("YOU SUNK 4 SHIP\n\n");
							game.computerPlayer.fourShip.shipAlreadySunk = true;
							//alert message
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("You sunk their 4 ship!");
							alert.setContentText("Your ship has sunk, please continue choosing new coordinates to sink another");
							alert.showAndWait();
						}

						if (game.computerPlayer.fiveShip.hitCount == game.computerPlayer.fiveShip.shipLength &&
								!game.computerPlayer.fiveShip.shipAlreadySunk){
							game.computerPlayer.fiveShip.shipAlreadySunk = true;
							System.out.println("YOU SUNK 5 SHIP\n\n");
							//alert message
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("You sunk their 5 ship!");
							alert.setContentText("Your ship has sunk, please continue choosing new coordinates to sink another");
							alert.showAndWait();
						}

						if (game.checkWinner().equals("currentPlayer")) {
							//do a, you win screen
							userWonScreen(primaryStage, pane);
						}
						else if (game.checkWinner().equals("otherPlayer")) {
							// do you lost screen
							userLostScreen (primaryStage, pane);
						}
					}
					//missed ship
					else {
						button.setStyle("-fx-background-color: red; -fx-text-fill:black;");
						popUpMessage.setText("You missed!\n Computer's turn");
						popUpMessage.setTextAlignment(TextAlignment.CENTER);
						//disable grid
						blackOutGrid("disable", playerHits, computerCoords);
						blackOutGrid("enable", computerHits, playerCoords);
						//calculate do computer's turn
						attackComputer.setDisable(true);
						// based off this boolean do the same checks as above
						String computerHit = game.computerPlayer.computerHit();

						while (game.checkAttack(computerHit, true)) {
							// loop through the player buttons, simulating the computers turn
							for (Button tempButton: playerCoords) {
								if (computerHit.equals(tempButton.getText())) {
									tempButton.setDisable(true);
									tempButton.setStyle("-fx-background-color: green; -fx-text-fill:black;");
									computerHits.add(computerHit);
								}
							}
							computerHit = game.computerPlayer.computerHit();
						}

						computerHits.add(computerHit); //if they miss, still add

						//find miss, disable it and set color to red
						for (int i = 0; i < playerCoords.size(); i++) {
							if (playerCoords.get(i).getText().equals(computerHit)) {
								playerCoords.get(i).setDisable(true);
								playerCoords.get(i).setStyle("-fx-background-color: red; -fx-text-fill:black;");
							}
						}

						popUpMessage.setText("Computer Missed!\nYour Turn!");
						popUpMessage.setTextAlignment(TextAlignment.CENTER);
						blackOutGrid("disable", computerHits, playerCoords);
						blackOutGrid("enable", playerHits, computerCoords);
						//not true, set that coordinate to red
					}
				});
			});
		}

		//return new Scene(pane);
		Scene gameScene = new Scene(pane, 1460, 800);
		primaryStage.setScene(gameScene);
		primaryStage.setMaximized(true);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public void blackOutGrid(String enable, ArrayList<String> hits, ArrayList<Button> buttons){
		// when it is player's turn, enable the computer grid
		if(enable.equals("enable")){
			for(Button button : buttons){
				//if it is a button that was already hit, do not re-enable
				if(hits.contains(button.getText())){
					button.setDisable(true);
				}
				else{
					button.setDisable(false);
				}
			}
		}
		else{
			// disabling all the buttons
			for(Button button : buttons){
				button.setDisable(true);
			}
		}
	}

	public void populateGrid(ArrayList<Button> populateCoordGridArray, GridPane currentPane, int buttonSize) {
		int rows = 10;

		String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		int cols = 10;

		for (int row = 0; row < letters.length; row++) {
			for (int col = 0; col < cols; col++) {
				// Create a new button
				Button button = new Button();

				// Set button text or other properties as needed
				button.setText(letters[row] + (col + 1)); // Set initial button text (e.g., "")

				// Set button size (optional, a; just as needed)
				button.setMinSize(buttonSize, buttonSize);// Add the button to the grid

				currentPane.add(button, col, row);

				populateCoordGridArray.add(button);
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
	}

	// you lost screen
	public void userLostScreen (Stage primaryStage, BorderPane tempPane) {
		ColorAdjust color = new ColorAdjust();
		color.setBrightness(-0.7);
		tempPane.setEffect(color);

		Button goHome;
		Text userWonText;
		VBox userChoiceButtons;

		goHome = new Button("Go Home");
		userWonText = new Text("YOU LOST, battleship!");
		userWonText.setFont(Font.font("Courier New", FontWeight.BOLD, 48));
		userWonText.setFill(rgb(255, 255, 255));

		goHome.setMinSize(100, 50);
		goHome.setFont(Font.font("Courier New", FontWeight.NORMAL, 24));

		userChoiceButtons = new VBox(50, userWonText, goHome);
		userChoiceButtons.setAlignment(Pos.CENTER);

		goHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				try {
					welcomeScreen(primaryStage);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		StackPane userWonPane = new StackPane(tempPane, userChoiceButtons);

		//pane.setStyle("-fx-background-color: rgb(98,170,237);");
		Scene mainScene = new Scene(userWonPane, 1440, 800);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	// you won screen
	public void userWonScreen(Stage primaryStage, BorderPane tempPane) {
		ColorAdjust color = new ColorAdjust();
		color.setBrightness(-0.7);
		tempPane.setEffect(color);

		Button goHome;
		Text userWonText;
		VBox userChoiceButtons;

		goHome = new Button("Go Home");
		userWonText = new Text("YOU WON, battleship!");
		userWonText.setFont(Font.font("Courier New", FontWeight.BOLD, 48));
		userWonText.setFill(rgb(255, 255, 255));

		goHome.setMinSize(100, 50);
		goHome.setFont(Font.font("Courier New", FontWeight.NORMAL, 24));

		userChoiceButtons = new VBox(50, userWonText, goHome);
		userChoiceButtons.setAlignment(Pos.CENTER);

		goHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
                try {
                    welcomeScreen(primaryStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
		});

		StackPane userWonPane = new StackPane(tempPane, userChoiceButtons);

		//pane.setStyle("-fx-background-color: rgb(98,170,237);");
		Scene mainScene = new Scene(userWonPane, 1440, 800);
		primaryStage.setScene(mainScene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	//this enables or disables all the buttons in the grid pane
	//parameters: String enable
	//return: void
	private void setDisableGridPane(String enable) {
		//enable all the grid buttons
		if (enable.equals("enable")) {
			for (int i = 0; i < setShipButtons.size(); i++) {
				if (!game.currentPlayer.allCoordinates.contains(setShipButtons.get(i).getText())) {
					setShipButtons.get(i).setDisable(false);
				}
			}
		}
		//disable all the grid buttons
		else {
			for (int i = 0; i < setShipButtons.size(); i++) {
				setShipButtons.get(i).setDisable(true);
			}
		}
	}
}



