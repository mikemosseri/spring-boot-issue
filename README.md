Example project to show that Spring Security login doesn't also add the default OpenEntityManagerInViewInterceptor from Spring Boot's spring.jpa.openInView property.

There are two workarounds. If you left join fetch the roles in the UserRepo lookup, it passes the first test but will fail on a cache hit.

The second workaround is to add the following to the Application config.

 @Bean
  public FilterRegistrationBean openEntityManagerInViewFilter() {
      FilterRegistrationBean reg = new FilterRegistrationBean();
      reg.setName("OpenEntityManagerInViewFilter");
      reg.setFilter(new OpenEntityManagerInViewFilter());
      return reg;
  }
