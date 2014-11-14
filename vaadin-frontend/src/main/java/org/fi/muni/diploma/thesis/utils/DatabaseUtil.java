package org.fi.muni.diploma.thesis.utils;

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

public class DatabaseUtil {

	private Connection conn;

	public DatabaseUtil() throws IOException, NamingException, SQLException {

		this.init();
	}

	public void init() throws IOException, NamingException, SQLException {

		ClientProperties prop = new ClientProperties();
		Context ctx = null;
		ctx = new InitialContext();
		DataSource ds = null;
		ds = (DataSource) ctx.lookup(prop.getDsJndi());
		this.conn = ds.getConnection();
	}

	public void close() throws SQLException {

		this.conn.close();
	}

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
