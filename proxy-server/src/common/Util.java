package common;

public class Util {
    public static String produceHtml(String title,String body){
        return "<html><head><title>"+title+"</title></head><body>"+body+"</body></html>\r\n";
    }
    public static String produceErrorMessage(String errorCode,String errorMessage){
        return "<b>"+errorCode+"</b><BR>"+errorMessage;
    }
    public static String produceResponse(String version,String status,String title,String body){
        return version + " " + status + "\r\n"+
                "\r\n"+
                produceHtml(title,body);
    }
}