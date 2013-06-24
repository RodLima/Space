package br.tche.ucpel.doo3.servlet;

import br.tche.ucpel.doo3.game.CanvasMenu;
import br.tche.ucpel.doo3.game.Record;
import javax.microedition.io.*;
import java.io.*;


/**
 * Classe de conexão com o Servlet
 * @author Rodrigo
 */
public class ServletClient {
    
    private static ServletClient instance;
    public static final int TOP = 5;
    
    public static String servlet = "http://localhost:8080/SpaceServ/SpaceServlet";
    public static String agent = "Space";
    
    private static HttpConnection conn = null;
    private static DataOutputStream dos = null;
    private static DataInputStream dis = null;
    
    private Record record;
    private CanvasMenu menu;
    private SendRecord sendRecord;
    private GetRecords getRecords;
    private boolean sendRecordCancelled;
    private boolean getRecordsCancelled;
    private Thread connectionThread = null;
    private static boolean connectionRunning = false;
   
    private ServletClient(CanvasMenu menu){
        this.menu = menu;
    }
    
    /**
     * Método para pegar a instancia 
     * @param menu Tela menu
     * @return Instancia de ServletClient
     */
    public static ServletClient getInstance(CanvasMenu menu){
        if(instance == null){
            instance = new ServletClient(menu);
        } else {
            instance.menu = menu;
        }
        return instance;
    }
    
    /**
     * Método para enviar o recorde para o servidor
     * @param record Recorde
     */
    public void enviarRecorde(Record record){
        this.record = record;
        if(sendRecord == null){
            sendRecord = new SendRecord();
        }
        
        if(connectionThread == null || (connectionThread != null && !connectionThread.isAlive())){
            connectionThread = new Thread(sendRecord);
            connectionThread.start();
        }
    }
    
    /**
     * Método para receber os recordes do servidor
     */
    public void receberMelhoresRecordes(){
        
        if(getRecords == null){
            getRecords = new GetRecords();
        }
        
        if(connectionThread == null || (connectionThread != null && !connectionThread.isAlive())){    
            connectionThread = new Thread(getRecords);
            connectionThread.start();
        }
    }
    
    /**
     * Método para cancelar o envio
     */
    public void cancelarEnvioRecorde(){
        sendRecordCancelled = true;
    }

     /**
     * Método para cancelar o recebimento
     */
    public void cancelarRecebimentoRecordes(){
        getRecordsCancelled = true;
    }
    
    class SendRecord implements Runnable{
        public void run(){
            if(!connectionRunning && !sendRecordCancelled){
                connectionRunning = true;
                try{
                
                    conn = (HttpConnection) Connector.open(servlet);
                    conn.setRequestMethod(HttpConnection.POST);
                    conn.setRequestProperty("User-Agent", agent);
                    conn.setRequestProperty("Content-Type", "application/octet-stream");                

                    dos = new DataOutputStream(conn.openOutputStream());              

                    dos.writeInt(record.getScore());
                    dos.writeUTF(record.getName());
                    dos.flush();
                    

                    int rc = conn.getResponseCode();
                    if(rc == HttpConnection.HTTP_OK && !sendRecordCancelled){
                        menu.recordSubmited(true);
                    } else if(!sendRecordCancelled) {
                        menu.recordSubmited(false);
                    }
                    
                }catch(IOException ex){
                    if(!sendRecordCancelled){
                        menu.recordSubmited(false);
                    }
                }finally{
                    try{
                        if(dos != null){
                            dos.close();
                        }
                        if(conn != null){
                            conn.close();
                        }
                    }catch(IOException ex){}
                    sendRecordCancelled = false;
                }
            }
            connectionRunning = false;
        }
    }
    
    class GetRecords implements Runnable{
        public void run() {
            if(!connectionRunning && !getRecordsCancelled){
                connectionRunning = true;
                try{

                    conn = (HttpConnection) Connector.open(servlet + "?action=melhores");
                    conn.setRequestMethod(HttpConnection.GET);
                    conn.setRequestProperty("User-Agent", agent);
                    
                    dis = new DataInputStream(conn.openDataInputStream());
                    

                    Record[] onlineRecords = new Record[TOP];
                    
                    Record currentRecord = null;
                    
                    for(int i=0;i<TOP;i++){
                        if(!getRecordsCancelled){
                            currentRecord = new Record();
                            currentRecord.setScore(dis.readInt());
                            currentRecord.setName(dis.readUTF());
                            onlineRecords[i] = currentRecord;
                        }
                    }
                    if(!getRecordsCancelled){
                        menu.onLineRecords(onlineRecords);
                    }
                } catch (IOException ioe){
                    
                } finally{
                    try{
                        if(dis != null) {
                            dis.close();
                        }
                        if(conn != null){
                            conn.close();
                        }
                    } catch (IOException ioe){}
                    getRecordsCancelled = false;
                }
            }
            connectionRunning = false;
        }
    }
}
