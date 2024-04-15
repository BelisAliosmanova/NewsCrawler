package com.web.crawler.WebCrawler.repositories;

import com.web.crawler.WebCrawler.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
