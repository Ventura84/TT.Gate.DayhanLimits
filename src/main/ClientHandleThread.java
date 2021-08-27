package main;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static main.Main.*;


public class ClientHandleThread extends Thread{


    private final Logger log = Logger.getLogger(ClientHandleThread.class);

    private String tName;
    private Socket ClientConnect;
    private String IP;
    private String[] parsedBODYs = new String[65];
    private String AccNum;
    private InputStream fromClient;
    private OutputStream toClient;
    private byte[] bytesFromCLIENT;



    public ClientHandleThread(Socket clientConnected, String tName) {
        this.ClientConnect = clientConnected;
        this.IP = String.valueOf(clientConnected.getRemoteSocketAddress()).substring(1);

        //this.DB_IP = db_ip;
        this.tName = tName;

    }

    private boolean threadRun = true;
    private void threadStop(){
        threadRun = false;
    }

    private void close_conn(String ip){

        try {

            this.fromClient.close();
            this.toClient.close();
            this.ClientConnect.close();

        } catch (IOException err) {
            //e.printStackTrace();
            log.error(ip + " ERROR: CAN`T CLOSE SOCKETS OR STREAMS", err);
        }

    }



    private boolean getConnectionStreamsIsFail() {

        try {
            ClientConnect.setSoTimeout(2000);
            fromClient = ClientConnect.getInputStream();
            toClient = ClientConnect.getOutputStream();
        } catch (Exception err) {
            log.error(IP + " ERROR: CAN`T GET STREAMS FROM CLIENT !\n", err);
            close_conn(IP);
            return true;
        }

        return false;
    }

    private boolean getBytesFromIsFail (Reader Reader) {

        try {
            bytesFromCLIENT = Reader.ReadFrom(fromClient, IP);
            //System.out.println(Arrays.toString(bytesFromCLIENT));
        } catch (Exception err) {
            log.error(IP + " ERROR: CAN`T GET BYTES FROM CLIENT !\n", err);
            close_conn(IP);
            return true;
        }

        return false;
    }

    private boolean getParsedBodyFromIsFail(Parser Parser) {

        try {
            //private String DB_IP;
            ArrayList<ArrayList<Byte>> PARSED_BODY = Parser.parseBODY_b(bytesFromCLIENT);
            for (int i = 0; i < PARSED_BODY.size(); i++) {
                if (PARSED_BODY.get(i) != null) {
                    parsedBODYs[i] = Parser.byteArrayToString(Parser.arrayListToByteArray(PARSED_BODY.get(i)));
                }
            }
            log.info(IP + " DATA FROM CLIENT PARSED");
            log.info(Parser.logFormatForFields(Parser.getFieldsForLog(PARSED_BODY)));
        } catch (Exception err) {
            log.error(IP + " ERROR: CAN`T PARSE BYTES FROM CLIENT !\n", err);
            close_conn(IP);
            return true;
        }

        return false;
    }


    private boolean sendTo (Sender Sender, String verifyResult) {

        if (!Sender.sendTo(toClient, verifyResult.getBytes(), IP, "VERF", "CLIENT")) {
            close_conn(IP);
            return false;
        }
        return true;
    }



//    private boolean checkAccountsVsPanSeeker(ArrayList<String> accList, String tid){
//
//        return accList.contains(tid);
//    }


    private boolean checkAccountsVsPan(String pan, String tid){

        ArrayList<String> accList = new ArrayList<>();
        //System.out.println(pan.substring(6, 7));
        //System.out.println(tid);
        switch (pan.substring(6, 7)){
            case "1" :
            case "2" : {
                accList.addAll(AccDokunKarhanasy);
                accList.addAll(AccGallaKarhanasy);
                accList.addAll(AccOsumligiGoramak);
                accList.addAll(AccTehKarhanasy);
                accList.addAll(AccSuwKarhanasy);
                accList.addAll(AccHowaYollary);
                accList.addAll(AccDayhanBirlesik);
                accList.addAll(AccSalyKarhanasy);
                //System.out.println("ACC LIST FROM METHOD 1 : " + accList);
                return accList.contains(tid);
            }
            case "3" : {
                accList.addAll(AccPagtaKarhanasy);
                accList.addAll(AccDokunKarhanasy);
                accList.addAll(AccTehKarhanasy);
                accList.addAll(AccSuwKarhanasy);
                accList.addAll(AccHowaYollary);
                accList.addAll(AccOsumligiGoramak);
                accList.addAll(AccDayhanBirlesik);
                //System.out.println("ACC LIST FROM METHOD 1 : " + accList);
                return accList.contains(tid);
            }
            case "4" : {
                accList.addAll(AccDokunKarhanasy);
                accList.addAll(AccSugundyrKarhanasy);
                accList.addAll(AccOsumligiGoramak);
                accList.addAll(AccTehKarhanasy);
                accList.addAll(AccSuwKarhanasy);
                accList.addAll(AccDayhanBirlesik);
                return accList.contains(tid);
            }
            
        }

        return false;
    }

