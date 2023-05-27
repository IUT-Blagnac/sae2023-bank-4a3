package application.view;

import application.DailyBankState;
import application.control.LoginDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Employe;

/**
 * Controller JavaFX de la view LoginDialog.
 * 
 * @see LoginDialog
 * @author IUT Blagnac
 */
public class LoginDialogController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à LoginDialogController
	private LoginDialog ldDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre

	// Manipulation de la fenêtre

	/**
	 * Initialisation du contrôleur de vue LoginDialogController.
	 *
	 * @param _containingStage Stage qui contient le fichier xml contrôlé par
	 *                         LoginDialogController
	 * @param _ld              Contrôleur de Dialogue qui réalisera les opérations
	 *                         de navigation ou calcul
	 * @param _dbstate         Etat courant de l'application
	 * @author IUT Blagnac
	 */
	public void initContext(Stage _containingStage, LoginDialog _ld, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
		this.ldDialogController = _ld;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Affichage de la fenêtre.
	 * 
	 * @author IUT Blagnac
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	/**
	 * Configuration de LoginDialogController. Fermeture par la croix.
	 * 
	 * @author IUT Blagnac
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
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

	// Attributs de la scene
	@FXML
	private TextField txtLogin;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Label lblMessage;

	// Actions

	/**
	 * Action quitter. Annuler le login et fermer la fenêtre (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doCancel() {
		this.ldDialogController.annulerLogin();
		this.primaryStage.close();
	}

	/**
	 * Action login.
	 * Vérifier que login/password non vides. Lancer la recherche par le contrôleur
	 * de dialogue. Si employé trouvé : fermer la fenêtre (sinon continuer)
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doOK() {
		String login = this.txtLogin.getText().trim();
		String password = new String(this.txtPassword.getText().trim());
		if (login.length() == 0 || password.length() == 0) {
			this.afficheErreur("Identifiants incorrects");
		} else {
			Employe e;
			e = this.ldDialogController.chercherEmployeParLogin(login, password);
			if (e == null) {
				this.afficheErreur("Identifiants incorrects");
			} else {
				this.primaryStage.close();
			}
		}
	}

	/**
	 * Affichage d'un message d'erreur
	 *
	 * @param texte Texte à afficher
	 * @author IUT Blagnac
	 */
	private void afficheErreur(String texte) {
		this.lblMessage.setText(texte);
		this.lblMessage.setStyle("-fx-text-fill:red; -fx-font-weight: bold;");
	}
}