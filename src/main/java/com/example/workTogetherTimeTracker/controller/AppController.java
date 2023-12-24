    package com.example.workTogetherTimeTracker.controller;

    import com.example.workTogetherTimeTracker.model.Employee;
    import com.example.workTogetherTimeTracker.model.EmployeePair;
    import com.example.workTogetherTimeTracker.model.ProjectDates;
    import com.example.workTogetherTimeTracker.parser.CsvParser;
    import com.example.workTogetherTimeTracker.reader.DatabaseReader;
    import com.example.workTogetherTimeTracker.service.EmployeeEntityService;
    import com.example.workTogetherTimeTracker.service.EmployeeService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.util.HashSet;
    import java.util.Map;
    import java.util.Set;

    @Controller
    @RequestMapping("/")
    public class AppController {

        private final EmployeeService employeeService;
        private final CsvParser csvParser;
        private final EmployeeEntityService employeeEntityService;
        private final DatabaseReader databaseReader;


        @Autowired
        public AppController(EmployeeService employeeService, CsvParser csvParser, EmployeeEntityService employeeEntityService, DatabaseReader databaseReader) {
            this.employeeService = employeeService;
            this.csvParser = csvParser;
            this.employeeEntityService = employeeEntityService;
            this.databaseReader = databaseReader;
        }

        @GetMapping("/")
        public String showHomepage(Model model) {
            // each request to /homepage we read data from database
            Set<Employee> employeesFromDatabase = databaseReader.readEmployeesFromDatabase();
            // updating employees
            employeeService.getEmployees().addAll(employeesFromDatabase);
            model.addAttribute("message", "Данные успешно считаны из базы данных");
            return "homepage";
        }

        @PostMapping("/")
        public String fileUpload(@RequestParam("file") MultipartFile file, Model model) {

            if (!file.isEmpty()) {
                // Очистка данных перед загрузкой нового файла
                employeeService.clearData();

                // Загрузка данных из базы данных
                Set<Employee> employeesFromDatabase = databaseReader.readEmployeesFromDatabase();
                employeeService.getEmployees().addAll(employeesFromDatabase);

                try {
                    Set<Employee> employeesListFromCSV = csvParser.parse(file.getInputStream());
                    if (employeesListFromCSV.isEmpty()) {
                        model.addAttribute("message", "File is empty. Please select a file with valid data.");
                        return "homepage";
                    }
                    employeeService.addEmployees(employeesListFromCSV);
                    employeeEntityService.clearData();


                    // Сохранение сотрудников в базу данных и обновление проектных дат
                    for (Employee employee : employeeService.getEmployees()) {
                        employeeEntityService.saveEmployeeToDatabase(employee);

                        for (Map.Entry<Long, Set<ProjectDates>> entry : employee.getAllProjectDates().entrySet()) {
                            Long projectId = entry.getKey();
                            Set<ProjectDates> projectDatesSet = entry.getValue();
                            for (ProjectDates projectDates : projectDatesSet) {
                                employee.addProject(projectId, projectDates.getDateFrom().toString(), projectDates.getDateTo().toString());
                            }
                        }
                    }

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

        @GetMapping("/read-from-database")
        public String readFromDatabase(Model model) {
            Set<Employee> employeesFromDatabase = databaseReader.readEmployeesFromDatabase();
            Set<Employee> employees = new HashSet<>();
            // Обновляем множество сотрудников
            employees.addAll(employeesFromDatabase);
            model.addAttribute("message", "Данные успешно считаны из базы данных");
            return "homepage";
        }
    }
