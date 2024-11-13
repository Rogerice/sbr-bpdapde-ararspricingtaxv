package com.santander.bp.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.santander.bp.model.Errors;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.ResponseWrapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ResponseBuilderUtilTest {

  @Test
  void testBuildSuccessResponse() {
    OffersPricingResponse offer =
        OffersPricingResponse.builder()
            .id("offer-1")
            .product("26")
            .productDescription("RENDA FIXA")
            .build();

    ResponseEntity<ResponseWrapper> response =
        ResponseBuilderUtil.buildSuccessResponse(List.of(offer));

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getData().size());
    assertEquals(offer, response.getBody().getData().get(0));
  }

  @Test
  void testBuildErrorResponse() {
    Errors error = ErrorBuilderUtil.buildNotFoundError("Resource not found");

    ResponseEntity<ResponseWrapper> response =
        ResponseBuilderUtil.buildErrorResponse(error, HttpStatus.NOT_FOUND);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getErrors().size());
    assertEquals("NOT_FOUND", response.getBody().getErrors().get(0).getCode());
    // assertEquals("Resource not found", response.getBody().getErrors().get(0).getMessage());
  }
}
