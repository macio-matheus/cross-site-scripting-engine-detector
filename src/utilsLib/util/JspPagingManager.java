package utilsLib.util;

import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Gerenciador de paginação para JSP. Pega dados do request e de características
 * da paginação por <code>PagingInfo</code> e oferece serviços para a
 * navegação.
 */
public class JspPagingManager extends Exception {
    private HttpServletRequest request;

    private PagingInfo pagingInfo;
    private int defaultPageSize;
    private int maxRegPerPage;
    private String currPageParamName = "currPage";
    private String pageSizeParamName = "pageSize";
    private String regCountParamName = "regCount";

    // Propriedades de formação de ancoras.
    private String baseUrl;
    private String htmlPatternATag;

    /**
     * Construtor padrão da classe. Inicializa a quantidade máxima de
     * registros por página para 20.
     */
    public JspPagingManager() {
        this.maxRegPerPage = 100;
        this.defaultPageSize = 15;
    }

    /**
     * Inicializa a paginação, de acordo com os parametros passados.
     *
     * @param pageContext Contexto da página para obtenção dos parametros e
     *                    objetos necessarios.
     */
    public void initPaging(PageContext pageContext) {
        if (pageContext == null) {
            throw new IllegalArgumentException(
                    "Parametro inválido: pageContext");
        }

        this.request = (HttpServletRequest) pageContext.getRequest();

        this.pagingInfo = new PagingInfo();
        this.pagingInfo.setCurrPage(Utils.parseInt(
                request.getParameter(getCurrPageParamName()), 1));
        this.pagingInfo.setPageSize(
                Utils.parseInt(request.getParameter(getPageSizeParamName()),
                               defaultPageSize));
        this.pagingInfo.setRegsCount(
                Utils.parseInt(request.getParameter(getRegCountParamName()),
                               Utils.UNDEFINED_NUMBER));
    }

    public PagingInfo getPagingInfo() {
        return pagingInfo;
    }

    public void setPagingInfo(PagingInfo pagingInfo) {
        this.pagingInfo = pagingInfo;
    }

