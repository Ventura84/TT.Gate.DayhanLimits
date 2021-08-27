package main;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    private static final Logger log = Logger.getLogger(Main.class);
    private static HashMap<String, Serializable> PROP;


//    public static double LIMIT_TOHUM;
//    public static double LIMIT_TEH_SURUM;
//    public static double LIMIT_TEH_TEKIZLEMEK;
//    public static double LIMIT_TEH_CILLER;
//    public static double LIMIT_TEH_CIZILLEMEK;
//    public static double LIMIT_TEH_EKIS;
//    public static double LIMIT_MIN_AMONIL;
//    public static double LIMIT_MIN_KARBAMID;
//    public static double LIMIT_SUW;
//    public static double LIMIT_HIMIKI;
//    public static double LIMIT_AVIA;



    public static double LIM_PA_MINERAL_DOKUNLER;
    public static double LIM_PA_TOHUM;
    public static double LIM_PA_TEH_HYZMAT;
    public static double LIM_PA_SUW_HYZMAT;
    public static double LIM_PA_UCAR_HYZMAT;
    public static double LIM_PA_OSUMLIKLERI_GORAMAK;
    public static double LIM_PA_MIKRODOKUNLERI_GARMAK;
    public static double LIM_PA_KEBELEK_TUTUJY;
    public static double LIM_PA_YAPRAGY_DUSURMEK;
    public static double LIM_PA_HASAL_OTLARA_GARSY;
    public static double LIM_PA_ULAG_CYKDAJYLARY;
    public static double LIM_PA_AWTOULAG_HARAJATLARY;

    public static double LIM_SA_MINERAL_DOKUNLER;
    public static double LIM_SA_TOHUM;
    public static double LIM_SA_TEH_HYZMAT;
    public static double LIM_SA_SUW_HYZMAT;
    public static double LIM_SA_UCAR_HYZMAT;
    public static double LIM_SA_HIMIKI;

    public static double LIM_SU_MINERAL_DOKUNLER;
    public static double LIM_SU_TOHUM;
    public static double LIM_SU_TEH_HYZMAT;
    public static double LIM_SU_SUW_HYZMAT;
    public static double LIM_SU_HIMIKI;

    public static double LIM_BU_MINERAL_DOKUNLER;
    public static double LIM_BU_TOHUM;
    public static double LIM_BU_TEH_HYZMAT;
    public static double LIM_BU_SUW_HYZMAT;
    public static double LIM_BU_UCAR_HYZMAT;
    public static double LIM_BU_OSUMLIKLERI_GORAMAK;



    public static ArrayList<String> AccSalyKarhanasy;
    public static ArrayList<String> AccGallaKarhanasy;
    public static ArrayList<String> AccPagtaKarhanasy;
    public static ArrayList<String> AccSugundyrKarhanasy;
    public static ArrayList<String> AccTehKarhanasy;
    public static ArrayList<String> AccSuwKarhanasy;
    public static ArrayList<String> AccDokunKarhanasy;
    public static ArrayList<String> AccOsumligiGoramak;
    public static ArrayList<String> AccHowaYollary;
    public static ArrayList<String> AccDayhanBirlesik;



    public static void main(String[] args) {
        //System.out.println("STARTED !");


        Date date = new Date();


        //System.out.println(date.format(date));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");


        Properties Properties = new Properties();
        try{
            PROP = Properties.getProp(date, dateFormat);
        } catch (Exception err){
            log.error("ERROR. CAN`T GET PROPERTIES", err);
            System.exit(0);
        }


        LIM_PA_MINERAL_DOKUNLER =       (double) PROP.get("LIM_PA_MINERAL_DOKUNLER");
        LIM_PA_TOHUM =                  (double) PROP.get("LIM_PA_TOHUM");
        LIM_PA_TEH_HYZMAT =             (double) PROP.get("LIM_PA_TEH_HYZMAT");
        LIM_PA_SUW_HYZMAT =             (double) PROP.get("LIM_PA_SUW_HYZMAT");
        LIM_PA_UCAR_HYZMAT =            (double) PROP.get("LIM_PA_UCAR_HYZMAT");
        LIM_PA_OSUMLIKLERI_GORAMAK =    (double) PROP.get("LIM_PA_OSUMLIKLERI_GORAMAK");
        LIM_PA_MIKRODOKUNLERI_GARMAK =  (double) PROP.get("LIM_PA_MIKRODOKUNLERI_GARMAK");
        LIM_PA_KEBELEK_TUTUJY =         (double) PROP.get("LIM_PA_KEBELEK_TUTUJY");
        LIM_PA_YAPRAGY_DUSURMEK =       (double) PROP.get("LIM_PA_YAPRAGY_DUSURMEK");
        LIM_PA_HASAL_OTLARA_GARSY =     (double) PROP.get("LIM_PA_HASAL_OTLARA_GARSY");
        LIM_PA_ULAG_CYKDAJYLARY =       (double) PROP.get("LIM_PA_ULAG_CYKDAJYLARY");
        LIM_PA_AWTOULAG_HARAJATLARY =   (double) PROP.get("LIM_PA_AWTOULAG_HARAJATLARY");

        LIM_SA_MINERAL_DOKUNLER =       (double) PROP.get("LIM_SA_MINERAL_DOKUNLER");
        LIM_SA_TOHUM =                  (double) PROP.get("LIM_SA_TOHUM");
        LIM_SA_TEH_HYZMAT =             (double) PROP.get("LIM_SA_TEH_HYZMAT");
        LIM_SA_SUW_HYZMAT =             (double) PROP.get("LIM_SA_SUW_HYZMAT");
        LIM_SA_UCAR_HYZMAT =            (double) PROP.get("LIM_SA_UCAR_HYZMAT");
        LIM_SA_HIMIKI =                 (double) PROP.get("LIM_SA_HIMIKI");

        LIM_SU_MINERAL_DOKUNLER =       (double) PROP.get("LIM_SU_MINERAL_DOKUNLER");
        LIM_SU_TOHUM =                  (double) PROP.get("LIM_SU_TOHUM");
        LIM_SU_TEH_HYZMAT =             (double) PROP.get("LIM_SU_TEH_HYZMAT");
        LIM_SU_SUW_HYZMAT =             (double) PROP.get("LIM_SU_SUW_HYZMAT");
        LIM_SU_HIMIKI =                 (double) PROP.get("LIM_SU_HIMIKI");

        LIM_BU_MINERAL_DOKUNLER =       (double) PROP.get("LIM_BU_MINERAL_DOKUNLER");
        LIM_BU_TOHUM =                  (double) PROP.get("LIM_BU_TOHUM");
        LIM_BU_TEH_HYZMAT =             (double) PROP.get("LIM_BU_TEH_HYZMAT");
        LIM_BU_SUW_HYZMAT =             (double) PROP.get("LIM_BU_SUW_HYZMAT");
        LIM_BU_UCAR_HYZMAT =            (double) PROP.get("LIM_BU_UCAR_HYZMAT");
        LIM_BU_OSUMLIKLERI_GORAMAK =    (double) PROP.get("LIM_BU_OSUMLIKLERI_GORAMAK");


        AccSalyKarhanasy =      Properties.getAccounts("ACCOUNTS/01_saly_karhanasy");
        AccGallaKarhanasy =     Properties.getAccounts("ACCOUNTS/02_galla_karhanasy");
        AccPagtaKarhanasy =     Properties.getAccounts("ACCOUNTS/03_pagta_karhanasy");
        AccSugundyrKarhanasy =  Properties.getAccounts("ACCOUNTS/04_sugundyr_karhanasy");
        AccTehKarhanasy =       Properties.getAccounts("ACCOUNTS/05_teh_karhanasy");
        AccSuwKarhanasy =       Properties.getAccounts("ACCOUNTS/06_suw_karhanasy");
        AccDokunKarhanasy =     Properties.getAccounts("ACCOUNTS/07_dokun_karhanasy");
        AccOsumligiGoramak =    Properties.getAccounts("ACCOUNTS/08_osumligi_goramak");
        AccHowaYollary =        Properties.getAccounts("ACCOUNTS/09_howa_yollary");
        AccDayhanBirlesik =     Properties.getAccounts("ACCOUNTS/10_dayhan_birlesik");




        DBWorker DBWorker = new DBWorker();
        try {
            DBWorker.createTablesOnStart();
        } catch (SQLException e) {
            log.error("ERROR. CAN`T CREATE TABLES ON START", e);
            System.exit(0);
        }




        try (
                ServerSocket AAPaymentGateway = new ServerSocket((int) PROP.get("MODULE_PORT"), 0, (InetAddress) PROP.get("MODULE_IP"))
        ){

            log.info("\n\n" +
                    "======================================================\n" +
                    "DAYHAN LIMITS v 1.0\n" +
                    "STARTED AT " + dateFormat.format(date) + "\n" +
                    "MODULE_IP        :   " + String.valueOf(PROP.get("MODULE_IP")).substring(1) + "\n" +
                    "MODULE_PORT      :   " + PROP.get("MODULE_PORT") + "\n" +
                    "======================================================\n\n");

            //ExecutorService es = Executors.newFixedThreadPool(4);
            ExecutorService es = Executors.newCachedThreadPool();



            //while (!STOP) {
            while (true) {

                try{

                    Socket ClientConnected = AAPaymentGateway.accept();
                    String ip = String.valueOf(ClientConnected.getRemoteSocketAddress()).substring(1);

                    log.info("NEW CONNECTION FROM: " + ip);
                    String tName = "D " + ip;

                    try{
                        es.submit(new ClientHandleThread(ClientConnected, tName));

                    } catch (Exception err){
                        log.error(ip + " THREAD START ERROR ", err);
                    }

                } catch (Exception err){
                    err.printStackTrace();
                }




            }


        } catch (IOException err) {
            log.error("CAN`T BIND ADDRESS OR PORT !!!", err);
        }

    }


}
