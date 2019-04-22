import { Component } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import $ from 'jquery';
import {Message} from './message';
import {register} from 'ts-node';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Chat App';
  private stompClient;
  private  name:string;
  private type: string;
  sessionId;
  flag:boolean = false;
  constructor() {

  }
  startWorking(message,type){
    this.connect(message,type);
    if (message!=null && type!=null){
      this.flag = true;
    }
  }
  initialize(){
  }
  connect(message, type) {
    let ws = new SockJS('http://localhost:8080/socket');
    this.stompClient = Stomp.over(ws);
    $('sessionIdLabel').html(this.sessionId);

    let that = this;
    this.stompClient.connect({}, function () {
      that.name = message;
      that.type = type;
      let messageModel:Message = new Message("/register", that.name, "web", that.type)
      that.stompClient.send("/app/send/message", {}, JSON.stringify(messageModel));
      $(".chat").prepend("<div class='alert alert-secondary flex-wrap'>" + 'registered' + "</div>");
      that.stompClient.subscribe("/user/queue", (message) => {
        if (message.body) {
          $(".chat").prepend("<div class='alert alert-secondary flex-wrap'>" + message.body + "</div>");
          }
      });

    });
  }

  sendMessage(message) {
    if (message) {
      let messageModel: Message = new Message(message, this.name,"web", this.type)
      this.stompClient.send("/app/send/message", {}, JSON.stringify(messageModel));
    }
    $('#input').val('');
  }

}
