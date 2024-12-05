package com.EmergencyAlertApplication.EAA.Services;

import com.EmergencyAlertApplication.EAA.Entities.Tag;
import com.EmergencyAlertApplication.EAA.Entities.User;
import com.EmergencyAlertApplication.EAA.Entities.UserToTag;
import com.EmergencyAlertApplication.EAA.Repositories.TagRepository;
import com.EmergencyAlertApplication.EAA.Repositories.UserToTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserToTagService {
    private final UserToTagRepository userToTagRepository;
    private final TagRepository tagRepository;

    @Autowired
    public UserToTagService(UserToTagRepository userToTagRepository, TagRepository tagRepository) {
        this.userToTagRepository = userToTagRepository;
        this.tagRepository = tagRepository;
    }

    public List<UserToTag> findAll() {
        return userToTagRepository.findAll();
    }

    public Optional<UserToTag> findById(UUID id) {
        return userToTagRepository.findById(id);
    }

    public UserToTag save(UserToTag userToTag) {
        return userToTagRepository.save(userToTag);
    }

    public void deleteById(UUID id) {
        userToTagRepository.deleteById(id);
    }

    public List<String> getUserTags(UUID id) {
        List<UserToTag> userTags = userToTagRepository.findByUserId(id);
        return userTags.stream().map(userToTag -> userToTag.getTag().getFunction()).toList();
    }

    @Transactional
    public void editUserTags(UUID id, User editedUser, List<String> tags) {
        try {
            userToTagRepository.deleteByUserId(id);
        } catch (Exception e) {
            System.err.println("Error deleting user regions: " + e.getMessage());
            throw e;
        }
        tags.forEach(tag -> {
            Tag tagObject = tagRepository.findByFunction(tag);
            UserToTag userToTag = new UserToTag();
            userToTag.setUser(editedUser);
            userToTag.setTag(tagObject);
            userToTagRepository.save(userToTag);
        });
    }

    public void addUserToTag(User user, Tag tagObject) {
        UserToTag userToTag = new UserToTag();
        userToTag.setUser(user);
        userToTag.setTag(tagObject);
        userToTagRepository.save(userToTag);
    }
}
