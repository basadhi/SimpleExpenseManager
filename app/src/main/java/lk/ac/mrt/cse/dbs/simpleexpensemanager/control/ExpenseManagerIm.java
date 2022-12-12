package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.SQLiteDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class ExpenseManagerIm extends ExpenseManager
{
    private PersistantAccountDAO persistantAccountDAO;
    private Context context;
    private SQLiteDB db;

    public ExpenseManagerIm(@Nullable Context context)
    {
        this.db=new SQLiteDB(context);
        try{
            setup();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void setup() throws ExpenseManagerException
    {
        TransactionDAO transactionDAO=new PersistentTransactionDAO(this.db);
        setTransactionsDAO(transactionDAO);
        AccountDAO accountDAO=new PersistantAccountDAO(this.db);
        setAccountsDAO(accountDAO);

    }
}
