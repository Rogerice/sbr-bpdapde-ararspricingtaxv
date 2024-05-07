package com.santander.bp.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;

import com.santander.bp.model.AppArsenalRequestDTO;
import com.santander.bp.model.AppArsenalResponseDTO;

import com.santander.bp.api.client.CucumberClient;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

public class CucumberFeatures {

	@Autowired
	private CucumberClient cucumberClient;

	private ResponseEntity<AppArsenalResponseDTO> response;

	private ResponseEntity<List<AppArsenalResponseDTO>> responseDTOS;

	@Given("^client makes call to GET all")
	public void sendRequest() {
		responseDTOS = cucumberClient.getAll();
	}

	@When("^client receives GET all status code (\\d+)")
	public void receiveStatusCodeFromAll(Integer status) {
		assertThat(responseDTOS.getStatusCode().value(), is(status));
	}
	
	@Given("^client makes call to GET with (\\d+)")
	public void sendRequestWithId(long id) {
		response = cucumberClient.getById(id);
	}

	@When("^client receives GET status code (\\d+)")
	public void receiveStatusCode(Integer status) {
		assertThat(response.getStatusCode().value(), is(status));
	}

	@Then("^client receives response ([^\"]*)")
	public void verifyGetByIdResponse(long id) {
		assertEquals(Objects.requireNonNull(response.getBody()).getId(), id, "Should be equals");
	}

	@Given("^client makes call to POST with \"([^\"]*)\"")
	public void sendPostRequest(String someInfo) {
		response = cucumberClient.create(AppArsenalRequestDTO.builder().otherInfo(someInfo).build());
	}

	@When("^client receives POST status code (\\d+)")
	public void receivePostStatusResponse(Integer status) {
		assertThat(response.getStatusCode().value(), is(status));
	}

	@Then("^client receives POST response \"([^\"]*)\"")
	public void verifyPostResponse(String someInfo) {
		someInfo = someInfo.isEmpty() ? null : someInfo;
		assertThat(Objects.requireNonNull(response.getBody()).getOtherInfo(), is(someInfo));
	}

	@Given("^client calls PUT with (\\d+) and \"([^\"]*)\"")
	public void sendPutRequest(Long id, String someInfo) {
		response = cucumberClient.update(id, AppArsenalRequestDTO.builder().otherInfo(someInfo).build());
	}

	@When("^client receives PUT status code (\\d+)")
	public void receivePutStatusResponse(Integer status) {
		assertThat(response.getStatusCode().value(), is(status));
	}

	@Then("^client receives PUT code (\\d+) and \"([^\"]*)\"")
	public void verifyPutResponse(Long id, String someInfo) {
		someInfo = someInfo.isEmpty() ? null : someInfo;

		assertThat(Objects.requireNonNull(response.getBody()).getId(), is(id));
		assertThat(Objects.requireNonNull(response.getBody()).getOtherInfo(), is(someInfo));
	}

	@Given("^client makes call to DELETE with code (\\d+)")
	public void sendDeleteRequest(Long id) {
		response = cucumberClient.delete(id);
	}

	@Then("^client receives status code (\\d+)")
	public void receiveDeleteStatusResponse(Integer status) {
		assertThat(response.getStatusCode().value(), is(status));
	}
}
