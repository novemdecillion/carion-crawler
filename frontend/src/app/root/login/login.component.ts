import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { LoginMutationVariables } from 'src/graphql/graphql';
import { AppState, getError, RootActions } from '../state';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styles: [
  ]
})
export class LoginComponent implements OnInit {

  formGroup: FormGroup;
  error$ = this.store.pipe(select(getError));

  constructor(formBuilder: FormBuilder, private store: Store<AppState>, private router: Router) {
    this.formGroup = formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  ngOnInit(): void {
  }

  onLogin(): void {
    let account: LoginMutationVariables = this.formGroup.value

    this.store.dispatch(RootActions.login(account));


    // this.loginGQL.mutate({ username: account.username, password: account.password })
    //   .subscribe(res => {
    //       this.store.dispatch(RootActions.logined({ account: res.data!.login! }));
    //       this.router.navigateByUrl('/');
    //     },
    //     _ => {
    //       this.error = true;
    //     }
    //   );
  }

}
