package system;

import utilsLib.util.*;
import java.util.*;

public class FormSearchParams extends SearchParams  {
    public static Factory formFactory = new Factory();
    public static int classID = formFactory.A_FORMULARIO_CLASS_ID;

    public FormSearchParams() {
	super(formFactory, classID);
        super.setFieldPrefix("af");
   }

	public Object clone() {
		FormSearchParams params = new FormSearchParams();
		params.copyFrom(this);
		return params;
	}
}