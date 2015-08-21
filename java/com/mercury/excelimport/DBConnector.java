package com.mercury.excelimport;

import com.mercury.excelimport.model.File;
import com.mercury.excelimport.model.FileRow;
import com.mercury.excelimport.model.SystemContract;
import com.mercury.excelimport.model.System;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.*;

/**
 * Created by Fedake on 2015-08-20.
 */
public class DBConnector
{
    private SessionFactory factory;

    private int faultyRowId = -1;

    public DBConnector()
    {
        this.factory = new Configuration().configure().buildSessionFactory();
    }


    // Ensures that system names from File objects exist in database as well, returns false when unknown system is found
    public boolean validateFile(File file)
    {
        Session s = this.factory.openSession();
        Transaction tx = null;

        try{
            tx = s.beginTransaction();
            List system = s.createQuery("FROM System").list();
            tx.commit();

            for(Iterator<FileRow> it = file.getRowsIterator(); it.hasNext();)
            {
                FileRow row = it.next();
                boolean valid = false;

                for(Iterator itSys = system.iterator(); itSys.hasNext();)
                {
                    System sys = (System)itSys.next();

                    if(sys.getName().equals(row.getSystem()))
                        valid = true;
                }

                if(!valid)
                {
                    return false;
                }
            }
        }
        catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        }

        return true;
    }

    // Handles dataset residing in File object
    public void handleFile(File file)
    {
        Iterator<FileRow> it = file.getRowsIterator();

        while(it.hasNext())
        {
            FileRow row = it.next();

            java.lang.System.out.println(row.getSystem());
            this.handleRow(row);
        }
    }

    // Handles single row from File object
    public void handleRow(FileRow row)
    {
        Session s = this.factory.openSession();
        Transaction tx = null;

        int sysId = -1;

        try{
            tx = s.beginTransaction();
            List system = s.createQuery("FROM System").list();



            for (Iterator iterator = system.iterator(); iterator.hasNext();)
            {
                System sys = (System) iterator.next();
                if(row.getSystem().equals(sys.getName())) {
                    sysId = sys.getId();
                    break;
                }

            }
            tx.commit();
        }
        catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }

        if(sysId != -1) {

            tx = null;
            try {
                tx = s.beginTransaction();
                SystemContract dbRow = new SystemContract(-1, row.isActive(), row.getAmount(),
                        row.getAmountPeriod(), row.getAmountType(), row.getAuthPercent(),
                        row.getFromDate(), row.getOrderNumber(), row.getRequest(), row.getToDate(), sysId);
                s.save(dbRow);
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null)
                    tx.rollback();

                e.printStackTrace();
            }
        }
        s.close();

    }

    public ArrayList<SystemContract> getContracts()
    {
        Session s = this.factory.openSession();
        Transaction tx = null;

        try {
            tx = s.beginTransaction();
            List contracts = s.createQuery("FROM SystemContract").list();
            tx.commit();

            ArrayList<SystemContract> result = new ArrayList<SystemContract>();

            for(Iterator it = contracts.iterator(); it.hasNext();)
            {
                result.add((SystemContract)it.next());
            }

            s.close();
            return result;
        }
        catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }

        s.close();
        return null;
    }
}
