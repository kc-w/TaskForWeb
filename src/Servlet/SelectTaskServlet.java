package Servlet;

import Bean.Task;
import Bean.TaskAndUser;
import Bean.User;
import Jdbc.factory.DaoFactory;
import Jdbc.interfaces.taskDao_interface;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SelectTaskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws  IOException {

        HttpSession session=request.getSession();
        User user = (User)session.getAttribute("user");


        String task_id = request.getParameter("task_id");
        String task_state = request.getParameter("task_state");
        String json = "";


        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("text/json;charset=utf-8");


        taskDao_interface userDao = null;

        //判断查询任务列表还是单个任务
        if (task_state==null || "".equals(task_state)){

            try {
                userDao = DaoFactory.getUsersDao();
                TaskAndUser taskAndUser = userDao.selectTask(Integer.valueOf(task_id));
                json = mapper.writeValueAsString(taskAndUser);
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

        }else {

            try {
                userDao = DaoFactory.getUsersDao();
                ArrayList<TaskAndUser> taskAndUsers = userDao.selectTaskState(task_state,user);
                json = mapper.writeValueAsString(taskAndUsers);
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

        PrintWriter out = response.getWriter();
        out.write(json);
        out.close();


    }
}
