package com.charan.employee_managment_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.charan.employee_managment_system.model.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

    Optional<Employee> findByfirstNameIgnoreCase(String firstName);

    Optional<Employee> findByEmail(String email);
    
    boolean existsByemail(String email);

    void deleteByemail(String email);

    Optional<Employee> findByid(Long id);


    //HQL

    @Query("select e FROM Employee e where lower(e.firstName) = lower(:firstName)")
    Optional<Employee> findByfirstNameIgnoreCaseHQL(String firstName);

    @Query("select e FROM Employee e where e.email = :email")
    Optional<Employee> findByEmailHQL(String email);

    @Transactional
    @Modifying
    @Query("delete from Employee e where e.email = :email")
    void deleteByemailHQL(String email);

    @Query("""
        select count(e) > 0 
        from Employee e
        where e.email = :email
    """)
    boolean existsByemailHQL(String email);

    @Query("select e from Employee e where cast(e.id as int) =: id")
    Optional<Employee> findByidHQL(Long id);

    @Transactional
    @Modifying
    @Query("update Employee e set e.phn =:phn where e.id = :id")
    int updateEmployeePhnHQL(Long id,String phn);

    @Transactional
    @Modifying
    @Query("""
        update Employee e set 
        e.phn = coalesce(:phn,e.phn), 
        e.address = coalesce(:address,e.address), 
        e.lastName = coalesce(:lastName,e.lastName)
        where e.id = :id
    """)
    int updateEmployeeDetailsHQL(Long id,String phn, String address, String lastName);

    //Native Queries

    @Query(value = "select * FROM employees e where lower(e.first_name) = lower(?1)",nativeQuery = true)
    Optional<Employee> findByfirstNameIgnoreCaseSQL(String firstName);

    @Query(value = "select * FROM employees e where e.email = ?1",nativeQuery = true)
    Optional<Employee> findByEmailSQL(String email);

    @Transactional
    @Modifying
    @Query(value = "delete from employees e where e.email = ?1",nativeQuery = true)
    void deleteByemailSQL(String email);

    @Query(value = """
        select count(e) > 0 
        from employees e
        where e.email = ?1
    """,nativeQuery = true)
    boolean existsByemailSQL(String email);

    @Query(value = "select * from employees e where cast(e.id as int) = ?1",nativeQuery=true)
    Optional<Employee> findByidSQL(Long id);

    @Transactional
    @Modifying
    @Query(value = "update employees e set e.phn = ?2 where e.id = ?1",nativeQuery = true)
    int updateEmployeePhnSQL(Long id,String phn);

    @Transactional
    @Modifying
    @Query(value = """
        update employees e set 
        phn = coalesce(?2,phn), 
        address = coalesce(?3,address), 
        last_name = coalesce(?4,last_name)
        where e.id = ?1
    """,nativeQuery = true)
    int updateEmployeeDetailsSQL(Long id,String phn, String address, String lastName);

} 
