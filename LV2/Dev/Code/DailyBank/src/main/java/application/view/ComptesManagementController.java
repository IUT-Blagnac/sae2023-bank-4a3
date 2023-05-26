package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.ComptesManagement;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;

public class ComptesManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ComptesManagementController
	private ComptesManagement cmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private Client clientDesComptes;
	private ObservableList<CompteCourant> oListCompteCourant;

	// Manipulation de la fenêtre
	public void initContext(Stage _containingStage, ComptesManagement _cm, DailyBankState _dbstate, Client client) {
		this.cmDialogController = _cm;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.clientDesComptes = client;
		this.configure();
	}

	private void configure() {
		String info;

		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListCompteCourant = FXCollections.observableArrayList();
		this.lvComptes.setItems(this.oListCompteCourant);
		this.lvComptes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvComptes.getFocusModel().focus(-1);
		this.lvComptes.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());

		info = this.clientDesComptes.nom + "  " + this.clientDesComptes.prenom + "  (id : "
				+ this.clientDesComptes.idNumCli + ")";
		this.lblInfosClient.setText(info);

		this.loadList();
		this.validateComponentState();
	}

	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblInfosClient;
	@FXML
	private ListView<CompteCourant> lvComptes;
	@FXML
	private Button btnVoirOpes;
	@FXML
	private Button btnModifierCompte;
	@FXML
	private Button btnSupprCompte;
	@FXML
	private Button btnAjouterCompte;

	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	@FXML
	private void doVoirOperations() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			this.cmDialogController.gererOperationsDUnCompte(cpt);
		}
		this.loadList();
		this.validateComponentState();
	}

	@FXML
	private void doNouveauCompte() {
		CompteCourant compte;
		compte = this.cmDialogController.creerNouveauCompte();
		if (compte != null) {
			oListCompteCourant.add(compte);
			lvComptes.setItems(oListCompteCourant);
			this.cmDialogController.creerNouveauCompte();
		}
	}

	@FXML
	private void doModifierCompte() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cptMod = this.oListCompteCourant.get(selectedIndice);
			CompteCourant result = this.cmDialogController.modifierCompte(cptMod);
			if (result != null) {
				this.oListCompteCourant.set(selectedIndice, result);
			}
		}
		this.loadList();
		this.validateComponentState();
	}

	@FXML
	private void doSupprimerCompte() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			if (cpt.solde == 0) {
				if(!AlertUtilities.confirmYesCancel(this.primaryStage, "Clôturer compte", "Êtes-vous sûr de vouloir clôturer ce compte ?", "Il ne sera pas possible de rouvrir ce compte.", AlertType.CONFIRMATION)) return;
				this.cmDialogController.cloturerCompte(cpt);
			} else {
				AlertUtilities.showAlert(this.primaryStage, "Clôturer compte", "Impossible de clôturer ce compte", "Afin de clôturer ce compte, merci de vous assurer que le solde soit bien égal à 0.\n\nSolde actuel : " + cpt.solde + " €", AlertType.WARNING);
			}
		}
		this.loadList();
		this.validateComponentState();
	}

	private void loadList() {
		ArrayList<CompteCourant> listeCpt;
		listeCpt = this.cmDialogController.getComptesDunClient();
		this.oListCompteCourant.clear();
		this.oListCompteCourant.addAll(listeCpt);
	}

	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnVoirOpes.setDisable(true);
		this.btnModifierCompte.setDisable(true);
		this.btnSupprCompte.setDisable(true);
		if(ConstantesIHM.estInactif(clientDesComptes)) {
			this.btnAjouterCompte.setDisable(true);
		}

		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		CompteCourant compte = this.oListCompteCourant.get(selectedIndice);

		if (selectedIndice >= 0) {
			this.btnVoirOpes.setDisable(false);
			if(!ConstantesIHM.estCloture(compte) && !ConstantesIHM.estInactif(clientDesComptes)) {
				this.btnModifierCompte.setDisable(false);
				this.btnSupprCompte.setDisable(false);
			}
		}
	}
}