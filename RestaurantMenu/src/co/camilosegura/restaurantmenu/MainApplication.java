package co.camilosegura.restaurantmenu;

import co.camilosegura.restaurantmenu.database.DataBase;
import android.app.Application;

public class MainApplication extends Application{
	/**
	 * 
	 */
	private DataBase dataBase;
	
    @Override
    public void onCreate(){
    	super.onCreate();
    	
    	dataBase = new DataBase(getApplicationContext());    	
    	
    }
    /**
     * 
     * @return
     */
    public DataBase getDataBase(){
    	if(!dataBase.isOpen()){
    		dataBase.open();
    	}
    	
    	return dataBase;
    }
}
