package com.santander.bp.app.resource;

import com.santander.bp.model.AppArsenalRequestDTO;
import com.santander.bp.model.AppArsenalResponseDTO;
import com.santander.bp.api.AppArsenalApiDelegate;
import com.santander.bp.app.service.AppArsenalService;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/** Controller responsible for handling AppArsenal operations. */
@Log4j2
@Component
public class AppArsenalResource implements AppArsenalApiDelegate {

  /** Service. */
  @Autowired
  private AppArsenalService appArsenalService;

  /**
   * Method responsible for retrieving all AppArsenal available.
   *
   * @return ResponseEntity containing a List of AppArsenalResponseDTO objects.
   */
  @Override
  public CompletableFuture<ResponseEntity<List<AppArsenalResponseDTO>>> getAll() {
    log.info("Searching for all AppArsenal");
    return CompletableFuture.supplyAsync(() -> ResponseEntity.ok().body(appArsenalService.getAll()));
  }

  /**
   * Method responsible for retriving a AppArsenal by ID.
   *
   * @param id that is to be found.
   * @return ResponseEntity containing the equivalent AppArsenalResponseDTO object.
   */
  @Override
  public CompletableFuture<ResponseEntity<AppArsenalResponseDTO>> getById(Long id) {
    log.info("Searching for AppArsenal with ID " + id);
    return CompletableFuture.supplyAsync(() -> ResponseEntity.ok().body(appArsenalService.getById(id)));
  }

  /**
   * Method responsible for creating a AppArsenal.
   *
   * @param appArsenal information that is to be creadted.
   * @return RespondeEntity containing the URI and equivalent AppArsenalResponseDTO object.
   */
  @Override
  public CompletableFuture<ResponseEntity<AppArsenalResponseDTO>> create(
      AppArsenalRequestDTO appArsenal) {
    AppArsenalResponseDTO saveAppArsenal = appArsenalService.create(appArsenal);
    URI locationResource =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saveAppArsenal.getId())
            .toUri();
    log.info("Successfully created AppArsenal with ID " + saveAppArsenal.getId());
    return CompletableFuture.supplyAsync(() -> ResponseEntity.created(locationResource).body(saveAppArsenal));
  }

  /**
   * Method responsible for updating a AppArsenal.
   *
   * @param id that is to be updated.
   * @param appArsenal information that is to be updated.
   * @return ResponseEntity containing the updated AppArsenalResponseDTO object.
   */
  @Override
  public CompletableFuture<ResponseEntity<AppArsenalResponseDTO>> update(
      Long id, AppArsenalRequestDTO appArsenal) {
    AppArsenalResponseDTO appArsenalUpdate = appArsenalService.update(id, appArsenal);
    log.info("Successfully updated AppArsenal with ID " + id);
    return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(appArsenalUpdate));
  }

  /**
   * Method responsible for deleting a AppArsenal.
   *
   * @param id that is to be deleted.
   * @return ResponseEntity with no content.
   */
  @Override
  public CompletableFuture<ResponseEntity<Void>> delete(Long id) {
    appArsenalService.delete(id);
    log.info("Successfully deleted AppArsenal with ID " + id);
    return CompletableFuture.supplyAsync(() -> ResponseEntity.noContent().build());
  }
}
