package basecampProxy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Properties;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// Usage: https://iotcgv1.herokuapp.com/IoTExplorer?PlatformEvent=GeneralPurpose__e&SerialNumber__c=12345&ErrorCode__c=ERROR

 
   
  @WebServlet(
      name = "IoTExplorerDFParking",
      urlPatterns = {"/IoTExplorerDFParking"}
  )
  public class IoTExplorerDFParking extends HttpServlet{

    
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException {
      Properties props = System.getProperties();
      props.setProperty("gate.home", "http://gate.ac.uk/wiki/code-repository");
      
    doPost(req, res);
  }
    String parking1;
    String parking2;
    String parking3;
    
    String status1;
    String status2;
    String status3;
    String payload1;
    String payload2;
    String payload3;
   


  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException {
    System.out.println("In doPost");
    /// check posted stuff
    String xml = "<message>HELLO!</message>";
    Integer paramcounter = 0;
    Enumeration paramNames = req.getParameterNames();
    String SerialNumber = null;
    String ErrorCode = null;
    String PlatformEvent = null;
    while(paramNames.hasMoreElements()) {
      String paramName = (String)paramNames.nextElement();
      System.out.println("param:" + paramName);
      String[] paramValues = req.getParameterValues(paramName);
      String paramValue = paramValues[0];
      if (paramName.equalsIgnoreCase("PlatformEvent")) {
        PlatformEvent = paramValue;
      }
      // Usage: https://iotcgv1.herokuapp.com/IoTExplorer?PlatformEvent=GeneralPurpose__e&SerialNumber=12345&ErrorCode=OK
      
      if (paramcounter == 0) {
        parking1= paramName;
        status1= paramValue;
        System.out.println("Parking1:" + paramValue);
        System.out.println("------");
        
      }
      else if (paramcounter == 1) {
        parking2= paramName;
        status2= paramValue;
        System.out.println("Parking2:" + paramValue);
        System.out.println("------");
        
      }
      else if (paramcounter == 2) {
        parking3= paramName;
        status3= paramValue;
        System.out.println("Parking3:" + paramValue);
        System.out.println("------");
        String[] getcolon = status3.split(":");
        if (getcolon != null) {
          System.out.println("-------- GOT A COLON --------");
          status3 = getcolon[0];
        }       
      }  
     
      
      paramcounter++;
    }
    System.out.println("end of params");
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    Document doc = null;
    try { 
      builder = factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    ///
    ///  create the JSON 
    ////
      
  payload1 = "{\"ParkingSpotID__c\": "+parking1+",\"Status__c\": \""+status1+"\"}";
  System.out.println("JSON Payload 1a: " + payload1);
  payload2 = "{\"ParkingSpotID__c\": "+parking2+",\"Status__c\": \""+status2+"\"}";
  payload3 = "{\"ParkingSpotID__c\": "+parking3+",\"Status__c\": \""+status3+"\"}";
  System.out.println("JSON Payload 2a: " + payload2);
    System.out.println("JSON Payload 3a: " + payload3); 

      URL url = new URL("https://login.salesforce.com/services/oauth2/token?grant_type=password&client_id=3MVG95NPsF2gwOiPlPuL6H9ogVxz8XlnX2bIeJwHjsLylKmbTzb3onawGbLVKMUTs.9hLQQbJraYxmyIKsw.4&client_secret=5774738336908218587&username=tservice@bcmadlegoparking18.demo&password=Salesforce1yab7ZJ4txdYJ9Usj6C7tiART");
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Cache-Control", "no-cache"); 
        conn.connect();
       
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                + conn.getResponseCode());
        }
  
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
    
        String output;
        System.out.println("Output from Server .... \n");
        String tokenstrings = null;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            tokenstrings = output;
        }
        // Get the tokens!
        String[] parts = tokenstrings.split("access_token\":\"");
        String part2 = parts[1];
        System.out.println("here is part2: "+part2);
        String[] parts2 = part2.split("\",\"");
        String part3 = parts2[0];
        System.out.println("here is part3: "+part3); // access token
        
        
      System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX got access token XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
     conn.disconnect();
     // we now have the token
     /////////////// create a row in the Platform Events Object
     System.out.println("JSON Payload: " + payload1);
     URL url2 = new URL("https://bcmadlegoparking18.my.salesforce.com/services/data/v40.0/sobjects/ParkingEvent__e/");
      HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
      conn2.setDoOutput(true);
      conn2.setRequestMethod("POST");
      conn2.setRequestProperty("Content-Type", "application/json");
      conn2.setRequestProperty("Accept", "application/json");
      conn2.setRequestProperty("Authorization", "Bearer "+part3);
      
      OutputStream os2 = conn2.getOutputStream();
      os2.write(payload1.getBytes());
      os2.flush();
  
      if (conn2.getResponseCode() > 201) {
          throw new RuntimeException("Failed : HTTP error code : "
              + conn2.getResponseCode());
      }

      BufferedReader br2 = new BufferedReader(new InputStreamReader(
              (conn2.getInputStream())));
  
      String output2;
      System.out.println("Output from Server .... \n");
      while ((output2 = br2.readLine()) != null) {
          System.out.println(output2);
      }

     System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXSent to IoTExplorer!XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
     conn2.disconnect();
      
      System.out.println("JSON Payload: " + payload2);
     URL url3 = new URL("https://bcmadlegoparking18.my.salesforce.com/services/data/v40.0/sobjects/ParkingEvent__e/");
      HttpURLConnection conn3 = (HttpURLConnection) url3.openConnection();
      conn3.setDoOutput(true);
      conn3.setRequestMethod("POST");
      conn3.setRequestProperty("Content-Type", "application/json");
      conn3.setRequestProperty("Accept", "application/json");
      conn3.setRequestProperty("Authorization", "Bearer "+part3);
      
      OutputStream os3 = conn3.getOutputStream();
      os3.write(payload2.getBytes());
      os3.flush();
  
      if (conn3.getResponseCode() > 401) {
          throw new RuntimeException("Failed : HTTP error code : "
              + conn3.getResponseCode());
      }

      BufferedReader br3 = new BufferedReader(new InputStreamReader(
              (conn3.getInputStream())));
  
      String output3;
      System.out.println("Output from Server .... \n");
      while ((output3 = br3.readLine()) != null) {
          System.out.println(output3);
      }

     System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXSent to IoTExplorer!XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
     conn3.disconnect();
     
      System.out.println("JSON Payload: " + payload3);
     URL url4 = new URL("https://bcmadlegoparking18.my.salesforce.com/services/data/v40.0/sobjects/ParkingEvent__e/");
      HttpURLConnection conn4 = (HttpURLConnection) url4.openConnection();
      conn4.setDoOutput(true);
      conn4.setRequestMethod("POST");
      conn4.setRequestProperty("Content-Type", "application/json");
      conn4.setRequestProperty("Accept", "application/json");
      conn4.setRequestProperty("Authorization", "Bearer "+part3);
      
      OutputStream os4 = conn4.getOutputStream();
      os4.write(payload3.getBytes());
      os4.flush();
  
      if (conn4.getResponseCode() > 401) {
          throw new RuntimeException("Failed : HTTP error code : "
              + conn4.getResponseCode());
      }

      BufferedReader br4 = new BufferedReader(new InputStreamReader(
              (conn4.getInputStream())));
  
      String output4;
      System.out.println("Output from Server .... \n");
      while ((output4 = br4.readLine()) != null) {
          System.out.println(output4);
      }

     System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXSent to IoTExplorer!XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
     conn4.disconnect();
      
      
     }
     
  
  
  }
