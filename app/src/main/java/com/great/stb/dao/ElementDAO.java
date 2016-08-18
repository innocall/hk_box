package com.great.stb.dao;



import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.great.stb.bean.ActDataMode;
import com.great.stb.bean.DeviceInfoMode;
import com.great.stb.bean.ElementMode;
import com.great.stb.database.SmartSTBSQLiteOpenHelper;
import com.great.stb.database.SqliteDBHandler;


public class ElementDAO {
	
	//在构造函数里初始化helper
	public ElementDAO(Context context){
		SqliteDBHandler.helper =new SmartSTBSQLiteOpenHelper(context);
	}
	
	public void insert(ElementMode bean){
		SqliteDBHandler.addElementinfo(bean);		
		if(bean.getActData()!=null&&bean.getActData().size()!=0){
			
			List<ActDataMode> list = bean.getActData();
			for (ActDataMode actData : list) {
				SqliteDBHandler.addActdata(actData, bean.getCode());				
			}
		}
		
	}
	
	public void insertActive(DeviceInfoMode deviceInfo,String box_id){
		
		SqliteDBHandler.addActiveInfo(deviceInfo,box_id);
		
	}
	
	public String getActive(){
		
		String box_id = SqliteDBHandler.getActive();
		return box_id;
		
	}
		
	public void update(ElementMode bean){
		ElementMode item = byCodeElementMode(bean.getCode());
		if(item!=null){
			SqliteDBHandler.updateElementMode(bean);
			if(bean.getActData()!=null&&bean.getActData().size()!=0){
				
				List<ActDataMode> list = bean.getActData();
				SqliteDBHandler.delete( bean.getCode());
				for (ActDataMode actData : list) {
					SqliteDBHandler.addActdata(actData, bean.getCode());
				}
			}
		}else{
			SqliteDBHandler.addElementinfo(bean);		
			if(bean.getActData()!=null&&bean.getActData().size()!=0){
				
				List<ActDataMode> list = bean.getActData();
				for (ActDataMode actData : list) {
					SqliteDBHandler.addActdata(actData, bean.getCode());				
				}
			}
		}
		
	}
	
	//桌面应用
	public ElementMode getDeskElementMode(){		
		List<ElementMode> list = SqliteDBHandler.getByCode("B10");
		ElementMode element= null;
		if(list.size()>0){
			element=list.get(0);
		}

		return element;
	}
	//企业应用
	public ElementMode getCompanyElementMode(){
		List<ElementMode> list = SqliteDBHandler.getByCode("B20");
		ElementMode element= null;
		if(list.size()>0){
			element=list.get(0);
		}

		return element;
	}
	//桌面内容
	public List<ElementMode> getDeskContentElementMode(){
		List<ElementMode> list = SqliteDBHandler.getAll();
		
		for (ElementMode elementMode : list) {
			List<ActDataMode> actList = SqliteDBHandler.getAllByWhere(elementMode.getCode());
			if(actList.size()>0){
				elementMode.setActData(actList);
			}
		}
		
		return list;
	}
	
	//企业内容
		public List<ElementMode> getComanyContentElementMode(){
			List<ElementMode> list = SqliteDBHandler.getAllCompany();
			
			for (ElementMode elementMode : list) {
				List<ActDataMode> actList = SqliteDBHandler.getAllByWhere(elementMode.getCode());
				if(actList.size()>0){
					elementMode.setActData(actList);
				}
			}
			return list;
		}
		
		//获取actdata中数据
		
		public ArrayList<ActDataMode> getAllByWhereActDataMode(String code) {
			
			ArrayList<ActDataMode> list = SqliteDBHandler.getAllByWhere(code);
			
			return list;
		}
		
		//bycode
		public ElementMode byCodeElementMode(String code){
			List<ElementMode> list = SqliteDBHandler.getByCode(code);
			ElementMode element= null;
			if(list.size()>0){
				element=list.get(0);
			}

			return element;
		}

		/*根据code更新软件版本*/
		public void updateVersionByCode(String version, String code) {
			SqliteDBHandler.updateVersionByCode(version,code);
		}
		
}
	