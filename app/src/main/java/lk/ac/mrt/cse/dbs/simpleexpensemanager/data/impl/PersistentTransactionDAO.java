package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.SQLiteDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO  implements TransactionDAO
{

    private  List<Transaction>transactions;
    private final SQLiteDB db;

    public PersistentTransactionDAO(SQLiteDB sqLiteDB) {
        this.db=sqLiteDB;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount)
    {
        SQLiteDatabase database=this.db.getWritableDatabase();

        if (expenseType==ExpenseType.EXPENSE){
            PersistentAccountDAO persistantAccountDAO=new PersistentAccountDAO(this.db);
            try {
                Account accountHolder=persistantAccountDAO.getAccount(accountNo);
                if (accountHolder.getBalance()<amount){
                    return;
                }
            } catch (Exception e) {
                System.out.println("Invalid account number!");
            }

        }
        ContentValues values=new ContentValues();
        values.put("date", String.valueOf(date));
        values.put("accountNo",accountNo);
        values.put("expense_type", String.valueOf(expenseType));
        values.put("amount",amount);
        database.insert("Transactions",null,values);
        database.close();


    }

    @Override
    public List<Transaction> getAllTransactionLogs()  {
        SQLiteDatabase database=this.db.getReadableDatabase();

        Cursor cursor=database.rawQuery("select * from Transactions ",null);
        ArrayList<Transaction>transactions=new ArrayList<>();
        if (cursor.getCount()!=0){
            while(cursor.moveToNext()){
                String accountNo=cursor.getString(cursor.getColumnIndex("accountNo"));
                String date=cursor.getString(cursor.getColumnIndex("date"));
                double amount=cursor.getDouble(cursor.getColumnIndex("amount"));
                String expense_type=cursor.getString(cursor.getColumnIndex("expense_type"));

                Date date1=StringDate(date);
                Transaction transaction=new Transaction(date1,accountNo,Strexpense(expense_type),amount);
                transactions.add(transaction);

            }
        }
        cursor.close();

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit)  {
        transactions=getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);

    }

    public  Date StringDate(String sdate)  {
        SimpleDateFormat dateFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date=new Date();

        try {
            date=dateFormat.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date;

    }

    public ExpenseType Strexpense(String expenseType){
        String expensetype=expenseType.toUpperCase();
        if (expensetype.equals("EXPENSE")){
            return ExpenseType.EXPENSE;
        }
        else  {
            return ExpenseType.INCOME;
        }
    }

}
