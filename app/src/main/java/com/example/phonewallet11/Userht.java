package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Userht extends Activity {
    private EditText id,account,name,login_password,pay_password,phone;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userht);

        id=(EditText) findViewById(R.id.id);
        account=(EditText) findViewById(R.id.account);
        name=(EditText) findViewById(R.id.name);
        login_password=(EditText) findViewById(R.id.login_password);
        pay_password=(EditText) findViewById(R.id.pay_password);
        phone=(EditText) findViewById(R.id.phone);
        tv = (TextView) findViewById(R.id.list);
		/*etbirth= (EditText) findViewById(R.id.editText);
		type= (RadioGroup) findViewById(R.id.radiogroup);
		//日期的监听
		etbirth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Calendar cl= Calendar.getInstance();
				int year = cl.get(Calendar.YEAR);
				int month = cl.get(Calendar.MONTH);
				int date = cl.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dp=new DatePickerDialog(Userht.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
						etbirth.setText(i+"-"+(i1+1)+"-"+i2);
					}
				},year,month,date);
				dp.setMessage("请选择发布日期");
				dp.show();
			}
		});*/
    }

    public void btread(View view){

        tv.setText("");
        DatebaseHelper helper=new DatebaseHelper(Userht.this);
        Cursor c=helper.queryuser();
        while(c.moveToNext()){
            String id =c.getString(c.getColumnIndex("_id"));
            String account =c.getString(c.getColumnIndex("account"));
            String name=c.getString(c.getColumnIndex("name"));
            String login_password =c.getString(c.getColumnIndex("login_password"));
            String pay_password=c.getString(c.getColumnIndex("pay_password"));
            String phone=c.getString(c.getColumnIndex("phone"));
            //String data=c.getString(c.getColumnIndex("data"));
            String tmp=tv.getText().toString();
            tv.setText(tmp+"\n"+"身份证号："+id+"\n" +"账号："+ account+"\n" +"姓名："+ name + "\n" +"登录密码："+ login_password+ "\n"+"支付密码："+pay_password+"\n"+"手机号码："+phone+"\n");
            Toast.makeText(Userht.this, "读取成功", Toast.LENGTH_SHORT).show();
        }
    }
    public void btupdate(View view) {

        String idNum = id.getText().toString().trim();
        String accountName = account.getText().toString().trim();
        String strName = name.getText().toString().trim();
        String strLoginPassword = login_password.getText().toString().trim();
        String strPayPassword = pay_password.getText().toString().trim();
        String strPhoneNum = phone.getText().toString().trim();

        if (idNum.isEmpty()||accountName.isEmpty()||strName.isEmpty()||strLoginPassword.isEmpty()||strPayPassword.isEmpty()||strPhoneNum.isEmpty()){
            Toast.makeText(this,"请输入所有信息",Toast.LENGTH_SHORT).show();
        }else {
            DatebaseHelper helper=new DatebaseHelper(Userht.this);
            if (helper.queryID(idNum)){
                helper.updateUser(idNum,accountName,strName,strLoginPassword,strPayPassword,strPhoneNum);
                id.setText("");
                account.setText("");
                name.setText("");
                login_password.setText("");
                pay_password.setText("");
                phone.setText("");
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"没有此用户",Toast.LENGTH_SHORT).show();
                id.setText("");
            }



        }



		/*DatabaseHelper helper = new DatabaseHelper(Userht.this);
		String t = id.getText().toString();
		final ContentValues values = new ContentValues();

		values.put("name", name.getText().toString());
		values.put("account", account.getText().toString());
		values.put("login_password", login_password.getText().toString());
		values.put("pay_password", pay_password.getText().toString());
		values.put("phone", phone.getText().toString());

		if (t.equals("")) {
			Toast.makeText(getApplicationContext(), "请输入身份证号码和要修改的数据！", Toast.LENGTH_SHORT).show();
		} else {
			if ((account.equals(""))||(name.equals(""))||(login_password.equals(""))||(phone.equals(""))){
				Toast.makeText(getApplicationContext(), "请填写完整的数据", Toast.LENGTH_SHORT).show();
			} else {
				helper.updateuser(values,t);
				Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
			}
		}
*/

    }





    public void btdelete(View view){
        String t=id.getText().toString();
        if(!t.equals("")){
            DatebaseHelper helper=new DatebaseHelper(Userht.this);
            int del = helper.del(t);
            if (del == 0){
                Toast.makeText(Userht.this, "不存在此用户!", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(Userht.this, "删除成功!", Toast.LENGTH_SHORT).show();

            }
            id.setText("");
        }else {
            Toast.makeText(Userht.this, "请输入要删除的用户身份证号", Toast.LENGTH_SHORT).show();
        }

    }
	/*public void btchakan(View view){


	}*/

    public void bttuichu(View view){

        Intent aa=new Intent(Userht.this,MainActivity.class);
        startActivity(aa);
    }

    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.userht, menu);
        return true;
    }

}
