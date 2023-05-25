package application.view;

import java.text.DecimalFormat;

import application.DailyBankState;
import application.control.TableauAmortissement;
import application.tools.TypeEmprunt;
import application.tools.TypeSimu;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.LigneTableau;

public class TableauAmortissementController {
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à EmployesManagementController
	private TableauAmortissement taDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;
	private TypeEmprunt te;
	private TypeSimu ts;
	private int montant;
	private double taux;
	private double tauxA;
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
		switch(this.ts) {
		case EMPRUNT:
			switch(this.te) {
			case MOIS:
				DecimalFormat df = new DecimalFormat("0.00");
				double tauxM = this.taux/12;
				double taux = tauxM/100;

				int periode = 1;
				double capDeb = this.montant;
				double interet=this.montant*taux;
				double montantRembourse = this.montant*(taux)/(1-Math.pow(1+taux, -this.duree*12));
				double montantPri = montantRembourse-interet;
				double capFin = capDeb - montantPri;

				String infom = "Capital emprunté : " + this.montant + " | taux mensuel : " +
				df.format(tauxM) + "% | durée en mois : " + this.duree*12 + " | Coût du prêt : " + df.format((this.duree*12)*montantRembourse)+"€";
				this.info.setText(infom);
				TableView<LigneTableau> table = new TableView<>();

				TableColumn<LigneTableau, Integer> periodeColumn= new TableColumn<>("Numéro période");
				periodeColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, Integer>("periode"));

				TableColumn<LigneTableau, String> capdColumn= new TableColumn<>("Capital Restant début");
				capdColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capd"));

				TableColumn<LigneTableau, String> interetColumn= new TableColumn<>("Montant des intérêts");
				interetColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("interet"));

				TableColumn<LigneTableau, String> montantpriColumn= new TableColumn<>("Montant du principal");
				montantpriColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantpri"));

				TableColumn<LigneTableau, String> montantremboursementColumn= new TableColumn<>("Montant à rembourser");
				montantremboursementColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantrem"));

				TableColumn<LigneTableau, String> capfColumn= new TableColumn<>("Capital Restant fin");
				capfColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capf"));

				table.getColumns().add(periodeColumn);
				table.getColumns().add(capdColumn);
				table.getColumns().add(interetColumn);
				table.getColumns().add(montantpriColumn);
				table.getColumns().add(montantremboursementColumn);
				table.getColumns().add(capfColumn);



				this.capd=capDeb;
				this.interet=interet;
				this.montantpri=montantPri;
				this.capf=capFin;

				table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				table.getItems().add(new LigneTableau(periode,df.format(capDeb)+"€", df.format(interet)+"€", df.format(montantPri)+"€", df.format(montantRembourse)+"€", df.format(capFin)+"€"));

				for(int i=2;i<=this.duree*12;i++) {
					tableau(montantRembourse,this.capf,taux);
					table.getItems().add(new LigneTableau(i,df.format(this.capd)+"€",df.format(this.interet)+"€",df.format(this.montantpri)+"€",df.format(montantRembourse)+"€",df.format(this.capf)+"€"));
				}

				this.bp.setCenter(table);

			break;
			case ANNEE:
				DecimalFormat dfA = new DecimalFormat("0.00");
				double tauxA = this.taux/100;

				int periodeA = 1;
				double capDebA = this.montant;
				double interetA=this.montant*tauxA;
				double montantRembourseA = this.montant*(tauxA)/(1-Math.pow(1+tauxA, -this.duree));
				double montantPriA = montantRembourseA-interetA;
				double capFinA = capDebA - montantPriA;

				String infoA = "Capital emprunté : " + this.montant+"€" + " | Taux annuel : " +
				dfA.format(this.taux) + "% | Durée en année : " + this.duree + " | Coût du prêt :"+ dfA.format(this.duree*montantRembourseA)+"€";
				this.info.setText(infoA);
				TableView<LigneTableau> tableA = new TableView<>();

