package main;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static main.Main.*;

public class DBWorker {

    //private static final Logger log = Logger.getLogger(DBWorker.class);
    private final Logger log = Logger.getLogger(DBWorker.class);


    private Connection connection;
    private Statement connStatement;
    private ResultSet requestResult;
    private String ip;



    private double LIM_PA_MINERAL_DOKUNLER_T;
    private double LIM_PA_TOHUM_T;
    private double LIM_PA_TEH_HYZMAT_T;
    private double LIM_PA_SUW_HYZMAT_T;
    private double LIM_PA_UCAR_HYZMAT_T;
    private double LIM_PA_OSUMLIKLERI_GORAMAK_T;
    private double LIM_PA_MIKRODOKUNLERI_GARMAK_T;
    private double LIM_PA_KEBELEK_TUTUJY_T;
    private double LIM_PA_YAPRAGY_DUSURMEK_T;
    private double LIM_PA_HASAL_OTLARA_GARSY_T;
    private double LIM_PA_ULAG_CYKDAJYLARY_T;
    private double LIM_PA_AWTOULAG_HARAJATLARY_T;

    private double LIM_SA_MINERAL_DOKUNLER_T;
    private double LIM_SA_TOHUM_T;
    private double LIM_SA_TEH_HYZMAT_T;
    private double LIM_SA_SUW_HYZMAT_T;
    private double LIM_SA_UCAR_HYZMAT_T;
    private double LIM_SA_HIMIKI_T;

    private double LIM_SU_MINERAL_DOKUNLER_T;
    private double LIM_SU_TOHUM_T;
    private double LIM_SU_TEH_HYZMAT_T;
    private double LIM_SU_SUW_HYZMAT_T;
    private double LIM_SU_HIMIKI_T;

    private double LIM_BU_MINERAL_DOKUNLER_T;
    private double LIM_BU_TOHUM_T;
    private double LIM_BU_TEH_HYZMAT_T;
    private double LIM_BU_SUW_HYZMAT_T;
    private double LIM_BU_UCAR_HYZMAT_T;
    private double LIM_BU_OSUMLIKLERI_GORAMAK_T;

    public DBWorker(){}
    public DBWorker(String ip){
        this.ip = ip;
    }


