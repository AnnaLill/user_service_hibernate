package org.example.service;

import org.example.dao.UserDao;
import org.example.dto.CreateUserDto;
import org.example.dto.UpdateUserDto;
import org.example.exception.UserNotFoundException;
import org.example.exception.ValidationException;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createWithValidDtoCallsDaoCreateAndReturnsUser() {

        CreateUserDto dto = new CreateUserDto("Vova", "vova@icloud.com", 30);


        when(userDao.create(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


        User created = userService.create(dto);


        assertEquals("Vova", created.getName());
        assertEquals("vova@icloud.com", created.getEmail());
        assertEquals(30, created.getAge());


        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao, times(1)).create(captor.capture());
        User passedToDao = captor.getValue();
        assertNull(passedToDao.getId());
    }

    @Test
    void createWithInvalidEmailThrowsValidationExceptionAndDoesNotCallDao() {

        CreateUserDto dto = new CreateUserDto("Vova", "poihpu", 30);


        assertThrows(ValidationException.class, () -> userService.create(dto));


        verifyNoInteractions(userDao);
    }

    @Test
    void updateExistingUserUpdatesFieldsAndCallsDaoUpdate() {
        Long id = 1L;
        User existing = new User(id, "Vova", "vova@icloud", 20, LocalDateTime.now());
        UpdateUserDto dto = new UpdateUserDto("new@icloud.com", 25);


        when(userDao.findById(id)).thenReturn(Optional.of(existing));

        when(userDao.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


        User updated = userService.update(id, dto);


        assertEquals("new@icloud.com", updated.getEmail());
        assertEquals(25, updated.getAge());


        verify(userDao).findById(id);
        verify(userDao).update(existing);
    }

    @Test
    void updateMissingUserThrowsUserNotFoundException() {
        Long id = 99L;
        UpdateUserDto dto = new UpdateUserDto("new@icloud.com", 25);


        when(userDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(id, dto));


        verify(userDao, never()).update(any());
    }

    @Test
    void deleteExistingUserCallsDaoDelete() {
        Long id = 1L;

        when(userDao.findById(id)).thenReturn(Optional.of(mock(User.class)));

        userService.delete(id);

        verify(userDao).findById(id);
        verify(userDao).delete(id);
    }

    @Test
    void deleteMissingUserThrowsUserNotFoundException() {
        Long id = 1L;
        when(userDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(id));

        verify(userDao, never()).delete(anyLong());
    }
}

