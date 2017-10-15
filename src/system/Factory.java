package system;

import utilsLib.util.ObjFactory;
import utilsLib.util.Utils;
import utilsLib.util.data.DataType;
import utilsLib.util.data.DefaultEntity;
import utilsLib.util.data.Entity;
import utilsLib.util.data.Field;
import utilsLib.util.data.ParamDataInput;

public class Factory implements ObjFactory {

	/**
	 * Código XSS
	 */
	public final static int A_CODIGO_XSS_CLASS_ID = 0;
	private static Entity codeXSSEntity;
	public final static String codeXSS_ENTITY_NAME = "A_CODIGO_XSS";

	public final static Field CODEXSS_ID_ID_FIELD = new Field("ID_CODIGO_XSS",
			"id", "", DataType.NUMBER);
	public final static Field CODEXSS_NAME_ID_FIELD = new Field("NOME", "name",
			"", DataType.STRING);
	public final static Field CODEXSS_CODE_ID_FIELD = new Field("CODIGO",
			"code", "", DataType.STRING);
	public final static Field CODEXSS_OUTPUT_ID_FIELD = new Field("SAIDA",
			"output", "", DataType.STRING);

	static {
		codeXSSEntity = new DefaultEntity(codeXSS_ENTITY_NAME,
				A_CODIGO_XSS_CLASS_ID + "", "ID");

		codeXSSEntity.addField(CODEXSS_ID_ID_FIELD);
		codeXSSEntity.addField(CODEXSS_NAME_ID_FIELD);
		codeXSSEntity.addField(CODEXSS_CODE_ID_FIELD);
		codeXSSEntity.addField(CODEXSS_OUTPUT_ID_FIELD);

	}

	/**
	 * Analyse
	 */
	public final static int A_ANALISE_CLASS_ID = 1;
	private static Entity analyseEntity;
	public final static String analyse_ENTITY_NAME = "A_ANALISE";

	public final static Field ANALYSE_ID_ID_FIELD = new Field("ID_ANALISE",
			"id", "", DataType.NUMBER);
	public final static Field ANALYSE_URL_ID_FIELD = new Field(
			"DOMINIO_OU_URL", "url", "", DataType.STRING);
	public final static Field ANALYSE_DATECREATE_ID_FIELD = new Field(
			"DATA_INICIO", "dateCreate", "", DataType.DATE_TIME);

	static {
		analyseEntity = new DefaultEntity(analyse_ENTITY_NAME,
				A_ANALISE_CLASS_ID + "", "ID");

		analyseEntity.addField(ANALYSE_ID_ID_FIELD);
		analyseEntity.addField(ANALYSE_URL_ID_FIELD);
		analyseEntity.addField(ANALYSE_DATECREATE_ID_FIELD);

	}

	/**
	 * Campos
	 */
	public final static int A_CAMPO_CLASS_ID = 2;
	private static Entity inputEntity;
	public final static String input_ENTITY_NAME = "A_CAMPO";

	public final static Field INPUT_ID_ID_FIELD = new Field("ID_CAMPO", "id",
			"", DataType.NUMBER);
	public final static Field INPUT_NAME_ID_FIELD = new Field("NOME", "name",
			"", DataType.STRING);
	public final static Field INPUT_TYPE_ID_FIELD = new Field("TIPO", "type",
			"", DataType.STRING);
	public final static Field INPUT_VALUE_ID_FIELD = new Field("VALOR",
			"value", "", DataType.STRING);
	public final static Field INPUT_ATTRID_ID_FIELD = new Field("ATTR_ID",
			"attrId", "", DataType.STRING);
	public final static Field INPUT_TYPESUGGESTEDVALUE_ID_FIELD = new Field(
			"TIPO_SUGERIDO", "typeSuggestedValue", "", DataType.NUMBER);
	public final static Field INPUT_IDFORMULARIO_ID_FIELD = new Field(
			"ID_FORMULARIO", "idForm", "", DataType.NUMBER);

	static {
		inputEntity = new DefaultEntity(input_ENTITY_NAME, A_CAMPO_CLASS_ID
				+ "", "ID");

		inputEntity.addField(INPUT_ID_ID_FIELD);
		inputEntity.addField(INPUT_NAME_ID_FIELD);
		inputEntity.addField(INPUT_TYPE_ID_FIELD);
		inputEntity.addField(INPUT_VALUE_ID_FIELD);
		inputEntity.addField(INPUT_ATTRID_ID_FIELD);
		inputEntity.addField(INPUT_TYPESUGGESTEDVALUE_ID_FIELD);
		inputEntity.addField(INPUT_IDFORMULARIO_ID_FIELD);
	}

