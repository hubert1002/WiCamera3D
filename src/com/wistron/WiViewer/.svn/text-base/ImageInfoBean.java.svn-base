package com.wistron.WiViewer;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

/**
 * 记录图片信息，包括图片名，文件创建时间，长宽，经纬度，模式
 * 
 **/
public class ImageInfoBean
{
    public	 String[]  strings;
    public	 String[]  stringsInfo;
	public String getM_name()
	{
		if(this.m_name!=null){
			 String[] m_filename;
			 m_filename=m_name.split("\\.");
			 String  m_nameString="";
			 for(int i=0;i<m_filename.length-1;i++)
			 {
				 m_nameString+=m_filename[i];
				 if(i<m_filename.length-2)
					 m_nameString+=".";
			 }
			return  m_nameString;
		}
		else{
			return "Unknown";
		}
		
	}

	public String getM_path()
	{
		String string = m_path;
		if (string.length() > 37)
		{

			return string.substring(0, 35) + "...";

		} else
		{
			return string;
		}
	}

	public String getM_gps_altitude()
	{
		return m_gps_altitude;
	}

	public String getM_gps_latitude()
	{
		String string;
		if (m_gps_latitude != null){
			string = m_gps_latitude;
			Log.e("getM_gps_latitude", m_gps_latitude);
			String[] liStrings = string.split(",");
			double[] mStrings =new double[liStrings.length];
			double e =0.0;
			String a=null;
			for(int i=0;i<liStrings.length;i++)
			{
                a=liStrings[i].split("/")[0];
				if(i!=2)
				mStrings[i]=Double.parseDouble(a);
				else{
			//	mStrings[i]=Double.parseDouble(a)/100;	
				}
			
			}
			e=mStrings[0]+mStrings[1]/60+mStrings[2]/3600;
			
			String reString=String.valueOf(e - (int) e);
			String mString_afterdote="";
			int lenghth= reString.length();
			if(lenghth<=1)
			{
				mString_afterdote=".0";
			}
			else if(lenghth<=7) {
				mString_afterdote=reString.substring(1, lenghth-1);
			}
			else
			{
				mString_afterdote=reString.substring(1, 6);
			}
			return (int) e + mString_afterdote;
		}
		else{
			return  "Unknown";
		}
	}

	public String getM_gps_longitude()
	{
		String string;
		if (m_gps_longitude != null){
			string = m_gps_longitude;
			Log.e("getM_gps_longitude", m_gps_longitude);
			String[] liStrings = string.split(",");
			double[] mStrings =new double[liStrings.length];
			double e =0;
			for(int i=0;i<liStrings.length;i++)
			{
				if(i!=2)
				mStrings[i]=Double.parseDouble(liStrings[i].split("/")[0]);
				else{
			//	mStrings[i]=Double.parseDouble(liStrings[i].split("/")[0])/100;	
				}
				
			}
			e=mStrings[0]+mStrings[1]/60+mStrings[2]/3600;
			
			String reString=String.valueOf(e - (int) e);
			String mString_afterdote="";
			int lenghth= reString.length();
			if(lenghth<=1)
			{
				mString_afterdote=".0";
			}
			else if(lenghth<=7) {
				mString_afterdote=reString.substring(1, lenghth-1);
			}
			else
			{
				mString_afterdote=reString.substring(1, 6);
			}
			return (int) e + mString_afterdote;
		}
		else{
			return  "Unknown";
		}
	}

	public String getM_image_length()
	{
		return m_image_length;
	}

	public String getM_image_width()
	{
		return m_image_width;
	}

	public String getM_mode()
	{
		if(m_mode==null)
			return  "Unknown";
		return m_mode;
	}

	public String getM_make()
	{
		if(m_mode==null)
			return  "Unknown";
		return m_make;
	}

	public void setM_name(String m_name)
	{

		this.m_name = m_name;

	}

	public void setM_path(String m_path)
	{

		this.m_path = m_path;

	}

	public void setM_gps_altitude(String m_gps_altitude)
	{
		this.m_gps_altitude = "null";

	}

	public void setM_gps_latitude(String m_gps_latitude)
	{

		this.m_gps_latitude = m_gps_latitude;

	}

