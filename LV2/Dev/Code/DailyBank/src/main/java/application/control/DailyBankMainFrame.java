package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.view.DailyBankMainFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.orm.LogToDatabase;
import model.orm.exception.DatabaseConnexionException;

/**
 * Classe de controleur de Dialogue de la fenêtre principale.
 *
 * @see DailyBankMainFrameController
 * @author IUT Blagnac
 * @author KRILL Maxence
 * @author LAMOUR Evan
 */
public class DailyBankMainFrame extends Application {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Stage de la fenêtre principale construite par DailyBankMainFrame
	private Stage primaryStage;

	/**
	 * Méthode de démarrage (JavaFX).
	 * 
	 * @author IUT Blagnac
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {
			// Création de l'état courant de l'application
			this.dailyBankState = new DailyBankState();
			this.dailyBankState.setEmployeActuel(null);

			// Chargement du source fxml
			FXMLLoader loader = new FXMLLoader(
					DailyBankMainFrameController.class.getResource("dailybankmainframe.fxml"));
			BorderPane root = loader.load();

			// Paramétrage du Stage : feuille de style, titre
			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.setTitle("Fenêtre Principale");

			/*
			 * // En mise au point :
			 * // Forcer une connexion existante pour rentrer dans l'appli en mode connecté
			 *
			 * try { Employe e; Access_BD_Employe ae = new Access_BD_Employe();
			 *
			 * e = ae.getEmploye("Tuff", "Lejeune");
			 *
			 * if (e == null) { System.out.println("\n\nPB DE CONNEXION\n\n"); } else {
			 * this.dailyBankState.setEmployeActuel(e); } } catch
			 * (DatabaseConnexionException e) { ExceptionDialog ed = new
			 * ExceptionDialog(primaryStage, this.dailyBankState, e);
			 * ed.doExceptionDialog(); this.dailyBankState.setEmployeActuel(null); } catch
			 * (ApplicationException ae) { ExceptionDialog ed = new
			 * ExceptionDialog(primaryStage, this.dailyBankState, ae);
			 * ed.doExceptionDialog(); this.dailyBankState.setEmployeActuel(null); }
			 *
			 * if (this.dailyBankState.getEmployeActuel() != null) {
			 * this.dailyBankState.setEmployeActuel(this.dailyBankState.getEmployeActuel());
			 * }
			 *
			 */

			// Récupération du contrôleur et initialisation (stage, contrôleur de dialogue,
			// état courant)
			DailyBankMainFrameController dbmfcViewController = loader.getController();
			dbmfcViewController.initContext(primaryStage, this, this.dailyBankState);
			dbmfcViewController.displayDialog();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Méthode principale de lancement de l'application.
	 * 
	 * @author IUT Blagnac
	 */
	public static void runApp() {
		Application.launch();
	}

	/**
	 * Réaliser la déconnexion de l'application.
	 * 
	 * @author IUT Blagnac
	 */
	public void deconnexionEmploye() {
		this.dailyBankState.setEmployeActuel(null);
		try {
			LogToDatabase.closeConnexion();
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
		}
	}

	/**
	 * Lancer la connexion de l'utilisateur (login/mdp employé).
	 *
	 * @author IUT Blagnac
	 */
	public void loginDunEmploye() {
		LoginDialog ld = new LoginDialog(this.primaryStage, this.dailyBankState);
		ld.doLoginDialog();
	}

	/**
	 * Ouvre la fenêtre de gestion des clients.
	 *
	 * @author IUT Blagnac
	 */
	public void gestionClients() {
		ClientsManagement cm = new ClientsManagement(this.primaryStage, this.dailyBankState);
		cm.doClientManagementDialog();
	}

	/**
	 * Ouvre la fenêtre de gestion des employés.
	 *
	 * @author KRILL Maxence
	 */
	public void gestionEmployes() {
		EmployesManagement em = new EmployesManagement(this.primaryStage, this.dailyBankState);
		em.doEmployeManagementDialog();
	}

	/**
	 * Ouvre la fenêtre de gestion des prélévements automatiques.
	 *
	 * @author LAMOUR Evan
	 */
	public void gestionPrelevement() {
		PrelevementManagement pm = new PrelevementManagement(this.primaryStage, this.dailyBankState);
		pm.doPrelevementManagementDialog();
	}

	/**
	 * Ouvre la fenêtre de simulation d'un emprunt.
	 *
	 * @author LAMOUR Evan
	 */
	public void simulerEmprunt() {
		SimulationEmprunt se = new SimulationEmprunt(this.primaryStage, this.dailyBankState);
		se.doSimulationEmpruntDialog();
	}
}