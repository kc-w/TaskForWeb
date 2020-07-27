package Servlet;

import Bean.User;
import Tool.GetDate;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FileServlet")
public class FileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session=request.getSession();
        User user = (User)session.getAttribute("user");



        String message = "";
        try{
            DiskFileItemFactory dff = new DiskFileItemFactory();
            ServletFileUpload sfu = new ServletFileUpload(dff);
            List<FileItem> items = sfu.parseRequest(request);
            for(FileItem item:items){
                if(item.isFormField()){
                    //普通键值对
                    String fieldName = item.getFieldName();
                    String fieldValue = item.toString();
                } else {// 获取上传字段
                    // 更改文件名为唯一的
                    String filename = item.getName();
                    if (filename != null) {
                        String time=GetDate.time().replace("-","").replace(" ","").replace(":","");
                        filename =  user.getName()+ time+"." + FilenameUtils.getExtension(filename);
                    }
                    // 生成存储路径
                    String storeDirectory = getServletContext().getRealPath("/file");
                    File file = new File(storeDirectory);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    // 处理文件的上传
                    item.write(new File(storeDirectory , filename));

                    String filePath = "/file/"+ filename;
                    message = "http://14.29.169.203:8080/TaskForWeb"+filePath;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "上传图片失败";
        } finally {
            System.out.println("返回的文件路径:"+message);
            response.getWriter().write(message);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