				TableColumn<LigneTableau, Integer> periodeColumnA= new TableColumn<>("Numéro période");
				periodeColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, Integer>("periode"));

				TableColumn<LigneTableau, String> capdColumnA= new TableColumn<>("Capital Restant début");
				capdColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capd"));

				TableColumn<LigneTableau, String> interetColumnA= new TableColumn<>("Montant des intérêts");
				interetColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("interet"));

				TableColumn<LigneTableau, String> montantpriColumnA= new TableColumn<>("Montant du principal");
				montantpriColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantpri"));

				TableColumn<LigneTableau, String> montantremboursementColumnA= new TableColumn<>("Montant à rembourser");
				montantremboursementColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantrem"));

				TableColumn<LigneTableau, String> capfColumnA= new TableColumn<>("Capital Restant fin");
				capfColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capf"));

				tableA.getColumns().add(periodeColumnA);
				tableA.getColumns().add(capdColumnA);
				tableA.getColumns().add(interetColumnA);
				tableA.getColumns().add(montantpriColumnA);
				tableA.getColumns().add(montantremboursementColumnA);
				tableA.getColumns().add(capfColumnA);


				this.capd=capDebA;
				this.interet=interetA;
				this.montantpri=montantPriA;
				this.capf=capFinA;

				tableA.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				tableA.getItems().add(new LigneTableau(periodeA,dfA.format(capDebA)+"€", dfA.format(interetA)+"€", dfA.format(montantPriA)+"€", dfA.format(montantRembourseA)+"€", dfA.format(capFinA)+"€"));

				for(int i=2;i<=this.duree;i++) {
					tableau(montantRembourseA,this.capf,tauxA);
					tableA.getItems().add(new LigneTableau(i,dfA.format(this.capd)+"€",dfA.format(this.interet)+"€",dfA.format(this.montantpri)+"€",dfA.format(montantRembourseA)+"€",dfA.format(this.capf)+"€"));
				}

				this.bp.setCenter(tableA);
				break;
			}
			break;

		case ASSURANCE:
			switch(this.te) {
			case MOIS:
				DecimalFormat df = new DecimalFormat("0.00");
				double tauxM = this.taux/12;
				double taux = tauxM/100;

				int periode = 1;
				double capDeb = this.montant;
				double interet=this.montant*taux;
				double montantRembourse = this.montant*(taux)/(1-Math.pow(1+taux, -this.duree*12));
				double montantPri = montantRembourse-interet;
				double capFin = capDeb - montantPri;
				double prixA = this.montant*(this.tauxA/100);
				double prixTota = this.duree*12*montantRembourse+prixA;
				String infom = "Capital emprunté : " + this.montant+"€" + " | taux mensuel : " +
				df.format(tauxM) + "% | durée en mois : " + this.duree*12 + " | Coût du prêt : " + df.format((this.duree*12)*montantRembourse)+"€"+ " | Taux assurance : "+df.format(this.taux) +"%"+" | coût assurance : " + df.format(prixA)+"€"+" | Coût Total : "+df.format(prixTota)+"€";
				this.info.setText(infom);
				this.info.setStyle("-fx-font-size : 15;");
				TableView<LigneTableau> table = new TableView<>();

				TableColumn<LigneTableau, Integer> periodeColumn= new TableColumn<>("Numéro période");
				periodeColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, Integer>("periode"));

				TableColumn<LigneTableau, String> capdColumn= new TableColumn<>("Capital Restant début");
				capdColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capd"));

				TableColumn<LigneTableau, String> interetColumn= new TableColumn<>("Montant des intérêts");
				interetColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("interet"));

				TableColumn<LigneTableau, String> montantpriColumn= new TableColumn<>("Montant du principal");
				montantpriColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantpri"));

				TableColumn<LigneTableau, String> montantremboursementColumn= new TableColumn<>("Montant à rembourser");
				montantremboursementColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantrem"));

				TableColumn<LigneTableau, String> capfColumn= new TableColumn<>("Capital Restant fin");
				capfColumn.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capf"));

				table.getColumns().add(periodeColumn);
				table.getColumns().add(capdColumn);
				table.getColumns().add(interetColumn);
				table.getColumns().add(montantpriColumn);
				table.getColumns().add(montantremboursementColumn);
				table.getColumns().add(capfColumn);



				this.capd=capDeb;
				this.interet=interet;
				this.montantpri=montantPri;
				this.capf=capFin;

				table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				table.getItems().add(new LigneTableau(periode,df.format(capDeb)+"€", df.format(interet)+"€", df.format(montantPri)+"€", df.format(montantRembourse)+"€", df.format(capFin)+"€"));

				for(int i=2;i<=this.duree*12;i++) {
					tableau(montantRembourse,this.capf,taux);
					table.getItems().add(new LigneTableau(i,df.format(this.capd)+"€",df.format(this.interet)+"€",df.format(this.montantpri)+"€",df.format(montantRembourse)+"€",df.format(this.capf)+"€"));
				}

				this.bp.setCenter(table);

			break;
			case ANNEE:
				DecimalFormat dfA = new DecimalFormat("0.00");
				double tauxA = this.taux/100;

				int periodeA = 1;
				double capDebA = this.montant;
				double interetA=this.montant*tauxA;
				double montantRembourseA = this.montant*(tauxA)/(1-Math.pow(1+tauxA, -this.duree));
				double montantPriA = montantRembourseA-interetA;
				double capFinA = capDebA - montantPriA;
				double prixAs = this.montant*(this.tauxA/100);
				double prixTot = this.duree*montantRembourseA+prixAs;

				String infoA = "Capital emprunté : " + this.montant+"€" + " | Taux annuel : " +
				dfA.format(this.taux) + "% | Durée en année : " + this.duree + " | Coût du prêt : "+ dfA.format(this.duree*montantRembourseA)+"€"+ " | Taux assurance : "+dfA.format(this.taux) +"%"+" | coût assurance : " + dfA.format(prixAs)+"€"+" | Coût Total : "+dfA.format(prixTot)+"€";;
				this.info.setText(infoA);
				this.info.setStyle("-fx-font-size : 15;");
				TableView<LigneTableau> tableA = new TableView<>();

				TableColumn<LigneTableau, Integer> periodeColumnA= new TableColumn<>("Numéro période");
				periodeColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, Integer>("periode"));

				TableColumn<LigneTableau, String> capdColumnA= new TableColumn<>("Capital Restant début");
				capdColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capd"));

				TableColumn<LigneTableau, String> interetColumnA= new TableColumn<>("Montant des intérêts");
				interetColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("interet"));

				TableColumn<LigneTableau, String> montantpriColumnA= new TableColumn<>("Montant du principal");
				montantpriColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantpri"));

				TableColumn<LigneTableau, String> montantremboursementColumnA= new TableColumn<>("Montant à rembourser");
				montantremboursementColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("montantrem"));

				TableColumn<LigneTableau, String> capfColumnA= new TableColumn<>("Capital Restant fin");
				capfColumnA.setCellValueFactory(new PropertyValueFactory<LigneTableau, String>("capf"));

				tableA.getColumns().add(periodeColumnA);
				tableA.getColumns().add(capdColumnA);
				tableA.getColumns().add(interetColumnA);
				tableA.getColumns().add(montantpriColumnA);
				tableA.getColumns().add(montantremboursementColumnA);
				tableA.getColumns().add(capfColumnA);


				this.capd=capDebA;
				this.interet=interetA;
				this.montantpri=montantPriA;
				this.capf=capFinA;

				tableA.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				tableA.getItems().add(new LigneTableau(periodeA,dfA.format(capDebA)+"€", dfA.format(interetA)+"€", dfA.format(montantPriA)+"€", dfA.format(montantRembourseA)+"€", dfA.format(capFinA)+"€"));

				for(int i=2;i<=this.duree;i++) {
					tableau(montantRembourseA,this.capf,tauxA);
					tableA.getItems().add(new LigneTableau(i,dfA.format(this.capd)+"€",dfA.format(this.interet)+"€",dfA.format(this.montantpri)+"€",dfA.format(montantRembourseA)+"€",dfA.format(this.capf)+"€"));
				}

				this.bp.setCenter(tableA);
				break;
			}
			break;


		}

		this.primaryStage.showAndWait();




	}

	public void tableau(double montantremboursé,double capfin,double taux) {
		this.capd = capfin;
		this.interet = this.capd*taux;
		this.montantpri = montantremboursé-this.interet;
		this.capf = this.capd-this.montantpri;
	}

	public void initContext(Stage _containingStage, TableauAmortissement ta, DailyBankState _dbstate,int montant,double taux,double tauxA,int duree,TypeEmprunt te,TypeSimu ts) {
		this.taDialogController = ta ;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.montant=montant;
		this.duree=duree;
		this.taux=taux;
		this.tauxA=tauxA;
		this.te=te;
		this.ts=ts;
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
