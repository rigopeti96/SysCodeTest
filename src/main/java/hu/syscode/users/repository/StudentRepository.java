package hu.syscode.users.repository;

import hu.syscode.users.data.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
    /**
     * Find all students in DB
     * @return list of students
     */
    List<Student> findAllByOrderByFullName();

    /**
     * Find a specific student with given id
     * @param id ID of student to find
     * @return the expected student
     */
    Student findStudentById(UUID id);

}
