package co.develhope.testUnit;

import co.develhope.testUnit.controllers.UserController;
import co.develhope.testUnit.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Tania Ielpo
 */

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	UserController userController;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}

	private User getUserByID(Long id) throws Exception {
		MvcResult result= this.mockMvc.perform(get("/user/"+id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		try {
			String userJson = result.getResponse().getContentAsString();
			return objectMapper.readValue(userJson, User.class);
		}catch (Exception e){
			return null;
		}

	}

	private User createAUser() throws Exception {
		User user=new User();
		user.setAge(35);
		user.setEmail("taniaie@tiscali.it");
		user.setName("Tania");
		user.setSurname("Ielpo");
		return createAUser(user);
	}

	private User createAUser(User user) throws Exception {

		MvcResult result = createAUserRequest(user);
		User userFromResponse= objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
		return userFromResponse;
	}

	private MvcResult createAUserRequest() throws Exception{
		User user=new User();
		user.setAge(35);
		user.setEmail("taniaie@tiscali.it");
		user.setName("Tania");
		user.setSurname("Ielpo");
		return createAUserRequest(user);
	}

	private MvcResult createAUserRequest(User user) throws Exception {


		return this.mockMvc.perform(post("/user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}


	@Test
	public void create() throws Exception {

		User userFromResponse= createAUser();
		assertThat(userFromResponse).isNotNull();
		assertThat(userFromResponse.getId()).isNotNull();
	}


	@Test
	public void getAll() throws Exception {
		createAUserRequest();
		MvcResult result= this.mockMvc.perform(get("/user/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		List<User> usersFromResponse= objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertThat(usersFromResponse).isNotNull();
		assertThat(usersFromResponse.size()).isNotZero();
	}

	@Test
	public void getSingle() throws Exception {
		User user=createAUser();
		assertThat(user.getId()).isNotNull();

		User usersFromResponse= getUserByID(user.getId());
		assertThat(usersFromResponse).isNotNull();
		assertThat(usersFromResponse.getId()).isEqualTo(user.getId());
	}

	@Test
	public void update() throws Exception {
		User user= createAUser();
		assertThat(user.getId()).isNotNull();

		String newName="Giovanni";
		user.setName(newName);

		String userJson= objectMapper.writeValueAsString(user);
		MvcResult result= this.mockMvc.perform(put("/user/"+user.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		User usersFromResponse= objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
		assertThat(usersFromResponse).isNotNull();
		assertThat(usersFromResponse.getId()).isEqualTo(user.getId());
		assertThat(usersFromResponse.getName()).isEqualTo(newName);

		User usersFromResponseGet= getUserByID(user.getId());
		assertThat(usersFromResponseGet).isNotNull();
		assertThat(usersFromResponseGet.getId()).isEqualTo(user.getId());
		assertThat(usersFromResponse.getName()).isEqualTo(newName);
	}

	@Test
	public void deleteUser() throws Exception {
		User user= createAUser();
		assertThat(user.getId()).isNotNull();

		MvcResult result= this.mockMvc.perform(delete("/user/"+user.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		User usersFromResponseGet= getUserByID(user.getId());
		assertThat(usersFromResponseGet).isNull();
	}

}
