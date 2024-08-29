package org.dementhium.mysql;

import java.sql.*;

public class SQLConnection {

    private static final String HOST_ADDR = "127.0.0.1";
    private static final String DB_NAME = "database_name";
    private static final String DB_USERNAME = "database_username";
    private static final String DB_PASSWORD = "database_password";
    private static final String DB_PORT = "42069";
    private static final boolean SQL_ENABLED = false;

    private static String getConnectionUrl() { return "jdbc:mysql://" + HOST_ADDR + ":" + DB_PORT + "/" + DB_NAME; }

    private Connection connection;
    private Statement statement;
    private String lastQuery;

    protected SQLConnection() {
        if(!SQL_ENABLED) {
            return;
        }
        connection = createNewDefaultConnection();
        statement = createStatementFromConnection(connection, true);
        if (connection == null || statement == null) {
            System.out.println("Unable to connect to MySQL");
            System.exit(1);
        } else {
            System.out.println("Successfully connected to database: "+DB_NAME);
        }
    }

    protected Connection getConnection() {
        return connection;
    }

    protected Statement getStatement() {
        return statement;
    }

    protected String getLastQuery() {
        return lastQuery;
    }

    protected static Connection createNewDefaultConnection() {
        try {
            return DriverManager.getConnection(getConnectionUrl(), DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static Statement createStatementFromConnection(Connection connection, boolean escapeProcessingState) {
        try {
            Statement statement = connection.createStatement();
            statement.setEscapeProcessing(escapeProcessingState);
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException npe) {
            System.out.println("SQL.csfc NPE (null connection passed)");
            return null;
        }
    }

    protected boolean isConnected() {
        try {
            statement.executeQuery("SELECT CURRENT_DATE");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    protected void closeConnection(){
        try {
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ResultSet runQuery(String query) {
        try {
            if((query.toLowerCase()).startsWith("select")){
                return statement.executeQuery(query);
            } else {
                statement.executeUpdate(query);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Runs a select query on the current database connection
     *
     * @param q
     *            The query to be ran
     */

    protected ResultSet getQuery(String q) throws SQLException {
        try {
            lastQuery = q;
            return statement.executeQuery(q);
        } catch (SQLException e) {
            if (!isConnected() && createNewDefaultConnection() != null) {
                return getQuery(q);
            }
            throw new SQLException(e.getMessage() + ": '" + lastQuery + "'", e
                    .getSQLState(), e.getErrorCode());
        }
    }

    /**
     * Runs a update/insert/replace query on the current database connection
     *
     * @param q
     *            The query to be ran
     */
    protected int updateQuery(String q) throws SQLException {
        try {
            lastQuery = q;
            return statement.executeUpdate(q);
        } catch (SQLException e) {
            if (!isConnected() && createNewDefaultConnection() != null) {
                return updateQuery(q);
            }
            throw new SQLException(e.getMessage() + ": '" + lastQuery + "'", e.getSQLState(), e.getErrorCode());
        }
    }

    protected int newQuery(String q) throws SQLException {
        try {
            Statement tempStatement = connection.createStatement();
            lastQuery = q;
            return tempStatement.executeUpdate(q);
        } catch (SQLException e) {
            if (!isConnected() && createNewDefaultConnection() != null) {
                return updateQuery(q);
            }
            throw new SQLException(e.getMessage() + ": '" + lastQuery + "'", e.getSQLState(), e.getErrorCode());
        }
    }
}
