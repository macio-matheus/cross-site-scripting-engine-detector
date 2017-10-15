package system;

import utilsLib.util.*;
import java.util.*;

public class InputSearchParams extends SearchParams  {
    public static Factory inputFactory = new Factory();
    public static int classID = inputFactory.A_CAMPO_CLASS_ID;

    public InputSearchParams() {
	super(inputFactory, classID);
        super.setFieldPrefix("ai");
   }

	public Object clone() {
		InputSearchParams params = new InputSearchParams();
		params.copyFrom(this);
		return params;
	}
}