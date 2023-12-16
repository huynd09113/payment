public class Transaction {

    private int billId;
    private String status;
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Transaction(int billId, String status) {
        this.billId = billId;
        this.status = status;
    }


    @Override
    public String toString() {
        return "Bill ID: " + billId + ", Status: " + status;
    }
}
