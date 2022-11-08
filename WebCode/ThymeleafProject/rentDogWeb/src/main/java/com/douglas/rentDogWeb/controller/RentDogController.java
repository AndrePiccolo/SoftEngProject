package com.douglas.rentDogWeb.controller;

import com.douglas.rentDogWeb.controller.entity.ContractResponse;
import com.douglas.rentDogWeb.controller.entity.DogRequest;
import com.douglas.rentDogWeb.model.database.entity.Contract;
import com.douglas.rentDogWeb.model.database.entity.Customer;
import com.douglas.rentDogWeb.model.database.entity.Doggo;
import com.douglas.rentDogWeb.model.database.repository.ContractRepository;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RentDogController {

    private static final String LOGIN = "login";
    private static final List<String> LIST_BREED = new ArrayList<>(Arrays.asList("Golden Retrievers",
            "Boston Terriers", "Labrador Retrievers",
            "Poodles", "Border Collie", "Beagle", "Irish Setter", "Staffordshire Bull Terrier",
            "Cavalier King Charles Spaniel", "Cockapoo", "Boxer", "Shih Tzu", "French Bulldog",
            "Basset Hound", "Cocker Spaniel", "Greyhound", "Great Dane", "Samoyed", "West Highland Terriers",
            "Pembroke Welsh Corgi", "Mixed Breed"));

    private boolean searchShowError = false;
    private String searchErrorMessage = "";

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private ContractRepository contractRepository;

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
        }
        Optional<List<Contract>> contractlist = contractRepository.findContractsByRenterId(
                    Integer.parseInt(session.getAttribute("userID").toString()));

        populateContractList(model, contractlist);
        return "/contract";
    }

    @PostMapping(path = "/cancelContract")
    public String cancelContract(Model model, HttpSession session,
                                 @RequestParam(name = "contractIDCancel", required = false) Integer contractId) {
        Optional<Contract> updatedContract = contractRepository.findById(contractId);
        updatedContract.get().setContractConfirmation(3);
        contractRepository.save(updatedContract.get());
        return "redirect:/contract";
    }

    @PostMapping(path = "/searchContract")
    public String searchContract(Model model, HttpSession session,
                                 @RequestParam(name = "startDate", required = false) String startDate,
                                 @RequestParam(name = "endDate", required = false) String endDate) {

        try{
            Optional<List<Contract>> contractlist;
            if(endDate.isBlank() && startDate.isBlank()){
                return "redirect:/contract";
            }else if(endDate.isBlank()){
                contractlist = contractRepository.
                        findContractsByRentDateAfterAndRenterId(
                                new SimpleDateFormat("yyyy-MM-dd").parse(startDate),
                                Integer.parseInt(session.getAttribute("userID").toString()));

            }else if (startDate.isBlank()){
                contractlist = contractRepository.
                        findContractsByRentDateBeforeAndRenterId(
                                new SimpleDateFormat("yyyy-MM-dd").parse(endDate),
                                Integer.parseInt(session.getAttribute("userID").toString()));
            } else{
                contractlist = contractRepository.
                        findContractsByRentDateAfterAndRentDateBeforeAndRenterId(
                                new SimpleDateFormat("yyyy-MM-dd").parse(startDate),
                                new SimpleDateFormat("yyyy-MM-dd").parse(endDate),
                                Integer.parseInt(session.getAttribute("userID").toString()));
            }

            populateContractList(model, contractlist);

        }catch (ParseException ex){
            log.error("Date conversion expception: " + ex.getMessage());
        }

        return "/contract";
    }

    private void populateContractList(Model model, Optional<List<Contract>> contractlist){
        List<ContractResponse> contractTable = new ArrayList<>();
        Double totalPrice = 0.0;
        for (Contract contract: contractlist.get()) {

            if (contract.getContractConfirmation() != 3) {
                if(convertToLocalDate(contract.getRentDate()).isBefore(LocalDate.now())){
                    contract.setContractConfirmation(2);
                }else if(convertToLocalDate(contract.getRentDate()).isAfter(LocalDate.now())){
                    contract.setContractConfirmation(0);
                }else{
                    contract.setContractConfirmation(1);
                }
            }

            Integer start = Integer.parseInt(contract.getContractStarted().replace(":",""));
            Integer end = Integer.parseInt(contract.getContractEnded().replace(":",""));

            Optional<Doggo> dogFromDB = dogRepository.findById(contract.getDogId());
            Double price = Math.round((end - start)/100)* dogFromDB.get().getDogPriceHour();
            if(contract.getContractConfirmation() == 2){
                totalPrice += price;
            }
            contractTable.add(ContractResponse.builder()
                    .ownerName(customerRepository.findById(contract.getOwnerID()).get().getCustomerName())
                    .dogName(dogFromDB.get().getDogName())
                    .contract(contract)
                    .price(price)
                    .build());
        }
        model.addAttribute("contractTable", contractTable);
        model.addAttribute("totalPay", totalPrice);

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
            model.addAttribute("inputErrorMessage", searchShowError);
            model.addAttribute("errorMessage", searchErrorMessage);

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

    @PostMapping(path = "/cardfields")
    public String verifyDogCardField(Model model, HttpSession session,
                                     @RequestParam(name = "dogid", required = false) String dogID,
                                     @RequestParam(name = "inputDateTime", required = false) String date,
                                     @RequestParam(name = "inputStartTime", required = false) String startTime,
                                     @RequestParam(name = "inputEndTime", required = false) String endTime){

        try{
            LocalDate localDate = LocalDate.now();
            if(localDate.isAfter(LocalDate.parse(date)) || localDate.isEqual(LocalDate.parse(date))){
                searchShowError = true;
                searchErrorMessage = "Date is invalid, need to be in the future";
                return "redirect:/search";
            }

            //check if dog is available in the specified day
            if(!contractRepository.findContractByDogIdAndRentDate(Integer.parseInt(dogID),
                    new SimpleDateFormat("yyyy-MM-dd").parse(date)).isEmpty()){
                searchShowError = true;
                searchErrorMessage =  "Date is not available";
                return "redirect:/search";
            }

            //get day of the week to check if dog is available
            DayOfWeek day = LocalDate.parse(date).getDayOfWeek();
            if(!checkDogAvailability(day.getDisplayName(TextStyle.FULL, Locale.CANADA),
                    Integer.parseInt(dogID))){
                searchShowError = true;
                searchErrorMessage = "Dog not available in selected day of week";
                return "redirect:/search";
            }

            //compare start time and end time
            Integer start = Integer.parseInt(startTime.replace(":",""));
            Integer end = Integer.parseInt(endTime.replace(":",""));
            if(start >= end || (end - start) < 100 ){
                searchShowError = true;
                searchErrorMessage = "Start Date must be before than end date and more than 1 hour";
                return "redirect:/search";
            }

            Optional<Doggo> dogDB = dogRepository.findById(Integer.parseInt(dogID));
            if(dogDB.isEmpty()){
                log.error("Failed to retrieve dog");
                return "redirect:/search";
            }

            contractRepository.save(Contract.builder()
                            .renterId(Integer.parseInt(session.getAttribute("userID").toString()))
                            .ownerID(dogDB.get().getCustomer().getCustomerId())
                            .dogId(Integer.parseInt(dogID))
                            .createDate(new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()))
                            .rentDate(new SimpleDateFormat("yyyy-MM-dd").parse(date))
                            .contractStarted(startTime)
                            .contractEnded(endTime)
                            .contractConfirmation(0)
                    .build());

            searchShowError = false;
        } catch (ParseException e) {
            log.error("Date format conversion failed");
            return "redirect:/search";
        }

        return "redirect:/contract";
    }

    private boolean checkDogAvailability(String dayOfWeek, Integer dogId){
        Optional<Doggo> dog = dogRepository.findById(dogId);
        if(dog.isEmpty()){
            return false;
        }
        switch (dayOfWeek){
            case "Sunday":
                return dog.get().getAvailabilitySunday() == 1;
            case "Monday":
                return dog.get().getAvailabilityMonday() == 1;
            case "Tuesday":
                return dog.get().getAvailabilityTuesday() == 1;
            case "Wednesday":
                return dog.get().getAvailabilityWednesday() == 1;
            case "Thursday":
                return dog.get().getAvailabilityThursday() == 1;
            case "Friday":
                return dog.get().getAvailabilityFriday() == 1;
            case "Saturday":
                return dog.get().getAvailabilitySaturday() == 1;
            default:
                return false;
        }
    }

    @GetMapping(path = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("userID");
        return "redirect:/login";
    }

    private LocalDate convertToLocalDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
}