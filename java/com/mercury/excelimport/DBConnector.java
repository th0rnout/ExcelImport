package com.mercury.excelimport;

import com.mercury.excelimport.model.File;
import com.mercury.excelimport.model.FileRow;
import com.mercury.excelimport.model.SystemContract;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by Fedake on 2015-08-20.
 */
public class DBConnector
{
    private SessionFactory factory;

    public DBConnector()
    {
        this.factory = new Configuration().configure().buildSessionFactory();
    }

    // Handles dataset residing in File object
    public void handleFile(File file)
    {
        Iterator<FileRow> it = file.getRowsIterator();

        while(it.hasNext())
        {
            FileRow row = it.next();
            this.handleRow(row);
        }
    }

    // Handles single row from File object
    public void handleRow(FileRow row)
    {
        Session s = this.factory.openSession();
        Transaction tx = null;

        try
        {
            tx = s.beginTransaction();
            SystemContract dbRow = new SystemContract(0, true, 40.2f, "test", "test", 99, new Date(), "numer", "12", new Date(), 1);
            s.save(dbRow);
            tx.commit();
            s.close();
        }
        catch(HibernateException e)
        {
            if(tx != null)
                tx.rollback();

            e.printStackTrace();
        }
    }
}
