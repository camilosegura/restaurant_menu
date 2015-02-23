package co.camilosegura.restaurantmenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.camilosegura.restaurantmenu.activities.MainMenu;
import co.camilosegura.restaurantmenu.util.Conexion;
import co.camilosegura.restaurantmenu.util.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity {

	// Splash screen timer
	private static final int SPLASH_TIME_OUT = 3000;
	private static final String INIT_DATA = "{'id':'25672332-5666-2fdd-e53a-f6f3a0dad43e','name':'archiesCategories','timestamp':1378937981795,'requests':[{'collectionId':'25672332-5666-2fdd-e53a-f6f3a0dad43e','id':'1084d1d2-6294-bffd-8b87-641428f5abc2','name':'Category','description':'','url':'http://192.237.180.31/archies/public/category/details/1','method':'GET','headers':'','data':[{'key':'type','value':'1','type':'text'},{'key':'msg','value':'Alo alo','type':'text'},{'key':'uuid','value':'84c33c754f9a588be9c8b13bb78cf739e062c50b19299b9d31ff848757cab55c','type':'text'}],'dataMode':'params','timestamp':0,'version':2},{'collectionId':'25672332-5666-2fdd-e53a-f6f3a0dad43e','id':'268b578f-7d3f-5b37-a1ea-d91c69188ea7','name':'Categories','description':'','url':'http://192.237.180.31/archies/public/category','method':'GET','headers':'','data':[{'key':'type','value':'1','type':'text'},{'key':'msg','value':'Alo alo','type':'text'},{'key':'uuid','value':'84c33c754f9a588be9c8b13bb78cf739e062c50b19299b9d31ff848757cab55c','type':'text'}],'dataMode':'params','timestamp':0,'version':2}]}";
	private static final String URL_SUBCATEGORY = "http://192.237.180.31/archies/public/subcategory/details/";
	private static final String URL_PUBLIC = "http://192.237.180.31/archies/public/";

	private MainApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		this.app = (MainApplication) getApplication();

		this.getMenuData();

		
	}

	/**
	 * 
	 */
	private void getMenuData() {
		new MenuDataTask().execute();
	}

	/**
	 * 
	 * @author Camilo Segura
	 *
	 */
	private class MenuDataTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(Utilities.isInternetConnectionAvaible(getApplicationContext())){
				getCategories();
			}else{
				
			}
			

			return true;
		}

		protected void onPostExecute(Boolean result) {
	         Intent intent = new Intent(getApplicationContext(), MainMenu.class);
	         startActivity(intent);
	     }

	}

	private void getCategories() {
		try {
			JSONObject jsonCategory = null;
			int id = 0;
			String createdAt = "";
			String updatedAt = "";
			String imgPath = "";
			int typeId = 0;
			int enabled = 0;
			String name = "";
			String imgUrl = "";

			JSONObject jsonInit = new JSONObject(INIT_DATA);
			String response = Conexion.get(jsonInit.getJSONArray("requests")
					.getJSONObject(1).getString("url"));
			JSONArray jsonCategories = new JSONArray(response);

			int catLength = jsonCategories.length();

			this.deleteCategories(jsonCategories);

			for (int i = 0; i < catLength; i++) {

				jsonCategory = jsonCategories.getJSONObject(i);
				id = jsonCategory.getInt("id");
				updatedAt = jsonCategory.getString("updated_at");

				if (!app.getDataBase().isUpdatedCategory(id, updatedAt)) {
					app.getDataBase().deleteCategory(id);
					name = jsonCategory.getString("name");
					createdAt = jsonCategory.getString("updated_at");
					imgPath = jsonCategory.getString("img_path");
					imgUrl =  URL_PUBLIC + imgPath;
					imgPath = Conexion.downloadImage(imgUrl, imgPath);

					app.getDataBase().createCategory(id, name, imgPath, typeId,
							enabled, createdAt, updatedAt);
				}
			}

			this.getSubCategories(jsonCategories);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void deleteCategories(JSONArray jsonCategories) {
		String id = "";
		JSONObject jsonCategory = null;
		int catLength = jsonCategories.length();
		String inIds = "";
		try {
			for (int i = 0; i < catLength; i++) {

				jsonCategory = jsonCategories.getJSONObject(i);
				id = jsonCategory.getString("id");

				inIds += id + ",";
			}

			if (!inIds.equals("")) {
				inIds = inIds.substring(0, inIds.length() - 1);
				app.getDataBase().deleteRemovedCategory(inIds);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getSubCategories(JSONArray jsonCategories) {
		int id = 0;
		int idCategory = 0;
		String createdAt = "";
		String updatedAt = "";
		String name = "";
		int additionEnable = 0;
		int enabled = 0;
		JSONArray jsonSubCategories = null;
		JSONObject jsonSubCategory = null;
		int catLength = jsonCategories.length();
		int subCatLength = 0;

		this.deleteSubCategories(jsonCategories);

		try {
			for (int i = 0; i < catLength; i++) {

				idCategory = jsonCategories.getJSONObject(i).getInt("id");
				jsonSubCategories = jsonCategories.getJSONObject(i)
						.getJSONArray("subcategory");
				subCatLength = jsonSubCategories.length();
				for (int j = 0; j < subCatLength; j++) {
					jsonSubCategory = jsonSubCategories.getJSONObject(j);
					id = jsonSubCategory.getInt("id");
					updatedAt = jsonSubCategory.getString("updated_at");

					if (!app.getDataBase().isUpdatedSubCategory(id, updatedAt)) {
						app.getDataBase().deleteSubCategory(id);
						name = jsonSubCategory.getString("name");
						createdAt = jsonSubCategory.getString("updated_at");
						additionEnable = jsonSubCategory
								.getInt("addition_enable");
						enabled = jsonSubCategory.getInt("enabled");
						app.getDataBase().createSubCategory(id, name,
								idCategory, additionEnable, enabled, createdAt,
								updatedAt);
					}

					this.getItems(id);

				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteSubCategories(JSONArray jsonCategories) {
		String id = "";
		JSONArray jsonSubCategories = null;
		JSONObject jsonSubCategory = null;
		int catLength = jsonCategories.length();
		int subCatLength = 0;
		String inIds = "";
		try {
			for (int i = 0; i < catLength; i++) {

				jsonSubCategories = jsonCategories.getJSONObject(i)
						.getJSONArray("subcategory");
				subCatLength = jsonSubCategories.length();
				for (int j = 0; j < subCatLength; j++) {
					jsonSubCategory = jsonSubCategories.getJSONObject(j);
					id = jsonSubCategory.getString("id");

					inIds += id + ",";
				}
			}

			if (!inIds.equals("")) {
				inIds = inIds.substring(0, inIds.length() - 1);
				app.getDataBase().deleteRemovedSubCategory(inIds);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getItems(int idSubCategory) {

		JSONObject jsonItem = null;
		int itemsLenght = 0;
		int id = 0;
		String createdAt = "";
		String updatedAt = "";
		String name = "";
		String imgPath = "";
		String leftImgPath = "";
		String rightImgPath = "";
		int enabled = 0;
		String description = "";
		String imgUrl = "";

		try {

			String response = Conexion.get(URL_SUBCATEGORY + idSubCategory);

			JSONObject jsonSubCategory = new JSONObject(response);
			JSONArray jsonItems = jsonSubCategory.getJSONArray("items");
			 
			itemsLenght = jsonItems.length();

			deleteItems(idSubCategory, jsonItems);
			
			for (int i = 0; i < itemsLenght; i++) {
				
				jsonItem = jsonItems.getJSONObject(i);
				
				id = jsonItem.getInt("id");
				
				updatedAt = jsonItem.getString("updated_at");
				
				
				if (!app.getDataBase().isUpdatedItem(id, updatedAt)) {
					
					createdAt = jsonItem.getString("created_at");
					name = jsonItem.getString("name");
					imgPath = jsonItem.getString("img_path");
					leftImgPath = jsonItem.getString("left_img_path");
					rightImgPath = jsonItem.getString("right_img_path");
					enabled = jsonItem.getInt("enabled");
					description = jsonItem.getString("description");
					
					imgUrl =  URL_PUBLIC + imgPath;
					imgPath = Conexion.downloadImage(imgUrl, imgPath);
					
					app.getDataBase().deleteItem(id);
					app.getDataBase().createItem(id, name, description,
							imgPath, leftImgPath, rightImgPath, idSubCategory,
							enabled, createdAt, updatedAt);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deleteItems(int idSubCategory, JSONArray jsonItems) {
		JSONObject jsonItem = null;
		int itemsLenght = 0;
		int id = 0;
		String inIds = "";
		try {
			itemsLenght = jsonItems.length();

			for (int i = 0; i < itemsLenght; i++) {
				
				jsonItem = jsonItems.getJSONObject(i);
				
				id = jsonItem.getInt("id");
				
				inIds += id + ",";
			}

			if (!inIds.equals("")) {
				inIds = inIds.substring(0, inIds.length() - 1);
				app.getDataBase().deleteRemovedItem(idSubCategory, inIds);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
