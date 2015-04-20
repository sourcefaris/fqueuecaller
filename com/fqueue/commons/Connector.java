package com.fqueue.commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;

public class Connector {
	private static SQLExecutor se = new SQLExecutor();
	private static Connector instance;
	public  static String SERVER_NAME;
	private File file=null;
//	private static String localPathCs = System.getProperty("local.path.cs");
//	private static String localPathTeller = System.getProperty("local.path.teller");
	
	static public Connector getInstance(){
		readQueueProp();
		if (instance==null){
			instance = new Connector();
		}
		return instance;
	}
	
	public static Connection getConnection() throws SQLException{
		Connection conn = null;
		String username = "root";
		String password = "";
		try{
		readQueueProp();
		Class.forName("org.gjt.mm.mysql.Driver");
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://"+SERVER_NAME+":3306/queue", username, password);
		}
		catch(SQLException sq){
			throw sq;
		}
		catch(Exception e){
			throw new SQLException(e.toString());
		}
		return conn;
	}
	
	public void closeConnection(Connection conn) throws SQLException{
		conn.close();
	}
	
	public boolean testConnection(){
		Connection conn = null;
		try {
			readQueueProp();
			conn = new Connector().getConnection();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (conn !=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}    
	}
	
	private static void readQueueProp(){
		BufferedReader in =null;
		String line = null;
		File file =null;
//		file = new File(localPathTeller); 
//		if(!file.exists())
//			file = new File(localPathCs);
			
		try {
//			in = new BufferedReader(new FileReader(file));
//			line = in.readLine();
			line = "127.0.0.1";
//			in.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
	  if (line!=null)
		SERVER_NAME =line;
	}
}
