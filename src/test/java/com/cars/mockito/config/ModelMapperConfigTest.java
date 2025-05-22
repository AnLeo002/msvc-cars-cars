package com.cars.mockito.config;

import com.cars.config.ModelMapperConfig;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.CarEntity;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class ModelMapperConfigTest {

    private ModelMapperConfig mapperConfig;
    private CarEntity carTest;
    @BeforeEach
    void setup(){
        mapperConfig = new ModelMapperConfig();
        carTest = DataProvider.createTestCar("bdp018");
    }
    @Test
    void modelMapperTest(){
        ModelMapper modelMapper = mapperConfig.modelMapper();
        CarDTOResponse result = modelMapper.map(carTest, CarDTOResponse.class);
        assertNotNull(result);
        assertEquals("BDP018",result.plate());
        assertEquals("nissan",result.brand());
        assertEquals(199923,result.km());
        assertEquals(1993,result.age());
        assertEquals("Champaña",result.color());
        assertEquals("1.6",result.motor());
    }
    @Test
    void modelMapperNullTest(){
        carTest.setTransmission(null);
        carTest.setModel(null);
        carTest.setFuel(null);
        carTest.setVersion(null);
        carTest.setPrice(null);
        carTest.setBrand(null);
        carTest.setType(null);
        ModelMapper modelMapper = mapperConfig.modelMapper();
        CarDTOResponse result = modelMapper.map(carTest, CarDTOResponse.class);
        assertEquals("BDP018",result.plate());
        assertEquals("N/A",result.brand());
        assertEquals("N/A",result.model());
        assertEquals("N/A",result.fuel());
        assertEquals("N/A",result.brand());
        assertEquals("N/A",result.type());
        assertEquals(null,result.transmission());
    }
}
