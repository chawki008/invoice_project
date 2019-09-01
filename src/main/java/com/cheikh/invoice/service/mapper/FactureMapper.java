package com.cheikh.invoice.service.mapper;

import com.cheikh.invoice.domain.*;
import com.cheikh.invoice.service.dto.FactureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Facture} and its DTO {@link FactureDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CorrectionMapper.class})
public interface FactureMapper extends EntityMapper<FactureDTO, Facture> {

    @Mapping(source = "sasisseur.id", target = "sasisseurId")
    @Mapping(source = "verificateur.id", target = "verificateurId")
    FactureDTO toDto(Facture facture);

    @Mapping(source = "sasisseurId", target = "sasisseur")
    @Mapping(source = "verificateurId", target = "verificateur")
    @Mapping(target = "removeCorrection", ignore = true)
    Facture toEntity(FactureDTO factureDTO);

    default Facture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Facture facture = new Facture();
        facture.setId(id);
        return facture;
    }
}