	public void setM_gps_longitude(String m_gps_longitude)
	{

		this.m_gps_longitude = m_gps_longitude;

	}

	public void setM_image_length(String m_image_length)
	{
		this.m_image_length = m_image_length;
	}

	public void setM_image_width(String m_image_width)
	{

		this.m_image_width = m_image_width;

	}

	public void setM_mode(String m_mode)
	{

		this.m_mode = m_mode;

	}

	public void setM_make(String m_make)
	{

		this.m_make = m_make;

	}

	public String getM_time()
	{
		return m_time;
		
		
		
	}

	public void setM_time(String m_time)
	{

		this.m_time = m_time;

	}

	private String m_path;
	private String m_name;
	private String m_gps_altitude;
	private String m_gps_latitude;
	private String m_gps_longitude;
	private String m_image_length;
	private String m_image_width;
	private String m_mode;
	private String m_make;
	private String m_time;
	private String m_format;
	private long m_size;
    public String getM_orientation()
	{
		return m_orientation;
	}

	public void setM_orientation(String m_orientation)
	{
		this.m_orientation = m_orientation;
	}

	private String m_orientation;
	public String getM_size()
	{
		if(this.m_size<=0)
			return   "Unknown";
		float size = m_size;
		if (size <= 1024)
			return size + "Byte";
		else if (size > 1024 && size <= 10 * 1024 * 1024)
		{
			String  s=String.valueOf(size / 1024 - (int) (size / 1024))+"0000";
			return (int) (size / 1024)
					+ s.substring(1, 4) + "KB";
		}
			
		else
		{
			String  ss=String.valueOf(
					size / (1024 * 1024) - (int) (size / (1024 * 1024)))+"000";
			return (int) (size / (1024 * 1024))
					+ ss.substring(1,3) + "MB";
			
		}
			

	}

