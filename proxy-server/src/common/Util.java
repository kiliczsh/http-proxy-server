package common;

import java.text.SimpleDateFormat;

public class Util {
    public static String produceHtml(String title,String body){
        return "<!DOCTYPE html>\r\n<html>\r\n<head>\r\n<title>"+title+"</title>\r\n</head>\r\n<body>\r\n<p>"+body+"</p>\r\n</body>\r\n</html>";
    }
    public static String produceErrorMessage(String errorCode,String errorMessage){
        return "<b>"+errorCode+"</b><BR>"+errorMessage;
    }
    public static String produceResponse(String version,String status,String title,String body){
        return version + " " + status + "\r\n"+
                "\r\n"+
                produceHtml(title,body);
    }
    public static String produceIfModifiedRequest(String request, String date){
        return request +
                "If-Modified-Since: "+ date +"\r\n";
    }
}