package utilsLib.util;

import java.io.*;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

public class AttachDataSource implements DataSource {
    private byte[] bytes;
    private String contentType;
    private String name;
    private String insideEmailName;

    public AttachDataSource(OutputStream output, String name,
                            String contentType) {
        if (contentType == null) {
            this.contentType = "application/octet-stream";
        } else {
            this.contentType = contentType;
        }

        this.name = name;
        
        if (output != null) {
        	this.bytes = ((ByteArrayOutputStream) output).toByteArray();
        }
//    this.encode(output);
    }

    private void encode(OutputStream out) {
        ByteArrayOutputStream encoded = new ByteArrayOutputStream();

        try {
            encoded.writeTo(MimeUtility.encode(out, "binary"));
        } catch (MessagingException ex1) {
        } catch (IOException ex) {
        }

        this.bytes = encoded.toByteArray();
    }

    public String getContentType() {
        return this.contentType;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes, 0, bytes.length);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OutputStream getOutputStream() throws IOException {
        throw new FileNotFoundException();
    }

    public int getInputSize() {
        return bytes.length;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getInsideEmailName() {
        return insideEmailName;
    }

    public boolean isInsideEmail() {
        return insideEmailName != null;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setInsideEmailName(String insideEmailName) {
        this.insideEmailName = insideEmailName;
    }
}
