package hu.syscode.users.integrationtest;

import hu.syscode.users.controller.StudentController;
import hu.syscode.users.data.Student;
import hu.syscode.users.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
public class StudentRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentRepository repository;

    @Test
    public void givenStudent_whenGetStudent_thenReturnJsonArray() throws Exception {

        Student alice = new Student();
        alice.setFullName("Student Alice");
        alice.setEmailAddress("student.alice@gmail.com");
        repository.save(alice);

        List<Student> allStudent = List.of(alice);

        given(repository.findAllByOrderByFullName()).willReturn(allStudent);
        RequestBuilder requestListBuilder = MockMvcRequestBuilders
                .get("/api/student/getStudents")
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(requestListBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].fullName").value("Student Alice"))
                .andExpect(jsonPath("$[0].emailAddress").value("student.alice@gmail.com"));
    }
}
