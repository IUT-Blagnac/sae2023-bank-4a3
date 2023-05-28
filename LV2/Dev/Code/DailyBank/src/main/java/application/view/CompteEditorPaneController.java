package application.view;

import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;

/**
 * Contrôleur pour l'édition des informations client dans une fenêtre.
 * 
 * @see ClientEditorPane
 * @author IUT Blagnac
 * @author LAMOUR Evan
 */
public class CompteEditorPaneController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private EditionMode editionMode;
	private Client clientDuCompte;
	private CompteCourant compteEdite;
	private CompteCourant compteResultat;

	/**
	 * Initialise le contrôleur de la fenêtre.
	 * 
	 * @param _containingStage
	 * @param _dbstate
	 */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Configure la fenêtre de gestion des Clients
	 * 
	 * @author IUT Blagnac
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.txtDecAutorise.focusedProperty().addListener((t, o, n) -> this.focusDecouvert(t, o, n));
		this.txtSolde.focusedProperty().addListener((t, o, n) -> this.focusSolde(t, o, n));
	}

	/**
	 * Affiche la boîte de dialogue d'édition des informations client.
	 * 
	 * @param client Le client à éditer (null pour une création)
	 * @param mode   Le mode d'édition
	 * @return Le client créé/modifié
	 * @author IUT Blagnac
	 * @author LAMOUR Evan
	 */
	public CompteCourant displayDialog(Client client, CompteCourant cpte, EditionMode mode) {
		this.clientDuCompte = client;
		this.editionMode = mode;
		if (cpte == null) {
			this.compteEdite = new CompteCourant(0, 100, 0, "N", this.clientDuCompte.idNumCli);
		} else {
			this.compteEdite = new CompteCourant(cpte);
		}
		this.compteResultat = null;
		this.txtIdclient.setDisable(true);
		this.txtIdAgence.setDisable(true);
		this.txtIdNumCompte.setDisable(true);
		switch (mode) {
			case CREATION:
				this.txtDecAutorise.setDisable(false);
				this.txtSolde.setDisable(false);
				this.lblMessage.setText("Informations sur le nouveau compte");
				this.lblSolde.setText("Solde (premier dépôt)");
				this.btnOk.setText("Ajouter");
				this.btnCancel.setText("Annuler");
				break;
			case MODIFICATION:
				this.txtDecAutorise.setDisable(false);
				this.txtSolde.setDisable(false);
				this.lblMessage.setText("Informations sur le compte à modifier");
				this.lblSolde.setText("Solde");
				this.txtSolde.setDisable(true);
				this.btnOk.setText("Modifier");
				this.btnCancel.setText("Annuler");
				break;
			default:
				break;

		}

		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}

		// initialisation du contenu des champs
		this.txtIdclient.setText("" + this.compteEdite.idNumCli);
		this.txtIdNumCompte.setText("" + this.compteEdite.idNumCompte);
		if (mode == EditionMode.CREATION) {
			this.txtIdNumCompte.setText("");
		}
		this.txtIdAgence.setText("" + this.dailyBankState.getEmployeActuel().idAg);
		this.txtDecAutorise.setText("" + this.compteEdite.debitAutorise);
		this.txtSolde.setText("" + this.compteEdite.solde);

		this.compteResultat = null;

		this.primaryStage.showAndWait();
		return this.compteResultat;
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

	/**
	 * Gestion du focus sur le champ txtDecAutorise.
	 * 
	 * @param txtField         Le champ en cours de saisie
	 * @param oldPropertyValue La valeur précédente du focus
	 * @param newPropertyValue La nouvelle valeur du focus
	 * @author IUT Blagnac
	 */
	private void focusDecouvert(ObservableValue<? extends Boolean> txtField, boolean oldPropertyValue,
			boolean newPropertyValue) {
		if (oldPropertyValue) {
			try {
				int val;
				val = Integer.parseInt(this.txtDecAutorise.getText().trim());
				this.compteEdite.debitAutorise = val;
			} catch (NumberFormatException nfe) {
				this.txtDecAutorise.setText("" + this.compteEdite.debitAutorise);
			}
		}
	}

	/**
	 * Gestion du focus sur le champ txtSolde.
	 * 
	 * @param txtField         Le champ en cours de saisie
	 * @param oldPropertyValue La valeur précédente du focus
	 * @param newPropertyValue La nouvelle valeur du focus
	 * @author IUT Blagnac
	 */
	private void focusSolde(ObservableValue<? extends Boolean> txtField, boolean oldPropertyValue,
			boolean newPropertyValue) {
		if (oldPropertyValue) {
			try {
				double val;
				val = Double.parseDouble(this.txtSolde.getText().trim());
				if (val < 0) {
					throw new NumberFormatException();
				}
				this.compteEdite.solde = val;
			} catch (NumberFormatException nfe) {
				this.txtSolde.setText("" + this.compteEdite.solde);
			}
		}
		this.txtSolde.setText("" + this.compteEdite.solde);
	}

	// Attributs de la scene + actions
	@FXML
	private Label lblMessage;
	@FXML
	private Label lblSolde;
	@FXML
	private TextField txtIdclient;
	@FXML
	private TextField txtIdAgence;
	@FXML
	private TextField txtIdNumCompte;
	@FXML
	private TextField txtDecAutorise;
	@FXML
	private TextField txtSolde;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnCancel;

	/**
	 * Gestion de l'appui sur le bouton Annuler (FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doCancel() {
		this.compteResultat = null;
		this.primaryStage.close();
	}

	@FXML
	private void doAjouter() {
		switch (this.editionMode) {
			case CREATION:
				if (this.isSaisieValide()) {
					this.compteResultat = this.compteEdite;
					this.primaryStage.close();
				}
				break;
			case MODIFICATION:
				if (this.isSaisieValide()) {
					this.compteResultat = this.compteEdite;
					this.primaryStage.close();
				}
				break;
			case SUPPRESSION:
				this.compteResultat = this.compteEdite;
				this.primaryStage.close();
				break;
		}
	}

	/**
	 * Vérifie que la saisie est valide.
	 * - Le solde du compte doit être supérieur au découvert autorisé
	 * 
	 * @return true si la saisie est valide, false sinon
	 * @author IUT Blagnac
	 */
	private boolean isSaisieValide() {
		if (this.compteEdite.solde < this.compteEdite.debitAutorise) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie",
					"Le solde du compte est inférieur au découvert autorisé",
					"Un compte ne peut pas être créé/édité si le solde est inférieur au découvert.\n\nSolde : "
							+ this.compteEdite.solde + " €\nDécouvert autorisé : -" + this.compteEdite.debitAutorise
							+ " €\n\nDifférence : -" + (0 - this.compteEdite.debitAutorise - this.compteEdite.solde)
							+ " €",
					AlertType.WARNING);
			return false;
		}

		if (this.compteEdite.solde < 50) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie",
					"Le solde du compte doit être supérieur à 50 €",
					"Un compte ne peut pas être créé/édité si le solde est inférieur à 50 €.",
					AlertType.WARNING);
			return false;
		}
		return true;
	}
}