	public void setM_size(long m_size)
	{
		this.m_size = m_size;
	}
	public void setLable()
	{
		 strings=new String[10];
	     strings[0]="Image Title";
	     strings[1]="Compression";
	     strings[2]="Creation Date & Time";
	     strings[3]="Camera Model";
	     strings[4]="Dimension";
	     strings[5]="Image Size";
	     strings[6]="Orientation";
	     strings[7]="Location";
	     strings[8]="Latitude";
	     strings[9]="Longitude";
	}
	public void setInfo()
	{
		 stringsInfo=new String[10];
		 stringsInfo[0]= this.getM_name();
		 stringsInfo[1]=this.getM_format();
		 stringsInfo[2]=this.getM_time();
		 stringsInfo[3]=this.getM_mode();
		 
		 if(this.getM_image_width().equals("0")||this.getM_image_length().equals("0"))
		 {
			 stringsInfo[4]= "Unknown";
		 }else {
			 stringsInfo[4]= this.getM_image_width() + "x" + this.getM_image_length();
		}
		 stringsInfo[5]= this.getM_size() ;
		 stringsInfo[6]= this.getM_orientation();
		 stringsInfo[7]=this.getM_path();
		 stringsInfo[8]=this.getM_gps_latitude();
		 stringsInfo[9]=this.getM_gps_longitude();

	}
	public SpannableString showInfoTEST()
	{
		setLable();
		setInfo();
		String string;
		string = strings[0] + "\n"+ stringsInfo[0]+ "\n"+ "                                                                                                                  \n"
		        + strings[1]+"\n"+ stringsInfo[1]+ "\n"+ "                                                                                                                  \n"
		        + strings[2]+"\n"+  stringsInfo[2]+ "\n"+ "                                                                                                                  \n"
		        + strings[3]+"\n"+  stringsInfo[3]+ "\n"+ "                                                                                                                  \n"
		        + strings[4] +"\n"+  stringsInfo[4] + "\n"+ "                                                                                                                  \n"
		        + strings[5]+"\n"+  stringsInfo[5]+ "\n"+ "                                                                                                                  \n"
				+strings[6]+"\n"+ stringsInfo[6]+ "\n"+ "                                                                                                                  \n"
				+strings[7]+"\n"+ stringsInfo[7]+ "\n"+ "                                                                                                                  \n"
				+strings[8]+"\n"+ stringsInfo[8]+ "\n"+ "                                                                                                                  \n"
				+strings[9]+"\n"+ stringsInfo[9]+ "\n"+"                                                                                                                  \n";
	   SpannableString ss = new SpannableString(string);  
	   int curindex=0;
	   int curstring=0;
	   int curinfo=0;
	   for(int i=0;i<strings.length;i++)
	   {
		   curstring=strings[i].length();
		   curinfo= stringsInfo[i].length();
		   ss.setSpan(new ForegroundColorSpan(Color.WHITE), curindex, curindex+curstring+1,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+curstring+1;
		   ss.setSpan(new ForegroundColorSpan(Color.rgb(31, 234, 189)),curindex, curindex+curinfo+1,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+curinfo+1;
		   ss.setSpan(new AbsoluteSizeSpan(5),curindex, curindex+115,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   ss.setSpan(new UnderlineSpan(),curindex, curindex+115,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+115;
	   }
		return ss;

	}
	
	
	public SpannableString showInfoRight()
	{
		setLable();
		setInfo();
		String string;
		string = strings[0] + "\n"+ stringsInfo[0]+ "\n"+ " \n"
		        + strings[1]+"\n"+ stringsInfo[1]+ "\n"+ " \n"
		        + strings[2]+"\n"+  stringsInfo[2]+ "\n"+ " \n"
		        + strings[3]+"\n"+  stringsInfo[3]+ "\n"+ " \n"
		        + strings[4] +"\n"+  stringsInfo[4] + "\n"+ " \n"
		        + strings[5]+"\n"+  stringsInfo[5]+ "\n"+ " \n"
				+strings[6]+"\n"+ stringsInfo[6]+ "\n"+ " \n"
				+strings[7]+"\n"+ stringsInfo[7]+ "\n"+ " \n"
				+strings[8]+"\n"+ stringsInfo[8]+ "\n"+ " \n"
				+strings[9]+"\n"+ stringsInfo[9]+ "\n"+" \n";
	   SpannableString ss = new SpannableString(string);  
	   int curindex=0;
	   int curstring=0;
	   int curinfo=0;
	   for(int i=0;i<strings.length;i++)
	   {
		   curstring=strings[i].length();
		   curinfo= stringsInfo[i].length();
		   ss.setSpan(new AbsoluteSizeSpan(20), curindex, curindex+curstring+1,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+curstring+1;
		   ss.setSpan(new AbsoluteSizeSpan(15),curindex, curindex+curinfo+1,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+curinfo+1;
		   ss.setSpan(new AbsoluteSizeSpan(5),curindex, curindex+2,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+2;
	   }
		return ss;

	}
	public SpannableString[] showInfoList()
	{
		SpannableString[] res=new SpannableString[10];
		setLable();
		setInfo();
		   ForegroundColorSpan whiteColor=new ForegroundColorSpan(Color.WHITE);
		   ForegroundColorSpan infoColor=new ForegroundColorSpan(Color.rgb(31, 234, 189));
		for(int i=0;i<res.length;i++)
		{
			   int curindex=0;
			   res[i]=new SpannableString(strings[i] + "\n"+ stringsInfo[i]);
			   int curstring=strings[i].length();
			   int curinfo= stringsInfo[i].length();
			   res[i].setSpan(whiteColor, curindex, curindex+curstring,  
		               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			   curindex=curindex+curstring+1;
			   res[i].setSpan(infoColor,curindex, curindex+curinfo,  
		               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return res;
	}
 	public SpannableString showInfo()
	{
		setLable();
		setInfo();
		String string;
		string = strings[0] + "\n"+ stringsInfo[0]+ "\n"
		        +strings[1]+"\n"+ stringsInfo[1]+ "\n"
		        + strings[2]+"\n"+  stringsInfo[2]+ "\n"
		        + strings[3]+"\n"+  stringsInfo[3]+ "\n"
		        + strings[4] +"\n"+  stringsInfo[4] + "\n"
		        + strings[5]+"\n"+  stringsInfo[5]+ "\n"
				+strings[6]+"\n"+ stringsInfo[6]+ "\n"
				+strings[7]+"\n"+ stringsInfo[7]+ "\n"
				+strings[8]+"\n"+ stringsInfo[8]+ "\n"
				+strings[9]+"\n"+ stringsInfo[9];
	   SpannableString ss = new SpannableString(string);  
	   int curindex=0;
	   int curstring=0;
	   int curinfo=0;
	   for(int i=0;i<strings.length;i++)
	   {
		   curstring=strings[i].length();
		   curinfo= stringsInfo[i].length();
		   ss.setSpan(new ForegroundColorSpan(Color.WHITE), curindex, curindex+curstring,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+curstring+1;
		   ss.setSpan(new ForegroundColorSpan(Color.rgb(31, 234, 189)),curindex, curindex+curinfo,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+curinfo+1;
	   }
//	   for(int i=0;i<strings.length;i++)
//	   {
//		   curstring=strings[i].length();
//		   curinfo= stringsInfo[i].length();
//		   ss.setSpan(new AbsoluteSizeSpan(20), curindex, curindex+curstring,  
//	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		   curindex=curindex+curstring+1;
//		   ss.setSpan(new AbsoluteSizeSpan(15),curindex, curindex+curinfo,  
//	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		   curindex=curindex+curinfo+1;
//		   ss.setSpan(new AbsoluteSizeSpan(1),curindex, curindex+1,  
//	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		   curindex=curindex+2;
//	   }
		return ss;

	}
	
	
	public SpannableString showInfo__()
	{
		setLable();
		setInfo();
		String string;
		string = strings[0] + stringsInfo[0]+ "\n"
		        + strings[1]+stringsInfo[1]+ "\n"
		        + strings[2]+ stringsInfo[2]+ "\n"
		        + strings[3]+ stringsInfo[3]+ "\n"
		        + strings[4] + stringsInfo[4] + "\n"
		        + strings[5]+ stringsInfo[5]+ "\n"
				+strings[6]+stringsInfo[6]+ "\n"
				+strings[7]+stringsInfo[7]+ "\n"
				+strings[8]+stringsInfo[8]+ "\n"
				+strings[9]+stringsInfo[9];
	   SpannableString ss = new SpannableString(string);  
	   int curindex=0;
	   int curstring=0;
	   int curinfo=0;
	   for(int i=0;i<strings.length;i++)
	   {
		   curstring=strings[i].length();
		   curinfo= stringsInfo[i].length();
		   ss.setSpan(new ForegroundColorSpan(Color.WHITE), curindex, curindex+curstring,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   ss.setSpan(new ForegroundColorSpan(Color.rgb(31, 234, 189)), curindex+curstring, curindex+curstring+curinfo,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+curstring+curinfo+1;
	   }
//	   for(int i=0;i<strings.length;i++)
//	   {
//		   curstring=strings[i].length();
//		   curinfo= stringsInfo[i].length();
//		   ss.setSpan(new AbsoluteSizeSpan(20), curindex, curindex+curstring,  
//	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		   curindex=curindex+curstring+1;
//		   ss.setSpan(new AbsoluteSizeSpan(15),curindex, curindex+curinfo,  
//	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		   curindex=curindex+curinfo+1;
//	   }
//	   for(int i=0;i<strings.length;i++)
//	   {
//		   curstring=strings[i].length();
//		   curinfo= stringsInfo[i].length();
//		   ss.setSpan(new AbsoluteSizeSpan(20), curindex, curindex+curstring,  
//	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		   curindex=curindex+curstring+1;
//		   ss.setSpan(new AbsoluteSizeSpan(15),curindex, curindex+curinfo,  
//	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		   curindex=curindex+curinfo+1;
//		   ss.setSpan(new AbsoluteSizeSpan(1),curindex, curindex+1,  
//	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		   curindex=curindex+2;
//	   }
		return ss;

	}

	public String getM_format()
	{
		if(m_format==null)
			return "Unknown";
		return m_format;
	}

	public void setM_format(String m_format)
	{
		this.m_format = m_format;
	}
	
}
