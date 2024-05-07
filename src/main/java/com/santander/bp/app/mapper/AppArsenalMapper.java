package com.santander.bp.app.mapper;

import com.santander.bp.domain.entity.AppArsenal;
import com.santander.bp.model.AppArsenalRequestDTO;
import com.santander.bp.model.AppArsenalResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

/** App arsenal mapper. */
@Mapper(componentModel = "spring")
public interface AppArsenalMapper {

	/**
	 * Mapper to DTO.
	 *
	 * @param appArsenal {@link AppArsenal}
	 * @return {@link AppArsenalResponseDTO}
	 */
	AppArsenalResponseDTO toDTO(AppArsenal appArsenal);

	/**
	 * Mapper to model.
	 *
	 * @param appArsenalRequestDTO {@link AppArsenalRequestDTO}
	 * @return {@link AppArsenal}
	 */
	AppArsenal toModel(AppArsenalRequestDTO appArsenalRequestDTO);

	/**
	 * Mapper to dto list.
	 *
	 * @param appArsenalList {@link List<AppArsenal>}
	 * @return {@link List<AppArsenalResponseDTO>}
	 */
	List<AppArsenalResponseDTO> toDTOList(List<AppArsenal> appArsenalList);
}