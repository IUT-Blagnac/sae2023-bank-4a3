package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ClientsManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.orm.Access_BD_Client;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

/**
 * Classe responsable de la gestion de la fenêtre de gestion des clients dans
 * l'application DailyBank.
 * 
 * @see ClientsManagementController
 * @see Access_BD_Client
 * @author IUT Blagnac
 * @author KRILL Maxence
 * @author LAMOUR Evan
 */
public class ClientsManagement {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private ClientsManagementController cmcViewController;

	/**
	 * Constructeur de la classe ClientsManagement.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @author IUT Blagnac
	 */
	public ClientsManagement(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(ClientsManagementController.class.getResource("clientsmanagement.fxml"));
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

			this.cmcViewController = loader.getController();
			this.cmcViewController.initContext(this.primaryStage, this, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des clients.
	 * 
	 * @author IUT Blagnac
	 */
	public void doClientManagementDialog() {
		this.cmcViewController.displayDialog();
	}

	/**
	 * Crée un nouveau client.
	 *
	 * @return Le nouveau client créé
	 * @author IUT Blagnac
	 */
	public Client nouveauClient() {
		Client client;
		ClientEditorPane cep = new ClientEditorPane(this.primaryStage, this.dailyBankState);
		client = cep.doClientEditorDialog(null, EditionMode.CREATION);
		if (client != null) {
			try {
				Access_BD_Client ac = new Access_BD_Client();

				ac.insertClient(client);
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
	 * Modifie un client existant.
	 *
	 * @param cliMod Le client à modifier
	 * @return Le client modifié
	 * @author IUT Blagnac
	 */
	public Client modifierClient(Client cliMod) {
		ClientEditorPane cep = new ClientEditorPane(this.primaryStage, this.dailyBankState);
		Client result = cep.doClientEditorDialog(cliMod, EditionMode.MODIFICATION);
		if (result != null) {
			try {
				Access_BD_Client ac = new Access_BD_Client();
				ac.updateClient(result);
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
	 * Vérifie si les comptes d'un client sont tous clôturés
	 *
	 * @param cliMod Le client sur lequel effectuer la vérification
	 * @return Le nombre de comptes ouverts du client, -1 en cas d'erreur
	 * @author KRILL Maxence
	 */
	public int verifierCloturer(Client cliMod) {
		int comptesOuverts = -1;
		if (cliMod != null) {
			try {
				Access_BD_Client ac = new Access_BD_Client();
				comptesOuverts = ac.verifierCloturer(cliMod.idNumCli);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
			}
		}
		return comptesOuverts;
	}

	/**
	 * Désactive un client.
	 *
	 * @param cliMod Le client à désactiver.
	 * @return Le client désactivé.
	 * @author KRILL Maxence
	 * @author LAMOUR Evan
	 */
	public Client clientInactif(Client cliMod) {
		cliMod.estInactif = ConstantesIHM.CLIENT_INACTIF;
		if (cliMod != null) {
			try {
				Access_BD_Client ac = new Access_BD_Client();
				ac.updateClient(cliMod);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				cliMod = null;
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				cliMod = null;
			}
		}
		return cliMod;
	}

	/**
	 * Gère les comptes d'un client.
	 *
	 * @param c Le client dont les comptes doivent être gérés
	 */
	public void gererComptesClient(Client c) {
		ComptesManagement cm = new ComptesManagement(this.primaryStage, this.dailyBankState, c);
		cm.doComptesManagementDialog();
	}

	/**
	 * Obtient la liste des clients en fonction des critères de recherche.
	 *
	 * @param _numCompte   Le numéro de compte (ou -1 pour tous les clients)
	 * @param _debutNom    Le début du nom du client
	 * @param _debutPrenom Le début du prénom du client
	 * @return La liste des clients correspondant aux critères de recherche
	 */
	public ArrayList<Client> getlisteComptes(int _numCompte, String _debutNom, String _debutPrenom) {
		ArrayList<Client> listeCli = new ArrayList<>();
		try {
			// Recherche des clients en BD. cf. AccessClient > getClients(.)
			// numCompte != -1 => recherche sur numCompte
			// numCompte == -1 et debutNom non vide => recherche nom/prenom
			// numCompte == -1 et debutNom vide => recherche tous les clients

			Access_BD_Client ac = new Access_BD_Client();
			listeCli = ac.getClients(this.dailyBankState.getEmployeActuel().idAg, _numCompte, _debutNom, _debutPrenom);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listeCli = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeCli = new ArrayList<>();
		}
		return listeCli;
	}
}