import common.Util;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Proxy implements Runnable,ProxyInterface{

    private BufferedReader fromClient;
    private PrintWriter toClient;
    private Socket connected;

    Proxy(Socket connected){
        this.connected = connected;
        try {
            this.fromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
            this.toClient = new PrintWriter(connected.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        String request;
        String response = "";
        try {
            request = fromClient.readLine();
            System.out.println("Request: "+request);
            ReturnFormat info;
            if((info= interpret(request)).errorCode == null){
                Socket socket;
                if(info.port == -1){
                    socket = new Socket(InetAddress.getByName(info.address).getHostAddress(),80);
                }else {
                    socket = new Socket(info.address,info.port);
                }

                BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter toServer = new PrintWriter(socket.getOutputStream(),
                        true);
                System.out.println(info.produceString());
                toServer.print(info.produceString()+"\r\n");
                toServer.flush();
                while((response = fromServer.readLine())!=null){ //forwarding site's message
                    System.out.println(response);
                    toClient.println(response);
                    toClient.flush();
                }
            }else if(info.errorCode.equals(tooLongStatus)){
                response = Util.produceResponse(version,tooLongStatus,tooLong,tooLongStatus);
                System.out.println("Response: "+response);
                toClient.print(response);
                toClient.flush();
            }else if(info.errorCode.equals(badRequestStatus)){
                response = Util.produceResponse(version,badRequestStatus,badRequest,badRequestStatus);
                System.out.println("Response: "+response);
                toClient.print(response);
                toClient.flush();
            }
        } catch (IOException e) {
            response = Util.produceResponse(version,notFoundStatus,notFound,notFoundStatus);
            System.out.println("Response: "+response);
            toClient.print(response);
            toClient.flush();
        }
        try{
            toClient.close();
            fromClient.close();
            connected.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private ReturnFormat interpret(String request){
        try{
            String[] requestParts = request.split(" ");
            if(requestParts[0].equals(typeGET)){
                String[] urlParts = requestParts[1].split("/");
                String uri;
                if(urlParts.length == 2){ //default format
                    if(checkIfValidSize(uri = urlParts[0])){
                        return new ReturnFormat(typeGET, uri);
                    }else{
                        return new ReturnFormat(tooLongStatus);
                    }
                }else if(urlParts[2].equals(defaultAddr) && checkIfValidSize(uri = urlParts[3])){
                    String[] addressParts = urlParts[2].split(":");

                    if(addressParts.length == 2){//else badrequest

                        return new ReturnFormat(typeGET,addressParts[0],Integer.parseInt(addressParts[1]), uri);
                    }else{
                        return new ReturnFormat(badRequestStatus);
                    }
                }else if(!urlParts[2].equals(defaultAddr)){
                    StringBuilder sb = new StringBuilder();
                    for(int i=3;i<urlParts.length;i++){
                        sb.append(urlParts[i]);
                        if(i!=urlParts.length-1){
                            sb.append("/");
                        }
                    }
                    return new ReturnFormat(typeGET,urlParts[2],-1,sb.toString());
                }
            }
        }catch (ArrayIndexOutOfBoundsException | NumberFormatException e){
            return new ReturnFormat(badRequestStatus);
        }
        return new ReturnFormat(tooLongStatus);
    }
    private boolean checkIfValidSize(String uri) throws NumberFormatException{
        int integer = Integer.parseInt(uri);
        return (integer >= 0 && integer < 9999);
    }
}
