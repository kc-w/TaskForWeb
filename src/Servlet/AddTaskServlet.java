package Servlet;

import Bean.Task;
import Bean.User;
import GeTui.AppPush;
import Jdbc.factory.DaoFactory;
import Jdbc.interfaces.taskDao_interface;
import Tool.GetDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AddTaskServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String> cids=new ArrayList<String>();
        AppPush appPush = new AppPush();

        //接收请求发来的json任务对象
        BufferedReader reader = request.getReader();
        String jsonObject= reader.readLine();

        //将json字符串对象转换为java对象
        ObjectMapper mapper = new ObjectMapper();
        Task task = mapper.readValue(jsonObject, Task.class);
        //得到session中存储的用户信息
        HttpSession session=request.getSession();
        User user = (User)session.getAttribute("user");


        task.setStart_id(user.getId());
        task.setStart_time(GetDate.time());
        task.setState("待批准");

        response.setContentType("text/json;charset=utf-8");
        PrintWriter out = response.getWriter();


        taskDao_interface userDao=null;
        try {
            userDao = DaoFactory.getUsersDao();
            Boolean mark = userDao.addTask(task);

            if (mark){
                out.write("创建任务完成!");

                if (user.getPermission()!=4){
                    cids = userDao.selectCids(user);

                    appPush.push(cids,"有新事件待批准",task.getName());

                    System.out.println(cids.toString());
                }



            }else {
                out.write("创建任务失败");
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (userDao!=null){
                    userDao.closeDB();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
