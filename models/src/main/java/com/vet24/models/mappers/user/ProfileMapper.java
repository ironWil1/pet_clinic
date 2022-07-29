package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.ProfileDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ProfileMapper extends DtoMapper <Profile, ProfileDto>, EntityMapper <ProfileDto, Profile> {
}
