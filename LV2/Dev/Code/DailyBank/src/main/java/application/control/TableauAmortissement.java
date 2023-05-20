package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.EmployesManagementController;
import application.view.TableauAmortissementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TableauAmortissement {
	
	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private TableauAmortissementController tacViewController;
	
	
	public TableauAmortissement(Stage _parentStage, DailyBankState _dbstate,int montant,double taux,int duree) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmployesManagementController.class.getResource("tableauAmortissement.fxml"));
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
			this.tacViewController.initContext(this.primaryStage, this, _dbstate,montant,taux,duree);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doTableauDialog() {
		this.tacViewController.displayDialog();
	}
	
	public void dateSuivante(int a,int m) {
		if(m==12) {
			m=0;
			a++;
		}else {
			m++;
		}
	}

}
