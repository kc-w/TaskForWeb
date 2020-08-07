package Jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

//数据库连接器
public class DBconnection {
    private static final String Driver="com.mysql.jdbc.Driver";


//    private static final String DBurl="jdbc:mysql://localhost:3306/taskmanagement";
//    private static final String user="root";
//    private static final String password="999999999852";


    private static final String DBurl="jdbc:mysql://10.10.0.221:3306/taskmanagement";
    private static final String user="root";
    private static final String password="23252699@Db#1";


    private Connection conn=null;

    public DBconnection() throws Exception{
        try{
            Class.forName(Driver);
            conn=DriverManager.getConnection(DBurl,user,password);
        }catch(Exception e){
            close();
            throw e;
        }
    }

    public Connection getdb(){
        return conn;
    }

    public void close()throws Exception{
        if(this.conn!=null){
            try{
                this.conn.close();
            }catch(Exception e){
                throw e;
            }
        }
    }

}
