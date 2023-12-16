import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int availableBalance;
    private List<Bill> bills;
    private List<Transaction> transactionHistory;

    public Customer() {
        this.availableBalance = 0;
        this.bills = new ArrayList<>();
        this.transactionHistory = new ArrayList<>();
    }

    public void addFunds(int amount) {
        if(amount > 0){
            availableBalance += amount;
            System.out.println("Your available balance: " + availableBalance);
        }
        else{
            System.out.println("amount must greater than 0");
        }
    }

    public void createBill(int id, String type, int amount, LocalDate dueDate, String state, String provider) {
        Bill oldBill = searchBillById(bills, id);
        if(oldBill == null){
            if(id > 0 && amount > 0){
                Bill bill = new Bill(id, type, amount, dueDate, state, provider, dueDate);
                bills.add(bill);
                addToTransactionHistory(id, CommonStatus.CREATED);
                System.out.println("Bill created with ID: " + id);
            }
            else{
                System.out.println("invalid bill ID or amount");
            }
        }
        else{
            System.out.println("Bill already exist");
        }
    }

    public void deleteBill(int billId) {
        bills.removeIf(bill -> bill.getId() == billId);
        addToTransactionHistory(billId, CommonStatus.DEL);
        System.out.println("Bill with ID " + billId + " has been deleted.");
    }

    public void updateBill(int billId, String type, int amount, LocalDate dueDate, String state, String provider) {
        for (Bill bill : bills) {
            if (bill.getId() == billId) {
                if(amount > 0){
                    bill.setType(type);
                    bill.setAmount(amount);
                    bill.setDueDate(dueDate);
                    bill.setState(state);
                    bill.setProvider(provider);
                    System.out.println("Bill with ID " + billId + " has been updated.");
                    addToTransactionHistory(billId, CommonStatus.UPDATED);
                    return;
                }
                else{
                    System.out.println("invalid input: amount or dueDate");
                }
            }
        }
        System.out.println("Bill with ID " + billId + " not found.");
    }

    public void viewBills() {
        for (Bill bill : bills) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDueDate = bill.getDueDate().format(formatter);
            System.out.println("Bill ID: " + bill.getId() +
                    " Type: " + bill.getType() +
                    " Amount: " + bill.getAmount() +
                    " Due Date: " + formattedDueDate +
                    " State: " + bill.getState() +
                    " Provider: " + bill.getProvider());
        }
    }

    public List<Bill> searchBillsByProvider(String provider) {
        List<Bill> billsByProvider = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getProvider().equalsIgnoreCase(provider)) {
                billsByProvider.add(bill);
            }
        }
        return billsByProvider;
    }

    public Bill searchBillById(List<Bill> bills, int billId) {
        for (Bill bill : bills) {
            if (bill.getId() == billId) {
                return bill;
            }
        }
        return null; // Return null if bill with given ID is not found
    }

    public boolean payBill(int billId) {
        Bill billToPay = searchBillById(bills, billId);
        if (billToPay != null && !CommonStatus.PAID.equalsIgnoreCase(billToPay.getState())) {
            if (availableBalance >= billToPay.getAmount()) {
                availableBalance -= billToPay.getAmount();
                billToPay.setState(CommonStatus.PAID);
                addToTransactionHistory(billId, CommonStatus.PAID);
                return true;
            } else {
                System.out.println("Not enough fund to proceed with payment for Bill with ID " + billId + ".");
            }
        } else {
            System.out.println("Sorry! Not found a bill with ID " + billId + ".");
        }
        return false;
    }

    public void scheduleBillPayment(int billId, LocalDate scheduledPaymentDate) {
        Bill billToSchedule = searchBillById(bills, billId);
        if (billToSchedule != null) {
            billToSchedule.setScheduledPaymentDate(scheduledPaymentDate);
            System.out.println("Bill with ID " + billId + " has been scheduled for payment on " + scheduledPaymentDate);
        } else {
            System.out.println("Not found a bill with ID " + billId + ".");
        }
    }

    public boolean payScheduledBills() {
        LocalDate currentDate = LocalDate.now();
        boolean paymentMade = false;

        for (Bill bill : bills) {
            if (bill.getScheduledPaymentDate() != null && currentDate.equals(bill.getScheduledPaymentDate())) {
                if (availableBalance >= bill.getAmount()) {
                    availableBalance -= bill.getAmount();
                    bill.setState(CommonStatus.PAID);
                    System.out.println("Scheduled payment for Bill with ID " + bill.getId() + " has been made.");
                    paymentMade = true;
                    addToTransactionHistory(bill.getId(), CommonStatus.PAID);
                } else {
                    System.out.println("Sorry! Not enough funds to pay the scheduled bill with ID " + bill.getId() + ".");
                }
            }
        }
        return paymentMade;
    }

    public void addToTransactionHistory(int billId, String status) {
        boolean isExist = isBillIdInTransactionHistory(transactionHistory, billId);
        if(!isExist){
            Transaction transaction = new Transaction(billId, status);
            transactionHistory.add(transaction);
        }
    }
    public boolean isBillIdInTransactionHistory(List<Transaction> transactionHistory, int billId) {
        for (Transaction transaction : transactionHistory) {
            if (transaction.getBillId() == billId) {
                return true; // If the billId is found, return true
            }
        }
        return false; // If the billId is not found in any Transaction, return false
    }

    public void viewTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("Transaction history is empty.");
        } else {
            System.out.println("Transaction History:");
            for (Transaction transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }
}
