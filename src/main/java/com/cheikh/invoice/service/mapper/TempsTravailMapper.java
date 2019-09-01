package com.cheikh.invoice.service.mapper;

import com.cheikh.invoice.domain.*;
import com.cheikh.invoice.service.dto.TempsTravailDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TempsTravail} and its DTO {@link TempsTravailDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TempsTravailMapper extends EntityMapper<TempsTravailDTO, TempsTravail> {

    @Mapping(source = "user.id", target = "userId")
    TempsTravailDTO toDto(TempsTravail tempsTravail);

    @Mapping(source = "userId", target = "user")
    TempsTravail toEntity(TempsTravailDTO tempsTravailDTO);

    default TempsTravail fromId(Long id) {
        if (id == null) {
            return null;
        }
        TempsTravail tempsTravail = new TempsTravail();
        tempsTravail.setId(id);
        return tempsTravail;
    }
}
