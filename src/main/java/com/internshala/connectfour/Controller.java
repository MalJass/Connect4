package com.internshala.connectfour ;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Controller implements Initializable{

	private static final int COLUMNS =7;
	private static final int ROWS =6;
	private static final int CIRCLE_DIAMETER = 80;
	private static final String discColor1 ="#FF0000";
	private static final String discColor2 ="#FFFF00";

	private static String PLAYER_ONE = "player one ";
	private static String PLAYER_TWO = "player two";

	private boolean isPlayerOneTurn = true;

	private Disc[][] insertedDiscArray = new Disc[ROWS][COLUMNS];


	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscPane;

	@FXML
	public Label playerNameLabel;

	@FXML
	public TextField playerOneTextField , playerTwoTextField;

	@FXML
	public Button setNamesButton;

	private boolean isAllowedToInsert = true;

	public void createPlayground(){

		Shape rectangleWithHoles = createGameStructureGird();
		rootGridPane.add(rectangleWithHoles,0,1);
		
		List<Rectangle> rectangleList =createClickableColumns ();
		for (Rectangle rectangle: rectangleList) {
			rootGridPane.add(rectangle, 0, 1);
		}

		setNamesButton.setOnAction(event ->{

			String playerOneName = playerOneTextField.getText().trim();
			String playerTwoName = playerTwoTextField.getText().trim();

			// Update PLAYER_ONE and PLAYER_TWO
			PLAYER_ONE = playerOneName.isEmpty() ? "Player One" : playerOneName;
			PLAYER_TWO = playerTwoName.isEmpty() ? "Player Two" : playerTwoName;

			// Update playerNameLabel
			playerNameLabel.setText(PLAYER_ONE);

		});
	}

	private Shape createGameStructureGird(){

		Shape  rectangleWithHoles = new Rectangle((COLUMNS+1) * CIRCLE_DIAMETER ,(ROWS+1) * CIRCLE_DIAMETER);

		for (int row = 0; row < ROWS; row++){

			for (int col=0; col < COLUMNS; col++){

				Circle circle = new Circle();
				circle.setRadius(CIRCLE_DIAMETER/2);
				circle.setCenterY(CIRCLE_DIAMETER/2);
				circle.setCenterX(CIRCLE_DIAMETER/2);
				circle.setSmooth(true);

				circle.setTranslateX(col * (CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/4);
				circle.setTranslateY(row * (CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/4);

				rectangleWithHoles=Shape.subtract(rectangleWithHoles, circle);

			}
		}

		rectangleWithHoles.setFill(Color.valueOf("Ffc0cb"));
		return rectangleWithHoles;

	}
	private List<Rectangle> createClickableColumns() {
		List<Rectangle> rectangleList =new ArrayList<>();

		for(int col =0; col< COLUMNS; col++) {

			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("eeeeee26")));
			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

			final int column =col;
			rectangle.setOnMouseClicked(event -> {
				if(isAllowedToInsert) {
					isAllowedToInsert = false;

					insertDisc(new Disc(isPlayerOneTurn), column);
				}
			});

			rectangleList.add(rectangle);
		}
		return rectangleList;
	}

	private void insertDisc(Disc disc , int column){

		int row =ROWS-1;
		while (row >=0){
			if (getDiscPresent(row , column )== null)
				break;
			row --;
		}
		if (row<0)
			return;

		insertedDiscArray[row][column]=disc;
		insertedDiscPane.getChildren().add(disc);

		disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

		int currentRow= row;
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.6) , disc);
		translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
		translateTransition.setOnFinished(event->{

			isAllowedToInsert=true;
			if (gameEnded(currentRow, column)){
				gameOver();

			}
			isPlayerOneTurn =!isPlayerOneTurn;
			playerNameLabel.setText(isPlayerOneTurn? PLAYER_ONE:PLAYER_TWO);
		});

		translateTransition.play();

	}

	private boolean gameEnded(int row, int column) {

		List<Point2D> verticalPoints =IntStream.rangeClosed(row -3 ,row +3)
				.mapToObj(r -> new Point2D(r, column))
				.collect(Collectors.toList());

		List<Point2D> horizontalPoints =IntStream.rangeClosed(column -3,column+3)
				.mapToObj(col -> new Point2D(row, col))
				.collect(Collectors.toList());

		Point2D startPoint1 = new Point2D(row-3 ,column+3);
		List<Point2D>diagonal1points = IntStream.rangeClosed(0,6)
				.mapToObj(i-> startPoint1.add(i,-i))
				.collect(Collectors.toList());


		Point2D startPoint2 = new Point2D(row-3 ,column-3);
		List<Point2D>diagonal2points = IntStream.rangeClosed(0,6)
				.mapToObj(i-> startPoint2.add(i,i))
				.collect(Collectors.toList());

		boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
				|| checkCombinations(diagonal1points) || checkCombinations(diagonal2points);


		return isEnded;


	}

	private boolean checkCombinations(List<Point2D> points) {

		int chain = 0;

		for (Point2D point :points) {

			int rowIndexForArray =(int) point.getX();
			int columnIndexForArray = (int) point.getY();

			Disc disc = getDiscPresent(rowIndexForArray , columnIndexForArray);

			if (disc!= null && disc.isPlayerOnemMove == isPlayerOneTurn) {

				chain++;
				if (chain == 4) {
					return true;
				}
			}else{
					chain=0;
			}

		}
		return false;

	}

	private Disc getDiscPresent(int row , int column){
		if (row >=ROWS || row < 0 || column >= COLUMNS || column < 0)
			return null;
		return insertedDiscArray[row][column];
	}

	private void gameOver(){
		String winner = isPlayerOneTurn? PLAYER_ONE : PLAYER_TWO;
		System.out.println("winner is " + winner);

		Alert alert= new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("connect four");
		alert.setHeaderText("the winner is " + winner);
		alert.setContentText("Want To Play Again?");

		ButtonType yesBtn = new ButtonType("Yes");
		ButtonType noBtn = new ButtonType("No, Exit!");
		alert.getButtonTypes().setAll(yesBtn, noBtn);

		Platform.runLater(()->{

			Optional<ButtonType> btnClicked = alert.showAndWait();
			if (btnClicked.isPresent() && btnClicked.get()== yesBtn){
				resetGame();
			} else {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	public void resetGame() {

		insertedDiscPane.getChildren().clear();

		for (int row=0; row< insertedDiscArray.length; row++){
			for (int col=0; col < insertedDiscArray[row].length;col++){
				insertedDiscArray[row][col]=null;
			}
		}

		isPlayerOneTurn = true;
		playerNameLabel.setText(PLAYER_ONE);

		createPlayground();
	}


	private static class Disc extends Circle{
		private final boolean isPlayerOnemMove;
		public Disc(boolean isPlayerOnemMove){
			this.isPlayerOnemMove= isPlayerOnemMove;
			setRadius(CIRCLE_DIAMETER /2);
			setFill(isPlayerOnemMove?Color.valueOf(discColor1):Color.valueOf(discColor2));
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterY(CIRCLE_DIAMETER/2);
		}

	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {



	}
}