import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../services/customer.service";
import {catchError, map, Observable, throwError} from "rxjs";
import {Customer} from "../model/customer.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit{
  customers$!: Observable<Array<Customer>>;
  errorMessage!: Object;
  searchFormGroup!: FormGroup;
  constructor(private customerService : CustomerService, private formBuilder : FormBuilder, private router:Router) {}

  ngOnInit(): void {
    this.searchFormGroup = this.formBuilder.group({
      keyword: this.formBuilder.control('')
    });
    this.handleSearchCustomer();
  }

  handleSearchCustomer() {
    let keyword = this.searchFormGroup.get('keyword')?.value;
    this.customers$ = this.customerService.searchCustomers(keyword).pipe(
      catchError((err) => {
        this.errorMessage = err.message;
        return throwError(err.message)
      })
    );
  }

  handleDeleteCustomer(id: number) {
    let confirm = window.confirm("Are you sure you want to delete this customer?");
    if (!confirm) return;
    this.customerService.deleteCustomer(id).subscribe(
      () => {
        this.customers$=this.customers$.pipe(
          map(data => {
            let index = data.findIndex(customer => customer.id == id);
            data.splice(index, 1);
            return data;
          })
        );
        this.handleSearchCustomer();
      },
      (err) => {
        this.errorMessage = err.message;
      }
    )

  }

  handleCustomerAccounts(customer: Customer) {
    this.router.navigateByUrl("/admin/accounts/" + customer.id, {state: customer});
  }
}
