/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.folder_adapters;

import java.io.File;
import java.nio.file.Path;
import servlet.FileLocationContextListener;

/**
 *
 * @author User
 */
public class FolderCheck {

    public boolean check(String path) {
        FileLocationContextListener listener;
        listener = new FileLocationContextListener();
        File fileG = new File(listener.getPath());
        Path general = fileG.toPath();
        File fileN = new File(path);
        Path newF = fileN.toPath();
        return newF.startsWith(general);
    }
}