	/**
	 * formulario
	 */
	public final static int A_FORMULARIO_CLASS_ID = 3;
	private static Entity formEntity;
	public final static String form_ENTITY_NAME = "A_FORMULARIO";

	public final static Field FORM_ID_ID_FIELD = new Field("ID_FORMULARIO",
			"id", "", DataType.NUMBER);
	public final static Field FORM_IDANALYSE_ID_FIELD = new Field("ID_ANALISE",
			"idAnalyse", "", DataType.NUMBER);
	public final static Field FORM_ACTION_ID_FIELD = new Field("ACTION_FORM",
			"action", "", DataType.STRING);
	public final static Field FORM_METHOD_ID_FIELD = new Field("METODO",
			"method", "", DataType.STRING);
	public final static Field FORM_URL_ID_FIELD = new Field("URL", "url", "",
			DataType.STRING);

	static {
		formEntity = new DefaultEntity(form_ENTITY_NAME, A_FORMULARIO_CLASS_ID
				+ "", "ID_FORMULARIO");

		formEntity.addField(FORM_ID_ID_FIELD);
		formEntity.addField(FORM_IDANALYSE_ID_FIELD);
		formEntity.addField(FORM_ACTION_ID_FIELD);
		formEntity.addField(FORM_METHOD_ID_FIELD);
		formEntity.addField(FORM_URL_ID_FIELD);

	}

	/**
	 * response form
	 */
	public final static int A_RESPOSTA_FORMULARIO_CLASS_ID = 4;
	private static Entity responseFormEntity;
	public final static String responseForm_ENTITY_NAME = "A_RESPOSTA_FORMULARIO";

	public final static Field RESPONSEFORM_ID_ID_FIELD = new Field(
			"ID_RESPOSTA", "id", "", DataType.NUMBER);
	public final static Field RESPONSEFORM_IDINPUT_ID_FIELD = new Field(
			"ID_CAMPO", "idInput", "", DataType.NUMBER);
	public final static Field RESPONSEFORM_IDFORM_ID_FIELD = new Field(
			"ID_FORMULARIO", "idForm", "", DataType.NUMBER);
	public final static Field RESPONSEFORM_RESPONSETEXT_ID_FIELD = new Field(
			"RESPOSTA", "responseText", "", DataType.STRING);
	public final static Field RESPONSEFORM_CODESTATUS_ID_FIELD = new Field(
			"STATUS_SERVIDOR", "codeStatus", "", DataType.STRING);
	public final static Field RESPONSEFORM_IDCODIGOXSS_ID_FIELD = new Field(
			"ID_CODIGO_XSS", "idCodeXSS", "", DataType.NUMBER);
	public final static Field RESPONSEFORM_RESULT_ID_FIELD = new Field(
			"RESULTADO", "result", "", DataType.BOOL);

	static {
		responseFormEntity = new DefaultEntity(responseForm_ENTITY_NAME,
				A_RESPOSTA_FORMULARIO_CLASS_ID + "", "ID_RESPOSTA");

		responseFormEntity.addField(RESPONSEFORM_ID_ID_FIELD);
		responseFormEntity.addField(RESPONSEFORM_IDINPUT_ID_FIELD);
		responseFormEntity.addField(RESPONSEFORM_IDFORM_ID_FIELD);
		responseFormEntity.addField(RESPONSEFORM_RESPONSETEXT_ID_FIELD);
		responseFormEntity.addField(RESPONSEFORM_CODESTATUS_ID_FIELD);
		responseFormEntity.addField(RESPONSEFORM_IDCODIGOXSS_ID_FIELD);
		responseFormEntity.addField(RESPONSEFORM_RESULT_ID_FIELD);

	}
	/**
	 * URL encontrada
	 */
	public final static int A_URL_CLASS_ID = 5;
	private static Entity urlFoundEntity;
	public final static String urlFound_ENTITY_NAME = "A_URL";

