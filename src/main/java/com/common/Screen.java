package com.common;

import java.io.IOException;

import com.mainpackage.SceneSwitching;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * <h1>Abstract Screen Class</h1>
 * all screens object should inherit from this class
 * <br>
 * <br>
 * use method:
 *  <pre><code>
 *      display(Stage stage,String fxmlPath)
 *  </code></pre>
 *  to display the fxml file
 * <br>
 * perhaps useful methods:
 * <pre><code>
 * //set the manager Object
 * public void getManager(Object manager)
 * //PLEASE NOTE THAT THE MANAGER IS DEFINED AS OBJECT SO YOU
 * //HAVE TO TYPECAST IT BEFORE YOU USE IT
 *
 * //for example if you want to use the method hi from helloWindowManager
 * ((helloWindowManager) this.manager).hi();
 * </code></pre>
 *
 * <pre><code>
 * //returns the current Stage
 * public Stage getStage(ActionEvent event)
 * </code></pre>
 * <h3>hope it works :)</h3><br>

 */
public abstract class Screen {
	protected Stage stage;
	private Parent root;
	protected Object manager;
	protected FXMLLoader loader;
	/**
	 * display function for the screens
	 * @param stage stage duhh
	 * @param fxmlPath path for the fxml file
	 */
	public void display(Stage stage,String fxmlPath) {
		this.loader= SceneSwitching.switchSceneR(stage,fxmlPath,this);
	}

	public Stage getStage(ActionEvent event) {
		return stage=(Stage)((Node) event.getSource()).getScene().getWindow();
	}
	public FXMLLoader getLoader(){
		return loader;
	}
	public void getManager(Object manager){
		this.manager=manager;
	}
}
