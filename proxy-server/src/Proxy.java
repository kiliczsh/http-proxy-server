import common.Util;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

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
        SimpleDateFormat date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        String request;
        String response = "";
        try {
            request = fromClient.readLine();
            System.out.println("Request: "+request);
            ReturnFormat info;
            if((info= interpret(request)).errorCode == null){
                Socket socket;
                String port;
                if(info.port == -1){
                    socket = new Socket(InetAddress.getByName(info.address).getHostAddress(),80);
                    port = "";
                }else {
                    socket = new Socket(info.address,info.port);
                    port = ":"+String.valueOf(info.port);
                }

                BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter toServer = new PrintWriter(socket.getOutputStream(),
                        true);
                System.out.println(info.produceString());
                String[] recorded;

                if((recorded = reachCache(info.address+port+info.uri,READ,null))==null){
                    request = info.produceString();
                }else{
                    request = Util.produceIfModifiedRequest(info.produceString(),recorded[1]);
                }
                toServer.print((request)+"\r\n");
                toServer.flush();

                int i = 0;
                StringBuilder newRecord = new StringBuilder();
                while((response = fromServer.readLine())!=null){ //forwarding site's message
                    try{
                        if((i == 0) && (recorded != null) && response.split(" ",2)[1].equals(notModifiedStatus)){
                            sendMessage(recorded[0]);
                            System.out.println("From Cache");
                            break;
                        }
                    }catch(ArrayIndexOutOfBoundsException ignored){

                    }
                    i++;
                    sendMessage(response);
                    newRecord.append(response).append("\r\n");

                }

                if(newRecord.length() > 1){

                    String[] newData = {newRecord.toString(),date.format(new Date().getTime())};
                    reachCache(info.address+port+info.uri,WRITE,newData);
                    System.out.println("Not From Cache");
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
                System.out.println();
                String uri;
                String[] addressParts;
                if(urlParts.length == 2){ //default format
                    if(checkIfValidSize(uri = urlParts[0])){
                        return new ReturnFormat(typeGET, uri);
                    }else{
                        return new ReturnFormat(tooLongStatus);
                    }
                }else if(urlParts[2].split(":")[0].equals(defaultAddr) && checkIfValidSize(uri = urlParts[3])){
                    System.out.println("here");
                    addressParts= urlParts[2].split(":");

                    if(addressParts.length == 2){//else badrequest

                        return new ReturnFormat(typeGET,addressParts[0],Integer.parseInt(addressParts[1]), uri);
                    }else{
                        return new ReturnFormat(badRequestStatus);
                    }
                }else if(!urlParts[2].split(":")[0].equals(defaultAddr)){
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
    private synchronized static String[] reachCache(String url, int operation, String[] newData){
        switch (operation){
            case READ:
                if(ProxyMain.cache.containsKey(url)){
                    return (String[]) ProxyMain.cache.get(url);
                }else{
                    return null;
                }
            case WRITE:
                if(ProxyMain.cache.containsKey(url)){
                    ProxyMain.cache.remove(url);
                    ProxyMain.cache.put(url,newData);
                    return (String[]) ProxyMain.cache.get(url);
                }else{
                    ProxyMain.cache.put(url,newData);
                    return (String[]) ProxyMain.cache.get(url);
                }
        }
        return null;
    }
    private void sendMessage(String message){
        System.out.println(message);
        toClient.println(message);
        toClient.flush();
    }
}
