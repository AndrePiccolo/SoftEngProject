package com.douglas.rentDogWeb.controller;

import com.douglas.rentDogWeb.controller.entity.DogRequest;
import com.douglas.rentDogWeb.model.database.entity.Customer;
import com.douglas.rentDogWeb.model.database.entity.Doggo;
import com.douglas.rentDogWeb.model.database.repository.CustomerRepository;
import com.douglas.rentDogWeb.model.database.repository.DogRepository;
import com.douglas.rentDogWeb.security.DecoderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RentDogController {

    private static final String LOGIN = "login";
    private static final String REGISTER = "register";
    private static final List<String> LIST_BREED = new ArrayList<>(Arrays.asList("Golden Retrievers",
            "Boston Terriers", "Labrador Retrievers",
            "Poodles", "Border Collie", "Beagle", "Irish Setter", "Staffordshire Bull Terrier",
            "Cavalier King Charles Spaniel", "Cockapoo", "Boxer", "Shih Tzu", "French Bulldog",
            "Basset Hound", "Cocker Spaniel", "Greyhound", "Great Dane", "Samoyed", "West Highland Terriers",
            "Pembroke Welsh Corgi", "Mixed Breed"));

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DogRepository dogRepository;

    @GetMapping(path = "/login")
    public String login(Model model) {
        model.addAttribute("errorMessage", "F");
        return "/login";
    }

    @PostMapping(path = "/login")
    public String validateLogin(Model model, @RequestParam("action") String action,
                                @RequestParam("userLogin") String user,
                                @RequestParam("userPassword") String pwd,
                                Customer customer, HttpSession session) {

        if (LOGIN.equalsIgnoreCase(action)) {
            if (!user.isEmpty() && !pwd.isEmpty()) {

                Customer dbRegister = customerRepository.findCustomerByCustomerEmailEquals(user);
                if (dbRegister!=null && !dbRegister.getCustomerPassword().isEmpty()) {
                    if (DecoderUtils.verifyUserPassword(pwd, dbRegister.getCustomerPassword(), user)) {
                        session.setAttribute("user", dbRegister.getCustomerEmail());
                        session.setAttribute("userID", dbRegister.getCustomerId());
                        return "redirect:/home";
                    }
                }
            }
        } else {
            return "redirect:/registeruser";
        }
        model.addAttribute("errorMessage", "T");
        return "/login";
    }


    @GetMapping(path = "/home")
    public String home(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        } else {
            Customer dbRegister = customerRepository.findCustomerByCustomerEmailEquals(session.getAttribute("user").toString());
            String welcome = "Welcome, " + dbRegister.getCustomerName() + "!";
            model.addAttribute("user", dbRegister);
            model.addAttribute("welcomeMessage", welcome);
        }
        return "/home";
    }

    @GetMapping(path = "/contract")
    public String contract(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        } else {

        }
        return "/contract";
    }

    @GetMapping(path = "/registerdog")
    public String registerdog(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("breedList", LIST_BREED);
        model.addAttribute("inputErrorMessage", "F");
        model.addAttribute("dogRequest", new DogRequest());
        return "/registerdog";
    }

    @PostMapping(path = "/registerdog")
    public String addNewDog(Model model, HttpSession session,
                            @ModelAttribute DogRequest dog) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (dog.getName().isBlank() || dog.getDescription().isBlank() || dog.getPrice().isBlank()) {
            model.addAttribute("inputErrorMessage", "T");
            model.addAttribute("errorMessage", "Please, fill up all the fields.");
            model.addAttribute("breedList", LIST_BREED);
            return "/registerdog";
        }

        dogRepository.save(Doggo.builder()
                .customer(customerRepository.findCustomerByCustomerEmailEquals(session.getAttribute("user").toString()))
                .dogName(dog.getName())
                .dogSize(dog.getSize())
                .dogBreed(dog.getBreed())
                .dogDesc(dog.getDescription())
                .dogPriceHour(Double.parseDouble(dog.getPrice()))
                .availabilitySunday((dog.getSunday() == null) ? 0 : 1)
                .availabilityMonday((dog.getMonday() == null) ? 0 : 1)
                .availabilityTuesday((dog.getTuesday() == null) ? 0 : 1)
                .availabilityWednesday((dog.getWednesday() == null) ? 0 : 1)
                .availabilityThursday((dog.getThursday() == null) ? 0 : 1)
                .availabilityFriday((dog.getFriday() == null) ? 0 : 1)
                .availabilitySaturday((dog.getSaturday() == null) ? 0 : 1)
                .build());

        return "redirect:/home";
    }

    @GetMapping(path = "/registeruser")
    public String registeruser(Model model) {
        model.addAttribute("inputErrorMessage", "F");
        return "/registeruser";
    }

    @PostMapping(path = "/registeruser")
    public String addNewUser(Model model, @RequestParam(name = "inputName", required = false) String name,
                             @RequestParam(name = "inputEmail", required = false) String email,
                             @RequestParam(name = "inputPassword", required = false) String pwd,
                             @RequestParam(name = "confirmPassword", required = false) String confirmPwd,
                             @RequestParam(name = "inputDob", required = false) String dob,
                             @RequestParam(name = "inputPhoneNumber", required = false) String phone,
                             @RequestParam(name = "inputAddress", required = false) String address,
                             @RequestParam(name = "inputCity", required = false) String city,
                             @RequestParam(name = "inputProvince", required = false) String province,
                             @RequestParam(name = "inputPostalCode", required = false) String postalCode,
                             @RequestParam(name = "gridCheck", required = false) String acceptedTerms) {

        try {

            //check if all the fields are values
            if (name.isEmpty() || email.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty() ||
                    dob.isEmpty() || phone.isEmpty() || address.isEmpty() || city.isEmpty() ||
                    province.isEmpty() || postalCode.isEmpty() || acceptedTerms.isEmpty()) {
                model.addAttribute("inputErrorMessage", "T");
                model.addAttribute("errorMessage", "Please, fill up all the fields.");
                return "/registeruser";
            }

            //check if email already in use
            Customer dbRegister = customerRepository.findCustomerByCustomerEmailEquals(email);
            if (dbRegister != null) {
                model.addAttribute("inputErrorMessage", "T");
                model.addAttribute("errorMessage", "Please, choose another email. Email is already in use.");
                return "/registeruser";
            }

            //check if password is valid
            if (pwd.replace(" ", "").length() < 6 || pwd.replace(" ", "").length() > 8) {
                model.addAttribute("inputErrorMessage", "T");
                model.addAttribute("errorMessage", "Your password must be 6-8 characters long.");
                return "/registeruser";
            }

            //check if password are equals
            if (!pwd.equals(confirmPwd)) {
                model.addAttribute("inputErrorMessage", "T");
                model.addAttribute("errorMessage", "Password field and confirm password field must be the same.");
                return "/registeruser";
            }

            //check if user is older than 19 years
            if (LocalDate.parse(dob).plusYears(19).isAfter(LocalDate.now())) {
                model.addAttribute("inputErrorMessage", "T");
                model.addAttribute("errorMessage", "User must be over 19 years older.");
                return "/registeruser";
            }

            Date birthday = new SimpleDateFormat("yyyy-MM-dd").parse(dob);

            customerRepository.save(Customer.builder().customerName(name)
                    .customerEmail(email)
                    .customerPassword(DecoderUtils.generateSecurePassword(pwd, email))
                    .customerDob(birthday)
                    .customerPhoneNumber(phone)
                    .customerStreetAddress(address)
                    .customerCity(city)
                    .customerProvince(province)
                    .customerPostalCode(postalCode)
                    .build());

        } catch (ParseException e) {
            log.error("Date format conversion failed");
            return "/registeruser";
        }

        return "redirect:/login";
    }

    @GetMapping(path = "/search")
    public String search(Model model, HttpSession session,
                         @RequestParam(name = "inputKeyword", required = false) String searchKey,
                         @RequestParam(name = "inputSearchType", required = false) String searchType) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        } else {
            List<Doggo> dogList = dogRepository.findAll();
            model.addAttribute("dogList",dogList);
        }
        return "/search";
    }

    @PostMapping(path = "/search")
    public String searchFilterApply(Model model, HttpSession session,
                                    @RequestParam(name = "inputKeyword", required = false) String searchKey,
                                    @RequestParam(name = "inputSearchType", required = false) String searchType) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        } else {
            List<Doggo> dogList;
            if("dog".equals(searchType)){
                dogList = dogRepository.findDoggoByDogNameContains(searchKey);
            } else if("breed".equals(searchType)) {
                dogList = dogRepository.findDoggoByDogBreedContains(searchKey);
            } else if("size".equals(searchType)){
                dogList = dogRepository.findDoggoByDogSizeContains(searchKey);
            }else{
                dogList = dogRepository.findAll();
            }
            model.addAttribute("dogList",dogList);
        }
        return "/search";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("userID");
        return "redirect:/login";
    }
}
