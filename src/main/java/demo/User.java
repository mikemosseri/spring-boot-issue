package demo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements Serializable, UserDetails {
  private static final long serialVersionUID = 4999960040345422742L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String username;

  private String password;

  @Column(name = "account_enabled")
  private boolean enabled = true;

  @Column(name = "account_expired")
  private boolean accountExpired = false;

  @Column(name = "account_locked")
  private boolean accountLocked = false;

  @Column(name = "credentials_expired")
  private boolean credentialsExpired = false;

  @ManyToMany(fetch = FetchType.LAZY)
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<Role> roles = new HashSet<Role>();
  

  /**
   * @return GrantedAuthority[] an array of roles.
   * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
   */
  @Transient
  public Set<GrantedAuthority> getAuthorities() {
    final Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
    authorities.addAll(roles);
    return authorities;
  }

  /**
   * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
   */
  @Transient
  public boolean isAccountNonExpired() {
    return !isAccountExpired();
  }

  /**
   * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
   */
  @Transient
  public boolean isAccountNonLocked() {
    return !isAccountLocked();
  }

  /**
   * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
   */
  @Transient
  public boolean isCredentialsNonExpired() {
    return !credentialsExpired;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isAccountExpired() {
    return accountExpired;
  }

  public void setAccountExpired(boolean accountExpired) {
    this.accountExpired = accountExpired;
  }

  public boolean isAccountLocked() {
    return accountLocked;
  }

  public void setAccountLocked(boolean accountLocked) {
    this.accountLocked = accountLocked;
  }

  public boolean isCredentialsExpired() {
    return credentialsExpired;
  }

  public void setCredentialsExpired(boolean credentialsExpired) {
    this.credentialsExpired = credentialsExpired;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}