    public int getMaxRegPerPage() {
        return maxRegPerPage;
    }

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        if (defaultPageSize < 1) {
            throw new IllegalArgumentException(
                    "Parametro inválido: defaultPageSize. Ele deve ser maior ou igual a 1");
        }
        this.defaultPageSize = defaultPageSize;
    }

    public void setMaxRegPerPage(int maxRegPerPage) {
        if (maxRegPerPage < 1) {
            throw new IllegalArgumentException(
                    "Parametro inválido: maxRegPerPage. Ele deve ser maior ou igual a 1");
        }
        this.maxRegPerPage = maxRegPerPage;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCurrPageParamName() {
        return currPageParamName;
    }

    public void setCurrPageParamName(String currPageParamName) {
        this.currPageParamName = currPageParamName;
    }

    public String getPageSizeParamName() {
        return pageSizeParamName;
    }

    public void setPageSizeParamName(String pageSizeParamName) {
        this.pageSizeParamName = pageSizeParamName;
    }

    public String getRegCountParamName() {
        return regCountParamName;
    }

    public void setRegCountParamName(String regCountParamName) {
        this.regCountParamName = regCountParamName;
    }

    /**
     * Retorna uma URL base já com alguns parametros obrigatórios para
     * a navegação, que são: quantidade de registros e
     * tamanho da página.
     *
     * @return String da url.
     */
    public String getConfiguredBaseUrl() {
        String configuredBaseUrl = baseUrl + "&" + this.getRegCountParamName()
                                   + "=" + pagingInfo.getRegsCount();
        if (pagingInfo.getCurrPageSize() != this.getDefaultPageSize()) {
            configuredBaseUrl += "&" + this.getPageSizeParamName()
                    + "=" + pagingInfo.getPageSize();
        }

        return configuredBaseUrl;
    }

    /**
     * Retorna a tag de ancore (<a>) padrão para ser utilizada na geração
     * do HTML. Ela possui marcações para onde colocar o texto e onde colocar
     * a URL destino já configurada. Entao, um exemplo
     * seria (as marcações são indicadas por <#urlDestin>:
     *
     * <p>
     * <a href= "<#urlDestin>"><#linkText></a>"
     *
     * @return  Tag padrão html.
     */
    public String getHtmlPatternATag() {
        return "<a href=\"<#urlDestin>\" class=\"links_paginacao\">"
                + "<#linkText></a>";
    }

    /**
     * Obtem o link de volta, de acodo com a url base. Utiliza
     * <code>getHtmlPatternATag()</code> para formar a âncora.
     *
     * @return  HTML com o link de retorn para página anterior.
     */
    public String getHtmlBackLink() {
        String baseUrl2 = getConfiguredBaseUrl() + "&" + getCurrPageParamName() +
                          "=";
        String html = "";
        // Testa se há palavra anterior.
        if (pagingInfo.getCurrPage() > 1) {
            html = Utils.format(
                    getHtmlPatternATag(),
                    new String[] {baseUrl2 + (pagingInfo.getCurrPage() - 1),
                    "Anterior"}
                    ,
                    new String[] {"<#urlDestin>", "<#linkText>"});
        } else {
            html = "";
        }

        return html;
    }

    /**
     * Obtem o link de volta para a primeira página, de acodo com a url base.
     * Utiliza <code>getHtmlPatternATag()</code> para formar a âncora.
     *
     * @return  HTML com o link de retorn para página anterior.
     */
    public String getHtmlFirstLink() {
        String baseUrl2 = getConfiguredBaseUrl() + "&" +
                          getCurrPageParamName() + "=";
        String html = "";
        // Testa se há palavra anterior.
        if (pagingInfo.getCurrPage() > 1) {
            html = Utils.format(
                    getHtmlPatternATag(),
                    new String[] {baseUrl2 + 1, "Primeira"}
                    ,
                    new String[] {"<#urlDestin>", "<#linkText>"});
        } else {
            html = "";
        }

        return html;
    }

    /**
     * Obtem o link da próxima página, de acodo com a url base. Utiliza
     * <code>getHtmlPatternATag()</code> para formar a âncora.
     *
     * @return  HTML com o link de retorn para página anterior.
     */
    public String getHtmlNextLink() {
        return getHtmlNextLink("Próxima");
    }


    public String getNextURL() {
        String baseUrl2 = getConfiguredBaseUrl() + "&" + getCurrPageParamName() +
                          "=";
        return baseUrl2 + (pagingInfo.getCurrPage() + 1);
    }

    public String getBackURL() {
        String baseUrl2 = getConfiguredBaseUrl() + "&" + getCurrPageParamName() +
                          "=";
        return baseUrl2 + (pagingInfo.getCurrPage() - 1);
    }

    public String getFirstURL() {
        String baseUrl2 = getConfiguredBaseUrl() + "&" +
                          getCurrPageParamName() + "=";
        return baseUrl2 + 1;
    }

    public String getLastURL() {
        String baseUrl2 = getConfiguredBaseUrl() + "&" + getCurrPageParamName() +
                          "=";
        return baseUrl2 + pagingInfo.getPagesCount();
    }

    public String getURL(int page) {
        String baseUrl2 = getConfiguredBaseUrl() + "&" + getCurrPageParamName() +
                          "=";
        return baseUrl2 + page;
    }

    public String getHtmlNextLink(String text) {
        String baseUrl2 = getConfiguredBaseUrl() + "&" + getCurrPageParamName() +
                          "=";
        String html = "";
        // Testa se há palavra anterior.
        if (pagingInfo.getCurrPage() < pagingInfo.getPagesCount()) {
            html = Utils.format(
                    getHtmlPatternATag(),
                    new String[] {baseUrl2 + (pagingInfo.getCurrPage() + 1),
                    text}
                    ,
                    new String[] {"<#urlDestin>", "<#linkText>"});
        } else {
            html = "";
        }

        return html;
    }

    /**
     * Obtem o link da última página, de acodo com a url base. Utiliza
     * <code>getHtmlPatternATag()</code> para formar a âncora.
     *
     * @return  HTML com o link de retorn para página anterior.
     */
    public String getHtmlLastLink() {
        String baseUrl2 = getConfiguredBaseUrl() + "&" + getCurrPageParamName() +
                          "=";
        String html = "";
        // Testa se há palavra anterior.
        if (pagingInfo.getCurrPage() < pagingInfo.getPagesCount()) {
            html = Utils.format(
                    getHtmlPatternATag(),
                    new String[] {baseUrl2 + pagingInfo.getPagesCount(),
                    "Última"}
                    ,
                    new String[] {"<#urlDestin>", "<#linkText>"});
        } else {
            html = "";
        }

        return html;
    }

    /**
     * Obtém informações gerais sobre a paginação.
     *
     * @return HTML contendo as informações.
     */
    public String getHtmlGeneralInfo() {
        String html = pagingInfo.getRegsCount() +
                      " registro(s) encontrado(s). "
                      + "Exibindo " + pagingInfo.getFirstRegPos() + " até "
                      + pagingInfo.getLastRegPos()
                      + "<br>"
                      + "<span class = \"textos_meio_destaque\">"
                      + "     Página " + pagingInfo.getCurrPage() + " de " +
                      pagingInfo.getPagesCount()
                      + "</span>";
        return html;
    }

    /**
     * Paginação por páginas. Utiliza <code>getHtmlPatternATag()</code> para
     * formar as âncoras.
     *
     * @param totalVisibleCount  Número de páginas que é permitido visualizar.
     * @return                    HTML com links para pular entre as páginas.
     */
    public String getHtmlPagingNav(int totalVisibleCount) {
        String html = "";
        String baseUrl2 = getConfiguredBaseUrl() + "&" + getCurrPageParamName() +
                          "=";

        int sel = pagingInfo.getCurrPage(); // Página selecionada.
        int totalPage = pagingInfo.getPagesCount(); // Total de páginas.
        int maxVisiblePage = sel + (totalVisibleCount / 2); // A última página a ser visualizada.
        int i = 1; // Contador para o looping

        // Ajustes nos valores.

        // Caso o selecionado esteja mais que no meio, realiza um descolocamento das páginas visíveis.
        if (sel > totalVisibleCount / 2) {
            i = sel - (totalVisibleCount / 2);
            if (totalVisibleCount % 2 == 0) {
                i++;
            }
        } else {
            maxVisiblePage = totalVisibleCount;
        }

        // Eventuais consertos do descolcamente, de acordo se extrapolou o total de páginas.
        if (maxVisiblePage >= totalPage) {
            maxVisiblePage = totalPage;
            i = maxVisiblePage - (totalVisibleCount) + 1;
        }

        if (i < 1) {
            i = 1;
        }

        for (; i <= maxVisiblePage; i++) {
            if (i != pagingInfo.getCurrPage()) {
                html += Utils.format(
                        getHtmlPatternATag(),
                        new String[] {baseUrl2 + i, i + ""}
                        ,
                        new String[] {"<#urlDestin>", "<#linkText>"}) + " ";
            } else {
                html += "[<b>" + i + "</b>] ";
            }
        }

        return html;
    }

    /**
     * Retorna um barra de navegação completa, contendo navegador para frente
     * e para trás, páginas e dados gerais. Utiliza <code>getHtmlPatternATag()</code> para
     * formar as âncoras.
     *
     * @return  HTML de navegação.
     */
    public String getHtmlNavBar() {
        String html =
                "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
                "        <tr bgcolor = \"#D9E1E8\"> " +
                "          <td class=\"conteudo_secao\" width = \"15%\">&nbsp;" +
                getHtmlBackLink() +
                "<BR><br>&nbsp;" +
                getHtmlFirstLink() +
                "          </td>" +
                "          <td class=\"conteudo_secao\" width = \"70%\" align = \"center\">" +
                getHtmlGeneralInfo() +
                "<br>" +
                getHtmlPagingNav(20) +
                "          </td>" +
                "          <td class=\"conteudo_secao\" width = \"15%\" align = \"right\">" +
                getHtmlNextLink() +
                "&nbsp;<br><br>" +
                getHtmlLastLink() +
                "&nbsp;" +
                "          </td>" +
                "        </tr>" +
                "</table>";

        return html;
    }

    /**
     * Retorna o HTML de uma barra definidora de registros por páginas.
     *
     * @param explainingText Texto explanador do que representa a estatística.
     *                       Possui o marcador <code><#pageRegsCount></code> para
     *                       ser posto a quantidade de registros nesta página.
     * @return               HTML do definidor.
     */
    public String getHtmlPageSizeDefiner(String explainingText) {
        String explaningText = Utils.formatIfNull(explainingText);
        explainingText = Utils.replace(explainingText, "<#pageRegsCount>",
                                       pagingInfo.getPageSize() + "", true);
        String scriptUrl = baseUrl + "&" + this.getRegCountParamName() + "&" +
                           this.getCurrPageParamName() + "=1&" +
                           this.getPageSizeParamName() + "=";
        String html =
                "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
                "        <tr> " +
                "          <td class=\"conteudo_subtitulo\">" + explainingText +
                "</td>" +
                "          <a name = \"qtdList\">" +
                "<script>" +
                "function goToPagingStat(regsCount) {" +
                "        location.href = \"" + scriptUrl +
                "\" + regsCount + \"\";" +
                "}" +
                "</script>" +
                "          <form name = \"form_qtdList\">" +
                "          <td class=\"conteudo_subtitulo\" align = \"right\">" +
                "                        <select name=\"qtdList\" class=\"form_comboBox_calendario\" onchange = \"goToPagingStat(document.form_qtdList.qtdList.value)\">" +
                "                                <option value=\"15\" SELECTED>Exibir 15</option>" +
                "                                <option value=\"25\" SELECTED>Exibir 25</option>" +
                "                                <option value=\"50\" SELECTED>Exibir 50</option>" +
                "                                <option value=\"100\" SELECTED>Exibir 100</option>" +
                "                        </select>" +
                "          </td>" +
                "          </form>  " +
                "          <script>" +
                "                document.form_qtdList.qtdList.value = \"" +
                pagingInfo.getPageSize() + "\"" +
                "          </script>" +
                "        </tr>" +
                "        <tr> " +
                "          <td><img src=\"/images/sp.gif\" width=\"15\" height=\"15\"></td>" +
                "        </tr>" +
                "</table>";

        return html;
    }

    public void setHtmlPatternATag(String htmlPatternATag) {
        this.htmlPatternATag = htmlPatternATag;
    }
}
