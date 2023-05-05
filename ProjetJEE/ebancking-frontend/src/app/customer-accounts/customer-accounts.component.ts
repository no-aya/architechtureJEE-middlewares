import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Customer} from "../model/customer.model";
import {BankAccount} from "../model/account.model";
import {AccountsService} from "../services/accounts.service";
import {Observable} from "rxjs";
import {CustomerService} from "../services/customer.service";

@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrls: ['./customer-accounts.component.css']
})
export class CustomerAccountsComponent {
  customerID!: string;
  customer!:Customer;
  accounts$!: Observable<Array<BankAccount>>

  constructor(private route : ActivatedRoute, private router : Router, private customerService: CustomerService) {
    this.customer = this.router.getCurrentNavigation()?.extras.state as Customer;

  }

  ngOnInit(): void {
    this.customerID = this.route.snapshot.params['id'];
    this.handleCustomerAccounts();

  }
  handleCustomerAccounts() {
    this.accounts$=this.customerService.getCustomerAccounts(this.customerID);
  }

  handleAccount(account: BankAccount) {
    this.router.navigateByUrl("/admin/accounts", {state: account});
  }
}
