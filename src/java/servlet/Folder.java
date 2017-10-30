/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
@WebServlet(name = "Folder", urlPatterns = {"/Folder"})
public class Folder extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String path = (String) session.getAttribute("goTo");
        if (path == null) {
            path = request.getParameter("goTo");
        }
        if (path == null) {
            path = "E:\\upload";
        }
        session.removeAttribute("goTo");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Folder</title>");
            out.println("<style type=\"text/css\">\n"
                    + "   TABLE {\n"
                    + "    background: maroon;\n"
                    + "    color: white; \n"
                    + "   }\n"
                    + "   TD {\n"
                    + "    background: navy;\n"
                    + "   }\n"
                    + "  </style>"
                    + "</head>");
            out.println("<body>");
            out.println("<p>" + path + "</p>");
            out.println("<table cellpadding=\"4\" cellspacing=\"1\">\n"
                    + "<tr><th>Button</th><th>Direcory/Name</th><th>Path</th><th>Delete</th></tr>");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
                for (Path file : stream) {
                    out.println("<tr>"
                            + "<td>");
                    String toInsert = "Default";
                    if (file.toFile().isDirectory()) {
                        out.println("<form method=\"post\"\n"
                                + "action=\"Folder\"\n"
                                + "autocomplete=\"off\">"
                                + "<input type=\"hidden\" name=\"goTo\" value=\"" + file + "\">"
                                //+ "<input type=\"button\" name=\"goTo\"  onClick=\"document.location = 'Folder'\"/>"
                                + "<input type=\"submit\" value=\"Open\">"
                                + "</form>");
                    }
                    if (file.toFile().isFile()) {
                        out.println("<form method=\"get\"\n"
                                + "action=\"UploadDownloadFileServlet\"\n"
                                + "autocomplete=\"off\">"
                                + "<input type=\"hidden\" name=\"toDownload\" value=\"" + file + "\">"
                                + "<input type=\"submit\" value=\"Download\">"
                                + "</form>");
                    }
                    out.println("<td>" + file.getFileName() + "</td>"
                            + "<td>" + file + "</td>");

                    out.println("<td><form method=\"post\"\n"
                            + "action=\"DeleteFolderAndFile\"\n"
                            + "autocomplete=\"off\">"
                            + "<input type=\"hidden\" name=\"whereTo\" value=\"" + file + "\">");
                    if (file.toFile().isDirectory()) {
                        out.println("<input type=\"submit\" value=\"Delete Folder\">");
                    }
                    if (file.toFile().isFile()) {
                        out.println("<input type=\"submit\" value=\"Delete File\">");
                    }
                    out.println("</form>\n</td>");

                }

            } catch (IOException | DirectoryIteratorException x) {
                System.err.println(x);
            }
            //out.println(Files.walk(Paths.get("C:\\Users\\User\\Desktop\\upload")).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList()));
            out.println("<form action=\"UploadDownloadFileServlet\" method=\"post\" enctype=\"multipart/form-data\">\n"
                    + "            Select File to Upload:<input type=\"file\" name=\"fileName\">\n"
                    + "<input type=\"hidden\" name=\"toUpload\" value=\"" + path + "\">"
                    + "            <input type=\"submit\" value=\"Upload File\">\n"
                    + "        </form><br>");

            out.println("<form method=\"post\"\n"
                    + "action=\"MakeDir\"\n"
                    + "autocomplete=\"off\">"
                    + "<input type=\"hidden\" name=\"whereTo\" value=\"" + path + "\">"
                    + "<input type=\"text\" name=\"folderName\">"
                    //+ "<input type=\"button\" name=\"goTo\"  onClick=\"document.location = 'Folder'\"/>"
                    + "<input type=\"submit\" value=\"Create Dir\">"
                    + "</form>");
            
            File pFolder = new File((String) request.getParameter("whereTo"));
            Path pFolderPath = pFolder.toPath();
            pFolderPath = pFolderPath.getParent();
            

            out.println("</table>\n<br>");
            out.println("<form method=\"post\"\n"
                    + "action=\"Folder\"\n"
                    + "autocomplete=\"off\">"
                    + "<input type=\"hidden\" name=\"whereTo\" value=\"" + pFolderPath.toAbsolutePath() + "\">"
                    + "<input type=\"submit\" value=\"Back\">"
                    + "</form>");
            out.println("</body>");
            out.println("</html>");
        }

    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
