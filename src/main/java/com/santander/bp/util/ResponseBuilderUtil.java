package com.santander.bp.util;

import com.santander.bp.model.Errors;
import com.santander.bp.model.OffersPricingResponse;
import com.santander.bp.model.ResponseWrapper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilderUtil {

  public static ResponseEntity<ResponseWrapper> buildSuccessResponse(
      List<OffersPricingResponse> data) {
    ResponseWrapper responseWrapper = ResponseWrapper.builder().data(data).build();
    return ResponseEntity.ok(responseWrapper);
  }

  public static ResponseEntity<ResponseWrapper> buildErrorResponse(
      Errors errors, HttpStatus status) {
    ResponseWrapper responseWrapper = ResponseWrapper.builder().errors(errors.getErrors()).build();
    return ResponseEntity.status(status).body(responseWrapper);
  }
}
