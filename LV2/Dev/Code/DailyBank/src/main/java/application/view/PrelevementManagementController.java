package application.view;

import application.DailyBankState;
import application.control.PrelevementManagement;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Prelevement;

public class PrelevementManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;
	
	// Contrôleur de Dialogue associé à ClientsManagementController
	private PrelevementManagement pmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	public void initContext(Stage _containingStage, PrelevementManagement _pm, DailyBankState _dbstate) {
		this.pmDialogController = _pm;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
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

	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}
	
	@FXML
	private void doNouveauPrelevement() {
		Prelevement pm;
		pm = this.pmDialogController.nouveauPrelevement();
	}

	private void validateComponentState() {

	}
}