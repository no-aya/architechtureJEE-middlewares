import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AccountsService} from "../services/accounts.service";
import {catchError, Observable, throwError} from "rxjs";
import {BankAccount} from "../model/account.model";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit{
  accountFormGroup!: FormGroup;
  currentPage:number = 0;
  size:number = 10;
  accountObservable$! : Observable<BankAccount>;
  bankAccount!: BankAccount;


  operationFormGroup!:FormGroup;
  errorMessages!: string;

  constructor(private route : ActivatedRoute, private router : Router,private formBuilder:FormBuilder, private accountService:AccountsService ) {
    if(this.router.getCurrentNavigation()?.extras.state){
      this.bankAccount = this.router.getCurrentNavigation()?.extras.state as BankAccount;
      this.accountObservable$= this.accountService.getAccount(this.bankAccount.id, this.currentPage, this.size);
    }
  }

  ngOnInit(): void{
    //launch a search for the account received in the state
    this.accountFormGroup=this.formBuilder.group({
      accountNumber:this.formBuilder.control(null)
    })
    this.operationFormGroup=this.formBuilder.group({
      operationType:this.formBuilder.control(null),
      amount:this.formBuilder.control(0),
      description:this.formBuilder.control(null),
      accountDestination:this.formBuilder.control(null)
    })
  }


  handlerSearchAccount() {
    let accountId = this.accountFormGroup.value.accountNumber;
    this.accountObservable$ = this.accountService.getAccount(accountId, this.currentPage, this.size).pipe(
      catchError((error) => {
        this.errorMessages = error.error.message;
        return throwError(error)
      })
    );
  }

  goToPage(page: number) {
    this.currentPage = page;
   this.handlerSearchAccount();
  }

  handleAccountOperation() {
    let accountNumber = this.accountFormGroup.value.accountNumber;
    let accountDestination = this.operationFormGroup.value.accountDestination;
    let operationType = this.operationFormGroup.value.operationType;
    let amount = this.operationFormGroup.value.amount;
    let description = this.operationFormGroup.value.description;
    if (operationType == "TRANSFER") {
      this.accountService.transfer(accountNumber,accountDestination, amount, description).subscribe(
        {
          next: (data) => {
            alert("Transfert successfull");
            this.operationFormGroup.reset();
            this.handlerSearchAccount();
          },
          error: (error) => {
            alert("Transfert failed");
          }
      });
      }else if (operationType == "DEBIT") {
      this.accountService.debit(accountNumber, amount, description).subscribe(
        {
          next: (data) => {
            alert("Debit successfull");
            this.operationFormGroup.reset();
            this.handlerSearchAccount();
          },
          error: (error) => {
            alert("Debit failed");
          }
      });

    }else if (operationType == "CREDIT") {
      this.accountService.credit(accountNumber, amount, description).subscribe(
        {
          next: (data) => {
            alert("Credit successfull");
            this.operationFormGroup.reset();
            this.handlerSearchAccount();
          },
          error: (error) => {
            alert("Credit failed");
          }
        });
    }

  }
}
