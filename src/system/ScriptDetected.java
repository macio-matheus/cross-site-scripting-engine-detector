package system;

public class ScriptDetected {
	private int id;
	private int idResponseForm;
	private int typeXSS;

	public ScriptDetected(int id, int idResponseForm, int typeXSS) {
		setId(id);
		setIdResponseForm(idResponseForm);
		setTypeXSS(typeXSS);

	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setIdResponseForm(int idResponseForm) {
		this.idResponseForm = idResponseForm;
	}

	public int getIdResponseForm() {
		return this.idResponseForm;
	}

	public void setTypeXSS(int typeXSS) {
		this.typeXSS = typeXSS;
	}

	public int getTypeXSS() {
		return this.typeXSS;
	}

	@Override
	public String toString() {
		return "ScriptDetected [id=" + id + ", idResponseForm="
				+ idResponseForm + ", typeXSS=" + typeXSS + "]";
	}
}