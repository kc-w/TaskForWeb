package Servlet;

import Bean.User;
import Jdbc.factory.DaoFactory;
import Jdbc.interfaces.taskDao_interface;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "checkNewVersionServlet")
public class CheckNewVersionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        try {
            taskDao_interface userDao= DaoFactory.getUsersDao();
            int version_code = userDao.checkVersionCode();
            out.write(String.valueOf(version_code));
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }
}
