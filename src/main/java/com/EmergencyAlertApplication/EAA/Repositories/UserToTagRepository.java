package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.UserToTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserToTagRepository extends JpaRepository<UserToTag, UUID> {

    List<UserToTag> findByUserId(UUID id);

    void deleteByUserId(UUID id);
}
