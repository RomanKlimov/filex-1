/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.folder_adapters;

import servlet.registration.models.User;

/**
 *
 * @author User
 */
public class whereIsUser {
    public String checkWhere(User user, String path){
        String dir = "E:\\" + String.valueOf(user.getFolder());
        return path.replace(dir, "your personal folder");
    }
}
