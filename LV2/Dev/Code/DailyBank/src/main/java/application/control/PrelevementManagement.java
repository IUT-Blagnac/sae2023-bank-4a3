package application.control;

import java.util.ArrayList;

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

	/**
	 * Modifie un prélèvement.
	 *
	 * @param pr Le prélèvement à modifier
	 * @return Le prélèvement modifié
	 * @author KRILL Maxence
	 */
	public Prelevement modifierPrelevement(Prelevement pr) {
		Prelevement pm;
		PrelevementEditorPane pep = new PrelevementEditorPane(this.primaryStage, this.dailyBankState);
		pm = pep.doPrelevementEditorDialog(pr, EditionMode.MODIFICATION);
		if (pm != null) {
			try {
				Access_BD_Prelevement ap = new Access_BD_Prelevement();
				ap.modifierPrelevement(pm);
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

	/**
	 * Supprime un prélèvement.
	 *
	 * @param pr Le prélèvement à modifier
	 * @author KRILL Maxence
	 */
	public void supprimerPrelevement(Prelevement pr) {
		if (pr != null) {
			try {
				Access_BD_Prelevement ap = new Access_BD_Prelevement();
				ap.supprimerPrelevement(pr);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
			}
		}
	}

	/**
	 * Obtient la liste des clients en fonction des critères de recherche.
	 *
	 * @param _numCompte   Le numéro de compte (ou -1 pour tous les clients)
	 * @param _debutNom    Le début du nom du client
	 * @param _debutPrenom Le début du prénom du client
	 * @return La liste des clients correspondant aux critères de recherche
	 */
	public ArrayList<Prelevement> getPrelevements(int _numCompte) {
		ArrayList<Prelevement> listePre = new ArrayList<>();
		try {
			// numCompte != -1 => recherche sur numCompte
			// numCompte == -1 => recherche sur l'agence

			Access_BD_Prelevement ac = new Access_BD_Prelevement();
			listePre = ac.getPrelevements(this.dailyBankState.getEmployeActuel().idAg, _numCompte);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listePre = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listePre = new ArrayList<>();
		}
		return listePre;
	}
}