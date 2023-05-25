package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ClientEditorPaneController;
import application.view.ClientsManagementController;
import application.view.PrelevementEditorController;
import application.view.PrelevementManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.Prelevement;

public class PrelevementEditorPane {
	
	private Stage primaryStage;
	private PrelevementEditorController pecViewController;
	private DailyBankState dailyBankState;
	
	/**
     * Constructeur de la classe ClientEditorPane.
     *
     * @param _parentStage Fenêtre parente
     * @param _dbstate État courant de l'application
     */
	public PrelevementEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(PrelevementManagementController.class.getResource("prelevementeditor.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion d'un prélèvement");
			this.primaryStage.setResizable(false);

			this.pecViewController = loader.getController();
			this.pecViewController.initContext(this.primaryStage, this.dailyBankState);

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
	public Prelevement doPrelevementEditorDialog(Prelevement pm, EditionMode em) {
		return this.pecViewController.displayDialog(pm, em);
	}
}


