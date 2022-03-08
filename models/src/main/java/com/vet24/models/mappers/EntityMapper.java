package com.vet24.models.mappers;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface EntityMapper<D, E>{
    D toDto (E entity);
    List<D> toDto (List<E> entities);
    void updateEntity(D dto, @MappingTarget E entity);

}
