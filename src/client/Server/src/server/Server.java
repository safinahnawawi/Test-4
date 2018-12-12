/*
Secure Socket Programming (Server)
 */
package server;

import java.io.*;
import java.net.*;
import javax.net.ServerSocketFactory;
import javax.net.ssl.*;

public class Server {

    public static final boolean DEBUG = true;
 public static final int HTTPS_PORT = 443; 
 public static final String KEYSTORE_LOCATION = "C:\\Program Files\\Java\\jdk1.8.0_181\\bin\\Cert.jks";
 public static final String KEYSTORE_PASSWORD = "P@ssword.123";

 // main program
 public static void main(String argv[]) throws Exception {

 // set system properties, alternatively you can also pass them as
 // arguments like -Djavax.net.ssl.keyStore="keystore"....
 System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
 System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);

 if (DEBUG)System.setProperty("javax.net.debug", "ssl:record");

 Server server = new Server();
 server.startServer();
 }

 
 public void startServer() {
 try {
 ServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
 SSLServerSocket serversocket = (SSLServerSocket) ssf.createServerSocket(HTTPS_PORT);
 
 while (true) {
 Socket client = serversocket.accept();
 ProcessRequest cc = new ProcessRequest(client);
 }
 } catch (Exception e) {
 System.out.println("Exception:" + e.getMessage());
 }
 }
}

class ProcessRequest extends Thread {
 
 Socket client;
 BufferedReader is;
 DataOutputStream out;

 public ProcessRequest(Socket s) {
 client = s;
 try {
 is = new BufferedReader(new InputStreamReader(client.getInputStream()));
 out = new DataOutputStream(client.getOutputStream());
 } catch (IOException e) {
 System.out.println("Exception: " + e.getMessage());
 }
 this.start(); 
 }

 public void run() {
 try {
 
 String request = is.readLine();
 System.out.println("Received from Client: " + request);
 try {
 out.writeBytes("HTTP/1.0 200 OK\r\n");
 out.writeBytes("Content-Type: text/html\r\n");
 out.writeBytes("<html><head>Server Page: This is Server!</head>\r\n");
 out.writeBytes("<body><b/><p>Client sent: ");
 out.writeBytes(request + "</p></body></html>\r\n");
 out.flush();
 } catch (Exception e) {
 out.writeBytes("Content-Type: text/html\r\n");
 out.writeBytes("HTTP/1.0 400 " + e.getMessage() + "\r\n"); 
 out.flush();
 } finally {
 out.close();
 }
 client.close();
 } catch (Exception e) {
 System.out.println("Exception: " + e.getMessage());
 }
 }
    
}
