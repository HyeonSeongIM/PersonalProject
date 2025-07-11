package project.personalproject.domain.search.dto.request;

import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;

public record SearchFilter(
        PostTag tag,
        PostCategory category
) {
}
