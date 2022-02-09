package service;

import entity.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import repository.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final PartyRepository partyRepository;
    private final CommentRepository commentRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public PostService(PostRepository postRepository,
                       LikeRepository likeRepository,
                       PartyRepository partyRepository,
                       CommentRepository commentRepository,
                       ProjectMemberRepository projectMemberRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.partyRepository = partyRepository;
        this.commentRepository = commentRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    /**
     * 게시글을 저장하는 함수
     *
     * @param post 저장할 게시글 객체 ( 유저정보,파티정보,프로젝트 정보, 글 정보가 담겨있음)
     * @return 성공하면 true, 실패하면 false 반환
     */
    public boolean createPost(Post post) {
        boolean result = false;

        //글 등록
        Post addResult = postRepository.addPost(post);
        if (addResult != null) {
            //결과값 반환
            result = true;
        }

        return result;
    }

    /**
     * 프로젝트와 관련된 게시글을 전부 불러오는 함수
     *
     * @param project 프로젝트
     * @return 값이 없어도 null이 아닌 new ArrayList<>()객체로 반환함
     */
    public List<Post> projectPosts(Project project) {
        List<Post> result = postRepository.findPostsByProject(project);
        return result;
    }

    /**
     * 사용자가 게시글에 추천시 작동하는 함수
     *
     * @param post 추천할 게시글
     * @param user 추천하는 사용자
     * @return 추천결과 메시지와 추천수를 json으로 반환한다.
     * {data:{msg:추천결과메시지,count:추천수}}
     */
    public JSONObject doLike(Post post, User user) throws UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        Integer uc = likeRepository.addLike(like);
        //추천 성공
        if (uc == 1) {
            data.put("access", true);
            data.put("msg", URLEncoder.encode("추천했습니다", "UTF-8").replace("+", "%20"));
        } else {
            data.put("access", true);
            data.put("msg", URLEncoder.encode("추천은 한번만가능합니다", "UTF-8").replace("+", "%20"));
//            data.put("msg", "추천은한번만가능합니다");
        }
        List<Like> likes = likeRepository.findLikesByPost(post);
        data.put("count", likes.size());
        result.put("data", data);
        return result;
    }

    /**
     * 게시글의 추천수를 반환하는 함수
     *
     * @param post 게시글 객체
     * @return Integer로 반환함
     */
    public Integer getLikes(Post post) {
        List<Like> likes = likeRepository.findLikesByPost(post);
        return likes.size();
    }

    /**
     * 게시글과 게시글의 좋아요를 삭제하는 함수
     *
     * @param post 삭제할 게시글
     * @param user 삭제명령을 지시한 사용자
     * @return 삭제에 성공하면 true, 실패시 fasle 반환
     */
    public boolean deletePost(Post post, User user) {
        boolean result = false;
        Party party = partyRepository.findById(post.getPartyId());

        // 삭제 가능한 유저인가? (글작성자, 파티장)
        if (party.getLeaderId().equals(user.getId()) || post.getUserId().equals(user.getId())) {
            //좋아요 삭제
            likeRepository.deleteLikesByPost(post);
            // 삭제
            Integer uc = postRepository.deletePost(post);
            result = true;
        }

        return result;
    }

    /**
     * 게시글에 해당하는 댓글들을 리스트로 불러오는 함수
     *
     * @param post 댓글을 찾으려는 게시글
     * @return 댓글객체(일반댓글에 대댓글까지 포함한)를 리스트로 반환함 빈값이여도 null이 아닌 arrayList
     */
    public List<Comment> getComments(Post post) {
        List<Comment> result = new ArrayList<>();
        List<Comment> commentList = commentRepository.findCommentByPost(post);
        List<String> parentCommentList = new ArrayList<>(); // 일반댓글id 순서저장용
        Map<String, Comment> parentCommentsMap = new HashMap<>(); //일반댓글의 대댓글 저장용 해쉬맵

        for (Comment comment : commentList) {
            //일반댓글인 경우
            if (comment.getParentComment() == null) {
                parentCommentList.add(comment.getId());
                parentCommentsMap.put(comment.getId(), comment);
            }
            //대댓글인 경우
            else {
                parentCommentsMap.get(comment.getParentCommentId()).appendReply(comment);
            }
        }

        /**
         * 댓글id를 시간 순서대로 저장한 리스트에서 차례대로 뽑아서
         * 맵에서 검색후 댓글객체(대댓글이 포함된 일반댓글)을
         * 리스트에 넣어서 반환함
         */
        for (String parentCommentId : parentCommentList) {
            result.add(parentCommentsMap.get(parentCommentId));
        }

        return result;
    }

    /**
     * 게시글의 댓글을 json으로 반환해줌
     *
     * @param post 댓글을 찾으려는 게시글
     * @return json객체로 반환
     */
    public JSONObject getCommentsToJSON(Post post) throws UnsupportedEncodingException {
        List<Comment> commentList = this.getComments(post);

        JSONObject result = new JSONObject();
        result.put("post_id", post.getId());
        result.put("access", true);

        JSONArray data = new JSONArray();

        for (Comment comment : commentList) {
            JSONObject jsonComment = new JSONObject();

            jsonComment.put("comment_id", comment.getId());
            jsonComment.put("user_name", URLEncoder.encode(comment.getUserName(), "UTF-8")
                    .replace("*", "%2A")
                    .replace("+", "%20")
//                    .replace("%","%25")
                    .replace("%7E", "~")
                    .replace("%3F","?"));
            jsonComment.put("content", URLEncoder.encode(comment.getContent(), "UTF-8")
                    .replace("*", "%2A")
                    .replace("+", "%20")
                    .replace("%7E", "~")
                    .replace("%3F","?"));
            jsonComment.put("created_date", comment.getCreateDate());
            //대댓글 json 으로
            JSONArray replies = new JSONArray();
            if (comment.getReplies().size() > 0) {
                for (Comment replyComment : comment.getReplies()) {
                    JSONObject reply = new JSONObject();

                    reply.put("comment_id", replyComment.getId());
                    reply.put("user_name", URLEncoder.encode(replyComment.getUserName(), "UTF-8")
                            .replace("*", "%2A")
                            .replace("+", "%20")
                            .replace("%7E", "~")
                            .replace("%3F","?"));
                    reply.put("content", URLEncoder.encode(replyComment.getContent(), "UTF-8")
                            .replace("*", "%2A")
                            .replace("+", "%20")
                            .replace("%7E", "~")
                            .replace("%3F","?"));

                    reply.put("created_date", replyComment.getCreateDate());
                    replies.put(reply);
                }
            }
            jsonComment.put("replies", replies);
            data.put(jsonComment);
        }
        result.put("data", data);

        return result;
    }

    /**
     * 해당 유저가 댓글이나 대 댓글을 달 때 작동하는 함수
     * 유저가 댓글을 달 수 있는지 검증(프로젝트에 소속해 있는가?)
     *
     * @param comment
     * @param user
     * @return 성공시 true 실패시 false 반환
     */
    public boolean addComment(Comment comment, User user) {
        boolean result = false;
        Post post = postRepository.findById(comment.getPostId());

//        유저가 댓글을 달 수 있는지 검증(프로젝트에 소속해 있는가?)
        ProjectMember isUserJoin = projectMemberRepository.findByProjectAndUser(post.getProject(), user);
        //댓글 작성 가능한 경우
        if (isUserJoin != null) {
            Integer uc = 0;
            uc = commentRepository.addComment(comment);
            if (uc == 1) {
                result = true;
            }
        }

        return result;
    }

    /**
     * 댓글 삭제시 실행되는 로직
     *
     * @param comment 삭제할 댓글
     * @param user    삭제를 실행하는 유저
     * @return 성공시 true, 실패시 false 반환
     */
    public boolean deleteComment(Comment comment, User user) {
        boolean result = false;
//        Post post = postRepository.findById(comment.getPostId());
        Post post = comment.getPost();
        comment = commentRepository.findCommentById(comment.getId());
//        유저가 댓글을 삭제 할 수 있는지 검증(작성자이거나 파티장인가?)
        post.setParty(partyRepository.findById(post.getPartyId()));
        if (user.getId().equals(post.getParty().getLeaderId()) || user.getId().equals(post.getUserId()) || user.getId().equals(comment.getUserId())) {
            Integer uc = 0;
            uc = commentRepository.deleteComment(comment);
            if (uc == 1) {
                result = true;
            }
        }

        return result;
    }

}
