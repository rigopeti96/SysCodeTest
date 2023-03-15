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

import static org.junit.Assert.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class StudentRestControllerListUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    /**
     * Test case: list the whole database without created any student
     * Expected result: list size is 0
     * @throws Exception if mockMvc.perform() method throws an Exception
     */
    @Test
    public void listStudentsAsInitialState() throws Exception {
        //List the empty database
        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .get("/api/student/getStudents")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult listResult = mockMvc.perform(requestModifyBuilder).andReturn();
        MockHttpServletResponse listResponse = listResult.getResponse();

        assertEquals(HttpStatus.OK.value(), listResponse.getStatus());
        assertEquals("[]", listResponse.getContentAsString());

        List<Student> studentEmptyList = repository.findAllByOrderByFullName();
        assertEquals(0, studentEmptyList.size());
    }

    /**
     * Test case: list the whole database after create a user
     * Expected result: list size is 1
     * @throws Exception if mockMvc.perform() method throws an Exception
     */
    @Test
    public void listStudentsAfterAddAStudent() throws Exception {
        //List the empty database
        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .get("/api/student/getStudents")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult listResult = mockMvc.perform(requestModifyBuilder).andReturn();
        MockHttpServletResponse listResponse = listResult.getResponse();

        assertEquals(HttpStatus.OK.value(), listResponse.getStatus());
        assertEquals("[]", listResponse.getContentAsString());

        List<Student> studentEmptyList = repository.findAllByOrderByFullName();
        assertEquals(0, studentEmptyList.size());

        //Create a new student
        createTestStudent();

        MvcResult listResultAfterAddStudent = mockMvc.perform(requestModifyBuilder).andReturn();
        MockHttpServletResponse listResponseAddStudent = listResultAfterAddStudent.getResponse();

        List<Student> studentNotEmptyList = repository.findAllByOrderByFullName();
        assertEquals(1, studentNotEmptyList.size());

        String expectedResponse = "[{\"id\":\""+ studentNotEmptyList.get(0).getId().toString() +"\",\"fullName\":\"Student Alice\",\"emailAddress\":\"student.alice@gmail.com\"}]";

        assertEquals(HttpStatus.OK.value(), listResponseAddStudent.getStatus());
        assertEquals(expectedResponse, listResponseAddStudent.getContentAsString());

        //reset database for further tests
        repository.deleteAll();
    }

    /**
     * Test case: list the whole database after create two student
     * Then remove one and list the database again
     * Expected result: list size is 1
     * @throws Exception if mockMvc.perform() method throws an Exception
     */
    @Test
    public void listStudentsAfterAddTwoStudentsAndDeleteOne() throws Exception {
        //List the empty database
        RequestBuilder requestModifyBuilder = MockMvcRequestBuilders
                .get("/api/student/getStudents")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult listResult = mockMvc.perform(requestModifyBuilder).andReturn();
        MockHttpServletResponse listResponse = listResult.getResponse();

        assertEquals(HttpStatus.OK.value(), listResponse.getStatus());
        assertEquals("[]", listResponse.getContentAsString());

        List<Student> studentEmptyList = repository.findAllByOrderByFullName();
        assertEquals(0, studentEmptyList.size());

        //Create a new student
        createTestStudent();

        //Create another student
        Student studentBob = new Student();
        studentBob.setEmailAddress("student.bob@gmail.com");
        studentBob.setFullName("Student Bob");
        repository.save(studentBob);

        //List the full database after add the two students
        MvcResult listResultAfterAddStudent = mockMvc.perform(requestModifyBuilder).andReturn();
        MockHttpServletResponse listResponseAddStudent = listResultAfterAddStudent.getResponse();

        List<Student> studentNotEmptyList = repository.findAllByOrderByFullName();
        assertEquals(2, studentNotEmptyList.size());

        //Get both students' ID
        String aliceId = "";
        String bobId = "";
        for (Student student : studentNotEmptyList) {
            if (student.getFullName().equals("Student Alice")) {
                aliceId = student.getId().toString();
            }
            if (student.getFullName().equals("Student Bob")) {
                bobId = student.getId().toString();
            }
        }

        //Declare the expected response after the list of the two students
        String expectedFirstResponse = "[" +
                "{\"id\":\""+ aliceId +"\"," +
                "\"fullName\":\"Student Alice\"," +
                "\"emailAddress\":\"student.alice@gmail.com\"},"+
                "{\"id\":\""+ bobId +"\"," +
                "\"fullName\":\"Student Bob\"," +
                "\"emailAddress\":\"student.bob@gmail.com\"}" +
                "]";

        assertEquals(HttpStatus.OK.value(), listResponseAddStudent.getStatus());
        assertEquals(expectedFirstResponse, listResponseAddStudent.getContentAsString());

        //Remove Student Bob
        repository.delete(studentBob);

        //List the database after delete
        MvcResult listResultAfterRemoveStudent = mockMvc.perform(requestModifyBuilder).andReturn();
        MockHttpServletResponse listResponseRemoveStudent = listResultAfterRemoveStudent.getResponse();

        List<Student> studentNotEmptyListAfterRemove = repository.findAllByOrderByFullName();
        assertEquals(1, studentNotEmptyListAfterRemove.size());

        String expectedSecondResponse = "[" +
                "{\"id\":\""+ aliceId +"\"," +
                "\"fullName\":\"Student Alice\"," +
                "\"emailAddress\":\"student.alice@gmail.com\"}" +
                "]";

        assertEquals(HttpStatus.OK.value(), listResponseRemoveStudent.getStatus());
        assertEquals(expectedSecondResponse, listResponseRemoveStudent.getContentAsString());

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
