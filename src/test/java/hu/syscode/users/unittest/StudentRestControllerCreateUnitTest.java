package hu.syscode.users.unittest;

import hu.syscode.users.data.Student;
import hu.syscode.users.exception.StudentException;
import hu.syscode.users.repository.StudentRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
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
public class StudentRestControllerCreateUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    /**
     * Test case: create a user as it is expected
     * Expected result: successful save
     * @throws Exception if mockMvc.perform() method throws an Exception
     */
    @Test
    public void createStudentWithCorrectData() throws Exception {
        String exampleCourseJson = "{\"fullName\":\"Student Alice\",\"emailAddress\":\"student.alice@gmail.com\"}";

        String expectedMessage = "Save was successful!";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/student/addStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedMessage, response.getContentAsString());

        //Check the created student's data
        List<Student> aliceStudent = repository.findAllByOrderByFullName();
        assertEquals(1, aliceStudent.size());
        assertEquals("student.alice@gmail.com", aliceStudent.get(0).getEmailAddress());
        assertEquals("Student Alice", aliceStudent.get(0).getFullName());
    }

    /**
     * Test case: try to create a user with a sample id in UUID format
     * Expected result: server throw an exception with the declared message
     */
    @Test
    public void createStudentWithId_ExpectedError(){
        UUID id = UUID.fromString("b47a951a-c2b5-11ed-afa1-0242ac120002");
        String exampleCourseJson = "{\"id\":\""+ id +"\",\"fullName\":\"Student Alice\",\"emailAddress\":\"student.alice@gmail.com\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/student/addStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
                .contentType(MediaType.APPLICATION_JSON);


        Exception exception = assertThrows(Exception.class, () -> {
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        });

        String expectedMessage = "Cannot add user with UUID!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test case: try to create a user with an incorrect email address
     * Expected result: server throw an exception with the declared message
     */
    @Test
    public void createStudentWithIncorrectEmailAddress_ExpectedError(){
        String exampleCourseJson = "{\"name\":\"Student Alice\",\"emailAddress\":\"incorrectEmailAddress\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/student/addStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
                .contentType(MediaType.APPLICATION_JSON);


        Exception exception = assertThrows(Exception.class, () -> {
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        });

        String expectedMessage = "Email address is not valid!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test case: try to create a user with an incorrect email address
     * Expected result: server throw an exception with the declared message
     */
    @Test
    public void createStudentWithIncorrectEmailAddress2_ExpectedError(){
        String exampleCourseJson = "{\"name\":\"Student Alice\",\"emailAddress\":\"incorrectEmailAddress@\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/student/addStudent")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
                .contentType(MediaType.APPLICATION_JSON);


        Exception exception = assertThrows(Exception.class, () -> {
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        });

        String expectedMessage = "Email address is not valid!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
