package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteDB extends SQLiteOpenHelper
{
    // creating constant variables for our database
    //database name
    private static final String DB_Name="200644F.db";
    //db version
    private static final int DB_Version =1;
    //table name
    private static final String TABLE_ACCOUNT =" Accounts";
    private static final String TABLE_TRANSACTION="Transactions";

    //id column

    private static final String ACCOUNTNO="accountNo";
    private static final String NAME="accountholdername";
    private static final String BANK_NAME="bankname";
    private static final String BALANCE="balance";





    public SQLiteDB(Context context) {
        super(context,DB_Name,null,DB_Version);
    }
    //create database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String query ="CREATE TABLE "+TABLE_ACCOUNT+"(accountNo integer  ,accountholdername text,bankname text,balance real)";
        sqLiteDatabase.execSQL(query);

        String query1="CREATE TABLE " + TABLE_TRANSACTION + "(trans_id integer primary key autoincrement , date text,accountNo text,expense_type text,amount real,foreign key(accountNo) references Accounts(accountNo))";
        sqLiteDatabase.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        //use to check whether the databse already exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        onCreate(sqLiteDatabase);

    }




}
