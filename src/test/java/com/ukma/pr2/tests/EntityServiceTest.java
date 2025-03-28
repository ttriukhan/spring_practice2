package com.ukma.pr2.tests;

import com.ukma.pr2.entity.EventEntity;
import com.ukma.pr2.entity.EventRepository;
import com.ukma.pr2.settings.BaseTest;
import com.ukma.pr2.settings.EntityService;
import com.ukma.pr2.utils.Utils;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EntityServiceTest extends BaseTest {

    @Autowired
    private EntityService entityService;

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testSaveAndFind() {
        EventEntity event = Utils.getRandomEvent();
        entityService.save(event);

        EventEntity foundEvent = entityService.findById(EventEntity.class, event.getId());
        assertNotNull(foundEvent);
        assertEquals(event.getId(), foundEvent.getId());
    }

    @Test
    public void testThatPersistWithSameIdentifierThrowException() {
        EventEntity first = Utils.getRandomEvent();
        EventEntity second = Utils.getRandomEvent();
        second.setId(first.getId());
        entityService.save(first);
        assertThrows(EntityExistsException.class, () -> entityService.save(second));
    }

    @Test
    public void testMerge() {
        EventEntity event = Utils.getRandomEvent();
        entityService.save(event);
        event.setName("Updated Event");
        EventEntity mergedEvent = (EventEntity) entityService.update(event);

        EventEntity foundEvent = entityService.findById(EventEntity.class, mergedEvent.getId());
        assertEquals("Updated Event", foundEvent.getName());
    }

    @Test
    public void testRemove() {
        EventEntity event = Utils.getRandomEvent();
        entityService.save(event);
        entityService.delete(event);

        EventEntity foundEvent = entityService.findById(EventEntity.class, event.getId());
        assertNull(foundEvent);
    }

    @Test
    public void testDetach() {
        EventEntity event = Utils.getRandomEvent();
        eventRepository.saveAndFlush(event);
        entityService.detach(event);
        event.setName("Detached Event");
        EventEntity foundEvent = eventRepository.findById(event.getId()).orElseThrow();
        assertNotEquals("Detached Event", foundEvent.getName());
    }

    @Test
    public void testRefresh() {
        EventEntity event = Utils.getRandomEvent();
        eventRepository.saveAndFlush(event);
        event.setName("Modified Name");
        event = (EventEntity) entityService.update(event);
        entityService.updateFromDB(event);
        EventEntity foundEvent = eventRepository.findById(event.getId()).orElseThrow();
        assertNotEquals("Modified Name", foundEvent.getName());
    }

    @Test
    public void testFind() {
        EventEntity event = Utils.getRandomEvent();
        eventRepository.saveAndFlush(event);
        assertEquals(event, entityService.findById(EventEntity.class, event.getId()));
    }

}
