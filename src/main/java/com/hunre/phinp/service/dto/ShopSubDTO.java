package com.hunre.phinp.service.dto;

import com.hunre.phinp.config.Constants;
import com.hunre.phinp.domain.Authority;
import com.hunre.phinp.domain.ShopSub;
import com.hunre.phinp.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class ShopSubDTO {
    private Long id;
    private String username;
    private String password;
    private String otp;
    private String token;
    private String name;
    private String message;
    private String status;
    private ZonedDateTime createDate;
    private ZonedDateTime updateDate;

    private Long shopId;

    public ShopSubDTO() {
        // Empty constructor needed for Jackson.
    }

    public ShopSubDTO(ShopSub shopSub) {
        this.id = shopSub.getId();
        this.createDate = shopSub.getCreateDate();
        this.message = shopSub.getMessage();
        this.name = shopSub.getName();
        this.password = shopSub.getPassword();
        this.status = shopSub.getStatus();
        this.token = shopSub.getToken();
        this.updateDate = shopSub.getUpdateDate();
        this.username = shopSub.getUsername();
        this.shopId = shopSub.getShopId().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
