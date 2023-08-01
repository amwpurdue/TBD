package com.amw.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

@Service
public class DBConnection
{
    private final Connection _connection;

    @Autowired
    public DBConnection(DataSource dataSource)
    {
        try
        {
            _connection = dataSource.getConnection();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static DBInfo getDBInfo()
    {
        final Optional<String> pwOptional = Optional.ofNullable(System.getenv("db_root_password"));
        final Optional<String> hostOptional = Optional.ofNullable(System.getenv("MY_RELEASE_SQL_POSTGRESQL_SERVICE_HOST"));
        final Optional<String> portOptional = Optional.ofNullable(System.getenv("MY_RELEASE_SQL_POSTGRESQL_SERVICE_PORT"));
        final Optional<String> dbNameOptional = Optional.ofNullable(System.getenv("db_name"));
        if (pwOptional.isEmpty() || hostOptional.isEmpty() || portOptional.isEmpty() || dbNameOptional.isEmpty())
        {
            return null;
        }

        final String urlBuilder = "jdbc:postgresql://" +
                hostOptional.get() +
                ":" +
                portOptional.get() +
                "/" +
                dbNameOptional.get();
        return new DBInfo(
                urlBuilder,
                "postgres",
                pwOptional.get()
        );
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

    public record DBInfo(String url, String user, String password)
    {
    }
}
