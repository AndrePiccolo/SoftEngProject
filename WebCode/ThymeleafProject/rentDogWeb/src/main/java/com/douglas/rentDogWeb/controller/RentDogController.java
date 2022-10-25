package com.douglas.rentDogWeb.controller;

import com.douglas.rentDogWeb.model.database.entity.Customer;
import com.douglas.rentDogWeb.model.database.repository.CustomerRepository;
import com.douglas.rentDogWeb.security.DecoderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RentDogController {

    private static final String LOGIN = "login";
    private static final String REGISTER = "register";

    @Autowired
    private CustomerRepository customerRepository;

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
                if (!dbRegister.getCustomerPassword().isEmpty()) {
                    if (DecoderUtils.verifyUserPassword(pwd, dbRegister.getCustomerPassword(), user)) {
                        session.setAttribute("user", dbRegister.getCustomerEmail());
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
    public String registerdog(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "/registerdog";
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
            if (dbRegister!=null) {
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

            customerRepository.save( Customer.builder().customerName(name)
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
    public String search(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        } else {

        }
        return "/search";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/login";
    }
}
