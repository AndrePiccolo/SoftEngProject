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

        if(LOGIN.equalsIgnoreCase(action)){
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
    public void registeruser() {
        //        String pwd = DecoderUtils.generateSecurePassword(customer.getCustomerPassword(), customer.getCustomerEmail());
    }

    @PostMapping(path = "/registeruser")
    public String addNewUser(@RequestParam("inputName") String name,
                              @RequestParam("inputEmail") String email,
                              @RequestParam("inputPassword") String pwd,
                              @RequestParam("confirmPassword") String confirmPwd,
                              @RequestParam("inputDob") String dob,
                              @RequestParam("inputPhoneNumber") String phone,
                              @RequestParam("inputAddress") String address,
                              @RequestParam("inputCity") String city,
                              @RequestParam("inputProvince") String province,
                              @RequestParam("inputPostalCode") String postalCode,
                              @RequestParam("gridCheck") String acceptedTerms) {
        log.info(name + " " + email + " " + pwd + " " + confirmPwd + " " + dob + " " + phone + " " +
                address + " " + city + " " + province + " " + postalCode + " " + acceptedTerms);
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
