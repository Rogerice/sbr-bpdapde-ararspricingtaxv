
package com.santander.bp.infra.dataprovider;

import com.santander.bp.domain.AppArsenalProvider;
import com.santander.bp.domain.entity.AppArsenal;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** App arsenal provider impl. */
@Component
public class AppArsenalProviderImpl implements AppArsenalProvider {
    @Override
    public List<AppArsenal> getAll() {
        List<AppArsenal> list = new ArrayList<>();
        AppArsenal example =
        AppArsenal.builder().id(1L).otherInfo("other info example").build();
        list.add(example);
        return list;
    }

    @Override
    public Optional<AppArsenal> getById(Long appArsenalId) {
        if (appArsenalId == 2) {
            return Optional.empty();
        }
        return Optional.of(AppArsenal.builder().id(1L).build());
    }


    @Override
    public AppArsenal create(AppArsenal appArsenal) {
        return AppArsenal.builder().id(1L).otherInfo(appArsenal.getOtherInfo()).build();
    }

    @Override
    public AppArsenal update(Long id, AppArsenal appArsenal) {
        return AppArsenal.builder().id(1L).otherInfo(appArsenal.getOtherInfo()).build();
    }

    @Override
    public void delete(Long appArsenalId) {
        // Do nothing, delete method
    }
}