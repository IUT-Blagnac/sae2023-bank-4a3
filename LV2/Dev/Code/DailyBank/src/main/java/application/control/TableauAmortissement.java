package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.tools.TypeEmprunt;
import application.tools.TypeSimu;
import application.view.TableauAmortissementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Classe de contrôleur de la fenêtre du tableau résultat de la simulation d'emprunt.
 *
 * @see TableauAmortissementController
 * @author LAMOUR Evan
 */
public class TableauAmortissement {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private TableauAmortissementController tacViewController;

	/**
	 * Constructeur de la classe TableauAmortissement.
	 *
	 * @param _parentStage Fenêtre parente
	 * @param _dbstate     État courant de l'application
	 * @author LAMOUR Evan
	 */
	public TableauAmortissement(Stage _parentStage, DailyBankState _dbstate, int montant, double taux, double tauxA,
			int duree, TypeEmprunt te, TypeSimu ts) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(
					TableauAmortissementController.class.getResource("tableauAmortissement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Tableau d'amortissement");
			this.primaryStage.setResizable(true);

			this.tacViewController = loader.getController();
			this.tacViewController.initContext(this.primaryStage, this, _dbstate, montant, taux, tauxA, duree, te, ts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la tableau résultat de la simulation d'emprunt.
	 * 
	 * @author LAMOUR Evan
	 */
	public void doTableauDialog() {
		this.tacViewController.displayDialog();
	}
}
