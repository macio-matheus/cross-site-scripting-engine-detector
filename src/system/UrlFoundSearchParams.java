package system;

import utilsLib.util.*;

public class UrlFoundSearchParams extends SearchParams  {
    public static Factory urlFoundFactory = new Factory();
    public static int classID = urlFoundFactory.A_URL_CLASS_ID;

    public UrlFoundSearchParams() {
	super(urlFoundFactory, classID);
        super.setFieldPrefix("aur");
   }

	public Object clone() {
		UrlFoundSearchParams params = new UrlFoundSearchParams();
		params.copyFrom(this);
		return params;
	}
}