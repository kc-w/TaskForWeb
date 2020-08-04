package Servlet;

import Bean.Task;
import Bean.TaskAndUser;
import Bean.User;
import Jdbc.factory.DaoFactory;
import Jdbc.interfaces.taskDao_interface;
import Tool.GetDate;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        HttpSession session=request.getSession();
        User user = (User)session.getAttribute("user");

        //接收请求发来的json任务对象
        BufferedReader reader = request.getReader();
        String sql= reader.readLine();



        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("text/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        taskDao_interface userDao=null;
        try {
            userDao = DaoFactory.getUsersDao();
            ArrayList<TaskAndUser> taskAndUsers = userDao.SearchServlet(sql,user);
            String json = mapper.writeValueAsString(taskAndUsers);

            out.write(json);

        } catch (Exception e) {
            e.printStackTrace();
            out.close();
        }finally {
            try {
                if (userDao!=null){
                    userDao.closeDB();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        out.close();

    }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
