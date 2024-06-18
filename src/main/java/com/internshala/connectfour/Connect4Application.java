package com.internshala.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Connect4Application extends Application {

private com.internshala.connectfour.Controller Controller;

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
		GridPane rootGridPane= fxmlLoader.load();

		Controller =fxmlLoader.getController();
		Controller.createPlayground();

		MenuBar menuBar = createMenu();
		menuBar.prefWidthProperty().bind(stage.widthProperty());
		Pane menuPane =(Pane) rootGridPane.getChildren().get(0);
		menuPane.getChildren().add(menuBar);



		Scene scene = new Scene(rootGridPane);
		stage.setTitle("connect four");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	private MenuBar createMenu(){

		//file

		Menu fileMenu = new Menu("file ");

		// new game
		MenuItem newGame  = new MenuItem("new game");
		newGame.setOnAction(actionEvent -> Controller.resetGame());

		/* reset */
		MenuItem resetGame  = new MenuItem("reset game ");
		resetGame.setOnAction(actionEvent -> Controller.resetGame());
		
		//exit
		MenuItem exitGame= new MenuItem("exit game ");
		exitGame.setOnAction(actionEvent -> exitGame());

		fileMenu.getItems().addAll(exitGame,resetGame,newGame);

		// help
		
		
		Menu helpMenu = new Menu("help ");

		MenuItem abtGame  = new MenuItem("about  game");
		abtGame.setOnAction(actionEvent -> abtGame());
		
		//abt me
		MenuItem abtMe  = new MenuItem("about me");
		abtMe.setOnAction(actionEvent -> abtMe());

		helpMenu.getItems().addAll(abtMe,abtGame);


		MenuBar menuBar= new MenuBar();
		menuBar.getMenus().addAll(fileMenu, helpMenu);

		return menuBar;
	}

	private void abtMe() {
		Alert alert= new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("about the developer");
		alert.setHeaderText("Malita Lewis");
		alert.setContentText("bca student. and wish to learn more");

		alert.show();
	}

	private void abtGame() {
		Alert alert= new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("about the connect4");
		alert.setHeaderText("how to play ");
		alert.setContentText(" Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");

		alert.show();
	}

	private void exitGame() {

		System.exit(0);
		Platform.exit();
	}

	private void resetGame() {
		
		// to do 
	}

	public static void main(String[] args) {
		launch();
	}
}