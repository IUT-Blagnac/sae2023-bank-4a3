package model.orm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import model.data.Operation;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 * Classe d'accès aux Operation en BD Oracle.
 * 
 * @author IUT Blagnac
 * @author KRILL Maxence
 * @author LAMOUR Evan
 */
public class Access_BD_Operation {

	public Access_BD_Operation() {
	}

	/**
	 * Recherche de toutes les opérations d'un compte.
	 *
	 * @param idNumCompte id du compte dont on cherche toutes les opérations
	 * @return Toutes les opérations du compte, liste vide si pas d'opération
	 * @throws DataAccessException        Erreur d'accès aux données (requête mal
	 *                                    formée ou autre)
	 * @throws DatabaseConnexionException Erreur de connexion
	 * @author IUT Blagnac
	 */
	public ArrayList<Operation> getOperations(int idNumCompte) throws DataAccessException, DatabaseConnexionException {

		ArrayList<Operation> alResult = new ArrayList<>();

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM Operation where idNumCompte = ?";
			query += " ORDER BY dateOp";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idNumCompte);

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int idOperation = rs.getInt("idOperation");
				double montant = rs.getDouble("montant");
				Date dateOp = rs.getDate("dateOp");
				Date dateValeur = rs.getDate("dateValeur");
				int idNumCompteTrouve = rs.getInt("idNumCompte");
				String idTypeOp = rs.getString("idTypeOp");

