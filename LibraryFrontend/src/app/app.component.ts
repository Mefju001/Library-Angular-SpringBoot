import { Component } from '@angular/core';
import { MyServiceService } from './Service/my-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'App-Rest';
  constructor() {
  }
}
