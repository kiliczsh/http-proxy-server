public interface ProxyInterface {
    String typeGET = "GET";
    String notFound = "Not Found";
    String tooLong = "Request-URI Too Long";
    String badRequest = "Bad Request";

    String notFoundStatus = "404 Not Found";
    String tooLongStatus = "414 Request-URI Too Long";
    String okStatus = "200 OK";
    String badRequestStatus = "400 Bad Request";

    String version = "HTTP/1.0";

    String defaultAddr = "localhost:8080";

    class ReturnFormat{
        String uri;
        private String requestType;
        String address;
        int port;
        String errorCode;
        ReturnFormat(String requestType,String address,int port,String uri){
            this.requestType = requestType;
            this.address = address;
            this.port = port;
            this.uri = uri;
            this.errorCode = null;
        }

        ReturnFormat(String requestType,String uri){
            this.requestType = requestType;
            this.errorCode = null;
            this.address = "localhost";
            this.port = 8080;
            this.uri = uri;
        }
        ReturnFormat(String errorCode){

            this.errorCode = errorCode;
        }
        String produceString(){
            return requestType+" /"+uri+" "+version+"\r\n";
        }
    }
}
