package com.fqueue.commons;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.StringTokenizer;
import java.util.Vector;

public class PrintPage implements Printable {  
      
	PageFormat pageFormat;
    String text = "kosong";

    public static final int PAPER_A4 = 1;
    public static final int PAPER_LETTER = 2;

    int normalFontSize = 25;
    int largeFontSize = 100;
    double widthInch = 8.27;
    double heightInch = 5;
    double dpiX = 180;
    double dpiY = 180;

    int width = (int) (widthInch * (double)dpiX) - 5;//612;
    int height = (int) (heightInch * (double)dpiY) - 5;//936;
    int leftMargin = 12;
    int rightMargin = 2;
    int topMargin = 10; 
    Vector v = null;
    
    public PrintPage(String trxDate, String queueNo, String service, String counter, String timeStartQ, String timeStartService, String waitingTime, String branch) {  
    	v = new Vector();
    	v.add(new String[]{"20","PLAIN","Tanggal Transaksi     : "+trxDate});
    	v.add(new String[]{"20","PLAIN","Nomer Antrian         : "+setLeadingZero(Integer.parseInt(queueNo))});
    	v.add(new String[]{"20","PLAIN","Jenis Layanan         : "+service});
    	v.add(new String[]{"20","PLAIN","Counter Yang Melayani : "+counter});
    	v.add(new String[]{"20","PLAIN","Waktu Mulai Antri     : "+timeStartQ});
    	v.add(new String[]{"20","PLAIN","Waktu Mulai Dilayani  : "+timeStartService});
    	v.add(new String[]{"20","PLAIN","Waktu Tunggu          : "+waitingTime});
    	v.add(new String[]{"20","PLAIN","Cabang                : "+branch});
    }
    
    private static String setLeadingZero(int qNo){
		NumberFormat formatter = new DecimalFormat("000");
		String res = formatter.format(qNo);
		return res;
	}
    
    public int print(Graphics g, PageFormat pageFormat, int index) throws PrinterException {
        System.out.println("PermataPrint");
        Graphics2D g2 = (Graphics2D) g;
        g2.translate((int)pageFormat.getImageableX(), (int)pageFormat.getImageableY());
        g2.scale(72.0d / dpiX, 72.0d / dpiY);

        System.out.println("x:"+72.0d / dpiX +"  y:"+ 72.0d / dpiY);
        /**
         * Calculate max length
         */
        int maxLength = 0;

        StringTokenizer token = new StringTokenizer(text, "\n");
        
        while(token.hasMoreTokens())
        {
            String next = token.nextToken();
            int length = next.length();
            if(length > maxLength)
            {
                maxLength = length;
            }
        }

        Font font;
        FontMetrics metrics = null;
        int fontSize = largeFontSize;
        int maxWidth;
        double paperWidth = pageFormat.getImageableWidth();
        System.out.println("PaperWidth:"+paperWidth);
        int yDelta = 0;
        int y = 0;

        for (int i=0; i<v.size(); i++){
    
        		leftMargin = 12;
        		yDelta = 30;
        	
        	
        	
        	String[] s = (String[]) v.get(i); 
        	font = Font.decode("Monospaced-"+s[1]+ "-" +s[0]);
        	metrics = g2.getFontMetrics(font);
    		 //metrics.getHeight();
    		y += yDelta;
    	
        	int width = metrics.charWidth('w');
            maxWidth = width * maxLength;
            g2.setFont(font);
            g2.setColor(Color.black);    
            
//            g2.drawString(s[2].substring(0, s[2].length()), leftMargin, y);
            g2.drawString(s[2], leftMargin, y);
            
        }
        
      
        return Printable.PAGE_EXISTS;
    }

  }  