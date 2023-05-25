package application.view;

import java.util.Locale;

import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.CategorieOperation;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.CompteCourant;
import model.data.Operation;

public class OperationEditorPaneController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private CategorieOperation categorieOperation;
	private CompteCourant compteEdite;
	private Operation operationResultat;
	@FXML
	private TextField comptecrediteur;
	@FXML
	private GridPane gp;
	@FXML
	private Label txtcomptecrediteur;

	// Manipulation de la fenêtre
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	public Operation displayDialog(CompteCourant cpte, CategorieOperation mode) {
		this.categorieOperation = mode;
		this.compteEdite = cpte;

		switch (mode) {
		case DEBIT:
			String info = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info);

			this.btnOk.setText("Effectuer Débit");
			this.btnCancel.setText("Annuler débit");

			ObservableList<String> listTypesOpesPossibles = FXCollections.observableArrayList();
			listTypesOpesPossibles.addAll(ConstantesIHM.OPERATIONS_DEBIT_GUICHET);

			this.cbTypeOpe.setItems(listTypesOpesPossibles);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
		case CREDIT:
			String infoc = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(infoc);

			this.btnOk.setText("Effectuer Crédit");
			this.btnCancel.setText("Annuler Crédit");

			ObservableList<String> listTypesOpesPossiblesC = FXCollections.observableArrayList();
			listTypesOpesPossiblesC.addAll(ConstantesIHM.OPERATIONS_CREDIT_GUICHET);

			this.cbTypeOpe.setItems(listTypesOpesPossiblesC);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
		case VIREMENT:
			this.compteEdite = cpte;
			String infov = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.txtcomptecrediteur = new Label("N° Compte créditeur");
			this.comptecrediteur = new TextField();
			this.lblMessage.setText(infov);
			this.txtcomptecrediteur.setText("N° Compte créditeur");
			this.btnOk.setText("Effectuer Virement");
			this.btnCancel.setText("Annuler Virement");
			this.cbTypeOpe.setDisable(true);
			this.gp.add(this.txtcomptecrediteur, 0, 1);
			this.gp.add(this.comptecrediteur, 1, 1);
			if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
				// rien pour l'instant
			}

			this.primaryStage.showAndWait();
			this.operationResultat.idNumCompte=Integer.parseInt(this.comptecrediteur.getText());
			return this.operationResultat;
		}

		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}

		this.operationResultat = null;
		this.cbTypeOpe.requestFocus();

		this.primaryStage.showAndWait();
		return this.operationResultat;
	}




	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblMessage;
	@FXML
	private Label lblMontant;
	@FXML
	private ComboBox<String> cbTypeOpe;
	@FXML
	private TextField txtMontant;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnCancel;
	@FXML
	private TextField txtNumCompte;


	@FXML
	private void doCancel() {
		this.operationResultat = null;
		this.primaryStage.close();
	}

	@FXML
	private void doAjouter() {
		switch (this.categorieOperation) {
		case DEBIT:
			// règles de validation d'un débit :
			// - le montant doit être un nombre valide
			// - et si l'utilisateur n'est pas chef d'agence,
			// - le débit ne doit pas amener le compte en dessous de son découvert autorisé
			double montant;

			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");
			String info = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info);

			try {
				montant = Double.parseDouble(this.txtMontant.getText().trim());
				if (montant <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}
			String typeOp = this.cbTypeOpe.getValue();
			if (this.compteEdite.solde - montant < this.compteEdite.debitAutorise) {
				if(!ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
					info = "Permission chef agence requise ! - Cpt. : " + this.compteEdite.idNumCompte + "  "
							+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
							+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
					this.lblMessage.setText(info);
					this.txtMontant.getStyleClass().add("borderred");
					this.lblMontant.getStyleClass().add("borderred");
					this.lblMessage.getStyleClass().add("borderred");
					this.txtMontant.requestFocus();
					return;
				} else {
					if(!AlertUtilities.confirmYesCancel(this.primaryStage, "Débit exceptionnel", "Êtes-vous sûr de vouloir réaliser cette opération ?", "Débit de " + montant + " €\nSolde actuel : " + this.compteEdite.solde + " €\nDécouvert autorisé : " + this.compteEdite.debitAutorise + " €\nDépassement : " + (0 - this.compteEdite.debitAutorise + this.compteEdite.solde - montant) + " €\nSolde après opération : " + (this.compteEdite.solde - montant) + " €", AlertType.CONFIRMATION)) return;
				}
			}
			this.operationResultat = new Operation(-1, montant, null, null, this.compteEdite.idNumCli, typeOp);
			this.primaryStage.close();
			break;
		case CREDIT:
			double montantC;

			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");
			String infoC = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(infoC);

			try {
				montantC = Double.parseDouble(this.txtMontant.getText().trim());
				if (montantC <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}

			String typeOpC = this.cbTypeOpe.getValue();
			this.operationResultat = new Operation(-1, montantC, null, null, this.compteEdite.idNumCli, typeOpC);
			this.primaryStage.close();
			break;
		case VIREMENT:
			double montantV;

			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");
			String infoV = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(infoV);

			try {
				montantV = Double.parseDouble(this.txtMontant.getText().trim());
				if (montantV <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}

			String typeOpV = this.cbTypeOpe.getValue();
			this.operationResultat = new Operation(-1, montantV, null, null, this.compteEdite.idNumCli, typeOpV);
			this.primaryStage.close();
			break;

		}
	}
}
