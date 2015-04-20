/*
 * Created on 28/10/2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fqueue.commons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fqueue.domain.Branchservicedetail;
import com.fqueue.domain.BranchservicedetailId;
import com.fqueue.domain.Process;
import com.fqueue.domain.ProcessId;
import com.mysql.jdbc.Connection;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataProvider {
	private static DataProvider instance;
	private final int Sum_Paper_Limit =16;
	private final int Sum_Paper_Empty =0;
	private final String Status_Paper_Limit="paper limit";
	private final String Status_Paper_Full ="paper full";
	private final String Status_Paper_Empty="paper empty";
	
	static public DataProvider getInstance(){
		if (instance==null){
			instance = new DataProvider();
		}
		return instance;
	}
	
	public Branchservicedetail getService(String myIP) throws Exception{
		Branchservicedetail bsd = new Branchservicedetail();
		BranchservicedetailId bsdID = new BranchservicedetailId();
		Connection conn = null;
		SQLExecutor se = null;
		ResultSet rs = null;
		String[] a = {};
		String query = "";
		
		try {
			se = new SQLExecutor();
			conn = new Connector().getConnection();
			query = "SELECT * FROM branchservicedetail WHERE ip_address='" + myIP + "'";
			rs = se.selectPreparedStatement(conn, query, a);
			if (rs.next())
			{
				bsdID.setBranchCode(rs.getString("branch_code"));
				bsdID.setCounterNo(rs.getInt("counter_no"));
				bsdID.setServiceName(rs.getString("service_name"));
				bsd.setId(bsdID);
				bsd.setIpAddress(rs.getString("ip_address"));
				return bsd;
			}				
			else
				throw new Exception("No service for ip=" + myIP);
		}
		catch (SQLException e) {
			if (conn == null){
				e.printStackTrace();
				throw new Exception("No connection to Queueing Server");				
			}
			else{
				e.printStackTrace();
				throw e;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (rs != null) 
					rs.close();
				if (conn !=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}				
		}
	}

	public String getBranchcode(){
		Connection conn = null;
		SQLExecutor se = null;
		String query = "";
		ResultSet rs = null;		
		String[] param={};
		String branchcode = null ;
		try {
			se = new SQLExecutor();
			conn = new Connector().getConnection();
			
			query="select VALUE from parameter where CODE = 'branchcode'";
			rs= se.selectPreparedStatement(conn, query,param );
			while(rs.next()){
				branchcode=rs.getString("VALUE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} 
		finally {
			try {
				if (conn !=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}				
		}
		return branchcode;
	}
	
	
	
	public List getData(String service, String status, String ip, String qryType,String no) throws Exception{
		Process detail;
		Connection conn = null;
		List lstResult = new ArrayList();
		ResultSet rs = null;
		String[] a = {};
		SQLExecutor se = null;
		String query = "";

		try {
			se = new SQLExecutor();
			conn = new Connector().getConnection();
			query = "SELECT p.*, CASE TIMEDIFF(time_finish,time_call) WHEN '00:00:00' THEN '-' ELSE TIMEDIFF(time_finish,time_call) END AS duration " +
					"FROM process p, branchservicedetail b " +
					"WHERE p.service IN (" + service + ") AND " +
						"CURDATE() = DATE_FORMAT(p.time,GET_FORMAT(DATE,'ISO')) AND " +
						"p.branch_code = b.branch_code AND " +   
						"p.counter_no = b.counter_no ";
			if (!status.equals(""))
				query += " AND p.status IN (" + status + ")";			
			if (!ip.equals(""))
				query += " AND b.ip_address='" + ip + "'";
			if (!no.equals(""))
				query += " AND p.NO='" + no + "'"; 
			if (qryType.equals("UNION"))
				query += " UNION " +
				"SELECT p.*, CASE TIMEDIFF(time_finish,time_call) WHEN '00:00:00' THEN '-' ELSE TIMEDIFF(time_finish,time_call) END AS duration " + 
				"FROM process p " + 
				"WHERE p.service IN (" + service + ") AND " + 
					"CURDATE() = DATE_FORMAT(p.time,GET_FORMAT(DATE,'ISO')) " ; 

			if (qryType.equals("CustomerExist"))
				query = "SELECT p.*, CASE TIMEDIFF(time_finish,time_call) WHEN '00:00:00' THEN '-' ELSE TIMEDIFF(time_finish,time_call) END AS duration " + 
						"FROM process p " + 
						"WHERE p.service IN (" + service + ") AND " +
							"CURDATE() = DATE_FORMAT(p.time,GET_FORMAT(DATE,'ISO')) AND " + 
							"status='-' ";

			query += "ORDER BY STATUS, NO";; 			
			//System.out.println("getData : \n"+query);			
			rs = se.selectPreparedStatement(conn, query, a);
			while (rs.next()){
				detail = new Process(null, null, null, null, Sum_Paper_Limit);
				detail.setId(new ProcessId(rs.getInt("no"),rs.getTimestamp("time")));				
				detail.setTimeCall(rs.getTimestamp("time_call"));
				detail.setTimeLastCall(rs.getTimestamp("time_last_call"));
				detail.setTimeFinish(rs.getTimestamp("time_finish"));
				detail.setTimeDuration(rs.getString("duration"));
				detail.setService(rs.getString("service"));
				detail.setCounterNo(new Integer(rs.getString("counter_no")==null?"0":rs.getString("counter_no")));				
				detail.setStatus(rs.getString("status"));
				detail.setCallBy(rs.getString("call_by"));
				detail.setTimeStartQ(rs.getTimestamp("time"));
				detail.setBranchCode(rs.getString("branch_code"));
				lstResult.add(detail);
			}
		}
		catch (SQLException e) {
			if (conn == null){
				e.printStackTrace();
				throw new Exception("No connection to Queueing Server");				
			}
			else{
				e.printStackTrace();
				throw e;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (rs != null) 
					rs.close();
				if (conn !=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}				
		}
		return lstResult;
	}
	
	public void workStationInsert(Branchservicedetail bsd) throws Exception
	{
		Connection conn = null;
		SQLExecutor se = null;
		String query = "";
		ResultSet rs = null;		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			se = new SQLExecutor();
			conn = new Connector().getConnection();
			
			query = "SELECT * FROM workstation " + 
			        "WHERE branch_code=? AND " + 
					      "counter_no=? AND " + 
						  "service_name=? AND " + 
						  "period=? AND " + 
						  "open_date=?";
			String[] param1 = {bsd.getId().getBranchCode() ,
					bsd.getId().getCounterNo() + "",
					bsd.getId().getServiceName(),
					TimeCategory.getCategory(new Date()),
					timeFormat.format(new Date())};		
			rs = se.selectPreparedStatement(conn, query, param1);
			
			if (!rs.next()){				
				query = "INSERT INTO workstation VALUES(?,?,?,?,?,?)";
				String[] a = {bsd.getId().getBranchCode() ,
						bsd.getId().getCounterNo() + "",
						bsd.getId().getServiceName(),
						TimeCategory.getCategory(new Date()),
						"1",
						timeFormat.format(new Date())};
				se.addPreparedStatement(conn,query,a);}
		}
		catch (SQLException e) {
			if (conn == null){
				e.printStackTrace();
				throw new Exception("No connection to Queueing Server");				
			}
			else{
				e.printStackTrace();
				throw e;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (conn !=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}				
		}
	}
	
	public Timestamp getTimeQmatic(){
		Connection conn = null;
		SQLExecutor se = null;
		String query = "";
		ResultSet rs = null;		
		String[] param={};
		Timestamp time = null ;
		try {
			se = new SQLExecutor();
			conn = new Connector().getConnection();
			
			query="select now() as time ";
			rs= se.selectPreparedStatement(conn, query,param );
			while(rs.next()){
				time=rs.getTimestamp("time");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} 
		finally {
			try {
				if (conn !=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}				
		}
		return time;
	}
	
	public String cekStatusPaper(){
		Connection conn=null;
		SQLExecutor se =null;
		String query = "";
		ResultSet rs = null;
		String valKertas = null;
		try {
			se = new SQLExecutor();
			conn = new Connector().getConnection();
			
			query="select value from parameter where code=? ";
			String[] param = {"paper"};
			rs= se.selectPreparedStatement(conn, query,param );
			if(rs.next()){
				valKertas=rs.getString("value");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} 
		finally {
			try {
				if (conn !=null)
					conn.close();	  
			} catch (SQLException e) {
				e.printStackTrace();
			}				
		}
		if(new Integer(valKertas).intValue()<=Sum_Paper_Limit && new Integer(valKertas).intValue()>Sum_Paper_Empty){
			return Status_Paper_Limit;
		 }else if(new Integer(valKertas).intValue()<=Sum_Paper_Empty){
		 	return Status_Paper_Empty;
		 }else
		 	return Status_Paper_Full;
	}
	
	public HashMap getLastQueue(String ip) throws Exception{
		Connection conn = null;
		ResultSet rs=null;
		String query="SELECT (SELECT value FROM parameter WHERE code='branchcode') AS 'branch',TIME_TO_SEC(TIMEDIFF(time_call,time)) 'sec',time,no,if((SELECT service_name FROM branchservicedetail WHERE ip_address=?)='qcs','CS','Teller') AS 'service',counter_no,time_call,TIMEDIFF(time_call,time) as 'wait_time'  " +
				"FROM process " +
				"WHERE time_call=(SELECT MAX(time_call) FROM process p,branchservicedetail b WHERE CURDATE() = DATE_FORMAT(process.`TIME`,GET_FORMAT(DATE,'ISO')) AND  b.ip_address=? and  p.counter_no=b.counter_no and status='dilayani');";
		SQLExecutor se = null;
		String[] args=new String[]{ip,ip};
		HashMap map=null;
		DateFormat dateFormat=new SimpleDateFormat("dd MMMM yyyy");
		DateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
		map=new HashMap();
		map.put("result",Boolean.FALSE);
		try {
			se = new SQLExecutor();
			conn = new Connector().getConnection();
			rs = se.selectPreparedStatement(conn, query, args);
			if(rs.next()){
				map.put("sec",new Integer(rs.getInt("sec")));
				map.put("date",dateFormat.format(rs.getTimestamp("time")));
				map.put("queue_no",rs.getString("no"));
				map.put("service",rs.getString("service"));
				map.put("counter_no",rs.getString("counter_no"));
				map.put("time",timeFormat.format(rs.getTimestamp("time")));
				map.put("start_time",timeFormat.format(rs.getTimestamp("time_call")));
				map.put("wait_time",rs.getString("wait_time"));
				map.put("branch",rs.getString("branch"));
				map.put("result",Boolean.TRUE);
			}
		} catch (SQLException e) {
			if (conn == null){
				e.printStackTrace();
				throw new Exception("No connection to Queueing Server");				
			}
			else{
				e.printStackTrace();
				throw e;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (rs != null) 
					rs.close();
				if (conn !=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}				
		}
		return map;	
	}
	//mengambil no antrian yg terakhir selesai
	public HashMap getPrintQueue(String ip,int no,Date time) throws Exception{
		Connection conn = null;
		ResultSet rs=null;
		String query="SELECT (SELECT value FROM parameter WHERE code='branchcode') AS 'branch',TIME_TO_SEC(TIMEDIFF(time_call,time)) 'sec',time,no,if((SELECT service_name FROM branchservicedetail WHERE ip_address=?)='qcs','CS','Teller') AS 'service',counter_no,time_call,TIMEDIFF(time_call,time) as 'wait_time'  FROM process WHERE no=? AND time=? AND status NOT IN('-')";
		SQLExecutor se = null;
//		SimpleDateFormat pDateFormat=new DateFormat("");
		String[] args=new String[]{ip,String.valueOf(no),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time)};
//		System.out.println(new SimpleDateFormat("yyyy-mm-dd HH:MM:ss").format(time)+" "+String.valueOf(no));
		HashMap map=null;
		DateFormat dateFormat=new SimpleDateFormat("dd MMMM yyyy");
		DateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
		map=new HashMap();
		map.put("result",Boolean.FALSE);
		try {
			se = new SQLExecutor();
			conn = new Connector().getConnection();



			rs=se.selectPreparedStatement(conn,query,args);

			if(rs.next()){
				map.put("sec",new Integer(rs.getInt("sec")));
				map.put("date",dateFormat.format(rs.getTimestamp("time")));
				map.put("queue_no",rs.getString("no"));
				map.put("service",rs.getString("service"));
				map.put("counter_no",rs.getString("counter_no"));
				map.put("time",timeFormat.format(rs.getTimestamp("time")));
				map.put("start_time",timeFormat.format(rs.getTimestamp("time_call")));
				map.put("wait_time",rs.getString("wait_time"));
				map.put("branch",rs.getString("branch"));
				map.put("result",Boolean.TRUE);
			}
		} catch (SQLException e) {
			if (conn == null){
				e.printStackTrace();
				throw new Exception("No connection to Queueing Server");				
			}
			else{
				e.printStackTrace();
				throw e;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (rs != null) 
					rs.close();
				if (conn !=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}				
		}
		
		
		return map;	
	}
	
		
}