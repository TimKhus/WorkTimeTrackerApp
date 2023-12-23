    package com.example.workTogetherTimeTracker.controller;

    import com.example.workTogetherTimeTracker.model.Employee;
    import com.example.workTogetherTimeTracker.model.EmployeePair;
    import com.example.workTogetherTimeTracker.parser.CsvParser;
    import com.example.workTogetherTimeTracker.service.EmployeeService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.util.Set;

    @Controller
    @RequestMapping("/")
    public class AppController {

        private final EmployeeService employeeService;
        private final CsvParser csvParser;


        @Autowired
        public AppController(EmployeeService employeeService, CsvParser csvParser) {
            this.employeeService = employeeService;
            this.csvParser = csvParser;
        }

        @GetMapping("/")
        public String showHomepage() {
            return "homepage";
        }

        @PostMapping("/")
        public String fileUpload(@RequestParam("file") MultipartFile file, Model model) {

            if (!file.isEmpty()) {
                employeeService.clearData();
                try {
                    Set<Employee> employees = csvParser.parse(file.getInputStream());
                    if (employees.isEmpty()) {
                        model.addAttribute("message", "File is empty. Please select a file with valid data.");
                        return "homepage";
                    }
                    employeeService.addEmployees(employees);
                    model.addAttribute("message", "File uploaded successfully");
                } catch (IOException exception) {
                    model.addAttribute("message", "Failed to upload file: " + exception.getMessage());
                }
                return "redirect:/employee-pair";
            } else {
                model.addAttribute("message", "Please select the correct file to upload");
                return null;
            }
        }

        @GetMapping("/employee-pair")
        public String employeePairs(Model model) {
            Set<EmployeePair> employeePairs = employeeService.createEmployeePairs();
            model.addAttribute("employeePair", employeePairs);

            for (EmployeePair employeePair : employeePairs) {
                employeeService.calculateTotalDaysWorkedTogether(employeePair);
            }

            EmployeePair mostDaysWorkedPair = employeeService.findPairWithMostDaysWorkedTogether();

            if (mostDaysWorkedPair != null) {
                employeeService.calculateTotalDaysWorkedTogether(mostDaysWorkedPair);
                model.addAttribute("employeePair", mostDaysWorkedPair);
            } else {
                model.addAttribute("message", "No employee pairs found.");
            }

            return "employee-pair";
        }
    }
