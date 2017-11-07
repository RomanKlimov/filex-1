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
import servlet.folder_adapters.FolderCheck;

/**
 *
 * @author User
 */
@WebServlet(name = "Folder", urlPatterns = {"/Folder"})
public class Main_servlet extends HttpServlet {

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

        FolderCheck check = new FolderCheck();
        FileLocationContextListener cl = new FileLocationContextListener();

        if (path == null) {
            path = request.getParameter("goTo");
        }
        if (path == null) {
            path = "E:\\upload";
        }
        session.removeAttribute("goTo");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>"
                    + "<link href=\"buttons.css\" rel=\"stylesheet\">"
                    + "<link href=\"hiddenWindow.css\" rel=\"stylesheet\">"
                    + "<link href=\"fileUpload.css\" rel=\"stylesheet\">"
                    + "<link href=\"links.css\" rel=\"stylesheet\">"
                    + "<link href=\"tables.css\" rel=\"stylesheet\">");
            out.println("<head>");
            out.println("<title>Servlet Folder</title>"
                    + "</head>");
            out.println("<body>");
            out.println("<p>" + path + "</p>");
            if (!path.equals(cl.getPath())) {
                out.println("<form method=\"post\"\n"
                        + "action=\"Folder\"\n"
                        + "autocomplete=\"off\">"
                        + "<input type=\"hidden\" name=\"goTo\" value=\"" + cl.getPath() + "\">"
                        + "<input type=\"submit\" value=\"Main Folder\">"
                        + "</form><br>");
            }
            out.println("<div id=\"uploadDIV\">\n"
                    + "      <div id=\"okno\">\n"
                    + "<form action=\"UploadDownloadFileServlet\" method=\"post\" enctype=\"multipart/form-data\">\n"
                    + "<input type=\"file\" name=\"fileName\" multiple>\n"
                    + "<input type=\"hidden\" name=\"toUpload\" value=\"" + path + "\">"
                    + "<input type=\"submit\" value=\"Upload\">\n"
                    + "</form><br>"
                    + "<a href=\"#\" class=\"close\">Close</a>"
                    + "      </div>\n"
                    + "    </div>\n"
                    + "<a href=\"#uploadDIV\">Upload</a>");

            out.println("<div id=\"createFolderDIV\">\n"
                    + "      <div id=\"okno\">\n"
                    + "<form method=\"post\"\n"
                    + "action=\"MakeDir\"\n"
                    + "autocomplete=\"off\">"
                    + "<input type=\"hidden\" name=\"whereTo\" value=\"" + path + "\">"
                    + "<input type=\"text\" name=\"folderName\">"
                    + "<input type=\"submit\" value=\"Create Dir\">"
                    + "</form><br>"
                    + "<a href=\"#\" class=\"close\">Close</a>"
                    + "      </div>\n"
                    + "    </div>\n"
                    + "<a href=\"#createFolderDIV\">Create Folder</a>");
            out.println("<table cellpadding=\"4\" cellspacing=\"1\">\n"
                    + "<tr><th>Button</th><th>Direcory/Name</th><th>Path</th><th>Delete</th></tr>");
            File pFolder = new File(path);
            Path pFolderPath = pFolder.toPath();

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
                                //+ "<input type=\"button\" name=\"goTo\"  onClick=\"document.location = 'Main_servlet'\"/>"
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
                    out.println("</form>\n");
                    if (file.toFile().isDirectory()) {
                        out.println("<form method=\"get\"\n"
                                + "action=\"zipFolder\"\n"
                                + "autocomplete=\"off\">"
                                + "<input type=\"hidden\" name=\"toZip\" value=\"" + file + "\">"
                                + "<input type=\"submit\" value=\"ZIP\">"
                                + "</form>\n</td>");
                    }
                    if (file.toString().endsWith(".zip")) {
                        out.println("<form method=\"get\"\n"
                                + "action=\"unZipFolder\"\n"
                                + "autocomplete=\"off\">"
                                + "<input type=\"hidden\" name=\"toUnZip\" value=\"" + file + "\">"
                                + "<input type=\"submit\" value=\"unZip\">"
                                + "</form>");
                    }

                }

                pFolderPath = pFolderPath.getParent();
            } catch (IOException | DirectoryIteratorException x) {
                System.err.println(x);
            }

            //out.println(Files.walk(Paths.get("C:\\Users\\User\\Desktop\\upload")).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList()));
            out.println("</table>\n<br>");

            if (check.check(pFolderPath.toAbsolutePath().toString())) {
                out.println("<form method=\"post\"\n"
                        + "action=\"Folder\"\n"
                        + "autocomplete=\"off\">"
                        + "<input type=\"hidden\" name=\"goTo\" value=\"" + pFolderPath.toAbsolutePath() + "\">"
                        + "<input type=\"submit\" value=\"Back\">"
                        + "</form>");
            }
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
