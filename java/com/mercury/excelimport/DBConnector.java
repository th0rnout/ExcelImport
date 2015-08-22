package com.mercury.excelimport;

import com.mercury.excelimport.model.*;
import com.mercury.excelimport.model.System;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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
        for(Iterator<FileRow> it = file.getRowsIterator(); it.hasNext();)
        {
            FileRow row = it.next();

            if(!validateRow(row))
            {
                return false;
            }
        }

        return true;
    }

    public boolean validateRow(FileRow row)
    {
        return this.getSystem(row.getSystem()) != null;
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

        System sys = this.getSystem(row.getSystem());

        if(sys != null)
        {
            try {
                tx = s.beginTransaction();
                SystemContract dbRow = new SystemContract(row.getContractId(), row.isActive(), row.getAmount(),
                        row.getAmountPeriod(), row.getAmountType(), row.getAuthPercent(),
                        row.getFromDate(), row.getOrderNumber(), row.getRequest(), row.getToDate(), sys.getId());
                s.saveOrUpdate(dbRow);
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

    // Gets System object by name
    public System getSystem(String name)
    {
        Session s = this.factory.openSession();
        Transaction tx = null;

        try{
            tx = s.beginTransaction();
            Query q = s.createQuery("FROM System WHERE name = :name");
            q.setParameter("name", name);

            List systems = q.list();
            tx.commit();

            for(Iterator itSys = systems.iterator(); itSys.hasNext();)
            {
                s.close();
                return (System)itSys.next();
            }
        }
        catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        }

        s.close();
        return null;
    }

    // Gets System object by id
    public System getSystem(int id)
    {
        Session s = this.factory.openSession();
        Transaction tx = null;

        try{
            tx = s.beginTransaction();
            Query q = s.createQuery("FROM System WHERE id = :id");
            q.setParameter("id", id);

            List systems = q.list();
            tx.commit();

            for(Iterator itSys = systems.iterator(); itSys.hasNext();)
            {
                s.close();
                return (System)itSys.next();
            }
        }
        catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        }

        s.close();
        return null;

    }

    public void deleteRow(int id)
    {
        Session s = this.factory.openSession();
        String query = "DELETE FROM SystemContract WHERE id = :id";
        Query q = s.createQuery(query);
        q.setParameter("id", id);
        int i = q.executeUpdate();
        s.close();

        java.lang.System.out.println("Rows affected: " + Integer.toString(i));
    }
}
