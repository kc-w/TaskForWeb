package Jdbc.interfaces;

import Bean.Task;
import Bean.TaskAndUser;
import Bean.User;

import java.util.ArrayList;
import java.util.List;


//功能接口
public interface taskDao_interface {


	public void closeDB() throws Exception ;


	//版本更新检测
	public int checkVersionCode()throws Exception;

	//人数标签查询
	public ArrayList<String> allUserName()throws Exception;

	//更新事件内容
	public boolean upadteTask(int id,String html) throws Exception;

	//用户登录验证
	public User login(String number, String password)throws Exception;

	//修改密码
	public Boolean updateUserState(User user, String pw) throws Exception;

	//添加任务
	public boolean addTask(Task task) throws Exception;

	//根据任务状态查询任务
	public ArrayList<TaskAndUser> selectTaskState(String task_state, User user) throws Exception;

	//根据任务id查询任务
	public TaskAndUser selectTask(int task_id) throws Exception;

	//批准执行,修改任务状态
	public Boolean updateTaskState1(User user, int task_id, String time) throws Exception;

	//批准执行,修改任务状态
	public Boolean updateTaskState2(int task_id) throws Exception;

	//批准执行,修改任务状态
	public Boolean updateTaskState3(User user, int task_id, String time) throws Exception;

	//批准执行,修改任务状态
	public Boolean updateTaskState4(int task_id, String json1, String json2) throws Exception;

	//查询任务
	public ArrayList<TaskAndUser> SearchServlet(String findString, User user) throws Exception;

	//删除任务
	public Boolean deleteTask(int task_id) throws Exception;

	//查找cid多人查询
	public ArrayList<String> selectCids(List<String> names) throws Exception;


	//查找cid单人查询
	public ArrayList<String> selectCids(User user) throws Exception;
	

}
