/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.folder_adapters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
@WebServlet(name = "zipFolder", urlPatterns = {"/zipFolder"})
public class zipFolder extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String zipFileName;
    public final int BUFFER = 1024;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dirName = request.getParameter("toZip");
        File dir = new File(dirName);
        String goTo = dir.getParent() + File.separator;
        
        if (request.getParameter("zipType") != null) {
            int max = 9000000;
            int min = 1000000;
            zipFileName = dir.getAbsolutePath() + File.separator + "BackUp" + String.valueOf(new Random().nextInt(max) + min) + ".zip";
            goTo = dir.getAbsolutePath();
        } else {
            zipFileName = dir.getParent() + File.separator + dir.getName() + ".zip";
        }
        
        request.removeAttribute("zipType");
        if (!dir.exists() || !dir.isDirectory()) {
            throw new FileNotFoundException(dir + " not found");
        }
        File[] files = dir.listFiles();
        ArrayList<String> listNamesFilesToZip = new ArrayList<>();
        ArrayList<String> listFilesToZip = new ArrayList<>();

        for (File file : files) {
            if (!file.isDirectory()) {
                listNamesFilesToZip.add(file.getName());
                listFilesToZip.add(file.getPath());
            }
        }

        String[] temp = {};
        String[] filesToZip = listFilesToZip.toArray(temp);
        String[] namesFilesToZip = listNamesFilesToZip.toArray(temp);

        // архивирование
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
                ZipOutputStream zos = new ZipOutputStream(fos)) {

            byte[] buffer = new byte[BUFFER];
            // метод сжатия
            zos.setLevel(Deflater.DEFAULT_COMPRESSION);

            for (int i = 0; i < filesToZip.length; i++) {
                zos.putNextEntry(new ZipEntry(namesFilesToZip[i]));
                try (FileInputStream fis = new FileInputStream(filesToZip[i])) {
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                    request.setAttribute("message", "Folder " + dir.getAbsolutePath() + " zipped!");
                } catch (FileNotFoundException e) {
                    request.setAttribute("message", "Файл не найден " + e);
                }
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", "Некорректный аргумент " + e);
        } catch (IOException e) {
            request.setAttribute("message", "Ошибка доступа " + e);
        }
        request.setAttribute("goTo", goTo);
        getServletContext().getRequestDispatcher("/message.jsp").forward(
                request, response);
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
