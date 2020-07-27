package Jdbc.factory;


import Jdbc.interfaces.taskDao_interface;
import Jdbc.service.taskDaoService;

//工厂类
public class DaoFactory {
	public static taskDao_interface getUsersDao() throws Exception{
		return new taskDaoService();
	}
}

