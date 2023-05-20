package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ClientEditorPaneController;
import application.view.ClientsManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;

/**
 * Classe responsable de la gestion de la fenêtre d'édition d'un client dans l'application DailyBank.
 */
public class ClientEditorPane {

	private Stage primaryStage;
	private ClientEditorPaneController cepcViewController;
	private DailyBankState dailyBankState;
	
	/**
     * Constructeur de la classe ClientEditorPane.
     *
     * @param _parentStage Fenêtre parente
     * @param _dbstate État courant de l'application
     */
	public ClientEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(ClientsManagementController.class.getResource("clienteditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion d'un client");
			this.primaryStage.setResizable(false);

			this.cepcViewController = loader.getController();
			this.cepcViewController.initContext(this.primaryStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * Affiche la boîte de dialogue d'édition du client.
     *
     * @param client Le client à éditer
     * @param em Le mode d'édition (ajout ou modification)
     * @return Le client modifié
     */
	public Client doClientEditorDialog(Client client, EditionMode em) {
		return this.cepcViewController.displayDialog(client, em);
	}
}