	public final static Field URLFOUND_ID_ID_FIELD = new Field("ID_URL", "id",
			"", DataType.NUMBER);
	public final static Field URLFOUND_IDFORM_ID_FIELD = new Field(
			"ID_FORMULARIO", "idForm", "", DataType.NUMBER);
	public final static Field URLFOUND_IDRESPONSE_ID_FIELD = new Field(
			"ID_RESPOSTA", "idResponse", "", DataType.NUMBER);
	public final static Field URLFOUND_URL_ID_FIELD = new Field("NOVA_URL",
			"url", "", DataType.STRING);

	static {
		urlFoundEntity = new DefaultEntity(urlFound_ENTITY_NAME, A_URL_CLASS_ID
				+ "", "ID_URL");

		urlFoundEntity.addField(URLFOUND_ID_ID_FIELD);
		urlFoundEntity.addField(URLFOUND_IDFORM_ID_FIELD);
		urlFoundEntity.addField(URLFOUND_IDRESPONSE_ID_FIELD);
		urlFoundEntity.addField(URLFOUND_URL_ID_FIELD);

	}

	/**
	 * Script detectado
	 */
	public final static int A_SCRIPT_DETECTADO_CLASS_ID = 6;
	private static Entity scriptDetectedEntity;
	public final static String scriptDetected_ENTITY_NAME = "A_SCRIPT_DETECTADO";

	public final static Field SCRIPTDETECTED_ID_ID_FIELD = new Field(
			"ID_SCRIPT_DETECTADO", "id", "", DataType.NUMBER);
	public final static Field SCRIPTDETECTED_IDRESPONSEFORM_ID_FIELD = new Field(
			"ID_RESPOSTA", "idResponseForm", "", DataType.NUMBER);
	public final static Field SCRIPTDETECTED_TYPEXSS_ID_FIELD = new Field(
			"TIPO", "typeXSS", "", DataType.NUMBER);

	static {
		scriptDetectedEntity = new DefaultEntity(scriptDetected_ENTITY_NAME,
				A_SCRIPT_DETECTADO_CLASS_ID + "", "ID_SCRIPT_DETECTADO");

		scriptDetectedEntity.addField(SCRIPTDETECTED_ID_ID_FIELD);
		scriptDetectedEntity.addField(SCRIPTDETECTED_IDRESPONSEFORM_ID_FIELD);
		scriptDetectedEntity.addField(SCRIPTDETECTED_TYPEXSS_ID_FIELD);

	}

	public Entity getClassEntity(int classID) {
		switch (classID) {
		case A_SCRIPT_DETECTADO_CLASS_ID:
			return scriptDetectedEntity;
		case A_URL_CLASS_ID:
			return urlFoundEntity;
		case A_RESPOSTA_FORMULARIO_CLASS_ID:
			return responseFormEntity;
		case A_FORMULARIO_CLASS_ID:
			return formEntity;
		case A_CAMPO_CLASS_ID:
			return inputEntity;
		case A_CODIGO_XSS_CLASS_ID:
			return codeXSSEntity;
		case A_ANALISE_CLASS_ID:
			return analyseEntity;
		default:
			throw new IllegalArgumentException("Identificador de "
					+ "classe inválido.");
		}
	}

