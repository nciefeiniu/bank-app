package com.example.phonewallet11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatebaseHelper extends SQLiteOpenHelper {
    private static final String DB_name="zfb";
    private static final String table_user="User";
    private static final String table_kabao="Ka_bao";
    private static final String table_record="Record";
    private static final String table_contact="Friend";
    private static final String table_zixun="zixun";
    private static final String table_focus="focus";
    private static final String CREATE_TABLE1="create table User(_id varchar(20) primary key,account varchar(20) UNIQUE,name varchar(20),sex varchar(2),phone integer,qx varchar(10),login_password varchar(20),pay_password varchar(10), money float)";
    private static final String CREATE_TABLE2="create table Ka_bao(_id varchar(20),k_sfz integer ,k_phone integer)";
    private static final String CREATE_TABLE3="create table Record(_id varchar(20),r_name varchar(20),r_mode varchar(10),r_money float,r_time time)";
//    private static final String CREATE_TABLE3="create table Record(_id varchar(20),r_name varchar(20),r_mode varchar(10),r_money float,r_time time)";
    private static final String CREATE_TABLE4="create table Friend(_id varchar(20),f_id varchar(20),f_name varchar(20),f_phone integer,f_group  varchar(20))";
    private static final String CREATE_TABLE5="create table zixun(_id integer primary key autoincrement,title text,source text,pic text,neirong text,type text,data text)";
    private static final String CREATE_TABLE6="create table focus(username varchar(20)  ,focustitle text)";
    private SQLiteDatabase db;
    //	在新建类的构造方法中重写父类的构造方法，用于创建指定数据库


    public DatebaseHelper(Context context) {
        super(context, DB_name, null, 1);
        /* 调用父类的带四个参数的构造方法
         * 第一个参数：上下文，存放所在Activity对象的地址
         * 第二个参数：数据库文件名，该数据库所在位置
         * data/data/包名/databases
         * 第三个参数－CursorFactory factory：游标工厂类，该值设置为null即可。
         * 第四个参数：数据库版本号，初始值设置为1,表示第一版。	*/
        // TODO Auto-generated constructor stub
    }

    public DatebaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*重写父类的onCreate()方法，该方法用于创建数据库中的表。
        提示：只有在第一次创建数据库时，本方法才被调用，其它情况下不会再次创建表。*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
        db.execSQL(CREATE_TABLE4);
        db.execSQL(CREATE_TABLE5);
        db.execSQL(CREATE_TABLE6);
    }
    /* 重写父类的onUpgrade方法
         用于修改数据表的表结构，如增加一个新的字段。该方法在数据库版本更新时被调用。*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists"+table_user);
        db.execSQL("drop table if exists"+table_kabao);
        db.execSQL("drop table if exists"+table_record);
        db.execSQL("drop table if exists"+table_contact);
        db.execSQL("drop table if exists"+table_zixun);
        db.execSQL("drop table if exists"+table_focus);

    }

    public Cursor loginquery(String account){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.query(table_user, null, "account=?", new String[]{""+account}, null, null, null);
        return cursor;
    }
    public Cursor query(String e) {
        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_user,null, "_id=?",new String[]{e}, null, null, null);
        return s;

    }
    public int insert(ContentValues values){
        SQLiteDatabase db=getWritableDatabase();
        long count= db.insert(table_user, null, values);

        return (int)count;

    }

    public void delkb(int id){
        //删除数据库中满足id值的记录
        if (db==null)
            db=getWritableDatabase();
        db.delete(table_kabao,"_id=?",new  String[]{String.valueOf(id)});
    }

    public Cursor querykb(String sfz){
        //数据库的查询操作，返回Cursor对象
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query( table_kabao,null,"k_sfz =?",new String[]{String.valueOf(sfz)},null,null,null);
        return c;
    }
    public int insertCard(ContentValues values) {
        // TODO Auto-generated method stub
        SQLiteDatabase db=getWritableDatabase();
        long count= db.insert(table_kabao, null, values);
        return (int)count;
    }


    public int updatauesr(ContentValues values,String id){
        SQLiteDatabase db=getWritableDatabase();
        long count= db.update(table_user, values, "_id=?",  new String[]{id});

        return (int)count;

    }

    public Cursor querykb2(String sfz,String num){
        //数据库的查询操作，返回Cursor对象
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query( table_kabao,null,"k_sfz =? and _id =?",new String[]{sfz,num},null,null,null);
        return c;
    }
    public Cursor querykbb(String id){
        //数据库的查询操作，返回Cursor对象
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query( table_kabao,null,"_id =?",new String[]{String.valueOf(id)},null,null,null);
        return c;
    }
    public Cursor querypwd(String id){
        SQLiteDatabase db=getWritableDatabase();

        Cursor cursor=db.query(table_user,new String[]{"pay_password"},"_id=?", new String[]{id}, null, null, null);
        return cursor;
    }
    public Cursor querymoney(String id){
        SQLiteDatabase db=getWritableDatabase();

        Cursor cursor=db.query(table_user,new String[]{"money"},"_id=?", new String[]{id}, null, null, null);
        return cursor;
    }


    public void updateuser(ContentValues values,String sfz){
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_user, values, "_id=?",  new String[]{sfz});

    }



    public void updateUser(String idNum,String account,String name,String loginPassword,String payPassword,String phoneNum){
        ContentValues values = new ContentValues();
        values.put("account", account);
        values.put("name", name);
        values.put("login_password",loginPassword);
        values.put("pay_password",payPassword);
        values.put("phone",phoneNum);

        long ret = -1;
        do {
            ret = db.update(table_user, values, "_id=?",
                    new String[] { idNum });
        } while (ret < 0);
    }


    public Cursor queryf(String name,String phone){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.query(table_user, null, "name=? and phone=?", new String[]{name,phone}, null, null, null);
        return cursor;
    }




    public int insertFriend(ContentValues values) {
        // TODO Auto-generated method stub
        SQLiteDatabase db=getWritableDatabase();
        long count= db.insert(table_contact, null, values);
        return (int)count;
    }
    public Cursor queryConcat(String sfz){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor = db.query(table_contact, null, "f_id=?", new String[]{sfz}, null, null, null);
        return cursor;
    }
    public Cursor queryConcat(String sfz,String sfz2){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor = db.query(table_contact, null, "f_id=? and _id=?", new String[]{sfz,sfz2}, null, null, null);
        return cursor;
    }
    public Cursor queryff(String sfz){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.query(table_user, null, "_id=?", new String[]{sfz}, null, null, null);
        return cursor;
    }

    public void delf(int fid, String id){
        //删除数据库中满足id值的记录
        if (db==null)
            db=getWritableDatabase();
        db.delete(table_contact,"_id=? and f_id=?",new  String[]{String.valueOf(fid),id});
    }

    public Cursor queryzz(String phone,String name){
        //数据库的查询操作，返回Cursor对象
        SQLiteDatabase db=getWritableDatabase();
        Cursor c;
        c = db.query( table_user,null,"phone=? and name=?",new String[]{phone,name},null,null,null);
        return c;
    }
    public Cursor queryrecord(String id){
        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_record, null, "_id=?", new String[]{id}, null, null, null);
        return s;

    }
    public int insertRecord(ContentValues values){
        SQLiteDatabase db=getWritableDatabase();
        long count= db.insert(table_record, null, values);

        return (int)count;

    }


///////////////////////////////////////////


    public Cursor telquery(ContentValues values){
        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_user, new String[]{"tel,name,age,sex"}, "tel=?", new String[]{(String) values.get("tel")}, null, null, null);
        return s;

    }
    public Cursor bank_telquery(String tel,String bank){

        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_kabao, null, "tel=? and bankcard=?", new String[]{tel,bank}, null, null, null);
        return s;

    }
    public Cursor queryBytel(String tel){
        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_user, null, "tel=?", new String[]{""+tel}, null, null, null);
        return s;
    }



    public Cursor pwdquery(String tel){
        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_user, null, "tel=?",new String[]{""+tel}, null, null, null);
        return s;
    }

    public void updateLogin_pwd(ContentValues values,String id){
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_user, values, "id=?",  new String[]{id});
        //db.close();
    }

    public void updatePhone(ContentValues values,String id){
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_user, values, "id=?",  new String[]{id});
        //db.close();
    }
    public int insert_con(ContentValues values){
        SQLiteDatabase db=getWritableDatabase();
        long count= db.insert(table_contact, null, values);
        Log.v("dubug", "djjjjjjj");

        return (int)count;

    }

    public Cursor tel1query(String tel) {
        // TODO Auto-generated method stub
        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_kabao, null, "tel=?", new String[]{""+tel}, null, null, null);
        return s;
    }
    public void updatePay_pwd(ContentValues values,String tel){
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_user, values, "id=?",  new String[]{tel});

    }
    public void updateMoney(ContentValues values,String tel){
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_user, values, "tel=?",  new String[]{tel});

    }

    public boolean queryID(String id) {
        if (db == null){
            db = getWritableDatabase();
        }

        Cursor cursor = db.query(table_user, null, "_id=?", new String[]{id}, null, null, null);
        boolean b = cursor.moveToFirst();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public int del(String id) {
        if (db == null)
            db = getWritableDatabase();
        if (queryID(id)){
            db.delete(table_user, "_id=?", new String[]{id});
            return 1;
        }else {
            return 0;
        }

    }


    public Cursor queryByTel(String tel){


        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query(table_user,new String[]{"tel ,name ,age ,id ,sex ,login_password ,pay_password , money"}, "tel=?", new String[]{String.valueOf(tel)}, null, null, null);
        return c;

    }


    public Cursor bankquery(String tel) {
        // TODO Auto-generated method stub
        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_kabao,  new String[]{"_id,bankcard"}, "tel=?",new String[]{tel}, null, null, null);
        Log.v("����", "end��");
        return s;

    }
    public Cursor queryByName(String name){

        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query(table_user,new String[]{"tel ,name ,age ,id ,sex ,login_password ,pay_password , money"}, "name=?", new String[]{name}, null, null, null);
        return c;
    }
    public Cursor queryName(){

        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query(table_user,null, null,null, null, null, null);
        return c;
    }
    public void close(){
        if(db!=null)
            db.close();
    }




    public Cursor telquery(String tel) {
        // TODO Auto-generated method stub
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query(table_user,null, "tel=?", new String[]{tel}, null, null, null);
        Log.v("��������", "�ɹ��ˣ�");
        return c;
    }


    public void insert2(ContentValues values){

        SQLiteDatabase db=getWritableDatabase();
        db.insert(table_record, null, values);
        db.close();
    }
    public void del1(int id){
        if(db==null)
            db=getWritableDatabase();
        db.delete(table_contact, "_id=?", new String[]{String.valueOf(id)});
    }

    public void delBankcard(int id){
        if(db==null)
            db=getWritableDatabase();
        db.delete(table_kabao, "_id=?", new String[]{String.valueOf(id)});
    }
    public Cursor query1() {
        // TODO Auto-generated method stub
        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_contact,  null,null,null, null, null, null);
        return s;

    }

    public Cursor bankquery1() {
        // TODO Auto-generated method stub
        SQLiteDatabase db=getWritableDatabase();
        Cursor s=db.query(table_kabao,  null,null,null, null, null, null);
        return s;

    }
    public void update(ContentValues values,String name){
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_user, values, "name=?", new String[]{name});
    }
    public Cursor queryrecordbyname(String name){

        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query( table_record, new String[]{"_id,name,time,money,out_name"}, "name = ?", new String[]{name},null,null,null);
        return c;

    }
    public Cursor query2(){

        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query( table_record, null, null, null,null,null,null);
        return c;

    }

    public void del(int tel){

        if(db==null)
            db=getWritableDatabase();
        db.delete(table_record, "_id=?", new String[]{String.valueOf(tel)});
    }
    public Cursor namequery(String name){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.rawQuery("select * from User where name=?",new String[]{name});

        return c;

    }

    public Cursor queryuser(){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.rawQuery("select _id,account,name,login_password,pay_password,phone from User", null);
        return c;
    }












    public void insertzx(ContentValues values){
        SQLiteDatabase db=getWritableDatabase();
        db.insert(table_zixun, null, values);
    }
    public void focusinsert(ContentValues values){
        SQLiteDatabase db=getWritableDatabase();
        db.insert("focus", null, values);
    }
    public void delzx(String title){
        if(db==null)
            db=getWritableDatabase();
        db.delete(table_zixun, "title=?", new String[]{String.valueOf(title)});
    }
    public void delid(String _id){
        if(db==null)
            db=getWritableDatabase();
        db.delete(table_zixun, "_id=?", new String[]{String.valueOf(_id)});
    }
    public Cursor queryzx1(String type){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.rawQuery("select * from zixun where type=? order by data DESC", new String[]{String.valueOf(type)});
        return c;
    }
    public Cursor queryzx2(String type){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.rawQuery("select * from zixun where type=? order by data DESC", new String[]{String.valueOf(type)});
        return c;
    }
    public Cursor queryall(){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.rawQuery("select * from zixun order by data DESC", null);
        return c;
    }
    public Cursor queryzixun(String title){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.rawQuery("select * from zixun where title=? order by data DESC", new String[]{title});

        return c;
    }
    public Cursor queryzixunid(String _id){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.rawQuery("select * from zixun where _id=? order by data DESC", new String[]{_id});

        return c;
    }
    public Cursor focusquery(String name){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.rawQuery("select * from focus where username=?",new String[]{name});

        return c;

    }
    public void update(String title,String source,String pic,String neirong,String type,String data) {
        // TODO Auto-generated method stub
        ContentValues values=new ContentValues();


        values.put("source", source);
        values.put("pic", pic);
        values.put("neirong", neirong);
        values.put("type", type);
        values.put("data", data);
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_zixun, values, "title=?", new String[]{String.valueOf(title)});
    }
    public void updateid(int _id,String title,String source,String pic,String neirong,String type,String data) {
        // TODO Auto-generated method stub
        ContentValues values=new ContentValues();
        values.put("title", title);

        values.put("source", source);
        values.put("pic", pic);
        values.put("neirong", neirong);
        values.put("type", type);
        values.put("data", data);
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_zixun, values, "_id=?", new String[]{String.valueOf(_id)});
    }
    public void update1(String title,String source) {
        // TODO Auto-generated method stub
        ContentValues values=new ContentValues();
        values.put("source", source);
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_zixun, values, "title=?", new String[]{String.valueOf(title)});
    }
    public void update2(String title,String pic) {
        // TODO Auto-generated method stub
        ContentValues values=new ContentValues();
        values.put("pic", pic);
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_zixun, values, "title=?", new String[]{String.valueOf(title)});
    }
    public void update3(String title,String neirong) {
        // TODO Auto-generated method stub
        ContentValues values=new ContentValues();
        values.put("neirong", neirong);
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_zixun, values, "title=?", new String[]{String.valueOf(title)});
    }
    public void update4(String title,String type) {
        // TODO Auto-generated method stub
        ContentValues values=new ContentValues();
        values.put("type", type);
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_zixun, values, "title=?", new String[]{String.valueOf(title)});
    }
    public void update5(String title,String data) {
        // TODO Auto-generated method stub
        ContentValues values=new ContentValues();
        values.put("data", data);
        SQLiteDatabase db=getWritableDatabase();
        db.update(table_zixun, values, "title=?", new String[]{String.valueOf(title)});
    }
    public void deletefocus(String username,String title){
        SQLiteDatabase db=getWritableDatabase();
        db.delete("focus", "username=? and focustitle=?", new String[]{String.valueOf(username),String.valueOf(title)});
    }

}
