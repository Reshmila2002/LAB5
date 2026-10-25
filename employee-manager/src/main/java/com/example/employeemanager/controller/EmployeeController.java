package com.example.employeemanager.controller;

import com.example.employeemanager.model.Employee;
import com.example.employeemanager.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listEmployees(@RequestParam(required = false) String keyword, Model model) {
        addListAttributes(model, keyword);
        model.addAttribute("employee", new Employee());
        return "employees";
    }

    @PostMapping
    public String createEmployee(@Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult bindingResult,
                                 Model model,
                                 @RequestParam(required = false) String keyword,
                                 RedirectAttributes redirectAttributes) {
        if (employeeService.emailExistsForAnotherEmployee(employee.getEmail(), null)) {
            bindingResult.addError(new FieldError("employee", "email", "This email is already registered"));
        }

        if (bindingResult.hasErrors()) {
            addListAttributes(model, keyword);
            return "employees";
        }

        employeeService.createEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee saved successfully.");
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "edit-employee";
    }

    @PostMapping("/{id}/update")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (employeeService.emailExistsForAnotherEmployee(employee.getEmail(), id)) {
            bindingResult.addError(new FieldError("employee", "email", "This email is already registered"));
        }

        if (bindingResult.hasErrors()) {
            employee.setId(id);
            return "edit-employee";
        }

        employeeService.updateEmployee(id, employee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully.");
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully.");
        return "redirect:/employees";
    }

    private void addListAttributes(Model model, String keyword) {
        model.addAttribute("employees", employeeService.getAllEmployees(keyword));
        model.addAttribute("keyword", keyword == null ? "" : keyword);
    }
}
