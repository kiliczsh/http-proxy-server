import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MyProxy implements Runnable {


    /**
     * Data structure for constant order lookup of cache items.
     * Key: URL of page/image requested.
     * Value: File in storage associated with this key.
     */
    static HashMap<String, File> cache;
    /**
     * Data structure for constant order lookup of blocked sites.
     * Key: URL of page/image requested.
     * Value: URL of page/image requested.
     */
    static HashMap<String, String> blockedSites;
    /**
     * ArrayList of threads that are currently running and servicing requests.
     * This list is required in order to join all threads on closing of server
     */
    static ArrayList<Thread> servicingThreads;
    private ServerSocket serverSocket;
    /**
     * Semaphore for Proxy and Consolee Management System.
     */
    private volatile boolean running = true;


    /**
     * Create the Proxy Server
     *
     * @param port Port number to run proxy server from.
     */
    public MyProxy(int port) {

        // Load in hash map containing previously cached sites and blocked Sites
        cache = new HashMap<>();
        blockedSites = new HashMap<>();

        // Create array list to hold servicing threads
        servicingThreads = new ArrayList<>();

        // Start dynamic manager on a separate thread.
        new Thread(this).start();    // Starts overriden run() method at bottom

        try {
            // Load in cached sites from file
            File cachedSites = new File("cachedSites.txt");
            if (!cachedSites.exists()) {
                System.out.println("No cached sites found - creating new file");
                cachedSites.createNewFile();
            } else {
                FileInputStream fileInputStream = new FileInputStream(cachedSites);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                cache = (HashMap<String, File>) objectInputStream.readObject();
                fileInputStream.close();
                objectInputStream.close();
            }

            // Load in blocked sites from file
            File blockedSitesTxtFile = new File("blockedSites.txt");
            if (!blockedSitesTxtFile.exists()) {
                System.out.println("No blocked sites found - creating new file");
                blockedSitesTxtFile.createNewFile();
            } else {
                FileInputStream fileInputStream = new FileInputStream(blockedSitesTxtFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                blockedSites = (HashMap<String, String>) objectInputStream.readObject();
                fileInputStream.close();
                objectInputStream.close();
            }
        } catch (IOException e) {
            System.out.println("Error loading previously cached sites file");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found loading in preivously cached sites file");
            e.printStackTrace();
        }

        try {
            // Create the Server Socket for the Proxy
            serverSocket = new ServerSocket(port);

            // Set the timeout
            //serverSocket.setSoTimeout(100000);	// debug
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "..");
            running = true;
        }

        // Catch exceptions associated with opening socket
        catch (SocketException se) {
            System.out.println("Socket Exception when connecting to client");
            se.printStackTrace();
        } catch (SocketTimeoutException ste) {
            System.out.println("Timeout occured while connecting to client");
        } catch (IOException io) {
            System.out.println("IO exception when connecting to client");
        }
    }

    /**
     * Looks for File in cache
     *
     * @param url of requested file
     * @return File if file is cached, null otherwise
     */
    public static File getCachedPage(String url) {
        return cache.get(url);
    }

    /**
     * Adds a new page to the cache
     *
     * @param urlString   URL of webpage to cache
     * @param fileToCache File Object pointing to File put in cache
     */
    public static void addCachedPage(String urlString, File fileToCache) {
        cache.put(urlString, fileToCache);
    }

    /**
     * Check if a URL is blocked by the proxy
     *
     * @param url URL to check
     * @return true if URL is blocked, false otherwise
     */
    public static boolean isBlocked(String url) {
        if (blockedSites.get(url) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Listens to port and accepts new socket connections.
     * Creates a new thread to handle the request and passes it the socket connection and continues listening.
     */
    public void listen() {

        while (running) {
            try {
                // serverSocket.accpet() Blocks until a connection is made
                Socket socket = serverSocket.accept();

                // Create new Thread and pass it Runnable RequestHandler
                Thread thread = new Thread(new RequestHandler(socket));

                // Key a reference to each thread so they can be joined later if necessary
                servicingThreads.add(thread);

                thread.start();
            } catch (SocketException e) {
                // Socket exception is triggered by management system to shut down the proxy
                System.out.println("Server closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the blocked and cached sites to a file so they can be re loaded at a later time.
     * Also joins all of the RequestHandler threads currently servicing requests.
     */
    private void closeServer() {
        System.out.println("\nClosing Server..");
        running = false;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("cachedSites.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(cache);
            objectOutputStream.close();
            fileOutputStream.close();
            System.out.println("Cached Sites written");

            FileOutputStream fileOutputStream2 = new FileOutputStream("blockedSites.txt");
            ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(fileOutputStream2);
            objectOutputStream2.writeObject(blockedSites);
            objectOutputStream2.close();
            fileOutputStream2.close();
            System.out.println("Blocked Site list saved");
            try {
                // Close all servicing threads
                for (Thread thread : servicingThreads) {
                    if (thread.isAlive()) {
                        System.out.print("Waiting on " + thread.getId() + " to close..");
                        thread.join();
                        System.out.println(" closed");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("Error saving cache/blocked sites");
            e.printStackTrace();
        }

        // Close Server Socket
        try {
            System.out.println("Terminating Connection");
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("Exception closing proxy's server socket");
            e.printStackTrace();
        }

    }

    /**
     * Creates a management interface which can dynamically update the proxy configurations
     * blocked : Lists currently blocked sites
     * cached	: Lists currently cached sites
     * close	: Closes the proxy server
     * *		: Adds * to the list of blocked sites
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        String command;
        while (running) {
            System.out.println("Enter new site to block, or type \"blocked\" to see blocked sites, \"cached\" to see cached sites, or \"close\" to close server.");
            command = scanner.nextLine();
            if (command.toLowerCase().equals("blocked")) {
                System.out.println("\nCurrently Blocked Sites");
                for (String key : blockedSites.keySet()) {
                    System.out.println(key);
                }
                System.out.println();
            } else if (command.toLowerCase().equals("cached")) {
                System.out.println("\nCurrently Cached Sites");
                for (String key : cache.keySet()) {
                    System.out.println(key);
                }
                System.out.println();
            } else if (command.equals("close")) {
                running = false;
                closeServer();
            } else {
                blockedSites.put(command, command);
                System.out.println("\n" + command + " blocked successfully \n");
            }
        }
        scanner.close();
    }

}