package servlet.file_adapters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet(name = "UploadDownloadFileServlet", urlPatterns = {"/UploadDownloadFileServlet"})
@MultipartConfig()
public class UploadDownloadFileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ServletFileUpload uploader = null;

    @Override
    public void init() throws ServletException {
        DiskFileItemFactory fileFactory = new DiskFileItemFactory();
        File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
        fileFactory.setRepository(filesDir);
        this.uploader = new ServletFileUpload(fileFactory);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("fileName");
        String path = request.getServletContext().getAttribute("FILES_DIR") + File.separator + fileName;

        String toDownload = request.getParameter("toDownload");

        if (toDownload != null && !toDownload.equals("")) {
            path = toDownload;
        }
        File file = new File(path);
        fileName = file.getName();
        System.out.println(file.getAbsolutePath());
        if (fileName == null || fileName.equals("")) {
            throw new ServletException("File Name can't be null or empty");
        }

        if (!file.exists()) {
            throw new ServletException("File doesn't exists on server.");
        }

        System.out.println("File location on server::" + file.getAbsolutePath());
        ServletContext ctx = getServletContext();
        try (InputStream fis = new FileInputStream(file)) {
            String mimeType = ctx.getMimeType(file.getAbsolutePath());
            response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            
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
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    //method for upload files
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("Content type is not multipart/form-data");
        }
        String path = request.getParameter("toUpload");
        String savePath = "E:\\upload";
        if (path != null) {
            savePath = path;
        }

        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);

            fileName = new File(fileName).getName();
            try {
                System.out.println(new File(fileName).canWrite());
                System.out.println(new File(fileName).exists());
                part.write(savePath + File.separator + fileName);
            } catch (java.io.IOException ex ) {

            }
        }

        request.setAttribute("message", "Upload has been done successfully!");
        request.setAttribute("goTo", savePath);
        getServletContext().getRequestDispatcher("/message.jsp").forward(
                request, response);
    }
}
