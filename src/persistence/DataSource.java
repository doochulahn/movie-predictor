package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataSource {
	private String dbUri="jdbc:mysql://localhost/thepredictor";
	private String user="root";
	private String password="ciaociao";
	
	public Connection getConnection()throws PersistenceException {
		Connection connection;	
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection=DriverManager.getConnection(this.dbUri,this.user,this.password);
		}
		catch(ClassNotFoundException e){
			throw new PersistenceException(e.getMessage());
		}
		catch(SQLException e){
			throw new PersistenceException(e.getMessage());
		}
		return connection;
	}


	public String getDbUri() {
		return dbUri;
	}


	public void setDbUri(String dbUri) {
		this.dbUri = dbUri;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
