package com.cheikh.invoice.service.mapper;

import com.cheikh.invoice.domain.*;
import com.cheikh.invoice.service.dto.CorrectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Correction} and its DTO {@link CorrectionDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CorrectionMapper extends EntityMapper<CorrectionDTO, Correction> {

    @Mapping(source = "sasisseur.id", target = "sasisseurId")
    @Mapping(source = "verificateur.id", target = "verificateurId")
    CorrectionDTO toDto(Correction correction);

    @Mapping(source = "sasisseurId", target = "sasisseur")
    @Mapping(source = "verificateurId", target = "verificateur")
    @Mapping(target = "factures", ignore = true)
    @Mapping(target = "removeFacture", ignore = true)
    Correction toEntity(CorrectionDTO correctionDTO);

    default Correction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Correction correction = new Correction();
        correction.setId(id);
        return correction;
    }
}
