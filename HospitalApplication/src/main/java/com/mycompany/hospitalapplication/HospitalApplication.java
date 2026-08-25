


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.hospitalapplication;

import java.util.*;
/**
 *
 * @author Lethabo
 */
public class HospitalApplication {

    // --- Enums and Classes ---

    // Feature 4: Patient Categories Enum
    enum PatientCategory {
        INPATIENT, OUTPATIENT, EMERGENCY
    }

    // Base Patient Class
    static class Patient {
        protected String patientId;
        protected String firstName;
        protected String lastName;
        protected int age;
        protected String gender;
        protected String medicalCondition;
        protected PatientCategory category;

        // Constructor
        public Patient(String patientId, String firstName, String lastName, int age, String gender, String medicalCondition, PatientCategory category) {
            this.patientId = patientId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.gender = gender;
            this.medicalCondition = medicalCondition;
            this.category = category;
        }

        // Getters
        public String getPatientId() { return patientId; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public int getAge() { return age; }
        public String getGender() { return gender; }
        public String getMedicalCondition() { return medicalCondition; }
        public PatientCategory getCategory() { return category; }

        // Setters
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setAge(int age) { this.age = age; }
        public void setGender(String gender) { this.gender = gender; }
        public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }
        public void setCategory(PatientCategory category) { this.category = category; }

        // Display method - to be overridden by Inpatient
        public void displayDetails() {
            System.out.println("Patient ID: " + patientId);
            System.out.println("Name: " + firstName + " " + lastName);
            System.out.println("Age: " + age);
            System.out.println("Gender: " + gender);
            System.out.println("Medical Condition: " + medicalCondition);
            System.out.println("Category: " + category);
        }
    }

    // Inpatient Class extending Patient
    static class Inpatient extends Patient {
        private int wardNumber;
        private String bedNumber;

        public Inpatient(String patientId, String firstName, String lastName, int age, String gender, String medicalCondition, int wardNumber, String bedNumber) {
            super(patientId, firstName, lastName, age, gender, medicalCondition, PatientCategory.INPATIENT);
            this.wardNumber = wardNumber;
            this.bedNumber = bedNumber;
        }

        public int getWardNumber() { return wardNumber; }
        public String getBedNumber() { return bedNumber; }
        public void setWardNumber(int wardNumber) { this.wardNumber = wardNumber; }
        public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }

