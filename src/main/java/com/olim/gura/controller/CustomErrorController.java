package com.olim.gura.controller;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Get the error status code
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        System.out.println(statusCode);

        if (statusCode != null) {
            if (statusCode == 404) {
                return "errors/error-404";
            } else if (statusCode == 500) {
                return "errors/error-500";
            }
            else if (statusCode == 403 || statusCode == 401) {
                return "errors/error-401";
            }
        }
        return "errors/error";
    }


//    @Override
    public String getErrorPath() {
        return "/error";
    }
}

