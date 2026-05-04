package models;

import enums.UserTier;

public class User {
    private UserTier tier;
    private String userId;

    public UserTier getTier() {
        return tier;
    }

    public void setTier(UserTier tier) {
        this.tier = tier;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
