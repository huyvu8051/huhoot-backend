package com.huhoot.model;

import com.huhoot.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({ AuditingEntityListener.class })
public class Admin extends Auditable implements CustomUserDetails {
	@Id
	@GeneratedValue
	private int id;

	@Column(unique = true)
	private String username;

	private String password;

	private Role role;

	private boolean isNonLocked;

	private UUID socketId;

	@OneToMany(mappedBy = "admin")
	private List<Challenge> challenges = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority(role.toString()));

		return authorities;
	}


	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	public Admin(String username, String password) {
		this.username = username;
		this.password = password;
		this.role = Role.HOST;
		this.isNonLocked = true;
	}
}
