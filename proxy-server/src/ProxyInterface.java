public interface ProxyInterface {
    String typeGET = "GET";
    String notFound = "Not Found";
    String tooLong = "Request-URI Too Long";

    String notFoundStatus = "404 Not Found";
    String tooLongStatus = "414 Request-URI Too Long";
    String okStatus = "200 OK";

    String version = "HTTP/1.0";

    String defaultAddr = "localhost:8080";

    class ReturnFormat{
        String uri;
        private String requestType;
        String address;
        int port;
        ReturnFormat(String requestType,String address,int port,String uri){
            this.requestType = requestType;
            this.address = address;
            this.port = port;
            this.uri = uri;
        }
        ReturnFormat(String requestType,String uri){
            this.requestType = requestType;
            this.address = "localhost";
            this.port = 8080;
            this.uri = uri;
        }
        String produceString(){
            return requestType+" /"+uri+" "+version+"\r\n";
        }
    }
}
