/*
 * Copyright 2017 User.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package servlet.admin_servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.adminDb.LogsDAO;
import servlet.registration.exceptions.DBException;

/**
 *
 * @author User
 */
@WebServlet(name = "getAllLogs", urlPatterns = {"/getAllLogs"})
public class getAllLogs extends HttpServlet {

  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LogsDAO dao = new LogsDAO();
        String path = "";
        try {
            
            path = dao.getAllLogs();
        } catch (DBException ex) {
            Logger.getLogger(getAllLogs.class.getName()).log(Level.SEVERE, null, ex);
        }
        File file = new File(path);

        if (!file.exists()) {
            throw new ServletException("File doesn't exists on server.");
        }

        System.out.println("File location on server::" + file.getAbsolutePath());
        ServletContext ctx = getServletContext();
        try (InputStream fis = new FileInputStream(file)) {
            String mimeType = ctx.getMimeType(file.getAbsolutePath());
            response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            try (ServletOutputStream os = response.getOutputStream()) {
                byte[] bufferData = new byte[1024];
                int read = 0;
                while ((read = fis.read(bufferData)) != -1) {
                    os.write(bufferData, 0, read);
                }
                os.flush();
            }
        }
        System.out.println("File downloaded at client successfully");
        request.getRequestDispatcher("Admin").forward(request, response);
    }


}
