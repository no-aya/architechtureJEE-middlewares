package ma.enset.ebankingbackend.exceptions;

public class CustomerNotFoundException extends Exception {
    //Pour créer une exception non surveillée, on hérite de RuntimeException
    // Une exception non surveillée est une exception qui n'est pas obligatoire de la gérer
    //Sinon, on hérite de Exception
    //Une exception surveillée est une exception qui est obligatoire de la gérer
    public CustomerNotFoundException(String customerNotFound) {
        super(customerNotFound);
    }
}
