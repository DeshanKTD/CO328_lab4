/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ac.pdn.co328.studentSystem.dbimplementation;

import java.sql.*;
import java.util.ArrayList;
import lk.ac.pdn.co328.studentSystem.Student;
import lk.ac.pdn.co328.studentSystem.StudentRegister;

/**
 *
 * @author himesh
 */
public class DerbyStudentRegister extends StudentRegister {

    Connection connection = null;
    public DerbyStudentRegister() throws SQLException
    {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            String dbURL1 = "jdbc:derby:studentDB;create=true";
            connection = DriverManager.getConnection(dbURL1);
            if (connection != null)
            {
                String SQL_CreateTable = "CREATE TABLE Students(id INT , fname VARCHAR(24), lname VARCHAR (24))";
                System.out.println ( "Creating table addresses..." );
                try 
                {
                    Statement stmnt = connection.createStatement();
                    stmnt.execute( SQL_CreateTable );
                    stmnt.close();
                    System.out.println("Table created");
                } catch ( SQLException e )
                {
                    System.out.println(e);
                }
               System.out.println("Connected to database");
            }
            else
            {
                throw new SQLException("Connection Failed");
            }
    }
    
    @Override
    public void addStudent(Student st) throws Exception {
        if (connection != null)
        {
            String SQL_AddStudent = "INSERT INTO Students VALUES (" + st.getId() + ",'" + st.getFirstName() +"','"
                    + st.getLastName() +  "')";
            System.out.println ( "Adding the student..." + SQL_AddStudent);

            Statement stmnt = connection.createStatement();
            stmnt.execute(SQL_AddStudent );
            stmnt.close();
            System.out.println("Student Added");

        }
        else
        {
            throw new Exception("Database Connection Error");
        }
    }

    @Override
    public void removeStudent(int regNo) throws Exception {
        if (connection != null) {
            String SQL_DeleteStudent = "DELETE FROM Students WHERE id=" + regNo;
            System.out.println("Deleting student from DB");

            Statement stmnt = connection.createStatement();
            stmnt.execute(SQL_DeleteStudent);
            stmnt.close();
            System.out.println("Student Deleted");

        } else
        {
            throw new Exception("Database Connection Error");
        }
    }

    @Override
    public Student findStudent(int regNo) throws Exception{
        Student student = null;
        if (connection !=null){
            String SQL_FindStudent = "SELECT * FROM Students WHERE id=" + regNo;
            System.out.println("Searching student from DB");

            Statement stmnt = connection.createStatement();
            ResultSet result =  stmnt.executeQuery(SQL_FindStudent);
            if(result.next()){
                student = new Student(result.getInt("id"),result.getString("fname"),result.getString("lname"));

            }
            stmnt.close();
            System.out.println("Student Search finished");

        }
        else {
            throw new Exception("Database Connection Error");
        }
        return student;
    }


    @Override
    public void reset() throws Exception{
        if(connection !=null){
            String SQL_Reset = "TRUNCATE  Students";
            System.out.println("Clearing DB");

            Statement stmnt = connection.createStatement();
            stmnt.execute(SQL_Reset);
            System.out.println("Database clearing finished");

        }
        else{
            throw new Exception("Database Connection Error");
        }
    }

    @Override
    public ArrayList<Student> findStudentsByName(String name) throws Exception{
        if(connection !=null){
            ArrayList<Student> reg = new ArrayList<Student>();

            String SQL_STNames_1 = "SELECT * FROM Students WHEHRE fname='"+name+"'";
            String SQL_STNames_2 = "SELECT * FROM Students WHEHRE lname='"+name+"'";
            System.out.println("Searching for students");

            Statement stmnt = connection.createStatement();
            ResultSet res1 =  stmnt.executeQuery(SQL_STNames_1);
            ResultSet res2 =  stmnt.executeQuery(SQL_STNames_2);

            while(res1.next()){
                Student student = new Student(res1.getInt("id"),res1.getString("fname"),res1.getString("lname"));
                reg.add(student);
            }
            while(res2.next()){
                Student student = new Student(res2.getInt("id"),res2.getString("fname"),res2.getString("lname"));
                reg.add(student);
            }
            System.out.println("Searching Students finished");

            return  reg;

        }
        else{
            throw new Exception("Database Connection Error");
        }
    }

    @Override
    public ArrayList<Integer> getAllRegistrationNumbers() throws Exception{
        if(connection != null){
            ArrayList<Integer> reg = new ArrayList<Integer>();

            String SQL_RegNumbers = "SELECT id FROM Students";
            System.out.println("Getting Student IDs from DB");

            Statement stmnt = connection.createStatement();
            ResultSet res =  stmnt.executeQuery(SQL_RegNumbers);

            while(res.next()){
                reg.add(res.getInt("id"));
            }
            System.out.println("Finished searching IDs");

            return reg;
        }
        else{
            throw new Exception("Database Connection Error");
        }
    }
    
}
