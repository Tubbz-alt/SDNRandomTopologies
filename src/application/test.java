package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class test {
	@FXML
	TextField NoSwitches;
	@FXML
	TextField NoHosts;
	@FXML
	Button nextBtn;
	@FXML
	AnchorPane content;
	public static int switches;
	public static int hosts;
	BufferedWriter bw = null;
	FileWriter fw = null;
	HashMap<String,ComboBox> cboxes = new HashMap<String,ComboBox>();
	HashMap<String,ComboBox> cboxes1 = new HashMap<String,ComboBox>();
	HashMap<String,ComboBox> cboxes2 = new HashMap<String,ComboBox>();
	HashMap<String,TextField> textFields = new HashMap<String,TextField>();
	public static ArrayList<String> hostsArray = new ArrayList<String>();
	public static ArrayList<String> vlans = new ArrayList<String>();
	public static ArrayList<String> VlanArray = new ArrayList<String>();
	Button applyBtn=new Button();
	Button skip=new Button();
	Button submit=new Button();
	String controllerIP;
	public void Next (ActionEvent event){
		if (NoSwitches.getText().isEmpty() || NoHosts.getText().isEmpty()){
			alertBox("Error","Empty Fields","Please insert Valid number of  Hosts");
		}else{
			switches=Integer.parseInt(NoSwitches.getText());
			hosts=Integer.parseInt(NoHosts.getText());
			Stage stage = (Stage) nextBtn.getScene().getWindow();
			stage.hide();
			stage.setTitle("Configure Vlans");
	        Scene scene = new Scene(new Group(), 600,700);
			GridPane grid = new GridPane();
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
			    cboxes.get("combox"+i).setStyle(" -fx-background-color: #d9d9d9;-fx-background-radius: 5,4,3,5;"
			    									+ "	-fx-background-insets: 0,1,2,0;-fx-font-size: 14px; -fx-text-fill: #008fb3;-fx-font-weight: bold;");
			}
			Label vlan=new Label("Enter Vlan ID");
		    grid.add(vlan,6, 0);
		    vlan.setStyle("-fx-text-fill: #008fb3;-fx-font-weight: bold;-fx-font-size: 20px;");
			for (int i=1;i<=hosts;i++){
				textFields.put("Textfield"+i,new TextField());
			    grid.add(textFields.get("Textfield"+i), 6, i);
			    textFields.get("Textfield"+i).setPromptText("ex:10");
			    textFields.get("Textfield"+i).setStyle(" -fx-background-color: #d9d9d9;-fx-background-radius: 5,4,3,5;"
			    									+ "	-fx-background-insets: 0,1,2,0;-fx-font-size: 14px; -fx-text-fill: #008fb3;-fx-font-weight: bold;");
			}
			
			applyBtn.setText("Apply");
			applyBtn.setStyle("-fx-background-color:#008fb3;-fx-pref-width: 100px;-fx-min-height:40;"
						+ "-fx-font-size: 20px;-fx-font-weight: bold;-fx-text-fill:white;");
			grid.add(applyBtn,6, hosts+6);
			skip.setText("Skip");
			skip.setStyle("-fx-background-color:#008fb3;-fx-pref-width: 100px;-fx-min-height:40;"
						+ "-fx-font-size: 20px;-fx-font-weight: bold;-fx-text-fill:white;");
			grid.add(skip,6, hosts+7);
	        Group root = (Group)scene.getRoot();
	        ScrollPane sp = new ScrollPane();
	        sp.setPannable(true);
	        sp.setPrefSize(600, 700);
	        sp.setContent(grid);
	        root.getChildren().add(sp);
	        stage.setScene(scene);
	        stage.show();
	        applyBtn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	hostsArray.clear();
	            	VlanArray.clear();
	                for (int i=1;i<=hosts;i++){
	                	hostsArray.add(cboxes.get("combox"+i).getSelectionModel().getSelectedItem().toString());
	                	VlanArray.add(textFields.get("Textfield"+i).getText().toString());
	                	vlans.add(hostsArray.get(i-1)+"=net.addHost('"+hostsArray.get(i-1)+"', cls=VLANHost, vlan="+VlanArray.get(i-1)+")");	
	                }
	               if (VlanArray.contains("")){
	            	   alertBox("Error","Empty Fields","Please insert Vlan ID in the Empty Fields");
	               }else{
	            	   addLinks();
	               }
	             }
	        });
		 
		 //skip button
		 skip.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	addLinks();
	            }
	        }); 
		}
	}
	
	public void Setting(ActionEvent event){
		Stage primaryStage = new Stage();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
			Scene scene = new Scene(root,700,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void addLinks(){
		switches=Integer.parseInt(NoSwitches.getText());
		hosts=Integer.parseInt(NoHosts.getText());
		Stage stage = (Stage) skip.getScene().getWindow();
		stage.hide();
		stage.setTitle("Configure links");
        Scene scene = new Scene(new Group(), 700,700);
		GridPane grid = new GridPane();
	    grid.setVgap(5);
	    grid.setHgap(20);
	    grid.setPadding(new Insets(40, 40, 20, 10));
	    Label title=new Label("Select Device");
	    grid.add(title,3, 0);
	    title.setStyle("-fx-text-fill: #008fb3;-fx-font-weight: bold;-fx-font-size: 20px;");  
		for (int i=1;i<=(hosts*switches);i++){
			cboxes.put("combox"+i,new ComboBox());
		    grid.add(cboxes.get("combox"+i), 3, i);
		    cboxes.get("combox"+i).getItems().add("Select Device");
		    for (int j=1;j<=switches;j++){
				cboxes.get("combox"+i).getItems().add("sw"+j);
			}
		    for (int j=1;j<=hosts;j++){
				cboxes.get("combox"+i).getItems().add("h"+j);
			}
		    
		    cboxes.get("combox"+i).getSelectionModel().selectFirst();
		    cboxes.get("combox"+i).setStyle(" -fx-background-color: #d9d9d9;-fx-background-radius: 5,4,3,5;"
		    									+ "	-fx-background-insets: 0,1,2,0;-fx-font-size: 14px; -fx-text-fill: #008fb3;-fx-font-weight: bold;");
		}
		Label vlan=new Label("Select Device");
	    grid.add(vlan,6, 0);
	    vlan.setStyle("-fx-text-fill: #008fb3;-fx-font-weight: bold;-fx-font-size: 20px;");
	    
	    for (int i=1;i<=(hosts*switches);i++){
			cboxes1.put("hcombox"+i,new ComboBox());
		    grid.add(cboxes1.get("hcombox"+i), 6, i);
		    cboxes1.get("hcombox"+i).getItems().add("Select Device");
		   
		    for (int j=1;j<=switches;j++){
				cboxes1.get("hcombox"+i).getItems().add("sw"+j);
			}
		    for (int j=1;j<=hosts;j++){
				cboxes1.get("hcombox"+i).getItems().add("h"+j);
			}
		    cboxes1.get("hcombox"+i).getSelectionModel().selectFirst();
		    cboxes1.get("hcombox"+i).setStyle(" -fx-background-color: #d9d9d9;-fx-background-radius: 5,4,3,5;"
		    									+ "	-fx-background-insets: 0,1,2,0;-fx-font-size: 14px; -fx-text-fill: #008fb3;-fx-font-weight: bold;");
		}
	    
	    FileInputStream fstream;
		try {
			fstream = new FileInputStream("Settings.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			Label title3=new Label("Select Link Properties");
			title3.setStyle("-fx-text-fill: #008fb3;-fx-font-weight: bold;-fx-font-size: 20px;");
		    grid.add(title3,8, 0);
			for (int i=1;i<=(hosts*switches);i++){
				cboxes2.put("lcombox"+i,new ComboBox());
			    grid.add(cboxes2.get("lcombox"+i), 8, i);
			    cboxes2.get("lcombox"+i).getItems().add("Default");	
			    cboxes2.get("lcombox"+i).getSelectionModel().selectFirst();
			    cboxes2.get("lcombox"+i).setStyle(" -fx-background-color: #d9d9d9;-fx-background-radius: 5,4,3,5;"
			    									+ "	-fx-background-insets: 0,1,2,0;-fx-font-size: 14px; -fx-text-fill: #008fb3;-fx-font-weight: bold;-fx-max-width: 200px;");
			   
			}
			while ((strLine = br.readLine()) != null)   {
				//System.out.println(strLine);
				for (int i=1;i<=(hosts*switches);i++){
				cboxes2.get("lcombox"+i).getItems().add(strLine);
				}
			}

		}catch (Exception ex){
		
		}
		
		submit.setText("Create");
		submit.setStyle("-fx-background-color:#008fb3;-fx-pref-width: 140px;-fx-min-height:45;"
					+ "-fx-font-size: 20px;-fx-font-weight: bold;-fx-text-fill:white;");
		grid.add(submit,6, ((hosts*switches)+1));
        Group root = (Group)scene.getRoot();
        ScrollPane sp = new ScrollPane();
      
        sp.setPannable(true);
        sp.setPrefSize(700, 700);
        sp.setContent(grid);
        root.getChildren().add(sp);
        //root.getChildren().add(grid);
        stage.setScene(scene);
        stage.show();
        
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					createBtn();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }); 
    }
//print the topology to a file
	public void createBtn() throws IOException{
		test testClass=new test();
		FileInputStream fstream1;
		try {
			fstream1 = new FileInputStream("ControllerIPAddress.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream1));	
			controllerIP = br.readLine(); 
			if (controllerIP.isEmpty()){
				controllerIP="127.0.0.1";
			}
		}catch (Exception ex){
		}
		try {
			File desktop = new File(System.getProperty("user.home"), "/Desktop/customTopo.py");
			fw = new FileWriter(desktop);
			bw = new BufferedWriter(fw);
				bw.write("#!/usr/bin/python"+System.lineSeparator()+"from mininet.topo import Topo"+System.lineSeparator()+"from mininet.net import Mininet"+System.lineSeparator());
				bw.write("from mininet.node import Controller, RemoteController, OVSController"+System.lineSeparator());
				bw.write("from mininet.node import CPULimitedHost, Host, Node"+System.lineSeparator());
				bw.write("from mininet.node import OVSKernelSwitch, UserSwitch"+System.lineSeparator());
				bw.write("from mininet.node import IVSSwitch"+System.lineSeparator());
				bw.write("from mininet.cli import CLI"+System.lineSeparator());
				bw.write("from mininet.log import setLogLevel, info"+System.lineSeparator());
				bw.write("from mininet.link import Link, TCLink, Intf"+System.lineSeparator());
				bw.write("from subprocess import call"+System.lineSeparator());
				bw.write("from mininet.util import irange,dumpNodeConnections"+System.lineSeparator());
				bw.write("class VLANHost( Host ):"+System.lineSeparator());
				bw.write("	def config( net, vlan=100, **params ):"+System.lineSeparator());
				bw.write("		r = super( Host, net ).config( **params )"+System.lineSeparator());
				bw.write("		intf = net.defaultIntf()"+System.lineSeparator());
				bw.write("		# remove IP from default, physical interface"+System.lineSeparator());
				bw.write("		net.cmd( 'ifconfig %s inet 0' % intf )"+System.lineSeparator());
				bw.write("		# create VLAN interface"+System.lineSeparator());
				bw.write("		net.cmd( 'vconfig add %s %d' % ( intf, vlan ) )"+System.lineSeparator());
				bw.write("		# assign the host's IP to the VLAN interface"+System.lineSeparator());
				bw.write("		net.cmd( 'ifconfig %s.%d inet %s' % ( intf, vlan, params['ip'] ) )"+System.lineSeparator());
				bw.write("		# update the intf name and host's intf map"+System.lineSeparator());
				bw.write("		newName = '%s.%d' % ( intf, vlan )"+System.lineSeparator());
				bw.write("		# update the (Mininet) interface to refer to VLAN interface name"+System.lineSeparator());
				bw.write("		intf.name = newName"+System.lineSeparator());
				bw.write("		# add VLAN interface to host's name to intf map"+System.lineSeparator());
				bw.write("		net.nameToIntf[ newName ] = intf"+System.lineSeparator());
				bw.write("		return r"+System.lineSeparator());
				bw.write("def topology():"+System.lineSeparator());
				//bw.write("	def __init__(net):"+System.lineSeparator());
				//bw.write("		Topo.__init__(net)"+System.lineSeparator());
				bw.write("	net = Mininet( controller=RemoteController, link=TCLink, switch=OVSKernelSwitch )"+System.lineSeparator());
				bw.write("	c1 = net.addController( 'c1', ip='"+controllerIP+"', port=6633 )"+System.lineSeparator());
				
				for (int hs=1;hs<=switches;hs++){
					bw.write("	sw"+hs+"=net.addSwitch('sw"+hs+"')"+System.lineSeparator());
				}
				if (!testClass.vlans.isEmpty()){
					for (int i=1;i<=hosts;i++){
						bw.write("	"+testClass.vlans.get(i-1)+System.lineSeparator());	
				    }
				}else{
					for (int ho=1;ho<=hosts;ho++){
						bw.write("	h"+ho+"=net.addHost('h"+ho+"')"+System.lineSeparator());
					}
				}
				for (int i=1;i<=(hosts*switches);i++){
					 if (!cboxes.get("combox"+i).getSelectionModel().getSelectedItem().equals("Select Device") &&
						 !cboxes1.get("hcombox"+i).getSelectionModel().getSelectedItem().equals("Select Device")&&
						 !cboxes2.get("lcombox"+i).getSelectionModel().getSelectedItem().equals("Default")){
						 bw.write("	net.addLink("+cboxes.get("combox"+i).getSelectionModel().getSelectedItem()+","+
								 cboxes1.get("hcombox"+i).getSelectionModel().getSelectedItem()+
								 ","+ cboxes2.get("lcombox"+i).getSelectionModel().getSelectedItem()+")"+System.lineSeparator());
					 }else if(!cboxes.get("combox"+i).getSelectionModel().getSelectedItem().equals("Select Device") &&
							 !cboxes1.get("hcombox"+i).getSelectionModel().getSelectedItem().equals("Select Device")&&
							 cboxes2.get("lcombox"+i).getSelectionModel().getSelectedItem().equals("Default")){
						 	 bw.write("	net.addLink("+cboxes.get("combox"+i).getSelectionModel().getSelectedItem()+","+
							 cboxes1.get("hcombox"+i).getSelectionModel().getSelectedItem()+")"+System.lineSeparator());
					 }
				}
		}catch(Exception e3){
			System.out.println("aaaa");
		}
			//bw.write("def perfTest():"+System.lineSeparator());
			bw.write("	net.build()"+System.lineSeparator());
			bw.write("	c1.start()"+System.lineSeparator());
			for (int c=1;c<=switches;c++){
				bw.write("	sw"+c+".start( [c1] )"+System.lineSeparator());
			}
			bw.write("	CLI( net )"+System.lineSeparator());
			bw.write("	net.stop()"+System.lineSeparator());
			bw.write("if __name__ == '__main__':"+System.lineSeparator());
			bw.write("	setLogLevel('info')"+System.lineSeparator());
			bw.write("	topology()"+System.lineSeparator());
			bw.close();
			fw.close();
	}
	
	public void close(ActionEvent event){
		System.exit(0);
	}
	 public static Integer switchesNumber()
	    {
	        return 	switches;
	    }
	 public static Integer hostNumber()
	    {
	        return 	hosts;
	    }
	 
	 public static  void alertBox(String title,String header,String message) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(message);
			alert.showAndWait();
		}
}
