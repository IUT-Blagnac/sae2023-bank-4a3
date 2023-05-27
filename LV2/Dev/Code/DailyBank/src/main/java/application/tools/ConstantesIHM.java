package application.tools;

import model.data.Client;
import model.data.CompteCourant;
import model.data.Employe;

/**
 * Ensemble de constantes utilisées dans tout DailyBank.
 *
 * @author IUT Blagnac
 */
public class ConstantesIHM {

	// Clients

	/**
	 * Code client inactif.
	 * 
	 * @author IUT Blagnac
	 */
	public static final String CLIENT_INACTIF = "O";

	/**
	 * Code client actif.
	 * 
	 * @author IUT Blagnac
	 */
	public static final String CLIENT_ACTIF = "N";

	/**
	 * Code compte inactif.
	 * 
	 * @author IUT Blagnac
	 */
	public static final String COMPTE_INACTIF = "O";

	/**
	 * Code compte actif.
	 * 
	 * @author IUT Blagnac
	 */
	public static final String COMPTE_ACTIF = "N";

	// Employés

	/**
	 * Libellé (en BD) d'un chef de banque.
	 * 
	 * @author IUT Blagnac
	 */
	public static final String AGENCE_CHEF = "chefAgence";

	/**
	 * Libellé (en BD) d'un guichetier.
	 * 
	 * @author IUT Blagnac
	 */
	public static final String AGENCE_GUICHETIER = "guichetier";

	// Types opérations

	/**
	 * Libellés (en BD) des différents types d'opérations existants.
	 * 
	 * @author IUT Blagnac
	 */
	public static final String TYPE_OP_1 = "Dépôt Espèces";
	public static final String TYPE_OP_2 = "Retrait Espèces";
	public static final String TYPE_OP_3 = "Dépôt Chèque";
	public static final String TYPE_OP_4 = "Paiement Chèque";
	public static final String TYPE_OP_5 = "Retrait Carte Bleue";
	public static final String TYPE_OP_6 = "Paiement Carte Bleue";
	public static final String TYPE_OP_7 = "Virement Compte à Compte";
	public static final String TYPE_OP_8 = "Prélèvement automatique";
	public static final String TYPE_OP_9 = "Prélèvement agios";

	// Listes d'opérations

	/**
	 * Liste des opérations de débit possible en agence
	 * 
	 * @author IUT Blagnac
	 */
	public static final String[] OPERATIONS_DEBIT_GUICHET = { ConstantesIHM.TYPE_OP_2, ConstantesIHM.TYPE_OP_5 };

	/**
	 * Liste des opérations de crédit possible en agence
	 * 
	 * @author IUT Blagnac
	 */
	public static final String[] OPERATIONS_CREDIT_GUICHET = { ConstantesIHM.TYPE_OP_1, ConstantesIHM.TYPE_OP_3 };

	// Méthodes utilitaires

	/**
	 * Teste si un droit d'accès correspond à un Admin, soit un chef d'agence pour
	 * le moment.
	 *
	 * @param droitAccess Libellé du droit d'accès
	 * @return true si admin, false sinon
	 * @author IUT Blagnac
	 */
	public static boolean isAdmin(String droitAccess) {
		return droitAccess.equals(ConstantesIHM.AGENCE_CHEF);
	}

	/**
	 * Teste si un droit d'accès correspond à un Admin, soit un chef d'agence pour
	 * le moment.
	 *
	 * @param employe Employé à tester
	 * @return true si admin, false sinon
	 * @author IUT Blagnac
	 */
	public static boolean isAdmin(Employe employe) {
		return employe.droitsAccess.equals(ConstantesIHM.AGENCE_CHEF);
	}

	/**
	 * Teste si un client est actif.
	 *
	 * @param c Client à tester
	 * @return true si le client est actif, false sinon
	 * @author IUT Blagnac
	 */
	public static boolean estActif(Client c) {
		return c.estInactif.equals(ConstantesIHM.CLIENT_ACTIF);
	}

	/**
	 * Retourne vrai si le compte est ouvert, faux sinon.
	 * 
	 * @param cc
	 * @return boolean
	 * @author IUT Blagnac
	 */
	public static boolean estOuvert(CompteCourant cc) {
		return cc.estCloture.equals(ConstantesIHM.COMPTE_ACTIF);
	}

	/**
	 * Retourne vrai si le compte est clôturé, faux sinon.
	 * 
	 * @param cc
	 * @return boolean
	 * @author IUT Blagnac
	 */
	public static boolean estCloture(CompteCourant cc) {
		return cc.estCloture.equals(ConstantesIHM.COMPTE_INACTIF);
	}

	/**
	 * Teste si un client est inactif.
	 *
	 * @param c Client à tester
	 * @return true si le client est inactif, false sinon
	 */
	public static boolean estInactif(Client c) {
		return c.estInactif.equals(ConstantesIHM.CLIENT_INACTIF);
	}
}