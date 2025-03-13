package com.cars.controller.dto;


import jakarta.validation.constraints.NotBlank;

public record CarsDTO ( @NotBlank String model,
                        @NotBlank int age,
                        @NotBlank double km,
                        @NotBlank String color,
                        @NotBlank String description,
                        @NotBlank String motor,
                        @NotBlank String price,
                        @NotBlank String fuel,
                        @NotBlank String brand,
                        @NotBlank String transmission,
                        @NotBlank String type){}
