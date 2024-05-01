package backend.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;

    @Column(name = "action")
    private String action;

    @Column(name = "componentid")
    private String componentId;

    @Column(name = "requestAt")
    private Timestamp requestDate;

    public UserActivity(User user, String action, String componentId) {
        this.id = user.getId() + "-" + componentId + "-" + action;
        this.user = user;
        this.action = action;
        this.componentId = componentId;
        this.requestDate = new Timestamp(System.currentTimeMillis());
    }

}
