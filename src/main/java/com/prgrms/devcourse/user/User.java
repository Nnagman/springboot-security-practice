package com.prgrms.devcourse.user;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "passwd")
    private String passwd;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("loginId", loginId)
                .append("passwd", passwd)
                .toString();
    }
}
