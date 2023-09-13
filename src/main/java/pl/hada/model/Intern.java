package pl.hada.model;

import pl.hada.exception.ValidationException;

public class Intern implements Entity {

  private Long id;
  private String name;
  private String email;
  private String programmingLanguage;

  public Intern(Long id, String name, String email, String programmingLanguage) {
    this.name = name;
    this.email = email;
    this.programmingLanguage = programmingLanguage;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getProgrammingLanguage() {
    return programmingLanguage;
  }

  public void setProgrammingLanguage(String programmingLanguage) {
    this.programmingLanguage = programmingLanguage;
  }

  @Override
  public void validate() {
    if (name == null || email == null || programmingLanguage == null) {
      throw new ValidationException("Validation error: Parameters are incorrect.");
    }
  }
}
