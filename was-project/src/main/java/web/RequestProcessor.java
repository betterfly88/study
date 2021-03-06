package web;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by betterFLY on 2018. 10. 27..
 */
public class RequestProcessor implements Runnable {
    private final static Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());
    private File rootDirectory;
    private String indexFileName = "index.html";
    private Socket connection;
    private final static String basePath = "src/main/webapp/web";

    public RequestProcessor(File rootDirectory, String indexFileName, Socket connection) {
        if (rootDirectory.isFile()) {
            throw new IllegalArgumentException(
                    "rootDirectory must be a directory, not a file");
        }
        try {
            rootDirectory = rootDirectory.getCanonicalFile();
        } catch (IOException ex) {
        }
        this.rootDirectory = rootDirectory;
        if (indexFileName != null)
            this.indexFileName = indexFileName;
        this.connection = connection;
    }

    @Override
    public void run() {
        // for security checks
        String root = rootDirectory.getPath();
        try {
            OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
            Writer out = new OutputStreamWriter(raw);
            Reader inputStream = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), "UTF-8");
            StringBuilder requestLine = new StringBuilder();

            int charRead;
            while (true) {
                requestLine.append((char) (charRead = inputStream.read()));
                if ((char) charRead == '\r') {
                    requestLine.append((char) inputStream.read());
                    charRead = inputStream.read();
                    if (charRead == '\r') {
                        requestLine.append((char) inputStream.read());
                        break;
                    } else {
                        requestLine.append((char) charRead);
                    }
                }
            }

            String get = requestLine.toString();
            logger.info(connection.getRemoteSocketAddress() + " " + get);
            String[] tokens = get.split("\\s+");
            String method = tokens[0];
            String version = "";
            if (method.equals("GET")) {
                String fileName = tokens[1];
                if (fileName.endsWith("/")) fileName += indexFileName;
                String contentType =
                        URLConnection.getFileNameMap().getContentTypeFor(fileName);
                if (tokens.length > 2) {
                    version = tokens[2];
                }
                File theFile = new File(rootDirectory, fileName.substring(1, fileName.length()));
                if (theFile.canRead()
                    // Don't let clients outside the document root
                        && theFile.getCanonicalPath().startsWith(root)) {
                    byte[] theData = Files.readAllBytes(theFile.toPath());
                    if (version.startsWith("HTTP/")) { // send a MIME header
                        sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
                    }
                    // send the file; it may be an image or other binary data
                    // so use the underlying output stream
                    // instead of the writer
                    raw.write(theData);
                    raw.flush();
                } else {
                    // can't find the file
//                    String body = new StringBuilder("<HTML>\r\n")
//                            .append("<HEAD><TITLE>File Not Found</TITLE>\r\n")
//                            .append("</HEAD>\r\n")
//                            .append("<BODY>")
//                            .append("<H1>HTTP Error 404: File Not Found</H1>\r\n")
//                            .append("</BODY></HTML>\r\n")
//                            .toString();
//                    if (version.startsWith("HTTP/")) { // send a MIME header
//                        sendHeader(out, "HTTP/1.0 404 File Not Found", "text/html; charset=utf-8", body.length());
//                    }
//                    out.write(body);
//                    out.flush();
//                    FileReader f = readFile(basePath+"/err/404.html");
                    outputStream(basePath+"/err/404.html", get);
                }
            } else {
                // method does not equal "GET"
                String body = new StringBuilder("<HTML>\r\n").append("<HEAD><TITLE>Not Implemented</TITLE>\r\n").append("</HEAD>\r\n")
                        .append("<BODY>")
                        .append("<H1>HTTP Error 501: Not Implemented</H1>\r\n")
                        .append("</BODY></HTML>\r\n").toString();
                if (version.startsWith("HTTP/")) { // send a MIME header
                    sendHeader(out, "HTTP/1.0 501 Not Implemented",
                            "text/html; charset=utf-8", body.length());
                }
                out.write(body);
                out.flush();
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), ex);
        } finally {
            try {
                connection.close();
            } catch (IOException ex) {
            }
        }
    }

    private void sendHeader(Writer out, String responseCode, String contentType, int length)
            throws IOException {
        out.write(responseCode + "\r\n");
        Date now = new Date();
        out.write("Date: " + now + "\r\n");
        out.write("Server: JHTTP 2.0\r\n");
        out.write("Content-length: " + length + "\r\n");
        out.write("Content-type: " + contentType + "\r\n\r\n");
        out.flush();
    }


    public FileReader readFile(String filePath){
        FileReader fReader = null;
        try{
            fReader= new FileReader(filePath);

            int tempInteger;
            while ((tempInteger = fReader.read()) != -1) {
                System.out.print((char) tempInteger);
            }
            fReader.close();
        }catch (FileNotFoundException fn){
            fn.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }
        return fReader;
    }


    public void outputStream(String filePath, String requestHeader){
        FileInputStream fis = null;
        try{
            File file = new File(filePath);
            fis= new FileInputStream(file);

            OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
            Writer out = new OutputStreamWriter(raw);

            String result = setReadBuffer(file);
            String[] tokens = requestHeader.split("\\s+");
            String version = "";
            if (tokens.length > 2) {
                version = tokens[2];
            }
            String contentType =
                    URLConnection.getFileNameMap().getContentTypeFor(filePath);
            if (version.startsWith("HTTP/")) { // send a MIME header
                sendHeader(out, "HTTP/1.0 200 OK", contentType, result.length());
            }
            out.write(result);
            out.flush();
            fis.close();
        }catch (FileNotFoundException fn){
            fn.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    public String setReadBuffer(File file) throws IOException{
        byte[] readBuffer = Files.readAllBytes(file.toPath());
        return new String(readBuffer);
    }

}