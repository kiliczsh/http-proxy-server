package common;

public class Util {
    public static String produceHtml(String title,String body){
        return "<HTML><HEAD><TITLE>"+title+"</TITLE></HEAD><BODY>"+body+"</BODY></HTML>";
    }
    public static String produceErrorMessage(String errorCode,String errorMessage){
        return "<b>"+errorCode+"</b><BR>"+errorMessage;
    }
    public static String produceResponse(String version,String status,String title,String body){
        return version + " " + status + "\r\n"+
                "Server: CSE4074 Proxy Server\r\n" + "Content-Type: text/html\r\n"+
                "Content-Length: "+body.length()+"\r\n"+
                "\r\n"+
                produceHtml(title,body);
    }
}