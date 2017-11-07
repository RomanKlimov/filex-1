/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.folder_adapters;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
@WebServlet(name = "unZipFolder", urlPatterns = {"/unZipFolder"})
public class unZipFolder extends HttpServlet {

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
        String nameZip = request.getParameter("toUnZip");
        File file = new File(nameZip);

        String destinationPath = file.toPath().getParent().toString();

        String feedBack = unPack(destinationPath + File.separator + file.getName().replace(".zip", "") + File.separator, nameZip);
        request.setAttribute("goTo", destinationPath);
        request.setAttribute("message", feedBack);
        getServletContext().getRequestDispatcher("/message.jsp").forward(
                request, response);
    }
    private File destFile;
    public final int BUFFER = 1024;

    public String unPack(String destinationDirectory, String nameZip) {

        File sourceZip = new File(nameZip);

        try (ZipFile zFile = new ZipFile(sourceZip);) {
            File unzipDir = new File(destinationDirectory);

            // открытие архива для распаковки
            Enumeration<? extends ZipEntry> zipFileEntries = zFile.entries();
            while (zipFileEntries.hasMoreElements()) {

                //извлечение текущей записи из архива
                ZipEntry entry = zipFileEntries.nextElement();
                String entryName = entry.getName();
                System.out.println("Extracting: " + entry);
                destFile = new File(unzipDir, entryName);

                // определение каталога
                File destinationParent = destFile.getParentFile();

                // создание структуры каталогов
                destinationParent.mkdirs();

                // распаковывание записи если она не каталог
                if (!entry.isDirectory()) {
                    writeFile(zFile, entry);
                }
            }
        } catch (IOException e) {
            return e.getMessage().toString();
        }
        return "ZIP file unzipped!";
    }

    private void writeFile(ZipFile zFile, ZipEntry entry) {
        int currentByte;
        byte[] data = new byte[BUFFER];

        try (BufferedInputStream bis = new BufferedInputStream(zFile.getInputStream(entry));
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);) {

            // запись файла на диск
            while ((currentByte = bis.read(data, 0, BUFFER)) > 0) {
                bos.write(data, 0, currentByte);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
