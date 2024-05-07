package com.santander.bp.domain.usecase;

import com.santander.bp.domain.AppArsenalProvider;
import com.santander.bp.domain.entity.AppArsenal;
import com.santander.bp.infra.exception.AppArsenalErrorCode;
import com.santander.ars.error.exceptions.BusinessException;

import java.util.List;

/** App arsenal use case. */
public class AppArsenalUseCase {

    private final AppArsenalProvider appArsenalProvider;

    /**
     * Provider constructor.
     *
     * @param appArsenalProvider {@link AppArsenalProvider}
     */
    public AppArsenalUseCase(AppArsenalProvider appArsenalProvider) {
        this.appArsenalProvider = appArsenalProvider;
    }

    /** {@inheritDoc}  */
    public List<AppArsenal> getAll() {
        return appArsenalProvider.getAll();
    }

    /**
     * Find by id.
     *
     * @param id {@link Long}
     * @return {@link AppArsenal}
     */
    public AppArsenal getById(Long id) {
        return appArsenalProvider.getById(id).orElseThrow(
                () -> new BusinessException(AppArsenalErrorCode.ERROR_APPARSENAL_NOT_FOUND)
        );
    }

    /**
     * Create new.
     *
     * @param appArsenal {@link AppArsenal}
     * @return {@link AppArsenal}
     */
    public AppArsenal create(AppArsenal appArsenal) {
        return appArsenalProvider.create(appArsenal);
    }

    /**
     * Update by id.
     *
     * @param id {@link Long}
     * @param appArsenal {@link AppArsenal}
     * @return {@link AppArsenal}
     */
    public AppArsenal update(Long id, AppArsenal appArsenal) {
        return appArsenalProvider.update(id, appArsenal);
    }

    /**
     * Delete by id.
     *
     * @param id {@link Long}
     */
    public void delete(Long id) {
        this.appArsenalProvider.delete(id);
    }
}