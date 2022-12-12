package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.Duration;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.SQLiteDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistantAccountDAO  implements AccountDAO
{



    ArrayList<Account>accounts=new ArrayList<>();
    private final SQLiteDB db;


    public PersistantAccountDAO(SQLiteDB sqLiteDB) {

        this.accounts=new ArrayList<>();
        this.db=sqLiteDB;

    }



    @Override
    public List<String> getAccountNumbersList()
    {
        SQLiteDatabase database=this.db.getWritableDatabase();
        Cursor cursorAccount=database.rawQuery("SELECT accountNo FROM Accounts"
                ,null);
        ArrayList<String>AccountNoList=new ArrayList<>();
        if (cursorAccount.moveToFirst()){
            do {

                AccountNoList.add(cursorAccount.getString(0));

            }while (cursorAccount.moveToNext());
        }

        cursorAccount.close();
        database.close();

        return AccountNoList;
    }

    @Override
    public List<Account> getAccountsList()
    {
        SQLiteDatabase database=this.db.getReadableDatabase();
        Cursor cursorAccount=database.rawQuery("SELECT * FROM Accounts"
                ,null);
        ArrayList<Account>AccountList=new ArrayList<>();
        if (cursorAccount.moveToFirst()){
            do {
                AccountList.add(new Account(
                        cursorAccount.getString(0),
                        cursorAccount.getString(1),
                        cursorAccount.getString(2),
                        cursorAccount.getDouble(3)));
            }while (cursorAccount.moveToNext());
        }
        //Account lastAccount=AccountList.get(AccountList.size()-1);
        cursorAccount.close();
        database.close();
        return AccountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException
    {
        SQLiteDatabase database=this.db.getReadableDatabase();
        Cursor cursorAccount=database.rawQuery("SELECT accountNo,acc_name,bank_name,balance FROM Accounts"
                +"WHERE acc_number="+accountNo,null);
        ArrayList<Account>AccountList=new ArrayList<>();
        if (cursorAccount.moveToFirst()){
            do {
                AccountList.add(new Account(cursorAccount.getString(1),cursorAccount.getString(2),
                        cursorAccount.getString(3),cursorAccount.getDouble(4)));
            }while (cursorAccount.moveToNext());
        }
        Account lastAccount=AccountList.get(AccountList.size()-1);
        cursorAccount.close();
        return lastAccount;


    }

    @Override
    public void addAccount(Account account)
    {
        SQLiteDatabase database=this.db.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("accountNo",account.getAccountNo());
        values.put("accountholdername",account.getAccountHolderName());
        values.put("bankname",account.getBankName());
        values.put("balance",account.getBalance());
        database.insert("Accounts",null,values);

        database.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException
    {
        SQLiteDatabase database=this.db.getWritableDatabase();
        database.delete("Accounts","accountNo=?",new String[]{accountNo});
        database.close();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount)

            throws InvalidAccountException
    {
        SQLiteDatabase databse=this.db.getWritableDatabase();
        String[] projection={
                "balance"
        };

        String selection="accountNo"+" =? ";
        String[] selectionargs={ accountNo };

        Cursor cursor= databse.query(
                "Accounts",
                projection,
                selection,
                selectionargs,
                null,
                null,
                null);

        double balance;
        if(cursor.moveToFirst())
            balance=cursor.getDouble(0);
        else{
            String msg="Accounts "+ accountNo + " is invalid ";
            throw new InvalidAccountException(msg);
        }

        ContentValues values=new ContentValues();
        switch (expenseType){
            case EXPENSE:
                values.put("balance",balance-amount);
            case INCOME:
                values.put("balance",balance+amount);
                break;
        }
        databse.update("Accounts",values,"accountNo=?",new String[]{ accountNo });
        cursor.close();
        databse.close();

//
 }
}
