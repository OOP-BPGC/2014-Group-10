package Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;


public class MySQLAccess {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private ResultSetMetaData metaData = null;
    public MySQLCabAccess CabDbase = new MySQLCabAccess();
    public MySQLClassRoomAccess ClassDbase = new MySQLClassRoomAccess();
    public Object entries[][]= new Object[10][10];
    public Object entries1[][];
    
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
		    System.out.println(truePwd);
		    System.out.println(password);
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
  
    
    public void displayAllCabBookings(String username) {
	    try {
	        resultSet = statement.executeQuery("select * from log where " +
		    		"UserName = \"" + username + "\";");
	        metaData = resultSet.getMetaData();
	        int RowCount = getRowCount();
		int ColumnCount = getColumnCount();
	        displayResultSet(RowCount, ColumnCount);
	  } catch (Exception e){
    	    e.printStackTrace();
        }
}

    public Object[][] displayAllClassRoomBookings(String username) throws Exception {   
        
	        resultSet = statement.executeQuery("select * from classroomlog where " +
		    		"UserName = \"" + username + "\";");
	        metaData = resultSet.getMetaData();
	        int RowCount = getRowCount();
			int ColumnCount = getColumnCount();
	      this.entries1=displayResultSet(RowCount, ColumnCount);
	
        return this.entries1;
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
//	    entries = new Object[10][10];
	    resultSet.first();
		for (int i = 1; i <= RowCount; i++) {
			for  (int j = 1; j <= ColumnCount ; j++){
				this.entries[i-1][j-1] = getValueAt(resultSet, i, j);
				
                                System.out.println(this.entries[i-1][j-1]);
			}
		}
        return this.entries;
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
    	
    	public boolean sendClassRoomDetails(Time StartingTime, Time EndingTime, String RoomNo) throws Exception {
		    String status = "Available";
		    resultSet = statement.executeQuery("select * from Rooms " +
		  	  	"where classRoom = "+ RoomNo);
		    metaData = resultSet.getMetaData();
		    if (resultSet.getString("Status") == status) {
		    	int RowCount = getRowCount();
		    	int ColumnCount = getColumnCount();
		    	displayResultSet(RowCount, ColumnCount);
		    	return true;
		    }
		    else {
			    return false;
		    }
		}
	   
        public void updateDatabase(String username, String ClassRoom, String purpose, Time StartingTime, Time EndingTime) throws Exception {
		    preparedStatement = connect.prepareStatement("insert into log" +
		  	  	"values (default, ?, ?, ?, ?, ?)");
		    preparedStatement.setString(1, username);
		    preparedStatement.setString(2, ClassRoom);
		    preparedStatement.setString(3, purpose);
		    preparedStatement.setTime(4, StartingTime);
		    preparedStatement.setTime(5, EndingTime);
		    preparedStatement.executeUpdate();
		    resultSet = statement.executeQuery("select BookingNo from log where UserName = " + username);
		    BookingNo = resultSet.getInt("BookingNo");
	  }
		    
	}
    
    
    public class MySQLCabAccess {
    	
	    public boolean sendCabDetails(Time StartingService, Time EndingService) throws Exception {
		    String status = "Available";
		    resultSet = statement.executeQuery("select * from cabs " +
		  	  	"where status = \""+ status + "\";");
		    metaData = resultSet.getMetaData();
		    
		    if (!resultSet.wasNull()) {
		    	int ColumnCount = getColumnCount();
		    	displayResultSet(1, ColumnCount);
		    	return true;
		    }
		    else {
			    resultSet = statement.executeQuery("select * from cabs " +
			    		"where BookedTime == null");
			    while (resultSet.next()) {
			    	if ((resultSet.getTime("StartingBookedTime").compareTo(StartingService) < 0 
					  	&& resultSet.getTime("EndingBookedTime").compareTo(StartingService) < 0) ||
					 		 (resultSet.getTime("StartingBookedTime").compareTo(EndingService) > 0 
								 && resultSet.getTime("EndingBookedTime").compareTo(EndingService) > 0)) {
			    		int ColumnCount = getColumnCount();
				    	displayResultSet(1, ColumnCount);
				    	return true;
			    	}
			    }
		    }
		    return false;
//		  int rowCount = getRowCount(resultSet);
		    
            }

        public void updateDatabase(String username, String CabDriver, String CabNo, Time StartingService, Time EndingService)throws Exception {
            preparedStatement = connect.prepareStatement("insert into log" +
		  	  	"values (default, ?, ?, ?, ?, ?)");
		    preparedStatement.setString(1, username);
		    preparedStatement.setString(2, CabDriver);
		    preparedStatement.setString(3, CabNo);
		    preparedStatement.setTime(4, StartingService);
		    preparedStatement.setTime(5, EndingService);
		    preparedStatement.executeUpdate();
		    resultSet = statement.executeQuery("select BookingNo from log where UserName = " + username);
		    BookingNo = resultSet.getInt("BookingNo");
        }
	}

  	 
	    
	    public static void main(String args[]) throws Exception {
	    	MySQLAccess SQLAccess = new MySQLAccess();
//	    	boolean a = SQLAccess.validateUser("Ram", "Ramayana");
//	    	System.out.println(a);
	    	SQLAccess.displayAllCabBookings("Ram");
//	    	SQLAccess.getBookingStatus(2);
//	    	Time t1 = new Time(1);
//	    	Time t2 = new Time(3);
//	    	SQLAccess.CabDbase.sendCabDetails(t1, t2);
                
                
	   }
  }
