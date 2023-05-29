package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
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
 * Classe responsable de la gestion de la fenêtre de gestion des employés dans
 * l'application DailyBank.
 * 
 * @see EmployesManagementController
 * @see Access_BD_Employe
 * @author KRILL Maxence
 */
public class EmployesManagement {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private EmployesManagementController emcViewController;

	/**
	 * Constructeur de la classe EmployesManagement.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @author KRILL Maxence
	 */
	public EmployesManagement(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(
					EmployesManagementController.class.getResource("employesmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des employés");
			this.primaryStage.setResizable(false);

			this.emcViewController = loader.getController();
			this.emcViewController.initContext(this.primaryStage, this, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des employés.
	 * 
	 * @author KRILL Maxence
	 */
	public void doEmployeManagementDialog() {
		this.emcViewController.displayDialog();
	}

	/**
	 * Crée un nouvel employé.
	 *
	 * @return Le nouvel employé créé
	 * @author KRILL Maxence
	 */
	public Employe nouveauEmploye() {
		Employe client;
		EmployeEditorPane cep = new EmployeEditorPane(this.primaryStage, this.dailyBankState);
		client = cep.doEmployeEditorDialog(null, EditionMode.CREATION);
		if (client != null) {
			try {
				Access_BD_Employe ae = new Access_BD_Employe();
				ae.insertEmploye(client);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				client = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				client = null;
			}
		}
		return client;
	}

	/**
	 * Modifie un employé existant.
	 *
	 * @param c L'employé à modifier
	 * @return L'employé modifié
	 * @author KRILL Maxence
	 */
	public Employe modifierEmploye(Employe c) {
		EmployeEditorPane cep = new EmployeEditorPane(this.primaryStage, this.dailyBankState);
		Employe result = cep.doEmployeEditorDialog(c, EditionMode.MODIFICATION);
		if (result != null) {
			try {
				Access_BD_Employe ae = new Access_BD_Employe();
				ae.updateEmploye(result);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				result = null;
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				result = null;
			}
		}
		return result;
	}

	/**
	 * Supprime un employé existant.
	 *
	 * @param e L'employé à supprimer
	 * @author KRILL Maxence
	 */
	public void supprimerEmploye(Employe e) {
		if (e != null) {
			try {
				Access_BD_Employe ae = new Access_BD_Employe();
				ae.deleteEmploye(e);
			} catch (DatabaseConnexionException f) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, f);
				ed.doExceptionDialog();
				e = null;
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				e = null;
			}
		}
	}

	/**
	 * Obtient la liste des employés en fonction des critères de recherche.
	 *
	 * @param _numEmp      Le numéro de l'employé (ou -1 pour tous les employés)
	 * @param _debutNom    Le début du nom de l'employé
	 * @param _debutPrenom Le début du prénom de l'employé
	 * @return La liste des employés correspondant aux critères de recherche
	 * @author KRILL Maxence
	 */
	public ArrayList<Employe> getlisteEmployes(int _numEmp, String _debutNom, String _debutPrenom) {
		ArrayList<Employe> listeEmp = new ArrayList<>();
		try {
			// Recherche des employés en BD. cf. AccessClient > getEmployes(.)
			// numEmp != -1 => recherche sur numEmp
			// numEmp == -1 et debutNom non vide => recherche nom/prenom
			// numEmp == -1 et debutNom vide => recherche tous les employés

			Access_BD_Employe ac = new Access_BD_Employe();
			listeEmp = ac.getEmploye(this.dailyBankState.getEmployeActuel().idAg, _numEmp, _debutNom, _debutPrenom);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listeEmp = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeEmp = new ArrayList<>();
		}
		return listeEmp;
	}
}