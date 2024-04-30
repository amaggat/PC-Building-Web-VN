package backend.user;

import backend.article.Post;
import backend.common.NameEntity;
import backend.pcprofile.PcProfile;
import backend.recommendation.rating.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User extends NameEntity {

    public User(String username, Integer id) {
        this.username = username;
        super.setId(id);
    }

    @Column(name = "username", unique = true)
    private String username = new String();

    @Column(name = "password")
    private String password = new String();

    @Column(name = "mail")
    private String mail = new String();

    @Column(name = "role")
    private String role = new String();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<PcProfile> pcProfile;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<CpuRating> cpuRatingList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<GpuRating> gpuRatingList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<MainboardRating> mainboardRatingList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<HddRating> hddRatingList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<SsdRating> ssdRatingList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<PsuRating> psuRatingList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<RamRating> ramRatingList;

    public User(String username, String password, String mail, String role) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.role = role;
    }

    public User() {

    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<PcProfile> getPcProfile() {
        return pcProfile;
    }

    public void setPcProfile(List<PcProfile> pcProfile) {
        this.pcProfile = pcProfile;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<CpuRating> getCpuRatingList() {
        return cpuRatingList;
    }

    public void setCpuRatingList(List<CpuRating> cpuRatingList) {
        this.cpuRatingList = cpuRatingList;
    }

    public List<GpuRating> getGpuRatingList() {
        return gpuRatingList;
    }

    public void setGpuRatingList(List<GpuRating> gpuRatingList) {
        this.gpuRatingList = gpuRatingList;
    }

    public List<MainboardRating> getMainboardRatingList() {
        return mainboardRatingList;
    }

    public void setMainboardRatingList(List<MainboardRating> mainboardRatingList) {
        this.mainboardRatingList = mainboardRatingList;
    }

    public List<HddRating> getHddRatingList() {
        return hddRatingList;
    }

    public void setHddRatingList(List<HddRating> hddRatingList) {
        this.hddRatingList = hddRatingList;
    }

    public List<SsdRating> getSsdRatingList() {
        return ssdRatingList;
    }

    public void setSsdRatingList(List<SsdRating> ssdRatingList) {
        this.ssdRatingList = ssdRatingList;
    }

    public List<PsuRating> getPsuRatingList() {
        return psuRatingList;
    }

    public void setPsuRatingList(List<PsuRating> psuRatingList) {
        this.psuRatingList = psuRatingList;
    }

    public List<RamRating> getRamRatingList() {
        return ramRatingList;
    }

    public void setRamRatingList(List<RamRating> ramRatingList) {
        this.ramRatingList = ramRatingList;
    }
}
