package com.prgrms.devcourse.user;

import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(name = "group_permission")
public class GroupPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "group_id")
    @ManyToOne(optional = false)
    private Group group;

    @JoinColumn(name = "permission_id")
    @ManyToOne(optional = false)
    private Permission permission;

    public void setGroup(Group group) {
        if (Objects.nonNull(this.group)) {
            this.group.getPermissions().remove(this);
        }
        this.group = group;
        group.getPermissions().add(this);
    }
}
