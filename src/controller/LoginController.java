package controller;

import entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import repository.ConnectionCheck;

import service.UserService;
import captcha.Captcha;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserService userService;
    private final ConnectionCheck connectionCheck;
    private final Captcha captcha;

    //오토와이어 안넣어도 되나? 됨!
    public LoginController(ConnectionCheck connectionCheck, UserService userService, Captcha captcha) {
        this.connectionCheck = connectionCheck;
        this.userService = userService;
        this.captcha = captcha;
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

        //캡챠 넘김
        String bal = captcha.bal();
        mnv.addObject("key",bal);

        String filedown = captcha.filedown(bal);
        System.out.println("filedown = " + filedown);
        mnv.addObject("fname",filedown);
        mnv.setViewName("create_account_page");
        return mnv;
    }

    // 회원가입 로직 페이지 ( 현재 임시)
    @RequestMapping("/create-account/create.pknu")
    public String createAccount(@ModelAttribute User user,
                                @RequestParam(value = "key",required = true)String key,
                                @RequestParam(value = "captcha",required = true)String inputCaptcha){

        Boolean vali = captcha.vali(key, inputCaptcha);
        if(!vali){
            return "redirect:/login.pknu?result=creatFail";
        }
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

        //로그인 실패
        if(result==null){
            return "redirect:/login.pknu?result=loginFail";
        }
        //로그인 성공
        else{
            System.out.println("user.getName() = " + result.getName());
            session.setAttribute("user",result);
            return "redirect:/main.pknu";
        }
    }

    //메인화면 페이지
    @RequestMapping("/main.pknu")
    public ModelAndView main_page(HttpSession session){
        User user = (User) session.getAttribute("user");
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("main_page");
        mnv.addObject("user",user);
        return mnv;
    }


}


