package com.amw.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloController
{
    private final DBConnection _connection;
    private final UserDetailsManager _userDetailsManager;

    @Autowired
    public HelloController(DBConnection connection, UserDetailsManager userDetailsManager)
    {
        _connection = connection;
        _userDetailsManager = userDetailsManager;
        System.out.println("HelloController: " + _connection);
        System.out.println("  " + _connection.select());
    }

    @GetMapping("/index")
    public String index()
    {
        System.out.println("HelloController#index");
        return "index";
    }

    @GetMapping("/login")
    public String login()
    {
        System.out.println("HelloController#login");
        return "login";
    }

    @PostMapping("/perform_login")
    public String doLogin(@RequestParam String username, @RequestParam String password)
    {
        System.out.println("HelloController#perform_login");
        return username + ": " + password;
    }

}