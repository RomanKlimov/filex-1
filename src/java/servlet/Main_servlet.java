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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import servlet.file_adapters.FileInfo;
import servlet.folder_adapters.FolderCheck;
import servlet.folder_adapters.whereIsUser;
import servlet.registration.models.User;

/**
 *
 * @author User
 */
@WebServlet(name = "Folder", urlPatterns = {"/Folder"})
public class Main_servlet extends HttpServlet {

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
            path = "E:\\" + String.valueOf(((User) session.getAttribute("user")).getFolder());
        }
        if (path.equals("E:\\")) {
            request.getRequestDispatcher("login").forward(request, response);
        }
        session.removeAttribute("goTo");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>"
                    + "<link href=\"css/buttons.css\" rel=\"stylesheet\">"
                    + "<link href=\"css/hiddenWindow.css\" rel=\"stylesheet\">"
                    + "<link href=\"css/fileUpload.css\" rel=\"stylesheet\">"
                    + "<link href=\"css/tables.css\" rel=\"stylesheet\">"
                    + "<link href=\"css/links-in-folder.css\" rel=\"stylesheet\">"
                    + "<link href=\"css/backUpCSS.css\" rel=\"stylesheet\">"
                    + "<link href=\"css/backgroundFolder.css\" rel=\"stylesheet\">"
                    + "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css\">\n"
                    + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\">"
                    + "<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js'></script>\n"
                    + "<script type=\"text/javascript\">\n"
                    + "	setTimeout(function(){$('.box').fadeOut('fast')},3000);\n"
                    + "</script>\n"
                    + "<link href=\"css/folderCSS.css\" rel=\"stylesheet\">\n"
                    + "<link rel=\"shortcut icon\" href=\"ico/ico.png\" type=\"image/png\">\n");
            out.println("<head>");
            out.println("<title>" + String.valueOf(((User) session.getAttribute("user")).getName() + "`s drive" + "</title>")
                    + "</head>");
            String message = request.getParameter("message");
            out.println("<body>\n"
                    + "<nav>\n"
                    + "            <div class=\"nav-wrapper green lighten-2\">\n"
                    + "                <a href=\"index.html\" class=\"brand-logo\"><i class=\"fa fa-cloud\" aria-hidden=\"true\"></i>FileX</a>\n"
                    + "                <a href=\"#\" data-activates=\"mobile-demo\" class=\"button-collapse\"><i class=\"material-icons\">menu</i></a>\n"
                    + "                <ul class=\"right hide-on-med-and-down\">\n"
                    + "                    <li><a href=\"index.html\">Main Page</a></li>\n"
                    + "                    <li><a href=\"logout\">Logout</a></li>\n"
                    + "                </ul>\n"
                    + "                <ul class=\"side-nav\" id=\"mobile-demo\">\n"
                    + "                    <li><a href=\"index.html\">Main Page</a></li>\n"
                    + "                    <li><a href=\"logout\">Logout</a></li>\n"
                    + "                </ul>\n"
                    + "            </div>\n"
                    + "        </nav>");
            if (message != null) {
                out.println("<div class=\"box\">\n"
                        + message
                        + "</div>");
            }
            out.println("<h3>" + "You working as: " + String.valueOf(((User) session.getAttribute("user")).getEmail()) + "</h3>");
            if ((User) session.getAttribute("user") != null) {
                out.println("<h4>" + "Now you`re in " + new whereIsUser().checkWhere((User) session.getAttribute("user"), path) + "</h4>\n<br>\n");
            }
            if (!path.equals("E:\\" + String.valueOf(((User) session.getAttribute("user")).getFolder()))) {
                out.println("<form method=\"post\"\n"
                        + "action=\"Folder\"\n"
                        + "autocomplete=\"off\">"
                        + "<input type=\"hidden\" name=\"goTo\" value=\"" + "E:\\" + String.valueOf(((User) session.getAttribute("user")).getFolder()) + "\">"
                        + "<input type=\"submit\" value=\"Main Folder\">"
                        + "</form><br>");
            }
            out.println("<div class = \"control\">\n"
                    + "<div id=\"uploadDIV\">\n"
                    + "      <div id=\"okno\" >\n"
                    + "<form action=\"UploadDownloadFileServlet\" method=\"post\" enctype=\"multipart/form-data\">\n"
                    + "<input type=\"file\" name=\"fileName\" multiple>\n"
                    + "<input type=\"hidden\" name=\"toUpload\" value=\"" + path + "\">"
                    + "<input type=\"submit\" value=\"Upload\">\n"
                    + "</form><br>"
                    + "<a href=\"#\" class=\"close\">Close</a>"
                    + "      </div>\n"
                    + "    </div>\n"
                    + "<a href=\"#uploadDIV\" class = \"important\">Upload</a>");

            out.println("<div id=\"createFolderDIV\">\n"
                    + "      <div id=\"okno\">\n"
                    + "<form method=\"post\"\n"
                    + "action=\"MakeDir\"\n"
                    + "autocomplete=\"off\">"
                    + "<input type=\"hidden\" name=\"whereTo\" value=\"" + path + "\">"
                    + "<input type=\"text\" name=\"folderName\">"
                    + "<input type=\"submit\" value=\"Create Dir\">"
                    + "</form>"
                    + "<a href=\"#\" class=\"close\">Close</a>"
                    + "      </div>\n"
                    + "    </div>\n"
                    + "<a href=\"#createFolderDIV\" class = \"important\">Create Folder</a>");

            if (path.equals("E:\\" + String.valueOf(((User) session.getAttribute("user")).getFolder()))) {
                out.println("<div id=\"backUpDIV\">\n"
                        + "      <div id=\"backUpWindow\">\n"
                        + "<form method=\"get\"\n"
                        + "action=\"zipFolder\"\n"
                        + "autocomplete=\"off\">"
                        + "<p>You really want to backup all your files?</p>\n"
                        + "<input type=\"hidden\" name=\"zipType\" value=\"backup\">"
                        + "<input type=\"hidden\" name=\"toZip\" value=\"" + path + "\">"
                        + "<input type=\"submit\" value=\"BackUp\">"
                        + "</form>\n"
                        + "<a href=\"#\" class=\"close\">Close</a>"
                        + "      </div>\n"
                        + "    </div>\n"
                        + "<a href=\"#backUpDIV\" class = \"important\">BackUp Drive</a><br><br>");
            }
            out.println("</div>");
            out.println("<table cellpadding=\"4\" cellspacing=\"1\">\n"
                    + "<tr><th>Button</th><th>Directory/Name</th><th>Path</th><th>Delete</th></tr>");
            File pFolder = new File(path);
            Path pFolderPath = pFolder.toPath().getParent();
            if (new File(path).list().length == 0) {
                out.println("<tr>");
                for (int i = 0; i < 4; i++) {
                    out.println("<td>Empty</td>");
                }
                out.println("</tr>");
            } else {

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
                        out.println("<td>" + file.getFileName() + "</td>");

                        out.println("<td>" + new FileInfo().getInfo(file) + "</td>");

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
                                    + "</form>\n");
                        }
                        if (file.toString().endsWith(".zip")) {
                            out.println("<form method=\"get\"\n"
                                    + "action=\"unZipFolder\"\n"
                                    + "autocomplete=\"off\">"
                                    + "<input type=\"hidden\" name=\"toUnZip\" value=\"" + file + "\">"
                                    + "<input type=\"submit\" value=\"unZip\">"
                                    + "</form>\n</td>");
                        }

                    }

                } catch (IOException | DirectoryIteratorException x) {
                    System.err.println(x);
                }
            }

            out.println("<br>\n</table>\n<br>");

            if (check.check("E:\\" + String.valueOf(((User) session.getAttribute("user")).getFolder()), pFolderPath.toAbsolutePath().toString())) {
                out.println("<form method=\"post\"\n"
                        + "action=\"Folder\"\n"
                        + "autocomplete=\"off\">"
                        + "<input type=\"hidden\" name=\"goTo\" value=\"" + pFolderPath.toAbsolutePath() + "\">"
                        + "<input type=\"submit\" value=\"Back\">"
                        + "</form>");
            }
            out.println(""
                    + "<script type=\"text/javascript\" src=\"https://code.jquery.com/jquery-2.1.1.min.js\"></script>\n"
                    + "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js\"></script>"
                    + "</body>");
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
