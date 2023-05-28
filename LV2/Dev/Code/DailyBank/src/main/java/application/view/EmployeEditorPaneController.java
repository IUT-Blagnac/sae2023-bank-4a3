package application.view;

import java.util.regex.Pattern;

import application.DailyBankState;
import application.control.EmployeEditorPane;
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

/**
 * Contrôleur pour l'édition des informations employé dans une fenêtre.
 * 
 * @see EmployeEditorPane
 * @author DIDENKO Andrii
 * @author KRILL Maxence
 * @author SHULSHINA Daria
 */
public class EmployeEditorPaneController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à EmployesManagementController
	private EmployeEditorPane emDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre

	private Employe employeEdite;
	private EditionMode editionMode;
	private Employe employeResultat;

	// Manipulation de la fenêtre

	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _em              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 * @author DIDENKO Andrii
	 * @author SHULSHINA Daria
	 */
	public void initContext(Stage _containingStage, EmployeEditorPane _em, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
		this.emDialogController = _em;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	/**
	 * Affiche la boîte de dialogue d'édition des informations employé.
	 * 
	 * @param employe L'employé à éditer (null pour une création)
	 * @param mode    Le mode d'édition (création, modification, suppression)
	 * @return L'employé résultat après l'édition, ou null si aucune édition n'a été
	 *         effectuée
	 * @author DIDENKO Andrii
	 * @author SHULSHINA Daria
	 */
	public Employe displayDialog(Employe employe, EditionMode mode) {

		this.editionMode = mode;
		if (employe == null) {
			this.employeEdite = new Employe(0, "", "", "", "", "", this.dailyBankState.getEmployeActuel().idAg);
		} else {
			this.employeEdite = new Employe(employe);
		}
		this.employeResultat = null;
		switch (mode) {
			case CREATION:
				this.txtIdemp.setDisable(true);
				this.txtNom.setDisable(false);
				this.txtPrenom.setDisable(false);
				this.txtLogin.setDisable(false);
				this.txtMdp.setDisable(false);
				this.lblMessage.setText("Informations sur le nouvel employé");
				this.butOk.setText("Ajouter");
				this.butCancel.setText("Annuler");
				break;
			case MODIFICATION:
				this.txtIdemp.setDisable(true);
				this.txtNom.setDisable(false);
				this.txtPrenom.setDisable(false);
				this.txtLogin.setDisable(false);
				this.txtMdp.setDisable(false);
				this.lblMessage.setText("Informations employé");
				this.butOk.setText("Modifier");
				this.butCancel.setText("Annuler");
				break;
			default:
				break;
		}
		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}
		// initialisation du contenu des champs
		this.txtIdemp.setText("" + this.employeEdite.idEmploye);
		if (mode == EditionMode.CREATION) {
			this.txtIdemp.setText("");
		}
		this.txtNom.setText(this.employeEdite.nom);
		this.txtPrenom.setText(this.employeEdite.prenom);
		this.txtLogin.setText(this.employeEdite.login);
		this.txtMdp.setText(this.employeEdite.motPasse);

		this.employeResultat = null;

		this.primaryStage.showAndWait();
		return this.employeResultat;
	}

	// Gestion du stage
	
	/**
	 * Ferme la fenêtre.
	 * 
	 * @param e L'événement de fermeture
	 * @return Object null
	 * @author DIDENKO Andrii
	 * @author SHULSHINA Daria
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblMessage;
	@FXML
	private TextField txtIdemp;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private TextField txtLogin;
	@FXML
	private TextField txtMdp;
	@FXML
	private RadioButton rbChefAgence;
	@FXML
	private RadioButton rbGuichetier;
	@FXML
	private ToggleGroup actifInactif;
	@FXML
	private Button butOk;
	@FXML
	private Button butCancel;

	/**
	 * Action associée au bouton Annuler (FXML).
	 * 
	 * @author DIDENKO Andrii
	 * @author SHULSHINA Daria
	 */
	@FXML
	private void doCancel() {
		this.employeResultat = null;
		this.primaryStage.close();
	}

	/**
	 * Action associée au bouton Ajouter/Modifier/Supprimer (FXML).
	 * 
	 * @author DIDENKO Andrii
	 * @author SHULSHINA Daria
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
	 * - Vérifie que les champs ne sont pas vides
	 * - Vérifie que le login et le mot de passe sont valides
	 * - Vérifie que le login n'est pas déjà utilisé
	 * - Vérifie que le mot de passe est assez fort
	 * 
	 * @return true si la saisie est valide, false sinon
	 * @author DIDENKO Andrii
	 * @author KRILL Maxence
	 * @author SHULSHINA Daria
	 */
	private boolean isSaisieValide() {
		this.employeEdite.nom = this.txtNom.getText().trim();
		this.employeEdite.prenom = this.txtPrenom.getText().trim();
		this.employeEdite.login = this.txtLogin.getText().trim();
		this.employeEdite.motPasse = this.txtMdp.getText().trim();

		if (this.rbChefAgence.isSelected()) {
			this.employeEdite.droitsAccess = ConstantesIHM.AGENCE_CHEF;
		} else {
			this.employeEdite.droitsAccess = ConstantesIHM.AGENCE_GUICHETIER;
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

		String regex = "^[a-zA-Z0-9!@#$%^&*()-_=+{}\\[\\]:\";'<>?,./]*$";
		if (!Pattern.matches(regex, this.employeEdite.login) || this.employeEdite.login.length() < 2) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", "Login invalide",
					"Le login doit :\n- Contenir au moins 2 caractères\n- Uniquement des lettres majuscules, minuscules, ou des caractères spéciaux parmis : !, @, #, $, %, ^, &, *, (, ), -, _, =, +, {, }, [, ], :, \", ;, ', <, >, ?, ,, ., /",
					AlertType.WARNING);
			this.txtMdp.requestFocus();
			return false;
		}

		if (this.emDialogController.verifierLogin(this.employeEdite) != 0) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", "Le login est déjà utilisé",
					"Merci de choisir un autre login.", AlertType.WARNING);
			this.txtLogin.requestFocus();
			return false;
		}

		regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+{}\\[\\]:\";'<>?,./])[a-zA-Z0-9!@#$%^&*()\\-_=+{}\\[\\]:\";'<>?,./]+$";
		if (!Pattern.matches(regex, this.employeEdite.motPasse) || this.employeEdite.motPasse.length() < 6) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", "Mot de passe invalide",
					"Le mot de passe doit :\n- Contenir au moins 6 caractères\n- Au moins une lettre majuscule\n- Au moins une lettre minuscule\n- Au moins un caractère spécial : !, @, #, $, %, ^, &, *, (, ), -, _, =, +, {, }, [, ], :, \", ;, ', <, >, ?, ,, ., /",
					AlertType.WARNING);
			this.txtMdp.requestFocus();
			return false;
		}
		return true;
	}
}