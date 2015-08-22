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

    private ArrayList<System> systems = new ArrayList<System>();

    private int faultyRowId = -1;

    public DBConnector()
    {
        this.factory = new Configuration().configure().buildSessionFactory();

        Session s = this.factory.openSession();
        Transaction tx = null;

        try{
            tx = s.beginTransaction();
            List systems = s.createQuery("FROM System").list();
            tx.commit();

            for(Iterator itSys = systems.iterator(); itSys.hasNext();)
            {
                System sys = (System)itSys.next();

                this.systems.add(sys);
            }
        }
        catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        }
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
                SystemContract dbRow = new SystemContract(-1, row.isActive(), row.getAmount(),
                        row.getAmountPeriod(), row.getAmountType(), row.getAuthPercent(),
                        row.getFromDate(), row.getOrderNumber(), row.getRequest(), row.getToDate(), sys.getId());
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

    // Gets System object by name
    public System getSystem(String name)
    {
        for(Iterator<System> itSys = this.systems.iterator(); itSys.hasNext();)
        {
            System sys = itSys.next();

            if(sys.getName().equals(name))
                return sys;
        }

        return null;
    }

    // Gets System object by id
    public System getSystem(int id)
    {
        for(Iterator<System> itSys = this.systems.iterator(); itSys.hasNext();)
        {
            System sys = itSys.next();

            if(sys.getId() == id)
                return sys;
        }

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
