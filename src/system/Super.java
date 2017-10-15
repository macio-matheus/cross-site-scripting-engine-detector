package system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import crawler.*;

import utilsLib.jsp.JSPUtils;
import utilsLib.util.C3p0Manager;
import utilsLib.util.ConnManager;
import utilsLib.util.DbConsultManager;

import utilsLib.util.PropertiesManager;
import utilsLib.util.Utils;

public class Super {

	public static String SYNCH_INIT = "SYNCH_INIT";

	protected static PropertiesManager props;

	private static ConnManager connManager;

	private Facade facade;

	static {
		try {
			Super.init();
		} catch (Exception e) {
			JSPUtils.log("[Super - init()] Error:\n" + Utils.getStackTrace(e));
		}
	}

	public Super() throws ClassNotFoundException, SQLException,
			FileNotFoundException, IOException {
		this.facade = createmy();
	}

	public Facade getFacade() {
		return facade;
	}

	public static void init() throws FileNotFoundException, IOException {
		synchronized (SYNCH_INIT) {
			if (props == null) {
				props = new PropertiesManager(JSPUtils.SITE_REAL_PATH
						+ "/WEB-INF/base.properties");

				JSPUtils.log("Começou tudo!");
			}

			if (connManager == null) {
				try {
					connManager = new C3p0Manager(new DbConsultManager());

				} catch (Exception e) {
					JSPUtils.log("[Super] Erro ao criar connManager: "
							+ Utils.getStackTrace(e));
				}
			}
		}
	}

	protected Facade createmy() {
		Factory factory = new Factory();

		DBCodeXSSRepository codeXSSRep = new DBCodeXSSRepository(connManager,factory, factory.A_CODIGO_XSS_CLASS_ID,
				connManager.GENERAL_CONN);
		CodeXSSBusiness codeXSSBusiness = new CodeXSSBusiness(codeXSSRep);

		DBAnalyseRepository analyseRep = new DBAnalyseRepository(connManager,
				factory, factory.A_ANALISE_CLASS_ID, connManager.GENERAL_CONN);
		AnalyseBusiness analyseBusiness = new AnalyseBusiness(analyseRep);

		DBInputRepository inputRep = new DBInputRepository(connManager,
				factory, factory.A_CAMPO_CLASS_ID, connManager.GENERAL_CONN);
		InputBusiness inputBusiness = new InputBusiness(inputRep);

		DBFormRepository formRep = new DBFormRepository(connManager, factory,
				factory.A_FORMULARIO_CLASS_ID, connManager.GENERAL_CONN);
		FormBusiness formBusiness = new FormBusiness(formRep);

		DBResponseFormRepository responseFormRep = new DBResponseFormRepository(
				connManager, factory, factory.A_RESPOSTA_FORMULARIO_CLASS_ID,
				connManager.GENERAL_CONN);
		ResponseFormBusiness responseFormBusiness = new ResponseFormBusiness(
				responseFormRep);

		DBUrlFoundRepository urlFoundRep = new DBUrlFoundRepository(
				connManager, factory, factory.A_URL_CLASS_ID,
				connManager.GENERAL_CONN);
		UrlFoundBusiness urlFoundBusiness = new UrlFoundBusiness(urlFoundRep);

		DBScriptDetectedRepository scriptDetectedRep = new DBScriptDetectedRepository(
				connManager, factory, factory.A_SCRIPT_DETECTADO_CLASS_ID,
				connManager.GENERAL_CONN);
		ScriptDetectedBusiness scriptDetectedBusiness = new ScriptDetectedBusiness(
				scriptDetectedRep);

		return new Facade(this, codeXSSBusiness, analyseBusiness,
				inputBusiness, formBusiness, responseFormBusiness,
				urlFoundBusiness, scriptDetectedBusiness);
	}

	public ConnManager getConnManager() {
		return connManager;
	}

	public static PropertiesManager getProps() {
		return props;
	}
}
