package com.trlogic.uploader;

import com.trlogic.uploader.repos.ImageRepo;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UploaderApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	ImageRepo imageRepo;

	@Test
	public void uploadMultipart() throws Exception{
		String testScriptPath = "./image.jpg";

		Long countBefore = imageRepo.count();

		byte[] xml = IOUtils.toByteArray(
				testScriptPath
		);

		MockMultipartFile firstFile = new MockMultipartFile("files",
				"image.jpg", "multipart/form-data", xml);

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(MockMvcRequestBuilders.multipart("/")
				.file(firstFile)
				.param("files", "1"))
				.andExpect(status().is(302));

		assertEquals(imageRepo.count(), countBefore + 1);
	}

	@Test
	public void uploadByBase64() throws Exception{
		String base64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAAB3RJTUUH1ggDCwMADQ4NnwAAAFVJREFUGJWNkMEJADEIBEcbSDkXUnfSgnBVeZ8LSAjiwjyEQXSFEIcHGP9oAi+H0Bymgx9MhxbFdZE2a0s9kTZdw01ZhhYkABSwgmf1Z6r1SNyfFf4BZ+ZUExcNUQUAAAAASUVORK5CYII=";

		Long countBefore = imageRepo.count();

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(MockMvcRequestBuilders.post("/")
				.param("imageBase64", base64))
				.andExpect(status().is(302));

		assertEquals(imageRepo.count(), countBefore + 1);
	}

	@Test
	public void uploadByUrl() throws Exception{
		String url = "https://st3.depositphotos.com/5366154/13886/i/450/depositphotos_138866570-stock-photo-fire-letter-m-of-burning.jpg";

		Long countBefore = imageRepo.count();

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(MockMvcRequestBuilders.post("/")
				.param("url", url))
				.andExpect(status().is(302));

		assertEquals(imageRepo.count(), countBefore + 1);
	}

}
