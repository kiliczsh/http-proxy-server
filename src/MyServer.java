/*
 * MyServer.java
 * Author: Muhammed Kılıç
 * @version 1.00
 */

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.StringTokenizer;

public class MyServer extends Thread {

    static final String HTML_START = "<html>" + "<title>CSE4074</title>" + "<body>";
    static final String HTML_END = "</body>" + "</html>";

    Socket connectedClient;
    BufferedReader inFromClient = null;
    DataOutputStream outToClient = null;


    public MyServer(Socket client) {
        connectedClient = client;
    }



    public void run() {
        try {

            System.out.println("The Client " + connectedClient.getInetAddress() + ":" +
                    connectedClient.getPort() + " is connected");

            inFromClient = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
            outToClient = new DataOutputStream(connectedClient.getOutputStream());

            String requestString = inFromClient.readLine();
            String headerLine = requestString;

            StringTokenizer tokenizer = new StringTokenizer(headerLine);

            String httpMethod = tokenizer.nextToken();      // "GET"
            String httpQueryString = tokenizer.nextToken(); // "/VALUE"
            String httpVersion = tokenizer.nextToken();     // "HTTP1.1"

            // obtain file size from httpQueryString
            boolean startsWithSlash = (httpQueryString.charAt(0)+"").equalsIgnoreCase("/");

            // "/VALUE" translated into "VALUE"
            String queryWithoutSlash = getRidOfSlash(httpQueryString); // returns zero(0) if not valid

            int fileSize = 1; //default value set to 1
            fileSize = Integer.parseInt(queryWithoutSlash); // assign VALUE to fileSize

            StringBuffer responseBuffer = new StringBuffer();
            responseBuffer.append("<b> This is the HTTP Server Home Page </b><BR>");
            responseBuffer.append("The HTTP Client request is <BR>");

            System.out.println("The HTTP request string is ");

            while (inFromClient.ready()) {
                // Read the HTTP complete HTTP Query
                responseBuffer.append(requestString);
                responseBuffer.append("<BR>");
                //System.out.println(requestString);
                requestString = inFromClient.readLine();
            }

            if (httpMethod.equals("GET")) {
                //System.out.println("StringBuffer Size: "+ responseBuffer.toString().getBytes().length+ " bytes." );
                boolean inSizeLimit = fileSize >= 100 && fileSize <= 20000;

                if ((""+httpQueryString.charAt(0)).equals("/")) {
                    if(inSizeLimit){
                        // The default home page
                        responseBuffer.append("The HTML file is "+fileSize+" bytes.....<BR>");
                        sendResponse(200, responseBuffer.toString(), false, httpVersion);
                    }else{
                        sendResponse(404, "<b>The Requested resource is too undecisive...</b>"+
                                        "<br>The HTML file is "+fileSize+" bytes.....<BR>",false, httpVersion);
                    }

                } else {
                        //This is interpreted as a file name
                        String fileName = httpQueryString.replaceFirst("/", "");
                        //System.out.println(fileName);
                        fileName = URLDecoder.decode(fileName, "UTF-8");
                        if (new File(fileName).isFile()) {
                            sendResponse(200, fileName, true, httpVersion);
                        } else {
                            sendResponse(404, "<b>The Requested resource not found ...." +
                                    "Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>", false, httpVersion);
                        }
                }
            } else{
                sendResponse(404, "<b>The Requested resource not found ...." +
                        "Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>", false, httpVersion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(int statusCode, String responseString, boolean isFile,String httpVersion) throws Exception {

        String statusLine = null;
        String serverdetails = "Server: Dinazor HTTPServer";
        String contentLengthLine = null;
        String fileName = null;
        String contentTypeLine = "Content-Type: text/html" + "\r\n";
        FileInputStream fin = null;

        if (statusCode == 200)
            statusLine = httpVersion +" 200 OK" + "\r\n";
        else
            statusLine = httpVersion +" 404 Not Found" + "\r\n";

        if (isFile) {
            fileName = responseString;
            fin = new FileInputStream(fileName);
            contentLengthLine = "Content-Length: " + Integer.toString(fin.available()) + "\r\n";
            if (!fileName.endsWith(".htm") && !fileName.endsWith(".html"))
                contentTypeLine = "Content-Type: \r\n";
        } else {
            responseString = MyServer.HTML_START + responseString + MyServer.HTML_END;
            contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
        }

        outToClient.writeBytes(statusLine);
        outToClient.writeBytes(serverdetails);
        outToClient.writeBytes(contentTypeLine);
        outToClient.writeBytes(contentLengthLine);
        outToClient.writeBytes("Connection: close\r\n");
        outToClient.writeBytes("\r\n");

        if (isFile) sendFile(fin, outToClient);
        else outToClient.writeBytes(responseString);

        outToClient.close();
    }

    public void sendFile(FileInputStream fin, DataOutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fin.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        fin.close();
    }

    public String getRidOfSlash(String httpQueryString){
        String queryWithoutSlash = "";
        if(httpQueryString.matches("/\\d+")){
            queryWithoutSlash = httpQueryString.replaceAll("\\D+","");
        }else{
            queryWithoutSlash = "0";
        }
        return queryWithoutSlash;
    }

}