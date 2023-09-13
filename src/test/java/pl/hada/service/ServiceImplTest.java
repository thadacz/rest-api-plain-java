package pl.hada.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.hada.exception.NotFoundException;
import pl.hada.exception.ValidationException;
import pl.hada.model.Device;
import pl.hada.model.Entity;

class ServiceImplTest {

    private ServiceImpl<Device> deviceService;

    @BeforeEach
    void setUp() {
        deviceService = new ServiceImpl<>(Device.class);
    }

    @Test
    void create() {

        String requestBody = "{\"name\": \"iPhone 14 Pro Max\",\"model\": \"A2651\"}";
        deviceService.create(requestBody);
        List<Device> entities = deviceService.getAll();
        assertEquals(1, entities.size());
        Device device = entities.get(0);
        assertEquals(1, device.getId());
        assertEquals("iPhone 14 Pro Max", device.getName());
        assertEquals("A2651", device.getModel());
        assertNotNull(device.getId());

        String requestBodyIncorrect = "{\"name\": \"iPhone 14 Pro Max\"}";
        assertThrows(ValidationException.class, () -> deviceService.create(requestBodyIncorrect));
    }

    @Test
    void getByIdNotFoundException() {
        long id = 1L;
        assertThrows(NotFoundException.class, () -> deviceService.getById(id));
    }

    @Test
    void getById() {
        long id = 1L;
        String requestBody = "{\"name\": \"iPhone 14 Pro Max\",\"model\": \"A2651\"}";
        Device device = new Device("iPhone 14 Pro Max", "A2651");
        deviceService.create(requestBody);
        assertEquals(device.getName(), deviceService.getById(id).getName());
    }

    @Test
    void getAll() {
        String requestBody = "{\"name\": \"iPhone 14 Pro Max\",\"model\": \"A2651\"}";
        Device device = new Device("iPhone 14 Pro Max", "A2651");
        List<Entity> entities = new ArrayList<>();
        entities.add(device);
        deviceService.create(requestBody);
        assertEquals(entities.size(), deviceService.getAll().size());
    }

    @Test
    void update() {
        long id = 1L;
        String requestBody = "{\"name\": \"iPhone 11\",\"model\": \"A2651\"}";
        deviceService.create(requestBody);
        String requestBodyToUpdate = "{\"name\": \"iPhone 14 Pro Max\",\"model\": \"A1312\"}";
        deviceService.update(id, requestBodyToUpdate);
        assertEquals("iPhone 14 Pro Max", deviceService.getById(id).getName());
    }

    @Test
    void updateNotFoundException() {
        assertThrows(NotFoundException.class, () -> deviceService.update(1L, ""));
    }

    @Test
    void delete() {
        long id = 1L;
        String requestBody = "{\"name\": \"iPhone 11\",\"model\": \"A2651\"}";
        deviceService.create(requestBody);
        deviceService.delete(id);
    }

    @Test
    void deleteNotFoundException() {
        assertThrows(NotFoundException.class, () -> deviceService.delete(1L));
    }
}