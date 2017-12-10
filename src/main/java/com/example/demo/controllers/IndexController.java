package com.example.demo.controllers;

import com.example.demo.models.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String Index(){
        return "Hello World";
    }

    @RequestMapping(path = "/fail", method = RequestMethod.GET)
    public String Fail(){
        String nul = null;
        nul.toCharArray();
        return "Hello World";
    }

    @RequestMapping(path = "/customException", method = RequestMethod.GET)
    public String Custom() throws Exception {
        throw new MissingServletRequestParameterException("param", "string");
    }
}
