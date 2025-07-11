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

    @Override
    public List<String> suggestTitle(String prefix) {
        List<Search> results = searchRepository.findTop10ByTitleStartingWith(prefix);

        return results.stream()
                .map(Search::getTitle)
                .toList();
    }
}
