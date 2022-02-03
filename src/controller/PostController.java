package controller;

import entity.Post;
import entity.Project;
import entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import repository.Hangul;
import service.PostService;
import service.ProjectService;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Controller
public class PostController {

    private final PostService postService;
    private final ProjectService projectService;


    public PostController(PostService postService, ProjectService projectService) {
        this.postService = postService;
        this.projectService = projectService;
    }

    @RequestMapping("/create-post.pknu")
    public ModelAndView createPostPage(@RequestParam("project_id") String projectId, @RequestParam("party_id") String partyId, HttpSession session){
        User user = (User) session.getAttribute("user");
        Project project = new Project();
        project.setId(projectId);
        project.setParty_id(partyId);

        //글작성 권한 검증
        boolean checkUserJoinedProject = projectService.checkUserJoinedProject(project, user);

        //페이지 이동
        ModelAndView mnv = new ModelAndView();
        if(checkUserJoinedProject){
            mnv.setViewName("create_post_page");
            mnv.addObject("project_id",projectId);
            mnv.addObject("party_id",partyId);
        }else{
            mnv.setViewName("redirect:/main.pknu?msg=incorrectConnection");
        }

        return mnv;
    }

    @RequestMapping(value = "/create-post/create.pknu")
    public String createPost(@RequestParam(value = "project_id" ,required = true) String projectId,
                             @RequestParam(value = "party_id",required = true) String partyId,
                             @RequestParam(value = "title", required = true) String title,
                             @RequestParam(value = "content", required = true) String content,
                             HttpSession session) throws UnsupportedEncodingException {

        System.out.println("=================================");
        System.out.println("projectId = " + projectId);
        System.out.println(URLDecoder.decode(projectId,"UTF-8")); //안해도 됨
        System.out.println("=================================");
        System.out.println("partyId = " + partyId);
        System.out.println(URLDecoder.decode(partyId,"UTF-8"));//안해도 됨
        System.out.println("=================================");
        System.out.println("title = " + title); //안됨
        System.out.println(URLDecoder.decode(title,"UTF-8")); //안됨
        System.out.println(Hangul.hangul(title)); //한글
        System.out.println(URLDecoder.decode(Hangul.hangul(title),"UTF-8")); //한글
        System.out.println("=================================");
        System.out.println("content = " + content); //안됨
        System.out.println(URLDecoder.decode(content,"UTF-8")); //한글

        User user = (User) session.getAttribute("user");
        title = Hangul.hangul(title);
        content = URLDecoder.decode(content,"UTF-8");
        Post post = new Post();
        post.setTitle(URLDecoder.decode(title,"UTF-8"));
        post.setContent(content);
        post.setUser(user);
        post.setProjectId(projectId);
        post.setPartyId(partyId);
        boolean addResult = postService.createPost(post);
        String rout = "redirect:/main.pknu?msg=incorrectConnection";
        if(addResult){
            rout = "redirect:/project.pknu?project_id="+projectId;
        }


//        return URLDecoder.decode(content,"UTF-8");
        return rout;
    }
}
