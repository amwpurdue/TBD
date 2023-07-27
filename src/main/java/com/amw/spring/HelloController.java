package com.amw.spring;

import com.amw.db.DBConnection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController
{
    private final DBConnection _connection;

    public HelloController()
    {
        _connection = DBConnection.create();
    }

    @GetMapping("/")
    public String index()
    {
        if (_connection != null)
        {
            return "Cars:<br>" + _connection.select().replaceAll("\\n", "<br>");
        }
        else
        {
            return "no connection to db";
        }
    }

}