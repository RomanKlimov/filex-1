/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.folder_adapters;

import java.io.File;

/**
 *
 * @author User
 */
public class createFolder {

    public void create(int name) {
        new File("E:\\" + String.valueOf(name)).mkdir();
    }
}
