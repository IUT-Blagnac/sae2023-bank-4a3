package application.view;

import application.DailyBankState;
import application.control.EmployesManagement;
import application.control.SimulationEmprunt;
import application.control.TableauAmortissement;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TableauAmortissementController {
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à EmployesManagementController
	private TableauAmortissement taDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;
	
	private int montant;
	private double taux;
	private int duree;
	
	@FXML
	private GridPane gp;
	
	public void displayDialog() {
		for (int i=1;i<this.duree+1;i++) {
			Label date = new Label(""+i);
			date.setStyle("-fx-font-size : 10");
			gp.add(date, 0, i);;
			
		}
		this.primaryStage.showAndWait();
		
		
		
		
	}
	
	public void initContext(Stage _containingStage, TableauAmortissement ta, DailyBankState _dbstate,int montant,double taux,int duree) {
		this.taDialogController = ta ;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.montant=montant;
		this.duree=duree;
		this.taux=taux;
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

}
