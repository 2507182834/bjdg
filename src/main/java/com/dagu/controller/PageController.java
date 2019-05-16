package com.dagu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PageController {

    @RequestMapping(value = "/toPatent", produces = "text/html;charset=UTF-8;", method = RequestMethod.GET)
    public String toPatent(HttpServletRequest request){

        return "patent1";
    }
    @RequestMapping(value = "/toQuality", produces = "text/html;charset=UTF-8;", method = RequestMethod.GET)
    public String toQuality(HttpServletRequest request){

        return "quality";
    }
    @RequestMapping(value = "/toOther", produces = "text/html;charset=UTF-8;", method = RequestMethod.GET)
    public String toOther(HttpServletRequest request){

        return "other";
    }
    @RequestMapping(value = "/toProduct", produces = "text/html;charset=UTF-8;", method = RequestMethod.GET)
    public String toProduct(HttpServletRequest request){

        return "product";
    }
}
