module com.internshala.connectfour {
	requires javafx.controls;
	requires javafx.fxml;


	opens com.internshala.connectfour to javafx.fxml;
	exports com.internshala.connectfour;
}