	public Entity getEntity(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException();
		}
		if (obj instanceof ScriptDetected) {
			ScriptDetected scriptDetected = (ScriptDetected) obj;

			Entity e = (Entity) getClassEntity(A_SCRIPT_DETECTADO_CLASS_ID)
					.clone();
			e.getField(SCRIPTDETECTED_ID_ID_FIELD).setValue(
					scriptDetected.getId());
			e.getField(SCRIPTDETECTED_IDRESPONSEFORM_ID_FIELD).setValue(
					scriptDetected.getIdResponseForm());
			e.getField(SCRIPTDETECTED_TYPEXSS_ID_FIELD).setValue(
					scriptDetected.getTypeXSS());

			return e;
		} else if (obj instanceof UrlFound) {
			UrlFound urlFound = (UrlFound) obj;

			Entity e = (Entity) getClassEntity(A_URL_CLASS_ID).clone();
			e.getField(URLFOUND_ID_ID_FIELD).setValue(urlFound.getId());
			e.getField(URLFOUND_IDFORM_ID_FIELD).setValue(urlFound.getIdForm());
			e.getField(URLFOUND_IDRESPONSE_ID_FIELD).setValue(
					urlFound.getIdResponseForm());
			e.getField(URLFOUND_URL_ID_FIELD).setValue(urlFound.getUrl());

			return e;
		} else if (obj instanceof ResponseForm) {
			ResponseForm responseForm = (ResponseForm) obj;

			Entity e = (Entity) getClassEntity(A_RESPOSTA_FORMULARIO_CLASS_ID)
					.clone();
			e.getField(RESPONSEFORM_ID_ID_FIELD).setValue(responseForm.getId());
			e.getField(RESPONSEFORM_IDINPUT_ID_FIELD).setValue(
					responseForm.getIdInput());
			e.getField(RESPONSEFORM_IDFORM_ID_FIELD).setValue(
					responseForm.getIdForm());
			e.getField(RESPONSEFORM_RESPONSETEXT_ID_FIELD).setValue(
					responseForm.getResponseText());
			e.getField(RESPONSEFORM_CODESTATUS_ID_FIELD).setValue(
					responseForm.getCodeStatus());
			e.getField(RESPONSEFORM_IDCODIGOXSS_ID_FIELD).setValue(
					responseForm.getIdCodeXSS());
			e.getField(RESPONSEFORM_RESULT_ID_FIELD).setValue(
					responseForm.isResult());

			return e;
		} else if (obj instanceof Form) {
			Form form = (Form) obj;

			Entity e = (Entity) getClassEntity(A_FORMULARIO_CLASS_ID).clone();
			e.getField(FORM_ID_ID_FIELD).setValue(form.getId());
			e.getField(FORM_IDANALYSE_ID_FIELD).setValue(form.getIdAnalyse());
			e.getField(FORM_ACTION_ID_FIELD).setValue(form.getAction());
			e.getField(FORM_METHOD_ID_FIELD).setValue(form.getMethod());
			e.getField(FORM_URL_ID_FIELD).setValue(form.getUrl());

			return e;
		} else if (obj instanceof Input) {
			Input input = (Input) obj;

			Entity e = (Entity) getClassEntity(A_CAMPO_CLASS_ID).clone();
			e.getField(INPUT_ID_ID_FIELD).setValue(input.getId());
			e.getField(INPUT_NAME_ID_FIELD).setValue(input.getName());
			e.getField(INPUT_TYPE_ID_FIELD).setValue(input.getType());
			e.getField(INPUT_VALUE_ID_FIELD).setValue(input.getValue());
			e.getField(INPUT_ATTRID_ID_FIELD).setValue(input.getAttrId());
			e.getField(INPUT_TYPESUGGESTEDVALUE_ID_FIELD).setValue(
					input.getTypeSuggestedValue());
			e.getField(INPUT_IDFORMULARIO_ID_FIELD).setValue(input.getIdForm());

			return e;
		} else if (obj instanceof Analyse) {
			Analyse analyse = (Analyse) obj;

			Entity e = (Entity) getClassEntity(A_ANALISE_CLASS_ID).clone();
			e.getField(ANALYSE_ID_ID_FIELD).setValue(analyse.getId());
			e.getField(ANALYSE_URL_ID_FIELD).setValue(analyse.getUrl());
			e.getField(ANALYSE_DATECREATE_ID_FIELD).setValue(
					analyse.getDateCreate());

			return e;
		} else if (obj instanceof CodeXSS) {
			CodeXSS codeXSS = (CodeXSS) obj;

			Entity e = (Entity) getClassEntity(A_CODIGO_XSS_CLASS_ID).clone();
			e.getField(CODEXSS_ID_ID_FIELD).setValue(codeXSS.getId());
			e.getField(CODEXSS_NAME_ID_FIELD).setValue(codeXSS.getName());
			e.getField(CODEXSS_CODE_ID_FIELD).setValue(codeXSS.getCode());
			e.getField(CODEXSS_OUTPUT_ID_FIELD).setValue(codeXSS.getOutput());

			return e;
		} else {
			throw new IllegalArgumentException("O objeto deve ser algum do "
					+ "módulo category.");
		}
	}

	public Object paramDataToObj(ParamDataInput pdi) {
		int classID = Utils.parseInt(pdi.getExtra());

		switch (classID) {
		case A_SCRIPT_DETECTADO_CLASS_ID: {
			return new ScriptDetected(
					pdi.getInt(SCRIPTDETECTED_ID_ID_FIELD.getName()),
					pdi.getInt(SCRIPTDETECTED_IDRESPONSEFORM_ID_FIELD.getName()),
					pdi.getInt(SCRIPTDETECTED_TYPEXSS_ID_FIELD.getName())

			);
		}
		case A_URL_CLASS_ID: {
			return new UrlFound(pdi.getInt(URLFOUND_ID_ID_FIELD.getName()),
					pdi.getInt(URLFOUND_IDFORM_ID_FIELD.getName()),
					pdi.getInt(URLFOUND_IDRESPONSE_ID_FIELD.getName()),
					pdi.getStr(URLFOUND_URL_ID_FIELD.getName()));
		}
		case A_RESPOSTA_FORMULARIO_CLASS_ID: {
			return new ResponseForm(pdi.getInt(RESPONSEFORM_ID_ID_FIELD
					.getName()), pdi.getInt(RESPONSEFORM_IDINPUT_ID_FIELD
					.getName()), pdi.getInt(RESPONSEFORM_IDFORM_ID_FIELD
					.getName()), pdi.getStr(RESPONSEFORM_RESPONSETEXT_ID_FIELD
					.getName()), pdi.getStr(RESPONSEFORM_CODESTATUS_ID_FIELD
					.getName()), pdi.getInt(RESPONSEFORM_IDCODIGOXSS_ID_FIELD
					.getName()), pdi.getBoo(RESPONSEFORM_RESULT_ID_FIELD
					.getName())

			);
		}
		case A_FORMULARIO_CLASS_ID: {
			return new Form(pdi.getInt(FORM_ID_ID_FIELD.getName()),
					pdi.getInt(FORM_IDANALYSE_ID_FIELD.getName()),
					pdi.getStr(FORM_ACTION_ID_FIELD.getName()),
					pdi.getStr(FORM_METHOD_ID_FIELD.getName()),
					pdi.getStr(FORM_URL_ID_FIELD.getName())

			);
		}
		case A_CODIGO_XSS_CLASS_ID: {
			return new CodeXSS(pdi.getInt(CODEXSS_ID_ID_FIELD.getName()),
					pdi.getStr(CODEXSS_NAME_ID_FIELD.getName()),
					pdi.getStr(CODEXSS_CODE_ID_FIELD.getName()),
					pdi.getStr(CODEXSS_OUTPUT_ID_FIELD.getName())

			);
		}

		case A_ANALISE_CLASS_ID: {
			return new Analyse(pdi.getInt(ANALYSE_ID_ID_FIELD.getName()),
					pdi.getStr(ANALYSE_URL_ID_FIELD.getName()),
					pdi.getDateTime(ANALYSE_DATECREATE_ID_FIELD.getName())

			);
		}

		case A_CAMPO_CLASS_ID: {
			return new Input(pdi.getInt(INPUT_ID_ID_FIELD.getName()),
					pdi.getStr(INPUT_NAME_ID_FIELD.getName()),
					pdi.getStr(INPUT_TYPE_ID_FIELD.getName()),
					pdi.getStr(INPUT_VALUE_ID_FIELD.getName()),
					pdi.getStr(INPUT_ATTRID_ID_FIELD.getName()),
					pdi.getInt(INPUT_TYPESUGGESTEDVALUE_ID_FIELD.getName()),
					pdi.getInt(INPUT_IDFORMULARIO_ID_FIELD.getName()));
		}

		default:
			throw new IllegalArgumentException("Identificlogor de "
					+ "classe inválido.");
		}
	}

	public Object[] getArray(int classID, int size) {
		switch (classID) {
		case A_SCRIPT_DETECTADO_CLASS_ID:
			return new ScriptDetected[size];
		case A_URL_CLASS_ID:
			return new UrlFound[size];
		case A_RESPOSTA_FORMULARIO_CLASS_ID:
			return new ResponseForm[size];
		case A_FORMULARIO_CLASS_ID:
			return new Form[size];
		case A_CAMPO_CLASS_ID:
			return new Input[size];
		case A_ANALISE_CLASS_ID:
			return new Analyse[size];
		case A_CODIGO_XSS_CLASS_ID:
			return new CodeXSS[size];
		default:
			throw new IllegalArgumentException("Param: classID");
		}
	}
}
