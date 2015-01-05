package com.wistron.WiGallery;

import java.util.ArrayList;
import java.util.List;

import Utilities.CSStaticData;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Cocoonshu
 *
 */
public class FavoriteDBAdapter{

	private Context           mContext         = null;
	private FavoriteDBHelper  mDbHelper        = null;
	private SQLiteDatabase    mDatabase        = null;
	private int               mDBVersion       = 1;
	private String            mDBName          = CSStaticData.DBNAME_FAVORITE;
	private String            mDBTableName     = "favorite";
	private String            mDBColUid        = "uid";
	private String            mDBColFileName   = "filename";
	private String            mCreateTableStr  = null;
	
	
	public FavoriteDBAdapter(Context context){
		mContext   = context;
		mDbHelper = new FavoriteDBHelper(context, mDBName, null, mDBVersion);
		
		/*
		 * CREATE TABLE IF NOT EXISTS favorite( 
		 * 						  uid INTEGER PRIMARY KEY AUTOINCREMENT,
		 * 						  filename TEXT NOT NULL ) 
		 */
		mCreateTableStr = "CREATE TABLE IF NOT EXISTS " 
		                + mDBTableName     + " ( "
				 		+ mDBColUid        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				 		+ mDBColFileName   + " TEXT NOT NULL ) ";
	}
	
	/**
	 * 设置一系列文件的favorite标签
	 * @param filenames
	 */
	public void setAsFavorite(List<String> filenames){
		int listSize = 0;
		
		if(filenames == null || filenames.size() == 0){
			return;
		}

		open();
		listSize = filenames.size();
		for(int i = 0; i < listSize; i++){
			try {
				if(!select(filenames.get(i))){
					insert(filenames.get(i));
				}
			} catch (SQLException exp) {
				exp.printStackTrace();
				//then Do Nothing and continue to next
			}
		}
		close();
	}

	/**
	 * 移除一系列文件的favorite标签
	 * @param filenames
	 */
	public void removeFromFavorite(List<String> filenames){
		int listSize = 0;

		if(filenames == null || filenames.size() == 0){
			return;
		}

		open();
		listSize = filenames.size();
		for(int i = 0; i < listSize; i++){
			try{
				delete(filenames.get(i));
			}catch (SQLException exp) {
				exp.printStackTrace();
				//then Do Nothing and continue to next
			}
		}
		close();
	}
	
	/**
	 * 获取被设定favorite标签的文件
	 * @return
	 */
	public List<String> getFavoriteFiles(){
		List<String> result = new ArrayList<String>();
		
		open();
		result = select();
		close();
		
		return result;
	}
	
	public void clearDataBase(){
		clear();
	}
	
	/**
	 * 析构函数
	 * @return
	 */
	public FavoriteDBAdapter dispose(){
		if(mDatabase != null){
			mDatabase.releaseReference();
		}
		
		return null;
	}
	
	private void open(){
		try{
			if(mDatabase != null && mDatabase.isOpen()){
				return;
			}else{
				mDatabase = mDbHelper.getWritableDatabase();
			}
		}catch(SQLiteException exp){
			mDbHelper.close();
			mDatabase = mDbHelper.getWritableDatabase();
		}finally{
			//Do Nothing
		}
	}
	
	private void close(){
		try{
			if(mDbHelper != null){
				mDbHelper.close();
			}
			
			if(mDatabase != null){
				if(mDatabase.isOpen()){
					mDatabase.close();
				}
				mDatabase = null;
			}
		}catch (Exception exp) {
			mDbHelper.close();
		}finally{
			//Do Nothing
		}
	}
	
	private void insert(String filename) throws SQLException{
		String sql = null;
		
		/*
		 * INSERT INTO favorite(filename)
		 *               VALUES('mnt/sdcard/123.jpg')
		 */ 
		sql = "INSERT INTO " + mDBTableName + " ( "
			+ mDBColFileName + " ) "
			+ "VALUES ( '"
			+ filename + "') ";
		
		if(mDatabase != null && mDatabase.isOpen()){
			mDatabase.execSQL(sql);
		}else{
			open();
			mDatabase.execSQL(sql);
		}
	}
	
	private boolean select(String filename){
		int     size     = 0;
		String  sql      = null;
		boolean result  = false;
		Cursor  cursor   = null;
		
		/*
		 * SELECT *
		 * FROM favorite
		 * WHERE filename = 'mnt/sdcard/123.jpg'
		 */
		sql = "SELECT * " + " "
			+ "FROM " + mDBTableName + " "
			+ "WHERE " + mDBColFileName + " = '" + filename + "'";
		
		if(mDatabase != null && mDatabase.isOpen()){
			cursor = mDatabase.rawQuery(sql, null);
		}else{
			open();
			if(mDatabase != null){
				cursor = mDatabase.rawQuery(sql, null);
			}
		}
		
		if(cursor != null && cursor.getCount() > 0){
			result = true;
			cursor.close();
		}else{
			result = false;
		}
		
		return result;
	}
	
	private List<String> select(){
		int    size    = 0;
		String sql     = null;
		Cursor cursor  = null;
		List<String> result = new ArrayList<String>();
		
		/*
		 * SELECT *
		 * FROM favorite
		 */
		sql = "SELECT * " + " "
			+ "FROM " + mDBTableName;
		
		if(mDatabase != null && mDatabase.isOpen()){
			cursor = mDatabase.rawQuery(sql, null);
		}else{
			open();
			if(mDatabase != null){
				cursor = mDatabase.rawQuery(sql, null);
			}
		}
		
		if(cursor != null){
			size = cursor.getCount();
			cursor.moveToFirst();
			for(int i = 0; i < size; i++){
				result.add(cursor.getString(1));
				cursor.moveToNext();
			}
			cursor.close();
		}
		
		return result;
	}
	
	private void delete(String filename) throws SQLException{
		String sql = null;
		
		/*
		 * DELETE FROM favorite
		 * WHERE filename = '/mnt/sdcard/123.jpg'
		 */ 
		sql = "DELETE FROM " + mDBTableName + " "
			+ "WHERE " + mDBColFileName + " = '" + filename + "' ";
		
		if(mDatabase != null && mDatabase.isOpen()){
			mDatabase.execSQL(sql);
		}else{
			open();
			mDatabase.execSQL(sql);
		}
	}
	
	private void clear() throws SQLException{
		String sql = null;
		
		/*
		 * DELETE FROM favorite
		 */
		sql = "DELETE FROM " + mDBTableName + " ";
		
		if(mDatabase != null && mDatabase.isOpen()){
			mDatabase.execSQL(sql);
		}else{
			open();
			mDatabase.execSQL(sql);
		}
	}
	
	/**
	 * 
	 * @author Cocoonshu
	 *
	 */
	class FavoriteDBHelper extends SQLiteOpenHelper{

		public FavoriteDBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(mCreateTableStr);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
		
	}

}
