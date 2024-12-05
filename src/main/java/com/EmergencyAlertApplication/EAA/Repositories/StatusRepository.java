package com.EmergencyAlertApplication.EAA.Repositories;

import com.EmergencyAlertApplication.EAA.Entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StatusRepository extends JpaRepository<Status, UUID> {

    Status findByName(String name);

}
