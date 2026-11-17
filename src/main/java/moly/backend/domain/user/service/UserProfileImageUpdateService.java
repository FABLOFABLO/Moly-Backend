package moly.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import moly.backend.global.s3.S3Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileImageUpdateService {
    private final UserRepository userRepository;
    private final S3Utils s3Utils;

    @Transactional
    public void execute(Long id, MultipartFile profileImage) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        String profileImageKey = s3Utils.upload(profileImage, "profiles");
        String profileImageUrl = s3Utils.getFileUrl(profileImageKey);

        user.updateProfileImage(profileImageUrl);
    }
}
