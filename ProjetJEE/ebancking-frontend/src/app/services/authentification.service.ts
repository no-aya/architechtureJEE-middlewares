import { Injectable } from '@angular/core';
import {AppUser} from "../model/user.model";
import {Observable, of, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthentificationService {
  users : AppUser[] = [];
  authenticatedUser: AppUser| undefined;
  constructor() {
    this.users.push({id:"1",username:"admin",password:"admin",role:["USER","ADMIN"]});
    this.users.push({id:"2",username:"user",password:"user",role:["USER"]});
    this.users.push({id:"3",username:"user1",password:"user1",role:["USER"]});
  }

  public login(username:string, password:string):Observable<AppUser> {
    let user = this.users.find(user => user.username == username && user.password == password);
    //Replace this code with a call to the backend
    if(user){
      return of(user);
    }
    return throwError(()=>"Invalid credentials");
  }

  public authenticate(user:AppUser):Observable<boolean>{
    this.authenticatedUser = user;
    sessionStorage.setItem("authenticatedUser", JSON.stringify({username:user.username, role:user.role, jwt:"JWT_TOKEN"}));
    //See how to use JWT
    return of(true);
  }
  public isAuthenticated():boolean{
    return this.authenticatedUser != undefined;
  }
  public hasRole(role:string):boolean{
    if(this.authenticatedUser){
      return this.authenticatedUser.role.includes(role);
    }
    return false;
  }

  public logout(){
    sessionStorage.removeItem("authenticatedUser");
    this.authenticatedUser = undefined;
  }

}
