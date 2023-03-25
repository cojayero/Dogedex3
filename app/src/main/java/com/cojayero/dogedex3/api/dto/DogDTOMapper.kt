package com.cojayero.dogedex3.api.dto

import com.cojayero.dogedex3.Dog

class DogDTOMapper {
     fun fromDogDTOToDogDomain(dogDTO: DogDTO): Dog {

        return Dog(
            dogDTO.id,
            dogDTO.index,
            dogDTO.name,
            dogDTO.type,
            dogDTO.heightFemale,
            dogDTO.heightMale,
            dogDTO.imageUrl,
            dogDTO.lifeExpectancy,
            dogDTO.temperament,
            dogDTO.weightFemale,
            dogDTO.weightMale
        )
    }

    /**
     *
     */
    fun fromDogDTOListToDogDomainList(dogDTOList:List<DogDTO>):List<Dog>{

        return dogDTOList.map {
            fromDogDTOToDogDomain(it)
        }
    }
}