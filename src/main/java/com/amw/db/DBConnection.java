package com.amw.db;

import java.sql.*;
import java.util.Optional;

public class DBConnection
{
    private final Connection _connection;

    public DBConnection(Connection connection)
    {
        _connection = connection;
    }

    public static DBConnection create()
    {
        final Optional<String> pwOptional = Optional.ofNullable(System.getenv("db_root_password"));
        final Optional<String> hostOptional = Optional.ofNullable(System.getenv("MY_RELEASE_SQL_POSTGRESQL_SERVICE_HOST"));
        final Optional<String> portOptional = Optional.ofNullable(System.getenv("MY_RELEASE_SQL_POSTGRESQL_SERVICE_PORT"));
        final Optional<String> dbNameOptional = Optional.ofNullable(System.getenv("db_name"));
        if (pwOptional.isEmpty() || hostOptional.isEmpty() || portOptional.isEmpty() || dbNameOptional.isEmpty())
        {
            return null;
        }
        try
        {
            final StringBuilder urlBuilder = new StringBuilder()
                    .append("jdbc:postgresql://")
                    .append(hostOptional.get())
                    .append(":")
                    .append(portOptional.get())
                    .append("/")
                    .append(dbNameOptional.get());
            final Connection connection = DriverManager.getConnection(
                    urlBuilder.toString(),
                    "postgres",
                    pwOptional.get());
            return new DBConnection(connection);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }


    public String select()
    {
        try
        {
            PreparedStatement preparedStatement = _connection.prepareStatement("select * from cars");
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder result = new StringBuilder();
            while (resultSet.next())
            {
                result.append(resultSet.getString("brand"))
                        .append(" ")
                        .append(resultSet.getString("model"))
                        .append(" ")
                        .append(resultSet.getInt("year"))
                        .append("\n");
            }
            return result.toString();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
