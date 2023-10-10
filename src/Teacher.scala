import scala.collection.mutable.ListBuffer
import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._
class Teacher(name:String, surname:String, age:Int, email:String, addr:Address, var login:String, var password:String, var balance:Double, var fiat:Double, var smartcontract:Smartcontract, var courseprice:Int) extends Human(name, surname, age, email, addr) with Connect_with_birga with Connect_with_platform {
  private var _login:String = login;
  private var _password:String = password;
  private var _balance:Double = balance;
  private var _fiat:Double = fiat;
  private var _smartcontract:Smartcontract = smartcontract;
  private var _listStudent = new ListBuffer[Student]();
  private var _numofstudent:Int = _listStudent.length;
  private var _courseprice:Int = courseprice;
  var list = new ListBuffer[Double]();
  val genesis_block = collection.mutable.Map("from" -> "", "to" -> "", "amount" -> 0.0, "description" -> "");
  
  override def toString(): String = super.toString() +  s", ${_numofstudent}" + s", ${_balance}" +s", ${_fiat}" + s", ${_courseprice}" + s", ${_smartcontract.toString()}";
  override def Show(): String = super.Show() + s"\nNumber of student: ${_numofstudent}\n" + s"Balance: ${_balance}\n" + s"Fiat: ${_fiat}\n"+ s"Courseprice: ${_courseprice}\n"+  s"Smartcontract: ${_smartcontract.toString()}\n";
  def addinlist(student:Student):Unit ={
    if(_smartcontract.profit == student.smartcontract.profit &&  _smartcontract.period == student.smartcontract.period && _smartcontract.percentage_part == student.smartcontract.percentage_part)
        _listStudent += student;
        _numofstudent = _listStudent.length;
  } 
  def showlist():String = {
    var data:String = "";
      for(i<-0 until _listStudent.length){
        data += _listStudent(i).Show();
      }
    return data;
  }
  val list_of_block = ListBuffer(genesis_block);
  def add_block_to_list(account_from:String, account_to:String, amount:Double, description:String):Unit = {
      val prev_block = list_of_block(list_of_block.length-1);
      val prew_hesh = prev_block.toString().hashCode();
      val new_block = collection.mutable.Map("from" -> account_from, "to" -> account_to, "amount" -> amount, "description" -> description, "prev_hesh" -> prew_hesh);
      list_of_block += new_block;
   }
  //Процесс начисления оценок и токенов
  def evaluation(birga:Birga, platform:Platform):ListBuffer[Double] = {
    var count_sell = 0;
    var count_buy = 0;
    if(_balance < _courseprice*Smartcontract.period){
      var need_tokens = (_courseprice*Smartcontract.period) - _balance;
      var toup:Tuple2[Double, Double] =buy(need_tokens, birga, _balance, _fiat)
            _balance = toup._1;
            _fiat = toup._2;
            add_block_to_list("birga", super.Name,need_tokens, "Покупка токенов на бирже");
    }
    //Оплата курса учителем платформе
    var newvalueplatformtokens = platform.get_amount_tokens + _courseprice*Smartcontract.period;
    platform.set_amount_tokens(newvalueplatformtokens);
    _balance = _balance-_courseprice*Smartcontract.period;
    add_block_to_list(super.Name, "platform", _courseprice*Smartcontract.period, "Отдача оренды платформе");
    
    //var bal = 0;
    //Цикл по студентам и начисление оценок
    
    for( i<-0 until Smartcontract.period){
      for(i<-0 until _listStudent.length){
        //рандомное начисление оценки студенту
        _listStudent(i).Grade(Random.nextInt(5));
        if(_listStudent(i).Grade == 4){
          
          if(birga.get_price>1.5){
            var tokens_to_sell = (birga.get_price/*-1.5*/)/0.02;
            platform.sell(tokens_to_sell, birga, birga.get_amount_tokens, birga.get_amount_tokens)
            add_block_to_list("platform", "birga", tokens_to_sell, "Продажа токенов на биржу");
          }
          //стипендия
          var price = _courseprice*1.1;
          give(platform, price);
          add_block_to_list("platform", _listStudent(i).Name, price, "Начисление стипендии");
          var new_st_bal_1 = _listStudent(i).Balance + price;
          _listStudent(i).set_balance(new_st_bal_1, birga, platform, _courseprice);
          if(_listStudent(i).Balance<_courseprice){
            var tokens_to_buy = _courseprice-_listStudent(i).Balance;
            var toup:Tuple2[Double, Double] =_listStudent(i).buy(tokens_to_buy, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
            add_block_to_list("birga", _listStudent(i).Name, tokens_to_buy, "Покупка токенов на бирже");
            
          }
          var new_st_bal_2 = _listStudent(i).Balance - _courseprice;
          add_block_to_list(_listStudent(i).Name, super.Name , _courseprice, "Начисление оплаты за курс");
          _listStudent(i).set_balance(new_st_bal_2,  birga, platform, _courseprice);
          if(_listStudent(i).Balance>_courseprice){
            var sell = (_listStudent(i).Balance - _courseprice)/4
            var toup:Tuple2[Double, Double] =_listStudent(i).sell(sell, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
            add_block_to_list(_listStudent(i).Name, "birga" , sell, "Продажа токенов на биржу");
          }
          _balance = _balance + _courseprice/2;
          get(platform, _courseprice);
           add_block_to_list(super.Name, "platform" , _courseprice/2, "Отдача части токенов платформе");
          if(_balance > _courseprice){
                var toup:Tuple2[Double, Double] =sell((_balance-_courseprice), birga, _balance, _fiat)
                _balance = toup._1;
                _fiat = toup._2;
                add_block_to_list(super.Name, "birga" , (_balance-_courseprice), "Продажа токенов на биржу");
            }
          if(birga.get_price < 0.5){
             var toup:Tuple2[Double, Double] = platform.buy(_courseprice, birga, platform.get_amount_tokens, platform.get_fiat);
             platform.set_amount_tokens(toup._1);
             platform.set_fiat(toup._2);
             add_block_to_list("birga", "platform" , ((birga.get_price-0.5).abs)/0.02, "Покупка токенов на бирже");
            
          }
          
          
        }
        if(_listStudent(i).Grade == 3){
          if(birga.get_price>1.5){
            var tokens_to_sell = (birga.get_price/*-1.5*/)/0.02;
             platform.sell(tokens_to_sell, birga, birga.get_amount_tokens, birga.get_amount_tokens)
             add_block_to_list("platform", "birga", tokens_to_sell, "Продажа токенов на биржу");
          }
          
          var price = _courseprice*1;
          give(platform, price);
          add_block_to_list("platform", _listStudent(i).Name, price, "Начисление стипендии");
          var new_st_bal_1 = _listStudent(i).Balance + price;
          _listStudent(i).set_balance(new_st_bal_1, birga, platform, _courseprice);
          if(_listStudent(i).Balance<_courseprice){
            var tokens_to_buy = _courseprice-_listStudent(i).Balance;
            var toup:Tuple2[Double, Double] = _listStudent(i).buy(tokens_to_buy, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
            add_block_to_list("birga", _listStudent(i).Name, tokens_to_buy, "Покупка токенов на бирже");
          }
          var new_st_bal_2 = _listStudent(i).Balance - _courseprice;
          add_block_to_list(_listStudent(i).Name, "birga" , _courseprice, "Начисление оплаты за курс");
          _listStudent(i).set_balance(new_st_bal_2, birga, platform, _courseprice);
          if(_listStudent(i).Balance>_courseprice){
            var sell = (_listStudent(i).Balance - _courseprice)/4
            var toup:Tuple2[Double, Double] =_listStudent(i).sell(sell, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
            add_block_to_list(_listStudent(i).Name, "birga" , sell, "Продажа токенов на биржу");
          }
          _balance = _balance + _courseprice/2;
          get(platform, _courseprice);
           add_block_to_list(super.Name, "platform" , _courseprice/2, "Отдача части токенов платформе");
          if(_balance > _courseprice){
                var toup:Tuple2[Double, Double] =sell((_balance-_courseprice), birga, _balance, _fiat)
                _balance = toup._1;
                _fiat = toup._2;
                add_block_to_list(super.Name, "birga" , (_balance-_courseprice), "Продажа токенов на биржу");
            }
          if(birga.get_price < 0.5){
            var toup:Tuple2[Double, Double] = platform.buy(_courseprice, birga, platform.get_amount_tokens, platform.get_fiat);
            platform.set_amount_tokens(toup._1);
             platform.set_fiat(toup._2);
            add_block_to_list("birga", "platform" ,((birga.get_price-0.5).abs)/0.02, "Покупка токенов на бирже");
          }
          
        }
        if(_listStudent(i).Grade == 2){
          if(birga.get_price>1.5){
             //_listStudent -= _listStudent(i);
            var tokens_to_sell = (birga.get_price/*-1.5*/)/0.02;
              platform.sell(tokens_to_sell, birga, birga.get_amount_tokens, birga.get_amount_tokens);
              add_block_to_list("platform", "birga", tokens_to_sell, "Продажа токенов на биржу");
          }
          
          var price = _courseprice*0.8;
          give(platform, price);
          add_block_to_list("platform", _listStudent(i).Name, price, "Начисление стипендии");
          var new_st_bal_1 = _listStudent(i).Balance + price /*- (_courseprice - price)*/;
          _listStudent(i).set_balance(new_st_bal_1, birga, platform, _courseprice);
          if(_listStudent(i).Balance<_courseprice){
            var tokens_to_buy = _courseprice-_listStudent(i).Balance;
            var toup:Tuple2[Double, Double] = _listStudent(i).buy(tokens_to_buy, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
            add_block_to_list("birga", _listStudent(i).Name, tokens_to_buy, "Покупка токенов на бирже");
          }
          var new_st_bal_2 = _listStudent(i).Balance - _courseprice;
          add_block_to_list(_listStudent(i).Name, "birga" , _courseprice, "Начисление оплаты за курс");
          _listStudent(i).set_balance(new_st_bal_2, birga, platform, _courseprice);
          if(_listStudent(i).Balance>_courseprice){
             var sell = (_listStudent(i).Balance - _courseprice)/4
            var toup:Tuple2[Double, Double] =_listStudent(i).sell(sell, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
             add_block_to_list(_listStudent(i).Name, "birga" , sell, "Продажа токенов на биржу");
          }
          _balance = _balance + _courseprice/2;
          get(platform, _courseprice);
          add_block_to_list(super.Name, "platform" , _courseprice/2, "Отдача части токенов платформе");
          if(_balance > _courseprice){
                var toup:Tuple2[Double, Double] =sell((_balance-_courseprice), birga, _balance, _fiat)
                _balance = toup._1;
                _fiat = toup._2;
                 add_block_to_list(super.Name, "birga" , (_balance-_courseprice), "Продажа токенов на биржу");
            }
          if(birga.get_price < 0.5){
            var toup:Tuple2[Double, Double] = platform.buy(_courseprice, birga, platform.get_amount_tokens, platform.get_fiat);
            platform.set_amount_tokens(toup._1);
             platform.set_fiat(toup._2);
            add_block_to_list("birga", "platform" , ((birga.get_price-0.5).abs)/0.02, "Покупка токенов на бирже");
          }
         
        }
        if(_listStudent(i).Grade == 1){
           if(birga.get_price>1.5){
             //_listStudent -= _listStudent(i);
             var tokens_to_sell = (birga.get_price/*-1.5*/)/0.02;
             platform.sell(tokens_to_sell, birga, birga.get_amount_tokens, birga.get_amount_tokens);
             add_block_to_list("platform", "birga", tokens_to_sell, "Продажа токенов на биржу");
          }
         var price = _courseprice*0.7;
          give(platform, price);
          add_block_to_list("platform", _listStudent(i).Name, price, "Начисление стипендии");
          var new_st_bal_1 = _listStudent(i).Balance + price /*- (_courseprice - price)*/;
          _listStudent(i).set_balance(new_st_bal_1, birga, platform, _courseprice);
          if(_listStudent(i).Balance<_courseprice){
            var tokens_to_buy = _courseprice-_listStudent(i).Balance;
            var toup:Tuple2[Double, Double] = _listStudent(i).buy(tokens_to_buy, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
            add_block_to_list("birga", _listStudent(i).Name,tokens_to_buy, "Покупка токенов на бирже");
          }
          var new_st_bal_2 = _listStudent(i).Balance - _courseprice;
          add_block_to_list(_listStudent(i).Name, "birga" , _courseprice, "Начисление оплаты за курс");
          _listStudent(i).set_balance(new_st_bal_2, birga, platform, _courseprice);
          if(_listStudent(i).Balance>_courseprice){
            var sell = (_listStudent(i).Balance - _courseprice)/4
            var toup:Tuple2[Double, Double] =_listStudent(i).sell(sell, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
             add_block_to_list(_listStudent(i).Name, "birga" , sell, "Продажа токенов на биржу");
          }
          _balance = _balance + _courseprice/2;
          get(platform, _courseprice);
          add_block_to_list(super.Name, "platform" , _courseprice/2, "Отдача части токенов платформе");
          if(_balance > _courseprice){
                var toup:Tuple2[Double, Double] =sell((_balance-_courseprice), birga, _balance, _fiat)
                _balance = toup._1;
                _fiat = toup._2;
                 add_block_to_list(super.Name, "birga" , (_balance-_courseprice), "Продажа токенов на биржу");
            }
          if(birga.get_price < 0.5){
            var toup:Tuple2[Double, Double] = platform.buy(_courseprice, birga, platform.get_amount_tokens, platform.get_fiat);
            platform.set_amount_tokens(toup._1);
             platform.set_fiat(toup._2);
            add_block_to_list("birga", "platform" , ((birga.get_price-0.5).abs)/0.02, "Покупка токенов на бирже");
          }
          
         
        }
        if(_listStudent(i).Grade == 0){
          if(birga.get_price>1.5){
             //_listStudent -= _listStudent(i);
            var tokens_to_sell = (birga.get_price/*-1.5*/)/0.02;
               platform.sell(tokens_to_sell, birga, birga.get_amount_tokens, birga.get_amount_tokens);
               add_block_to_list("platform", "birga", tokens_to_sell, "Продажа токенов на биржу");
          }
          var price = _courseprice*0.1;
          give(platform, price);
          add_block_to_list("platform", _listStudent(i).Name, price, "Начисление стипендии");
          var new_st_bal_1 = _listStudent(i).Balance + price /*- (_courseprice - price)*/;
          _listStudent(i).set_balance(new_st_bal_1, birga, platform, _courseprice);
          if(_listStudent(i).Balance<_courseprice){
            var tokens_to_buy = _courseprice-_listStudent(i).Balance;
            var toup:Tuple2[Double, Double] = _listStudent(i).buy(tokens_to_buy, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
            add_block_to_list("birga", _listStudent(i).Name, tokens_to_buy, "Покупка токенов на бирже");
          }
              
          var new_st_bal_2 = _listStudent(i).Balance - _courseprice;
          add_block_to_list(_listStudent(i).Name, "birga" , _courseprice, "Начисление оплаты за курс");
          _listStudent(i).set_balance(new_st_bal_2,  birga, platform, _courseprice);
          if(_listStudent(i).Balance>_courseprice){
            var sell = (_listStudent(i).Balance - _courseprice)/4
            var toup:Tuple2[Double, Double] =_listStudent(i).sell(sell, birga, _listStudent(i).Balance, _listStudent(i).Fiat)
            _listStudent(i).set_balance(toup._1, birga, platform, _courseprice);
            _listStudent(i).set_fiat(toup._2);
             add_block_to_list(_listStudent(i).Name, "birga" , sell, "Продажа токенов на биржу");
          }
          _balance = _balance + _courseprice/2;
          get(platform, _courseprice);
          add_block_to_list(super.Name, "platform" , _courseprice/2, "Отдача части токенов платформе");
          if(_balance > _courseprice){
                var toup:Tuple2[Double, Double] =sell((_balance-_courseprice), birga, _balance, _fiat)
                _balance = toup._1;
                _fiat = toup._2;
                 add_block_to_list(super.Name, "birga" , (_balance-_courseprice), "Продажа токенов на биржу");
            }
          if(birga.get_price < 0.5){
            var toup:Tuple2[Double, Double] =platform.buy(_courseprice, birga, platform.get_amount_tokens, platform.get_fiat);
            platform.set_amount_tokens(toup._1);
             platform.set_fiat(toup._2);
            add_block_to_list("birga", "platform" ,((birga.get_price-0.5).abs)/0.02, "Покупка токенов на бирже");
          }
          
        }
        println(_listStudent(i).Grade);
        list += birga.get_price;
        }
    }
    println(list_of_block);
    return list;
 
  }
  

  def Login:String = _login;
  def Login(newValue:String) = {
    _login=newValue;
  }
  def Password:String = _password;
  def Password(newValue:String) = {
    _password=newValue;
  }
  
  def Balance:Double = _balance;
  def set_balance(newValue:Double) = {
    _balance=newValue;
  }
  def Fiat:Double = _fiat;
  def set_fiat(newValue:Double) = {
    _fiat=newValue;
  }
  def Smartcontract:Smartcontract = _smartcontract;
  def Smartcontract(newValue:Smartcontract) = {
    _smartcontract=newValue;
  }
  def Courseprice:Int = _courseprice;
  def Cpurseprice(newValue:Int) = {
    _courseprice=newValue;
  }
  def ListStudent:ListBuffer[Student] = _listStudent;
}