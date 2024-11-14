package com.santander.bp.api.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OffersSteps {

  @Given("client makes call to GET all")
  public void clientMakesCallToGetAll() {
    System.out.println("Chamando API para buscar todas as ofertas.");
  }

  @When("client receives GET all status code {int}")
  public void clientReceivesGetAllStatusCode(Integer statusCode) {
    System.out.println("Verificando status code recebido: " + statusCode);
  }

  @Given("client makes call to GET with {int}")
  public void clientMakesCallToGetWith(Integer id) {
    System.out.println("Realizando a chamada GET com o ID: " + id);
  }

  @When("client receives GET status code {int}")
  public void clientReceivesGetStatusCode(Integer statusCode) {
    System.out.println("Verificando o status code: " + statusCode);
  }

  @Then("client receives response {int}")
  public void clientReceivesResponse(Integer response) {
    System.out.println("Verificando a resposta recebida: " + response);
  }

  @Given("client makes call to POST with {string}")
  public void clientMakesCallToPostWith(String someInfo) {
    System.out.println("Realizando a chamada POST com as informações: " + someInfo);
  }

  @When("client receives POST status code {int}")
  public void clientReceivesPostStatusCode(Integer statusCode) {
    System.out.println("Verificando o status code da resposta POST: " + statusCode);
  }

  @Then("client receives POST response {string}")
  public void clientReceivesPostResponse(String response) {
    System.out.println("Verificando a resposta recebida do POST: " + response);
  }

  @Given("client calls PUT with {int} and {string}")
  public void clientCallsPutWithAnd(Integer id, String info) {
    System.out.println("Realizando a chamada PUT com o ID: " + id + " e informações: " + info);
  }

  @When("client receives PUT status code {int}")
  public void clientReceivesPutStatusCode(Integer statusCode) {
    System.out.println("Verificando o status code da resposta PUT: " + statusCode);
  }

  @Then("client receives PUT code {int} and {string}")
  public void clientReceivesPutCodeAnd(Integer id, String info) {
    System.out.println(
        "Verificando a resposta recebida do PUT com ID: " + id + " e informações: " + info);
  }

  @Given("client makes call to DELETE with code {int}")
  public void clientMakesCallToDeleteWithCode(Integer id) {
    System.out.println("Realizando a chamada DELETE com o ID: " + id);
  }

  @Then("client receives status code {int}")
  public void clientReceivesStatusCode(Integer statusCode) {
    System.out.println("Verificando o status code da resposta DELETE: " + statusCode);
  }
}
