/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.swing.JTextArea;

/**
 *
 * @author stas
 */
public class Connection {

    
    OutputStream outStream; //выходящий поток
    InputStream inStream; //входящий поток
    StreamConnection conn; //соединение
    PrintWriter pWriter; //объект для отправки команд
    String lineRead; //строка хранящая ответ
    BufferedReader bReader2; //объект для приема команд
    /*соединяемся открываем входящий и выходящий потоки*/
    public Connection(String url) {
        try {
       
          conn = (StreamConnection) Connector.open(url);
      
           OutputStream outStream=conn.openOutputStream(); 
           pWriter=new PrintWriter(new OutputStreamWriter(outStream));
           
           inStream=conn.openInputStream();
           bReader2=new BufferedReader(new InputStreamReader(inStream));
           
           try {         
                lineRead=bReader2.readLine();
            } catch (IOException ex) {}
            System.out.println(lineRead);
        }
        catch ( IOException e ) { System.err.print(e.toString()); }
    
    }
      
    public void getData(String str,  ArrayList arrstr) 
    {
        pWriter.write(str + "\n");
        pWriter.flush();
        if(str.equals("CCDR")){
                try {

                    do{
                        lineRead=bReader2.readLine();                                                              
                        arrstr.add(lineRead);
                    }
                    while(!lineRead.equals("!cdr"));

                } catch (IOException ex) {

            }
        } else if (str.equals("RING")) {
            try {
                lineRead = bReader2.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (str.equals("NO C")){
            try {
                lineRead = bReader2.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
    }
}
