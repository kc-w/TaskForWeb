package Servlet;

import Bean.Task;
import Bean.TaskAndUser;
import Bean.User;
import GeTui.AppPush;
import Jdbc.factory.DaoFactory;
import Jdbc.interfaces.taskDao_interface;
import Tool.GetDate;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.TabableView;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "UpStaetServlet")
public class UpStaetServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        int task_id = Integer.valueOf(request.getParameter("task_id"));
        String flag = request.getParameter("flag");

        //得到session中存储的用户信息
        HttpSession session=request.getSession();
        User user = (User)session.getAttribute("user");


        response.setContentType("text/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        Boolean mark=false;
        String message = "";


        List<String> cids=new ArrayList<String>();
        AppPush appPush = new AppPush();


        try {
            taskDao_interface userDao = DaoFactory.getUsersDao();

            if ("agree".equals(flag)){
                mark = userDao.updateTaskState1(user,task_id, GetDate.time());
                if (mark){
                    message="通过批准完成!";

                    TaskAndUser taskAndUser = userDao.selectTask(task_id);

                    String start_name = taskAndUser.getUser().getName();

                    String json1 = taskAndUser.getTask().getExecute_people();
                    String json2 = taskAndUser.getTask().getAssist_people();

                    if (json1.indexOf("全体员工")!=-1 || json2.indexOf("全体员工")!=-1){
                        appPush.push(cids,"有新事件待确认!",taskAndUser.getTask().getName());
                    }else {

                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, String> execute_map=mapper.readValue(json1, Map.class);
                        Map<String, String> assist_map=mapper.readValue(json2, Map.class);

                        Map<String, String> combineResultMap = new HashMap<String, String>();
                        combineResultMap.putAll(execute_map);
                        combineResultMap.putAll(assist_map);

                        Set<String> s1 = combineResultMap.keySet();//获取KEY集合
                        List<String> arrayList = new ArrayList<String>();
                        for (String str : s1) {
                            arrayList.add(str);
                        }

                        if (arrayList.size()!=0){
                            cids = userDao.selectCids(arrayList);
                            appPush.push(cids,"有新事件待确认!",taskAndUser.getTask().getName());
                        }

                        arrayList.clear();
                        arrayList.add(start_name);

                        cids = userDao.selectCids(arrayList);
                        appPush.push(cids,"事件已批准!",taskAndUser.getTask().getName());

                    }




                }else {
                    message="通过批准失败!";
                }

            }

            if ("noagree".equals(flag)){
                mark = userDao.updateTaskState3(user,task_id, GetDate.time());
                if (mark){
                    message="取消批准完成!";
                }else {
                    message="取消批准失败!";
                }
            }

            if ("finish".equals(flag)){
                mark = userDao.updateTaskState2(task_id);
                if (mark){
                    message="确认完成!";
                }else {
                    message="确认失败!";
                }
            }
            if ("delete".equals(flag)){
                mark = userDao.deleteTask(task_id);
                if (mark){
                    message="删除完成!";
                }else {
                    message="删除失败!";
                }
            }

            if ("receive".equals(flag)){

                TaskAndUser taskAndUser = userDao.selectTask(task_id);
                String json1 = taskAndUser.getTask().getExecute_people();
                String json2 = taskAndUser.getTask().getAssist_people();

                ObjectMapper mapper = new ObjectMapper();

                Map<String, String> execute_map=mapper.readValue(json1, Map.class);
                Set<String> s1 = execute_map.keySet();//获取KEY集合
                for (String str : s1) {
                    if (user.getName().equals(str)){
                        execute_map.put(user.getName(),"已确认");
                    }
                }
                json1 = mapper.writeValueAsString(execute_map);

                if(!"".equals(json2)){
                    Map<String, String> assist_map=mapper.readValue(json2, Map.class);

                    Set<String> s2 = assist_map.keySet();//获取KEY集合
                    for (String str : s2) {
                        if (user.getName().equals(str)){
                            assist_map.put(user.getName(),"已确认");
                        }
                    }
                    json2 = mapper.writeValueAsString(assist_map);
                }



                mark = userDao.updateTaskState4(task_id,json1,json2);
                if (mark){
                    message="确认完成!";
                }else {
                    message="确认失败!";
                }
            }




            out.write("{\"message\":"+message+"}");

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);

    }
}
