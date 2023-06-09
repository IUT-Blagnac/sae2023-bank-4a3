package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.EmployesManagement;
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
import model.data.Employe;

/**
 * Contrôleur pour la fenêtre de gestion des Employés.
 * 
 * @see EmployesManagement
 * @author DIDENKO Andrii
 * @author KRILL Maxence
 * @author SHULSHINA Daria
 */
public class EmployesManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à EmployesManagementController
	private EmployesManagement emDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private ObservableList<Employe> oListEmployes;

	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _cm              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 * @author KRILL Maxence
	 */
	// Manipulation de la fenêtre
	public void initContext(Stage _containingStage, EmployesManagement _cm, DailyBankState _dbstate) {
		this.emDialogController = _cm;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Configure la fenêtre de gestion des Employés
	 * 
	 * @author KRILL Maxence
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListEmployes = FXCollections.observableArrayList();
		this.lvEmployes.setItems(this.oListEmployes);
		this.lvEmployes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvEmployes.getFocusModel().focus(-1);
		this.lvEmployes.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
		this.validateComponentState();
		this.doRechercher();
	}

	/**
	 * Affiche la fenêtre de gestion des Employés
	 * 
	 * @author KRILL Maxence
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	// Gestion du stage

	/**
	 * Ferme la fenêtre.
	 * 
	 * @param e L'événement de fermeture
	 * @return Object null
	 * @author KRILL Maxence
	 */
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
	private ListView<Employe> lvEmployes;
	@FXML
	private Button btnDesactEmploye;
	@FXML
	private Button btnModifEmploye;

	/**
	 * Ferme la fenêtre de gestion des Employés (bouton FXML).
	 * 
	 * @author KRILL Maxence
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Recherche les employés en fonction des critères de recherche (bouton FXML).
	 * 
	 * @author KRILL Maxence
	 */
	@FXML
	private void doRechercher() {
		if (!ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel()))
			return;
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

		// Recherche des employés en BD. cf. AccessEmploye > getEmployes(.)
		// numCompte != -1 => recherche sur numCompte
		// numCompte != -1 et debutNom non vide => recherche nom/prenom
		// numCompte != -1 et debutNom vide => recherche tous les employés
		ArrayList<Employe> listeEmp;
		listeEmp = this.emDialogController.getlisteEmployes(numCompte, debutNom, debutPrenom);

		this.oListEmployes.clear();
		this.oListEmployes.addAll(listeEmp);
		this.validateComponentState();
	}

	/**
	 * Crée un nouvel employé (bouton FXML).
	 * 
	 * @author DIDENKO Andrii
	 * @author SHULSHINA Daria
	 */
	@FXML
	private void doNouveauEmploye() {
		if (!ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel()))
			return;
		Employe employe;
		employe = this.emDialogController.nouveauEmploye();
		if (employe != null) {
			this.oListEmployes.add(employe);
		}
	}

	/**
	 * Modifie un employé (bouton FXML).
	 * 
	 * @author KRILL Maxence
	 */
	@FXML
	private void doModifierEmploye() {
		if (!ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel()))
			return;
		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Employe cliMod = this.oListEmployes.get(selectedIndice);
			Employe result = this.emDialogController.modifierEmploye(cliMod);
			if (result != null) {
				this.oListEmployes.set(selectedIndice, result);
			}
		}
	}

	/**
	 * Supprime un employé (bouton FXML).
	 * 
	 * @author DIDENKO Andrii
	 * @author SHULSHINA Daria
	 */
	@FXML
	private void doSupprimerEmploye() {
		if (!ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel()))
			return;
		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Employe empMod = this.oListEmployes.get(selectedIndice);
			String permission;
			if (empMod.droitsAccess == ConstantesIHM.AGENCE_CHEF) {
				permission = "Chef d'agence";
			} else {
				permission = "Guichetier";
			}
			if (!AlertUtilities.confirmYesCancel(this.primaryStage, "Supprimer employé",
					"Êtes-vous sûr de vouloir supprimer cet employé ?",
					"Il ne sera pas possible de restaurer cet employé, cependant, vous pourrez le recréer.\n\nEmployé :\nID : "
							+ empMod.idEmploye + "\nNom : " + empMod.nom + "\nPrénom : " + empMod.prenom + "\nDroits : "
							+ permission,
					AlertType.CONFIRMATION))
				return;
			this.emDialogController.supprimerEmploye(empMod);
			this.oListEmployes.remove(selectedIndice);
		}
		this.validateComponentState();
	}

	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnDesactEmploye.setDisable(true);
		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifEmploye.setDisable(false);
			this.btnDesactEmploye.setDisable(false);
		} else {
			this.btnModifEmploye.setDisable(true);
			this.btnDesactEmploye.setDisable(true);
		}
	}
}