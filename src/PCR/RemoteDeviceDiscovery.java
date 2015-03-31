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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.*;


//Поиск блютус устройств
public class RemoteDeviceDiscovery {

    public final Vector <RemoteDevice> devicesDiscovered = new Vector();//Список найденных устройств

    public RemoteDeviceDiscovery() throws IOException, InterruptedException {
        final Object inquiryCompletedEvent = new Object();//Синхронизирующая переменная

        devicesDiscovered.clear();

    DiscoveryListener listener = new DiscoveryListener() {

                public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                    System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
                    devicesDiscovered.addElement( btDevice );
                    try {
                        System.out.println("     name " + btDevice.getFriendlyName(false));
                    } catch (IOException cantGetDeviceName) {
                    }
                }

                public void inquiryCompleted(int discType) {
                    System.out.println("Device Inquiry completed!");
                    synchronized(inquiryCompletedEvent){
                        inquiryCompletedEvent.notifyAll();
                    }
                }

                public void serviceSearchCompleted(int transID, int respCode) {}
                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) { }
            };

        synchronized(inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                System.out.println("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                System.out.println(devicesDiscovered.size() +  " device(s) found");
            }
        }
    }

    
    
    public static void main(String[] args){

        try {
            new RemoteDeviceDiscovery();
        } catch (IOException ex) {
            Logger.getLogger(RemoteDeviceDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RemoteDeviceDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
