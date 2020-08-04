package Jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Bean.Task;
import Bean.TaskAndUser;
import Bean.User;
import Jdbc.interfaces.taskDao_interface;
import Tool.GetDate;

//数据操作,实现数据库操作
public class taskDaoImpl implements taskDao_interface {
	
	private Connection conn=null;
	private PreparedStatement pstmt=null;
	private ResultSet rs=null;
	
	public taskDaoImpl(Connection conn) {
		this.conn=conn;
	}


	@Override
	public void closeDB() throws Exception {
		if(pstmt!=null) {
			pstmt.close();
		}
	}

	@Override
	public int checkVersionCode() throws Exception {

		int version_code =0;
		String sql = "select * from version order by id desc limit 1";

		try {
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();

			while(rs.next()) {
				version_code=rs.getInt("version_code");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}
		return version_code;
	}

	@Override
	public ArrayList<String> allUserName() throws Exception {

		ArrayList<String> names=new ArrayList<String>();
		String sql = "select name from user order by id desc";

		try {
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();

			while(rs.next()) {
				String user_name=rs.getString("name");
				names.add(user_name);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}
		return names;

	}

	//用户登陆验证
	public User login(String number,String password)throws Exception{

		User user=null;
		String sql="select * from user where number=?";


		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, number);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("password").equals(password)) {
					user = new User();
					user.setId(rs.getInt("id"));
					user.setCid(rs.getString("cid"));
					user.setName(rs.getString("name"));
					user.setNumber(rs.getString("number"));
					user.setPassword(rs.getString("password"));
					user.setDepartment(rs.getString("department"));
					user.setPermission(rs.getInt("permission"));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}
		return user;
	}


	//修改用户状态.密码
	public Boolean updateUserState(User user, String pw) throws Exception {
		boolean mark=true;
		//设置手动提交事务模式
		conn.setAutoCommit(false);

		String sql="";

		if (pw.length()>14){
			sql="update user set cid = ? where id = ?";

		}else {
			sql="update user set password = ? where id = ?";
		}

		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, pw);
			pstmt.setInt(2,user.getId());
			pstmt.executeUpdate();

