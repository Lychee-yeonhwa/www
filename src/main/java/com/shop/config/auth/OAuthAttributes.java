package com.shop.config.auth;

import com.shop.constant.Role;
import com.shop.entity.Member;
import com.shop.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email,			 String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
    /* of()
     * OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나 변환
     */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /* toEntity()
     * User 엔티티 생성
     * OAuthAttributes에서 엔티티 생성 시점 = 처음 가입 시
     * OAuthAttributes 클래스 생성이 끝났으면 같은 패키지에 SessionUser 클래스 생성
     */

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)	// 가입 기본 권한 == GUEST
                .build();
    }
}