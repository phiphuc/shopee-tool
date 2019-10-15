package com.hunre.phinp.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A ShopMain.
 */
@Entity
@Table(name = "shop_main")
public class ShopMain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id")
    private String shopId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "link_shop")
    private String linkShop;

    @Column(name = "name")
    private String name;

    @Column(name = "product")
    private String product;

    @Column(name = "follow")
    private String follow;

    @Column(name = "following")
    private String following;

    @Column(name = "rate")
    private String rate;

    @Column(name = "address")
    private String address;

    @Column(name = "version")
    private String version;

    @Column(name = "error_msg")
    private String errorMsg;

    @Column(name = "error")
    private String error;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "update_date")
    private ZonedDateTime updateDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public ShopMain shopId(String shopId) {
        this.shopId = shopId;
        return this;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getUserId() {
        return userId;
    }

    public ShopMain userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLinkShop() {
        return linkShop;
    }

    public ShopMain linkShop(String linkShop) {
        this.linkShop = linkShop;
        return this;
    }

    public void setLinkShop(String linkShop) {
        this.linkShop = linkShop;
    }

    public String getName() {
        return name;
    }

    public ShopMain name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public ShopMain product(String product) {
        this.product = product;
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getFollow() {
        return follow;
    }

    public ShopMain follow(String follow) {
        this.follow = follow;
        return this;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getFollowing() {
        return following;
    }

    public ShopMain following(String following) {
        this.following = following;
        return this;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getRate() {
        return rate;
    }

    public ShopMain rate(String rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAddress() {
        return address;
    }

    public ShopMain address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVersion() {
        return version;
    }

    public ShopMain version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ShopMain errorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getError() {
        return error;
    }

    public ShopMain error(String error) {
        this.error = error;
        return this;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public ShopMain createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public ShopMain updateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopMain)) {
            return false;
        }
        return id != null && id.equals(((ShopMain) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShopMain{" +
            "id=" + getId() +
            ", shopId='" + getShopId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", linkShop='" + getLinkShop() + "'" +
            ", name='" + getName() + "'" +
            ", product='" + getProduct() + "'" +
            ", follow='" + getFollow() + "'" +
            ", following='" + getFollowing() + "'" +
            ", rate='" + getRate() + "'" +
            ", address='" + getAddress() + "'" +
            ", version='" + getVersion() + "'" +
            ", errorMsg='" + getErrorMsg() + "'" +
            ", error='" + getError() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            "}";
    }
}
