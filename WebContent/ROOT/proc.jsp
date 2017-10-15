<%@ include file="/include/i_utils.jsp" %>

<%
	int actionID = jspUtils.getInt("actionID", -1);
	
	
	switch(actionID){
		case 1:{

			String url = Jsoup.clean(jspUtils.getStr("url", ""), Whitelist.simpleText());

			if(!Utils.isValidUrl(url)){
				out.print("0");
				return;
			} 
			
			Analyse analyse = new Analyse(Utils.UNDEFINED_NUMBER, url, new Date());
			
			fac.addAnalyse(analyse);
			
			out.print(analyse.getId()); 
			
			fac.beginAnalyse(analyse);
			
			session.setAttribute("idAnalyse", analyse.getId());

			return;
		}
		
		// Andamento
		case 2:{
			
			int idAnalyse = jspUtils.getInt("idAnalyse", Utils.UNDEFINED_NUMBER);
			
			if (idAnalyse == Utils.UNDEFINED_NUMBER){
				out.print("Operação inválida");
				return;
			}
			
			int qtdForms = 0;
			int qtdInputs = 0;
			int qtdResponses = 0;
			int qtdUrls = 0;
			
			Form[] forms = null;
			Analyse analyse = fac.searchAnalyse(idAnalyse);
			
			forms = analyse.getForms();
			
			if (forms.length > 0){
				qtdForms = (analyse.getForms()).length;
				
				ResponseForm[] responses = null;
				// Quantidade de inputs
				for(int i = 0; i < (analyse.getForms()).length; i++) {
					
					responses = forms[i].getResponses();
					qtdInputs = qtdInputs + (forms[i].getInputs()).length;	
					qtdResponses = qtdResponses + responses.length;
					qtdUrls = qtdUrls + (forms[i].getUrls()).length;
					
					for(int j = 0; j < responses.length; j++) {
						qtdUrls = qtdUrls + (responses[j].getUrls()).length;
					}
				}
				
			}
			out.print("{\"detailJson\":{\"qtdInputs\":\"" + qtdInputs + "\", \"qtdForms\":\"" + qtdForms + "\", \"qtdResponses\":\"" + qtdResponses + "\", \"qtdUrls\":\"" + qtdUrls + "\"}}");
		}
	}
%>