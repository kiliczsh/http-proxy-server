public class Main {

    public static void main(String args[]) throws Exception {


        MyProxy myProxy = new MyProxy(8888);
        myProxy.listen();
/*
        ServerSocket Server = new ServerSocket(5000, 10, InetAddress.getByName("127.0.0.1"));
        System.out.printf("Waiting for client on port %d on address %s ...%n", Server.getLocalPort(), Server.getInetAddress());

        while (true) {
            try {
                Socket connected = Server.accept();
                (new MyServer(connected)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}
