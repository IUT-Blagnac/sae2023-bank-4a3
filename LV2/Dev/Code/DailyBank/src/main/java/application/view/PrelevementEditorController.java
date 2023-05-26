package application.view;

import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Prelevement;

public class PrelevementEditorController {
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre

	private Prelevement prelevementEdite;
	private EditionMode editionMode;
	private Prelevement prelevementResultat;

	// Manipulation de la fenêtre

	/**
     * Initialise le contexte du contrôleur.
     * @param _containingStage le stage contenant la scène
     * @param _dbstate l'état courant de l'application
     */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	public Prelevement displayDialog(Prelevement pm, EditionMode mode) {

		this.editionMode = mode;
		if (pm == null) {
			this.prelevementEdite = new Prelevement(0,0,0,"",0);
		} else {
			this.prelevementEdite = new Prelevement(pm);
		}
		this.prelevementResultat = null;
		switch (mode) {
		case CREATION:
			this.txtIdpre.setDisable(true);
			this.txtmontant.setDisable(false);
			this.txtdate.setDisable(false);
			this.txtbeneficiaire.setDisable(false);
			this.txtIdCompte.setDisable(false);

			this.lblMessage.setText("Informations sur le nouveau prélevement");
			this.butOk.setText("Ajouter");
			this.butCancel.setText("Annuler");
			break;
		case MODIFICATION:

			break;
		case SUPPRESSION:


			break;
		}
		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}

		this.prelevementResultat = null;

		this.primaryStage.showAndWait();
		return this.prelevementResultat;
	}

	@FXML
	private void doCancel() {
		this.prelevementResultat = null;
		this.primaryStage.close();
	}

	@FXML
	private void doAjouter() {
		switch (this.editionMode) {
			case CREATION:
				if (this.isSaisieValide()) {
					this.prelevementResultat = this.prelevementEdite;
					this.primaryStage.close();
				}
				break;
			case MODIFICATION:
				break;
			case SUPPRESSION:
				break;
		}
	}

	private boolean isSaisieValide() {
		this.prelevementEdite.montant = Integer.parseInt(this.txtmontant.getText().trim());
		this.prelevementEdite.date = Integer.parseInt(this.txtdate.getText().trim());
		this.prelevementEdite.beneficiaire = this.txtbeneficiaire.getText().trim();
		this.prelevementEdite.idNumCompte = Integer.parseInt(this.txtIdCompte.getText().trim());
		
		if(this.txtbeneficiaire.getText().isEmpty() ) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur saisie", "beneficiaire n'est pas remplis", "Veuillez remplir le champs", AlertType.ERROR);
			this.txtbeneficiaire.requestFocus();
			return false;
		}
		if(this.prelevementEdite.date<1 || this.prelevementEdite.date>28) {
			AlertUtilities.showAlert(this.primaryStage,"Erreur date" , "La date est non valide", "Veuillez rentrer une date entre 1 et 28", AlertType.ERROR);
			this.txtdate.requestFocus();
			return false;
		}
		if(this.prelevementEdite.montant<1) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur montant","Le montant n'est pas valide", "Veuillez entrez un montant positif", AlertType.ERROR);
			this.txtmontant.requestFocus();
			return false;
		}

		return true;
	}

	@FXML
	private Label lblMessage;
	@FXML
	private TextField txtIdpre;
	@FXML
	private TextField txtmontant;
	@FXML
	private TextField txtdate;
	@FXML
	private TextField txtbeneficiaire;
	@FXML
	private TextField txtIdCompte;
	@FXML
	private Button butOk;
	@FXML
	private Button butCancel;
}