package utilsLib.util;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;

/**
 * Implementador do gatilho de extra��o / povoamento de dados para ter sa�da
 * em JSP..
 */
public class JspDataExtractingListener implements DataExtractingListener {
    private JspWriter pageOut;
    private boolean alreadyBegin;
    private int showBasis;
    private String outMask;

    public JspDataExtractingListener(JspWriter pageOut, int showBasis) {
        this(pageOut, showBasis, "(<#fileName>, <#currRegister>)<br>");
    }


    public JspDataExtractingListener(JspWriter pageOut, int showBasis,
                                     String outMask) {
        setAlreadyBegin(false);
        setPageOut(pageOut);
        setShowBasis(showBasis);
        setOutMask(outMask);
    }

    public void extractDataEvent(int extractedCount, String path) {
        if (pageOut == null) {
            throw new RuntimeException("N�o h� sa�da para os dados.");
        }

        try {
            if (!alreadyBegin) {
                pageOut.print(
                        "Log de extra��o de informa��es: BD -> XML.<br><hr>");
                alreadyBegin = true;
            }
            pageOut.print("Extra��o atual: " + extractedCount +
                          "<br>Caminho do arquivo destino: " + path +
                          "<br><hr>");
            pageOut.flush();
        } catch (IOException error) {
            throw new RuntimeException("Erro na sa�da. Erro de IO: " +
                                       error.getMessage());
        }
    }

    public void populateDataEvent(String fileName, int currRegister,
                                  String sql) {
        if (pageOut == null) {
            throw new RuntimeException("N�o h� sa�da para os dados.");
        }


        try {
            if (!alreadyBegin) {
                pageOut.print("Log de extra��o de informa��es: XML -> BD.<br>(�ndice da tabela atual, registro atual); ...<br><hr>");
                alreadyBegin = true;
            }

            if ((currRegister - 1) % showBasis == 0) {
                pageOut.print(Utils.format(outMask,
                                           new String[] {fileName,
                                           currRegister + "", sql}
                                           , new String[] {
                                           "<#fileName>", "<#currRegister>",
                                           "<#sql>"
                }
                        ));
                pageOut.flush();
            }

        } catch (IOException error) {
            throw new RuntimeException("Erro na sa�da. Erro de IO: " +
                                       error.getMessage());
        }
    }

    public void setPageOut(JspWriter pageOut) {
        if (pageOut == null) {
            throw new IllegalArgumentException("Parametro inv�lido: pageOut");
        }
        this.pageOut = pageOut;
    }

    public void setAlreadyBegin(boolean alreadyBegin) {
        this.alreadyBegin = alreadyBegin;
    }

    public boolean isAlreadyBegin() {
        return alreadyBegin;
    }

    public int getShowBasis() {
        return showBasis;
    }

    public void setShowBasis(int showBasis) {
        if (showBasis <= 0) {
            new IllegalArgumentException("Param: showBasis <= 0");
        }
        this.showBasis = showBasis;
    }

    /**
     * M�scara de saida. <#fileName>, <#currRegister>, <#sql>
     *
     * @return
     */
    public String getOutMask() {
        return outMask;
    }

    public void setOutMask(String outMask) {
        this.outMask = outMask;
    }
}
