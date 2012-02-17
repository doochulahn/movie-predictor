package persistence.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistence.DataSource;
import persistence.GenreDao;
import persistence.PersistenceException;


public class GenreDaoImpl implements GenreDao{
	private DataSource dataSource;

	public GenreDaoImpl(){
		this.dataSource=new DataSource();
	}

	@Override
	public Integer[] retrieveGenreVectorByMovieID( int movieID) throws PersistenceException{
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		Integer[] vector=new Integer[21];
		String query="select * from genres where movieid=?";
		try{
			statement=connection.prepareStatement(query);
			statement.setInt(1, movieID);
			ResultSet res=statement.executeQuery();
			while(res.next() ){
				for (int i=0; i<vector.length;i++){
					vector[i]=res.getInt(i+1);
				}
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new PersistenceException(e.getMessage());
		} finally{
			try{
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e){
				throw new PersistenceException(e.getMessage());
			}
		}
		return vector;
	}


	@Override
	public List<Integer[]> retrieveSimilarVectors(Integer[] vector)throws PersistenceException {
		List<Integer[]> vectors=new ArrayList<Integer[]>();
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		
		String query="select * from genres where "+getWhereConds(vector);
		try{
			statement=connection.prepareStatement(query);
			ResultSet res=statement.executeQuery();
			while(res.next() ){
				Integer[] v=new Integer[21];
				for (int j=0; j<21; j++){
					v[j]=res.getInt(j+1);
				}
				vectors.add(v);
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new PersistenceException(e.getMessage());
		} finally{
			try{
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e){
				throw new PersistenceException(e.getMessage());
			}
		}
		return vectors;
	}

	private static String getWhereConds(Integer[] vector){
		String[] int2genre=new String[21];
		int2genre[0]="movieid";
		int2genre[1]="action";
		int2genre[2]="adventure";
		int2genre[3]="animation";
		int2genre[4]="children";
		int2genre[5]="comedy";
		int2genre[6]="documentary";
		int2genre[7]="drama";
		int2genre[8]="fantasy";
		int2genre[9]="filmnoir";
		int2genre[10]="horror";
		int2genre[11]="imax";
		int2genre[12]="musical";
		int2genre[13]="mystery";
		int2genre[14]="romance";
		int2genre[15]="scifi";
		int2genre[16]="short";
		int2genre[17]="thriller";
		int2genre[18]="war";
		int2genre[19]="western";
		int2genre[20]="crime";
		
		String queryWhereConds="";
		queryWhereConds+=int2genre[1]+"=1 ";
		for (int i=2;i<vector.length; i++){
			if (vector[i]==1){
				queryWhereConds+="or "+int2genre[i]+"=1 ";
			}
		}
		return queryWhereConds;
	}




}