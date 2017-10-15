package system;

import java.util.ArrayList;

import crawler.Facade;
import utilsLib.util.FilterParam;
import utilsLib.util.Utils;
import utilsLib.util.FilterParam.COMPARE_TYPE;

public class Input {
	private int id;
	private String name;
	private String type;
	private String value;
	private String attrId;
	private int typeSuggestedValue;
	private int idForm;

	public static enum typeSuggested {
		INTEGER, CPF, STRING, EMAIL, DATE, UNDEFINED
	};

	public Input(int id, String name, String type, String value, String attrId,
			int typeSuggestedValue, int idForm) {
		setId(id);
		setName(name);
		setType(type);
		setValue(value);
		setAttrId(attrId);
		setTypeSuggestedValue(typeSuggestedValue);
		setIdForm(idForm);
	}

	public int getIdForm() {
		return idForm;
	}

	public void setIdForm(int formId) {
		this.idForm = formId;
	}

	// retorna o dado de acordo com o tipo desejado
	// retorna por exemplo, uma String com um inteiro,
	// ou string com um cpf valido
	public String generateDataToType() {

		int index = this.typeSuggestedValue;

		typeSuggested type = (typeSuggested) Utils.indexToEnum(index,
				typeSuggested.values());

		switch (type) {
		case INTEGER: {
			return "15068";
		}
		case CPF: {
			return "38105237636";
		}
		case STRING: {
			return "15068";
		}
		case EMAIL: {
			return "emailtestsofttcc@gmail.com";
		}
		case DATE: {
			return "23/09/1992";
		}
		case UNDEFINED: {
			return "UNDEFINED";
		}
		default: {
			return "";
		}
		}
	}

	// Passa um array de termos, pra ele verificar e
	public void setTypeSuggestedValue() {
		String name = this.name.toLowerCase();
		String type = this.type.toLowerCase();
		
		int typeSuggestedValue = 0;

		if (type.contains("email") || name.contains("email") || name.contains("e-mail")
				|| name.contains("mail") || name.contains("e_mail")) {
			typeSuggestedValue = typeSuggested.EMAIL.ordinal();
		} else if (name.contains("cpf") || name.contains("c_p_f")) {
			typeSuggestedValue = typeSuggested.CPF.ordinal();
		} else if (type.contains("url") || type.contains("search") || name.contains("nome") || name.contains("endere")
				|| name.contains("assunt") || name.contains("nome")) {
			typeSuggestedValue = typeSuggested.STRING.ordinal();
		} else if (type.contains("date") || name.contains("data") || name.contains("dt")
				|| name.contains("date") || name.contains("nasc")) {
			typeSuggestedValue = typeSuggested.DATE.ordinal();
		} else if (type.contains("number")) {
			typeSuggestedValue = typeSuggested.INTEGER.ordinal();
		} else {
			typeSuggestedValue = typeSuggested.UNDEFINED.ordinal();
		}
		
		this.typeSuggestedValue = typeSuggestedValue;
	}
	
	/**
	 * Retorna os xss que este campo é vulnerável
	 * @return
	 */
	public ArrayList<CodeXSS> getCodesXSSVulnerable() {
		
		Super creator = null;
		try {
			creator = new Super();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Facade fac = creator.getFacade(); 
		
		Form form = fac.searchForm(this.idForm);
		ResponseForm[] responses = form.getResponses();
		ArrayList<CodeXSS> scripts = new ArrayList<>();
		
		for (int i = 0; i < responses.length; i++) {
			scripts.add(fac.searchCodeXSS(responses[i].getIdCodeXSS()));
		}
		
		return scripts;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getAttrId() {
		return this.attrId;
	}

	public void setTypeSuggestedValue(int typeSuggestedValue) {
		this.typeSuggestedValue = typeSuggestedValue;
	}

	public int getTypeSuggestedValue() {
		return this.typeSuggestedValue;
	}

	@Override
	public String toString() {
		return "Input [id=" + id + ", name=" + name + ", type=" + type
				+ ", value=" + value + ", attrId=" + attrId
				+ ", typeSuggestedValue=" + typeSuggestedValue + ", idForm="
				+ idForm + "]";
	}

}