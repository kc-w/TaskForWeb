package Servlet;

import Bean.User;
import Jdbc.factory.DaoFactory;
import Jdbc.interfaces.taskDao_interface;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ChangeUserServlet")
public class ChangeUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String,String> map=new HashMap<String, String>();
        HttpSession session=request.getSession();
        User user = (User)session.getAttribute("user");

        String message = "";
        try{
            DiskFileItemFactory dff = new DiskFileItemFactory();
            ServletFileUpload sfu = new ServletFileUpload(dff);
            List<FileItem> items = sfu.parseRequest(request);
            for(FileItem item:items){
                if(item.isFormField()){//判断是表单类型还是文件类型
                    //普通键值对
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    map.put(fieldName,fieldValue);
                }
            }

            taskDao_interface userDao = DaoFactory.getUsersDao();

            if (map.get("cid") == null){
                User user1 = userDao.login(user.getNumber(),map.get("oldPW"));

                if (user1==null){
                    message = "原密码错误!";
                }else {
                    Boolean flag = userDao.updateUserState(user,map.get("newPW"));
                    if (flag){
                        message="密码修改成功!";
                    }else {
                        message="修改失败!";
                    }
                }

            }else {
                Boolean flag = userDao.updateUserState(user,map.get("cid"));
                if (flag){
                    message="cid更新完成!";
                }else {
                    message="cid更新失败!";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "发生错误!";
        } finally {
            response.getWriter().write(message);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }
}
