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
public class StudentRestControllerDeleteUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    /**
     * Test case: create a user as it is expected then delete it
     * Expected result: successful delete, list size must be 0
     * @throws Exception if mockMvc.perform() method throws an Exception
     */
    @Test
    public void createStudentWithCorrectDataThenDelete() throws Exception {
        //Create a new student
        createTestStudent();

        //Get student and check its size
        List<Student> studentList = repository.findAllByOrderByFullName();
        assertEquals(studentList.size(), 1);

        //Delete the created student
        //The id should be in string format
        String exampleCourseDeleteJson = "{\"id\":\""+ studentList.get(0).getId().toString() +"\"}";
        String expectedDeleteMessage = "Remove student was successful!";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/student/deleteStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseDeleteJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(response.getContentAsString(), expectedDeleteMessage);

        //Check if the list is empty
        List<Student> studentListAfterDelete = repository.findAllByOrderByFullName();
        assertEquals(studentListAfterDelete.size(), 0);

        //Remove all user for further tests
        repository.deleteAll();
    }

    /**
     * Test case: create two user as it is expected then delete the first created
     * Expected result: successful delete, list size must be 1
     * @throws Exception if mockMvc.perform() method throws an Exception
     */
    @Test
    public void createMultipleStudentWithCorrectDataThenDeleteFirst() throws Exception {
        //Create a new student
        createTestStudent();

        //Create another student
        Student studentBob = new Student();
        studentBob.setEmailAddress("student.bob@gmail.com");
        studentBob.setFullName("Student Bob");
        repository.save(studentBob);

        //Get student and check its size
        List<Student> studentList = repository.findAllByOrderByFullName();
        assertEquals(studentList.size(), 2);

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

        //Delete the created student (Student Alice)
        //The id should be in string format
        String exampleCourseDeleteJson = "{\"id\":\""+ aliceId +"\"}";
        String expectedDeleteMessage = "Remove student was successful!";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/student/deleteStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseDeleteJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(response.getContentAsString(), expectedDeleteMessage);

        //Check if the remaining student is Student Bob
        List<Student> studentAfterDeleteList = repository.findAllByOrderByFullName();
        assertEquals(studentAfterDeleteList.size(), 1);
        assertEquals(studentAfterDeleteList.get(0).getId().toString(), bobId);
        assertEquals(studentAfterDeleteList.get(0).getFullName(), "Student Bob");
        assertEquals(studentAfterDeleteList.get(0).getEmailAddress(), "student.bob@gmail.com");

        //Remove all user for further tests
        repository.deleteAll();
    }

    /**
     * Test case: create a user as it is expected then we try to delete it but without id
     * Expected result: server throw an exception with the declared message
     */
    @Test
    public void createStudentWithCorrectDataAndDeleteWithoutId_ExpectedError() {
        //Create a new student
        createTestStudent();

        //Get student and check its size
        List<Student> studentList = repository.findAllByOrderByFullName();
        assertEquals(studentList.size(), 1);

        //Try to delete the created student without id
        String exampleCourseModifyJson = "{ }";
        String expectedMessage = "Student was not found!";

        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .delete("/api/student/deleteStudent")
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
     * Test case: create a user as it is expected then we try to delete it but without id
     * Expected result: server throw an exception with the declared message
     */
    @Test
    public void createStudentWithCorrectDataAndDeleteWithWrongId_ExpectedError() {
        //Create a new student
        createTestStudent();

        //Create a test UUID
        UUID id = UUID.fromString("b47a951a-c2b5-11ed-afa1-0242ac120002");

        //Get student and check its size
        List<Student> studentList = repository.findAllByOrderByFullName();
        assertEquals(studentList.size(), 1);

        //Try to delete the created student with wrong id
        String exampleCourseModifyJson = "{\"id\":\""+ id +"\"}";
        String expectedMessage = "Student was not found!";

        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .delete("/api/student/deleteStudent")
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
