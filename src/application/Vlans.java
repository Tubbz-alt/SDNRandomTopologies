package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Vlans {
	@FXML 
	Button ConfigureVlan ;
	BufferedWriter bw = null;
	FileWriter fw = null;
	HashMap<String,ComboBox> cboxes = new HashMap<String,ComboBox>();
	HashMap<String,TextField> textFields = new HashMap<String,TextField>();
	public static ArrayList<String> hostsArray = new ArrayList<String>();
	public static ArrayList<String> VlanArray = new ArrayList<String>();
	HashMap<Integer,String> links=new HashMap<Integer,String>();
	//vlans button
	public void ConfigureVlan (ActionEvent event){
		test testClass=new test();
		int hosts=test.hostNumber();
			Stage stage = (Stage) ConfigureVlan.getScene().getWindow();
			stage.hide();
			stage.setTitle("Configure Vlans");
	        Scene scene = new Scene(new Group(), 600,600);
			GridPane grid = new GridPane();
			ScrollPane sp = new ScrollPane(grid);
			grid.setHgrow(sp, Priority.ALWAYS);
			sp.setHbarPolicy(ScrollBarPolicy.NEVER);
			sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
			sp.setFitToWidth(true);
		    grid.setVgap(5);
		    grid.setHgap(20);
		    grid.setPadding(new Insets(40, 40, 20, 10));
		    Label title=new Label("Select Host");
		    grid.add(title,3, 0);
		    title.setStyle("-fx-text-fill: #008fb3;-fx-font-weight: bold;-fx-font-size: 20px;");
			for (int i=1;i<=hosts;i++){
				cboxes.put("combox"+i,new ComboBox());
			    grid.add(cboxes.get("combox"+i), 3, i);
			    for (int j=1;j<=hosts;j++){
					cboxes.get("combox"+i).getItems().add("h"+j);
				}
			    cboxes.get("combox"+i).getSelectionModel().selectFirst();
			    cboxes.get("combox"+i).setStyle(" -fx-background-color: #ffd24d;-fx-background-radius: 5,4,3,5;"
			    									+ "	-fx-background-insets: 0,1,2,0;-fx-font-size: 14px; -fx-text-fill: #008fb3;-fx-font-weight: bold;");
			}
			Label vlan=new Label("Enter Vlan ID");
		    grid.add(vlan,6, 0);
		    vlan.setStyle("-fx-text-fill: #008fb3;-fx-font-weight: bold;-fx-font-size: 20px;");
			for (int i=1;i<=hosts;i++){
				textFields.put("Textfield"+i,new TextField());
			    grid.add(textFields.get("Textfield"+i), 6, i);
			    textFields.get("Textfield"+i).setPromptText("ex:10");
			    textFields.get("Textfield"+i).setStyle(" -fx-background-color: #ffd24d;-fx-background-radius: 5,4,3,5;"
			    									+ "	-fx-background-insets: 0,1,2,0;-fx-font-size: 14px; -fx-text-fill: #008fb3;-fx-font-weight: bold;");
			}
			
			Button btn=new Button();
			btn.setText("Apply");
			btn.setStyle("-fx-background-color:#e6ac00;-fx-min-width: 80px;-fx-min-height:40;"
						+ "-fx-font-size: 20px;-fx-font-weight: bold;-fx-text-fill:white;");
			grid.add(btn,6, hosts+6);
	        Group root = (Group)scene.getRoot();
	        root.getChildren().add(grid);
	        root.getChildren().add(sp);
	        stage.setScene(scene);
	        stage.show();
	        //when create button in vlans stage is clicked
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	hostsArray.clear();
	            	VlanArray.clear();
	                for (int i=1;i<=hosts;i++){
	                	hostsArray.add(cboxes.get("combox"+i).getSelectionModel().getSelectedItem().toString());
	                	VlanArray.add(textFields.get("Textfield"+i).getText().toString());
	                	//System.out.println(VlanArray.get(i-1));	
	                }
	               if (VlanArray.contains("")){
	            	   testClass.alertBox("Error","Empty Fields","Please insert Vlan ID in the Empty Fields");
	               }else{
	            	Stage stage = (Stage) btn.getScene().getWindow();
	           		stage.close();
	    			Stage primaryStage = new Stage();
	    			Parent root;
					try {
						root = FXMLLoader.load(getClass().getResource("MainFxml.fxml"));
						Scene scene = new Scene(root,700,700);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		    			primaryStage.setScene(scene);
		    			primaryStage.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
	               }
	             }
	        });
	}
}
