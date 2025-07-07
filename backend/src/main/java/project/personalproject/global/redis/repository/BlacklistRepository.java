package project.personalproject.global.redis.repository;

import org.springframework.data.repository.CrudRepository;
import project.personalproject.global.redis.domain.Blacklist;


public interface BlacklistRepository extends CrudRepository<Blacklist, String> {
}
