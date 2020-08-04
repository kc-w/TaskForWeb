package Jdbc.service;


import Bean.Task;
import Bean.TaskAndUser;
import Bean.User;
import Jdbc.DBconnection;
import Jdbc.impl.taskDaoImpl;
import Jdbc.interfaces.taskDao_interface;

import java.util.ArrayList;
import java.util.List;

//业务操作,实现数据库开关
public class taskDaoService implements taskDao_interface {
	
	private DBconnection conn=null;
	private taskDao_interface dao=null;
	
	public taskDaoService() throws Exception {
		this.conn=new DBconnection();
		this.dao=new taskDaoImpl(this.conn.getdb());
	}

	public void closeDB() throws Exception {
		dao.closeDB();
		conn.close();
	}

	@Override
	public int checkVersionCode() throws Exception {
		return dao.checkVersionCode();
	}

	@Override
	public ArrayList<String> allUserName() throws Exception {
		return dao.allUserName();
	}

	@Override
	public boolean upadteTask(int id, String html) throws Exception {
		return dao.upadteTask(id,html);
	}


	//用户登陆验证
	public User login(String number,String password) throws Exception {

		return dao.login(number,password);
	}

	@Override
	public Boolean updateUserState(User user, String pw) throws Exception {
		return dao.updateUserState(user,pw);
	}

	//添加任务
	public boolean addTask(Task task) throws Exception {
		return dao.addTask(task);
	}

	//根据任务状态查询任务(需要表连接查询)
	public ArrayList<TaskAndUser> selectTaskState(String task_state, User user) throws Exception {
		return dao.selectTaskState(task_state,user);
	}

	//根据任务id查询任务(需要表连接查询)
	public TaskAndUser selectTask(int task_id) throws Exception {
		return dao.selectTask(task_id);
	}

	//批准执行,修改任务状态
	public Boolean updateTaskState1(User user, int task_id,String time) throws Exception {
		return dao.updateTaskState1(user,task_id,time);
	}

	//完成执行,修改任务状态
	public Boolean updateTaskState2(int task_id) throws Exception {
		return dao.updateTaskState2(task_id);
	}

	//批准执行,修改任务状态
	public Boolean updateTaskState3(User user, int task_id,String time) throws Exception {
		return dao.updateTaskState3(user,task_id,time);
	}

	@Override
	public Boolean updateTaskState4(int task_id, String json1, String json2) throws Exception {
		return dao.updateTaskState4(task_id,json1,json2);
	}

	@Override
	public ArrayList<TaskAndUser> SearchServlet(String findString, User user) throws Exception {
		return dao.SearchServlet(findString,user);
	}

	@Override
	public Boolean deleteTask(int task_id) throws Exception {
		return dao.deleteTask(task_id);
	}

	@Override
	public ArrayList<String> selectCids(List<String> names) throws Exception {
		return dao.selectCids(names);
	}

	@Override
	public ArrayList<String> selectCids(User user) throws Exception {
		return dao.selectCids(user);
	}


}
