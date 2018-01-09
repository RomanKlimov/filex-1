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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.adminDb.CapacityDAO;
import servlet.registration.exceptions.AlreadyExistException;
import servlet.registration.exceptions.DBException;

/**
 *
 * @author User
 */
@WebServlet(name = "setKey", urlPatterns = {"/setKey"})
public class setKey extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");
        String email = request.getParameter("user");
        CapacityDAO dao = new CapacityDAO();
        try {
            dao.setKeyToUser(email, key);
        } catch (DBException ex) {
            Logger.getLogger(setKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyExistException ex) {
            Logger.getLogger(setKey.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("Admin").forward(request, response);
    }

}
