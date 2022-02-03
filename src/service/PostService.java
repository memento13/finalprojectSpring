package service;

import entity.Post;
import entity.Project;
import entity.User;
import org.springframework.stereotype.Service;
import repository.PostRepository;
import repository.PostRepository_Impl_Maria;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
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
}
