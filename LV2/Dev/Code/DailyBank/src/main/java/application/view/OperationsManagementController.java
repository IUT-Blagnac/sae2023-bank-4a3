package application.view;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import application.DailyBankState;
import application.control.OperationsManagement;
import application.tools.ConstantesIHM;
import application.tools.NoSelectionModel;
import application.tools.PairsOfValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;

/**
 * Contrôleur pour la gestion des opérations.
 * 
 * @see OperationsManagement
 * @author IUT Blagnac
 * @author DIDENKO Andrii
 * @author LAMOUR Evan
 * @author SHULSHINA Daria
 */
public class OperationsManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à OperationsManagementController
	private OperationsManagement omDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private Client clientDuCompte;
	private CompteCourant compteConcerne;
	private ObservableList<Operation> oListOperations;

	// Manipulation de la fenêtre

	/**
	 * Initialise le contexte du contrôleur.
	 * 
	 * @param _containingStage Le stage contenant la scène
	 * @param _om              Le contrôleur de dialogue associé
	 * @param _dbstate         L'état courant de l'application
	 * @param client           Le client associé au compte
	 * @param compte           Le compte courant concerné
	 * @author IUT Blagnac
	 */
	public void initContext(Stage _containingStage, OperationsManagement _om, DailyBankState _dbstate, Client client,
			CompteCourant compte) {
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.omDialogController = _om;
		this.clientDuCompte = client;
		this.compteConcerne = compte;
		this.configure();
	}

	/**
	 * Configure les composants de la fenêtre.
	 * 
	 * @author IUT Blagnac
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListOperations = FXCollections.observableArrayList();
		this.lvOperations.setItems(this.oListOperations);
		this.lvOperations.setSelectionModel(new NoSelectionModel<Operation>());
		this.updateInfoCompteClient();
		this.validateComponentState();
	}

	/**
	 * Affiche la boîte de dialogue des opérations.
	 * 
	 * @author IUT Blagnac
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	// Gestion du stage

	/**
	 * Ferme la fenêtre.
	 * 
	 * @param e L'événement de fermeture
	 * @return Object null
	 * @author IUT Blagnac
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblInfosClient;
	@FXML
	private Label lblInfosCompte;
	@FXML
	private ListView<Operation> lvOperations;
	@FXML
	private Button btnDebit;
	@FXML
	private Button btnReleve;
	@FXML
	private Button btnCredit;
	@FXML
	private Button btnVirement;

	/*
	 * Ferme la fenêtre (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Enregistre un débit (bouton FXML).
	 * 
	 * @author IUT Blagnac
	 */
	@FXML
	private void doDebit() {
		Operation op = this.omDialogController.enregistrerDebit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
	 * Génère un relevé en PDF à la racine du projet (bouton FXML).
	 * 
	 * @author DIDENKO Andrii
	 * @author SHULSHINA Daria
	 */
	@FXML
	private void doReleve() {
		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream("" + this.clientDuCompte.nom + this.clientDuCompte.prenom
					+ this.compteConcerne.idNumCompte + ".pdf"));
			doc.open();
			Font f = new Font(FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.RED);
			Paragraph par1 = new Paragraph("DailyBank", f);
			par1.setAlignment(Element.ALIGN_CENTER);
			doc.add(par1);
			doc.add(new Paragraph(""));

			// Font a = new Font(FontFamily.HELVETICA, 15.0f, Font.BOLD, BaseColor.BLACK);
			Paragraph par2 = new Paragraph("Relevé de : " + this.clientDuCompte.nom + " " + this.clientDuCompte.prenom + "\nCompte numéro : " + this.compteConcerne.idNumCompte);
			doc.add(par2);
			PairsOfValue<CompteCourant, ArrayList<Operation>> opesEtCompte;
			opesEtCompte = this.omDialogController.operationsEtSoldeDunCompte();
			ArrayList<Operation> listeOP;
			listeOP = opesEtCompte.getRight();
			doc.add(new Paragraph(""));
			doc.add(new Paragraph(
					"--------------------------------------------------------------------------------------"));
			doc.add(new Paragraph(""));
			for (Operation element : listeOP) {
				doc.add(new Paragraph(element.toString()));
			}

			doc.close();
			Desktop.getDesktop().open(new File("" + this.clientDuCompte.nom + this.clientDuCompte.prenom
					+ this.compteConcerne.idNumCompte + ".pdf"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Enregistre un crédit (bouton FXML).
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doCredit() {
		Operation op = this.omDialogController.enregistrerCredit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
	 * Enregistre un virement (bouton FXML).
	 * 
	 * @author LAMOUR Evan
	 */
	@FXML
	private void doAutre() {
		Operation op = this.omDialogController.enregistrerVirement();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
	 * Valide l'état des composants de la fenêtre.
	 * 
	 * @author IUT Blagnac
	 * @author LAMOUR Evan
	 */
	private void validateComponentState() {
		// Non implémenté => désactivé
		if (ConstantesIHM.estCloture(compteConcerne) || ConstantesIHM.estInactif(clientDuCompte)) {
			this.btnCredit.setDisable(true);
			this.btnDebit.setDisable(true);
			this.btnReleve.setDisable(true);
			this.btnVirement.setDisable(true);
		} else {
			this.btnCredit.setDisable(false);
			this.btnDebit.setDisable(false);
			this.btnReleve.setDisable(false);
			this.btnVirement.setDisable(false);
		}
	}

	/**
	 * Met à jour les informations du compte client.
	 * 
	 * @author IUT Blagnac
	 */
	private void updateInfoCompteClient() {
		PairsOfValue<CompteCourant, ArrayList<Operation>> opesEtCompte;

		opesEtCompte = this.omDialogController.operationsEtSoldeDunCompte();

		ArrayList<Operation> listeOP;
		this.compteConcerne = opesEtCompte.getLeft();
		listeOP = opesEtCompte.getRight();

		String info;
		info = this.clientDuCompte.nom + "  " + this.clientDuCompte.prenom + "  (id : " + this.clientDuCompte.idNumCli
				+ ")";
		this.lblInfosClient.setText(info);

		info = "Cpt. : " + this.compteConcerne.idNumCompte + "  "
				+ String.format(Locale.ENGLISH, "%12.02f", this.compteConcerne.solde) + "  /  "
				+ String.format(Locale.ENGLISH, "%8d", this.compteConcerne.debitAutorise);
		this.lblInfosCompte.setText(info);

		this.oListOperations.clear();
		this.oListOperations.addAll(listeOP);

		this.validateComponentState();
	}
}