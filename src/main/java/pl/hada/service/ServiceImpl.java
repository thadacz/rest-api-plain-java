package pl.hada.service;

import com.google.gson.Gson;
import pl.hada.exception.NotFoundException;
import pl.hada.model.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceImpl<T extends Entity> implements Service<T> {

  Logger logger = Logger.getLogger(ServiceImpl.class.getSimpleName());

  private final Map<Long, T> entities = new HashMap<>();
  private final AtomicLong nextId = new AtomicLong(1);
  private final Gson gson = new Gson();

  private final Class<T> entityType;

  public ServiceImpl(Class<T> entityType) {
    this.entityType = entityType;
  }

  public void create(String requestBody) {
    T entity = gson.fromJson(requestBody, entityType);
    entity.validate();
    entity.setId(nextId.getAndIncrement());
    entities.put(entity.getId(), entity);
  }

  public T getById(Long id) {
    T entity = entities.get(id);
    if (entity != null) {
      return entity;
    } else {
      logger.log(
          Level.WARNING, "Entity with id " + id + " cannot be found, because it doesn't exists.");
      throw new NotFoundException(
          "Entity with id " + id + " cannot be found, because it doesn't exists.");
    }
  }

  public List<T> getAll() {
    return entities.values().stream().toList();
  }

  public void update(Long id, String requestBody) {
    if (entities.get(id) != null) {
      T entity = gson.fromJson(requestBody, entityType);
      entity.setId(id);
      entities.put(id, entity);
    } else {
      logger.log(
          Level.WARNING, "Entity with id " + id + " cannot be updated, because it doesn't exists.");
      throw new NotFoundException(
          "Entity with id " + id + " cannot be updated, because it doesn't exists.");
    }
  }

  public void delete(Long id) {
    if (entities.get(id) != null) {
      entities.remove(id);
    } else {
      logger.log(
          Level.WARNING, "Entity with id " + id + " cannot be deleted, because doesn't exists.");
      throw new NotFoundException(
          "Entity with id " + id + " cannot be deleted, because doesn't exists.");
    }
  }
}
