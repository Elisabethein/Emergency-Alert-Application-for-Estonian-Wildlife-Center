package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.UserToRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserToRoleRepository extends JpaRepository<UserToRole, UUID> {
    List<UserToRole> findByUserId(UUID id);

    void deleteByUserId(UUID id);
}
