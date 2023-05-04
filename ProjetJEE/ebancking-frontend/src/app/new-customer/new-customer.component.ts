import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Customer} from "../model/customer.model";
import {CustomerService} from "../services/customer.service";

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.css']
})
export class NewCustomerComponent {
  newCutomerFormGroup!:FormGroup;
  errorMessage!: string;
  message!: string;
  constructor(private formBuilder:FormBuilder, private customerService:CustomerService) {

  }
  ngOnInit(): void {
    this.newCutomerFormGroup=this.formBuilder.group({
      firstName:this.formBuilder.control(null,[Validators.required,Validators.minLength(4)] ),
      lastName:this.formBuilder.control(null,[Validators.required,Validators.minLength(4)]),
      email:this.formBuilder.control(null, [Validators.required,Validators.email]),
      phoneNumber:this.formBuilder.control(null, [Validators.required,Validators.pattern('[0-9]{10}')]),
    })
  }

  handleSaveCustomer() {
    let customer:Customer = this.newCutomerFormGroup.value;

    this.customerService.saveCustomer(customer).subscribe(
    data=>{
      this.message = "Customer saved successfully";
      this.newCutomerFormGroup.reset();
    },
    error => {
      this.errorMessage = error.message;
    }
    );}

}
