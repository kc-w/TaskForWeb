package Servlet;

import Jdbc.factory.DaoFactory;
import Jdbc.interfaces.taskDao_interface;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SelectUserNameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        taskDao_interface userDao=null;
        try {
            userDao= DaoFactory.getUsersDao();
            ArrayList<String> namelist = userDao.allUserName();


            String json = mapper.writeValueAsString(namelist);
            out.write(json);
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
