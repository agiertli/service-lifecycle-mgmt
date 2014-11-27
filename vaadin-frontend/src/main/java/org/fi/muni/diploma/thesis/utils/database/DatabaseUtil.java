package org.fi.muni.diploma.thesis.utils.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.fi.muni.diploma.thesis.utils.properties.ClientProperties;

/**
 * Wrapper for all related database functionality
 * @author Anton Giertli
 *
 */
public class DatabaseUtil {

	private Connection conn;

	public DatabaseUtil() throws IOException, NamingException, SQLException {

		this.init();
	}

	/**
	 * Opens the connection
	 * @throws IOException
	 * @throws NamingException
	 * @throws SQLException
	 */
	public void init() throws IOException, NamingException, SQLException {

		ClientProperties prop = new ClientProperties();
		Context ctx = null;
		ctx = new InitialContext();
		DataSource ds = null;
		ds = (DataSource) ctx.lookup(prop.getDsJndi());
		this.conn = ds.getConnection();
	}

	/**
	 * Close the connection
	 * @throws SQLException
	 */
	public void close() throws SQLException {

		this.conn.close();
	}

	/**
	 * Insert processed notification about retired service invocation into the database
	 * @param retirement_date
	 * @param invocationTimestamp
	 * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
	 * @throws SQLException
	 */
	public int insertProcessedNotification(long retirement_date, long invocationTimestamp) throws SQLException {

		Statement createTable = null;

		String createSql = "CREATE TABLE IF NOT EXISTS NOTIFICATION(RETIREMENT_DATE BIGINT, INVOCATION_TIMESTAMP BIGINT, PRIMARY KEY(RETIREMENT_DATE, INVOCATION_TIMESTAMP))";
		createTable = this.conn.createStatement();
		createTable.executeUpdate(createSql);

		Statement insertRow = null;
		insertRow = this.conn.createStatement();
		String insertSql = "INSERT INTO NOTIFICATION " + "VALUES (" + String.valueOf(retirement_date)+","+String.valueOf(invocationTimestamp) + ")";
		int counter = insertRow.executeUpdate((insertSql));

		return counter;
	}

	/**
	 * Looks in the database for notification
	 * 
	 * @param retirementtimestamp
	 * @param invocationTimestamp
	 * @return null when nothing was found in db or actual result
	 * @throws NamingException
	 * @throws SQLException
	 * @throws IOException
	 */
	public ResultSet findNotificationById(long retirementtimestamp,long invocationTimestamp) throws NamingException, SQLException, IOException {

		Statement createTable = null;

		String createSql = "CREATE TABLE IF NOT EXISTS NOTIFICATION(RETIREMENT_DATE BIGINT, INVOCATION_TIMESTAMP BIGINT, PRIMARY KEY(RETIREMENT_DATE, INVOCATION_TIMESTAMP))";
		createTable = this.conn.createStatement();
		createTable.executeUpdate(createSql);

		Statement selectByIdStatement = null;
		String selectByIdSQL = "Select * FROM NOTIFICATION WHERE RETIREMENT_DATE =" + String.valueOf(retirementtimestamp+"AND INVOCATION_TIMESTAMP="+invocationTimestamp);
		selectByIdStatement = this.conn.createStatement();
		ResultSet rs = selectByIdStatement.executeQuery(selectByIdSQL);

		return rs;

	}

}
