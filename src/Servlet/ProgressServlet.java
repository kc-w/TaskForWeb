package Servlet;

import Bean.Task;
import Bean.TaskAndUser;
import Bean.User;
import Jdbc.factory.DaoFactory;
import Jdbc.interfaces.taskDao_interface;
import Tool.ConstValue;
import Tool.GetDate;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



        HttpSession session=request.getSession();
        User user = (User)session.getAttribute("user");


        taskDao_interface userDao=null;

        int id = Integer.valueOf(request.getParameter("id"));
        String httpHtml=request.getParameter("html");

        String message = "进度发布完成";
        try{

            userDao = DaoFactory.getUsersDao();

            TaskAndUser task = userDao.selectTask(id);

            String html = task.getTask().getContent();

            String data="</br><p style=\"border:1px solid #000000;\">汇报人:"+user.getName()+"&nbsp;&nbsp;&nbsp;汇报日期:"+GetDate.time()+"</p>";

            userDao.upadteTask(id,html+data+httpHtml);


        } catch (Exception e) {
            e.printStackTrace();
            message = "进度发布失败";
        } finally {
            try {
                if (userDao!=null){
                    userDao.closeDB();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.getWriter().write(message);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }
}
