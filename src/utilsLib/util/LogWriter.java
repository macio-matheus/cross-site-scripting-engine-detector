package utilsLib.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import utilsLib.util.data.Entity;

import com.Ostermiller.util.CSVPrinter;

public class LogWriter {
    private String fileName;
    private String fileIDMask;
    private boolean append;

    public LogWriter() {
        append = true;
    }

    public LogWriter(String fileName, String fileIDMask, boolean append) {
        setAppend(append);
        setFileName(fileName);
        setFileIDMask(fileIDMask);
    }

    public LogWriter(String fileName, String fileIDMask) {
        setAppend(true);
        setFileName(fileName);
        setFileIDMask(fileIDMask);
    }

    public void log(Entity entity) throws IOException {
        log("", entity);
    }

    public void log(String fileID, Entity entity) throws IOException {
        log(fileID, new Entity[] {entity});
    }

    /**
     * @param out         Destino do que se deseja escrever.
     * @param valuesRows  Linhas de valores para escrita.
     * @param toExcel     Se o CSV é para ser montado no formato que o Excel
     *                    aceita.
     * @throws IOException
     */
    public void log(OutputStream out, String[][] valuesRows, boolean toExcel) throws
            IOException {
        if (valuesRows == null) {
            throw new IllegalArgumentException("Parametro inválido: valuesRows");
        }

        if (out == null) {
            throw new IllegalArgumentException("Parametro inválido: out");
        }

        OutputStreamWriter os = new OutputStreamWriter(out,
                Utils.PATTERN_STRING_ENCODING);
        try {
            // Escreve no CSV
            if (toExcel) {
                for (int i = 0; i < valuesRows.length; i++) {
                    for (int j = 0; j < valuesRows[i].length; j++) {
                        String text = "\"" +
                                      Utils.replace(valuesRows[i][j], "\"",
                                "\"\"", true) +
                                      "\"";

                        if (j < valuesRows[i].length - 1) {
                            text += ";";
                        }
                        os.write(text);
                    }

                    if (i < valuesRows.length - 1) {
                        os.write(Utils.getBreakLine());
                    }
                }
            } else {
                CSVPrinter csvp = new CSVPrinter(os);

                for (int i = 0; i < valuesRows.length; i++) {
                    csvp.println(valuesRows[i]);
                }
            }

        } finally {
            os.close();
        }
    }

    public void log(String[] values, boolean logAppendMode) throws
            IOException {
        this.log(null, values, logAppendMode);
    }

    public void log(String fileID, String[] values) throws IOException {
        log(fileID, values, true);
    }

    public void log(String fileID, String[] values, boolean logAppendMode) throws
            IOException {
        String filePath = null;
        if (fileName == null) {
            filePath = fileID;
        } else {
            filePath = Utils.replace(fileName, fileIDMask, fileID);
        }

        OutputStreamWriter os = new OutputStreamWriter(
                new FileOutputStream(filePath, logAppendMode),
                Utils.PATTERN_STRING_ENCODING);

        try {
            // Escreve no CSV
            CSVPrinter csvp = new CSVPrinter(os);
            csvp.println(values);
        } finally {
            os.close();
        }
    }

    /**
     * @param fileID    O que vai entrar no lugar do fileIDMask.
     * @param entities  Entidades a serem escritas.
     * @throws IOException
     */
    public void log(String fileID, Entity[] entities) throws IOException {
        log(fileID, entities, append);
    }

    public void log(String fileID, Entity[] entities, boolean logAppendMode) throws
            IOException {
        String filePath = null;
        if (fileName == null) {
            filePath = fileID;
        } else {
            filePath = Utils.replace(fileName, fileIDMask, fileID);
        }

        OutputStreamWriter os = new OutputStreamWriter(
                new FileOutputStream(filePath, logAppendMode),
                Utils.PATTERN_STRING_ENCODING);

        try {
            // Escreve no CSV
            CSVPrinter csvp = new CSVPrinter(os);
            for (int i = 0; i < entities.length; i++) {
                csvp.println(entities[i].getFieldValues());
            }
        } finally {
            os.close();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileIDMask() {
        return fileIDMask;
    }

    public void setFileIDMask(String fileIDMask) {
        if (fileIDMask == null) {
            fileIDMask = "";
        }
        this.fileIDMask = fileIDMask;
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public void log(String fileID, ObjFactory objFactory,
                    Object[] objs, boolean logAppendMode) throws IOException {
        Entity[] entities = new Entity[objs.length];

        for (int j = 0; j < entities.length; j++) {
            entities[j] = objFactory.getEntity(objs[j]);
        }

        log(fileID, entities, logAppendMode);
    }
}
