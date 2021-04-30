package com.vet24.models;

import com.vet24.models.dtos.ClientDto;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.mappers.MapStructMapper;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;


public class ClientMapperTest {

    @Test
    public void shouldMapClientToDto() {
        //given
        Client client = new Client("Andrew", "Bones",
                "AndrLogin", "password", new Role(RoleNameEnum.CLIENT), new HashSet<>());

        //when
        ClientDto clientDto = MapStructMapper.INSTANCE.clientToClientDto(client);

        //then
        assertThat(clientDto).isNotNull();
        assertThat(clientDto.getUsername()).isEqualTo("AndrLogin");
//        assertThat( clientDto.getEmail()).isEqualTo();
//        assertThat( clientDto.getPets()).isEqualTo();
    }


}
