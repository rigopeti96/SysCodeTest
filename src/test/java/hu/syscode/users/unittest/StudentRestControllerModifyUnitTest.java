package hu.syscode.users.unittest;

import hu.syscode.users.data.Student;
import hu.syscode.users.repository.StudentRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class StudentRestControllerModifyUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    /**
     * Test case: create a user as it is expected then we try to modify its email address
     * Expected result: successful save
     * @throws Exception if mockMvc.perform() method throws an Exception
     */
    @Test
    public void createStudentWithCorrectDataAndModifyItsEmailAddress() throws Exception {
        //Create a new student
        createTestStudent();

        //Get student and check its size
        List<Student> studentList = repository.findAllBy();
        assertEquals(1, studentList.size());

        //Modify the created student
        //The id should be in string format
        String exampleCourseModifyJson = "{\"id\":\""+ studentList.get(0).getId().toString() +"\",\"emailAddress\":\"student.alice2@gmail.com\"}";
        String expectedModifyMessage = "Student modification was successful!";

        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .put("/api/student/modifyStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseModifyJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult modifyResult = mockMvc.perform(requestModifyBuilder).andReturn();
        MockHttpServletResponse modifyResponse = modifyResult.getResponse();

        assertEquals(HttpStatus.OK.value(), modifyResponse.getStatus());
        assertEquals(expectedModifyMessage, modifyResponse.getContentAsString());

        List<Student> studentModifiedList = repository.findAllBy();
        Student modifiedStudent = studentModifiedList.get(0);
        assertEquals(studentList.get(0).getId().toString(), modifiedStudent.getId().toString());
        assertEquals("student.alice2@gmail.com", modifiedStudent.getEmailAddress());

        //reset database for further tests
        repository.deleteAll();
    }

    /**
     * Test case: create a user as it is expected then we try to modify its full name
     * Expected result: successful save
     * @throws Exception if mockMvc.perform() method throws an Exception
     */
    @Test
    public void createStudentWithCorrectDataAndModifyItsFullName() throws Exception {
        //Create a new student
        createTestStudent();

        //Get student and check its size
        List<Student> studentList = repository.findAllBy();
        assertEquals(1, studentList.size());

        //Modify the created student
        //The id should be in string format
        String exampleCourseModifyJson = "{\"id\":\""+ studentList.get(0).getId().toString() +"\",\"fullName\":\"Student Alice Emily\",\"emailAddress\":\"student.alice.emily@gmail.com\"}";
        String expectedModifyMessage = "Student modification was successful!";

        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .put("/api/student/modifyStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseModifyJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult modifyResult = mockMvc.perform(requestModifyBuilder).andReturn();
        MockHttpServletResponse modifyResponse = modifyResult.getResponse();

        assertEquals(HttpStatus.OK.value(), modifyResponse.getStatus());
        assertEquals(expectedModifyMessage, modifyResponse.getContentAsString());

        List<Student> studentModifiedList = repository.findAllBy();
        Student modifiedStudent = studentModifiedList.get(0);
        assertEquals(studentList.get(0).getId().toString(), modifiedStudent.getId().toString());
        assertEquals("Student Alice Emily", modifiedStudent.getFullName());
        assertEquals("student.alice.emily@gmail.com", modifiedStudent.getEmailAddress());

        //reset database for further tests
        repository.deleteAll();
    }

    /**
     * Test case: create multiple users as it is expected then we try to modify all data of one of them
     * Expected result: successful save
     * @throws Exception if mockMvc.perform() method throws an Exception
     */
    @Test
    public void createMultipleStudentWithCorrectDataAndModifyItsAllData() throws Exception {
        //Create a new student
        createTestStudent();

        //Create another student
        Student studentBob = new Student();
        studentBob.setEmailAddress("student.bob@gmail.com");
        studentBob.setFullName("Student Bob");
        repository.save(studentBob);

        //Get student and check its size
        List<Student> studentList = repository.findAllBy();
        assertEquals(2, studentList.size());

        //Get both students' id
        String aliceId = "";
        String bobId = "";
        for (Student student : studentList) {
            if (student.getFullName().equals("Student Alice")) {
                aliceId = student.getId().toString();
            }
            if (student.getFullName().equals("Student Bob")) {
                bobId = student.getId().toString();
            }
        }

        //Modify the created student
        //The id should be in string format
        String exampleCourseModifyJson = "{\"id\":\""+ aliceId +"\",\"fullName\":\"Student Alice Emily\"}";
        String expectedModifyMessage = "Student modification was successful!";

        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .put("/api/student/modifyStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseModifyJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult modifyResult = mockMvc.perform(requestModifyBuilder).andReturn();
        MockHttpServletResponse modifyResponse = modifyResult.getResponse();

        assertEquals(HttpStatus.OK.value(), modifyResponse.getStatus());
        assertEquals(expectedModifyMessage, modifyResponse.getContentAsString());

        List<Student> studentModifiedList = repository.findAllBy();
        assertEquals(2, studentModifiedList.size());

        //Check if the modification was succesful
        Student modifiedStudent = repository.findStudentById(UUID.fromString(aliceId));
        assertEquals(aliceId, modifiedStudent.getId().toString());
        assertEquals("Student Alice Emily", modifiedStudent.getFullName());
        assertEquals("student.alice@gmail.com", modifiedStudent.getEmailAddress());

        //Check if the other student's properties not modified
        Student originalStudent = repository.findStudentById(UUID.fromString(bobId));
        assertEquals(bobId, originalStudent.getId().toString());
        assertEquals("Student Bob", originalStudent.getFullName());
        assertEquals("student.bob@gmail.com", originalStudent.getEmailAddress());

        //reset database for further tests
        repository.deleteAll();
    }

    /**
     * Test case: create a user as it is expected then we try to modify its full name but without id
     * Expected result: server throw an exception with the declared message
     */
    @Test
    public void createStudentWithCorrectDataAndModifyWithoutId_ExpectedError() {
        //Create a new student
        createTestStudent();

        //Get student and check its size
        List<Student> studentList = repository.findAllBy();
        assertEquals(studentList.size(), 1);

        //Modify the created student
        //The id should be in string format
        String exampleCourseModifyJson = "{\"fullName\":\"Student Alice Emily\",\"emailAddress\":\"student.adrian@gmail.com\"}";
        String expectedMessage = "ID field must not be empty!";

        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .put("/api/student/modifyStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseModifyJson)
                .contentType(MediaType.APPLICATION_JSON);

        Exception exception = assertThrows(Exception.class, () -> {
            MvcResult result = mockMvc.perform(requestModifyBuilder).andReturn();
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        //reset database for further tests
        repository.deleteAll();
    }

    /**
     * Test case: create a user as it is expected then we try to modify its full name but with wrong id
     * Expected result: server throw an exception with the declared message
     */
    @Test
    public void createStudentWithCorrectDataAndModifyWithWrongId_ExpectedError() {
        //Create a new student
        createTestStudent();

        //Get student and check its size
        List<Student> studentList = repository.findAllBy();
        assertEquals(1, studentList.size());

        //Modify the created student
        //The id should be in string format
        UUID id = UUID.fromString("b47a951a-c2b5-11ed-afa1-0242ac120002");
        String exampleCourseModifyJson = "{\"id\":\""+ id +"\",\"name\":\"Student Alice\",\"emailAddress\":\"student.alice@gmail.com\"}";
        String expectedMessage = "Student with given Id does not exits!";

        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .put("/api/student/modifyStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseModifyJson)
                .contentType(MediaType.APPLICATION_JSON);

        Exception exception = assertThrows(Exception.class, () -> {
            MvcResult result = mockMvc.perform(requestModifyBuilder).andReturn();
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        //reset database for further tests
        repository.deleteAll();
    }

    /**
     * Function to create a standard test student
     */
    private void createTestStudent(){
        Student studentAlice = new Student();
        studentAlice.setEmailAddress("student.alice@gmail.com");
        studentAlice.setFullName("Student Alice");
        repository.save(studentAlice);
    }
}
