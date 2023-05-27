package application.view;

import application.DailyBankState;
import application.control.DailyBankMainFrame;
import application.tools.AlertUtilities;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.AgenceBancaire;
import model.data.Employe;

/**
 * Contrôleur pour la fenêtre principale de l'application.
 * 
 * @see DailyBankMainFrame
 * @author IUT Blagnac
 * @author LAMOUR Evan
 */
public class DailyBankMainFrameController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à DailyBankMainFrameController
	private DailyBankMainFrame dbmfDialogController;

	// Fenêtre physique ou est la scène contenant le fichier FXML contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre

	// Manipulation de la fenêtre

	/**
	 * Initialisation du contrôleur de vue DailyBankMainFrameController.
	 *
	 * @param _containingStage Stage qui contient le fichier FXML contrôlé par
	 *                         DailyBankMainFrameController
	 * @param _dbmf            Contrôleur de Dialogue qui réalisera les opérations
	 *                         de navigation ou calcul
	 * @param _dbstate         Etat courant de l'application
	 * @author IUT Blagnac
	 */
	public void initContext(Stage _containingStage, DailyBankMainFrame _dbmf, DailyBankState _dbstate) {
		this.dbmfDialogController = _dbmf;
		this.dailyBankState = _dbstate;
		this.primaryStage = _containingStage;
		this.configure();
		this.validateComponentState();
	}

	/**
	 * Affichage de la fenêtre.
	 * 
	 * @author IUT Blagnac
	 */
	public void displayDialog() {
		this.primaryStage.show();
	}

	/**
	 * Configuration de DailyBankMainFrameController. Fermeture par la croix,
	 * bindings des boutons connexion/déconnexion.
	 * 
	 * @author IUT Blagnac
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
		this.btnConn.managedProperty().bind(this.btnConn.visibleProperty());
		this.btnDeconn.managedProperty().bind(this.btnDeconn.visibleProperty());
	}

	/**
	 * Ferme la fenêtre.
	 *
	 * @param e L'événement de fermeture
	 * @return Object null
	 * @author IUT Blagnac
	 */
	private Object closeWindow(WindowEvent e) {
		this.doQuit();
		e.consume();
		return null;
	}

	// Attributs de la scene
	@FXML
	private Label lblAg;
	@FXML
	private Label lblAdrAg;
	@FXML
	private Label lblEmpNom;
	@FXML
	private Label lblEmpPrenom;
	@FXML
	private MenuItem mitemClient;
	@FXML
	private MenuItem mitemEmploye;
	@FXML
	private MenuItem mitemConnexion;
	@FXML
	private MenuItem mitemDeConnexion;
	@FXML
	private MenuItem mitemQuitter;
	@FXML
	private MenuItem mitememprunt;
	@FXML
	private MenuItem mitemPrelevement;
	@FXML
	private Button btnConn;
	@FXML
	private Button btnDeconn;

	// Actions

	/**
	 * Action menu quitter. Demander une confirmation puis fermer la fenêtre (donc
	 * l'application car fenêtre principale).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doQuit() {

		if (AlertUtilities.confirmYesCancel(this.primaryStage, "Quitter l'application",
				"Etes vous sur de vouloir quitter l'appli ?", null, AlertType.CONFIRMATION)) {
			this.quitterBD();
			this.primaryStage.close();
		}
	}

	/**
	 * Affichage d'une alerte avec information.
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doActionAide() {
		String contenu = "DailyBank v2\nSAE 2.01 Développement\nIUT-Blagnac\n\nAuteurs : TDA3\n- DIDENKO Andrii\n- LAMOUR Evan\n- KRILL Maxence\n- SHULSHINA Daria";
		AlertUtilities.showAlert(this.primaryStage, "Aide", "A propos de cette application :", contenu,
				AlertType.INFORMATION);
	}

	/**
	 * Demande au contrôleur de dialogue de lancer le login puis met à jour la
	 * fenêtre.
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doLogin() {
		this.dbmfDialogController.loginDunEmploye();
		this.validateComponentState();
	}

	/**
	 * Demande au contrôleur de dialogue de réaliser la déconnexion puis met à jour
	 * la fenêtre.
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doDisconnect() {
		this.dbmfDialogController.deconnexionEmploye();
		this.validateComponentState();
	}

	/**
	 * Demande au contrôleur de dialogue d'ouvrir la fenêtre de simulation
	 * d'emprunt.
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doSimulerEmprunt() {
		this.dbmfDialogController.simulerEmprunt();
		this.validateComponentState();
	}

	/**
	 * Les champs d'affichage de la banque et de l'employé sont mis à jour ainsi que
	 * les boutons de connexion/déconnexion .
	 * - Si un employé est connecté : les champs sont remplis et les boutons/menus
	 * activés, sauf connexion.
	 * - Si aucun employé n'est connecté : les champs sont vidés et les
	 * boutons/menus désactivés, sauf le bouton connexion.
	 * 
	 * @author IUT Blagnac
	 */
	private void validateComponentState() {
		Employe e = this.dailyBankState.getEmployeActuel();
		AgenceBancaire a = this.dailyBankState.getAgenceActuelle();
		if (e != null && a != null) {
			this.lblAg.setText(a.nomAg);
			this.lblAdrAg.setText(a.adressePostaleAg);
			this.lblEmpNom.setText(e.nom);
			this.lblEmpPrenom.setText(e.prenom);
			if (this.dailyBankState.isChefDAgence()) {
				this.mitemEmploye.setDisable(false);
				this.mitememprunt.setDisable(false);
			} else {
				this.mitemEmploye.setDisable(true);
				this.mitememprunt.setDisable(true);
			}
			this.mitemClient.setDisable(false);
			this.mitemConnexion.setDisable(true);
			this.mitemDeConnexion.setDisable(false);
			this.mitemPrelevement.setDisable(false);
			this.btnConn.setVisible(false);
			this.btnDeconn.setVisible(true);
		} else {
			this.lblAg.setText("");
			this.lblAdrAg.setText("");
			this.lblEmpNom.setText("");
			this.lblEmpPrenom.setText("");

			this.mitemClient.setDisable(true);
			this.mitemEmploye.setDisable(true);
			this.mitememprunt.setDisable(true);
			this.mitemConnexion.setDisable(false);
			this.mitemDeConnexion.setDisable(true);
			this.mitemPrelevement.setDisable(true);
			this.btnConn.setVisible(true);
			this.btnDeconn.setVisible(false);
		}
	}

	/**
	 * Demande au contrôleur de dialogue de lancer la gestion des clients.
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doClientOption() {
		this.dbmfDialogController.gestionClients();
	}

	/**
	 * Demande au contrôleur de dialogue de lancer la gestion des employés.
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doEmployeOption() {
		this.dbmfDialogController.gestionEmployes();
	}

	/**
	 * Demande au contrôleur de dialogue de lancer la gestion des emprunts.
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doPrelevementOption() {
		this.dbmfDialogController.gestionPrelevement();
	}

	/**
	 * Demande au contrôleur de dialogue de se déconnecter de la base de données
	 * Oracle.
	 * 
	 * @author IUT Blagnac
	 */
	private void quitterBD() {
		this.dbmfDialogController.deconnexionEmploye();
	}
}