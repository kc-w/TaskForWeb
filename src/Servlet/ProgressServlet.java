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


        String html=request.getParameter("html");
        String progresshtml=request.getParameter("progresshtml");

        String httpHtml=request.getParameter("httpHtml");

        String flag=request.getParameter("flag");

        if("add".equals(flag)){

            String message = "进度发布完成";
            try{

                userDao = DaoFactory.getUsersDao();

                TaskAndUser task = userDao.selectTask(id);

                String oldhtml = task.getTask().getContent();

                String data="<p style=\"border:1px solid #000000;\">汇报人:"+user.getName()+"&nbsp;&nbsp;&nbsp;时间:"+GetDate.time()+"</p>";

                userDao.upadteTask(id,oldhtml+data+"<p>"+httpHtml+"</p>"+"</br>");


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
        }else {

            String message = "修改完成";
            try{


                userDao = DaoFactory.getUsersDao();

                TaskAndUser task = userDao.selectTask(id);

                String oldhtml = task.getTask().getContent();

                String data=html+"</p><p>"+progresshtml;

                String newdata=html+"</p><p>"+httpHtml;

                String newhtml=oldhtml.replace(data,newdata);


                userDao.upadteTask(id,newhtml);


            } catch (Exception e) {
                e.printStackTrace();
                message = "修改失败";
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



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }
}
