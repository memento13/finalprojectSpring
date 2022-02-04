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
import repository.PostRepository;
import service.PostService;
import service.ProjectService;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
public class PostController {

    private final PostService postService;
    private final ProjectService projectService;
    private final PostRepository postRepository;


    public PostController(PostService postService, ProjectService projectService, PostRepository postRepository) {
        this.postService = postService;
        this.projectService = projectService;
        this.postRepository = postRepository;
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

//        System.out.println("=================================");
//        System.out.println("projectId = " + projectId);
//        System.out.println(URLDecoder.decode(projectId,"UTF-8")); //안해도 됨
//        System.out.println("=================================");
//        System.out.println("partyId = " + partyId);
//        System.out.println(URLDecoder.decode(partyId,"UTF-8"));//안해도 됨
//        System.out.println("=================================");
//        System.out.println("title = " + title); //안됨
//        System.out.println(URLDecoder.decode(title,"UTF-8")); //안됨
//        System.out.println(Hangul.hangul(title)); //한글
//        System.out.println(URLDecoder.decode(Hangul.hangul(title),"UTF-8")); //한글
//        System.out.println("=================================");
//        System.out.println("content = " + content); //안됨
//        System.out.println(URLDecoder.decode(content,"UTF-8")); //한글

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
        String route = "redirect:/main.pknu?msg=incorrectConnection";
        if(addResult){
            route = "redirect:/project.pknu?project_id="+projectId;
        }

//        return URLDecoder.decode(content,"UTF-8");
        return route;
    }

    @RequestMapping("/post.pknu")
    public ModelAndView postPage(@RequestParam(value = "post_id", required = true)String postId,HttpSession session){

        ModelAndView mnv = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Post post = postRepository.findById(postId);
        //포스트 열람가능한지 확인
        //순환참조를 피하면서 중복된 서비스를 만들지 않고 기능을 구현할 방법이 있는가?
        boolean isAuthorizedReader = projectService.checkUserJoinedProject(post.getProject(), user);
        if(isAuthorizedReader){
            //post 객체 넘김
            //추후 좋아요, 등등 생긴다면 넘김
            mnv.addObject("post",post);
            mnv.setViewName("project/post_page");
        }
        else{
            mnv.setViewName("redirect:/main.pknu?msg=incorrectConnection");
        }

        return mnv;
    }

    @ResponseBody
    @RequestMapping("/like")
    public String createLike(@RequestParam(value = "post_id",required = true)String postId,HttpSession session){

        User user = (User) session.getAttribute("user");
        Post post = postRepository.findById(postId);

        //추천가능한지 확인
        boolean isAuthorizedReader = projectService.checkUserJoinedProject(post.getProject(), user);
        if(isAuthorizedReader){
            //인코딩해야함

        }


        // 추천수와 메시지를 같이 보내어 추천이 반영되었는지 ex) 추천했습니다, 이미 추천한 게시글입니다, 올바른접근이 아닙니다.

    }



}
