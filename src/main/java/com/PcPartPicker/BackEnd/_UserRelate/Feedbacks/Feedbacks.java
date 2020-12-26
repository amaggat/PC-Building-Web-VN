package com.PcPartPicker.BackEnd._UserRelate.Feedbacks;

import com.PcPartPicker.BackEnd._UserRelate.Article.Article;
import com.PcPartPicker.BackEnd._UserRelate.NameEntity;
import com.PcPartPicker.BackEnd._UserRelate.User.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "feedbacks")
public class Feedbacks extends NameEntity {

    @Column(name = "details")
    @NotEmpty
    private String details;

    @Column(name = "creationtime")
    @NotEmpty
    private Date CreationTime;

    @ManyToOne
    @JoinColumn(name = "creatorid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PostID")
    private Article article;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(Date creationTime) {
        CreationTime = creationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}