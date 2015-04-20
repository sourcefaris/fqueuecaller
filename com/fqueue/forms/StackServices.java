/*
 * Created on Oct 13, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fqueue.forms;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;

import com.fqueue.commons.Connector;
import com.fqueue.commons.DataProvider;
import com.fqueue.commons.WaitDialog;
import com.fqueue.domain.Process;
import com.fqueue.domain.ProcessId;
import com.jeta.forms.components.panel.FormPanel;
import com.rubean.beantable.BeanProvider;
import com.rubean.beantable.BeanTable;
import com.rubean.beantable.BeanTableException;
import com.rubean.beantable.CalcColumnHandler;
import com.rubean.beantable.Column;
import com.rubean.beantable.ErrorEvent;
import com.rubean.beantable.ErrorListener;
import com.rubean.rcms.msf.OpenException;
import com.rubean.rcms.ui.RubeanButton;
import com.rubean.rcms.ui.RubeanComboBox;
import com.rubean.rcms.ui.RubeanDialog;
import com.rubean.rcms.ui.RubeanInfoPanel;
import com.rubean.rcms.ui.RubeanLabel;
import com.rubean.rcms.ui.RubeanOptionPane;
import com.rubean.rcms.ui.RubeanPanel;
import com.rubean.rcms.ui.RubeanTableView;

public class StackServices  extends RubeanPanel{
	public String screenVersion = "$Revision: 1.15 $";
	private String msg = "";
	
	protected int ttlFinish;
	protected int ttlServed;
	private List listFinish;
	private List listServed;
	private String noQueue="";
	private RefreshTimerSingleton timerx;
	private RubeanInfoPanel panelInfoPaper=null;
	private String autorefreshTime =System.getProperty("time.autorefresh");
	private static String warningFont="blue";
	private static String emptyFont="red";
	private String userName="";
	private String serviceName="";
	private String callBy="";
	private String status="";
	private Timestamp timeStartService=null;
	private Timestamp timeEndService=null;
	private Timestamp timeStartQ=null;
	private String branchCode=null;
	private Integer counter=null;
	private RubeanLabel lblMsgInfo;
	
	private FormPanel reportPanel=null;
	private RubeanLabel lblDate=null;
	private RubeanLabel lblNo=null;
	private RubeanLabel lblService=null;
	private RubeanLabel lblCounter=null;
	private RubeanLabel lblTime=null;
	private RubeanLabel lblStartTime=null;
	private RubeanLabel lblWaitTime=null;
	private RubeanLabel lblBranch=null;
	// 8 menit
	
	private String limitWaitTime=System.getProperty("time.waiting");
	private int POPUP_WAIT_TIME;
	
	public StackServices(){
		try {
			qinit();
			activate();
			initStartLock();
	
		} catch (Exception e) {
			e.printStackTrace();
			lblMsgInfo.setText(e.getMessage());
		}
		
	}
	
	private void showDialog() throws Exception{
		
		RubeanDialog dialog=new RubeanDialog();
		dialog.setModal(true);
		String services=DataProvider.getInstance().getService(myIP).getId().getServiceName();
		dialog.setTitle("Waktu Tunggu > "+limitWaitTime+" menit");
		dialog.getContentPane().add(reportPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	private void showReport(){
		try {
			HashMap map=DataProvider.getInstance().getLastQueue(InetAddress.getLocalHost().getHostAddress());
			if(((Boolean)map.get("result")).booleanValue()){
				String services=DataProvider.getInstance().getService(myIP).getId().getServiceName();

				if(((Integer)map.get("sec")).intValue()>POPUP_WAIT_TIME){
					lblDate.setText((String) map.get("date"));
					lblNo.setText((String) map.get("queue_no"));
					lblService.setText((String) map.get("service"));
					lblCounter.setText((String) map.get("counter_no"));
					lblTime.setText((String) map.get("time"));
					lblStartTime.setText((String) map.get("start_time"));
					lblWaitTime.setText((String) map.get("wait_time"));
					lblBranch.setText((String) map.get("branch"));
					showDialog();
				}
			}
			
		} catch (Exception e1) {
			lblMsgInfo.setText(e1.getMessage());
			e1.printStackTrace();
		}	
	}
	
	private void initReportDialog(){
		reportPanel = new FormPanel("com/fqueue/res/Report.jfrm");
		lblDate=(RubeanLabel) reportPanel.getComponentByName("lblDate");
		lblNo=(RubeanLabel) reportPanel.getComponentByName("lblNo");
		lblService=(RubeanLabel) reportPanel.getComponentByName("lblService");
		lblCounter=(RubeanLabel) reportPanel.getComponentByName("lblCounter");
		lblTime=(RubeanLabel) reportPanel.getComponentByName("lblTime");
		lblStartTime=(RubeanLabel) reportPanel.getComponentByName("lblStartTime");
		lblWaitTime=(RubeanLabel) reportPanel.getComponentByName("lblWaitTime");
		lblBranch=(RubeanLabel) reportPanel.getComponentByName("lblBranch");
		
	}
	
	private synchronized void qinit(){
		String formName = this.getClass().getName();
		panel = new FormPanel("com/fqueue/res/StackServices.jfrm");
		JScrollPane scrollpane = new JScrollPane(panel);
		panelInfoPaper = (RubeanInfoPanel)panel.getComponentByName("panelInfoPaper");
		panelInfoPaper.setVisible(false);
		lblMsgInfo=(RubeanLabel) panel.getComponentByName("lblMsgInfo");
		cmdTransfer  = (RubeanButton) panel.getComponentByName("cmdTransfer");
		cmbKlinik = (RubeanComboBox) panel.getComponentByName("cmbKlinik");
		try {
		    beanTable = new BeanTable();
		    myIP = InetAddress.getLocalHost().getHostAddress();
			service = DefineService();
			
			if ( service.equals("error") ){
			    msg = "Tidak ada service untuk IP "+myIP;
			    enable = false;
			}
			else if (service.equals("'exception'")){
			    msg = "terdapat kesalahan setting service pada server";
			    enable = false;
			}
			else{
			    msg = "Koneksi telah siap";
			    enable = true;
			}
	
			lblMsgInfo.setText(msg);
			
			if ( !service.equals("error") ){
				beanTable.setBeanClassName(Process.class.getName());
				beanTable.setProvider(beanProvider1);
				beanTable.setCalcColumnHandler(calcColumnHandler1);
				beanTable.setCalculatedColumns(new Column[]{
						new Column(ProcessId.class, "id.no", 0, 0, false, false),
						new Column(ProcessId.class, "id.time", 0, 0, false, false),
						new Column(Process.class, "timeCall", 0, 0, false, false),
						new Column(Process.class, "timeFinish", 0, 0, false, false),
						new Column(Process.class, "timeDuration", 0, 0, false, false),
						new Column(Process.class, "status", 0, 0, false, false),
						new Column(Process.class, "counter_no", 0, 0, false, false)});
				
				beanTable.addErrorListener(new ErrorListener(){
					public void error(ErrorEvent arg0) {
						Throwable t = arg0.getException();
						t.printStackTrace();
						while (t.getCause()!=null) {
							t = t.getCause();
						}
						lblMsgInfo.setText(t.getMessage());
						
					}
				});
			}			
		} catch (Exception e1) {
			lblMsgInfo.setText(e1.getLocalizedMessage());
			e1.printStackTrace();
		}
		
		try {
			RubeanTableView tblStackServices =(RubeanTableView) panel.getComponentByName("tblStackServices");
			tblStackServices.setBeanTable(beanTable);
			tblStackServices.setColumnNames(new String[] { "id.no", "service", "id.time", "timeCall", "timeFinish", "timeDuration", "status", "counter_no"});
			
			tblStackServices.setCaptions(new String[] { "Nomor Antrian", "Tujuan", "Mulai Antri", "Mulai Dilayani","Selesai Dilayani", "Waktu Pelayanan", "Status","Dilayani Oleh"});
				
			tblStackServices.setFormatPatterns(new String[] { null, null, new String("hh:mm:ss"), new String("hh:mm:ss"), new String("hh:mm:ss"), null, null, null});

			
			tblStackServices.getTableHeader().setDraggedColumn(null);
			tblStackServices.getTableHeader().setResizingAllowed(true);

	        tblStackServices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblStackServices.setVisible(true);
			RubeanButton cmdCall = (RubeanButton) panel.getComponentByName("cmdCall");
			cmdCall.setEnabled(enable);
			cmdCall.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					callCustThread thread = new callCustThread();
					dialog = new WaitDialog();
					dialog.setLocationRelativeTo(null);
					dialog.setInfoWait("Memanggil Pasien");
					thread.start();
					dialog.show();
				}
			});

			RubeanButton cmdRefresh  = (RubeanButton) panel.getComponentByName("cmdRefresh");
			cmdRefresh.setEnabled(enable);
			cmdRefresh.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
				    activate();
				}
			});

			
			RubeanButton cmdFinish  = (RubeanButton) panel.getComponentByName("cmdFinish");
			cmdFinish.setEnabled(enable);
			cmdFinish.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					endCustThread thread = new endCustThread();
					dialog = new WaitDialog();
					dialog.setLocationRelativeTo(null);
					dialog.setInfoWait("Mengakhiri Pemanggilan Pasien");
					thread.start();
					dialog.show();
				}
			});
			
			cmdTransfer.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					sendCustThread thread = new sendCustThread();
					dialog = new WaitDialog();
					dialog.setLocationRelativeTo(null);
					dialog.setInfoWait("Mengalihkan Pasien");
					thread.start();
					dialog.show();
				}
			});
			cmdTransfer.setEnabled(enable);
			
			this.add(panel);
			String services=DataProvider.getInstance().getService(myIP).getId().getServiceName();
			
			POPUP_WAIT_TIME=Integer.parseInt(limitWaitTime)*60;
			initReportDialog();
			
			
			try {
				initTableTimer();
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			
			
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void initTableTimer() {
		int delay= Integer.parseInt(autorefreshTime)*60*1000; // get time from vm arg ,autoresfresh supposed minutes and convert to ms 
		timerx =  RefreshTimerSingleton.getInstance(delay , new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	activate();
		    }    
		});
		timerx.start();
	}
	


	
	public class sendCustThread extends Thread {
		public void run() {
			boolean refresh = false;
			Socket s;		
			try {
				lblMsgInfo.setText("");
				
				if (DataProvider.getInstance().getData(service,"'dilayani'",myIP,"CustomerFinish","").size()!=0){
				String myIP = InetAddress.getLocalHost().getHostAddress();
				s = new Socket(ServerName,portNumber);
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter out = new PrintWriter (new OutputStreamWriter (s.getOutputStream()));
				
				String serviceSend = null;
				if("Klinik Anak".equals(cmbKlinik.getSelectedItem().toString()))
					serviceSend = "klinikAnak;";
				else if("Klinik Bedah".equals(cmbKlinik.getSelectedItem().toString()))
					serviceSend = "klinikBedah;";
				else if("Klinik Obsgyn".equals(cmbKlinik.getSelectedItem().toString()))
					serviceSend = "klinikObsgyn;";
				else if("Klinik Penyakit Dalam".equals(cmbKlinik.getSelectedItem().toString()))
					serviceSend = "klinikPenyakitDalam;";
				else if("Klinik Syaraf".equals(cmbKlinik.getSelectedItem().toString()))
					serviceSend = "klinikSyaraf;";
				else if("Klinik THT dan Mata".equals(cmbKlinik.getSelectedItem().toString()))
					serviceSend = "klinikTHTMata;";
				
				out.println(serviceSend + myIP);
				out.flush();
				out.println ("BYE");
				out.flush();
				String str="";
				while (true){
					str = in.readLine();
					if (str == null) 
						break;
					else if (str.equals("klinikAnakOK"))
						refresh=true;
					else if (str.equals("klinikBedahOK"))
						refresh=true;
					else if (str.equals("klinikObsgynOK"))
						refresh=true;
					else if (str.equals("klinikPenyakitDalamOK"))
						refresh=true;
					else if (str.equals("klinikSyarafOK"))
						refresh=true;
					else if (str.equals("klinikTHTMataOK"))
						refresh=true;
				}
				s.close();
				if (refresh){
					activate();
					cmdTransfer.setEnabled(false);
					lblMsgInfo.setText("Mengalihkan Pasien");
				}
			 }else
				lblMsgInfo.setText("Belum ada Pasien yang sedang dilayani");
			} catch (UnknownHostException e) {
				lblMsgInfo.setText(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				lblMsgInfo.setText(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				lblMsgInfo.setText(e.getMessage());
				e.printStackTrace();
			} finally {	
				dialog.dispose();
			}
		}
	}
	
	/**
	 * 	ask server to update time finish and call the top queue index customer 
	 */
	public class endCustThread extends Thread {
		public void run() {
			boolean refresh = false;
			Socket s;		
			boolean finish=false;
			try {
				lblMsgInfo.setText("");				
				listServed = DataProvider.getInstance().getData(service,"'dilayani'",myIP,"CustomerFinish","");
				ttlServed = listServed.size();
				if (ttlServed!=0){
					Process detailServed = (Process)listServed.get(0);
					noQueue = new Integer(detailServed.getId().getNo()).toString();
					int i=RubeanOptionPane.showConfirmDialog(panel,"Selesai untuk antrian ?","Konfirmasi",RubeanOptionPane.YES_NO_OPTION);
					if (i==RubeanOptionPane.YES_OPTION){
						String myIP = InetAddress.getLocalHost().getHostAddress();
						s = new Socket(ServerName,portNumber);
						BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
						PrintWriter out = new PrintWriter (new OutputStreamWriter (s.getOutputStream()));
						out.println("Finish;" + myIP);
						out.flush();
						out.println ("BYE");
						out.flush();
						String str="";
						while (true){  
							str = in.readLine();
							if (str == null) 
								break;
							else if (str.equals("FinishOk")){
								refresh=true;
								break;
							}
						}			
						s.close();
						if (refresh)
							activate();
					}
		
					if (!noQueue.equals("")){
						try {
							listFinish = DataProvider.getInstance().getData(service,"'selesai'",myIP,"CustomerSelesai",noQueue);
							ttlFinish = listFinish.size();
							if(ttlFinish!=0){
								Process detailFinish = (Process)listFinish.get(0);
								setValueQueueInfo(detailFinish);
								finish=true;
							}
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					
				}
				else
					lblMsgInfo.setText("Belum ada Pasien yang sedang dilayani");
			} catch (UnknownHostException e) {
				lblMsgInfo.setText(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				lblMsgInfo.setText(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				lblMsgInfo.setText(e.getMessage());;
				e.printStackTrace();
			} finally {	
				dialog.dispose();
				if(finish){
					showReport();	
				}
			}
		}
	}
	
	public class updateQueueDisplayThread extends Thread{
		public void run(){
			boolean refresh = false;
			Socket s;		
				lblMsgInfo.setText("");
				if (RubeanOptionPane.OK_OPTION == RubeanOptionPane.showConfirmDialog(panel, "Update Kurs dan Suku Bunga","Update QueueDisplay", RubeanOptionPane.YES_NO_OPTION)){
				try{
					String myIP = InetAddress.getLocalHost().getHostAddress();
					s = new Socket(ServerName,portNumber);
					BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintWriter out = new PrintWriter (new OutputStreamWriter (s.getOutputStream()));				
					out.println("UpdateQueueDisplay;" + myIP);
					out.flush();
					out.println ("BYE");
					out.flush();
					String str="";
					while (true){
						str = in.readLine();
						if (str == null) 
							break;
						else if (str.equals("UpdateQueueDisplayOk"))
							refresh=true;
					}			
					s.close();
				} catch (UnknownHostException e) {
					lblMsgInfo.setText(e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					lblMsgInfo.setText(e.getMessage());
					e.printStackTrace();
				} catch (Exception e) {
					lblMsgInfo.setText(e.getMessage());
					e.printStackTrace();
				} finally {	
					dialog.dispose();
				}
			   }	
				else{
					dialog.dispose();
			}
		}
	}
	
	/**
	 *   ask server to call the top queue index customer
	 */
	public class callCustThread extends Thread {
		public void run() {
			Socket s;
			boolean refresh = false;
			String lastCustomer = "";
			boolean custExist = true;
		
			boolean finish=false;
			try {
				lblMsgInfo.setText("");
				cmdTransfer.setEnabled(true);
				
				if (DataProvider.getInstance().getData(service,"'-'","","CustomerExist","").size()==0)
					custExist = false;
			
				listServed =DataProvider.getInstance().getData(service,"'dilayani'",myIP,"CustomerFinish","");
				if (listServed.size()!=0)
				{				
					Process detailFinish = (Process)listServed.get(0);
					noQueue = new Integer(detailFinish.getId().getNo()).toString();
				    if (RubeanOptionPane.OK_OPTION == RubeanOptionPane.showConfirmDialog(panel, "Pasien telah selesai dilayani ?", "Queue Services", RubeanOptionPane.YES_NO_OPTION))
						lastCustomer = "finish";
					else
						lastCustomer = "noshow";
				}
				else
					lastCustomer = "finish";
				
				String  myIP = InetAddress.getLocalHost().getHostAddress();
				DataProvider.getInstance().workStationInsert(DataProvider.getInstance().getService(myIP));
				s = new Socket(ServerName,portNumber);
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter out = new PrintWriter (new OutputStreamWriter (s.getOutputStream()));
				out.println("Call;" + myIP + ";" + lastCustomer);
				out.flush();
				out.println ("BYE");
				out.flush();
				String str="";
				while (true){  
					str = in.readLine();
					if (str == null) 
						break;
					else if (str.equals("CallOk")){
						refresh=true;
						break;
					}else if (str.equals("CallNoQueue0")){
						custExist = false;
						refresh=true;
						break;
					}
				}		
				
				if (lastCustomer.equalsIgnoreCase("finish")){
					if(!noQueue.equals("")){
						try {
							listFinish = DataProvider.getInstance().getData(service,"'selesai'",myIP,"CustomerSelesai",noQueue);
							if(listFinish.size()!=0){
								Process detailFinish = (Process)listFinish.get(0);			
								setValueQueueInfo(detailFinish);
							}
						
							
						} catch (Exception e){
							e.printStackTrace();
						}	
					}
				}else if(lastCustomer.equalsIgnoreCase("noshow")){
					try {
						listFinish = DataProvider.getInstance().getData(service,"'noshow'",myIP,"CustomerSelesai",noQueue);
						if (listFinish.size()!=0){
							Process detailFinish = (Process)listFinish.get(0);
							setValueQueueInfo(detailFinish);
						}
						} catch (Exception e1) {
							e1.printStackTrace();
						}		
				} 
				
				s.close();
				if (refresh){
					activate();				
					if (custExist){
						lblMsgInfo.setText("Memanggil Customer");
						finish=true;
					}
					else 
						lblMsgInfo.setText("Tidak Ada Pasien");
					
					String stat =DataProvider.getInstance().cekStatusPaper();
					if(stat.equalsIgnoreCase("paper limit")){
						panelInfoPaper.setVisible(true);
						panelInfoPaper.setWarning("<html><b> <font color="+warningFont+">Kertas antrian akan habis,Segera lakukan pengisian kertas dan BSM wajib melakukan reset di Mesin Antrian(Ctrl-8 klik reset)</html> ");						
					}else if(stat.equalsIgnoreCase("paper empty")){
						panelInfoPaper.setVisible(true);
						panelInfoPaper.setError("<html><b> <font color="+emptyFont+">Kertas antrian habis,Segera lakukan pengisian kertas dan BSM wajib melakukan reset di Mesin Antrian(Ctrl-8 klik reset)</html>");
					}
				}				
			} catch (UnknownHostException e) {
//				setMessageInfoPanel(e.getMessage());
				lblMsgInfo.setText(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
//				setMessageInfoPanel(e.getMessage());
				lblMsgInfo.setText(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
//				setMessageInfoPanel(e.getMessage());
				lblMsgInfo.setText(e.getMessage());
				e.printStackTrace();
			} finally {	
				dialog.dispose();
//				showReport();
				if(finish){
					showReport();	
				}
				
				
			}
		}
	}	
	
	private void setValueQueueInfo(Process detailFinish){
		try {
			serviceName=detailFinish.getService();
			timeStartQ=(Timestamp)detailFinish.getTimeStartQ();
			timeStartService=(Timestamp)detailFinish.getTimeCall();
			timeEndService=(Timestamp)detailFinish.getTimeFinish();
			callBy=detailFinish.getCallBy();
			counter=detailFinish.getCounterNo();
			status=detailFinish.getStatus();
			branchCode=detailFinish.getBranchCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private CalcColumnHandler calcColumnHandler1 = new CalcColumnHandler(){
		
		public Object calcValue(BeanTable arg0, String colname, int rowposition) throws BeanTableException {
			if (rowposition>results.size()-1) return null;
			Process row = (Process) beanTable.getRow(rowposition);
			if("id.no".equals(colname))
				return setLeadingZero(row.getId().getNo()+"");
			if("timeCall".equals(colname))
				if(row.getStatus().equals("-"))
					return "";
				else 
					return row.getTimeCall();
			if("id.time".equals(colname))
				if(row.getStatus().equals("-"))
					return "";
				else 
					return row.getId().getTime();
			if("timeFinish".equals(colname))
				if(row.getStatus().equals("selesai"))
					return row.getTimeFinish();
				else 
					return "";
			else if("timeDuration".equals(colname))
				if(row.getStatus().equals("selesai"))
					return row.getTimeDuration();
				else 
					return "";			
			else if("status".equals(colname))
				if(row.getStatus().equals("-"))
					return "";
				else 
					return row.getStatus();
			else if("counter_no".equals(colname))
				if(!row.getCounterNo().equals(new Integer(0)))
					return "counter " + row.getCounterNo();		
				
			return null;			
		}
		
		public void setValue(BeanTable arg0, String arg1, Object arg2) throws BeanTableException {
		}
		
	};
	
	BeanProvider beanProvider1 = new BeanProvider() {
		
		public Collection provideData() throws BeanTableException {
			try {
				results = getData("");
				if (results.size()>0){
					List firstColumnResult = new ArrayList();
					for (int i=0; i< results.size(); i++){
						firstColumnResult.add(results.get(i));
					}					
					return firstColumnResult;
				} 
			}catch(Exception e){
				lblMsgInfo.setText(e.getMessage());
				e.printStackTrace();
			} 
			return results;
		}
	};
	
	public void activate() {
		try {
			refresh();
		} catch (BeanTableException e) {			
			e.printStackTrace();
		}
	}
	
	public void open() throws OpenException {
		try {
			refresh();
		} catch (BeanTableException e) {
			e.printStackTrace();
		}	
	}
	
	public void refresh() throws BeanTableException{
		try {
			beanTable.close();
			beanTable.open();
		} catch (BeanTableException ex) {
			throw  createBeanTableException(ex);
		}	
	}
	
	private BeanTableException createBeanTableException(Exception e){
		Throwable t = e;
		while (t.getCause()!=null) t = t.getCause();
		return new BeanTableException(t.getMessage(), t);		
	}
	
	public List getData(String status){
		try {
			List data = DataProvider.getInstance().getData(service,status,myIP,"UNION","");
			return data;
		} catch (Exception e) {
			lblMsgInfo.setText(e.getMessage());
		}
		return null;
	}
	
	private String DefineService(){
		String exc = "";
	    try{
		    service=DataProvider.getInstance().getService(myIP).getId().getServiceName();
			
				if(service.equals("Kasir")){

					exc = "'Kasir','Registrasi'";
				}
				else if(service.equals("Registrasi")){
					exc = "'Registrasi','Kasir'";
				}
				else
				    exc = "'exception'";
			
		
		}
	    catch(Exception ex){
	        ex.printStackTrace();
	        exc = "error";	        
		}
		
		return exc;
	}
	
	public static void main(String[] args) {
		if(isRunningCMS()==false&&isRunningSNS()==false){
		   JFrame frame = new JFrame("Queue Contingency");
		   frame.getContentPane().add(new StackServices());
		   frame.setVisible(true);
           double d = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
           double d1 = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
           frame.pack();
           frame.setLocation((int)((d - (double)frame.getWidth())/2D), (int)((d1 - (double)frame.getHeight())/2D));
           frame.getContentPane().setBackground(Color.white);
           frame.setBackground(Color.white);
		   frame.setDefaultCloseOperation(3);
		}
		if (isRunningCMS()) {
			JOptionPane.showMessageDialog(null,
					"Aplikasi Teller harus dimatikan terlebih dahulu",
					"Warning", JOptionPane.ERROR_MESSAGE);
							System.exit(0);
		}
		if (isRunningSNS()) {
			JOptionPane.showMessageDialog(null,
					"Aplikasi CS harus dimatikan terlebih dahulu", "Warning",
					JOptionPane.ERROR_MESSAGE);
							System.exit(0);
		}
	}
	
	private static boolean isRunningSNS(){
		boolean enable=false;
		try {
			File file = new File(System.getProperty("lock.sns"));
			long l = file.lastModified();
			if (l != 0L) {
				long l1 = System.currentTimeMillis() - l;
				if (l1 < 4000L)
					enable= true;
			} else {
				enable= false;
			}
		} catch (Exception e) {
		}
		return enable;
	}
	
	private static boolean isRunningCMS(){
		boolean enable=false;
		try {
			File file = new File(System.getProperty("lock.cms"));
			long l = file.lastModified();
			if (l != 0L) {
				long l1 = System.currentTimeMillis() - l;
				if (l1 < 4000L)
					enable= true;
			} else {
				enable= false;
			}
		} catch (Exception e) {
		}
		return enable;
	}
	
	private void initStartLock() {
        final File file = new File(System.getProperty("lock.qcty"));
	    try {
	         long l = file.lastModified();
	         Timer timer=new Timer(4000, new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					file.delete();
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
	         timer.start();
	         if(l != 0L)
	         {
	             long l1 = System.currentTimeMillis() - l;
	             if(l1 < 4000L){
	             	JOptionPane.showMessageDialog(null,
	    					"Aplikasi Queue Contingency harus dimatikan terlebih dahulu",
	    					"Warning", JOptionPane.ERROR_MESSAGE);
	    							System.exit(0);}
	         }
	     }
	     catch(Exception ioexception)
	     {
	         ioexception.printStackTrace();
	     }
	 }
	
	private String setLeadingZero(String str){
		NumberFormat formatter = new DecimalFormat("000");
		return formatter.format(Integer.parseInt(str));		
	}
	final String ServerName=Connector.getInstance().SERVER_NAME;
	final int portNumber = 9999;        
	private String service = "";
	private String myIP = "";
	private BeanTable beanTable = null;
	private FormPanel panel = null;
	private List results = null;
	private RubeanButton cmdTransfer = null;
	private RubeanComboBox cmbKlinik = null;
	private String menuTitle = "Queue Services";
	private WaitDialog dialog = null;
	private boolean enable = true;
	
}