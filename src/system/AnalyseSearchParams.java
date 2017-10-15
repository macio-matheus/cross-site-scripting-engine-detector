package system;

import utilsLib.util.*;

public class AnalyseSearchParams extends SearchParams  {
    public static Factory analyseFactory = new Factory();
    public static int classID = analyseFactory.A_ANALISE_CLASS_ID;

    public AnalyseSearchParams() {
	super(analyseFactory, classID);
        super.setFieldPrefix("aa");
   }

	public Object clone() {
		AnalyseSearchParams params = new AnalyseSearchParams();
		params.copyFrom(this);
		return params;
	}
}