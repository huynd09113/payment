import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer();

        Map<String, CommandHandler> commandMap = new HashMap<>();
        commandMap.put("CASH_IN", Main::handleCashInCommand);
        commandMap.put("CREATE_BILL", Main::handleCreateBillCommand);
        commandMap.put("DELETE_BILL", Main::handleDeleteBillCommand);
        commandMap.put("UPDATE_BILL", Main::handleUpdateBillCommand);
        commandMap.put("VIEW_BILLS", Main::handleViewBillCommand);
        commandMap.put("PAY_BILL", Main::handlePayBillCommand);
        commandMap.put("SEARCH_BILL_BY_PROVIDER", Main::handleSearchBillByProviderCommand);
        commandMap.put("LIST_PAYMENT", Main::handleListPaymentCommand);
        commandMap.put("SCHEDULE_PAYMENT", Main::handleSchedulePaymentCommand);
        commandMap.put("PAY_SCHEDULED_BILLS", Main::handlePayScheduledBillsCommand);

        if (args.length > 0) {
            String command = args[0];
            CommandHandler handler = commandMap.get(command);
            if (handler != null) {
                handler.handle(customer, args);
            } else {
                System.out.println("Invalid command.");
            }
        } else {
            System.out.println("Please provide a command.");
        }
    }
    private static void handleCashInCommand(Customer customer, String[] args) {
        if (args.length > 1) {
            try {
                int amount = Integer.parseInt(args[1]);
                if(amount > 0){
                    customer.addFunds(amount);
                }
                else{
                    System.out.println("Invalid amount. amount must greather than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please provide a valid number.");
            }
        } else {
            System.out.println("Please provide the amount to add.");
        }
    }


    private static void handleCreateBillCommand(Customer customer, String[] args) {
        if (args.length > 6) {
            try {
                boolean isValidDate = CommonFunction.isValidDateFormat(args[4]);
                if(!isValidDate){
                    System.out.println("Invalid input format. Invalid Date");
                }
                else{
                    int id = Integer.parseInt(args[1]);
                    int amount = Integer.parseInt(args[3]);
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
                    LocalDate dueDate = LocalDate.parse(args[4], dateFormatter);
                    customer.createBill(id, args[2], amount, dueDate, args[5], args[6]);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. invalid bill ID or amount");
            }
        } else {
            System.out.println("Please provide bill details: ID, Type, Amount, Due Date, State, Provider.");
        }
    }

    private static void handleDeleteBillCommand(Customer customer, String[] args) {
        if (args.length > 1) {
            try {
                int billId = Integer.parseInt(args[1]);
                customer.deleteBill(billId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid bill ID. Please provide a valid number.");
            }
        } else {
            System.out.println("Please provide the ID of the bill to delete.");
        }
    }

    private static void handleUpdateBillCommand(Customer customer, String[] args) {
        if (args.length > 6) {
            try {
                boolean isValidDate = CommonFunction.isValidDateFormat(args[4]);
                if(!isValidDate){
                    System.out.println("Invalid input format. Invalid Date");
                }
                else{
                    int id = Integer.parseInt(args[1]);
                    int amount = Integer.parseInt(args[3]);
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
                    LocalDate dueDate = LocalDate.parse(args[4], dateFormatter);
                    customer.updateBill(id, args[2], amount, dueDate, args[5], args[6]);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format.");
            }
        } else {
            System.out.println("Please provide bill details: ID, Type, Amount, Due Date, State, Provider.");
        }
    }

    private static void handleViewBillCommand(Customer customer, String[] args) {
        if (args.length > 1) {
            try {
                customer.viewBills();
            } catch (Exception e) {
                System.out.println("Invalid input format.");
            }
        }
    }

    private static void handlePayBillCommand(Customer customer, String[] args) {
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                try {
                    int billId = Integer.parseInt(args[i]);
                    boolean paymentSuccess = customer.payBill(billId);
                    if (paymentSuccess) {
                        System.out.println("Payment has been completed for Bill with id " + billId + ".");
                    } else {
                        System.out.println("Failed to pay Bill with id " + billId + ". Check if the bill exists or there's enough fund.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid bill ID. Please provide a valid number.");
                }
            }
        } else {
            System.out.println("Please provide the IDs of the bills to pay.");
        }
    }


    private static void handleSearchBillByProviderCommand(Customer customer, String[] args) {
        if (args.length > 1) {
            String providerName = args[1];
            List<Bill> billsForProvider = customer.searchBillsByProvider(providerName);
            if (!billsForProvider.isEmpty()) {
                System.out.println("Bills for " + providerName + ":");
                for (Bill bill : billsForProvider) {
                    System.out.println("Bill ID: " + bill.getId() +
                            " Type: " + bill.getType() +
                            " Amount: " + bill.getAmount() +
                            " Due Date: " + bill.getDueDate() +
                            " State: " + bill.getState());
                }
            } else {
                System.out.println("No bills found for the specified provider.");
            }
        } else {
            System.out.println("Please provide the provider's name to search bills.");
        }
    }

    private static void handleSchedulePaymentCommand(Customer customer, String[] args) {
        if (args.length > 2) {
            try {
                int billId = Integer.parseInt(args[1]);
                LocalDate scheduledDate = LocalDate.parse(args[2]); // Parsing date from input
                customer.scheduleBillPayment(billId, scheduledDate);
            } catch (NumberFormatException | DateTimeParseException e) {
                System.out.println("Invalid input format.");
            }
        } else {
            System.out.println("Please provide bill ID and scheduled date for payment.");
        }
    }

    private static void handlePayScheduledBillsCommand(Customer customer, String[] args) {
        boolean paymentMade = customer.payScheduledBills();
        if (paymentMade) {
            System.out.println("Scheduled bills have been paid.");
        } else {
            System.out.println("No scheduled bills for payment today.");
        }
    }

    private static void handleListPaymentCommand(Customer customer, String[] args) {
        customer.viewTransactionHistory();
    }

}