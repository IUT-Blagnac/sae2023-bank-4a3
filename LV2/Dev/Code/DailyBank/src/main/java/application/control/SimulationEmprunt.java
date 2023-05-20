package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.ClientsManagementController;
import application.view.SimulerEmpruntController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SimulationEmprunt {
	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private SimulerEmpruntController secViewController;

	/**
     * Constructeur de la classe ClientsManagement.
     *
     * @param _parentStage Fenêtre parente
     * @param _dbstate État courant de l'application
     */
	public SimulationEmprunt(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(ClientsManagementController.class.getResource("simulerEmprunt.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Simulation emprunt");
			this.primaryStage.setResizable(false);

			this.secViewController = loader.getController();
			this.secViewController.initContext(this.primaryStage, this, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * Affiche la fenêtre de gestion des clients.
     */
	public void doSimulationEmpruntDialog() {
		this.secViewController.displayDialog();
	}
	
	public void simulation(int montant,double taux,int duree) {
		System.out.println(montant + " " + taux + " " + duree);
		TableauAmortissement ta = new TableauAmortissement(this.primaryStage, this.dailyBankState, montant, taux, duree);
		ta.doTableauDialog();
	}


}
