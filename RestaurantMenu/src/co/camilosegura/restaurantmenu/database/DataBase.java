package co.camilosegura.restaurantmenu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "restaurant";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_CATEGORY = "category";
	private static final String TABLE_SUBCATEGORY = "subcategory";
	private static final String TABLE_ITEM = "item";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_ENABLED = "enabled";
	private static final String KEY_CREATED_AT = "created_at";
	private static final String KEY_UPDATED_AT = "updated_at";
	private static final String KEY_IMG_PATH = "img_path";

	private static final String KEY_CATEGORY_ID = KEY_ID;
	private static final String KEY_CATEGORY_NAME = KEY_NAME;
	private static final String KEY_CATEGORY_IMG_PATH = KEY_IMG_PATH;
	private static final String KEY_CATEGORY_TYPE_ID = "type_id";
	private static final String KEY_CATEGORY_ENABLED = KEY_ENABLED;
	private static final String KEY_CATEGORY_CREATED_AT = KEY_CREATED_AT;
	private static final String KEY_CATEGORY_UPDATED_AT = KEY_UPDATED_AT;

	private static final String KEY_SUBCATEGORY_ID = KEY_ID;
	private static final String KEY_SUBCATEGORY_NAME = KEY_NAME;
	private static final String KEY_SUBCATEGORY_CATEGORY_ID = "category_id";
	private static final String KEY_SUBCATEGORY_ADDITION_ENABLE = "addition_enable";
	private static final String KEY_SUBCATEGORY_ENABLED = KEY_ENABLED;
	private static final String KEY_SUBCATEGORY_CREATED_AT = KEY_CREATED_AT;
	private static final String KEY_SUBCATEGORY_UPDATED_AT = KEY_UPDATED_AT;

	private static final String KEY_ITEM_ID = KEY_ID;
	private static final String KEY_ITEM_NAME = KEY_NAME;
	private static final String KEY_ITEM_DESCRIPTION = "description";
	private static final String KEY_ITEM_IMG_PATH = KEY_IMG_PATH;
	private static final String KEY_ITEM_LEFT_IMG_PATH = "left_img_path";
	private static final String KEY_ITEM_RIGHT_IMG_PATH = "right_img_path";
	private static final String KEY_ITEM_SUBCATEGORY_ID = "subcategory_id";
	private static final String KEY_ITEM_ENABLED = KEY_ENABLED;
	private static final String KEY_ITEM_CREATED_AT = KEY_CREATED_AT;
	private static final String KEY_ITEM_UPDATED_AT = KEY_UPDATED_AT;

	private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
			+ TABLE_CATEGORY + "(" + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_CATEGORY_NAME + " TEXT," + KEY_CATEGORY_IMG_PATH + " TEXT,"
			+ KEY_CATEGORY_TYPE_ID + " INTEGER," + KEY_CATEGORY_ENABLED
			+ " INTEGER," + KEY_CATEGORY_CREATED_AT + " TEXT,"
			+ KEY_CATEGORY_UPDATED_AT + " TEXT);";

	private static final String CREATE_TABLE_SUBCATEGORY = "CREATE TABLE "
			+ TABLE_SUBCATEGORY + " (" + KEY_SUBCATEGORY_ID
			+ " INTEGER PRIMARY KEY," + KEY_SUBCATEGORY_NAME + " TEXT,"
			+ KEY_SUBCATEGORY_CATEGORY_ID + " INTEGER,"
			+ KEY_SUBCATEGORY_ADDITION_ENABLE + " INTEGER,"
			+ KEY_SUBCATEGORY_ENABLED + " INTEGER,"
			+ KEY_SUBCATEGORY_CREATED_AT + " TEXT,"
			+ KEY_SUBCATEGORY_UPDATED_AT + " TEXT);";

	private static final String CREATE_TABLE_ITEM = "CREATE TABLE "
			+ TABLE_ITEM + " (" + KEY_ITEM_ID + " INTEGER," + KEY_ITEM_NAME
			+ " TEXT," + KEY_ITEM_DESCRIPTION + " TEXT," + KEY_ITEM_IMG_PATH
			+ " TEXT," + KEY_ITEM_LEFT_IMG_PATH + " TEXT,"
			+ KEY_ITEM_RIGHT_IMG_PATH + " TEXT," + KEY_ITEM_SUBCATEGORY_ID
			+ " INTEGER," + KEY_ITEM_ENABLED + " INTEGER,"
			+ KEY_ITEM_CREATED_AT + " TEXT," + KEY_ITEM_UPDATED_AT + " TEXT);";

	private static final String DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS "
			+ TABLE_CATEGORY;
	private static final String DROP_TABLE_SUBCATEGORY = "DROP TABLE IF EXISTS "
			+ TABLE_SUBCATEGORY;
	private static final String DROP_TABLE_ITEM = "DROP TABLE IF EXISTS "
			+ TABLE_ITEM;

	private SQLiteDatabase mDb;

	/**
	 * 
	 * @param context
	 */
	public DataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_CATEGORY);
		db.execSQL(CREATE_TABLE_SUBCATEGORY);
		db.execSQL(CREATE_TABLE_ITEM);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(DROP_TABLE_CATEGORY);
		db.execSQL(DROP_TABLE_SUBCATEGORY);
		db.execSQL(DROP_TABLE_ITEM);
	}

	/**
	 * 
	 */
	public void open() {
		mDb = getWritableDatabase();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isOpen() {
		if (mDb == null) {
			return false;
		}
		return mDb.isOpen();
	}

	/**
	 * 
	 */
	public void deleteAllCategory() {
		this.deleteAllTable(TABLE_CATEGORY);
	}

	/**
	 * 
	 */
	public void deleteAllSubCategory() {
		this.deleteAllTable(TABLE_SUBCATEGORY);
	}

	/**
	 * 
	 */
	public void deleteAllItem() {
		this.deleteAllTable(TABLE_ITEM);
	}

	/**
	 * 
	 * @param table
	 */
	private void deleteAllTable(String table) {
		mDb.delete(table, null, null);
	}

	/**
	 * 
	 * @param id
	 */
	public void deleteCategory(int id) {
		this.deleteTable(TABLE_CATEGORY, KEY_ID, String.valueOf(id));
	}

	/**
	 * 
	 * @param id
	 */
	public void deleteSubCategory(int id) {
		this.deleteTable(TABLE_SUBCATEGORY, KEY_ID, String.valueOf(id));
	}

	/**
	 * 
	 * @param id
	 */
	public void deleteItem(int id) {
		this.deleteTable(TABLE_ITEM, KEY_ID, String.valueOf(id));
	}

	/**
	 * 
	 * @param table
	 * @param id
	 */
	private void deleteTable(String table, String key, String val) {
		String where = key + " = ?";
		String[] whereArgs = new String[] { val };
		mDb.delete(table, where, whereArgs);
	}

	/**
	 * 
	 * @param inIds
	 */
	public void deleteRemovedCategory(String inIds) {
		String idCategory = "";
		String idSubCategory = "";
		Cursor cSubCategory = null;
		Cursor cCategory = mDb.rawQuery("SELECT * FROM " + TABLE_CATEGORY
				+ " WHERE " + KEY_ID + " NOT IN (" + inIds + ")", null);

		if (cCategory.moveToFirst()) {
			do {
				idCategory = cCategory.getString(cCategory
						.getColumnIndex(KEY_ID));
				cSubCategory = mDb.rawQuery("SELECT * FROM "
						+ TABLE_SUBCATEGORY + " WHERE "
						+ KEY_SUBCATEGORY_CATEGORY_ID + " = " + idCategory,
						null);

				deleteTable(TABLE_SUBCATEGORY, KEY_SUBCATEGORY_CATEGORY_ID,
						idCategory);

				if (cSubCategory.moveToFirst()) {
					do {
						idSubCategory = cSubCategory.getString(cSubCategory
								.getColumnIndex(KEY_ID));

						deleteTable(TABLE_ITEM, KEY_ITEM_SUBCATEGORY_ID,
								idSubCategory);

					} while (cSubCategory.moveToNext());
				}
			} while (cCategory.moveToNext());
		}

		this.deleteRemovedTable(TABLE_CATEGORY, inIds);
	}

	/**
	 * 
	 * @param inIds
	 */
	public void deleteRemovedSubCategory(String inIds) {
		String idSubCategory = "";
		Cursor cSubCategory = mDb.rawQuery("SELECT * FROM " + TABLE_SUBCATEGORY
				+ " WHERE " + KEY_ID + " NOT IN (" + inIds + ")", null);

		if (cSubCategory.moveToFirst()) {
			do {
				idSubCategory = cSubCategory.getString(cSubCategory
						.getColumnIndex(KEY_ID));

				deleteTable(TABLE_ITEM, KEY_ITEM_SUBCATEGORY_ID, idSubCategory);

			} while (cSubCategory.moveToNext());
		}

		this.deleteRemovedTable(TABLE_SUBCATEGORY, inIds);
	}

	/**
	 * 
	 * @param inIds
	 */
	public void deleteRemovedItem(int idSubCategory, String inIds) {
		mDb.rawQuery("DELETE FROM " + TABLE_ITEM + " WHERE " + KEY_ID + " NOT IN ("
				+ inIds + " AND " + KEY_ITEM_SUBCATEGORY_ID + " = " + idSubCategory + ")", null);
	}

	/**
	 * 
	 * @param table
	 * @param inIds
	 */
	private void deleteRemovedTable(String table, String inIds) {

		mDb.rawQuery("DELETE FROM " + table + " WHERE " + KEY_ID + " NOT IN ("
				+ inIds + ")", null);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param imgPath
	 * @param typeId
	 * @param enabled
	 * @param createdAt
	 * @param updatedAt
	 * @return
	 */
	public long createCategory(int id, String name, String imgPath, int typeId,
			int enabled, String createdAt, String updatedAt) {
		ContentValues newVal = new ContentValues();
		newVal.put(KEY_CATEGORY_ID, id);
		newVal.put(KEY_CATEGORY_NAME, name);
		newVal.put(KEY_CATEGORY_IMG_PATH, imgPath);
		newVal.put(KEY_CATEGORY_TYPE_ID, typeId);
		newVal.put(KEY_CATEGORY_ENABLED, enabled);
		newVal.put(KEY_CATEGORY_CREATED_AT, createdAt);
		newVal.put(KEY_CATEGORY_UPDATED_AT, updatedAt);

		return mDb.insert(TABLE_CATEGORY, null, newVal);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param categoryId
	 * @param additionEnable
	 * @param enabled
	 * @param createdAt
	 * @param updatedAt
	 * @return
	 */
	public long createSubCategory(int id, String name, int categoryId,
			int additionEnable, int enabled, String createdAt, String updatedAt) {
		ContentValues newVal = new ContentValues();
		newVal.put(KEY_SUBCATEGORY_ID, id);
		newVal.put(KEY_SUBCATEGORY_NAME, name);
		newVal.put(KEY_SUBCATEGORY_CATEGORY_ID, categoryId);
		newVal.put(KEY_SUBCATEGORY_ADDITION_ENABLE, additionEnable);
		newVal.put(KEY_SUBCATEGORY_ENABLED, enabled);
		newVal.put(KEY_SUBCATEGORY_CREATED_AT, createdAt);
		newVal.put(KEY_SUBCATEGORY_UPDATED_AT, updatedAt);

		return mDb.insert(TABLE_SUBCATEGORY, null, newVal);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param imgPath
	 * @param leftImgPath
	 * @param rightImgPath
	 * @param subCategoryId
	 * @param enabled
	 * @param createdAt
	 * @param updatedAt
	 * @return
	 */
	public long createItem(int id, String name, String description,
			String imgPath, String leftImgPath, String rightImgPath,
			int subCategoryId, int enabled, String createdAt, String updatedAt) {
		ContentValues newVal = new ContentValues();
		newVal.put(KEY_ITEM_ID, id);
		newVal.put(KEY_ITEM_NAME, name);
		newVal.put(KEY_ITEM_DESCRIPTION, description);
		newVal.put(KEY_ITEM_IMG_PATH, imgPath);
		newVal.put(KEY_ITEM_LEFT_IMG_PATH, leftImgPath);
		newVal.put(KEY_ITEM_RIGHT_IMG_PATH, rightImgPath);
		newVal.put(KEY_ITEM_SUBCATEGORY_ID, subCategoryId);
		newVal.put(KEY_ITEM_ENABLED, enabled);
		newVal.put(KEY_ITEM_CREATED_AT, createdAt);
		newVal.put(KEY_ITEM_UPDATED_AT, updatedAt);

		return mDb.insert(TABLE_ITEM, null, newVal);
	}

	/**
	 * 
	 * @param id
	 * @param updatedAt
	 * @return
	 */
	public boolean isUpdatedCategory(int id, String updatedAt) {
		return isUpdated(TABLE_CATEGORY, id, updatedAt);
	}

	/**
	 * 
	 * @param id
	 * @param updatedAt
	 * @return
	 */
	public boolean isUpdatedSubCategory(int id, String updatedAt) {
		return isUpdated(TABLE_SUBCATEGORY, id, updatedAt);
	}

	/**
	 * 
	 * @param id
	 * @param updatedAt
	 * @return
	 */
	public boolean isUpdatedItem(int id, String updatedAt) {
		return isUpdated(TABLE_ITEM, id, updatedAt);
	}

	/**
	 * 
	 * @param table
	 * @param id
	 * @param updatedAt
	 * @return
	 */
	private boolean isUpdated(String table, int id, String updatedAt) {

		String[] whereArgs = new String[] { String.valueOf(id) };

		String lastUpdatedAt = "";

		Cursor cUpdated = mDb.rawQuery("SELECT " + KEY_UPDATED_AT + " FROM "
				+ table + " WHERE " + KEY_ID + " = ?", whereArgs);
		if (cUpdated.moveToFirst()) {
			lastUpdatedAt = cUpdated.getString(cUpdated
					.getColumnIndex(KEY_UPDATED_AT));
			if (lastUpdatedAt.equals(updatedAt)) {
				return true;
			}
		}

		return false;
	}
	
	
}
