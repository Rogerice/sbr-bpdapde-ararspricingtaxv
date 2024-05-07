package com.santander.bp.app.service.impl;

import org.springframework.stereotype.Service;
import com.santander.bp.model.AppArsenalRequestDTO;
import com.santander.bp.model.AppArsenalResponseDTO;
import com.santander.bp.app.mapper.AppArsenalMapper;
import com.santander.bp.app.service.AppArsenalService;
import com.santander.bp.domain.usecase.AppArsenalUseCase;
import com.santander.bp.infra.exception.AppArsenalErrorCode;
import com.santander.ars.error.exceptions.BusinessException;

import java.util.List;

/** App arsenal service impl. */
@Service
public class AppArsenalServiceImpl implements AppArsenalService {

    private final AppArsenalUseCase appArsenalUseCase;

    private final AppArsenalMapper mapper;

    /**
     * Constructor appArsenalUseCase and mapper
     *
     * @param appArsenalUseCase {@link AppArsenalUseCase}
     * @param mapper {@link AppArsenalMapper}
     */
    public AppArsenalServiceImpl(AppArsenalUseCase appArsenalUseCase, AppArsenalMapper mapper) {
        this.appArsenalUseCase = appArsenalUseCase;
        this.mapper = mapper;
    }

    /** {@inheritDoc}  */
    public List<AppArsenalResponseDTO> getAll() {
        return mapper.toDTOList(this.appArsenalUseCase.getAll());
    }

    /** {@inheritDoc}  */
    public AppArsenalResponseDTO getById(Long id) {
        return mapper.toDTO(this.appArsenalUseCase.getById(id));
    }

    /** {@inheritDoc}  */
    public AppArsenalResponseDTO create(AppArsenalRequestDTO appArsenalRequest) {
        return mapper.toDTO(this.appArsenalUseCase.create(mapper.toModel(appArsenalRequest)));
    }

    /** {@inheritDoc}  */
    public AppArsenalResponseDTO update(Long id, AppArsenalRequestDTO appArsenalRequest) {
        return mapper.toDTO(this.appArsenalUseCase.update(id, mapper.toModel(appArsenalRequest)));
    }

    /** {@inheritDoc}  */
    public void delete(Long id) {
        this.appArsenalUseCase.delete(id);
    }
}