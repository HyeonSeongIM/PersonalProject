package project.personalproject.domain.post.post.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import project.personalproject.domain.post.comment.dto.PostCommentDTO;
import project.personalproject.domain.post.comment.service.PostCommentService;
import project.personalproject.domain.post.image.dto.PostImageDTO;
import project.personalproject.domain.post.image.service.PostImageService;
import project.personalproject.domain.post.post.graphql.dto.PostDTO;
import project.personalproject.domain.post.post.graphql.dto.PostListDTO;
import project.personalproject.domain.post.post.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostResolver {

    private final PostService postService;
    private final PostImageService postImageService;
    private final PostCommentService postCommentService;

    @QueryMapping
    public PostDTO getPost(@Argument long id) {
        return postService.getPost(id);
    }

    @QueryMapping
    public PostListDTO getPostList(@Argument int page,
                                   @Argument int size) {
        return postService.getPostList(PageRequest.of(page, size));
    }

    @SchemaMapping(typeName = "Post", field = "images")
    public List<PostImageDTO> getPostImage(PostDTO post,
                                           @Argument int page,
                                           @Argument int size) {
        return postImageService.getPostImageByPostId(post.id(), PageRequest.of(page, size));
    }

    @SchemaMapping(typeName = "Post", field = "comments")
    public List<PostCommentDTO> getPostComments(PostDTO post,
                                                @Argument int page,
                                                @Argument int size) {
        return postCommentService.getCommentByPost(post.id(), PageRequest.of(page, size));
    }

}
