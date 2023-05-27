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
import model.orm.Access_BD_Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Classe responsable de la gestion de la fenêtre d'édition d'un employé dans
 * l'application DailyBank.
 * 
 * @see EmployeEditorPaneController
 * @author KRILL Maxence
 */
public class EmployeEditorPane {

	private Stage primaryStage;
	private EmployeEditorPaneController cepcViewController;
	private DailyBankState dailyBankState;

	/**
	 * Constructeur de la classe EmployeEditorPane.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @author KRILL Maxence
	 */
	public EmployeEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(
					EmployesManagementController.class.getResource("employeeditorpane.fxml"));
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
			this.cepcViewController.initContext(this.primaryStage, this, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la boîte de dialogue d'édition du employé.
	 *
	 * @param employe L'employé à éditer
	 * @param em      Le mode d'édition (ajout ou modification)
	 * @return L'employé édité
	 * @author KRILL Maxence
	 */
	public Employe doEmployeEditorDialog(Employe employe, EditionMode em) {
		return this.cepcViewController.displayDialog(employe, em);
	}

	/**
	 * Vérifie que le login n'est pas déjà utilisé.
	 *
	 * @param empLog L'employé sur lequel effectuer la vérification
	 * @return 0 si le login n'est pas déjà utilisé, 1 si il est utilisé, -1 en cas
	 *         d'erreur
	 * @author KRILL Maxence
	 */
	public int verifierLogin(Employe empLog) {
		int loginUtilise = -1;
		if (empLog != null) {
			try {
				Access_BD_Employe ac = new Access_BD_Employe();
				if (ac.loginExist(empLog.login)) {
					loginUtilise = 0;
				} else {
					loginUtilise = 1;
				}
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
			}
		}
		return loginUtilise;
	}
}