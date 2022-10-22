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

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RentDogController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(path = "/login")
    public String login(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("errorMessage", "F");
        return "login";
    }

    @PostMapping(path = "/login")
    public String validateLogin(Model model, Customer customer, HttpSession session) {

        if (!customer.getCustomerEmail().isEmpty() && !customer.getCustomerPassword().isEmpty()) {

            Customer dbRegister = customerRepository.findCustomerByCustomerEmailEquals(customer.getCustomerEmail());
            if (!dbRegister.getCustomerPassword().isEmpty()) {
                if (DecoderUtils.verifyUserPassword(customer.getCustomerPassword(),
                        dbRegister.getCustomerPassword(),
                        customer.getCustomerEmail())) {
                    session.setAttribute("user", customer.getCustomerEmail());
                    return "redirect:/home";
                }
            }
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
    public void contract() {
    }

    @GetMapping(path = "/registerdog")
    public void registerdog() {
    }

    @GetMapping(path = "/registeruser")
    public void registeruser() {
        //        String pwd = DecoderUtils.generateSecurePassword(customer.getCustomerPassword(), customer.getCustomerEmail());
    }

    @GetMapping(path = "/search")
    public void search() {
    }

    @GetMapping(path = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/login";
    }
}
