package project.personalproject.domain.search.service;

import java.util.List;

public interface AutocompleteService {
    List<String> suggestTitle(String prefix);
}
