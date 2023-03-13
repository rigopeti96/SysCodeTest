package hu.syscode.users.controller;

import hu.syscode.users.data.Student;
import hu.syscode.users.exception.StudentException;
import hu.syscode.users.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentRepository studentRepository;

    /**
     * Initialize repository
     * @param studentRepository variable to initialize
     */
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * List method to find all student
     * @return list of all student
     */
    @GetMapping("/getStudents")
    public List<Student> getStudents(){
        return studentRepository.findAllBy();
    }

    /**
     * Modify the specific student
     * It is successful if the request body has UUID
     * @return status message if the modification was successful or not
     */
    @PutMapping("/modifyStudent")
    public String modifyStudent(
            @RequestBody Student modifiedStudent
    ) throws StudentException {
        if(modifiedStudent.getId() != null){
            Student actStudent = studentRepository.findStudentById(UUID.fromString(String.valueOf(modifiedStudent.getId())));
            if(actStudent == null){
                throw new StudentException("Student with given Id does not exits!");
            }
            if(modifiedStudent.getFullName() != null){
                actStudent.setFullName(modifiedStudent.getFullName());
            }

            if(modifiedStudent.getEmailAddress() != null && patternMatches(modifiedStudent.getEmailAddress(), "^(.+)@(\\S+)$")){
                actStudent.setEmailAddress(modifiedStudent.getEmailAddress());
            }

            studentRepository.save(actStudent);
            return "Save was successful!";
        }

        throw new StudentException("ID field must not be empty!");
    }

    /**
     * Add new student to the database
     * We accept entity even if it has empty parameters
     * @param newStudent new student to add
     * @return status message about success of save
     */
    @PostMapping("/addStudent")
    public String addStudent(
            @RequestBody Student newStudent
    ) throws StudentException {
        if(newStudent.getId() != null){
            throw new StudentException("Student with given Id does not exits!");
        }

        if(newStudent.getEmailAddress() != null && !patternMatches(newStudent.getEmailAddress(), "^(.+)@(\\S+)$")){
            throw new StudentException("Email address is not valid!");
        }

        studentRepository.save(newStudent);
        return "Save was successful!";
    }

    /**
     * Remove student with given ID
     * @param removeStudent student to remove
     * @return status message if the delete method was successful
     * @throws StudentException custom exception if something went wrong
     */
    @DeleteMapping("/deleteStudent")
    public String deleteStudent(
            @RequestBody Student removeStudent
    ) throws StudentException {
        try {
            Student removeableStudent = studentRepository.findStudentById(UUID.fromString(String.valueOf(removeStudent.getId())));
            System.out.println("Removeable student: " + removeableStudent.getFullName());
            studentRepository.delete(removeableStudent);
            return "Remove student was successful!";
        } catch (Exception e){
            throw new StudentException("Student was not found!");
        }
    }

    /**
     * Helper method to validate email address at create or modify student
     * @param emailAddress given email address in message
     * @param regexPattern pattern to validate email address
     * @return true if the given address is valid, else false
     */
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
