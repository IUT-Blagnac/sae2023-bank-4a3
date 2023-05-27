package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.PrelevementManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Prelevement;
import model.orm.Access_BD_Prelevement;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Classe responsable de la gestion de la fenêtre de gestion des prélèvements
 * automatiques dans l'application DailyBank.
 * 
 * @see PrelevementManagementController
 * @see Access_BD_Prelevement
 * @author LAMOUR Evan
 */
public class PrelevementManagement {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private PrelevementManagementController pmcViewController;

	/**
	 * Constructeur de la classe PrelevementsMangement.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @author LAMOUR Evan
	 */
	public PrelevementManagement(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(
					PrelevementManagementController.class.getResource("prelevementManagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des clients");
			this.primaryStage.setResizable(false);

			this.pmcViewController = loader.getController();
			this.pmcViewController.initContext(this.primaryStage, this, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des employés.
	 * 
	 * @author LAMOUR Evan
	 */
	public void doPrelevementManagementDialog() {
		this.pmcViewController.displayDialog();
	}

	/**
	 * Crée un nouveau prélèvement.
	 *
	 * @return Le prélèvement créé
	 * @author LAMOUR Evan
	 */
	public Prelevement nouveauPrelevement() {
		Prelevement pm;
		PrelevementEditorPane pep = new PrelevementEditorPane(this.primaryStage, this.dailyBankState);
		pm = pep.doPrelevementEditorDialog(null, EditionMode.CREATION);
		if (pm != null) {
			try {
				Access_BD_Prelevement ap = new Access_BD_Prelevement();

				ap.insertPrelevement(pm);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				pm = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				pm = null;
			}
		}
		return pm;
	}
}