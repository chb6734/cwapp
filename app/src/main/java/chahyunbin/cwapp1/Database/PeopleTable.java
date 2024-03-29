package chahyunbin.cwapp1.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import chahyunbin.cwapp1.model.Person;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;


public class PeopleTable extends DatabaseHelper.BaseTable {
    protected static PeopleTable instance;
    final String TAG = "BookDatabase";

    // 테이블
    public final static String TABLE_NAME = "people";

    // 필드
    public static final String ID = _ID;
    public static final String NAME = "name";
    public static final String PHONENUMBER = "phonenumber";
    public static final String AGE ="age";
    public static final String BIRTHDAY = "birthday";


    private static final String ORDER_BY_DEFAULT = _ID + " asc";
    private static final String ORDER_BY_DEFAULT_DESC = _ID + " desc";

    private static final String[] COLUMNS = { _ID,NAME, PHONENUMBER, AGE, BIRTHDAY};

    private static final String WHERE_BY_ID = _ID + "=?";

    public static final String createSql = "CREATE TABLE if not exists " + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, " + NAME + " text," + PHONENUMBER+ " text, " + AGE+" text, "+  BIRTHDAY + " text);";

    public synchronized static PeopleTable instance(Context context) {
        if (instance == null) {
            synchronized (PeopleTable.class) {
                if (instance == null)
                    instance = new PeopleTable(context);
            }
        }
        return instance;
    }

    private PeopleTable(Context context){
        super(context);
    }

    public int insert(String name, String phonenumber, String age, String birthday) {
        Log.d(TAG, "3");

        ContentValues values = new ContentValues();

        values.put(NAME, name.trim());
        values.put(PHONENUMBER, phonenumber.trim());
        values.put(AGE, age.trim());
        values.put(BIRTHDAY, birthday.trim());

        db().insertOrThrow(TABLE_NAME, null, values);
        Log.d(TAG, "insert: "+super.insert());
        return super.insert();
    }
    private Person makeBean(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(ID));
        String name = cursor.getString(cursor.getColumnIndex(NAME));
        String phonenumber = cursor.getString(cursor.getColumnIndex(PHONENUMBER));
        String age = cursor.getString(cursor.getColumnIndex(AGE));
        String birthDay = cursor.getString(cursor.getColumnIndex(BIRTHDAY));
        return new Person(id, name, phonenumber,age, birthDay);
    }

    public ArrayList<Person> loadByDate(boolean isDesc) {
        ArrayList<Person> result = new ArrayList<>();

        String order = ORDER_BY_DEFAULT;
        if(isDesc)
            order = ORDER_BY_DEFAULT_DESC;
        Cursor c = db().query(TABLE_NAME,null,null,null,null,null, order);
        if(c.getCount() == 0)
            return result;
        c.moveToFirst();
        while (!c.isAfterLast()) {
            result.add(makeBean(c));
            c.moveToNext();
        }
        c.close();
        return result;
    }

    public static int deleteById(int id) {

        String[] whereArgs = {id + ""};
        return db().delete(TABLE_NAME, WHERE_BY_ID, whereArgs);
    }

    public String call(int position){
        Cursor c = db().rawQuery("select * from "+TABLE_NAME, null);
       c.moveToPosition(position);
        String phonenumber = "tel:"+ c.getString(c.getColumnIndex(PHONENUMBER));

        return phonenumber;
    }




}
