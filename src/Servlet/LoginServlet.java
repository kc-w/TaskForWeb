package Servlet;


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

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        HttpSession session=request.getSession();

        String number = request.getParameter("number");
        String password = request.getParameter("password");




        response.setContentType("text/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        ObjectMapper mapper = new ObjectMapper();

        try {
            taskDao_interface userDao= DaoFactory.getUsersDao();
            User user = userDao.login(number,password);


            if(user==null){
                out.write("{\"id\":-1}");
            }else {
                session.setAttribute("user",user);
                System.out.println("用户登录:"+user.getName());
                out.write(mapper.writeValueAsString(user));
            }

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
