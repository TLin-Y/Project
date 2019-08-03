package ReplicaHost1;

public class Message {
    public String seqId;
    public String FEHostAddress;
    public String city;
    public String message;

    public Message() {
    }

    public Message(String seqId, String FEHostAddress, String department, String message) {
        this.seqId = seqId;
        this.FEHostAddress = FEHostAddress;
        this.city = department;
        this.message = message;
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getFEHostAddress() {
        return FEHostAddress;
    }

    public void setFEHostAddress(String FEHostAddress) {
        this.FEHostAddress = FEHostAddress;
    }

    public String getDepartment() {
        return city;
    }

    public void setDepartment(String department) {
        this.city = department;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
