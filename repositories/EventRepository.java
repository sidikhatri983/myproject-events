package com.even.gestion.repositories;

import com.even.gestion.models.AppUser;
import com.even.gestion.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // You can add custom queries if necessary
    List<Event> findByUser(AppUser user);
    List<Event> findByLocationIgnoreCase(String city);
}

