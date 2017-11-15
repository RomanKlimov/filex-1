/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.file_adapters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;

/**
 *
 * @author User
 */
public class FileInfo {

    public String getInfo(Path path) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        String info = "CreationTime: " + convertDate(attr.creationTime().toMillis()) + "<br>\n";
        info += "LastAccessTime: " + convertDate(attr.lastAccessTime().toMillis()) + "<br>\n";
        info += "Size: " + String.valueOf(attr.size() / 1024) + " kb" + "\n";

        return info;
    }

    private String convertDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "." + String.valueOf(calendar.get(Calendar.MONTH))
                + "." + String.valueOf(calendar.get(Calendar.YEAR)) + " " + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(calendar.get(Calendar.MINUTE) + ":" + String.valueOf(calendar.get(Calendar.SECOND)));
        return date;
    }

}
