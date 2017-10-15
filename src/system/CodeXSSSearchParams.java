package system;

import utilsLib.util.*;

public class CodeXSSSearchParams extends SearchParams {
	public static Factory codeXSSFactory = new Factory();
	public static int classID = codeXSSFactory.A_CODIGO_XSS_CLASS_ID;

	public CodeXSSSearchParams() {
		super(codeXSSFactory, classID);
		super.setFieldPrefix("a");
	}

	public Object clone() {
		CodeXSSSearchParams params = new CodeXSSSearchParams();
		params.copyFrom(this);
		return params;
	}
}