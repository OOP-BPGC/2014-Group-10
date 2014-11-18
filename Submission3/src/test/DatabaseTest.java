package test;


import static junit.framework.Assert.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import Database.MySQLAccess;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
public class DatabaseTest 
{
	
		    private Connection connect = null;
		    private Statement statement = null;
		    private PreparedStatement preparedStatement = null;
		    private ResultSet resultSet = null;
		    private ResultSetMetaData metaData = null;
	      //public MySQLCabAccess CabDbase = new MySQLCabAccess();
		    public Object[] entries;
		    public DatabaseTest(){
			   try {
		  	//load the MySQL driver
			    Class.forName("com.mysql.jdbc.Driver");
			    //setup connection with the DB.
			    connect = DriverManager.getConnection("jdbc:mysql://localhost/" +
			  	  	"resource_management?user=root&password=k9d3h3j");
			    //statements allow to issue SQL queries to the database
			    statement = connect.createStatement(
				  	  ResultSet.TYPE_SCROLL_INSENSITIVE, 
					    ResultSet.CONCUR_READ_ONLY);

		  }
		  
	  	    catch (ClassNotFoundException e) {
			    e.printStackTrace();
		  }
		    catch (SQLException e) {
			    e.printStackTrace();
		  }
	  }

 MySQLAccess m=new MySQLAccess();
 MySQLAccess.MySQLClassRoomAccess y=m.new MySQLClassRoomAccess();
 
 
 @Test
 public void testValidateUser()
 {
	 boolean b=m.validateUser("Ram","Ramayana");
	 assertTrue(b);
 }
 
 
 @Test 
 public void testUpdateDatabaseCab(){
 Time start = null,end = null;
 
   try {
	    m.CabDbase.updateDatabase("x", "r", "y",start, end);
       } catch (SQLException e) {
	                             e.printStackTrace();
                                } catch (Exception e) {
	                                                      e.printStackTrace();
	                                                  } 
   try {
        //resultSet gets the result of the SQL query
        resultSet = statement.executeQuery("select CabDriver from Cabs where"+
  	  	"UserName = " +"x" );
        resultSet.first();
        String Cabdriver = resultSet.getString("CabDriver");
	    assertEquals(Cabdriver,"y"); 
      } catch (SQLException e) 
      {
	    e.printStackTrace();
      }
}

 @Test 
 public void testUpdateDatabaseClassroom(){
 Time start = null,end = null;
 
  try {
	     y.updateDatabase("x", "", "",start, end);
      } catch (SQLException e) 
      {
	   e.printStackTrace();
      } catch (Exception e) {
	                         e.printStackTrace();
	                        } 
  try {
        String x = "Ram";
        //resultSet gets the result of the SQL query
        resultSet = statement.executeQuery("select Classroom from classroomlog where"+
  	  	"UserName = \"" + x + "\";");
        resultSet.first();
        String Classroom = resultSet.getString("Classroom");
	    assertEquals(Classroom,"C401"); 
      } catch (SQLException e) {
	                            e.printStackTrace();
                               }
}

 @Test 
 public void testSendCabDetails()
 {
	 Time start = new Time(16, 30, 0), end = new Time(20, 0, 0);
	 
		        try {
                                Object testarr[] = new Object[3];
                                testarr[0] = "Ramesh";
                                testarr[1] = "GA4756";
                                testarr[2] = "8770956643";
				Object arr[][]=  new Object[1][3];
				arr=m.CabDbase.sendCabDetails(start,end);
				assertEquals(arr[0][0],testarr[0]);
                                assertEquals(arr[0][1],testarr[1]);
                                assertEquals(arr[0][2],testarr[2]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                       
		
		}
 @Test
 public void testSendClassroomDetails()
 {
	 Time start = null,end = null;
	 String RoomNo="C405";
	 boolean b;
	        try {
	        
				Object arr=  new Object[1][7];
				arr=m.displayResultSet(1,7);
				assertEquals(arr,"");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 }

 @Test
 public void testDisplayResultSet()
 {
	 int bookingNo=3;
	        try {
	        
				Object arr=  new Object[1][7];
				arr=m.displayResultSet(1,7);
				assertEquals(arr,"");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	 
	
 }
 
 }
