package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(path = "/test", method = RequestMethod.POST)
    public ResponseEntity Test()  {
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(path = "/normal", method = RequestMethod.GET)
    public String normal()  {
        return "Hello world";
    }

}
