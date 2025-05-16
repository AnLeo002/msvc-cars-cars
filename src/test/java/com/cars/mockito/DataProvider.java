package com.cars.mockito;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.controller.dto.TransmissionDTO;
import com.cars.controller.dto.TransmissionDTOResponse;
import com.cars.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class DataProvider {
    public static BrandEntity createTestBrand(){
        BrandEntity brand = new BrandEntity();
        brand.setId(1L);
        brand.setBrand("nissan");
        return brand;
    }
    public static FuelTypeEntity createTestFuel(){
        FuelTypeEntity fuel = new FuelTypeEntity();
        fuel.setId(1L);
        fuel.setFuel("corriente");
        return fuel;
    }
    public static TransmissionEntity createTestTrans(){
        TransmissionEntity transmission = new TransmissionEntity();
        transmission.setId(1L);
        transmission.setTransmission("automatica");
        transmission.setSpeeds(7);
        return transmission;
    }
    public static CarTypeEntity createTestCarType(){
        CarTypeEntity carType = new CarTypeEntity();
        carType.setId(1L);
        carType.setType("sedan");
        return carType;
    }
    public static VersionEntity createTestVersion(){
        VersionEntity version = new VersionEntity();
        version.setId(1L);
        version.setVersion("b13");
        return version;
    }
    public static ModelEntity createTestModel(){
        ModelEntity model = new ModelEntity();
        model.setId(1L);
        model.setVersionEntities(Set.of(createTestVersion()));
        model.setModel("sentra");
        model.setBrand(createTestBrand());
        return model;
    }
    public static ModelEntity createTestModelVersionEmpty(){
        ModelEntity model = new ModelEntity();
        model.setId(1L);
        model.setVersionEntities(Set.of());
        model.setModel("sentra");
        model.setBrand(createTestBrand());
        return model;
    }
    public static CarEntity createTestCar(String plate) {
        CarEntity car = new CarEntity();
        car.setPlate(plate);
        car.setAge(1993);
        car.setKm(199923);
        car.setColor("Champaña");
        car.setDescription("Hermoso Nissan Sentra B13 único dueño");
        car.setMotor("1.6");
        car.setPrice(BigDecimal.valueOf(23000000));

        // Establecemos relaciones
        car.setBrand(createTestBrand());
        car.setFuel(createTestFuel());
        car.setType(createTestCarType());
        car.setTransmission(createTestTrans());
        car.setModel(createTestModel());
        car.setVersion(createTestVersion());

        System.out.println(car.getPlate());

        return car;
    }
    public static List<CarEntity> createTestCarList() {
        return List.of(
                createTestCar("BDP018"),
                createTestCar("ABC123"),
                createTestCar("XYZ789")
        );
    }
    public static CarDTOResponse createTestCarMapResponse(CarEntity car){
        TransmissionDTOResponse transmissionDTOResponse = new TransmissionDTOResponse(
                car.getTransmission().getId(),
                car.getTransmission().getTransmission(),
                car.getTransmission().getSpeeds());
        return new CarDTOResponse(
                car.getPlate(),
                car.getAge(),
                car.getKm(),
                car.getColor(),
                car.getDescription(),
                car.getMotor(),
                car.getPrice(),
                car.getFuel().getFuel(),
                car.getBrand().getBrand(),
                transmissionDTOResponse,
                car.getType().getType(),
                car.getModel().getModel(),
                car.getVersion().getVersion()
        );
    }
    public static CarDTO createTestCarDTO(String plate,String brand){

        TransmissionDTO transmissionDTO = new TransmissionDTO(
                "automatica",
                7);
        return new CarDTO(
                plate,
                1993,
                199923,
                "Champaña",
                "Hermoso Nissan Sentra B13 único dueño",
                "1.6",
                BigDecimal.valueOf(23000000),
                "corriente",
                brand,
                transmissionDTO,
                "sedan",
                "sentra",
                "b13"
        );
    }
}
