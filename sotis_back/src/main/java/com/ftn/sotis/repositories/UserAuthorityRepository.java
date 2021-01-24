package com.ftn.sotis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ftn.sotis.entities.UserAuthority;


public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long>{

}