    private void postgreSQLConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ttgate", "postgres", "postgre@!");
            //connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ttgate", "postgres", "postgres");
            connStatement = connection.createStatement();
        } catch (SQLException e) {
            log.error("ERROR: UNABLE CONNECT TO DB ! :(");
            System.err.println("ERROR: UNABLE CONNECT TO DB !");
        }
    }

    private void closeConnections(){
        try {
            if(requestResult != null){requestResult.close();}
            if(connStatement != null){connStatement.close();}
            if(connection != null){connection.close();}
        } catch (SQLException e) {
            log.error(ip + " ERROR: CLOSING DB CONNECTIONS ERROR ! :(");
            System.err.println(ip + " ERROR: CLOSING DB CONNECTIONS ERROR !");
        }
    }




    private String createTable1(){

        return "CREATE TABLE IF NOT EXISTS limits" +
                "(\n" +
                "  id bigserial NOT NULL,\n" +
                "  pan text,\n" +
                "  field_area smallint,\n" +
                "  lim_mineral_dokunler numeric DEFAULT -1,\n" +
                "  lim_tohum numeric DEFAULT -1,\n" +
                "  lim_teh_hyzmat numeric DEFAULT -1,\n" +
                "  lim_suw_hyzmat numeric DEFAULT -1,\n" +
                "  lim_ucar_hyzmat numeric DEFAULT -1,\n" +
                "  lim_himiki numeric DEFAULT -1,\n" +
                "  lim_osumlikleri_goramak numeric DEFAULT -1,\n" +
                "  lim_mikrodokunleri_garmak numeric DEFAULT -1,\n" +
                "  lim_kebelek_tutujy numeric DEFAULT -1,\n" +
                "  lim_yapragy_dusurmek numeric DEFAULT -1,\n" +
                "  lim_hasal_otlara_garsy numeric DEFAULT -1,\n" +
                "  lim_ulag_cykdajylary numeric DEFAULT -1,\n" +
                "  lim_awtoulag_harajatlary numeric DEFAULT -1,\n" +
                "  CONSTRAINT limits_pkey PRIMARY KEY (id)\n" +
                ")";

    }

    private String createTable2(){

        return "CREATE TABLE IF NOT EXISTS lim_operations\n" +
                "(\n" +
                "  id bigserial NOT NULL,\n" +
                "  op_date date,\n" +
                "  op_time time without time zone,\n" +
                "  pan text,\n" +
                "  field_area smallint,\n" +
                "  service text,\n" +
                "  amount numeric,\n" +
                "  rrn text,\n" +
                "  tid text,\n" +
                "  status smallint,\n" +
                "  CONSTRAINT lim_operations_pkey PRIMARY KEY (id)\n" +
                ")";

    }


    public void createTablesOnStart() throws SQLException {

        postgreSQLConnection();

        connStatement.execute(createTable1());
        connStatement.execute(createTable2());

        closeConnections();

    }







    private String checkPAN(String pan){
        //System.out.println("CHECKING PAN");

//        if(!pan.substring(0, 6).equals("993443")){
//            return "726"; // PAN IS REJECTED
//        }
// TODO ENABLE IT IN PROD VERSION

        String queryToPg = String.format(Locale.US, "SELECT COUNT(pan) FROM limits WHERE pan = '%s'", pan);

        //System.out.println("QUERY : " + queryToPg);
        int count = 0;
        try {
            requestResult = Objects.requireNonNull(connStatement).executeQuery(queryToPg);
            if(requestResult.next()) {
                count = requestResult.getInt("count");
            }
        } catch (SQLException e) {
            log.error(ip + " ERROR: CHECKING PAN :(");
            System.err.println(ip + " ERROR: CHECKING PAN !");
            closeConnections();
            return "722"; // SQL/DB PROBLEM
        }

        if(count > 0){
            return "OK";
        } else {
            return "ADD";
        }

        
    }

    private void calculateLimits(String ha){

        LIM_PA_MINERAL_DOKUNLER_T = LIM_PA_MINERAL_DOKUNLER * Integer.parseInt(ha.trim());
        LIM_PA_TOHUM_T = LIM_PA_TOHUM * Integer.parseInt(ha.trim());
        LIM_PA_TEH_HYZMAT_T = LIM_PA_TEH_HYZMAT * Integer.parseInt(ha.trim());
        LIM_PA_SUW_HYZMAT_T = LIM_PA_SUW_HYZMAT * Integer.parseInt(ha.trim());
        LIM_PA_UCAR_HYZMAT_T = LIM_PA_UCAR_HYZMAT * Integer.parseInt(ha.trim());
        LIM_PA_OSUMLIKLERI_GORAMAK_T = LIM_PA_OSUMLIKLERI_GORAMAK * Integer.parseInt(ha.trim());
        LIM_PA_MIKRODOKUNLERI_GARMAK_T = LIM_PA_MIKRODOKUNLERI_GARMAK * Integer.parseInt(ha.trim());
        LIM_PA_KEBELEK_TUTUJY_T = LIM_PA_KEBELEK_TUTUJY * Integer.parseInt(ha.trim());
        LIM_PA_YAPRAGY_DUSURMEK_T = LIM_PA_YAPRAGY_DUSURMEK * Integer.parseInt(ha.trim());
        LIM_PA_HASAL_OTLARA_GARSY_T = LIM_PA_HASAL_OTLARA_GARSY * Integer.parseInt(ha.trim());
        LIM_PA_ULAG_CYKDAJYLARY_T = LIM_PA_ULAG_CYKDAJYLARY * Integer.parseInt(ha.trim());
        LIM_PA_AWTOULAG_HARAJATLARY_T = LIM_PA_AWTOULAG_HARAJATLARY * Integer.parseInt(ha.trim());

        LIM_SA_MINERAL_DOKUNLER_T = LIM_SA_MINERAL_DOKUNLER * Integer.parseInt(ha.trim());
        LIM_SA_TOHUM_T = LIM_SA_TOHUM * Integer.parseInt(ha.trim());
        LIM_SA_TEH_HYZMAT_T = LIM_SA_TEH_HYZMAT * Integer.parseInt(ha.trim());
        LIM_SA_SUW_HYZMAT_T = LIM_SA_SUW_HYZMAT * Integer.parseInt(ha.trim());
        LIM_SA_UCAR_HYZMAT_T = LIM_SA_UCAR_HYZMAT * Integer.parseInt(ha.trim());
        LIM_SA_HIMIKI_T = LIM_SA_HIMIKI * Integer.parseInt(ha.trim());

        LIM_SU_MINERAL_DOKUNLER_T = LIM_SU_MINERAL_DOKUNLER * Integer.parseInt(ha.trim());
        LIM_SU_TOHUM_T = LIM_SU_TOHUM * Integer.parseInt(ha.trim());
        LIM_SU_TEH_HYZMAT_T = LIM_SU_TEH_HYZMAT * Integer.parseInt(ha.trim());
        LIM_SU_SUW_HYZMAT_T = LIM_SU_SUW_HYZMAT * Integer.parseInt(ha.trim());
        LIM_SU_HIMIKI_T = LIM_SU_HIMIKI * Integer.parseInt(ha.trim());

        LIM_BU_MINERAL_DOKUNLER_T = LIM_BU_MINERAL_DOKUNLER * Integer.parseInt(ha.trim());
        LIM_BU_TOHUM_T = LIM_BU_TOHUM * Integer.parseInt(ha.trim());
        LIM_BU_TEH_HYZMAT_T = LIM_BU_TEH_HYZMAT * Integer.parseInt(ha.trim());
        LIM_BU_SUW_HYZMAT_T = LIM_BU_SUW_HYZMAT * Integer.parseInt(ha.trim());
        LIM_BU_UCAR_HYZMAT_T = LIM_BU_UCAR_HYZMAT * Integer.parseInt(ha.trim());
        LIM_BU_OSUMLIKLERI_GORAMAK_T = LIM_BU_OSUMLIKLERI_GORAMAK * Integer.parseInt(ha.trim());

        //System.out.println("CALCULATING IS FINISHED");
    }

    private String addNewPAN(String[] fields){


        //System.out.println("ADDING NEW PAN");
        calculateLimits(fields[5]);
        //System.out.println("END OF CALC");

        String queryToPg = null;

        switch (fields[2].substring(6, 7)){
            case "1" : {
                queryToPg = String.format(Locale.US, "INSERT INTO limits (pan, field_area, lim_mineral_dokunler, lim_tohum, lim_teh_hyzmat, lim_suw_hyzmat, lim_ucar_hyzmat, lim_himiki) " +
                       "VALUES ('%s', %d, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f)", fields[2], Integer.parseInt(fields[5].trim()), LIM_SA_MINERAL_DOKUNLER_T, LIM_SA_TOHUM_T, LIM_SA_TEH_HYZMAT_T, LIM_SA_SUW_HYZMAT_T, LIM_SA_UCAR_HYZMAT_T, LIM_SA_HIMIKI_T);
                break;
            }
            case "2" : {
                queryToPg = String.format(Locale.US, "INSERT INTO limits (pan, field_area, lim_mineral_dokunler, lim_tohum, lim_teh_hyzmat, lim_suw_hyzmat, lim_ucar_hyzmat, lim_osumlikleri_goramak) " +
                       "VALUES ('%s', %d, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f)", fields[2], Integer.parseInt(fields[5].trim()), LIM_BU_MINERAL_DOKUNLER_T, LIM_BU_TOHUM_T, LIM_BU_TEH_HYZMAT_T, LIM_BU_SUW_HYZMAT_T, LIM_BU_UCAR_HYZMAT_T, LIM_BU_OSUMLIKLERI_GORAMAK_T);
                break;
            }
            case "3" : {
                queryToPg = String.format(Locale.US, "INSERT INTO limits (pan, field_area, lim_mineral_dokunler, lim_tohum, lim_teh_hyzmat, lim_suw_hyzmat, lim_ucar_hyzmat, lim_osumlikleri_goramak, lim_mikrodokunleri_garmak, lim_kebelek_tutujy, lim_yapragy_dusurmek, lim_hasal_otlara_garsy, lim_ulag_cykdajylary, lim_awtoulag_harajatlary) " +
                       "VALUES ('%s', %d, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f)", fields[2], Integer.parseInt(fields[5].trim()), LIM_PA_MINERAL_DOKUNLER_T, LIM_PA_TOHUM_T, LIM_PA_TEH_HYZMAT_T, LIM_PA_SUW_HYZMAT_T, LIM_PA_UCAR_HYZMAT_T, LIM_PA_OSUMLIKLERI_GORAMAK_T, LIM_PA_MIKRODOKUNLERI_GARMAK_T, LIM_PA_KEBELEK_TUTUJY_T, LIM_PA_YAPRAGY_DUSURMEK_T, LIM_PA_HASAL_OTLARA_GARSY_T, LIM_PA_ULAG_CYKDAJYLARY_T, LIM_PA_AWTOULAG_HARAJATLARY_T);
                break;
            }
            case "4" : {
                queryToPg = String.format(Locale.US, "INSERT INTO limits (pan, field_area, lim_mineral_dokunler, lim_tohum, lim_teh_hyzmat, lim_suw_hyzmat, lim_himiki) " +
                       "VALUES ('%s', %d, %.2f, %.2f, %.2f, %.2f, %.2f)", fields[2], Integer.parseInt(fields[5].trim()), LIM_SU_MINERAL_DOKUNLER_T, LIM_SU_TOHUM_T, LIM_SU_TEH_HYZMAT_T, LIM_SU_SUW_HYZMAT_T, LIM_SU_HIMIKI_T);
                break;
            }


        }

        try {
            connStatement.execute(queryToPg);
        } catch (SQLException e) {
            log.error(ip + " ERROR: ADDING NEW PAN ERROR !!! :(");
            System.err.println(ip + " ERROR: ADDING NEW PAN ERROR !!!\n" + e);
            closeConnections();
            return "722"; // SQL/DB PROBLEM

        }
        //System.out.println("ADDING NEW PAN FINISHED");
        return "OK";
    }

    private String checkLimits(String[] fields){

        //System.out.println("CHECKING LIMITS...");
        String limitToFind = "lim_" + fields[4].toLowerCase().trim(); // SERVICE NAME

        String queryToPg = String.format(Locale.US, "SELECT %s, %s FROM limits WHERE pan = '%s'", limitToFind, "field_area", fields[2]);
        //System.out.println("QUERY : " + queryToPg);
        double limitAmount = 0;
        int fieldArea = 0; // HA // GEKTARS
        double paymentAmount = (Integer.parseInt(fields[3]) / 100.d);
        try {
            requestResult = connStatement.executeQuery(queryToPg);
            if(requestResult.next()) {
                limitAmount = requestResult.getDouble(limitToFind);
                fieldArea = requestResult.getInt("field_area");
            }
        } catch (SQLException e) {
            log.error(ip + " ERROR: CHECKING LIMITS ERROR !!! :(");
            System.err.println(ip + " ERROR: CHECKING LIMITS ERROR !!!\n" + e);
            closeConnections();
            return "722"; // SQL/DB PROBLEM
        }
        //System.out.println("LIMIT_AMOUNT : " + limitAmount);
        //System.out.println("GET AREA : " + fieldArea);

        if(limitAmount < paymentAmount){
            closeConnections();
            return "724"; //LIMIT EXCEEDED
        } else if (fieldArea < Integer.parseInt(fields[5])) {
            closeConnections();
            return "725"; // AREA IS TO BIG
        } else {
            closeConnections();
            return "OK";
        }



    }


    public String dbVerify(String[] fields) {

        postgreSQLConnection();

        String checkPanResult = checkPAN(fields[2]);
        String addNewPanResult;
        switch (checkPanResult){
            case "OK" : {
                return checkLimits(fields);
            }
            case "ADD" : {
                addNewPanResult = addNewPAN(fields);
                if(addNewPanResult.equals("OK")){
                    return checkLimits(fields);
                } else {
                    closeConnections();
                    return addNewPanResult;
                }
            }
            default : {
                closeConnections();
                return checkPanResult;
            }
        }



    }



    private void insertPaid(String[] fields){

        String service = fields[4].toLowerCase().trim();

        Date date = new Date();
        String dateS = String.format("%tF", date);
        String timeS = String.format("%tT", date);

        String queryToPg = String.format(Locale.US, "INSERT INTO lim_operations (op_date, op_time, pan, field_area, service, amount, rrn, tid, status) VALUES ('%s', '%s', '%s', %d, '%s', %.2f, '%s', '%s', %d)", dateS, timeS, fields[2], Integer.parseInt(fields[5].trim()), service, Integer.parseInt(fields[3]) / 100.d, fields[6], fields[7], 1);
        String queryToPg2 = String.format(Locale.US, "UPDATE limits SET lim_%s = lim_%s-%.2f WHERE pan = '%s'", service, service, Integer.parseInt(fields[3]) / 100.d, fields[2]);
//        System.out.println(queryToPg);
//        System.out.println(queryToPg2);
        try {
            connStatement.execute(queryToPg);
            log.info(ip + " OPERATION -= PAID =- SAVED");
            connStatement.execute(queryToPg2);
            log.info(ip + " LIMITS FOR SERVICE " + service.toUpperCase() + " UPDATED. PAID.\n");
        } catch (SQLException e) {
            log.error(ip + " ERROR: SAVING -= PAID =- OPERATION !!! :(");
            System.err.println(ip + " ERROR: SAVING -= PAID =- OPERATION !!!\n" + e);
            closeConnections();
        }
        closeConnections();

    }

    public void dbPaid(String[] fields){

        postgreSQLConnection();

        insertPaid(fields);




    }


    private void insertCancel(String[] fields){


        int isCanceled = 0;
        String isCanceledQuery = String.format(Locale.US, "SELECT status FROM lim_operations WHERE pan = '%s' AND rrn = '%s'", fields[2], fields[6]);
        try {
            requestResult = connStatement.executeQuery(isCanceledQuery);
            if(requestResult.next()) {
                isCanceled = requestResult.getInt("status");

            }
        } catch (SQLException e) {
            log.error(ip + " ERROR: CHECKING OPERATION STATUS !!! :(");
            System.err.println(ip + " ERROR: CHECKING OPERATION STATUS !!!\n" + e);
            closeConnections();
        }

        //System.out.println("STATUS = " + isCanceled);
        if(isCanceled > 0) {

            String queryToPg = String.format(Locale.US, "UPDATE lim_operations SET status = 0 WHERE pan = '%s' AND rrn = '%s' RETURNING service", fields[2], fields[6]);
            String service = "";
            try {
                requestResult = connStatement.executeQuery(queryToPg);
                if(requestResult.next()) {
                    service = requestResult.getString("service");

                }
                log.info(ip + " OPERATION STATUS CHANGED. CANCEL");
            } catch (SQLException e) {
                log.error(ip + " ERROR: INSERTING -= CANCEL =- OPERATION ERROR !!! :(");
                System.err.println(ip + " ERROR: INSERTING -= CANCEL =- OPERATION ERROR !!!\n" + e);
                closeConnections();
            }

            //System.out.println("SERVICE : " + service);


            String queryToPg2 = String.format(Locale.US, "UPDATE limits SET lim_%s = lim_%s + %.2f WHERE pan = '%s'", service, service, Integer.parseInt(fields[3]) / 100.d, fields[2]);
            //System.out.println("QUERY 2 : " + queryToPg2);
            try {
                connStatement.execute(queryToPg2);
                log.info(ip + " LIMITS FOR SERVICE " + service.toUpperCase() + " UPDATED. CANCEL.\n");
            } catch (SQLException e) {
                log.error(ip + " ERROR: UPDATING LIMITS. CANCEL !!! :(");
                System.err.println(ip + " ERROR: UPDATING LIMITS. CANCEL !!!\n" + e);
                closeConnections();
            }

        }


        closeConnections();



    }


    public void dbCancel(String[] fields){

        postgreSQLConnection();

        insertCancel(fields);

    }


}
