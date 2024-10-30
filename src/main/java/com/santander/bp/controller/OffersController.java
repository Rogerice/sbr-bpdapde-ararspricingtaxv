package com.santander.bp.controller;

import com.santander.bp.exception.RestApiException;
import com.santander.bp.model.OffersAltairRequest;
import com.santander.bp.model.OffersAltairResponse;
import com.santander.bp.service.OffersService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/offers")
public class OffersController {

  @Autowired private OffersService offersService;

  @GetMapping
  public ResponseEntity<?> processarOferta(@RequestBody OffersAltairRequest ofertaRequest) {
    try {
      List<OffersAltairResponse> response = offersService.processOffers(ofertaRequest);
      return ResponseEntity.ok(response);
    } catch (RestApiException e) {
      return ResponseEntity.status(e.getError().getHttpStatus()).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erro interno no processamento da oferta.");
    }
  }
}
