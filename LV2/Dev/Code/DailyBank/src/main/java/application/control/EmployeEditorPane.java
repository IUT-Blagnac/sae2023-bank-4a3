package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.EmployeEditorPaneController;
import application.view.EmployesManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Employe;

/**
 * Classe responsable de la gestion de la fenêtre d'édition d'un employé dans l'application DailyBank.
 */
public class EmployeEditorPane {

	private Stage primaryStage;
	private EmployeEditorPaneController cepcViewController;
	private DailyBankState dailyBankState;

	/**
     * Constructeur de la classe EmployeEditorPane.
     *
     * @param _parentStage Fenêtre parente
     * @param _dbstate État courant de l'application
     */
	public EmployeEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmployesManagementController.class.getResource("employeeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion d'un employé");
			this.primaryStage.setResizable(false);

			this.cepcViewController = loader.getController();
			this.cepcViewController.initContext(this.primaryStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * Affiche la boîte de dialogue d'édition du employé.
     *
     * @param employe L'employé à éditer
     * @param em Le mode d'édition (ajout ou modification)
     * @return L'employé modifié
     */
	public Employe doEmployeEditorDialog(Employe employe, EditionMode em) {
		return this.cepcViewController.displayDialog(employe, em);
	}
}
