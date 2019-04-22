export class Message{
  user:string;
  value:string;
  platform:string;
  typeOfUser:string;
  sendTo:string;

  constructor(message:string,user:string,platform:string,typeOfUser:string){
    this.value = message;
    this.user = user;
    this.platform = platform;
    this.typeOfUser=typeOfUser;
  }
}
