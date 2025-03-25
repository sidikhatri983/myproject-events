package com.even.gestion.repositories;

import com.even.gestion.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    public AppUser findByEmail(String email);
}
