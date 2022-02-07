package controller;

import entity.Comment;
import entity.Post;
import entity.Project;
import entity.User;
import org.json.JSONObject;
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

    //글작성페이지
    @RequestMapping("/create-post.pknu")
    public ModelAndView createPostPage(@RequestParam("project_id") String projectId, @RequestParam("party_id") String partyId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Project project = new Project();
        project.setId(projectId);
        project.setParty_id(partyId);

        //글작성 권한 검증
        boolean checkUserJoinedProject = projectService.checkUserJoinedProject(project, user);

        //페이지 이동
        ModelAndView mnv = new ModelAndView();
        if (checkUserJoinedProject) {
            mnv.setViewName("create_post_page");
            mnv.addObject("project_id", projectId);
            mnv.addObject("party_id", partyId);
        } else {
            mnv.setViewName("redirect:/main.pknu?msg=incorrectConnection");
        }

        return mnv;
    }

    //글작성로직
    @RequestMapping(value = "/create-post/create.pknu")
    public String createPost(@RequestParam(value = "project_id", required = true) String projectId,
                             @RequestParam(value = "party_id", required = true) String partyId,
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
        content = URLDecoder.decode(content, "UTF-8");
        Post post = new Post();
        post.setTitle(URLDecoder.decode(title, "UTF-8"));
        post.setContent(content);
        post.setUser(user);
        post.setProjectId(projectId);
        post.setPartyId(partyId);
        boolean addResult = postService.createPost(post);
        String route = "redirect:/main.pknu?msg=incorrectConnection";
        if (addResult) {
            route = "redirect:/project.pknu?project_id=" + projectId;
        }

//        return URLDecoder.decode(content,"UTF-8");
        return route;
    }

    //글 보기
    @RequestMapping("/post.pknu")
    public ModelAndView postPage(@RequestParam(value = "post_id", required = true) String postId, HttpSession session) {

        ModelAndView mnv = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Post post = postRepository.findById(postId);
        //포스트 열람가능한지 확인
        //순환참조를 피하면서 중복된 서비스를 만들지 않고 기능을 구현할 방법이 있는가?
        boolean isAuthorizedReader = projectService.checkUserJoinedProject(post.getProject(), user);
        if (isAuthorizedReader) {
            //post 객체 넘김
            mnv.addObject("post", post);
            //추후 좋아요, 등등 생긴다면 넘김
            Integer likes = postService.getLikes(post);
            mnv.addObject("likes", likes);

            mnv.setViewName("project/post_page");
        } else {
            mnv.setViewName("redirect:/main.pknu?msg=incorrectConnection");
        }

        return mnv;
    }

    // 좋아요 로직
    @ResponseBody
    @RequestMapping(value = "/like.pknu")
    public String createLike(@RequestParam(value = "post_id", required = true) String postId, HttpSession session) throws UnsupportedEncodingException {

        User user = (User) session.getAttribute("user");
        Post post = postRepository.findById(postId);

        String resultJSONtoString = null;

        //추천가능한지 확인 가능시 access : true(doLike에서 넣어줌), 실패시 false(컨트롤러에서 넣음) 값이 들어감
        boolean isAuthorizedReader = projectService.checkUserJoinedProject(post.getProject(), user);
        if (isAuthorizedReader) {
            // 추천후 결과 json 으로 반환
            JSONObject jsonObject = postService.doLike(post, user);
            //인코딩해야함
//            resultJSONtoString = URLEncoder.encode(jsonObject.toString(), "UTF-8");
            resultJSONtoString = jsonObject.toString();
        } else {
            JSONObject resultJSON = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("access", false);
            resultJSON.put("data", data);

//            resultJSONtoString = URLEncoder.encode(resultJSON.toString(),"UTF-8");
            resultJSONtoString = resultJSON.toString();
        }


        return resultJSONtoString;
    }

    //글 삭제 로직
    @RequestMapping(value = "/post/delete.pknu")
    public String deletePost(@RequestParam(value = "post_id", required = true) String postId, HttpSession session) {

        User user = (User) session.getAttribute("user");
        Post post = postRepository.findById(postId);
        boolean deleteResult = postService.deletePost(post, user);

        String route = "redirect:/project.pknu?project_id=" + post.getProjectId();
        if (deleteResult) {
            route += "&msg=postDeleteSuccess";
        } else {
            route += "&msg=postDeleteFail";
        }
        // 삭제 가능한 유저인가? (글작성자, 파티장)
        // 삭제
        // 해당 프로젝트 화면으로 리다이렉션
        return route;
    }

    //댓글 불러오는 로직
    @ResponseBody
    @RequestMapping(value = "/comment.pknu")
    public String showComments(@RequestParam("post_id") String postId, HttpSession session) throws UnsupportedEncodingException {
        User user = (User) session.getAttribute("user");
        Post post = postRepository.findById(postId);

        String resultJSONtoString = null;

        //게시글 열람 가능한지 확인 access : true(getCommentsToJSON 에서 넣어줌), 실패시 false(컨트롤러에서 넣음) 값이 들어감
        boolean isAuthorizedReader = projectService.checkUserJoinedProject(post.getProject(), user);

        if (isAuthorizedReader) {
            JSONObject commentsToJSON = postService.getCommentsToJSON(post);
            resultJSONtoString = commentsToJSON.toString();
        } else {
            JSONObject result = new JSONObject();
            result.put("post_id", post.getId());
            result.put("access", false);
            resultJSONtoString = result.toString();
        }

        return resultJSONtoString;
    }

    //댓글 작성
    @ResponseBody
    @RequestMapping(value = "/comment/create.pknu")
    public String createComment(@RequestParam("post_id") String postId,
                                @RequestParam("content") String content,
                                HttpSession session) throws UnsupportedEncodingException {

        User user = (User) session.getAttribute("user");
        content = URLDecoder.decode(content, "UTF-8");
        Post post = postRepository.findById(postId);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        boolean addCommentResult = postService.addComment(comment, user);

        String resultJSONtoString = null;

        //게시글 열람 가능한지 확인 access : true(getCommentsToJSON 에서 넣어줌), 실패시 false(컨트롤러에서 넣음) 값이 들어감
        boolean isAuthorizedReader = projectService.checkUserJoinedProject(post.getProject(), user);

        if (isAuthorizedReader) {
            JSONObject commentsToJSON = postService.getCommentsToJSON(post);
            resultJSONtoString = commentsToJSON.toString();
        } else {
            JSONObject result = new JSONObject();
            result.put("post_id", post.getId());
            result.put("access", false);
            resultJSONtoString = result.toString();
        }

        return resultJSONtoString;
    }

    @ResponseBody
    @RequestMapping(value = "/comment/reply.pknu")
    public String createReply(@RequestParam("post_id") String postId,
                              @RequestParam("parent_comment_id") String parentCommentId,
                              @RequestParam("content") String content,
                              HttpSession session) throws UnsupportedEncodingException {

        User user = (User) session.getAttribute("user");
        content = URLDecoder.decode(content, "UTF-8");
        Post post = postRepository.findById(postId);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setParentCommentId(parentCommentId);
        comment.setContent(content);
        boolean addCommentResult = postService.addComment(comment, user);

        String resultJSONtoString = null;

        //게시글 열람 가능한지 확인 access : true(getCommentsToJSON 에서 넣어줌), 실패시 false(컨트롤러에서 넣음) 값이 들어감
        boolean isAuthorizedReader = projectService.checkUserJoinedProject(post.getProject(), user);

        if (isAuthorizedReader) {
            JSONObject commentsToJSON = postService.getCommentsToJSON(post);
            resultJSONtoString = commentsToJSON.toString();
        } else {
            JSONObject result = new JSONObject();
            result.put("post_id", post.getId());
            result.put("access", false);
            resultJSONtoString = result.toString();
        }

        System.out.println("resultJSONtoString = " + resultJSONtoString);
        return resultJSONtoString;
    }

    @ResponseBody
    @RequestMapping(value = "/comment/delete.pknu")
    public String deleteComment(@RequestParam("post_id") String postId,
                              @RequestParam("comment_id") String commentId,
                              HttpSession session) throws UnsupportedEncodingException {

        User user = (User) session.getAttribute("user");
        System.out.println("postId = " + postId);
        System.out.println("commentId = " + commentId);
        Post post = postRepository.findById(postId);

        Comment comment = new Comment();
        comment.setPost(post);
//        comment.setUser(user);
        comment.setId(commentId);
        boolean deleteCommentResult = postService.deleteComment(comment, user);
        System.out.println("deleteCommentResult = " + deleteCommentResult);

        String resultJSONtoString = null;

        //게시글 열람 가능한지 확인 access : true(getCommentsToJSON 에서 넣어줌), 실패시 false(컨트롤러에서 넣음) 값이 들어감
        boolean isAuthorizedReader = projectService.checkUserJoinedProject(post.getProject(), user);

        if (isAuthorizedReader) {
            JSONObject commentsToJSON = postService.getCommentsToJSON(post);
            resultJSONtoString = commentsToJSON.toString();
        } else {
            JSONObject result = new JSONObject();
            result.put("post_id", post.getId());
            result.put("access", false);
            resultJSONtoString = result.toString();
        }

        System.out.println("resultJSONtoString = " + resultJSONtoString);
        return resultJSONtoString;
    }

}
