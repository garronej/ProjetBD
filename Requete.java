
import java.sql.*;

public class Requete {
	static final String CONN_URL = "jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1";
	static final String USER = "bretf";
	static final String PASSWD = "LB8zusMh";
	private String Query = " ";
	private Connection conn;
	
	public Requete(){
		boolean estConnecte=false;
		do {
			try {
				this.conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
				System.out.println("connected to database");
				conn.setAutoCommit(false);
				estConnecte=true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}while(!estConnecte);
	}
	
	public void requete() throws Exception {
		try {
			// Etablissement de la connection
			//System.out.print("Executing query... ");
			// Creation de la requete
			Statement stmt = conn.createStatement();
			// Execution de la requete
			ResultSet rset = stmt.executeQuery(this.Query);
			// Affichage du resultat
			if (Query.contains("SELECT")){
				System.out.println("Results:");
				dumpResultSet(rset);
			}
			// Fermeture
			rset.close();
			stmt.close();
			//System.out.println("done\n");
		} catch (SQLException e) {
			System.err.println("failed");
			e.printStackTrace(System.err);
		}
	}
	public int getInteger(String colonne) throws SQLException{
		try {
		Statement stmt = conn.createStatement();
		// Execution de la requete
		ResultSet rset = stmt.executeQuery(this.Query);
		int result=0;
		while(rset.next()){
			result = rset.getInt(colonne);
		}
		rset.close();
		stmt.close();
		return result;
		} catch (SQLException e) {
			System.err.println("failed");
			e.printStackTrace(System.err);
			return 0;
		}
	}
	
	public void dumpResultSet(ResultSet rset) throws SQLException {
		ResultSetMetaData rsetmd = rset.getMetaData();
		int i = rsetmd.getColumnCount();
		for (int k = 1; k <= i; k++)
			System.out.print(rsetmd.getColumnName(k) + "\t");
		System.out.println();
		while (rset.next()) {
			for (int j = 1; j <= i; j++) {
				System.out.print(rset.getString(j) + "\t");
			}
			System.out.println();
		}
	}
	
	public void displayFields() throws SQLException{
		Statement stmt = conn.createStatement();
		ResultSetMetaData rset = stmt.executeQuery(this.Query).getMetaData();
		int i = rset.getColumnCount();
		for (int k = 1; k <= i; k++)
			System.out.print(rset.getColumnName(k) + "\t");
		System.out.println();
	}
	
	public void setQuery(String s){
		this.Query = s;
	}

	public void nextTransaction() {
		try {
			conn.commit();
			System.out.println("new transaction");
		} catch (SQLException e) {
			System.err.println("failed");
			e.printStackTrace(System.err);
		}
	}

	public void startConnection() throws SQLException {
		// Enregistrement du driver Oracle
		System.out.print("Loading Oracle driver... ");
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		System.out.println("loaded");
	}

	public  Connection getConnection() {
		return conn;
	}

	public void endConnection() throws SQLException {
		this.conn.close();
		System.out.println("connection closed");
	}
	
}
