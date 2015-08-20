package com.mercury.excelimport;

/**
 * Created by Daniel on 2015-08-20.
 */
public class Person
{
    private String name;
    private int age;

    public Person(String name, int age)
    {
        this.name = name;
        this.age = age;
    }
    public String getName() { return name; }
    public int getAge() { return age; }
}