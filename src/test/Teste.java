package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import system.Analyse;
import system.AnalyseSearchParams;
import system.Form;
import system.FormSearchParams;
import system.Input;
import system.InputSearchParams;
import system.ResponseForm;
import system.ResponseFormSearchParams;
import system.ScriptDetectedSearchParams;
import system.Super;
import utilsLib.jsp.JSPUtils;
import utilsLib.util.FilterParam;
import utilsLib.util.FilterParam.COMPARE_TYPE;
import utilsLib.util.Utils;
import crawler.ExecutorHelper;
import crawler.Facade;
import crawler.WebCrawler;

public class Teste {
	public static void main(String[] args) throws Exception {

		try {

			Super creator = new Super();
/*
			Facade fac = creator.getFacade();

			AnalyseSearchParams aparams = new AnalyseSearchParams();

			Analyse[] analyse = fac.getAnalyses(aparams);
*/
			ThreadPoolExecutor executor = null;

			//for (int i = 0; i < analyse.length; i++) {

				WebCrawler crawler = new WebCrawler(11);

				executor = ExecutorHelper.getInstance();
				executor.execute(crawler);

			//}

			while (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				Thread.sleep(1000);

				System.out.println("Tarefas em execução: "
						+ executor.getTaskCount());
				JSPUtils.log("BEGIN ANALYSEXSS", "Tarefas em execução: "
						+ executor.getTaskCount());

				System.out.println("Tarefas completas: "
						+ executor.getCompletedTaskCount());
				JSPUtils.log("BEGIN ANALYSEXSS", "Tarefas completas: "
						+ executor.getCompletedTaskCount());
			}

			System.out.println("Finished all threads");
			JSPUtils.log("BEGIN ANALYSEXSS", "Finished all threads");
			executor.shutdown();

		} catch (Exception e) {
			System.out.println(Utils.getStackTrace(e));
		}
	}

	public void result() throws Exception {

		Super creator = new Super();

		Facade fac = creator.getFacade();

		AnalyseSearchParams aparam = new AnalyseSearchParams();
		Analyse[] analyse = fac.getAnalyses(aparam);
		
		ArrayList<ResponseForm> idsResponsesPos = new ArrayList<ResponseForm>();
		
		for (int i = 0; i < analyse.length; i++) {
			
			
			FormSearchParams fparam = new FormSearchParams();
			fparam.addFilter(new FilterParam("idAnalyse", analyse[i].getId(), COMPARE_TYPE.EQUAL));
			
			Form[] form = fac.getForms(fparam);
			
			for (int j = 0; j < form.length; j++) {
				
				ResponseFormSearchParams rparam = new ResponseFormSearchParams();
				rparam.addFilter(new FilterParam("idForm", form[i].getId(), COMPARE_TYPE.EQUAL));
				
				ResponseForm[] response = fac.getResponseForms(rparam);
				
				for (int k = 0; k < response.length; k++) {
					
					if (response[k].isResult()) {
						idsResponsesPos.add(response[k]);
					}
					ScriptDetectedSearchParams sparam = new ScriptDetectedSearchParams();
					sparam.addFilter(new FilterParam("idForm", form[i].getId(), COMPARE_TYPE.EQUAL));
				}
				
				InputSearchParams iparam = new InputSearchParams();
				iparam.addFilter(new FilterParam("idForm", form[i].getId(), COMPARE_TYPE.EQUAL));
				Input[] input = fac.getInputs(iparam);
				
				for (int k = 0; k < input.length; k++) {
					
				}
			}
		}
	}

	public void testeDeCampos() {
		Document tagForm = WebCrawler
				.getDocument("http://support.hostgator.com/articles/specialized-help/technical/phpmyadmin/how-to-rename-a-database-in-phpmyadmin");

		ArrayList<Elements> inputsAllElements = new ArrayList<Elements>();

		inputsAllElements.add(tagForm.select("input[type=text]"));
		inputsAllElements.add(tagForm.getElementsByTag("textarea"));
		inputsAllElements.add(tagForm.select("input[type=hidden]"));
		inputsAllElements.add(tagForm.select("input[type=date]"));
		inputsAllElements.add(tagForm.select("input[type=url]"));
		inputsAllElements.add(tagForm.select("input[type=search]"));
		inputsAllElements.add(tagForm.select("input[type=number]"));
		inputsAllElements.add(tagForm.getElementsByTag("select"));
		inputsAllElements.add(tagForm.select("input[type=email]"));

		Input input = null;
		for (Elements inputElements : inputsAllElements) {
			for (Element fields : inputElements) {
				input = new Input(Utils.UNDEFINED_NUMBER, fields.attr("name"),
						fields.attr("type"), fields.attr("value"),
						fields.attr("id"), Utils.UNDEFINED_NUMBER,
						Utils.UNDEFINED_NUMBER);
				if (!input.getName().equals("")) {

					System.out.println(input.toString());
				}
			}
		}
	}
}