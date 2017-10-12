package com.ef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.FileSystemNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by dipak on 10/11/2017.
 */
public class Parser {
    public static void main(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Exactly 4 parameters required !");
        }
        try {
            String logPath = args[0];
            String startDate = args[1].replace('.', ' ');
            String duration = args[2];
            int threshold = 0;
            try {
                threshold = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Exception on input 4 "+e.getMessage());
            }
            SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
            Date formattedStartDate = formatter.parse(startDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(formattedStartDate);
            if (duration.equals(Constants.THRESHOLD_HOULRY)){
                cal.add(Calendar.HOUR_OF_DAY,1);
            }else if (duration.equals(Constants.THRESHOLD_DAILY)){
                cal.add(Calendar.DAY_OF_WEEK,1);
            }
            String endDate = formatter.format(cal.getTime());
            Date formattedEndDate = formatter.parse(endDate);
            if(logPath !=null) {
                String delimiter = Pattern.quote(Constants.DELIMITER);
                BufferedReader bufferedReader = new BufferedReader(new FileReader(logPath));
                String logLine;
                List<String> ipAddressList = new ArrayList<String>();
                while ((logLine = bufferedReader.readLine()) != null) {
                    String[] splitData = logLine.split(delimiter);
                    Date formattedLogDate = formatter.parse(splitData[0]);
                    if (formattedLogDate.after(formattedStartDate) && formattedLogDate.before(formattedEndDate)){
                        ipAddressList.add(splitData[1]);
                    }
                }
                bufferedReader.close();
                Map<String, Integer> duplicateIP = new HashMap<String, Integer>();
                for (String ipAddress : ipAddressList) {
                    duplicateIP.put(ipAddress, duplicateIP.containsKey(ipAddress) ? duplicateIP.get(ipAddress) + 1 : 1);
                }
                for (Map.Entry<String, Integer> entry : duplicateIP.entrySet()) {
                    if (entry.getValue()>= threshold) {
                        System.out.println("IP Address: " + entry.getKey() + ", Total requests: " + entry.getValue());
                        InsertHelper.insertDataIntoDB(entry.getKey(), entry.getValue());
                    }
                }
            }else {
                throw new FileSystemNotFoundException("File path parameter is missed.");
            }
        } catch (Exception ex) {
            System.out.println("Exception found "+ex.getMessage());
        }
    }
}
