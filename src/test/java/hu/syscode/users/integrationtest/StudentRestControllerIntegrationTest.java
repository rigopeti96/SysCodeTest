package hu.syscode.users.integrationtest;

import hu.syscode.users.UsersApplication;
import hu.syscode.users.data.Student;
import hu.syscode.users.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = UsersApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationTest.properties")
public class StudentRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentRepository repository;

    @Test
    /*public void givenStudent_whenGetEmployees_thenStatus200()
            throws Exception {

        createTestStudent();

        mvc.perform(get("/api/getStudents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("bob")));
    }*/

    private void createTestStudent() {
        Student studentAlice = new Student();
        studentAlice.setEmailAddress("student.alice@gmail.com");
        studentAlice.setFullName("Student Alice");
        repository.save(studentAlice);

        Student studentBob = new Student();
        studentBob.setEmailAddress("student.bob@gmail.com");
        studentBob.setFullName("Student Bob");
        repository.save(studentBob);
    }


}
