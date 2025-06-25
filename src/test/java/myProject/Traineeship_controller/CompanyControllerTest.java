package myProject.Traineeship_controller;

import myProject.TraineeshipApp_domain.Company;
import myProject.TraineeshipApp_domain.TraineeshipPosition;
import myProject.TraineeshipApp_domain.User;
import myProject.Traineeship_service.CompanyService;
import myProject.Traineeship_service.PositionService;
import myProject.Traineeship_service.TraineeshipPositionService;
import myProject.Traineeship_service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @Mock
    private PositionService positionService;

    @Mock
    private TraineeshipPositionService traineeshipPositionService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @InjectMocks
    private CompanyController companyController;

    @Test
    void testShowProfile() {
        when(principal.getName()).thenReturn("maria");

        Company mockCompany = new Company();
        mockCompany.setUsername("maria");

        when(companyService.findByUsername("maria")).thenReturn(mockCompany);

        String result = companyController.showProfile(model, principal);

        verify(model).addAttribute("company", mockCompany);
        assertEquals("company_profile", result);
    }
    @Test
    void testEditProfile() {
        when(principal.getName()).thenReturn("maria");

        Company mockCompany = new Company();
        mockCompany.setUsername("maria");

        when(companyService.findByUsername("maria")).thenReturn(mockCompany);

        String result = companyController.editProfile(model, principal);

        verify(model).addAttribute("company", mockCompany);
        assertEquals("company_edit_profile", result);
    }
    @Test
    void testUpdateProfile() {
        when(principal.getName()).thenReturn("maria");

        Company existingCompany = new Company();
        existingCompany.setUsername("maria");

        Company updatedCompany = new Company();
        updatedCompany.setCompanyName("Tech");
        updatedCompany.setLocation("Athens");
        updatedCompany.setEmail("info@tech.com");
        updatedCompany.setFirstName("Maria");
        updatedCompany.setLastName("Tsa");
        updatedCompany.setPassword("n123");

        when(companyService.findByUsername("maria")).thenReturn(existingCompany);

        String result = companyController.updateProfile(updatedCompany, principal);

        assertEquals("Tech", existingCompany.getCompanyName());
        assertEquals("Athens", existingCompany.getLocation());
        assertEquals("info@tech.com", existingCompany.getEmail());
        assertEquals("Maria", existingCompany.getFirstName());
        assertEquals("Tsa", existingCompany.getLastName());
        assertEquals("n123", existingCompany.getPassword());

        verify(companyService).save(existingCompany);
        assertEquals("redirect:/company/profile", result);
    }
    @Test
    void testShowDashboardSuccess() {
        when(principal.getName()).thenReturn("maria");

        Company company = new Company();
        company.setUsername("maria");

        when(companyService.findByUsername("maria")).thenReturn(company);

        String result = companyController.showDashboard(model, principal);

        verify(model).addAttribute("company", company);
        assertEquals("company_dashboard", result);
    }
    @Test
    void testShowDashboardCompanyNotFound() {
        when(principal.getName()).thenReturn("maria");
        when(companyService.findByUsername("maria")).thenReturn(null);

        String result = companyController.showDashboard(model, principal);

        assertEquals("error", result);
    }

    @Test
    void testDeletePosition() {
        String result = companyController.deletePosition(42L);
        verify(traineeshipPositionService).delete(42L);
        assertEquals("redirect:/company_available_positions", result);
    }
    @Test
    void testCreatePosition() {
        when(principal.getName()).thenReturn("maria");

        User mockUser = new User();
        mockUser.setUsername("maria");

        Company company = new Company();
        company.setUsername("maria");

        TraineeshipPosition newPosition = new TraineeshipPosition();

        when(userService.getUserByUsername("maria")).thenReturn(Optional.of(mockUser));
        when(companyService.findByUsername("maria")).thenReturn(company);

        String result = companyController.createPosition(newPosition, principal);

        assertEquals(company, newPosition.getCompany());
        verify(traineeshipPositionService).save(newPosition);
        assertEquals("redirect:/company_dashboard", result);
    }

}
