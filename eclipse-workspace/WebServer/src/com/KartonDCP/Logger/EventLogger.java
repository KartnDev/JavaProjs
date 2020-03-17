package com.KartonDCP.Logger;

import Configurations.ConfigModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventLogger implements ILogger{


    private File file;

    public EventLogger(File fileName){
        file = fileName;
    }
    public EventLogger(){
        String current = null;
        try {
            current = new File( "." ).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        var fileGson = new File(current + "/src/Configurations/config.JSON");

        String strJSON = null;
        try {
            strJSON = Files.readString(fileGson.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        var schema = gson.fromJson(strJSON, ConfigModel.class);

        this.file = new File(current + schema.LogsPath);
    }

    public static void logEventToFile(File file, LogEvent event) {

    }

    @Override
    public void logEvent(LogEvent event) {

    }

    @Override
    public void logException(Exception e) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss:ms z");
        Date date = new Date(System.currentTimeMillis());

        String eventString = "Where thown an exception .. Time: " + formatter.format(date) + "\n"
                              + " stackTrace were taken: ";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            eventString += e.getStackTrace()[i] + "\n";
        }
        eventString += " and message " + e.getMessage() + "\n";

        try {

            try(PrintWriter output = new PrintWriter(new FileWriter(file,true)))
            {
                output.printf("%s\r\n", eventString);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void logEventAndThrowAgain(Exception e) {

    }
}
