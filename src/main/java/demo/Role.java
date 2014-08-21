package demo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role implements Serializable, GrantedAuthority {
  private static final long serialVersionUID = -7549607287882878972L;

  @Id
  @GeneratedValue
  private Long id;
  
  private String name;

  /**
   * @return the name property (getAuthority required by GrantedAuthority interface)
   * @see org.springframework.security.core.GrantedAuthority#getAuthority()
   */
  @Transient
  public String getAuthority() {
    return getName();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  
}
