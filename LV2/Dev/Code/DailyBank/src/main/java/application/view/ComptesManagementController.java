package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.ComptesManagement;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;

/**
 * Contrôleur pour la fenêtre de gestion des Comptes.
 * 
 * @see ComptesManagement
 * @author IUT Blagnac
 * @author LAMOUR Evan
 */
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

	/**
	 * Initialise la fenêtre de gestion des Comptes
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _cm              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 * @param client           Le client dont on veut gérer les comptes
	 * @author IUT Blagnac
	 */
	// Manipulation de la fenêtre
	public void initContext(Stage _containingStage, ComptesManagement _cm, DailyBankState _dbstate, Client client) {
		this.cmDialogController = _cm;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.clientDesComptes = client;
		this.configure();
	}

	/**
	 * Configure la fenêtre de gestion des Comptes
	 * 
	 * @author IUT Blagnac
	 */
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

	/**
	 * Affiche la fenêtre de gestion des Comptes
	 * 
	 * @author IUT Blagnac
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	/**
	 * Ferme la fenêtre.
	 * 
	 * @param e L'événement de fermeture
	 * @return Object null
	 * @author IUT Blagnac
	 */
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

	/**
	 * Ferme la fenêtre de gestion des Comptes (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Ouvre la fenêtre d'affichage des opérations du compte (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
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

	/**
	 * Ouvre la fenêtre de création d'un nouveau compte (bouton FXML).
	 * 
	 * @author LAMOUR Evan
	 */
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

	/**
	 * Ouvre la fenêtre de recherche de comptes (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
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

	/**
	 * Ouvre la fenêtre de recherche de comptes (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doSupprimerCompte() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			if (cpt.solde == 0) {
				if (!AlertUtilities.confirmYesCancel(this.primaryStage, "Clôturer compte",
						"Êtes-vous sûr de vouloir clôturer ce compte ?",
						"Il ne sera pas possible de rouvrir ce compte.", AlertType.CONFIRMATION))
					return;
				this.cmDialogController.cloturerCompte(cpt);
			} else {
				AlertUtilities.showAlert(this.primaryStage, "Clôturer compte", "Impossible de clôturer ce compte",
						"Afin de clôturer ce compte, merci de vous assurer que le solde soit bien égal à 0.\n\nSolde actuel : "
								+ cpt.solde + " €",
						AlertType.WARNING);
			}
		}
		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Affiche la liste des comptes du client.
	 * 
	 * @author IUT Blagnac
	 */
	private void loadList() {
		ArrayList<CompteCourant> listeCpt;
		listeCpt = this.cmDialogController.getComptesDunClient();
		this.oListCompteCourant.clear();
		this.oListCompteCourant.addAll(listeCpt);
	}

	/**
	 * Active/Désactie les boutons en fonction de l'état du compte selectionné
	 * 
	 * @author IUT Blagnac
	 */
	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnVoirOpes.setDisable(true);
		this.btnModifierCompte.setDisable(true);
		this.btnSupprCompte.setDisable(true);
		if (ConstantesIHM.estInactif(clientDesComptes)) {
			this.btnAjouterCompte.setDisable(true);
		}

		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		
		if (selectedIndice >= 0) {
			CompteCourant compte = this.oListCompteCourant.get(selectedIndice);
			this.btnVoirOpes.setDisable(false);
			if (!ConstantesIHM.estCloture(compte) && !ConstantesIHM.estInactif(clientDesComptes)) {
				this.btnModifierCompte.setDisable(false);
				this.btnSupprCompte.setDisable(false);
			}
		}
	}
}