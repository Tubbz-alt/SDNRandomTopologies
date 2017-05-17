package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Settings implements Initializable{
	ArrayList<String> data=new ArrayList<String>();
	@FXML
	TextField bandwidth;
	@FXML
	TextField delay;
	@FXML
	TextField loss;
	@FXML
	TextField maxQueueSize;
	@FXML
	CheckBox use_htb;
	@FXML
	ComboBox<String> chooseLink;
	@FXML
	Button add;	
	@FXML
	Button Delete;	
	@FXML
	TextField controllerIPAddress;
	String BW,Delay,Loss,MaxQueueSize,ControllerIPAddress;
	String useHtb="False";
	BufferedReader br;
	private static final Pattern ValidateIPv4 = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	
	public void Add(ActionEvent event){
		if (bandwidth.getText().isEmpty() || delay.getText().isEmpty() || loss.getText().isEmpty() 
			|| maxQueueSize.getText().isEmpty()){
			test.alertBox("Error","Empty Fields","Please Fill the empty fields");
		}else{
			BW=bandwidth.getText().toString();
			Delay=delay.getText().toString();
			Loss=loss.getText().toString();
			MaxQueueSize=maxQueueSize.getText().toString();
			if (use_htb.isSelected()){
				useHtb="True";
			}
			 try {
			        File file = new File("Settings.txt");
			        // if file doesnt exists, then create it
			        if (!file.exists()) {
			            file.createNewFile();
			        }

			        FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			        BufferedWriter bw = new BufferedWriter(fw);
			        bw.write(("bw="+BW+", delay='"+Delay+"ms', loss="+Loss+", max_queue_size="+MaxQueueSize+", use_htb="+useHtb+System.lineSeparator()));
			        bw.close();
			        showAlert("Deleted Successfully");
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
		}	
	}

	
	public void Delete(ActionEvent event){
		try {
            File inFile = new File("Settings.txt");
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader("Settings.txt"));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String line ;
            //Read from the original file and write to the new 
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(chooseLink.getSelectionModel().getSelectedItem())) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
 
            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }
            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");
 
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		showAlert("Deleted Successfully");
	}
	
	public void AddController(ActionEvent event){
		if (controllerIPAddress.getText().isEmpty()){
				test.alertBox("Error","Empty Fields","Please Enter IP address");
			}else{
				ControllerIPAddress=controllerIPAddress.getText().toString();
			}
		Boolean valid=IPv4validation(ControllerIPAddress);
		if (!valid){
			test.alertBox("Error","Invalid Inputs","Please Enter Valid IP address");
		}else{
			try {
		        File file = new File("ControllerIPAddress.txt");
		        // if file doesnt exists, then create it
		        if (!file.exists()) {
		            file.createNewFile();
		        }
		        FileWriter fw = new FileWriter(file.getAbsoluteFile());
		        BufferedWriter bw = new BufferedWriter(fw);
		        bw.write(ControllerIPAddress);
		        bw.close();
		        showAlert("Added Successfully");
		    } catch (IOException e) {
		    	
		    }
		}
	}
	
	public static boolean IPv4validation(String ipAddress) {
	    return ValidateIPv4.matcher(ipAddress).matches();
	}
	
	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Done");
		alert.setHeaderText(null);
		alert.setContentText(message);
		Optional<ButtonType> result = alert.showAndWait();
		try{
			if (result.get() == ButtonType.OK){
				Stage stage = (Stage) add.getScene().getWindow();
				stage.close();
			} 
		}catch(Exception e){
			Stage stage = (Stage) add.getScene().getWindow();
			stage.close();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
			FileInputStream fstream;
			try {
				fstream = new FileInputStream("Settings.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				String strLine;
				chooseLink.getItems().add("Select Link Parameter");
				//Read File Line By Line
				while ((strLine = br.readLine()) != null)   {
				  // Print the content on the console
					chooseLink.getItems().add(strLine);
				 // System.out.println (strLine);
				}
				chooseLink.getSelectionModel().selectFirst();
				//Close the input stream
				br.close();
			} catch (IOException e) {
			}	
	}

}
