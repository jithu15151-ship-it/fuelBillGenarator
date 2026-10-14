# ⛽ Fuel Bill Generator

A Java-based desktop application for generating fuel bills, managing fuel prices, and viewing previous billing records.

The application uses Java Swing for the graphical user interface and JDBC to connect to a MySQL database.

## ✨ Features

- **Fuel Billing:** Generate bills for Petrol and Diesel.
- **Customer Details:** Enter customer names and vehicle numbers.
- **Automatic Calculation:** Calculate the total amount based on fuel price and quantity.
- **Fuel Price Management:** View and update fuel prices.
- **Payment Methods:** Select Cash, UPI, or Card.
- **Bill History:** View previously generated bills.
- **Bill Search:** Search billing records using bill number or customer name.
- **Database Integration:** Store bills and fuel prices in MySQL.
- **Input Validation:** Validate quantities and handle invalid numeric inputs.

## 🛠️ Technologies Used

- Java
- Java Swing
- JDBC (Java Database Connectivity)
- MySQL
- Maven
- Git and GitHub
- XAMPP (for local MySQL)

## 📂 Project Structure


fuelBillGenarator/
├── lib/
│   └── mysql-connector-j-26.7.0.jar
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── jithu/
│                   └── fuelbill/
│                       ├── Main.java
│                       ├── FuelPriceTest.java
│                       ├── config/
│                       │   └── DatabaseConnection.java
│                       ├── service/
│                       │   ├── BillService.java
│                       │   ├── BillHistoryService.java
│                       │   └── FuelPriceService.java
│                       └── ui/
│                           ├── BillingUI.java
│                           ├── BillHistoryUI.java
│                           └── FuelPriceUI.java
├── pom.xml
├── readme.md
└── .gitignore
