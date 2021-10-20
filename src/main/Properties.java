package main;

import org.apache.log4j.Logger;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Properties {

    private final Logger log = Logger.getLogger(Properties.class);


    public HashMap<String, Serializable> getProp(Date date, SimpleDateFormat dateFormat) throws UnknownHostException {


        HashMap<String, Serializable> coreProperties = new HashMap<>();
        ////////////////////////////

        java.util.Properties config = new java.util.Properties();
        try {
            config.load(new FileInputStream(new File("config.properties")));
        } catch (IOException e) {
            log.error("CAN`T LOAD PROPERTIES FILE", e);
            //e.printStackTrace();
        }

        coreProperties.put("MODULE_IP",   InetAddress.getByName(config.getProperty("MODULE_IP")));
        coreProperties.put("MODULE_PORT", Integer.parseInt(config.getProperty("MODULE_PORT")));
        //coreProperties.put("DB_IP",       config.getProperty("DB_IP"));


        coreProperties.put("LIM_PA_MINERAL_DOKUNLER",           Double.parseDouble(config.getProperty("LIM_PA_MINERAL_DOKUNLER")));
        coreProperties.put("LIM_PA_TOHUM",           Double.parseDouble(config.getProperty("LIM_PA_TOHUM")));
        coreProperties.put("LIM_PA_TEH_HYZMAT",           Double.parseDouble(config.getProperty("LIM_PA_TEH_HYZMAT")));
        coreProperties.put("LIM_PA_SUW_HYZMAT",           Double.parseDouble(config.getProperty("LIM_PA_SUW_HYZMAT")));
        coreProperties.put("LIM_PA_UCAR_HYZMAT",           Double.parseDouble(config.getProperty("LIM_PA_UCAR_HYZMAT")));
        coreProperties.put("LIM_PA_OSUMLIKLERI_GORAMAK",           Double.parseDouble(config.getProperty("LIM_PA_OSUMLIKLERI_GORAMAK")));
        coreProperties.put("LIM_PA_MIKRODOKUNLERI_GARMAK",           Double.parseDouble(config.getProperty("LIM_PA_MIKRODOKUNLERI_GARMAK")));
        coreProperties.put("LIM_PA_KEBELEK_TUTUJY",           Double.parseDouble(config.getProperty("LIM_PA_KEBELEK_TUTUJY")));
        coreProperties.put("LIM_PA_YAPRAGY_DUSURMEK",           Double.parseDouble(config.getProperty("LIM_PA_YAPRAGY_DUSURMEK")));
        coreProperties.put("LIM_PA_HASAL_OTLARA_GARSY",           Double.parseDouble(config.getProperty("LIM_PA_HASAL_OTLARA_GARSY")));
        coreProperties.put("LIM_PA_ULAG_CYKDAJYLARY",           Double.parseDouble(config.getProperty("LIM_PA_ULAG_CYKDAJYLARY")));
        coreProperties.put("LIM_PA_AWTOULAG_HARAJATLARY",           Double.parseDouble(config.getProperty("LIM_PA_AWTOULAG_HARAJATLARY")));

        coreProperties.put("LIM_SA_MINERAL_DOKUNLER",           Double.parseDouble(config.getProperty("LIM_SA_MINERAL_DOKUNLER")));
        coreProperties.put("LIM_SA_TOHUM",           Double.parseDouble(config.getProperty("LIM_SA_TOHUM")));
        coreProperties.put("LIM_SA_TEH_HYZMAT",           Double.parseDouble(config.getProperty("LIM_SA_TEH_HYZMAT")));
        coreProperties.put("LIM_SA_SUW_HYZMAT",           Double.parseDouble(config.getProperty("LIM_SA_SUW_HYZMAT")));
        coreProperties.put("LIM_SA_UCAR_HYZMAT",           Double.parseDouble(config.getProperty("LIM_SA_UCAR_HYZMAT")));
        coreProperties.put("LIM_SA_HIMIKI",           Double.parseDouble(config.getProperty("LIM_SA_HIMIKI")));

        coreProperties.put("LIM_SU_MINERAL_DOKUNLER",           Double.parseDouble(config.getProperty("LIM_SU_MINERAL_DOKUNLER")));
        coreProperties.put("LIM_SU_TOHUM",           Double.parseDouble(config.getProperty("LIM_SU_TOHUM")));
        coreProperties.put("LIM_SU_TEH_HYZMAT",           Double.parseDouble(config.getProperty("LIM_SU_TEH_HYZMAT")));
        coreProperties.put("LIM_SU_SUW_HYZMAT",           Double.parseDouble(config.getProperty("LIM_SU_SUW_HYZMAT")));
        coreProperties.put("LIM_SU_HIMIKI",           Double.parseDouble(config.getProperty("LIM_SU_HIMIKI")));

        coreProperties.put("LIM_BU_MINERAL_DOKUNLER",           Double.parseDouble(config.getProperty("LIM_BU_MINERAL_DOKUNLER")));
        coreProperties.put("LIM_BU_TOHUM",           Double.parseDouble(config.getProperty("LIM_BU_TOHUM")));
        coreProperties.put("LIM_BU_TEH_HYZMAT",           Double.parseDouble(config.getProperty("LIM_BU_TEH_HYZMAT")));
        coreProperties.put("LIM_BU_SUW_HYZMAT",           Double.parseDouble(config.getProperty("LIM_BU_SUW_HYZMAT")));
        coreProperties.put("LIM_BU_UCAR_HYZMAT",           Double.parseDouble(config.getProperty("LIM_BU_UCAR_HYZMAT")));
        coreProperties.put("LIM_BU_OSUMLIKLERI_GORAMAK",           Double.parseDouble(config.getProperty("LIM_BU_OSUMLIKLERI_GORAMAK")));


        // OUT

        File std_err = new File("std_err");
        if (!std_err.exists()) {
            std_err.mkdir();
        }

        File std_out = new File("std_out");
        if (!std_out.exists()) {
            std_out.mkdir();
        }



        try {
            System.setErr(new PrintStream(new FileOutputStream("std_err/std_err_" + dateFormat.format(date) + ".log")));
            System.setOut(new PrintStream(new FileOutputStream("std_out/std_out_" + dateFormat.format(date) + ".log")));
        } catch (FileNotFoundException e) {
            log.error("CAN`T CREATE STD_OUT FILES", e);
            //e.printStackTrace();
        }


        ////////////////////////////
        return coreProperties;
    }

    public ArrayList<String> getAccounts(String fileName){

        File accountsFile = new File(fileName);
        FileInputStream fis = null;
        try {
             fis = new FileInputStream(accountsFile);
        } catch (FileNotFoundException e) {
            log.error(" ACCOUNT FILE NOT EXIST (" + fileName + ")" , e);
            System.exit(0);
            //e.printStackTrace();
        }

        String accountsList = null;
        try {
            byte[] aListB = new byte[Objects.requireNonNull(fis).available()];
            fis.read(aListB);
            accountsList = new String(aListB);

        } catch (IOException e) {
            log.error(" CAN`T READ ACCOUNT FILE (" + fileName + ")", e);
            System.exit(0);
            //e.printStackTrace();
        }

        //String[] accountsListArr = accountsList.split("\r\n");
        String[] accountsListArr = accountsList.split("\n");
        int count = 0;
        for (String acc : accountsListArr) {
            accountsListArr[count] = acc.trim();
            count++;
        }

        //System.out.println(fileName + " : " + Arrays.asList(accountsListArr));

        return new ArrayList<>(Arrays.asList(accountsListArr));

    }



}
