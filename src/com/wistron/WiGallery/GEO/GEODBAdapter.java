package com.wistron.WiGallery.GEO;

import Utilities.CSStaticData;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.util.Log;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-02-29 11:21:49
 * @purpose 数据库辅助类
 * @detail 数据库的名称为[geoCache]
 *         数据库中，每种语言对应一个表，命名规则：TAB_NAME + "_" + TAB_LANG
 */
public class GEODBAdapter extends SQLiteOpenHelper{
	
	private   static final String TAG       = "GEODBAdapter";
	
	protected static final int    DB_VER    = 1;                           //数据库版本
	protected static final String DB_NAME   = CSStaticData.DBNAME_GEOINFO; //数据库名称
	protected static final String TAB_NAME  = "geoContrast";               //数据表名称
	protected static       String TAB_LANG  = "";                          //语言
	
	public    static final String UID       = "uid";                       //主键：由经纬度换算而来
	public    static final String COUNTRY   = "country";                   //国家
	public    static final String PROVINCE  = "province";                  //省
	public    static final String CITY      = "city";                      //市
	public    static final String DISTRICT  = "district";                  //区
	
	
	private   SQLiteDatabase      mDB       = null;
	private   Context             mContext  = null;
	
	public GEODBAdapter(Context context, String lang) {
		super(context, DB_NAME, null, DB_VER);
		mContext = context;
		TAB_LANG = lang.toLowerCase(); //定义数据库语言
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlStr = "";
		try{
		/*  
		 *  CREATE TABLE IF NOT EXISTS 
		 *  geoContrast_ENGLISH(uid INTEGER PRIMARY KEY NOT NULL,
		 *                      country TEXT,
		 *                      province TEXT,
		 *                      city TEXT,
		 *                      district)
		 */ 
		sqlStr = "CREATE TABLE IF NOT EXISTS " +
				   TAB_NAME + "_" + TAB_LANG + "(" +
				   UID      + " INTEGER PRIMARY KEY, " +
				   COUNTRY  + " TEXT, " +
				   PROVINCE + " TEXT, " +
				   CITY     + " TEXT, " +
				   DISTRICT + " TEXT) ";
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[createTable] " + sqlStr);
		}
		db.execSQL(sqlStr);
		}catch (Exception e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			db.close();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void doCreate(String lang){
		String sqlStr = "";
		try{
			
			if(mDB == null || !mDB.isOpen()){
				mDB = getWritableDatabase();
			}
			
			if(lang == null){
				lang = "";
			}
			TAB_LANG = lang.toLowerCase();
			
			/*  
			 *  CREATE TABLE IF NOT EXISTS 
			 *  geoContrast_ENGLISH(uid INTEGER PRIMARY KEY NOT NULL,
			 *                      country TEXT,
			 *                      province TEXT,
			 *                      city TEXT,
			 *                      district)
			 */ 
			sqlStr = "CREATE TABLE IF NOT EXISTS " +
					   TAB_NAME + "_" + TAB_LANG + "(" +
					   UID      + " INTEGER PRIMARY KEY, " +
					   COUNTRY  + " TEXT, " +
					   PROVINCE + " TEXT, " +
					   CITY     + " TEXT, " +
					   DISTRICT + " TEXT) ";
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[createTable] " + sqlStr);
			}
			mDB.execSQL(sqlStr);
			}catch (Exception e) {
				if(CSStaticData.DEBUG){
					e.printStackTrace();
				}
				close();
			}
	}
	
	/**
	 * 清空数据表
	 * 指定待清空数据表的语言
	 * @param lang 指定要清空的语言
	 */
	public void clear(String langeuage){
		String sqlStr = "";
		String lang   = "";
		try{
			if(langeuage != null){
				lang = langeuage;
			}
				
			/*  
			 *  DROP TABLE IF NOT EXISTS 
			 *  geoContrast_ENGLISH
			 */ 
			sqlStr = "DROP TABLE IF NOT EXISTS " +
					   TAB_NAME + "_" + lang.toLowerCase();
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[clear] " + sqlStr);
			}
			mDB.execSQL(sqlStr);
		}catch (Exception e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 清空数据库
	 */
	public void clearAll(){
		close();
		mContext.deleteDatabase(DB_NAME);
	}
	
	/**
	 * 写出所有数据
	 * @param lang 语言
	 * @return
	 */
	public Cursor list(String langeuage){
		Cursor cursor = null;
		String lang   = "";
		String sqlStr = "";
		
		try{
			if(langeuage != null){
				lang = langeuage;
			}
			
			if(mDB == null || !mDB.isOpen()){
				mDB = getWritableDatabase();
			}
			/*
			 *  SELECT *
			 *  FROM geoContrast_ENGLISH
			 */
			sqlStr = "SELECT * " +
				     "FROM " + TAB_NAME + "_" + lang.toLowerCase();
			if(CSStaticData.DEBUG && false){
				Log.w(TAG, "[list] " + sqlStr);
			}
			cursor = mDB.rawQuery(sqlStr, null);
		}catch (SQLException e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			cursor = null;
			if(mDB != null && mDB.isOpen()){
				close();
			}
		}
		
		return cursor;
	}
	
	/**
	 * 插入一条数据
	 * @param uid    GPS UID
	 * @param locale 位置信息
	 * @param lang   语言
	 * @return       是否插入成功
	 */
	public boolean insert(long uid, GEOAddress address, String langeuage){
		boolean state  = true;
		String  lang   = "";
		String  sqlStr = "";
		
		try{
			
			if(langeuage != null){
				lang = langeuage;
			}
			
			if(mDB == null || !mDB.isOpen()){
				mDB = getWritableDatabase();
			}
			
			/*
			 *  INSERT INTO 
			 *  geoContrast_ENGLISH(uid, country, province, city, district)
			 *  VALUES(20155711583, 'CHINA', 'HuBei', 'WuHan', 'JiangXia')
			 */
			sqlStr = "INSERT INTO " + TAB_NAME + "_" + lang.toLowerCase() + 
					 "(" + UID + ", " + COUNTRY + ", " + PROVINCE + ", " + CITY + ", " + DISTRICT + ") " +
					 "VALUES(" +  uid + ", '" +
					          address.getCountryName()  + "', '" +
					          address.getLocality()     + "', '" + 
					          address.getAdminArea()    + "', '" +
					          address.getSubAdminArea() + "')";
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[insert] " + sqlStr);
			}
			mDB.execSQL(sqlStr);
			
		}catch (SQLException e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			state = false;
			if(mDB != null && mDB.isOpen()){
				close();
			}
		}
		return state;
	}
	
