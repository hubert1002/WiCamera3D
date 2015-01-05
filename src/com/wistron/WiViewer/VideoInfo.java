package com.wistron.WiViewer;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;


public class VideoInfo {    

	private String m_title;    
	private String m_album;    
	private String m_artist;    
	private String m_displayName;    
	private String m_mimeType;    
	private String m_path;    
	private long m_size;    
	private long m_duration;    
	
	
	public String getM_date()
	{
		return m_date;
	}
	public String getM_format()
	{
		return m_format;
	}
	public String getM_videoquality()
	{
		return m_videoquality;
	}
	public String getM_videoSize()
	{
		return m_videoSize;
	}
	public String getM_framerate()
	{
		return m_framerate;
	}
	public String getM_AudioChannel()
	{
		return m_AudioChannel;
	}
	public void setM_date(String m_date)
	{
		this.m_date = m_date;
	}
	public void setM_format(String m_format)
	{
		this.m_format = m_format;
	}
	public void setM_videoquality(String m_videoquality)
	{
		this.m_videoquality = m_videoquality;
	}
	public void setM_videoSize(String m_videoSize)
	{
		this.m_videoSize = m_videoSize;
	}
	public void setM_framerate(String m_framerate)
	{
		this.m_framerate = m_framerate;
	}
	public void setM_AudioChannel(String m_AudioChannel)
	{
		this.m_AudioChannel = m_AudioChannel;
	}

	private String m_date;
	private String m_format;
	private String m_videoquality;
	private String m_videoSize;
	private String m_framerate;
	private String m_AudioChannel;
   private  String[]  strings;
   private  String[]  stringsInfo;
	public VideoInfo() {        
		super();    
	}    

	public String getTitle() {      
		if(this.m_title==null)
			return   "Unknown";
		return m_title;    
	}    

	public void setTitle(String title) {    
		
		this.m_title = title;    
	}    

	public String getAlbum() {
		if(this.m_album==null)
			return   "Unknown";
		return m_album;    
	}    

	public void setAlbum(String album) {        
		this.m_album = album;    
	}    

	public String getArtist() {
		if(this.m_artist==null )
			return  "Unknown";
		return m_artist;    
	}    

	public void setArtist(String artist) {        
		this.m_artist = artist;    
	}    

	public String getDisplayName() { 
		if(this.m_displayName==null )
			return  "Unknown";
		return m_displayName;    
	}    

	public void setDisplayName(String displayName) {        
		this.m_displayName = displayName;    
	}    

	public String getMimeType() {        
		return m_mimeType;    
	}    

	public void setMimeType(String mimeType) {        
		this.m_mimeType = mimeType;    
	}    

	public String getPath() {  
		if(this.m_path==null)
			return   "Unknown";
		
		return this.m_path;	
		
	}    


	public String showPath() {  
		String string=this.m_path;
		if(this.m_path==null)
			return   "Unknown";
		if(string.length()>37){
			
			return  string.substring(0,35)+"...";
			
		}
		else{
		return string;	
		}
	}    
	public void setPath(String path) {        
		this.m_path = path;    
	}    

	public String getSize() { 
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

	public void setSize(long size) {        
		this.m_size = size;    
	}    

	public String getDuration() { 
		if(this.m_duration<=0)
			return   "Unknown";
		double duration = 0;
		int minute   = 0,
			second   = 0,
			hour      =0;
		duration=m_duration;
		duration=Math.ceil(duration/1000);
		hour=(int) (duration)/3600;
		minute = (int) (duration)/60-hour*60;
		second = (int) (duration) %60;
		return String.format("%02d:%02d:%02d",hour,minute,second);
	}    

	public void setDuration(long duration) {        
		this.m_duration = duration;    
	}
	
	public void setLable()
	{
		 strings=new String[8];
	     strings[0]="Video Title";
         strings[1]="Duration";
	     strings[2]="Creation Date & Time";
	     strings[3]="Compression";
	     strings[4]="Video Quality";
         strings[5]="Video Size";
	     strings[6]="Frame Rate";
         strings[7]="Audio Channel";
	}
	public void setInfo()
	{
		 stringsInfo=new String[8];
		 stringsInfo[0]= this.getTitle();
		 stringsInfo[1]=this.getDuration();
		 stringsInfo[2]=this.getM_date();
		 stringsInfo[3]=this.getM_format();
		 stringsInfo[4]=this.getM_videoquality();
		 stringsInfo[5]=this.getSize();
		 stringsInfo[6]=this.getM_framerate();
		 stringsInfo[7]=this.getM_AudioChannel();
	}
	public SpannableString[] showInfoList()
	{
		SpannableString[] res=new SpannableString[8];
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
		string = strings[0] + "\n"+ stringsInfo[0]+ "\n"+ " \n"
		        + strings[1]+"\n"+ stringsInfo[1]+ "\n"+ " \n"
		        + strings[2]+"\n"+  stringsInfo[2]+ "\n"+ " \n"
		        + strings[3]+"\n"+  stringsInfo[3]+ "\n"+ " \n"
		        + strings[4] +"\n"+  stringsInfo[4] + "\n"+ " \n"
		        + strings[5]+"\n"+  stringsInfo[5]+ "\n"+ " \n"
				+strings[6]+"\n"+ stringsInfo[6]+ "\n"+ " \n"
				+strings[7]+"\n"+ stringsInfo[7]+ "\n"+ " \n";
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
	public SpannableString showInfo_()
	{
		setLable();
		setInfo();
		String string;
		string =strings[0]+"\n"+stringsInfo[0]+"\n" +
    			strings[1]+"\n"+stringsInfo[1]+"\n" +
    			strings[2]+"\n"+stringsInfo[2]+"\n" +
    			strings[3]+"\n"+stringsInfo[3]+"\n" +
    			strings[4]+"\n"+stringsInfo[4]+"\n" +
    			strings[5]+"\n"+stringsInfo[5]+"\n" +
    			strings[6]+"\n"+stringsInfo[6]+"\n" +
    			strings[7]+"\n"+stringsInfo[7];
	   SpannableString ss = new SpannableString(string);  
	   int curindex=0;
	   int curstring=0;
	   int curinfo=0;
	   for(int i=0;i<strings.length;i++)
	   {
		   curstring=strings[i].length();
		   curinfo= stringsInfo[i].length();
		   ss.setSpan(new AbsoluteSizeSpan(20), curindex, curindex+curstring,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+curstring+1;
		   ss.setSpan(new AbsoluteSizeSpan(15),curindex, curindex+curinfo,  
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		   curindex=curindex+curinfo+1;
	   }
		return ss;

	}
	 public SpannableString showInfo__()
	    {
	   setLable();
	   setInfo();
	    	String string;
	    	string= 
	    			strings[0]+stringsInfo[0]+"\n" +
	    			strings[1]+stringsInfo[1]+"\n" +
	    			strings[2]+stringsInfo[2]+"\n" +
	    			strings[3]+stringsInfo[3]+"\n" +
	    			strings[4]+stringsInfo[4]+"\n" +
	    			strings[5]+stringsInfo[5]+"\n" +
	    			strings[6]+stringsInfo[6]+"\n" +
	    			strings[7]+stringsInfo[7];
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
	    		   ss.setSpan(new ForegroundColorSpan(0xff88ee00), curindex+curstring, curindex+curstring+curinfo,  
	    	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	    		   curindex=curindex+curstring+curinfo+1;
	    	   }
	    	   
			return ss;
	    	
	    }
}
