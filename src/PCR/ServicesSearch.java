/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCR;

/**
 *
 * @author Администратор
 */
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.bluetooth.*;

public class ServicesSearch {

   static final UUID HELLO_SERVICE = new UUID(0x1101);

   public final Vector<String> serviceFound = new Vector();

    public ServicesSearch(RemoteDevice btDevice) {
    
        UUID[] searchUuidSet = new UUID[] { HELLO_SERVICE };
        int[] attrIDs = new int[] { 0x0100 }; // Service name

      

      serviceFound.clear();

      final Object serviceSearchCompletedEvent = new Object();

    DiscoveryListener listener = new DiscoveryListener() {
             public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) { }
             public void inquiryCompleted(int discType) { }

             public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                   for (int i = 0; i < servRecord.length; i++) {
                      String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                      if (url == null) { continue; }
                      serviceFound.add(url);
                      DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                      if (serviceName != null) {
                         System.out.println("service " + serviceName.getValue() + " found " + url);
                      } else {
                         System.out.println("service found " + url);
                      }
                   }
             }
             public void serviceSearchCompleted(int transID, int respCode) {
                System.out.println("service search completed!");

                synchronized( serviceSearchCompletedEvent ){
                    serviceSearchCompletedEvent.notifyAll();
                }
             }
          };
     

      

         synchronized( serviceSearchCompletedEvent ) {
            try {
                System.out.println("search services on " +  btDevice.getBluetoothAddress() + " " + btDevice.getFriendlyName(false));
            } catch (IOException ex) {
                Logger.getLogger(ServicesSearch.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, btDevice, listener);
            } catch (BluetoothStateException ex) {
                Logger.getLogger(ServicesSearch.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                serviceSearchCompletedEvent.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ServicesSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      
    }

   
   
   public static void main(String[] args) throws IOException, InterruptedException {
       
   }
}
