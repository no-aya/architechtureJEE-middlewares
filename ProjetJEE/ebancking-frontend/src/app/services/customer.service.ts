import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../model/customer.model";
import {environment} from "../../environments/environment";
import {BankAccount} from "../model/account.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  backendUrl = environment.backendHost+'/customers';

  constructor(private http: HttpClient) {}
  public getCustomers(): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(this.backendUrl);
  }

  searchCustomers(keyword: String): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(this.backendUrl+'/search?keyword='+keyword);
  }
  saveCustomer(customer : Customer): Observable<Customer> {
    console.log(customer);
    return this.http.post<Customer>(this.backendUrl, customer);
  }

  deleteCustomer(id: number) {
    return this.http.delete(this.backendUrl+'/'+id);
  }
  public getCustomerAccounts(customerId: string) {
    ///customers/{id}/bankAccounts
    return this.http.get<BankAccount[]>(this.backendUrl+"/"+customerId+"/bankAccounts");
  }
}
