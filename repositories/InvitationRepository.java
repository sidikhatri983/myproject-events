package com.even.gestion.repositories;

import com.even.gestion.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    // You can add custom queries if necessary
}

