package io.github.soundbar91.aipipeline.domain.auth.service.social;

import io.github.soundbar91.aipipeline.domain.user.entity.SocialProvider;

public interface SocialAuthService {
    SocialProvider getProvider();
    SocialUserInfo getUserInfo(String accessToken);
}