	/**
	 * 删除一条记录
	 * @param uid  GPS UID
	 * @param lang 语言
	 * @return     是否删除成功
	 */
	public boolean delete(long uid, String langeuage){
		boolean state  = true;
		String  lang   = "";
		String  sqlStr = "";
		
		try{
			
			if(langeuage != null){
				lang = langeuage;
			}
			
			if(mDB == null || !mDB.isOpen()){
				mDB = getWritableDatabase();
			}
			
			/*
			 *  DELETE FROM geoContrast_ENGLISH
			 *  WHERE uid = 20155711583
			 */
			sqlStr = "DELETE FROM " + TAB_NAME + "_" + lang.toLowerCase() + " " +
				     "WHERE " + UID + " = " + uid;
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[delete] " + sqlStr);
			}
			mDB.execSQL(sqlStr);
			
		}catch (SQLException e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			state = false;
			if(mDB != null && mDB.isOpen()){
				close();
			}
		}
		
		return state;
	}
	
	/**
	 * 修改一条记录
	 * @param uid     GPS UID
	 * @param address 待重写的位置信息
	 * @param lang    语言
	 * @return        是否修改成功
	 */
	public boolean update(long uid, Address address, String langeuage){
		boolean state  = true;
		String  lang   = "";
		String  sqlStr = "";
		
		try{
			
			if(langeuage != null){
				lang = langeuage;
			}

			if(mDB == null || !mDB.isOpen()){
				mDB = getWritableDatabase();
			}
			
			/*
			 *  UPDATE geoContrast_ENGLISH
			 *  SET country = 'CHINA', province = 'ChongQing', city = 'ChongQing', district = 'FuLing'
			 *  WHERE uid = 20155711583
			 */
			sqlStr = "UPDATE " + TAB_NAME + "_" + lang.toLowerCase() + " " +
				     "SET " + COUNTRY  + " = '" + address.getCountryName()  + "', " + 
		                      PROVINCE + " = '" + address.getLocality()     + "', " +
		                      CITY     + " = '" + address.getAdminArea()    + "', " +
		                      DISTRICT + " = '" + address.getSubAdminArea() + "' "  +
		             "WHERE " + UID + " = " + uid;
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[update] " + sqlStr);
			}
			mDB.execSQL(sqlStr);

		}catch (SQLException e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			state = false;
			if(mDB != null && mDB.isOpen()){
				close();
			}
		}

		return state;
	}
	
	/**
	 * 查询记录
	 * @param uid  GPS UID
	 * @param lang 语言
	 * @return     查询到的记录集
	 */
	public Cursor select(long uid, String langeuage){
		Cursor cursor = null;
		String lang   = "";
		String sqlStr = "";
		
		try{
			
			if(lang != null){
				lang = langeuage;
			}

			if(mDB == null || !mDB.isOpen()){
				try{
					mDB = getWritableDatabase();
				}catch (SQLException e) {
					e.printStackTrace();
					if(e.getMessage().startsWith("no such table")){
						doCreate(lang);
					}else{
						throw e;
					}
				}
			}
			
			/*
			 *  SELECT *
			 *  FROM geoContrast_ENGLISH
			 *  WHERE uid = 20155711583
			 */
			sqlStr = "SELECT * " +
				     "FROM " + TAB_NAME + "_" + lang.toLowerCase() + " " +
				     "WHERE " + UID + " = " + uid;
			if(CSStaticData.DEBUG && false){
				Log.w(TAG, "[select] " + sqlStr);
			}
			cursor = mDB.rawQuery(sqlStr, null);

		}catch (SQLException e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			if(cursor != null){
				cursor.close();
				cursor = null;
			}
			if(mDB != null && mDB.isOpen()){
				close();
			}
		}
		
		return cursor;
	}
	
	/**
	 * 释放数据库
	 */
	public void close(){
		if(mDB != null){
			if(mDB.isOpen()){
//				mDB.close();
				super.close();
				mDB = null;
			}
		}
	}
}
