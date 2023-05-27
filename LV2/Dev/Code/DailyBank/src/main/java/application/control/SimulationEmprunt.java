package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.tools.TypeEmprunt;
import application.tools.TypeSimu;
import application.view.SimulerEmpruntController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Classe de contrôleur de la fenêtre de simulation d'emprunt.
 *
 * @see SimulerEmpruntController
 * @author LAMOUR Evan
 */
public class SimulationEmprunt {
	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private SimulerEmpruntController secViewController;

	/**
	 * Constructeur de la classe SimulationEmprunt.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @author LAMOUR Evan
	 */
	public SimulationEmprunt(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(SimulerEmpruntController.class.getResource("simulerEmprunt.fxml"));
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
	 * Affiche la fenêtre de simulation d'emprunt.
	 * 
	 * @author LAMOUR Evan
	 */
	public void doSimulationEmpruntDialog() {
		this.secViewController.displayDialog();
	}

	/**
	 * Effectue une simulation d'emprunt.
	 * 
	 * @param montant Montant de l'emprunt
	 * @param taux    Taux de l'emprunt
	 * @param tauxA   Taux de l'assurance
	 * @param duree   Durée de l'emprunt
	 * @param te      Type d'emprunt
	 * @param ts      Type de simulation
	 * @author LAMOUR Evan
	 */
	public void simulation(int montant, double taux, double tauxA, int duree, TypeEmprunt te, TypeSimu ts) {
		TableauAmortissement ta = new TableauAmortissement(this.primaryStage, this.dailyBankState, montant, taux, tauxA,
				duree, te, ts);
		ta.doTableauDialog();
	}
}
