package pl.hada.service;

import pl.hada.model.Entity;

import java.util.List;

public interface Service<T extends Entity> {

  void create(String requestBody);

  T getById(Long id);

  List<T> getAll();

  void update(Long id, String requestBody);

  void delete(Long id);
}
