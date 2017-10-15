package system;

import utilsLib.util.*;

public class ScriptDetectedSearchParams extends SearchParams  {
    public static Factory scriptDetectedFactory = new Factory();
    public static int classID = scriptDetectedFactory.A_SCRIPT_DETECTADO_CLASS_ID;

    public ScriptDetectedSearchParams() {
	super(scriptDetectedFactory, classID);
        super.setFieldPrefix("asd");
   }

	public Object clone() {
		ScriptDetectedSearchParams params = new ScriptDetectedSearchParams();
		params.copyFrom(this);
		return params;
	}
}