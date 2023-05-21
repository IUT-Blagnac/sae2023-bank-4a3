package application.view;

import java.util.Locale;


import application.DailyBankState;
import application.control.EmployesManagement;
import application.control.SimulationEmprunt;
import application.control.TableauAmortissement;
import application.tools.TypeEmprunt;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.LigneTableau;
import java.text.DecimalFormat;

public class TableauAmortissementController {
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à EmployesManagementController
	private TableauAmortissement taDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;
	private TypeEmprunt te;
	private int montant;
	private double taux;
	private int duree;
	@FXML
	private Label info;
	@FXML
	private BorderPane bp;

	private double capd;
	private double interet;
	private double montantpri;
	private double capf;
	
	public void displayDialog() {
		switch(this.te) {
		case MOIS:
			DecimalFormat df = new DecimalFormat("0.00");
			double tauxM = this.taux/12;
			double taux = tauxM/100;
			String infom = "Capital emprunté : " + this.montant + " | taux mensuel : " + 
			df.format(tauxM) + "% | durée en mois : " + this.duree;
			this.info.setText(infom);
			TableView<LigneTableau> table = new TableView<LigneTableau>();
			
			TableColumn<LigneTableau, Integer> periodeColumn= new TableColumn<LigneTableau, Integer>("Numéro période");
			periodeColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, Integer>("periode"));
			
			TableColumn<LigneTableau, String> capdColumn= new TableColumn<LigneTableau, String>("Capital Restant début");
			capdColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capd"));
			
			TableColumn<LigneTableau, String> interetColumn= new TableColumn<LigneTableau, String>("Montant des intérêts");
			interetColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("interet"));
			
			TableColumn<LigneTableau, String> montantpriColumn= new TableColumn<LigneTableau, String>("Montant du principal");
			montantpriColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantpri"));
			
			TableColumn<LigneTableau, String> montantremboursementColumn= new TableColumn<LigneTableau, String>("Montant à rembourser");
			montantremboursementColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantrem"));
			
			TableColumn<LigneTableau, String> capfColumn= new TableColumn<LigneTableau, String>("Capital Restant fin");
			capfColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capf"));
			
			table.getColumns().add(periodeColumn);
			table.getColumns().add(capdColumn);
			table.getColumns().add(interetColumn);
			table.getColumns().add(montantpriColumn);
			table.getColumns().add(montantremboursementColumn);
			table.getColumns().add(capfColumn);
			
			int periode = 1;
			double capDeb = this.montant;
			double interet=this.montant*taux; 
			double montantRembourse = this.montant*(taux)/(1-Math.pow(1+taux, -this.duree));
			double montantPri = montantRembourse-interet;
			double capFin = capDeb - montantPri;
			
			this.capd=capDeb;
			this.interet=interet;
			this.montantpri=montantPri;
			this.capf=capFin;
			
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			table.getItems().add(new LigneTableau(periode,df.format(capDeb)+"€", df.format(interet)+"€", df.format(montantPri)+"€", df.format(montantRembourse)+"€", df.format(capFin)+"€"));
			
			for(int i=2;i<=this.duree;i++) {
				tableau(montantRembourse,this.capf,taux);
				table.getItems().add(new LigneTableau(i,df.format(this.capd)+"€",df.format(this.interet)+"€",df.format(this.montantpri)+"€",df.format(montantRembourse)+"€",df.format(this.capf)+"€")); 
			}
			
			this.bp.setCenter(table);
			
			
			
			
			
			
			
			
			
			
		
			
		break;
		case ANNEE:
			System.out.println("année");
		
			
			
			
		}
		
		this.primaryStage.showAndWait();
		
		
		
		
	}
	
	public void tableau(double montantremboursé,double capfin,double taux) {
		this.capd = capfin;
		this.interet = this.capd*taux;
		this.montantpri = montantremboursé-this.interet;
		this.capf = this.capd-this.montantpri;
	}
	
	public void initContext(Stage _containingStage, TableauAmortissement ta, DailyBankState _dbstate,int montant,double taux,int duree,TypeEmprunt te) {
		this.taDialogController = ta ;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.montant=montant;
		this.duree=duree;
		this.taux=taux;
		this.te=te;
		this.configure();
	}
	
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}
	
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}
	
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

}
