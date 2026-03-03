package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {

    private static final String CREATE_CAR_VIEW = "createCar";
    private static final String EDIT_CAR_VIEW = "editCar";
    private static final String CAR_LIST_VIEW = "carList";
    private static final String REDIRECT_CAR_LIST = "redirect:/car/listCar";
    private static final String CAR_ATTRIBUTE = "car";
    private static final String CARS_ATTRIBUTE = "cars";

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/createCar")
    public String createCarPage(Model model) {
        model.addAttribute(CAR_ATTRIBUTE, new Car());
        return CREATE_CAR_VIEW;
    }

    @PostMapping("/createCar")
    public String createCarPost(@ModelAttribute Car car, Model model) {
        carService.create(car);
        return REDIRECT_CAR_LIST;
    }

    @GetMapping("/listCar")
    public String carListPage(Model model) {
        List<Car> allCars = carService.findAll();
        model.addAttribute(CARS_ATTRIBUTE, allCars);
        return CAR_LIST_VIEW;
    }

    @GetMapping("/editCar/{carId}")
    public String editCarPage(@PathVariable String carId, Model model) {
        Car car = carService.findById(carId);
        if (car == null) {
            return REDIRECT_CAR_LIST;
        }
        model.addAttribute(CAR_ATTRIBUTE, car);
        return EDIT_CAR_VIEW;
    }

    @PostMapping("/editCar")
    public String editCarPost(@ModelAttribute Car car, Model model) {
        carService.update(car.getCarId(), car);
        return REDIRECT_CAR_LIST;
    }

    @PostMapping("/deleteCar/{carId}")
    public String deleteCar(@PathVariable String carId) {
        Car car = carService.findById(carId);
        if (car != null) {
            carService.delete(carId);
        }
        return REDIRECT_CAR_LIST;
    }
}
