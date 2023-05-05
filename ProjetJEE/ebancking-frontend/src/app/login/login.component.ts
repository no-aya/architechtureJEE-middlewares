import { Component } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AuthentificationService} from "../services/authentification.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  userFormGroup!: FormGroup;
  errorMessage!: string ;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private authService: AuthentificationService) {
  }
  ngOnInit() {
    this.userFormGroup = this.formBuilder.group({
      username: this.formBuilder.control(null),
      password: this.formBuilder.control(null)
    });
  }

  handleLogin() {
    let username=this.userFormGroup.value.username;
    let password=this.userFormGroup.value.password;

    this.authService.login(username,password).subscribe(
      (data:any)=>{
        this.authService.authenticate(data).subscribe(
          (data:any)=>{
            this.router.navigateByUrl("/admin");
          },
          (error)=>{
            this.errorMessage="Invalid credentials";
          }
        );
      },
      (error)=>{
        this.errorMessage="Invalid credentials";
      }
    )


  }
}
