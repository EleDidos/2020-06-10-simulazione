/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;


import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	//CONTROLLA SE ESISTE GRAFO
    	if(model.getGraph()==null) {
    		txtResult.setText("Devi prima creare il grafo!");
    		return;
    	}
    	Actor scelto=null;
    	try {
    		scelto=boxAttore.getValue();
    		if(scelto==null) {
    			txtResult.setText("Scegli un attore");
    			return;
    		}
    	}catch(NullPointerException npe) {
    		txtResult.setText("Scegli un attore");
    		return;
    	}
    	List<Actor> vicini = model.getVicini(scelto);
    	Collections.sort(vicini,new ComparatoreDiActors());
    	txtResult.appendText("\n\nI vertici raggiungibili da quello scelto sono:\n");
    	for(Actor a:vicini) {
    		if(!a.equals(scelto)) //per non stampare se stesso
    			txtResult.appendText(a+"\n");
    	}
    		
    }
    
    
    public class ComparatoreDiActors implements Comparator <Actor>{
		public int compare (Actor a1, Actor a2) {
			return a1.getLastName().compareTo(a2.getLastName());
		}
	}

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String genere=null;
    	
    	try {
    		genere=boxGenere.getValue();
    		if(genere==null) {
    			txtResult.setText("Scegli un genere");
    			return;
    		}
    	}catch(NullPointerException npe) {
    		txtResult.setText("Scegli un genere");
    		return;
    	}
    	
    	model.creaGrafo(genere);
    	txtResult.appendText("Caratteristiche del grafo:\n#VERTICI = "+model.getNVertici()+"\n#ARCHI = "+model.getNArchi());
    	
    	boxAttore.getItems().addAll(model.getVertici());
    	
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	//CONTROLLA SE ESISTE GRAFO
    	if(model.getGraph()==null) {
    		txtResult.setText("Devi prima creare il grafo!");
    		return;
    	}
    	
    	Integer n=0;
    	
    	try {
    		n=Integer.parseInt(txtGiorni.getText());
    		
    	}catch(NumberFormatException nfe) {
    		txtResult.setText("Inserisci un numero intero di giorni");
    		return;
    	}catch(NullPointerException npe) {
    		txtResult.setText("Inserisci un numero intero di giorni");
    		return;
    	}
    	
    	model.simula(n);
    	
    	txtResult.appendText("\n\nGli attori intervistati sono:\n");
    	for(Actor a: model.getIntervistati())
    		txtResult.appendText(a+"\n");
    	txtResult.appendText("\n\nI giorni di pausa sono: "+model.getPause());
    	
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List <String> generi = model.getGeneri();
    	Collections.sort(generi);
    	boxGenere.getItems().addAll( generi);
    }
}
