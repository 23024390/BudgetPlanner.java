import java.util.Scanner;

// Abstract class for all types of expenses
abstract class Expense {
    protected String category;  // What kind of expense it is (e.g., Groceries, Rent, etc.)
    protected double amount;    // How much the expense costs

    // Constructor to set the category and amount of the expense
    public Expense(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    // Get the category of the expense
    public String getCategory() {
        return category;
    }

    // Get the amount of the expense
    public double getAmount() {
        return amount;
    }

    // Update the amount of the expense
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Abstract method to display the expense details (to be implemented by subclasses)
    public abstract void displayExpense();
}

// Class for handling home loan-specific expenses
class HomeLoan extends Expense {
    private double purchasePrice;
    private double deposit;
    private double interestRate;
    private int monthsToRepay;

    // Constructor for home loan, calculating the monthly repayment amount
    public HomeLoan(double purchasePrice, double deposit, double interestRate, int monthsToRepay) {
        super("Home Loan", 0);  // Home loan category with an initial amount of 0
        this.purchasePrice = purchasePrice;
        this.deposit = deposit;
        this.interestRate = interestRate;
        this.monthsToRepay = monthsToRepay;
        this.amount = calculateMonthlyRepayment();  // Calculate the actual monthly repayment
    }

    // Calculate the monthly repayment based on standard loan formula
    private double calculateMonthlyRepayment() {
        double loanAmount = purchasePrice - deposit;
        double monthlyInterestRate = (interestRate / 100) / 12;
        return loanAmount * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -monthsToRepay));
    }

    // Display the monthly home loan repayment
    @Override
    public void displayExpense() {
        System.out.println("Home Loan Monthly Repayment: " + amount);
    }
}

// Class for handling rent expenses
class Rent extends Expense {
    public Rent(double rentAmount) {
        super("Rent", rentAmount);  // Set rent category and amount
    }

    // Display the monthly rent amount
    @Override
    public void displayExpense() {
        System.out.println("Monthly Rent: " + amount);
    }
}

// Class for handling other simple expenses like groceries, utilities, etc.
class SimpleExpense extends Expense {
    public SimpleExpense(String category, double amount) {
        super(category, amount);  // Set expense category and amount
    }

    // Display the amount for a simple expense category
    @Override
    public void displayExpense() {
        System.out.println(category + ": " + amount);
    }
}

// Main class for managing the budget and expense details
public class BudgetPlanner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Array to store various expenses (groceries, utilities, etc.)
        Expense[] expenses = new Expense[5];

        // Step 1: Input gross income and tax
        System.out.print("Enter your gross monthly income: ");
        double grossIncome = scanner.nextDouble();

        System.out.print("Enter estimated monthly tax: ");
        double tax = scanner.nextDouble();

        // Step 2: Input basic monthly expenses
        System.out.print("Enter monthly groceries expense: ");
        double groceries = scanner.nextDouble();
        expenses[0] = new SimpleExpense("Groceries", groceries);

        System.out.print("Enter monthly water and lights expense: ");
        double waterLights = scanner.nextDouble();
        expenses[1] = new SimpleExpense("Water and Lights", waterLights);

        System.out.print("Enter monthly travel expense: ");
        double travel = scanner.nextDouble();
        expenses[2] = new SimpleExpense("Travel", travel);

        System.out.print("Enter monthly phone expense: ");
        double cellPhone = scanner.nextDouble();
        expenses[3] = new SimpleExpense("Cell Phone", cellPhone);

        System.out.print("Enter any other monthly expenses: ");
        double other = scanner.nextDouble();
        expenses[4] = new SimpleExpense("Other", other);

        // Step 3: Rent or Buy decision
        System.out.print("Do you want to rent or buy a property? (rent/buy): ");
        String decision = scanner.next();
        Expense homeExpense;

        if (decision.equalsIgnoreCase("rent")) {
            // If renting, input monthly rent
            System.out.print("Enter your monthly rent: ");
            double rentAmount = scanner.nextDouble();
            homeExpense = new Rent(rentAmount);
        } else {
            // If buying, input home loan details
            System.out.print("Enter property purchase price: ");
            double purchasePrice = scanner.nextDouble();

            System.out.print("Enter total deposit: ");
            double deposit = scanner.nextDouble();

            System.out.print("Enter interest rate (%): ");
            double interestRate = scanner.nextDouble();

            System.out.print("Enter number of months to repay (between 240 and 360): ");
            int months = scanner.nextInt();

            homeExpense = new HomeLoan(purchasePrice, deposit, interestRate, months);

            // Check if the monthly repayment exceeds 1/3 of gross income
            if (homeExpense.getAmount() > grossIncome / 3) {
                System.out.println("Warning: Home loan approval unlikely due to high repayment.");
            }
        }

        // Calculate the total expenses including tax and home expense
        double totalExpenses = tax;
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount();
        }
        totalExpenses += homeExpense.getAmount();

        // Calculate remaining money after all expenses
        double remainingMoney = grossIncome - totalExpenses;
        System.out.println("Remaining monthly money after all deductions: " + remainingMoney);

        // Step 4: Display the breakdown of all expenses
        System.out.println("\nExpense Breakdown:");
        for (Expense expense : expenses) {
            expense.displayExpense();
        }
        homeExpense.displayExpense();

        scanner.close();
    }
}
