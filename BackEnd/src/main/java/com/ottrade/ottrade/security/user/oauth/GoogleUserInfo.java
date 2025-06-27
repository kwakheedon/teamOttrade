package com.ottrade.ottrade.security.user.oauth;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    public String getProvider() { return "google"; }
    public String getProviderId() { return (String) attributes.get("sub"); }
    public String getEmail() { return (String) attributes.get("email"); }
    public String getName() { return (String) attributes.get("name"); }
}
