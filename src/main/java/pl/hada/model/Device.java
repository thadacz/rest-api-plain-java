package pl.hada.model;

import pl.hada.exception.ValidationException;

public class Device implements Entity {

  private Long id;
  private String name;
  private String model;

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Device(String name, String model) {
    this.name = name;
    this.model = model;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  @Override
  public void validate() {
    if (name == null || model == null) {
      throw new ValidationException("Validation error: Parameters are incorrect.");
    }
  }
}
