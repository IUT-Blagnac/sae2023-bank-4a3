package application.view;




import application.DailyBankState;
import application.control.ClientsManagement;
import application.control.SimulationEmprunt;
import application.tools.TypeEmprunt;
import application.tools.TypeSimu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
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
	@FXML
	private TextField Assurance;
	@FXML
	private ToggleButton tB;
	private TypeEmprunt te;
	private TypeSimu ts;
	
	private int montant;
	private double taux;
	private int duree;
	private double tauxA;
	
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}
	
	public void initContext(Stage _containingStage, SimulationEmprunt se, DailyBankState _dbstate) {
		this.seDialogController = se;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.Assurance.setVisible(false);
		this.ts = TypeSimu.EMPRUNT;
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
	private void doMois() {
		this.te = TypeEmprunt.MOIS;
	}
	@FXML
	private void doAnnee() {
		this.te = TypeEmprunt.ANNEE;
	}
	@FXML
	private void doAssurance() {
		this.Assurance.setVisible(true);
		this.ts = TypeSimu.ASSURANCE;
	}
	
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}
	@FXML
	private void Valider() {
		if(this.ts == TypeSimu.ASSURANCE) {
			this.tauxA = Double.parseDouble(this.Assurance.getText());
			System.out.println("Assurance");
			
		}else {this.tauxA=0;
			System.out.println("emprunt");
		}
		this.montant = Integer.parseInt(this.tfmontant.getText());
		this.duree = Integer.parseInt(this.tfduree.getText());
		this.taux = Double.parseDouble(this.tftaux.getText());
		this.seDialogController.simulation(this.montant, this.taux,this.tauxA,this.duree,this.te,this.ts);
	

	}
}
