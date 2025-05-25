package com.mainpackage;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * <h1>Abstract Screen Class</h1>
 * all screens object should inherit from this class
 * <br>
 * constructor consists of two parameters:<br>
 * <b>stage</b>:Stage -the Stage where the new scene should be displayed <br>
 * <b>fxmlPath</b>:String -path of the .fxml file <br> 
 * <br>
 * Once a Screen object is created the scene will automatically change to the scene in the fxml file
 * <br>
 * <h3>It is advised that the classes inheriting from this class has the following constructor layout</h3><br>
 * <pre><code>
 * public MainScreen(Stage stage){
 * 		super(stage,"../.../somefxmlfile.fxml");
 * 		...whatever else you want...
 * }
 * </code></pre> 
 */
public abstract class Screen {
	private Stage stage;
	private Parent root;
	
	public Screen(Stage stage,String fxmlPath) {
		this.stage=stage;
		try {
			this.root=FXMLLoader.load(getClass().getResource(fxmlPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		changeScene();
	}
	/**
	 * 
	 * @param newPath for the fxml file
	 */
	public void changeRoot(String newPath) {
		try {
			root=FXMLLoader.load(getClass().getResource(newPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * change the scene of the current stage to the scene corresponding to the root 
	 */
	public void changeScene() {
		Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void changeTitle(String title) {
		stage.setTitle(title);
	}

}
