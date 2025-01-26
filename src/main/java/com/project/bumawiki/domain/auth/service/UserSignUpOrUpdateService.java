package com.project.bumawiki.domain.auth.service;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.domain.user.exception.UserNotLoginException;
import com.project.bumawiki.global.annotation.ServiceWithTransactionalReadOnly;
import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.resource.BsmUserResource;
import leehj050211.bsmOauth.exception.BsmOAuthCodeNotFoundException;
import leehj050211.bsmOauth.exception.BsmOAuthInvalidClientException;
import leehj050211.bsmOauth.exception.BsmOAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@ServiceWithTransactionalReadOnly
public class UserSignUpOrUpdateService {
    private final BsmOauth bsmOauth;
    private final UserRepository userRepository;

    @Transactional
    public User execute(String authId) throws IOException {
        String token;
        BsmUserResource resource;
        try {
            token = bsmOauth.getToken(authId);
            resource = bsmOauth.getResource(token);
        }catch(BsmOAuthCodeNotFoundException | BsmOAuthTokenNotFoundException e){
            throw UserNotLoginException.EXCEPTION;
        }catch(BsmOAuthInvalidClientException e){
            throw UserNotLoginException.EXCEPTION;
        }
        return updateOrSignUp(resource);
    }

    @Transactional
    protected User updateOrSignUp(BsmUserResource resource) {
        Optional<User> user = userRepository.findByEmail(resource.getEmail());
        if(user.isEmpty()){
            return saveUser(resource);
        }
        User updateUser = user.get();
        return updateUser.update(resource);
    }

    @Transactional
    protected User saveUser(final BsmUserResource resource) {
        return userRepository.save(
                User.builder()
                        .email(resource.getEmail())
                        .nickName(resource.getNickname())
                        .authority(Authority.USER)
                        .enroll(resource.getStudent().getEnrolledAt())
                        .name(resource.getStudent().getName())
                        .build()
                );
    }
}
