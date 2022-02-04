package service;

import entity.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import repository.LikeRepository;
import repository.PartyRepository;
import repository.PartyRepository_Impl_Maria;
import repository.PostRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final PartyRepository partyRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, PartyRepository partyRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.partyRepository = partyRepository;
    }

    /**
     * 게시글을 저장하는 함수
     * @param post 저장할 게시글 객체 ( 유저정보,파티정보,프로젝트 정보, 글 정보가 담겨있음)
     * @return 성공하면 true, 실패하면 false 반환
     */
    public boolean createPost(Post post){
        boolean result = false;

        //글 등록
        Post addResult = postRepository.addPost(post);
        if(addResult != null){
            //결과값 반환
            result = true;
        }

        return result;
    }

    /**
     * 프로젝트와 관련된 게시글을 전부 불러오는 함수
     * @param project 프로젝트
     * @return 값이 없어도 null이 아닌 new ArrayList<>()객체로 반환함
     */
    public List<Post> projectPosts(Project project){
        List<Post> result = postRepository.findPostsByProject(project);
        return result;
    }

    /**
     * 사용자가 게시글에 추천시 작동하는 함수
     * @param post 추천할 게시글
     * @param user 추천하는 사용자
     * @return 추천결과 메시지와 추천수를 json으로 반환한다.
     * {data:{msg:추천결과메시지,count:추천수}}
     */
    public JSONObject doLike(Post post,User user) throws UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        Integer uc = likeRepository.addLike(like);
        //추천 성공
        if(uc==1){
            data.put("access",true);
            data.put("msg",URLEncoder.encode("추천했습니다","UTF-8"));
        }
        else{
            data.put("access",true);
            data.put("msg", URLEncoder.encode("추천은한번만가능합니다","UTF-8"));
//            data.put("msg", "추천은한번만가능합니다");
        }
        List<Like> likes = likeRepository.findLikesByPost(post);
        data.put("count",likes.size());
        result.put("data",data);
        return result;
    }

    /**
     * 게시글의 추천수를 반환하는 함수
     * @param post 게시글 객체
     * @return Integer로 반환함
     */
    public Integer getLikes(Post post){
        List<Like> likes = likeRepository.findLikesByPost(post);
        return likes.size();
    }

    /**
     * 게시글과 게시글의 좋아요를 삭제하는 함수
     * @param post 삭제할 게시글
     * @param user 삭제명령을 지시한 사용자
     * @return 삭제에 성공하면 true, 실패시 fasle 반환
     */
    public boolean deletePost(Post post,User user){
        boolean result = false;
        Party party = partyRepository.findById(post.getPartyId());

        // 삭제 가능한 유저인가? (글작성자, 파티장)
        if(party.getLeaderId().equals(user.getId())||post.getUserId().equals(user.getId())){
            //좋아요 삭제
            likeRepository.deleteLikesByPost(post);
            // 삭제
            Integer uc = postRepository.deletePost(post);
            result = true;
        }

        return result;
    }


}
