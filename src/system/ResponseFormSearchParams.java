package system;

import utilsLib.util.*;

public class ResponseFormSearchParams extends SearchParams  {
    public static Factory responseFormFactory = new Factory();
    public static int classID = responseFormFactory.A_RESPOSTA_FORMULARIO_CLASS_ID;

    public ResponseFormSearchParams() {
	super(responseFormFactory, classID);
        super.setFieldPrefix("arf");
   }

	public Object clone() {
		ResponseFormSearchParams params = new ResponseFormSearchParams();
		params.copyFrom(this);
		return params;
	}
}