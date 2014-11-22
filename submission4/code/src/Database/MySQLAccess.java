package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Date;
//import java.util.Date;


public class MySQLAccess {
    private Connection connect = null;
    private Statement statement = null;
    private Statement statement1 = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private ResultSet resultSet1 = null;
    private ResultSetMetaData metaData = null;
    public MySQLCabAccess CabDbase = new MySQLCabAccess();
    public MySQLClassRoomAccess ClassDbase = new MySQLClassRoomAccess();
    public Object entries[][]= new Object[10][10];
    
    public int BookingNo;
 
    
    public MySQLAccess() {
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
                    statement1 = connect.createStatement(
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
  
  
    public boolean validateUser(String username, String password) {
	    try {
		    //resultSet gets the result of the SQL query
		    resultSet = statement.executeQuery("select pwd from registered_users where"+
		  	  	" UserName = \"" +username+ "\";");
		    resultSet.first();
		    String truePwd = resultSet.getString("pwd");
//		    System.out.println(truePwd);
//		    System.out.println(password);
		  // make changes in GUI from here or return true;
		    if (password.equals(truePwd)) {
		        return true;
		    }
	  }
            
	    catch (SQLException e) {
		    e.printStackTrace();
	    }
	    return false;
  }
  
    
    public Object[][] displayAllCabBookings(String username) {
	    try {
	        resultSet = statement.executeQuery("select * from cablog where " +
		    		"UserName = \"" + username + "\";");
	        metaData = resultSet.getMetaData();
	        int RowCount = getRowCount();
		int ColumnCount = getColumnCount();
                resultSet.first();
		this.entries = displayResultSet(RowCount, ColumnCount);				
	  } catch (Exception e){
    	    e.printStackTrace();
        }
            return entries;
}

    public Object[][] displayAllClassRoomBookings(String username) {   
        try {
	        resultSet = statement.executeQuery("select * from classroomlog where " +
		    		"username = \"" + username + "\";");
	        metaData = resultSet.getMetaData();
	        int RowCount = getRowCount();
                int ColumnCount = getColumnCount();
                resultSet.first();
                entries=displayResultSet(RowCount, ColumnCount);

}
        catch (Exception e) {
            
        }
        return this.entries;
    }
    public Object[][] displayPendingBookings() {
        try {
                String status = "Pending";
                resultSet = statement.executeQuery("select * from admin where " + "status = \"" + status + "\";");
                metaData = resultSet.getMetaData();
                int RowCount = getRowCount();
                int ColumnCount = getColumnCount();
                this.entries = displayResultSet(RowCount, ColumnCount);				
      } catch (Exception e){
                e.printStackTrace();
      }
        return entries;
}
    
    public void approve(int S_No) {
     try {   
        String status = "Approved";
        preparedStatement = connect.prepareStatement("update admin set status = \"" + status + "\" where S_no = " + S_No + ";");
        preparedStatement.executeUpdate();
        preparedStatement = connect.prepareStatement("insert into classroomlog(username, roomNo, bookingDate, startTime, endTime, purpose)"
                + " select useraname, roomNo, bookingDate, startTime, endTime, purpose from admin "
                + "where S_No = " + S_No + ";");
        preparedStatement.executeUpdate();
    }
     catch (SQLException e) {
         
     }
    }
    public void rejectRequest(int S_No) throws SQLException {
        String status = "Rejected";
        preparedStatement = connect.prepareStatement("update admin set status = \"" + status + "\" where S_no = " + S_No + ";");
        preparedStatement.executeUpdate();
    }
    
    public void getBookingStatus(int bookingNo) throws Exception {
	    try {
	        resultSet = statement.executeQuery("select * from log where " +
		    		"BookingNo = \"" + bookingNo + "\";");
	        metaData = resultSet.getMetaData();
			int ColumnCount = getColumnCount();
	        displayResultSet(1, ColumnCount);
   	}   catch (SQLException e){
        	e.printStackTrace();
        }
  }
    
    public Object[][] displayResultSet(int RowCount, int ColumnCount) throws Exception{
	    entries = new Object[10][10];
	    resultSet.first();
		for (int i = 1; i <= RowCount; i++) {
			for  (int j = 1; j <= ColumnCount ; j++){
				this.entries[i-1][j-1] = getValueAt(resultSet, i, j);
//                              System.out.println(entries[i-1][j-1]);
			}
		}
            return this.entries;
    }

    public void cancel(int BookingNo, String table) {
     try {   
        System.out.println(BookingNo);
        preparedStatement = connect.prepareStatement("delete from " + table + " where BookingNo = " + BookingNo + ";");
        preparedStatement.executeUpdate();
//      statement.executeQuery("delete * from log where BookingNo = " + BookingNo + ";");
    }
     catch (SQLException e) {
         
     }
    }
    public int getRowCount() throws SQLException {
    	resultSet.last();
	    int rowCount = resultSet.getRow();
	    return rowCount;
    }

    public int getColumnCount() throws SQLException {
    	int ColumnCount = metaData.getColumnCount();
    	return ColumnCount;
    }
    
    public Object getValueAt(ResultSet resultSet, int i, int j) throws Exception {
	    resultSet.absolute(i);
	    return resultSet.getObject(j);
    }
    
    
    public class MySQLClassRoomAccess {
    	
    	public boolean sendClassRoomDetails(String BookingDate, Time StartingService, Time EndingService, String RoomNo) throws Exception {
                
		    Date d1 = new Date(Integer.parseInt(BookingDate.substring(6,10))-1900, Integer.parseInt(BookingDate.substring(3,5))-1,
                    Integer.parseInt(BookingDate.substring(0,2)));
                    
                    String status = "Available";
		    resultSet = statement.executeQuery("select * from classroomlog " +
		  	  	"where roomNo = \""+ RoomNo + "\";");
		    metaData = resultSet.getMetaData();
                    boolean a = true;
                    while (resultSet.next()) {
                        System.out.println(resultSet.getDate("bookingDate").toString() + d1.toString());
                        if (resultSet.getDate("bookingDate").equals(d1)) {
                            System.out.println("1");
                            a = false;
                            if((resultSet.getTime("endTime").compareTo(StartingService) < 0) ||
					 		 (resultSet.getTime("startTime").compareTo(EndingService) > 0)) {
                                return true;
                            }
                            else { return false; }
                        }
                    }
                    if (a) {
                        return true;
                    }
                    else { return false; }

        }
		
	   
        public void updateAdminDatabase(String username, String ClassRoom, String BookingDate, Time StartingTime, Time EndingTime, String purpose, String requirements) throws Exception {
		    Date d1 = new Date(Integer.parseInt(BookingDate.substring(6,10))-1900, Integer.parseInt(BookingDate.substring(3,5))-1,
                    Integer.parseInt(BookingDate.substring(0,2)));
                    String status = "Pending";
                    preparedStatement = connect.prepareStatement("insert into admin values (default, ?, ?, ?, ?, ?, ?, ?, ?);");
		    preparedStatement.setString(1, username);
		    preparedStatement.setString(2, ClassRoom);
		    preparedStatement.setDate(3, d1);
		    preparedStatement.setTime(4, StartingTime);
		    preparedStatement.setTime(5, EndingTime);
                    preparedStatement.setString(6, purpose);
                    preparedStatement.setString(7, requirements);
                    preparedStatement.setString(8, "Pending");
		    preparedStatement.executeUpdate();
	  }
		    
	}
    
    public class MySQLCabAccess {
    	
	    public Object[][] sendCabDetails(String BookingDate, Time StartingService, Time EndingService) throws Exception {
                    Date d1 = new Date(Integer.parseInt(BookingDate.substring(6,10))-1900, Integer.parseInt(BookingDate.substring(3,5))-1,
                    Integer.parseInt(BookingDate.substring(0,2)));
		    entries = new Object[10][10];
                    String status = "Available";   
                    resultSet = statement.executeQuery("select * from cablog " +
		  	  	"where BookingDate = \"" + d1 +"\";");
		    metaData = resultSet.getMetaData();
                    resultSet1 = statement1.executeQuery("select * from cabs;");
                    
                    while (resultSet1.next()) {
                        resultSet.beforeFirst();
                        boolean a = true;
                        while (resultSet.next()) {
      //                      System.out.println(resultSet1.getString("CabDriver") + " " + resultSet.getString("CabDriver"));
      //                      System.out.println(resultSet1.getString("CabDriver").equals(resultSet.getString("CabDriver")));
                            if (resultSet1.getString("CabDriver").equals(resultSet.getString("CabDriver"))) {
      //                          System.out.println("0");
                                a = false;
                                break;
                            }
                        }
                        if (a) {
      //                      System.out.println("1");
                            entries[0][0] = resultSet1.getString("CabDriver");
                            entries[0][1] = resultSet1.getString("CabNo");
                            entries[0][2] = resultSet1.getString("ContactNo");
                            return entries;
                        }
                    }

                    resultSet.beforeFirst();
//                       resultSet = statement.executeQuery("select * from cablog " +
//		  	  	"where BookingDate = \"" + d1 +"\";");
                    while (resultSet.next()) {
 //                           System.out.println(resultSet.getTime("StartingBookedTime") + " " +
 //                                   resultSet.getTime("EndingBookedTime")+ " " + 
 //                                   EndingService + " " +
 //                                       StartingService + " " +
 //                                      resultSet.getTime("StartingBookedTime").compareTo(StartingService)+ " " +
 //                                       resultSet.getTime("EndingBookedTime").compareTo(StartingService)+ " " +
 //                                       resultSet.getTime("StartingBookedTime").compareTo(EndingService)+ " " +
 //                                       resultSet.getTime("EndingBookedTime").compareTo(EndingService));
	            if ((resultSet.getTime("EndService").compareTo(StartingService) < 0) ||
					 		 (resultSet.getTime("StartService").compareTo(EndingService) > 0)) {
 //                                       System.out.println(ColumnCount);
//                                        System.out.println(i);
				    	entries[0][0] = resultSet.getString("CabDriver");
                                        entries[0][1] = resultSet.getString("CabNo");
                                        entries[0][2] = resultSet.getString("ContactNo");
//                                        System.out.println(entries[0][0] + " "+ entries[0][1] +" "+ entries[0][2]);
				    	return entries;
                                }
                    }
               return entries;
            }

        public void updateDatabase(String username, String CabDriver, String CabNo, String ContactNo, String BookingDate, Time StartingService, Time EndingService, String Destination)throws Exception {
                    Date d1 = new Date(Integer.parseInt(BookingDate.substring(6,10))-1900, Integer.parseInt(BookingDate.substring(3,5))-1,
                    Integer.parseInt(BookingDate.substring(0,2)));
                    preparedStatement = connect.prepareStatement("insert into cablog values(default,?,?,?,?,?,?,?,?);");
//            preparedStatement = connect.prepareStatement("insert into log" +
//		  	  	"values (4, \"" + username + "\", " + CabDriver + "\", " + CabNo + "\", " + StartingService
//                  + "\", " + EndingService + "\", " + "Vasco");
		    preparedStatement.setString(1, username);
		    preparedStatement.setString(2, CabDriver);
		    preparedStatement.setString(3, CabNo);
                    preparedStatement.setString(4, ContactNo);
                    preparedStatement.setDate(5, d1);
		    preparedStatement.setTime(6, StartingService);
		    preparedStatement.setTime(7, EndingService);
                    preparedStatement.setString(8, Destination);
		    preparedStatement.executeUpdate();
        }
	}

  	 
	    
	    public static void main(String args[]) throws Exception {
	    	MySQLAccess SQLAccess = new MySQLAccess();
//	    	boolean a = SQLAccess.validateUser("Ram", "Ramayana");
//	    	System.out.println(a);
//	    	SQLAccess.displayAllCabBookings("Ram");
//	    	SQLAccess.getBookingStatus(2);
	    	Time t1 = new Time(11, 30, 0);
	    	Time t2 = new Time(12, 0, 0);
//	    	SQLAccess.CabDbase.sendCabDetails(t1, t2);
//                SQLAccess.CabDbase.updateDatabase("Shyam", "Ramesh", "GA4756", t1, t2);
//                SQLAccess.cancel(7, "classroomlog");
                Date d1 = new Date(2014-1900, 11-1, 24);
//                SQLAccess.CabDbase.updateDatabase("Ram", "RajuRam", "GA5763", "24-11-2014", t1, t2, "Airport");
 //               DateFormat df = new SimpleDateFormat("24 11 2014");
 //               System.out.println(d1.toString());
//                boolean b = SQLAccess.ClassDbase.sendClassRoomDetails("24-11-2014", t1, t2, "C-401");
 //               System.out.println(b);
      //          SQLAccess.entries = SQLAccess.CabDbase.sendCabDetails("24-11-2014", t1, t2);
      //          System.out.println(SQLAccess.entries[0][0].toString()+SQLAccess.entries[0][1].toString()+SQLAccess.entries[0][2].toString());
//                SQLAccess.approveRequest(1);
//                SQLAccess.rejectRequest(2);
//               SQLAccess.cancel(4, "cablog");
//                Object entries[][]= new Object[10][10];
                SQLAccess.entries = SQLAccess.displayPendingBookings();
                System.out.println(SQLAccess.entries[0][1].toString());
                System.out.println(SQLAccess.entries[0][2].toString());
            }
  }
