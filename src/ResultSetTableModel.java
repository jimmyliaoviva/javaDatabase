import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel {
	private final Connection connection;
	private final Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;
	
	private boolean connectedToDatabase = false;
	
	public ResultSetTableModel(String url,String username,String password,String query) throws SQLException{
		connection = DriverManager.getConnection(url,username,password);
		
		statement = connection.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		
		connectedToDatabase = true;
		
		setQuery(query);
		
		
	}//end constructor
	
	public Class getColumnClass(int column) throws IllegalStateException
	{
		if(!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		
		try {
			String className = metaData.getColumnClassName(column+1);
			return Class.forName(className);
		}//end try
		catch(Exception exception) {
			exception.getStackTrace();
		}//end catch
		
		return Object.class;
	}//end class getColumnClass
	
	public int getColumnCount() throws IllegalStateException{
		
		if(!connectedToDatabase){
			throw new IllegalStateException("Not connected to database");
			
		}//end if
		
		try {
			return metaData.getColumnCount();
		}//end try
		catch(SQLException sqlException) {
			sqlException.printStackTrace();
		}//end catch
		
		return 0;
	}//end getColumnCount
	
	public String getColumnName(int column) throws IllegalStateException{
		if(!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		
		try
		{
			return metaData.getColumnName(column+1);
		}//end try 
		catch(SQLException sqlException) {
			 sqlException.printStackTrace();
		}//end catch
		
		return "";
	}//end getColumnName
	
	public int getRowCount() throws IllegalStateException{
		if(!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		
		return numberOfRows;
	}//end getRowCount
	
	public Object getValueAt(int row,int column) throws IllegalStateException{
		
		if(!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		
		
		try {
			resultSet.absolute(row+1);
			return resultSet.getObject(column+1);
		}//end try
		catch(SQLException sqlException) {
			sqlException.printStackTrace();
		}//end catch
		
		return "";
	}//end getValueAt
	
	public void setQuery(String query) throws SQLException,IllegalStateException{
		if(!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		
		resultSet = statement.executeQuery(query);
		
		metaData = resultSet.getMetaData();
		
		resultSet.last();
		numberOfRows = resultSet.getRow();
		
		fireTableStructureChanged();
		
	}//end setQuery
	
	public void disconnectFromDatabase() {
		if(connectedToDatabase) {
			try {
				resultSet.close();
				statement.close();
				connection.close();
				
			}//end try
			catch(SQLException sqlException) {
				sqlException.printStackTrace();
			}//end catch
			finally {
				connectedToDatabase = false;
			}//end finally
		}//end if
	}//end disconnectFromDatabase
	
	public void getData(String name) {
		String query = "SELECT * FROM member.people WHERE name = "+ "\'" + name +"\'";
		try {
			resultSet = statement.executeQuery(query);
			resultSet.absolute(1);
			String type = resultSet.getObject(3).toString();
			System.out.println(type);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}//end getData
	
}//end class ResultTableModel
