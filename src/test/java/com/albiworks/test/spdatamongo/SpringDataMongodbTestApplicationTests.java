package com.albiworks.test.spdatamongo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringDataMongodbTestApplication.class)
@WebAppConfiguration
@ActiveProfiles("unit-test")
public class SpringDataMongodbTestApplicationTests {
	
	@Autowired
	WebApplicationContext ctx;
	
	MockMvc mockMvc;
	
	@Before
	public void init(){
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	@Test
	public void contextLoads() {
			
	}

	@Test
	public void testRestRequest() throws Exception{
		mockMvc.perform(post("/msg").contentType(MediaType.TEXT_PLAIN_VALUE).content("Hello World!!!"))
		.andExpect(status().isCreated())
		.andExpect(header().string("location", Matchers.notNullValue()))
		;
	}
	
	
	@Test
	public void testRestRequestWithAuthor() throws Exception{
		mockMvc.perform(post("/msg").contentType(MediaType.TEXT_PLAIN_VALUE).content("Hola Mundo!!!").header("author", "Alex"))
		.andExpect(status().isCreated())
		.andExpect(header().string("location", Matchers.notNullValue()))
		;
		
	}
	
	@Test
	public void testRetrieveMessages() throws Exception{
		testRestRequest(); //Insert one object just to make sure there is something if this test runs first or individually
		mockMvc.perform(get("/msg/list"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.notNullValue()))
		.andExpect(jsonPath("$[0]", Matchers.notNullValue()))
		.andExpect(jsonPath("$[0].message").value(Matchers.equalTo("Hello World!!!")))
		.andDo(MockMvcResultHandlers.print())
		;
	}
	
}
