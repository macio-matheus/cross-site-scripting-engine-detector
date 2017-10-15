package system;

public class UrlFound {
	private int id;
	private int idForm;
	private int idResponseForm;
	private String url;
	
    public UrlFound(int id, int idForm, int idResponseForm, String url) {
		setId(id);
		setIdForm(idForm);
		setUrl(url);
		setIdResponseForm(idResponseForm);
    }
    
    public int getIdResponseForm() {
		return idResponseForm;
	}

	public void setIdResponseForm(int idResponseForm) {
		this.idResponseForm = idResponseForm;
	}
    
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
    
	public void setIdForm(int idForm) {
		this.idForm = idForm;
	}

	public int getIdForm() {
		return this.idForm;
	}
    
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	@Override
	public String toString() {
		return "UrlFound [id=" + id + ", idForm=" + idForm
				+ ", idResponseForm=" + idResponseForm + ", url=" + url + "]";
	}
}