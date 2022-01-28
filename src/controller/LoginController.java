package controller;

import entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import repository.ConnectionCheck;

import service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserService userService;
    private final ConnectionCheck connectionCheck;

    //오토와이어 안넣어도 되나? 됨!
    public LoginController(ConnectionCheck connectionCheck, UserService userService) {
        this.connectionCheck = connectionCheck;
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping("/dbcheck.pknu")
    public String dbCheck(){
        System.out.println("LoginController.dbCheck");
        System.out.println(connectionCheck.toString());
        return connectionCheck.dbConnectionCheck();
    }
    //    로그인페이지로 이동
    @RequestMapping("/login.pknu")
    public ModelAndView loginPage(HttpSession session){
        ModelAndView mnv = new ModelAndView();

        if(session.getAttribute("user")!=null){
            session.removeAttribute("user");
        }
        mnv.setViewName("login_page");
        return mnv;
    }

    //    로그아웃 로직
    @RequestMapping("/logout.pknu")
    public String logout(HttpSession session){

        if(session.getAttribute("user")!=null){
            session.removeAttribute("user");
        }

        return "redirect:/login.pknu?result=logoutSuccess";
    }

    //    회원가입페이지로 이동
    @RequestMapping("/create-account.pknu")
    public ModelAndView createAccountPage(){
        System.out.println("LoginController.createAccountPage");
        ModelAndView mnv = new ModelAndView();

        mnv.setViewName("create_account_page");
        return mnv;
    }

    // 회원가입 로직 페이지 ( 현재 임시)
    @RequestMapping("/create-account/create.pknu")
    public String createAccount(@ModelAttribute User user){

        boolean result = userService.createAccount(user);
        if(result){
            return "redirect:/login.pknu?result=createSuccess";
        }
        return "redirect:/login.pknu?result=creatFail";
    }

    // 로그인 로직 페이지 ( 현재 임시)
    @RequestMapping("/login/authorization.pknu")
    public String authorization(@ModelAttribute User user, HttpSession session){


        User result = userService.loginAuthorization(user);

        System.out.println("user.getName() = " + result.getName());
        //로그인 실패
        if(result==null){
            return "redirect:/login.pknu?result=loginFail";
        }
        //로그인 성공
        else{
            session.setAttribute("user",result);
            return "redirect:/main.pknu";
        }
    }

    //메인화면 페이지
    @RequestMapping("/main.pknu")
    public ModelAndView main_page(HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println("user.getName() = " + user.getName());
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("main_page");
        mnv.addObject("user",user);
        return mnv;
    }


}