				alResult.add(new Operation(idOperation, montant, dateOp, dateValeur, idNumCompteTrouve, idTypeOp));
			}
			rs.close();
			pst.close();
			return alResult;
		} catch (SQLException e) {
			throw new DataAccessException(Table.Operation, Order.SELECT, "Erreur accès", e);
		}
	}

	/**
	 * Recherche d'un prélévement automatique par son id.
	 *
	 * @param idOperation id du prélévement automatique recherchée (clé primaire)
	 * @return Une Operation ou null si non trouvé
	 * @throws RowNotFoundOrTooManyRowsException La requête renvoie plus de 1 ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 * @author IUT Blagnac
	 */
	public Operation getOperation(int idOperation)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {

		Operation operationTrouvee;

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM PrelevementAutomatique  where" + " idOperation = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idOperation);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				int idOperationTrouve = rs.getInt("idOperation");
				double montant = rs.getDouble("montant");
				Date dateOp = rs.getDate("dateOp");
				Date dateValeur = rs.getDate("dateValeur");
				int idNumCompteTrouve = rs.getInt("idNumCompte");
				String idTypeOp = rs.getString("idTypeOp");

				operationTrouvee = new Operation(idOperationTrouve, montant, dateOp, dateValeur, idNumCompteTrouve,
						idTypeOp);
			} else {
				rs.close();
				pst.close();
				return null;
			}

			if (rs.next()) {
				rs.close();
				pst.close();
				throw new RowNotFoundOrTooManyRowsException(Table.Operation, Order.SELECT,
						"Recherche anormale (en trouve au moins 2)", null, 2);
			}
			rs.close();
			pst.close();
			return operationTrouvee;
		} catch (SQLException e) {
			throw new DataAccessException(Table.Operation, Order.SELECT, "Erreur accès", e);
		}
	}

	/**
	 * Enregistrement d'un débit.
	 *
	 * Se fait par procédure stockée : - Vérifie que le débitAutorisé n'est pas
	 * dépassé
	 * - Enregistre l'opération
	 * - Met à jour le solde du compte.
	 *
	 * @param idNumCompte Compte débité
	 * @param montant     Montant débité
	 * @param typeOp      Libellé de l'opération effectuée (cf TypeOperation)
	 * @throws DataAccessException        Erreur d'accès aux données (requête mal
	 *                                    formée ou autre)
	 * @throws DatabaseConnexionException Erreur de connexion
	 * @throws ManagementRuleViolation    Si dépassement découvert autorisé
	 * @author IUT Blagnac
	 */
	public void insertDebit(int idNumCompte, double montant, String typeOp)
			throws DatabaseConnexionException, ManagementRuleViolation, DataAccessException {
		try {
			Connection con = LogToDatabase.getConnexion();
			CallableStatement call;

			String q = "{call Debiter (?, ?, ?, ?)}";
			// les ? correspondent aux paramètres : cf. déf procédure (4 paramètres)
			call = con.prepareCall(q);
			// Paramètres in
			call.setInt(1, idNumCompte);
			// 1 -> valeur du premier paramètre, cf. déf procédure
			call.setDouble(2, montant);
			call.setString(3, typeOp);
			// Paramètres out
			call.registerOutParameter(4, java.sql.Types.INTEGER);
			// 4 type du quatrième paramètre qui est déclaré en OUT, cf. déf procédure

			call.execute();

			int res = call.getInt(4);

			if (res != 0) { // Erreur applicative
				throw new ManagementRuleViolation(Table.Operation, Order.INSERT,
						"Erreur de règle de gestion : découvert autorisé dépassé", null);
			}
		} catch (SQLException e) {
			throw new DataAccessException(Table.Operation, Order.INSERT, "Erreur accès", e);
		}
	}

	/**
	 * Enregistrement d'un débit exceptionnel.
	 *
	 * - Ne vérifie pas que le débitAutorisé n'est pas
	 * dépassé
	 * - Enregistre l'opération
	 * - Met à jour le solde du compte.
	 *
	 * @param idNumCompte Compte débité
	 * @param montant     Montant débité
	 * @param typeOp      Libellé de l'opération effectuée (cf TypeOperation)
	 * @throws DataAccessException        Erreur d'accès aux données (requête mal
	 *                                    formée ou autre)
	 * @throws DatabaseConnexionException Erreur de connexion
	 * @throws ManagementRuleViolation    Si dépassement découvert autorisé
	 * @author KRILL Maxence
	 */
	public void insertDebitExceptionnel(int idNumCompte, double montant, String typeOp)
			throws DatabaseConnexionException, ManagementRuleViolation, DataAccessException {
		try {
			Connection con = LogToDatabase.getConnexion();
			CallableStatement call;

			String q = "{call Debit_Exceptionnel (?, ?, ?, ?)}";
			// les ? correspondent aux paramètres : cf. déf procédure (4 paramètres)
			call = con.prepareCall(q);
			// Paramètres in
			call.setInt(1, idNumCompte);
			// 1 -> valeur du premier paramètre, cf. déf procédure
			call.setDouble(2, montant);
			call.setString(3, typeOp);
			// Paramètres out
			call.registerOutParameter(4, java.sql.Types.INTEGER);
			// 4 type du quatrième paramètre qui est déclaré en OUT, cf. déf procédure

			call.execute();

			int res = call.getInt(4);

			if (res != 0) { // Erreur applicative
				throw new ManagementRuleViolation(Table.Operation, Order.INSERT,
						"Erreur de règle de gestion : découvert autorisé dépassé", null);
			}
		} catch (SQLException e) {
			throw new DataAccessException(Table.Operation, Order.INSERT, "Erreur accès", e);
		}
	}

	/**
	 * Enregistrement d'un crédit.
	 * 
	 * @param idNumCompte Compte crédité
	 * @param montant     Montant crédité
	 * @param typeOp      Libellé de l'opération effectuée (@see ConstantesIHM)
	 * @throws DatabaseConnexionException Erreur de connexion
	 * @throws ManagementRuleViolation    Si dépassement découvert autorisé
	 * @throws DataAccessException        Erreur d'accès aux données (requête mal
	 *                                    formée ou autre)
	 * @author LAMOUR Evan
	 */
	public void insertCredit(int idNumCompte, double montant, String typeOp)
			throws DatabaseConnexionException, ManagementRuleViolation, DataAccessException {
		try {
			Connection con = LogToDatabase.getConnexion();
			CallableStatement call;

			String q = "{call CREDITER (?, ?, ?, ?)}";
			// les ? correspondent aux paramètres : cf. déf procédure (4 paramètres)
			call = con.prepareCall(q);
			// Paramètres in
			call.setInt(1, idNumCompte);
			// 1 -> valeur du premier paramètre, cf. déf procédure
			call.setDouble(2, montant);
			call.setString(3, typeOp);
			// Paramètres out
			call.registerOutParameter(4, java.sql.Types.INTEGER);
			// 4 type du quatrième paramètre qui est déclaré en OUT, cf. déf procédure

			call.execute();

			int res = call.getInt(4);

			if (res != 0) { // Erreur applicative
				throw new ManagementRuleViolation(Table.Operation, Order.INSERT,
						"Erreur de règle de gestion : découvert autorisé dépassé", null);
			}
		} catch (SQLException e) {
			throw new DataAccessException(Table.Operation, Order.INSERT, "Erreur accès", e);
		}
	}

	/**
	 * Enregistrement d'un virement.
	 * 
	 * @param idCompteDebiteur  id du compte débité.
	 * @param idCompteCrediteur id du compte crédité.
	 * @param montant           Montant du virement.
	 * @throws DatabaseConnexionException Erreur de connexion
	 * @throws ManagementRuleViolation    Si dépassement découvert autorisé
	 * @throws DataAccessException        Erreur d'accès aux données (requête mal
	 *                                    formée ou autre)
	 * @author LAMOUR Evan
	 */
	public void virement(int idCompteDebiteur, int idCompteCrediteur, double montant)
			throws DatabaseConnexionException, ManagementRuleViolation, DataAccessException {
		try {
			Connection con = LogToDatabase.getConnexion();
			CallableStatement call;

			String q = "{call VIRER (?, ?, ?, ?)}";
			// les ? correspondent aux paramètres : cf. déf procédure (4 paramètres)
			call = con.prepareCall(q);
			// Paramètres in
			call.setInt(1, idCompteDebiteur);
			// 1 -> valeur du premier paramètre, cf. déf procédure
			call.setInt(2, idCompteCrediteur);
			call.setDouble(3, montant);
			// Paramètres out
			call.registerOutParameter(4, java.sql.Types.INTEGER);
			// 4 type du quatrième paramètre qui est déclaré en OUT, cf. déf procédure

			call.execute();

			int res = call.getInt(4);

			if (res != 0) { // Erreur applicative
				throw new ManagementRuleViolation(Table.Operation, Order.INSERT,
						"Erreur de règle de gestion : découvert autorisé dépassé", null);
			}
		} catch (SQLException e) {
			throw new DataAccessException(Table.Operation, Order.INSERT, "Erreur accès", e);
		}
	}

	/**
	 * Fonction utilitaire qui retourne un ordre sql "to_date" pour mettre une date
	 * dans une requête sql
	 *
	 * @param d Date (java.sql) à transformer
	 * @return Une chaine : TO_DATE('j/m/a', 'DD/MM/YYYY') : 'j/m/a' avec
	 *         "j" = jour, "m" = mois, "a" = an.
	 *         Exemple : TO_DATE('25/01/2019', 'DD/MM/YYYY')
	 * @author IUT Blagnac
	 */
	private String dateToString(Date d) {
		String sd;
		Calendar cal;
		cal = Calendar.getInstance();
		cal.setTime(d);
		sd = "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
		sd = "TO_DATE( '" + sd + "' , 'DD/MM/YYYY')";
		return sd;
	}
}