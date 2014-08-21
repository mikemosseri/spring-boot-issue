package demo;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
  public User findByUsername(final String username);
}