			//手动提交事务
			conn.commit();
		}catch(Exception e){
			mark=false;
			//如有异常则回滚
			conn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}

		return mark;
	}
	


	
	//添加事件
	public boolean addTask(Task task) throws Exception {
		boolean mark=true;
		//设置手动提交事务模式
		conn.setAutoCommit(false);
		
		String sql="insert into task" +
				"(name,content,start_id,start_time,preset_time,execute_people,assist_people,agree_id,agree_time,finish_time,state)" +
				"values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, task.getName());
			pstmt.setString(2, task.getContent());
			pstmt.setInt(3,task.getStart_id());
			pstmt.setString(4, task.getStart_time());
			pstmt.setString(5,task.getPreset_time());
			pstmt.setString(6,task.getExecute_people());
			pstmt.setString(7,task.getAssist_people());
			pstmt.setInt(8,task.getAgree_id());
			pstmt.setString(9,task.getAgree_time());
			pstmt.setString(10,task.getFinish_time());
			pstmt.setString(11,task.getState());


			pstmt.executeUpdate();
			
			//手动提交事务
			conn.commit();
		}catch(Exception e){
			mark=false;
			//如有异常则回滚
			conn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}
		
		return mark;
	}



	//添加评论
	public boolean upadteTask(int id,String html) throws Exception {
		boolean mark=true;
		//设置手动提交事务模式
		conn.setAutoCommit(false);

		String sql="update task set content = ? where id = ?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, html);
			pstmt.setInt(2, id);

			pstmt.executeUpdate();

			//手动提交事务
			conn.commit();
		}catch(Exception e){
			mark=false;
			//如有异常则回滚
			conn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}

		return mark;
	}

	//根据任务状态查询任务
	public ArrayList<TaskAndUser> selectTaskState(String task_state,User user) throws Exception {

		ArrayList<TaskAndUser> taskAndUsers = new ArrayList<TaskAndUser>();
		String sql="";




		if (user.getPermission()<=3){
			if("进行中".equals(task_state)){
				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where table1.a12='进行中' and (table1.b5 like ? or table1.a7 like ? or table1.a7 like ? or table1.a8 like ? or table1.a8 like ?) and (select TIMESTAMPDIFF(DAY, current_timestamp,table1.a6 ))>=0 order by table1.a5 desc";
			}
			if("待批准".equals(task_state)){
				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where table1.a12='待批准' and (table1.b5 like ? or table1.a7 like ? or table1.a7 like ? or table1.a8 like ? or table1.a8 like ?)  order by table1.a5 desc";

			}
			if("已完成".equals(task_state) ){
				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where table1.a12='已完成' and (table1.b5 like ? or table1.a7 like ? or table1.a7 like ? or table1.a8 like ? or table1.a8 like ?) order by table1.a5 desc";

			}
			if("延期".equals(task_state)){
				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where table1.a12='进行中' and (table1.b5 like ? or table1.a7 like ? or table1.a7 like ? or table1.a8 like ? or table1.a8 like ?) and (select TIMESTAMPDIFF(DAY, current_timestamp,table1.a6 ))<0 order by table1.a5 desc";
			}

			if("myThing".equals(task_state)){
				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where (table1.a7 like ? or table1.a7 like ? or table1.a8 like ? or table1.a8 like ? or table1.a4= ? or table1.a9 =?) order by table1.a5 desc";
			}
		}else {
			if("进行中".equals(task_state)){

				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where table1.a12='进行中' and (select TIMESTAMPDIFF(DAY, current_timestamp,table1.a6 ))>=0 order by table1.a5 desc";

			}
			if("待批准".equals(task_state)){

				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where table1.a12='待批准' order by table1.a5 desc";


			}
			if("已完成".equals(task_state) ){

				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where table1.a12 = '已完成' order by table1.a5 desc";

			}
			if("延期".equals(task_state)){

				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where table1.a12='进行中' and (select TIMESTAMPDIFF(DAY, current_timestamp,table1.a6 ))<0 order by table1.a5 desc";

			}
			if("myThing".equals(task_state)){

				sql="select table1.* from " +
						"(select "+
						"task.id as a1,task.name as a2,task.content as a3,task.start_id as a4,task.start_time as a5,task.preset_time as a6,"+
						"task.execute_people as a7,task.assist_people as a8,task.agree_id as a9,task.agree_time as a10,task.finish_time as a11,task.state as a12,"+
						"user.id as b1,user.name as b2,user.number as b3,user.password as b4,user.department as b5,user.permission as b6,"+
						"user1.id as c1,user1.name as c2,user1.number as c3,user1.password as c4,user1.department as c5,user1.permission as c6 "+
						"from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id)  as table1 "+
						"where table1.a7 like ? or table1.a7 like ? or table1.a8 like ? or table1.a8 like ? or table1.a4= ? or table1.a9 =?  order by table1.a5 desc";

			}
		}

		try {
			pstmt=conn.prepareStatement(sql);



			if (user.getPermission()<=3){



				if ("myThing".equals(task_state)){
					pstmt.setString(1, "%"+user.getName()+"%");
					pstmt.setString(2, "%"+"全体员工"+"%");
					pstmt.setString(3, "%"+user.getName()+"%");
					pstmt.setString(4, "%"+"全体员工"+"%");
					pstmt.setInt(5, user.getId());
					pstmt.setInt(6, user.getId());
				}else{
					pstmt.setString(1, user.getDepartment());
					pstmt.setString(2, "%"+user.getName()+"%");
					pstmt.setString(3, "%"+"全体员工"+"%");
					pstmt.setString(4, "%"+user.getName()+"%");
					pstmt.setString(5, "%"+"全体员工"+"%");
				}

			}else {
				if ("myThing".equals(task_state)){
					pstmt.setString(1, "%"+user.getName()+"%");
					pstmt.setString(2, "%"+"全体员工"+"%");
					pstmt.setString(3, "%"+user.getName()+"%");
					pstmt.setString(4, "%"+"全体员工"+"%");
					pstmt.setInt(5, user.getId());
					pstmt.setInt(6, user.getId());
				}
			}


			rs=pstmt.executeQuery();

			while(rs.next()) {
				TaskAndUser taskAndUser = new TaskAndUser();


				taskAndUser.getTask().setId(rs.getInt(1));
				taskAndUser.getTask().setName(rs.getString(2));
				taskAndUser.getTask().setContent(rs.getString(3));
				taskAndUser.getTask().setStart_id(rs.getInt(4));
				taskAndUser.getTask().setStart_time(rs.getString(5));
				taskAndUser.getTask().setPreset_time(rs.getString(6));
				taskAndUser.getTask().setExecute_people(rs.getString(7));
				taskAndUser.getTask().setAssist_people(rs.getString(8));
				taskAndUser.getTask().setAgree_id(rs.getInt(9));
				taskAndUser.getTask().setAgree_time(rs.getString(10));
				taskAndUser.getTask().setFinish_time(rs.getString(11));
				taskAndUser.getTask().setState(rs.getString(12));
				taskAndUser.getUser().setId(rs.getInt(13));
				taskAndUser.getUser().setName(rs.getString(14));
				taskAndUser.getUser().setNumber(rs.getString(15));
				taskAndUser.getUser().setPassword(rs.getString(16));
				taskAndUser.getUser().setDepartment(rs.getString(17));
				taskAndUser.getUser().setPermission(rs.getInt(18));

				taskAndUser.getUser1().setId(rs.getInt(19));
				taskAndUser.getUser1().setName(rs.getString(20));
				taskAndUser.getUser1().setNumber(rs.getString(21));
				taskAndUser.getUser1().setPassword(rs.getString(22));
				taskAndUser.getUser1().setDepartment(rs.getString(23));
				taskAndUser.getUser1().setPermission(rs.getInt(24));

				taskAndUsers.add(taskAndUser);

//				task.setId(rs.getInt("task_id"));
//				task.setName(rs.getString("task_name"));
//				task.setTask_department(rs.getString("task_department"));
//				task.setContent(rs.getString("task_content"));
//				task.setStart_people(rs.getInt("start_people"));
//				task.setStart_people_name(rs.getString("start_people_name"));
//				task.setAssist_people(rs.getString("assist_people"));
//				task.setAgree_people(rs.getInt("agree_people"));
//				task.setAgree_people_name(rs.getString("agree_people_name"));
//				task.setTask_start_time(rs.getString("task_start_time"));
//				task.setTask_execute_time(rs.getString("task_execute_time"));
//				task.setPredict_time(rs.getString("predict_time"));
//				task.setTask_state(rs.getString("task_state"));

			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}

		return taskAndUsers;
	}

	//根据任务id查询任务
	public TaskAndUser selectTask(int task_id) throws Exception {

		TaskAndUser taskAndUser = new TaskAndUser();

		String sql="select task.*,user.*,user1.* from task,user,user as user1 where task.start_id = user.id and task.agree_id = user1.id and task.id = ?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, task_id);
			rs=pstmt.executeQuery();

			while(rs.next()) {
				taskAndUser.getTask().setId(rs.getInt(1));
				taskAndUser.getTask().setName(rs.getString(2));
				taskAndUser.getTask().setContent(rs.getString(3));
				taskAndUser.getTask().setStart_id(rs.getInt(4));
				taskAndUser.getTask().setStart_time(rs.getString(5));
				taskAndUser.getTask().setPreset_time(rs.getString(6));
				taskAndUser.getTask().setExecute_people(rs.getString(7));
				taskAndUser.getTask().setAssist_people(rs.getString(8));
				taskAndUser.getTask().setAgree_id(rs.getInt(9));
				taskAndUser.getTask().setAgree_time(rs.getString(10));
				taskAndUser.getTask().setFinish_time(rs.getString(11));
				taskAndUser.getTask().setState(rs.getString(12));
				taskAndUser.getUser().setId(rs.getInt(13));
				taskAndUser.getUser().setCid(rs.getString(14));
				taskAndUser.getUser().setName(rs.getString(15));
				taskAndUser.getUser().setNumber(rs.getString(16));
//				taskAndUser.getUser().setPassword(rs.getString(17));
				taskAndUser.getUser().setDepartment(rs.getString(18));
				taskAndUser.getUser().setPermission(rs.getInt(19));
				taskAndUser.getUser1().setId(rs.getInt(20));
				taskAndUser.getUser1().setCid(rs.getString(21));
				taskAndUser.getUser1().setName(rs.getString(22));
				taskAndUser.getUser1().setNumber(rs.getString(23));
//				taskAndUser.getUser1().setPassword(rs.getString(24));
				taskAndUser.getUser1().setDepartment(rs.getString(25));
				taskAndUser.getUser1().setPermission(rs.getInt(26));

			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}
		return taskAndUser;
	}

	//修改任务状态,批准执行
	public Boolean updateTaskState1(User user, int task_id,String time) throws Exception {
		boolean mark=true;
		//设置手动提交事务模式
		conn.setAutoCommit(false);

		String sql="update task set agree_id = ?,agree_time= ?,state= ? where id = ?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, user.getId());
			pstmt.setString(2,time);
			pstmt.setString(3,"进行中");
			pstmt.setInt(4,task_id);
			pstmt.executeUpdate();

			//手动提交事务
			conn.commit();
		}catch(Exception e){
			mark=false;
			//如有异常则回滚
			conn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}

		return mark;
	}

	//修改任务状态,完成执行
	public Boolean updateTaskState2(int task_id) throws Exception {
		boolean mark=true;
		//设置手动提交事务模式
		conn.setAutoCommit(false);

		String sql="update task set state= ?,finish_time= ? where id = ?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, "已完成");
			pstmt.setString(2, GetDate.time());
			pstmt.setInt(3,task_id);
			pstmt.executeUpdate();

			//手动提交事务
			conn.commit();
		}catch(Exception e){
			mark=false;
			//如有异常则回滚
			conn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}

		return mark;
	}

	//修改任务状态,取消批准
	public Boolean updateTaskState3(User user, int task_id,String time) throws Exception {
		boolean mark=true;
		//设置手动提交事务模式
		conn.setAutoCommit(false);

		String sql="update task set agree_id = ?,agree_time= ?,state= ? where id = ?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, user.getId());
			pstmt.setString(2,time);
			pstmt.setString(3,"未批准");
			pstmt.setInt(4,task_id);
			pstmt.executeUpdate();

			//手动提交事务
			conn.commit();
		}catch(Exception e){
			mark=false;
			//如有异常则回滚
			conn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}

		return mark;
	}

	//确认收到任务
	public Boolean updateTaskState4(int task_id, String json1, String json2) throws Exception {
		boolean mark=true;
		//设置手动提交事务模式
		conn.setAutoCommit(false);

		String sql="update task set execute_people = ?,assist_people= ? where id = ?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, json1);
			pstmt.setString(2,json2);
			pstmt.setInt(3,task_id);
			pstmt.executeUpdate();

			//手动提交事务
			conn.commit();
		}catch(Exception e){
			mark=false;
			//如有异常则回滚
			conn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}

		return mark;
	}


	@Override
	public Boolean deleteTask(int task_id) throws Exception {
		boolean mark=true;
		//设置手动提交事务模式
		conn.setAutoCommit(false);

		String sql="delete from task where id = ?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1,task_id);
			pstmt.executeUpdate();

			//手动提交事务
			conn.commit();
		}catch(Exception e){
			mark=false;
			//如有异常则回滚
			conn.rollback();
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}

		return mark;
	}



	@Override
	public ArrayList<TaskAndUser> SearchServlet(String findSql, User user) throws Exception {

		String sql="";

		ArrayList<TaskAndUser> taskAndUsers = new ArrayList<TaskAndUser>();

		if (user.getPermission()<=3){
			sql=findSql+" and (table1.b5 like ? or table1.a7 like ? or table1.a7 like ? or table1.a8 like ? or table1.a8 like ?)";

		}else {

			sql=findSql;
		}




		try {
			pstmt=conn.prepareStatement(sql);

			if (user.getPermission()<=3){
				pstmt.setString(1, "%"+user.getDepartment()+"%");
				pstmt.setString(2, "%"+user.getName()+"%");
				pstmt.setString(3, "%"+"全体员工"+"%");
				pstmt.setString(4, "%"+user.getName()+"%");
				pstmt.setString(5, "%"+"全体员工"+"%");
			}

			rs=pstmt.executeQuery();


			while(rs.next()) {
				TaskAndUser taskAndUser = new TaskAndUser();

				taskAndUser.getTask().setId(rs.getInt(1));
				taskAndUser.getTask().setName(rs.getString(2));
				taskAndUser.getTask().setContent(rs.getString(3));
				taskAndUser.getTask().setStart_id(rs.getInt(4));
				taskAndUser.getTask().setStart_time(rs.getString(5));
				taskAndUser.getTask().setPreset_time(rs.getString(6));
				taskAndUser.getTask().setExecute_people(rs.getString(7));
				taskAndUser.getTask().setAssist_people(rs.getString(8));
				taskAndUser.getTask().setAgree_id(rs.getInt(9));
				taskAndUser.getTask().setAgree_time(rs.getString(10));
				taskAndUser.getTask().setFinish_time(rs.getString(11));
				taskAndUser.getTask().setState(rs.getString(12));
				taskAndUser.getUser().setId(rs.getInt(13));
				taskAndUser.getUser().setName(rs.getString(14));
				taskAndUser.getUser().setNumber(rs.getString(15));
//				taskAndUser.getUser().setPassword(rs.getString(16));
				taskAndUser.getUser().setDepartment(rs.getString(17));
				taskAndUser.getUser().setPermission(rs.getInt(18));
				taskAndUser.getUser1().setId(rs.getInt(19));
				taskAndUser.getUser1().setName(rs.getString(20));
				taskAndUser.getUser1().setNumber(rs.getString(21));
//				taskAndUser.getUser1().setPassword(rs.getString(22));
				taskAndUser.getUser1().setDepartment(rs.getString(23));
				taskAndUser.getUser1().setPermission(rs.getInt(24));

				taskAndUsers.add(taskAndUser);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}

		return taskAndUsers;


	}


	@Override
	public ArrayList<String> selectCids(List<String> names) throws Exception {
		String sql = "select cid from user where name = ?";

		ArrayList<String> cids = new ArrayList<String>();

		for (String name : names){

			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, name);
			rs=pstmt.executeQuery();

			while(rs.next()) {

				cids.add(rs.getString(1));

			}

		}

		return cids;

	}

	@Override
	public ArrayList<String> selectCids(User user) throws Exception {
		String sql = "select cid from user where (department like ? and permission > ?) or permission = 4 ";

		ArrayList<String> cids = new ArrayList<String>();


		pstmt=conn.prepareStatement(sql);
		pstmt.setString(1, "%"+user.getDepartment().substring(0,2)+"%");
		pstmt.setInt(2,user.getPermission());
		rs=pstmt.executeQuery();

		System.out.println(pstmt.toString());

		while(rs.next()) {

			if (rs.getString(1)==null || "".equals(rs.getString(1))){
				continue;
			}else {
				cids.add(rs.getString(1));
			}


		}


		return cids;
	}


}
