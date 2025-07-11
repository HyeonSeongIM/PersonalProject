package project.personalproject.domain.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.personalproject.domain.search.entity.Search;
import project.personalproject.domain.search.repository.SearchRepository;
import project.personalproject.domain.search.service.AutocompleteService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AutocompleteServiceImpl implements AutocompleteService {

    private final SearchRepository searchRepository;

    /**
     * 사용자가 입력한 접두어(prefix)로 시작하는 게시글 제목 10개를 자동완성 추천.
     *
     * @param prefix 사용자 입력 접두어
     * @return 제목 추천 리스트
     */
    @Override
    public List<String> suggestTitle(String prefix) {
        List<Search> results = searchRepository.findTop10ByTitleStartingWith(prefix);

        return results.stream()
                .map(Search::getTitle)
                .toList();
    }
}
