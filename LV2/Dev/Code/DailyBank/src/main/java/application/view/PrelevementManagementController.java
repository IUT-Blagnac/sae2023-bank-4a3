package application.view;

import application.DailyBankState;
import application.control.PrelevementManagement;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Prelevement;

/**
 * Contrôleur pour la gestion des prélèvements automatiques.
 * 
 * @see PrelevementManagement
 * @author LAMOUR Evan
 */
public class PrelevementManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ClientsManagementController
	private PrelevementManagement pmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _pm              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 * @author LAMOUR Evan
	 */
	public void initContext(Stage _containingStage, PrelevementManagement _pm, DailyBankState _dbstate) {
		this.pmDialogController = _pm;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Configure les différents éléments de la fenêtre.
	 * 
	 * @author LAMOUR Evan
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
		this.validateComponentState();
	}

	/**
	 * Affiche la boîte de dialogue de gestion des prélèvements.
	 * 
	 * @author LAMOUR Evan
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
	 * @author LAMOUR Evan
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	/**
	 * Ferme la fenêtre (bouton FXML).
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Ouvre la fenêtre d'édition d'un prélèvement (bouton FXML).
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doNouveauPrelevement() {
		Prelevement pm = this.pmDialogController.nouveauPrelevement();
	}

	/**
	 * Actualise l'état des composants de la fenêtre.
	 * 
	 * @author KRILL Maxence
	 */
	private void validateComponentState() {

	}
}