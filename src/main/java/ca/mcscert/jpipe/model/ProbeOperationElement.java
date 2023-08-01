package ca.mcscert.jpipe.model;

public abstract class ProbeOperationElement {
    protected final String id;
    protected final String value;

    public ProbeOperationElement(String id, String value)
    {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
