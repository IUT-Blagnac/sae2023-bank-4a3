package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ComptesManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
import model.orm.Access_BD_Client;
import model.orm.Access_BD_CompteCourant;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.Table;

/**
 * Classe responsable de la gestion de la fenêtre de gestion des comptes dans l'application DailyBank.
 */
public class ComptesManagement {

	private Stage primaryStage;
	private ComptesManagementController cmcViewController;
	private DailyBankState dailyBankState;
	private Client clientDesComptes;

	/**
     * Constructeur de la classe ComptesManagement.
     *
     * @param _parentStage Fenêtre parente
     * @param _dbstate État courant de l'application
     * @param client Client associé aux comptes
     */
	public ComptesManagement(Stage _parentStage, DailyBankState _dbstate, Client client) {

		this.clientDesComptes = client;
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(ComptesManagementController.class.getResource("comptesmanagement.fxml"));
			BorderPane root = loader.load();
			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des comptes");
			this.primaryStage.setResizable(false);

			this.cmcViewController = loader.getController();
			this.cmcViewController.initContext(this.primaryStage, this, _dbstate, client);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * Affiche la fenêtre de gestion des comptes.
     */
	public void doComptesManagementDialog() {
		this.cmcViewController.displayDialog();
	}

	/**
     * Gère les opérations d'un compte spécifique.
     *
     * @param cpt Compte courant à gérer
     */
	public void gererOperationsDUnCompte(CompteCourant cpt) {
		OperationsManagement om = new OperationsManagement(this.primaryStage, this.dailyBankState,
				this.clientDesComptes, cpt);
		om.doOperationsManagementDialog();
	}

	/**
     * Crée un nouveau compte.
     *
     * @return Le compte courant créé
     */
	public CompteCourant creerNouveauCompte() {
		CompteCourant compte;
		CompteEditorPane cep = new CompteEditorPane(this.primaryStage, this.dailyBankState);
		compte = cep.doCompteEditorDialog(this.clientDesComptes, null, EditionMode.CREATION);
		if (compte != null) {
			try {
				Access_BD_CompteCourant ac = new Access_BD_CompteCourant();

				ac.insertCompte(compte);

				// if JAMAIS vrai
				// existe pour compiler les catchs dessous
//				if (Math.random() < -1) {
//					throw new ApplicationException(Table.CompteCourant, Order.INSERT, "todo : test exceptions", null);
//				}
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
			}
		}
		return compte;
	}

	/**
     * Récupère les comptes d'un client.
     *
     * @return La liste des comptes courants du client
     */
	public ArrayList<CompteCourant> getComptesDunClient() {
		ArrayList<CompteCourant> listeCpt = new ArrayList<>();

		try {
			Access_BD_CompteCourant acc = new Access_BD_CompteCourant();
			listeCpt = acc.getCompteCourants(this.clientDesComptes.idNumCli);
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listeCpt = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeCpt = new ArrayList<>();
		}
		return listeCpt;
	}
	
	public void cloturerCompte(CompteCourant compte) {
		if(compte!=null) {
				try {
					Access_BD_CompteCourant ac = new Access_BD_CompteCourant();
					ac.cloturerCompte(compte);
					if(Math.random()<-1){
						throw new ApplicationException(Table.CompteCourant, Order.INSERT, "todo : test exceptions", null);
					}
				}catch (DatabaseConnexionException e) {
					ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
					ed.doExceptionDialog();
					this.primaryStage.close();
				} catch (ApplicationException ae) {
					ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
					ed.doExceptionDialog();
				}
				
		}
	}
	}

