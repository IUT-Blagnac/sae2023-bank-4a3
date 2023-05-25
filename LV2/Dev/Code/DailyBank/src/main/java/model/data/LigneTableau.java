package model.data;

public class LigneTableau {
	public int periode;
	public String capd;
	public String interet;
	public String montantpri;
	public String montantrem;
	public String capf;

	public LigneTableau(int periode, String capd, String interet, String montantpri, String montantrem, String capf) {
		this.periode = periode;
		this.capd = capd;
		this.interet = interet;
		this.montantpri = montantpri;
		this.montantrem = montantrem;
		this.capf = capf;
	}

	public int getPeriode() {
		return periode;
	}

	public void setPeriode(int periode) {
		this.periode = periode;
	}

	public String getCapd() {
		return capd;
	}

	public void setCapd(String capd) {
		this.capd = capd;
	}

	public String getInteret() {
		return interet;
	}

	public void setInteret(String interet) {
		this.interet = interet;
	}

	public String getMontantpri() {
		return montantpri;
	}

	public void setMontantpri(String montantpri) {
		this.montantpri = montantpri;
	}

	public String getMontantrem() {
		return montantrem;
	}

	public void setMontantrem(String montantrem) {
		this.montantrem = montantrem;
	}

	public String getCapf() {
		return capf;
	}

	public void setCapf(String capf) {
		this.capf = capf;
	}
}