        @Override
        public void displayDetails() {
            super.displayDetails();
            System.out.println("Ward Number: " + wardNumber);
            System.out.println("Bed Number: " + bedNumber);
        }
    }

    // --- Main Application Logic ---

    // Data storage
    private static List<Patient> patients = new ArrayList<>();
    private static String[][] wardBeds = new String[4][5]; // 4x5 layout
    private static boolean[][] bedOccupied = new boolean[4][5];

    // Scanner for user input
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeWard();
        boolean exit = false;

        while (!exit) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    managePatients();
                    break;
                case 2:
                    manageBeds();
                    break;
                case 3:
                    generateReports();
                    break;
                case 4:
                    System.out.println("Exiting system. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // --- Initialization ---
    private static void initializeWard() {
        String[] bedIds = {"B01", "B02", "B03", "B04", "B05",
                           "B06", "B07", "B08", "B09", "B10",
                           "B11", "B12", "B13", "B14", "B15",
                           "B16", "B17", "B18", "B19", "B20"};
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                wardBeds[i][j] = bedIds[index++];
                bedOccupied[i][j] = false;
            }
        }
    }

    // --- Main Menu ---
    private static void printMainMenu() {
        System.out.println("\n=== HOSPITAL MANAGEMENT SYSTEM ===");
        System.out.println("1. Patient Management");
        System.out.println("2. Bed Management");
        System.out.println("3. Reports");
        System.out.println("4. Exit");
    }

    // --- Feature 1: Patient Management ---
    private static void managePatients() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Patient Management ---");
            System.out.println("1. Register New Patient");
            System.out.println("2. Search for Patient by ID");
            System.out.println("3. Update Patient Details");
            System.out.println("4. Delete Patient");
            System.out.println("5. Display All Patients");
            System.out.println("6. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    searchPatient();
                    break;
                case 3:
                    updatePatient();
                    break;
                case 4:
                    deletePatient();
                    break;
                case 5:
                    displayAllPatients();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Feature 1: Register a new patient
    private static void registerPatient() {
        System.out.println("\n--- Register New Patient ---");
        String patientId = getStringInput("Enter Patient ID: ");
        
        // Check for duplicate ID
        if (findPatientById(patientId) != null) {
            System.out.println("Error: A patient with this ID already exists.");
            return;
        }

        String firstName = getStringInput("Enter First Name: ");
        String lastName = getStringInput("Enter Last Name: ");
        int age = getIntInput("Enter Age: ");
        String gender = getStringInput("Enter Gender (M/F): ");
        String medicalCondition = getStringInput("Enter Medical Condition: ");
        
        System.out.println("Select Patient Category:");
        System.out.println("1. Inpatient");
        System.out.println("2. Outpatient");
        System.out.println("3. Emergency");
        int categoryChoice = getIntInput("Enter choice (1-3): ");
        
        PatientCategory category;
        switch (categoryChoice) {
            case 1: category = PatientCategory.INPATIENT; break;
            case 2: category = PatientCategory.OUTPATIENT; break;
            case 3: category = PatientCategory.EMERGENCY; break;
            default:
                System.out.println("Invalid category. Defaulting to Outpatient.");
                category = PatientCategory.OUTPATIENT;
        }

        Patient newPatient;
        if (category == PatientCategory.INPATIENT) {
            // For inpatients, we need ward and bed allocation (Feature 2)
            // Find available bed
            int[] bedPosition = findAvailableBed();
            if (bedPosition == null) {
                System.out.println("Error: No beds available for inpatient. Patient registered as Outpatient.");
                newPatient = new Patient(patientId, firstName, lastName, age, gender, medicalCondition, PatientCategory.OUTPATIENT);
            } else {
                int row = bedPosition[0];
                int col = bedPosition[1];
                String bedNumber = wardBeds[row][col];
                bedOccupied[row][col] = true;
                newPatient = new Inpatient(patientId, firstName, lastName, age, gender, medicalCondition, 1, bedNumber);
                System.out.println("Inpatient allocated to Bed " + bedNumber);
            }
        } else {
            newPatient = new Patient(patientId, firstName, lastName, age, gender, medicalCondition, category);
        }

        patients.add(newPatient);
        System.out.println("Patient registered successfully!");
        newPatient.displayDetails();
    }

    // Feature 1: Search for a patient
    private static void searchPatient() {
        String patientId = getStringInput("Enter Patient ID to search: ");
        Patient patient = findPatientById(patientId);
        if (patient != null) {
            System.out.println("\nPatient found:");
            patient.displayDetails();
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Feature 1: Update patient details
    private static void updatePatient() {
        String patientId = getStringInput("Enter Patient ID to update: ");
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\n--- Update Patient Details (leave blank to keep current) ---");
        System.out.println("Current First Name: " + patient.getFirstName());
        String newFirstName = getStringInput("Enter new First Name: ");
        if (!newFirstName.trim().isEmpty()) patient.setFirstName(newFirstName);

        System.out.println("Current Last Name: " + patient.getLastName());
        String newLastName = getStringInput("Enter new Last Name: ");
        if (!newLastName.trim().isEmpty()) patient.setLastName(newLastName);

        System.out.println("Current Age: " + patient.getAge());
        String newAgeStr = getStringInput("Enter new Age: ");
        if (!newAgeStr.trim().isEmpty()) {
            try {
                patient.setAge(Integer.parseInt(newAgeStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Age not updated.");
            }
        }

        System.out.println("Current Gender: " + patient.getGender());
        String newGender = getStringInput("Enter new Gender (M/F): ");
        if (!newGender.trim().isEmpty()) patient.setGender(newGender);

        System.out.println("Current Medical Condition: " + patient.getMedicalCondition());
        String newCondition = getStringInput("Enter new Medical Condition: ");
        if (!newCondition.trim().isEmpty()) patient.setMedicalCondition(newCondition);

        System.out.println("Patient details updated successfully!");
    }

    // Feature 1: Delete a patient
    private static void deletePatient() {
        String patientId = getStringInput("Enter Patient ID to delete: ");
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        // If inpatient, release their bed (Feature 2)
        if (patient instanceof Inpatient) {
            Inpatient inpatient = (Inpatient) patient;
            releaseBed(inpatient.getBedNumber());
        }

        patients.remove(patient);
        System.out.println("Patient deleted successfully!");
    }

    // Feature 1: Display all patients
    private static void displayAllPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        System.out.println("\n--- All Registered Patients ---");
        for (Patient p : patients) {
            System.out.println("-----------------------------");
            p.displayDetails();
        }
    }

    // Helper: Find patient by ID
    private static Patient findPatientById(String patientId) {
        for (Patient p : patients) {
            if (p.getPatientId().equalsIgnoreCase(patientId)) {
                return p;
            }
        }
        return null;
    }

    // --- Feature 2: Bed Management ---
    private static void manageBeds() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Bed Management ---");
            System.out.println("1. Allocate Bed to Inpatient");
            System.out.println("2. Release Bed");
            System.out.println("3. Display Ward Layout");
            System.out.println("4. Display Available Beds");
            System.out.println("5. Display Occupied Beds");
            System.out.println("6. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    allocateBed();
                    break;
                case 2:
                    releaseBedMenu();
                    break;
                case 3:
                    displayWardLayout();
                    break;
                case 4:
                    displayAvailableBeds();
                    break;
                case 5:
                    displayOccupiedBeds();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Feature 2: Allocate a bed to an inpatient
    private static void allocateBed() {
        // Check if beds available
        if (isWardFull()) {
            System.out.println("Error: No beds available!");
            return;
        }

        String patientId = getStringInput("Enter Inpatient ID to allocate bed: ");
        Patient patient = findPatientById(patientId);
        
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }
        
        if (!(patient instanceof Inpatient)) {
            System.out.println("Error: Only Inpatients can be allocated beds.");
            return;
        }

        Inpatient inpatient = (Inpatient) patient;
        
        // Check if patient already has a bed
        if (inpatient.getBedNumber() != null && !inpatient.getBedNumber().isEmpty()) {
            System.out.println("This patient already has a bed allocated (Bed " + inpatient.getBedNumber() + ").");
            return;
        }

        int[] bedPosition = findAvailableBed();
        if (bedPosition == null) {
            System.out.println("Error: No beds available!");
            return;
        }

        int row = bedPosition[0];
        int col = bedPosition[1];
        String bedNumber = wardBeds[row][col];
        bedOccupied[row][col] = true;
        inpatient.setBedNumber(bedNumber);
        inpatient.setWardNumber(1); // Assuming ward number 1 for simplicity
        
        System.out.println("Bed " + bedNumber + " allocated to patient " + patientId);
    }

    // Feature 2: Release a bed
    private static void releaseBedMenu() {
        String bedNumber = getStringInput("Enter bed number to release (e.g., B01): ");
        releaseBed(bedNumber);
    }

    private static void releaseBed(String bedNumber) {
        int[] position = findBedPosition(bedNumber);
        if (position == null) {
            System.out.println("Invalid bed number.");
            return;
        }

        int row = position[0];
        int col = position[1];
        
        if (!bedOccupied[row][col]) {
            System.out.println("This bed is already available.");
            return;
        }

        // Find the inpatient using this bed and remove the bed assignment
        for (Patient p : patients) {
            if (p instanceof Inpatient) {
                Inpatient inpatient = (Inpatient) p;
                if (bedNumber.equals(inpatient.getBedNumber())) {
                    inpatient.setBedNumber(null);
                    inpatient.setWardNumber(0);
                    break;
                }
            }
        }

        bedOccupied[row][col] = false;
        System.out.println("Bed " + bedNumber + " has been released.");
    }

    // Feature 2: Display ward layout
    private static void displayWardLayout() {
        System.out.println("\n--- Ward Layout (4x5) ---");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                String status = bedOccupied[i][j] ? "[O]" : "[A]";
                System.out.print(wardBeds[i][j] + status + " ");
            }
            System.out.println();
        }
        System.out.println("[O] = Occupied, [A] = Available");
    }

    // Feature 2: Display available beds
    private static void displayAvailableBeds() {
        System.out.println("\n--- Available Beds ---");
        boolean found = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (!bedOccupied[i][j]) {
                    System.out.print(wardBeds[i][j] + " ");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No available beds.");
        } else {
            System.out.println();
        }
    }

    // Feature 2: Display occupied beds
    private static void displayOccupiedBeds() {
        System.out.println("\n--- Occupied Beds ---");
        boolean found = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (bedOccupied[i][j]) {
                    System.out.print(wardBeds[i][j] + " ");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No occupied beds.");
        } else {
            System.out.println();
        }
    }

    // Feature 2: Check if ward is full
    private static boolean isWardFull() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (!bedOccupied[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Helper: Find an available bed position
    private static int[] findAvailableBed() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (!bedOccupied[i][j]) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // Helper: Find bed position by bed number
    private static int[] findBedPosition(String bedNumber) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (wardBeds[i][j].equalsIgnoreCase(bedNumber)) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // --- Feature 3: Reports ---
    private static void generateReports() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Display All Patients");
        System.out.println("2. Display All Available Beds");
        System.out.println("3. Display All Occupied Beds");
        System.out.println("4. Display Total Number of Registered Patients");
        System.out.println("5. Display Total Number of Occupied Beds");
        System.out.println("6. Display Ward Occupancy Percentage");
        System.out.println("7. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1:
                displayAllPatients();
                break;
            case 2:
                displayAvailableBeds();
                break;
            case 3:
                displayOccupiedBeds();
                break;
            case 4:
                System.out.println("Total registered patients: " + patients.size());
                break;
            case 5:
                int occupiedCount = countOccupiedBeds();
                System.out.println("Total occupied beds: " + occupiedCount);
                break;
            case 6:
                int occupied = countOccupiedBeds();
                double percentage = (occupied / 20.0) * 100;
                System.out.printf("Ward occupancy: %.2f%%%n", percentage);
                break;
            case 7:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static int countOccupiedBeds() {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (bedOccupied[i][j]) count++;
            }
        }
        return count;
    }

    // --- Utility Methods for Input ---
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // clear invalid input
            }
        }
    }
}