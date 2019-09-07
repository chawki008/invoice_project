package com.cheikh.invoice.service.mapper;

import com.cheikh.invoice.domain.Authority;
import com.cheikh.invoice.domain.User;
import com.cheikh.invoice.service.dto.UserDTO;
import com.cheikh.invoice.service.dto.FactureDTO;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import com.cheikh.invoice.repository.FactureRepository;

import static java.util.stream.Collectors.groupingBy;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as
 * MapStruct support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {
    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    private FactureLazyMapper factureMapper;

    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(User user) {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.of("GMT+1");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(now);
        List<FactureDTO> factures = this.factureRepository
                .findAllBySasisseurAndLastModifiedAtBetween(user, LocalDate.now().minusDays(4).atStartOfDay().toInstant(zoneOffSet), Instant.now())
                .stream().map(factureMapper::toDto).collect(Collectors.toList());
        Map<LocalDate, List<FactureDTO>> byDay = factures.stream().collect(groupingBy(facture -> LocalDateTime.ofInstant(facture.getLastModifiedAt(), zoneOffSet).toLocalDate()));
        UserDTO userDTO = new UserDTO(user);
        Map<LocalDate, Integer> nbrFacturesParJour = byDay.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, f -> f.getValue().size()));
        //userDTO.setFactures(factures);
        //userDTO.setByDay(byDay);
        userDTO.setNbrFactureParJour(nbrFacturesParJour);
        userDTO.setNbrFactureSaisies(factures.size());
        return userDTO;
    }

    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
    }

    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setActivated(userDTO.isActivated());
            user.setLangKey(userDTO.getLangKey());
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            user.setAuthorities(authorities);
            return user;
        }
    }

    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities = authoritiesAsString.stream().map(string -> {
                Authority auth = new Authority();
                auth.setName(string);
                return auth;
            }).collect(Collectors.toSet());
        }

        return authorities;
    }

    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
