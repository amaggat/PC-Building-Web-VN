package backend.feedbacks;

import backend.article.Post;
import backend.common.NameEntity;
import backend.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "feedbacks")
public class Feedbacks extends NameEntity {

    @Column(name = "details")
    @NotEmpty
    private String details;

    @Column(name = "creationtime")
    @NotEmpty
    private String creationtime;

    @ManyToOne
    @JoinColumn(name = "creatorid")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "postid")
    private Post post;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(String creationtime) {
        this.creationtime = creationtime;
    }

    public Integer getUser() {
        return user.getId();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPost() {
        return post.getId();
    }

    public void setPost(Post post) {
        this.post = post;
    }


}
