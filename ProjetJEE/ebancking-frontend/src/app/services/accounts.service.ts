import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {BankAccount} from "../model/account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  baseUrl:string = environment.backendHost+"/bankAccounts";

  constructor(private http:HttpClient) {

  }

  public getAccount(accountId:String, page:number, size:number) {
    return this.http.get<BankAccount>(this.baseUrl+"/"+accountId+"/paged"+"?page="+page+"&size="+size);
  }

  public getBankAccount(accountId:String):Observable<BankAccount> {
    ///bankAccounts/{ID}
    return this.http.get<BankAccount>(this.baseUrl+"/"+accountId);
  }

  public debit(accountId:number, amount:number, description:string){
    return this.http.post(this.baseUrl+"/withdraw", {accountID:accountId,amount:amount, description:description});
    // private String accountID;
    //     private double amount;
    //     private String description;
///bankAccounts/withdraw
  }

  public credit(accountId:number, amount:number, description:string){
    return this.http.post(this.baseUrl+"/deposit", {accountID:accountId,amount:amount, description:description});
  }

  public transfer(accountId:number, accountDestinationId:number, amount:number, description:string):Observable<BankAccount> {
    return this.http.post<BankAccount>(this.baseUrl+"/transfer", {accountIDSource:accountId,accountIDDestination:accountDestinationId,amount:amount, description:description});
    /*
    * private String accountIDSource;
    * private String accountIDDestination;
    * private double amount;
    * private String description;
    * */
  }
}

