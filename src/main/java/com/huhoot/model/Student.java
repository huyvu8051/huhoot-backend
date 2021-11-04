package com.huhoot.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EntityListeners({ AuditingEntityListener.class })
public class Student implements UserDetails{
	@Id
	@GeneratedValue
	private int id;
	
	@Column(unique = true)
	private String username;

	private String fullName;

	private String password;

	private boolean isDeleted;
	
	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date modifiedDate;

	@OneToMany(mappedBy = "primaryKey.student")
	private List<StudentChallenge> studentChallenges = new ArrayList<>();

	@OneToMany(mappedBy = "primaryKey.student", cascade = CascadeType.ALL)
	private List<StudentAnswer> studentAnswers = new ArrayList<>();

	public Student(String username, String fullName, String password) {
		this.username = username;
		this.fullName = fullName;
		this.password = password;
		this.isDeleted = false;
	}
	
	
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority("STUDENT"));

		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	

}
