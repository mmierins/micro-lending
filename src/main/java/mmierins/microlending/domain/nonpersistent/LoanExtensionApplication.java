package mmierins.microlending.domain.nonpersistent;

import mmierins.microlending.domain.Client;

public class LoanExtensionApplication {

    private Client client;
    private int term;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
