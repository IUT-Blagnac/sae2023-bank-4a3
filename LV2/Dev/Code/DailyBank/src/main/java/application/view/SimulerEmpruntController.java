package application.view;

import application.DailyBankState;
import application.control.ClientsManagement;
import application.control.SimulationEmprunt;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class SimulerEmpruntController {
	
	private DailyBankState dailyBankState;
	private Stage primaryStage;
	private SimulationEmprunt seDialogController;
	@FXML
	private TextField tfmontant;
	@FXML
	private TextField tftaux;
	@FXML
	private TextField tfduree;
	
	private int montant;
	private double taux;
	private int duree;
	
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}
	
	public void initContext(Stage _containingStage, SimulationEmprunt se, DailyBankState _dbstate) {
		this.seDialogController = se;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}
	
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}
	
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}
	
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}
	@FXML
	private void Valider() {
		this.montant = Integer.parseInt(this.tfmontant.getText());
		this.duree = Integer.parseInt(this.tfduree.getText());
		this.taux = Double.parseDouble(this.tftaux.getText());
		this.seDialogController.simulation(this.montant, this.taux, this.duree);
	}
	

}
