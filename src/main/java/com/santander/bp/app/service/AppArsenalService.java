package com.santander.bp.app.service;

import com.santander.bp.model.AppArsenalRequestDTO;
import com.santander.bp.model.AppArsenalResponseDTO;
import com.santander.bp.domain.usecase.AppArsenalUseCase;

import java.util.List;

/** App arsenal service. */
public interface AppArsenalService {

    /**
     * Method responsible for retrieving appArsenals available.
     *
     * @return AppArsenalResponseDTO objects.
     */
    List<AppArsenalResponseDTO> getAll();

    /**
     * Method responsible for retrieving a appArsenal by ID.
     *
     * @param id that is to be found.
     * @return The equivalent AppArsenalResponseDTO object.
     */
    AppArsenalResponseDTO getById(Long id);

    /**
     * Method responsible for creating a appArsenal.
     *
     * @param appArsenalRequest information for the new appArsenal to be created.
     * @return The equivalent AppArsenalResponseDTO object.
     */
    AppArsenalResponseDTO create(AppArsenalRequestDTO appArsenalRequest);

    /**
     * Method responsible for updating a appArsenal.
     *
     * @param id that is to be updated.
     * @param appArsenalRequest information that is to be updated.
     * @return The updated AppArsenalResponseDTO object.
     */
    AppArsenalResponseDTO update(Long id, AppArsenalRequestDTO appArsenalRequest);

    /**
     * Method responsible for deleting a appArsenal.
     *
     * @param id of the appArsenal to be deleted.
     */
    void delete(Long id);
}