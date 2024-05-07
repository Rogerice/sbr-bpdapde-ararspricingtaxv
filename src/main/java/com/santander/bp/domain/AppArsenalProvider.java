
package com.santander.bp.domain;

import com.santander.bp.domain.entity.AppArsenal;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

/** App arsenal provider. */
public interface AppArsenalProvider {

    /**
     * Return all.
     *
     * @return {@link List<AppArsenal>}
     */
    List<AppArsenal> getAll();

    /**
     * Find by id.
     *
     * @param appArsenalId {@link Long}
     * @return {@link List<AppArsenal>}
     */
    Optional<AppArsenal> getById(Long appArsenalId);

    /**
     * Create new.
     *
     * @param appArsenal {@link AppArsenal}
     * @return {@link AppArsenal}
     */
    AppArsenal create(AppArsenal appArsenal);

    /**
     * Update by id.
     *
     * @param id {@link Long}
     * @param appArsenal {@link AppArsenal}
     * @return {@link AppArsenal}
     */
    AppArsenal update(Long id, AppArsenal appArsenal);

    /**
     * Delete by id.
     *
     * @param appArsenalId {@link Long}
     */
    void delete(Long appArsenalId);
}