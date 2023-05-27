package model.data;

/**
 * Classe LigneTableau
 * 
 * @author LAMOUR Evan
 */
public class LigneTableau {
	public int periode;
	public String capd;
	public String interet;
	public String montantpri;
	public String montantrem;
	public String capf;

	/**
	 * Constructeur de la classe LigneTableau avec tous les paramètres.
	 * 
	 * @param periode    La période
	 * @param capd       Le capital de départ
	 * @param interet    L'intérêt
	 * @param montantpri Le montant du principal
	 * @param montantrem Le montant du remboursement
	 * @param capf       Le capital final
	 * @author LAMOUR Evan
	 */
	public LigneTableau(int periode, String capd, String interet, String montantpri, String montantrem, String capf) {
		this.periode = periode;
		this.capd = capd;
		this.interet = interet;
		this.montantpri = montantpri;
		this.montantrem = montantrem;
		this.capf = capf;
	}

	/**
	 * Retourne la période.
	 * 
	 * @return int La période
	 * @author LAMOUR Evan
	 */
	public int getPeriode() {
		return periode;
	}

	/**
	 * Modifie la période.
	 * 
	 * @param periode La nouvelle période à affecter à la ligne du tableau
	 * @author LAMOUR Evan
	 */
	public void setPeriode(int periode) {
		this.periode = periode;
	}

	/**
	 * Retourne le capital de départ.
	 * 
	 * @return String Le capital de départ de la ligne du tableau
	 * @author LAMOUR Evan
	 */
	public String getCapd() {
		return capd;
	}

	/**
	 * Modifie le capital de départ.
	 * 
	 * @param capd Le nouveau capital de départ
	 * @author LAMOUR Evan
	 */
	public void setCapd(String capd) {
		this.capd = capd;
	}

	/**
	 * Retourne l'intérêt.
	 * 
	 * @return String L'intérêt de la ligne du tableau
	 * @author LAMOUR Evan
	 */
	public String getInteret() {
		return interet;
	}

	/**
	 * Modifie l'intérêt.
	 * 
	 * @param interet Le nouvel intérêt
	 * @author LAMOUR Evan
	 */
	public void setInteret(String interet) {
		this.interet = interet;
	}

	/**
	 * Retourne le montant du principal.
	 * 
	 * @return String Le montant du principal de la ligne du tableau
	 * @author LAMOUR Evan
	 */
	public String getMontantpri() {
		return montantpri;
	}

	/**
	 * Modifie le montant du principal.
	 * 
	 * @param montantpri Le nouveau montant du principal
	 * @author LAMOUR Evan
	 */
	public void setMontantpri(String montantpri) {
		this.montantpri = montantpri;
	}

	/**
	 * Retourne le montant du remboursement.
	 * 
	 * @return String Le montant du remboursement de la ligne du tableau
	 * @author LAMOUR Evan
	 */
	public String getMontantrem() {
		return montantrem;
	}

	/**
	 * Modifie le montant du remboursement.
	 * 
	 * @param montantrem Le nouveau montant du remboursement
	 * @author LAMOUR Evan
	 */
	public void setMontantrem(String montantrem) {
		this.montantrem = montantrem;
	}

	/**
	 * Retourne le capital final.
	 * 
	 * @return String Le capital final de la ligne du tableau
	 * @author LAMOUR Evan
	 */
	public String getCapf() {
		return capf;
	}

	/**
	 * Modifie le capital final.
	 * 
	 * @param capf Le nouveau capital final
	 * @author LAMOUR Evan
	 */
	public void setCapf(String capf) {
		this.capf = capf;
	}
}