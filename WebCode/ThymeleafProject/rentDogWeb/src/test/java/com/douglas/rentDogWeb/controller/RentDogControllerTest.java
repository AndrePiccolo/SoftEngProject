package com.douglas.rentDogWeb.controller;

import com.douglas.rentDogWeb.controller.entity.DogRequest;
import com.douglas.rentDogWeb.model.database.entity.Contract;
import com.douglas.rentDogWeb.model.database.entity.Customer;
import com.douglas.rentDogWeb.model.database.entity.Doggo;
import com.douglas.rentDogWeb.model.database.repository.ContractRepository;
import com.douglas.rentDogWeb.model.database.repository.CustomerRepository;
import com.douglas.rentDogWeb.model.database.repository.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RentDogController.class)
class RentDogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @BeforeEach
    public void beforeEach() throws ParseException {
        Customer mockedCostumer = new Customer();
        mockedCostumer.setCustomerName("User Test");
        mockedCostumer.setCustomerPassword("12345");

        Optional<Doggo> dog = Optional.of(new Doggo());
        when(customerRepository.findCustomerByCustomerEmailEquals("anyUserTest")).thenReturn(mockedCostumer);
        when(dogRepository.findById(89)).thenReturn(dog);
        Optional<Contract> contract = Optional.of(new Contract());
        when(Optional.of(contractRepository.findContractByDogIdAndRentDate(89, new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")))).thenReturn(Optional.of(contract));

        Optional<Doggo> dog70 = Optional.of(new Doggo());
        dog70.get().setAvailabilitySunday(1);
        dog70.get().setAvailabilityMonday(1);
        dog70.get().setAvailabilityTuesday(1);
        dog70.get().setAvailabilityWednesday(1);
        dog70.get().setAvailabilityThursday(1);
        dog70.get().setAvailabilityFriday(1);
        dog70.get().setAvailabilitySaturday(1);
        dog70.get().setCustomer(mockedCostumer);
        dog70.get().setDogPriceHour(20.00);
        mockedCostumer.setCustomerId(30);
        when(dogRepository.findById(70)).thenReturn(dog70);
        when(customerRepository.findCustomerByCustomerEmailEquals("mateus.olvr@gmail.com")).thenReturn(mockedCostumer);
        when(customerRepository.findById(2)).thenReturn(Optional.of(mockedCostumer));

        Contract contract1 = new Contract();
        contract1.setContractConfirmation(0);
        contract1.setRentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"));
        contract1.setRentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"));
        contract1.setContractStarted("6:00");
        contract1.setContractEnded("7:00");
        contract1.setDogId(70);
        contract1.setOwnerID(2);
        contract1.setRenterId(2);
        List<Contract> listContracts = new ArrayList<>();
        listContracts.add(contract1);
        Optional<List<Contract>> contracts = Optional.of(listContracts);
        when(contractRepository.findContractsByRentDateAfterAndRenterId(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), 5)).thenReturn(contracts);
        when(contractRepository.findContractsByRentDateBeforeAndRenterId(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-02"), 5)).thenReturn(contracts);
        when(contractRepository.findContractsByRentDateAfterAndRentDateBeforeAndRenterId(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-02"), 5)).thenReturn(contracts);
        when(contractRepository.findContractsByRentDateAfter(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"))).thenReturn(contracts);
        when(contractRepository.findContractsByRentDateBefore(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-02"))).thenReturn(contracts);
        when(contractRepository.findContractsByRentDateAfterAndRentDateBefore(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-02"))).thenReturn(contracts);
        Optional<Contract> mockedContract = Optional.of(new Contract());
        when(contractRepository.findById(2)).thenReturn(mockedContract);
        when(contractRepository.findContractsByRenterId(3)).thenReturn(Optional.of(listContracts));
        List<Doggo> dogList = new ArrayList<>();
        Doggo newDog = new Doggo();
        dogList.add(newDog);
        when(dogRepository.findDoggoByDogActiveAndCustomerIsNot(1, mockedCostumer)).thenReturn(dogList);
    }



    @MockBean
    private DogRepository dogRepository;
    @MockBean
    private ContractRepository contractRepository;

    @Test
    void login() throws Exception {

        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login"));

        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", equalTo("F")));
    }

    @Test
    void validateLogin() throws Exception {

        this.mockMvc.perform(post("/login")
                        .param("action", "")
                        .param("userLogin", "")
                        .param("userPassword", "")
                        .param("customer", "anyUserTest")
                )
                .andExpect(redirectedUrl("/registeruser"))
                .andExpect(view().name("redirect:/registeruser"));
//
        this.mockMvc.perform(post("/login")
                        .param("action", "login")
                        .param("userLogin", "admin@admin")
                        .param("userPassword", "admin")
                        .param("customer", "anyUserTest")
                )
                .andExpect(redirectedUrl("/admin"))
                .andExpect(view().name("redirect:/admin"));
    }

    @Test
    void admin() throws Exception {
        Map attributes = new HashMap();

        // Null user (no attributes)
        this.mockMvc.perform(get("/admin"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        attributes.put("user", "anyUserTest");
        this.mockMvc.perform(get("/admin").sessionAttrs(attributes))
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"))
                .andExpect(model().attribute("totalPay", equalTo(null)))
                .andExpect(model().attribute("contractTable", equalTo(null)));

        attributes.replace("user", "admin@admin");
        this.mockMvc.perform(get("/admin").sessionAttrs(attributes))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin"));

    }

    @Test
    void searchContractAdmin() throws Exception {
        this.mockMvc.perform(post("/searchContractAdmin")
                        .param("startDate", "")
                        .param("endDate", "")
                )
                .andExpect(redirectedUrl("/admin"))
                .andExpect(view().name("redirect:/admin"));

        this.mockMvc.perform(post("/searchContractAdmin")
                        .param("startDate", "2022-01-01")
                        .param("endDate", "")
                        .sessionAttr("userID", 5)
                )
                .andExpect(view().name("/admin"));

        this.mockMvc.perform(post("/searchContractAdmin")
                        .param("startDate", "")
                        .param("endDate", "2022-01-02")
                        .sessionAttr("userID", 5)
                )
                .andExpect(view().name("/admin"));
    }

    @Test
    void home() throws Exception {

        // Null user (no attributes)
        this.mockMvc.perform(get("/home"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    void contract() throws Exception {

        this.mockMvc.perform(get("/contract"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        this.mockMvc.perform(get("/contract")
                        .sessionAttr("user", "anyTestUser")
                        .sessionAttr("userID", 3)
                )
                .andExpect(view().name("/contract"));
    }

    @Test
    void cancelContract() throws Exception {

        this.mockMvc.perform(post("/cancelContract")
                        .param("contractIDCancel", "2")
                )
                .andExpect(redirectedUrl("/contract"))
                .andExpect(view().name("redirect:/contract"));
    }

    @Test
    void searchContract() throws Exception {

        this.mockMvc.perform(post("/searchContract")
                        .param("startDate", "")
                        .param("endDate", "")
                )
                .andExpect(redirectedUrl("/contract"))
                .andExpect(view().name("redirect:/contract"));

        this.mockMvc.perform(post("/searchContract")
                        .param("startDate", "2022-01-01")
                        .param("endDate", "")
                        .sessionAttr("userID", 5)
                )
                .andExpect(view().name("/contract"));
        this.mockMvc.perform(post("/searchContract")
                        .param("startDate", "")
                        .param("endDate", "2022-01-02")
                        .sessionAttr("userID", 5)
                )
                .andExpect(view().name("/contract"));
    }

    @Test
    void registerdog() throws Exception {
        Map attributes = new HashMap();

        // Null user (no attributes)
        this.mockMvc.perform(get("/registerdog"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        final List<String> LIST_BREED = new ArrayList<>(Arrays.asList("Golden Retrievers",
                "Boston Terriers", "Labrador Retrievers",
                "Poodles", "Border Collie", "Beagle", "Irish Setter", "Staffordshire Bull Terrier",
                "Cavalier King Charles Spaniel", "Cockapoo", "Boxer", "Shih Tzu", "French Bulldog",
                "Basset Hound", "Cocker Spaniel", "Greyhound", "Great Dane", "Samoyed", "West Highland Terriers",
                "Pembroke Welsh Corgi", "Mixed Breed"));

        attributes.put("user", "anyUserTest");
        this.mockMvc.perform(get("/registerdog").sessionAttrs(attributes))
                .andExpect(status().isOk())
                .andExpect(view().name("/registerdog"))
                .andExpect(model().attribute("breedList", equalTo(LIST_BREED)))
                .andExpect(model().attribute("inputErrorMessage", equalTo("F")))
                .andExpect(model().attribute("dogRequest", new DogRequest()));
    }

    @Test
    void addNewDog() {
    }

    @Test
    void registeruser() throws Exception {
        this.mockMvc.perform(get("/registeruser"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("inputErrorMessage", equalTo("F")))
                .andExpect(view().name("/registeruser"));
    }

    @Test
    void updateUser() throws Exception {
        mockMvc.perform(post("/removeDog").param("removeDog", "89"))
                .andExpect(redirectedUrl("/home"))
                .andExpect(view().name("redirect:/home"));
        assertEquals(Optional.ofNullable(dogRepository.findById(89).get().getDogActive()), Optional.ofNullable(0));
    }

    @Test
    void testUpdateUser() throws Exception {
        this.mockMvc.perform(post("/updateUser")
                        .param("inputName", "Mateus")
                        .param("inputPhoneNumber", "6049999999")
                        .param("inputAddress", "13 somewhere")
                        .param("inputCity", "New West")
                        .param("inputProvince", "BC")
                        .param("inputPostalCode", " ")
                )
                .andExpect(redirectedUrl("/home"))
                .andExpect(view().name("redirect:/home"));

        this.mockMvc.perform(post("/updateUser")
                        .param("inputName", "Mateus")
                        .param("inputPhoneNumber", "6049999999")
                        .param("inputAddress", "13 somewhere")
                        .param("inputCity", "New West")
                        .param("inputProvince", "BC")
                        .param("inputPostalCode", "V3T")
                        .sessionAttr("user", "anyUserTest")
                )
                .andExpect(redirectedUrl("/home"))
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    void addNewUser() throws Exception {

        this.mockMvc.perform(post("/registeruser")
                        .param("inputName", "Mateus")
                        .param("inputEmail", "mateus.olvr@gmail.com")
                        .param("inputPassword", "12345")
                        .param("confirmPassword", "12345")
                        .param("inputDob", "1995-01-01")
                        .param("inputPhoneNumber", "6049999999")
                        .param("inputAddress", "13 somewhere")
                        .param("inputCity", "New West")
                        .param("inputProvince", "BC")
                        .param("inputPostalCode", "V3T")
                        .param("gridCheck", "")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("inputErrorMessage", equalTo("T")))
                .andExpect(model().attribute("errorMessage", equalTo("Please, fill up all the fields.")))
                .andExpect(view().name("/registeruser"));

        this.mockMvc.perform(post("/registeruser")
                        .param("inputName", "Mateus")
                        .param("inputEmail", "mateus.olvr@gmail.com")
                        .param("inputPassword", "12345")
                        .param("confirmPassword", "12345")
                        .param("inputDob", "1995-01-01")
                        .param("inputPhoneNumber", "6049999999")
                        .param("inputAddress", "13 somewhere")
                        .param("inputCity", "New West")
                        .param("inputProvince", "BC")
                        .param("inputPostalCode", "V3T")
                        .param("gridCheck", "1")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("inputErrorMessage", equalTo("T")))
                .andExpect(model().attribute("errorMessage", equalTo("Please, choose another email. Email is already in use.")))
                .andExpect(view().name("/registeruser"));

        this.mockMvc.perform(post("/registeruser")
                        .param("inputName", "Mateus")
                        .param("inputEmail", "mateus.olvr_v2@gmail.com")
                        .param("inputPassword", "12345")
                        .param("confirmPassword", "12345")
                        .param("inputDob", "1995-01-01")
                        .param("inputPhoneNumber", "6049999999")
                        .param("inputAddress", "13 somewhere")
                        .param("inputCity", "New West")
                        .param("inputProvince", "BC")
                        .param("inputPostalCode", "V3T")
                        .param("gridCheck", "1")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("inputErrorMessage", equalTo("T")))
                .andExpect(model().attribute("errorMessage", equalTo("Your password must be 6-8 characters long.")))
                .andExpect(view().name("/registeruser"));

        this.mockMvc.perform(post("/registeruser")
                        .param("inputName", "Mateus")
                        .param("inputEmail", "mateus.olvr_v2@gmail.com")
                        .param("inputPassword", "1234567")
                        .param("confirmPassword", "12345")
                        .param("inputDob", "1995-01-01")
                        .param("inputPhoneNumber", "6049999999")
                        .param("inputAddress", "13 somewhere")
                        .param("inputCity", "New West")
                        .param("inputProvince", "BC")
                        .param("inputPostalCode", "V3T")
                        .param("gridCheck", "1")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("inputErrorMessage", equalTo("T")))
                .andExpect(model().attribute("errorMessage", equalTo("Password field and confirm password field must be the same.")))
                .andExpect(view().name("/registeruser"));

        this.mockMvc.perform(post("/registeruser")
                        .param("inputName", "Mateus")
                        .param("inputEmail", "mateus.olvr_v2@gmail.com")
                        .param("inputPassword", "1234567")
                        .param("confirmPassword", "1234567")
                        .param("inputDob", "2022-01-01")
                        .param("inputPhoneNumber", "6049999999")
                        .param("inputAddress", "13 somewhere")
                        .param("inputCity", "New West")
                        .param("inputProvince", "BC")
                        .param("inputPostalCode", "V3T")
                        .param("gridCheck", "1")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("inputErrorMessage", equalTo("T")))
                .andExpect(model().attribute("errorMessage", equalTo("User must be over 19 years older.")))
                .andExpect(view().name("/registeruser"));

        this.mockMvc.perform(post("/registeruser")
                        .param("inputName", "Mateus")
                        .param("inputEmail", "mateus.olvr_v2@gmail.com")
                        .param("inputPassword", "1234567")
                        .param("confirmPassword", "1234567")
                        .param("inputDob", "1995-01-01")
                        .param("inputPhoneNumber", "6049999999")
                        .param("inputAddress", "13 somewhere")
                        .param("inputCity", "New West")
                        .param("inputProvince", "BC")
                        .param("inputPostalCode", "V3T")
                        .param("gridCheck", "1")
                )
                .andExpect(model().attribute("inputErrorMessage", equalTo(null)))
                .andExpect(model().attribute("errorMessage", equalTo(null)))
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    void search() throws Exception {
        mockMvc.perform(get("/search")
                        .param("inputKeyword", "")
                        .param("inputSearchType", "")
                )
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        mockMvc.perform(get("/search")
                        .param("inputKeyword", "")
                        .param("inputSearchType", "")
                        .sessionAttr("user", "anyUserTest")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/search"));
    }

    @Test
    void searchFilterApply() throws Exception {
        mockMvc.perform(post("/search")
                        .param("inputKeyword", "")
                        .param("inputSearchType", "")
                )
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        mockMvc.perform(post("/search")
                        .param("inputKeyword", "")
                        .param("inputSearchType", "")
                        .sessionAttr("user", "anyUserTest")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/search"));
    }

    @Test
    void verifyDogCardField() throws Exception {
        mockMvc.perform(post("/cardfields")
                        .param("dogid", "89")
                        .param("inputDateTime", "2022-01-01")
                        .param("inputStartTime", "6:00")
                        .param("inputEndTime", "7:00")
                )
                .andExpect(redirectedUrl("/search"))
                .andExpect(view().name("redirect:/search"));


        mockMvc.perform(post("/cardfields")
                        .param("dogid", "89")
                        .param("inputDateTime", "2023-01-01")
                        .param("inputStartTime", "6:00")
                        .param("inputEndTime", "7:00")
                )
                .andExpect(redirectedUrl("/search"))
                .andExpect(view().name("redirect:/search"));

        mockMvc.perform(post("/cardfields")
                        .param("dogid", "90")
                        .param("inputDateTime", "2023-01-01")
                        .param("inputStartTime", "6:00")
                        .param("inputEndTime", "7:00")
                )
                .andExpect(redirectedUrl("/search"))
                .andExpect(view().name("redirect:/search"));

        mockMvc.perform(post("/cardfields")
                        .param("dogid", "70")
                        .param("inputDateTime", "2023-01-02")
                        .param("inputStartTime", "6:00")
                        .param("inputEndTime", "5:00")
                )
                .andExpect(redirectedUrl("/search"))
                .andExpect(view().name("redirect:/search"));

        mockMvc.perform(post("/cardfields")
                        .param("dogid", "70")
                        .param("inputDateTime", "2023-01-02")
                        .param("inputStartTime", "6:00")
                        .param("inputEndTime", "7:00")
                        .sessionAttr("userID", 1)
                )
                .andExpect(redirectedUrl("/contract"))
                .andExpect(view().name("redirect:/contract"));
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"))
                .andExpect(model().attribute("user", equalTo(null)))
                .andExpect(model().attribute("userID", equalTo(null)));
    }
}