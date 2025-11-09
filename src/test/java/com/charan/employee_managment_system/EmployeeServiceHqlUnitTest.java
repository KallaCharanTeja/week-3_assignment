package com.charan.employee_managment_system;
import com.charan.employee_managment_system.Service.EmployeeService_hql;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Optional;

import com.charan.employee_managment_system.dto.createEmployeewithPhn;
import com.charan.employee_managment_system.model.Employee;
import com.charan.employee_managment_system.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmployeeServiceHqlUnitTest {

    @Mock
    private EmployeeRepository repo;

    @InjectMocks
    private EmployeeService_hql service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByNameHQL_whenFound_returnsOptionalEmployee() {
        Employee e = new Employee("Kalla", "C", "k@c.com", "addr", "111");
        when(repo.findByfirstNameIgnoreCaseHQL("Kalla")).thenReturn(Optional.of(e));

        Optional<Employee> out = service.findByNameHQL("Kalla");

        assertTrue(out.isPresent());
        assertEquals("k@c.com", out.get().getEmail());
        verify(repo).findByfirstNameIgnoreCaseHQL("Kalla");
    }

    @Test
    void findByEmailHQL_whenNotFound_returnsEmpty() {
        when(repo.findByEmailHQL("noone@example.com")).thenReturn(Optional.empty());

        Optional<Employee> out = service.findByEmailHQL("noone@example.com");

        assertTrue(out.isEmpty());
        verify(repo).findByEmailHQL("noone@example.com");
    }

    @Test
    void createEmployeeWithPhn_savesAndReturnsEmployee() {
        createEmployeewithPhn dto = new createEmployeewithPhn();
        dto.setFirstName("A");
        dto.setLastName("B");
        dto.setEmail("a@b.com");
        dto.setAddress("addr");
        dto.setPhn("999");

        Employee saved = new Employee("A", "B", "a@b.com", "addr", "999");
        when(repo.save(any(Employee.class))).thenReturn(saved);

        Employee out = service.createEmployeeWithPhn(dto);

        assertNotNull(out);
        assertEquals("a@b.com", out.getEmail());
        verify(repo).save(any(Employee.class));
    }

    @Test
    void deleteEmployeeByMailHQL_whenNotExists_returnsInformativeMessage() {
        when(repo.existsByemailHQL("x@y.com")).thenReturn(false);

        String res = service.deleteEmployeeByMailHQL("x@y.com");

        assertTrue(res.contains("There is no employee"));
        verify(repo).existsByemailHQL("x@y.com");
        verify(repo, never()).deleteByemailHQL(anyString());
    }

    @Test
    void deleteEmployeeByMailHQL_whenExists_deletesAndReturnsSuccessMessage() {
        when(repo.existsByemailHQL("d@e.com")).thenReturn(true);
        doNothing().when(repo).deleteByemailHQL("d@e.com");

        String res = service.deleteEmployeeByMailHQL("d@e.com");

        assertTrue(res.toLowerCase().contains("deleted with email"));
        verify(repo).existsByemailHQL("d@e.com");
        verify(repo).deleteByemailHQL("d@e.com");
    }

    @Test
    void updateEmployeePhn_whenUpdated_returnsSuccessMessage() {
        when(repo.updateEmployeePhnHQL(5L, "777")).thenReturn(1);

        String res = service.updateEmployeePhn(5L, "777");

        assertTrue(res.contains("updated successfully"));
        verify(repo).updateEmployeePhnHQL(5L, "777");
    }

    @Test
    void updateEmployeePhn_whenNoRowAffected_returnsNotFoundMessage() {
        when(repo.updateEmployeePhnHQL(99L, "000")).thenReturn(0);

        String res = service.updateEmployeePhn(99L, "000");

        assertTrue(res.contains("There is no Employee"));
        verify(repo).updateEmployeePhnHQL(99L, "000");
    }

    @Test
    void updateEmployeeDetails_whenUpdated_returnsSuccessMessage() {
        when(repo.updateEmployeeDetailsHQL(3L, "111", "newAddr", "NewLast")).thenReturn(1);

        String res = service.updateEmployeeDetails(3L, Map.of(
            "phn", "111",
            "address", "newAddr",
            "lastName", "NewLast"
        ));

        assertTrue(res.contains("updated successfully"));
        verify(repo).updateEmployeeDetailsHQL(3L, "111", "newAddr", "NewLast");
    }

    @Test
    void updateEmployeeDetails_whenNotFound_returnsNotFoundMessage() {
        when(repo.updateEmployeeDetailsHQL(500L, null, null, null)).thenReturn(0);

        String res = service.updateEmployeeDetails(500L, Map.of());

        assertTrue(res.contains("not found"));
        verify(repo).updateEmployeeDetailsHQL(500L, null, null, null);
    }
}