    private boolean checkPaymentVsPan(String pan, String service){

        switch (pan.substring(6, 7)){
            case "1" : {
                if(service.equals("MINERAL_DOKUNLER")||
                   service.equals("TOHUM") ||
                   service.equals("TEH_HYZMAT") ||
                   service.equals("SUW_HYZMAT") ||
                   service.equals("UCAR_HYZMAT") ||
                   service.equals("HIMIKI")
                )
                    return true;
                break;
            }
            case "2" : {
                if(service.equals("MINERAL_DOKUNLER")||
                   service.equals("TOHUM") ||
                   service.equals("TEH_HYZMAT") ||
                   service.equals("SUW_HYZMAT") ||
                   service.equals("UCAR_HYZMAT") ||
                   service.equals("OSUMLIKLERI_GORAMAK")
                )
                    return true;
                break;
            }
            case "3" : {
                if(!service.equals("HIMIKI"))
                    return true;
                break;
            }
            case "4" : {
                if(service.equals("MINERAL_DOKUNLER")||
                   service.equals("TOHUM") ||
                   service.equals("TEH_HYZMAT") ||
                   service.equals("SUW_HYZMAT") ||
                   service.equals("HIMIKI")
                )
                    return true;
                break;
            }

        }

        return false;
    }


    private void setAccNum(String tid){
        //System.out.println("TID FROM METHOD 3 : " + tid);
        ArrayList<ArrayList<String>> accLists = new ArrayList<>();
        accLists.add(AccSalyKarhanasy);
        accLists.add(AccGallaKarhanasy);
        accLists.add(AccPagtaKarhanasy);
        accLists.add(AccSugundyrKarhanasy);
        accLists.add(AccTehKarhanasy);
        accLists.add(AccSuwKarhanasy);
        accLists.add(AccDokunKarhanasy);
        accLists.add(AccOsumligiGoramak);
        accLists.add(AccHowaYollary);
        accLists.add(AccDayhanBirlesik);
        //System.out.println("ACC LISTS FROM METHOD 3 : " + accLists);
        for (ArrayList<String> accList : accLists) {
            //System.out.println("ACC LIST FROM 3 METHOD LOOPING : " + accList);
            if (accList.contains(tid)){
                AccNum = accList.get(0);
                //System.out.println("ACC NUM : " + AccNum);
                return;
            }

        }

    }

    private boolean checkAccountVsPayment(String tid, String service){

        setAccNum(tid);


        switch (AccNum){
            case "01" :
            case "02" :
            case "03" :
            case "04" : {
                return service.equals("TOHUM");
            }
            case "05" :
            case "10" : {
                return service.equals("TEH_HYZMAT");
            }
            case "06" : {
                return service.equals("SUW_HYZMAT");
            }
            case "07" : {
                return service.equals("MINERAL_DOKUNLER");
            }
            case "08" : {
                return service.equals("OSUMLIKLERI_GORAMAK") || service.equals("MIKRODOKUNLERI_GARMAK") || service.equals("KEBELEK_TUTUJY") || service.equals("YAPRAGY_DUSURMEK") || service.equals("HASAL_OTLARA_GARSY") || service.equals("HIMIKI");
            }
            case "09" : {
                return service.equals("UCAR_HYZMAT");
            }
            default:
                throw new IllegalStateException("UNEXPECTED VALUE OF ACC_NUM : " + AccNum);
        }

    }




    @Override
    public void run(){

        Thread.currentThread().setName(tName);


        Reader Reader = new Reader();
        Parser Parser = new Parser();
        Sender Sender = new Sender();
        DBWorker DBWorker = new DBWorker(IP);


        while (threadRun){

            if(getConnectionStreamsIsFail()){break;}

            if(getBytesFromIsFail(Reader)){break;}

            if(getParsedBodyFromIsFail(Parser)){break;}


            boolean error = false;
            //log.info(IP + " VERIFYING..." );
            switch (parsedBODYs[1]){
                case "VERF" : {
                    log.info(IP + " 1 VERIFYING..." );
                    if(!checkAccountsVsPan(parsedBODYs[2], parsedBODYs[7])){
                        if(!sendTo(Sender, "727")){error = true;}
                        break;
                    }
                    log.info(IP + " 2 VERIFYING..." );
                    if(!checkPaymentVsPan(parsedBODYs[2], parsedBODYs[4])){
                        if(!sendTo(Sender, "728")){error = true;}
                        break;
                    }
                    log.info(IP + " 3 VERIFYING..." );
                    if(!checkAccountVsPayment(parsedBODYs[7], parsedBODYs[4])){
                        if(!sendTo(Sender, "729")){error = true;}
                        break;
                    }
                    log.info((IP + " DB VERIFYING..."));
                    String verResult = DBWorker.dbVerify(parsedBODYs);
                    if(!sendTo(Sender, verResult)){error = true;}
                    break;
                }
                case "PAID" : {
                    DBWorker.dbPaid(parsedBODYs);
                    break;
                }
                case "CNCL" : {
                    DBWorker.dbCancel(parsedBODYs);
                    break;
                }
            }

            if (error){
                close_conn(IP);
                break;
            }



            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            close_conn(IP);
            threadStop();

        } // WHILE


    } // RUN






}
