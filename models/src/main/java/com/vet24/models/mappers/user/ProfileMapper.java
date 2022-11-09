package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.ProfileDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.Profile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface ProfileMapper extends DtoMapper <Profile, ProfileDto>, EntityMapper <ProfileDto, Profile> {
    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProfileDto dto, @MappingTarget Profile entity);
}
