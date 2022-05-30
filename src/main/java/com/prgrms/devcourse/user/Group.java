package com.prgrms.devcourse.user;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@Table(name = "group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<GroupPermission> permissions = new ArrayList<>();

    public void addPermission(GroupPermission groupPermission) {
        groupPermission.setGroup(this);
    }

    public List<GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(gp -> new SimpleGrantedAuthority(
                        gp.getPermission().getName()))
                .collect(toList());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("authorities", getAuthorities())
                .toString();
    }
}
