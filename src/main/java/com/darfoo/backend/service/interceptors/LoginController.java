package com.darfoo.backend.service.interceptors;

import org.omg.CORBA.Request;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by zjh on 14-12-3.
 */

@Controller
@RequestMapping(value = "/login")
public class LoginController {
    @RequestMapping(value = {"/", ""})
    public String index() {
        return "login";
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody String auth(HttpServletRequest request, HttpSession session) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println(username);
        System.out.println(password);

        if (username.equals("Darfoo@board") && password.equals("Oofrad@Oofrad")){
            request.getSession().setAttribute("loginUser", "cleantha");
            return "200";
        }else if((username.equals("darfoo@upload") && password.equals("Upload@333")) || (username.equals("darfoo@tech") && password.equals("Oofrad@darfoo"))){
            request.getSession().setAttribute("loginUser", "upload");
            return "200";
        }else{
            return "501";
        }
    }

    @RequestMapping(value = "/permissiondeny", method = RequestMethod.GET)
    public String permissiondeny() {
        return "permissiondeny";
    }

    @RequestMapping("/out")
    public String out(HttpServletRequest req) {
        req.getSession().removeAttribute("loginUser");
        return "redirect:/rest/login";
    }
}
