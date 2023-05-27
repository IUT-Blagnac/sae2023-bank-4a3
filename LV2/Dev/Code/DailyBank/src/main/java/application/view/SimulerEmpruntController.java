package application.view;

import application.DailyBankState;
import application.control.SimulationEmprunt;
import application.tools.TypeEmprunt;
import application.tools.TypeSimu;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Contrôleur pour la simulation d'emprunt.
 * 
 * @see SimulerEmprunt
 * @author LAMOUR Evan
 */
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

	/**
	 * Affiche la boîte de dialogue de simulation d'emprunt.
	 * 
	 * @author LAMOUR Evan
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param se               Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 * @author LAMOUR Evan
	 */
	public void initContext(Stage _containingStage, SimulationEmprunt se, DailyBankState _dbstate) {
		this.seDialogController = se;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.Assurance.setVisible(false);
		this.ts = TypeSimu.EMPRUNT;
		this.configure();
	}

	/**
	 * Configure les différents éléments de la fenêtre.
	 * 
	 * @author LAMOUR Evan
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	/**
	 * Ferme la fenêtre.
	 * 
	 * @param e L'événement de fermeture
	 * @return Object null
	 * @author LAMOUR Evan
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	/**
	 * Passe le mode de calcul en mois (menu FXML).
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doMois() {
		this.te = TypeEmprunt.MOIS;
	}

	/**
	 * Passe le mode de calcul en année (menu FXML).
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doAnnee() {
		this.te = TypeEmprunt.ANNEE;
	}

	/**
	 * Ajout/retrait d'une assurance.
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doAssurance() {
		if (this.Assurance.isVisible()) {
			this.Assurance.setText("");
			this.Assurance.setVisible(false);
			this.ts = TypeSimu.EMPRUNT;
		} else {
			this.Assurance.setVisible(true);
			this.ts = TypeSimu.ASSURANCE;
		}
	}

	/**
	 * Vérifie que les champs sont remplis.
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Valide les champs et lance la simulation (ouvre le résultat dans un tableau).
	 * 
	 * @see TableauAmortissement
	 * @see TableauAmortissementController
	 * @author LAMOUR Evan
	 */
	@FXML
	private void Valider() {
		if (this.ts == TypeSimu.ASSURANCE) {
			this.tauxA = Double.parseDouble(this.Assurance.getText());
		} else {
			this.tauxA = 0;
		}
		this.montant = Integer.parseInt(this.tfmontant.getText());
		this.duree = Integer.parseInt(this.tfduree.getText());
		this.taux = Double.parseDouble(this.tftaux.getText());
		this.seDialogController.simulation(this.montant, this.taux, this.tauxA, this.duree, this.te, this.ts);
	}
}