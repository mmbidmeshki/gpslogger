package com.mendhak.gpslogger.loggers.usage;

import android.app.usage.UsageEvents;
import android.location.Location;
import android.support.annotation.Nullable;

import com.mendhak.gpslogger.common.Strings;
import com.mendhak.gpslogger.loggers.FileLogger;
import com.mendhak.gpslogger.loggers.Files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mahdi on 5/25/2017.
 */

public class AppUsageLogger {
    private final Float batteryLevel;
    private File file;
    protected final String name = "TXT";

    public AppUsageLogger (File file, @Nullable Float batteryLevel) {
        this.file = file;
        this.batteryLevel = batteryLevel;
    }

    public void write(UsageEvents.Event event) throws Exception {
        if (!file.exists()) {
            file.createNewFile();

            FileOutputStream writer = new FileOutputStream(file, true);
            BufferedOutputStream output = new BufferedOutputStream(writer);
            String header = "time,package,event,battery\n";
            output.write(header.getBytes());
            output.flush();
            output.close();

        }

        FileOutputStream writer = new FileOutputStream(file, true);
        BufferedOutputStream output = new BufferedOutputStream(writer);

        String dateTimeString = Strings.getIsoDateTime(new Date(event.getTimeStamp()));
        String outputString = String.format(Locale.US, "%s,%s,%d,%s\n",
                dateTimeString,
                event.getPackageName(),
                event.getEventType(),
                (batteryLevel != null) ? batteryLevel : ""
        );

        output.write(outputString.getBytes());
        output.flush();
        output.close();
        Files.addToMediaDatabase(file, "text/csv");
    }

}
