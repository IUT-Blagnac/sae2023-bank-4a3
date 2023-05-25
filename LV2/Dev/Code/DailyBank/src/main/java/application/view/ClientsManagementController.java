package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.ClientsManagement;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;

public class ClientsManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ClientsManagementController
	private ClientsManagement cmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private ObservableList<Client> oListClients;

	// Manipulation de la fenêtre
	public void initContext(Stage _containingStage, ClientsManagement _cm, DailyBankState _dbstate) {
		this.cmDialogController = _cm;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListClients = FXCollections.observableArrayList();
		this.lvClients.setItems(this.oListClients);
		this.lvClients.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvClients.getFocusModel().focus(-1);
		this.lvClients.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
		this.validateComponentState();
		this.doRechercher();
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
	private TextField txtNum;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private ListView<Client> lvClients;
	@FXML
	private Button btnDesactClient;
	@FXML
	private Button btnModifClient;
	@FXML
	private Button btnComptesClient;

	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	@FXML
	private void doRechercher() {
		int numCompte;
		try {
			String nc = this.txtNum.getText();
			if (nc.equals("")) {
				numCompte = -1;
			} else {
				numCompte = Integer.parseInt(nc);
				if (numCompte < 0) {
					this.txtNum.setText("");
					numCompte = -1;
				}
			}
		} catch (NumberFormatException nfe) {
			this.txtNum.setText("");
			numCompte = -1;
		}

		String debutNom = this.txtNom.getText();
		String debutPrenom = this.txtPrenom.getText();

		if (numCompte != -1) {
			this.txtNom.setText("");
			this.txtPrenom.setText("");
		} else {
			if (debutNom.equals("") && !debutPrenom.equals("")) {
				this.txtPrenom.setText("");
			}
		}

		// Recherche des clients en BD. cf. AccessClient > getClients(.)
		// numCompte != -1 => recherche sur numCompte
		// numCompte != -1 et debutNom non vide => recherche nom/prenom
		// numCompte != -1 et debutNom vide => recherche tous les clients
		ArrayList<Client> listeCli;
		listeCli = this.cmDialogController.getlisteComptes(numCompte, debutNom, debutPrenom);

		this.oListClients.clear();
		this.oListClients.addAll(listeCli);
		this.validateComponentState();
	}

	@FXML
	private void doComptesClient() {
		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Client client = this.oListClients.get(selectedIndice);
			this.cmDialogController.gererComptesClient(client);
		}
		this.validateComponentState();
	}
	
	@FXML
	private void doNouveauClient() {
		Client client;
		client = this.cmDialogController.nouveauClient();
		if (client != null) {
			this.oListClients.add(client);
		}
		this.validateComponentState();
	}

	@FXML
	private void doModifierClient() {
		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Client cliMod = this.oListClients.get(selectedIndice);
			Client result = this.cmDialogController.modifierClient(cliMod);
			if (result != null) {
				this.oListClients.set(selectedIndice, result);
			}
		}
		this.validateComponentState();
	}

	@FXML
	private void doDesactiverClient() {
		if(!ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) return;
		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Client cliMod = this.oListClients.get(selectedIndice);
			int comptesOuverts = this.cmDialogController.verifierCloturer(cliMod);
			if (comptesOuverts == -1) {
				AlertUtilities.showAlert(this.primaryStage, "Client inactif - Erreur", "Une erreur est survenue", "Impossible d'obtenir le nombre de comptes ouverts du client.", AlertType.WARNING);
				return;
			} else if(comptesOuverts != 0) {
				AlertUtilities.showAlert(this.primaryStage, "Client inactif - Erreur", "Impossible de rendre inactif ce client", "Merci de vous assurer que tout les comptes de ce client soient clôturés avant de le rendre inactif.\nCe client a actuellement " + comptesOuverts + " compte(s) ouverts.", AlertType.WARNING);
				return;
			}
			if(!AlertUtilities.confirmYesCancel(this.primaryStage, "Désactiver client", "Êtes-vous sûr de vouloir désactiver ce client ?", "Tout client désactivé ne peut pas être réactivé.\n\nClient :\nID : " + cliMod.idNumCli + "\nNom : " + cliMod.nom + "\nPrénom : " + cliMod.prenom + "\nAdresse postale : " + cliMod.adressePostale + "\nEmail : " + cliMod.email + "\nTéléphone : " + cliMod.telephone, AlertType.CONFIRMATION)) return;
			Client result = this.cmDialogController.clientInactif(cliMod);
			if (result != null) {
				this.oListClients.set(selectedIndice, result);
			}
		}
		this.validateComponentState();
	}

	private void validateComponentState() {
		// Non implémenté => désactivé
		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();

		if (selectedIndice >= 0) {
			this.btnDesactClient.setDisable(true);
			this.btnComptesClient.setDisable(false);
			if(ConstantesIHM.estActif(this.oListClients.get(selectedIndice))) {
				this.btnModifClient.setDisable(false);
				if(ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
					this.btnDesactClient.setDisable(false);
				}
			} else {
				this.btnModifClient.setDisable(true);
			}
		} else {
			this.btnModifClient.setDisable(true);
			this.btnComptesClient.setDisable(true);
			this.btnDesactClient.setDisable(true);
		}
	}
}