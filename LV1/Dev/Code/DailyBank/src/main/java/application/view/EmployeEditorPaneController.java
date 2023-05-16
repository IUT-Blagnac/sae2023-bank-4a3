package application.view;

import java.util.regex.Pattern;

import application.DailyBankState;
import application.control.ExceptionDialog;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.Order;
import model.orm.exception.Table;

/**
 * Contrôleur pour l'édition des informations employé dans une fenêtre.
 */
public class EmployeEditorPaneController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre

	private Employe employeEdite;
	private EditionMode editionMode;
	private Employe employeResultat;

	// Manipulation de la fenêtre

	/**
     * Initialise le contexte du contrôleur.
     * @param _containingStage le stage contenant la scène
     * @param _dbstate l'état courant de l'application
     */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	/**
     * Affiche la boîte de dialogue d'édition des informations employé.
     * @param employe L'employé à éditer (null pour une création)
     * @param mode Le mode d'édition (création, modification, suppression)
     * @return L'employé résultat après l'édition, ou null si aucune édition n'a été effectuée
     */
	public Employe displayDialog(Employe employe, EditionMode mode) {

		this.editionMode = mode;
		if (employe == null) {
			this.employeEdite = new Employe(0, "", "", "", "", "", "N", this.dailyBankState.getEmployeActuel().idAg);
		} else {
			this.employeEdite = new Employe(employe);
		}
		this.employeResultat = null;
		switch (mode) {
		case CREATION:
			this.txtIdcli.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			this.txtTel.setDisable(false);
			this.txtMail.setDisable(false);
			this.rbActif.setSelected(true);
			this.rbInactif.setSelected(false);
			if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
				this.rbActif.setDisable(false);
				this.rbInactif.setDisable(false);
			} else {
				this.rbActif.setDisable(true);
				this.rbInactif.setDisable(true);
			}
			this.lblMessage.setText("Informations sur le nouveau employé");
			this.butOk.setText("Ajouter");
			this.butCancel.setText("Annuler");
			break;
		case MODIFICATION:
			this.txtIdcli.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			this.txtTel.setDisable(false);
			this.txtMail.setDisable(false);
			this.rbActif.setSelected(true);
			this.rbInactif.setSelected(false);
			if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
				this.rbActif.setDisable(false);
				this.rbInactif.setDisable(false);
			} else {
				this.rbActif.setDisable(true);
				this.rbInactif.setDisable(true);
			}
			this.lblMessage.setText("Informations employé");
			this.butOk.setText("Modifier");
			this.butCancel.setText("Annuler");
			break;
		case SUPPRESSION:
			ApplicationException ae = new ApplicationException(Table.NONE, Order.OTHER, "SUPPRESSION EMPLOYE NON PREVUE",
					null);
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();

			break;
		}
		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}
		// initialisation du contenu des champs
		this.txtIdcli.setText("" + this.employeEdite.idNumCli);
		this.txtNom.setText(this.employeEdite.nom);
		this.txtPrenom.setText(this.employeEdite.prenom);
		this.txtAdr.setText(this.employeEdite.adressePostale);
		this.txtMail.setText(this.employeEdite.email);
		this.txtTel.setText(this.employeEdite.telephone);

		if (ConstantesIHM.estInactif(this.employeEdite)) {
			this.rbInactif.setSelected(true);
		} else {
			this.rbInactif.setSelected(false);
		}

		this.employeResultat = null;

		this.primaryStage.showAndWait();
		return this.employeResultat;
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblMessage;
	@FXML
	private TextField txtIdcli;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private TextField txtAdr;
	@FXML
	private TextField txtTel;
	@FXML
	private TextField txtMail;
	@FXML
	private RadioButton rbActif;
	@FXML
	private RadioButton rbInactif;
	@FXML
	private ToggleGroup actifInactif;
	@FXML
	private Button butOk;
	@FXML
	private Button butCancel;

	/**
     * Action associée au bouton Annuler.
     */
	@FXML
	private void doCancel() {
		this.employeResultat = null;
		this.primaryStage.close();
	}

	/**
     * Action associée au bouton Ajouter/Modifier/Supprimer.
     */
	@FXML
	private void doAjouter() {
		switch (this.editionMode) {
		case CREATION:
			if (this.isSaisieValide()) {
				this.employeResultat = this.employeEdite;
				this.primaryStage.close();
			}
			break;
		case MODIFICATION:
			if (this.isSaisieValide()) {
				this.employeResultat = this.employeEdite;
				this.primaryStage.close();
			}
			break;
		case SUPPRESSION:
			this.employeResultat = this.employeEdite;
			this.primaryStage.close();
			break;
		}

	}

	/**
     * Vérifie la validité de la saisie des informations employé.
     * @return true si la saisie est valide, false sinon
     */
	private boolean isSaisieValide() {
		this.employeEdite.nom = this.txtNom.getText().trim();
		this.employeEdite.prenom = this.txtPrenom.getText().trim();
		this.employeEdite.adressePostale = this.txtAdr.getText().trim();
		this.employeEdite.telephone = this.txtTel.getText().trim();
		this.employeEdite.email = this.txtMail.getText().trim();
		if (this.rbActif.isSelected()) {
			this.employeEdite.estInactif = ConstantesIHM.EMPLOYE_ACTIF;
		} else {
			this.employeEdite.estInactif = ConstantesIHM.EMPLOYE_INACTIF;
		}

		if (this.employeEdite.nom.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le nom ne doit pas être vide",
					AlertType.WARNING);
			this.txtNom.requestFocus();
			return false;
		}
		if (this.employeEdite.prenom.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le prénom ne doit pas être vide",
					AlertType.WARNING);
			this.txtPrenom.requestFocus();
			return false;
		}

		String regex = "(0)[1-9][0-9]{8}";
		if (!Pattern.matches(regex, this.employeEdite.telephone) || this.employeEdite.telephone.length() > 10) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le téléphone n'est pas valable",
					AlertType.WARNING);
			this.txtTel.requestFocus();
			return false;
		}
		regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		if (!Pattern.matches(regex, this.employeEdite.email) || this.employeEdite.email.length() > 20) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le mail n'est pas valable",
					AlertType.WARNING);
			this.txtMail.requestFocus();
			return false;
		}

		return true;
	}
}
