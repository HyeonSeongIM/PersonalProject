package project.personalproject.domain.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.search.service.AutocompleteService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AutocompleteServiceImpl implements AutocompleteService {

    @Override
    public List<String> suggestTitle(String prefix) {
        return List.of();
    }
}
