package com.mercury.excelimport.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Fedake on 2015-08-20.
 */
public class File
{
    ArrayList<FileRow> rows;

    public File()
    {
        this.rows = new ArrayList<FileRow>();
    }

    public void addRow(FileRow row)
    {
        this.rows.add(row);
    }

    public void clearRows() { this.rows.clear(); }

    public Iterator<FileRow> getRowsIterator()
    {
        return this.rows.iterator();
    }
}
