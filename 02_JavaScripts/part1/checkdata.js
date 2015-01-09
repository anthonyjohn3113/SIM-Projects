/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function checkdata(){
    
    if(!checkname()){return false;}
    if(!checkadd()){return false;}
    if(!checkstate()){return false;}
    if(!checkcard()){return false;}
    if(!confirmSubmit()){return false;}
    else return true;
}

function checkname(){
    var gname = document.getElementById('givenname').value; 
    var fname = document.getElementById('firstname').value;
    
    var tgname = new RegExp("^[A-Z]([A-Z]|[a-z]){2,20}$");
    if(!tgname.test(gname)){
        alert("Given name not valid. First letter must be capital and between 3-21 chars.");
        return false;
    }
    var tfname = new RegExp("^[A-Z]([A-Z]|[a-z]|[']|[-]){2,20}$");
    if(!tfname.test(fname)){
        alert("Family name not valid. First letter must be capital and between 3-21 chars. Can contain ' or - .");
        return false;
    }
    return true;
}

function checkadd(){
    var addl1 = document.getElementById('addline1').value; 
    var addl2 = document.getElementById('addline2').value + ("-");
    var cit = document.getElementById('city').value;
    
    var tadd = new RegExp("^([A-Z]|[a-z]|[0-9]|[,]|[-]|[ ]|[/]){1,20}$");
    if (!tadd.test(addl1)){
        alert("Address Line 1 not valid. Please try again.");
        return false;
    }
    if (!tadd.test(addl2)){
        alert("Address Line 2 not valid. Please try again.");
        return false;
    }
    if (!tadd.test(cit)){
        alert("City not valid. Please try again.");
        return false;
    }
    return true;
}

function checkstate(){
    var sta = document.getElementById('state').value;
    var pstcd = document.getElementById('postcode').value;
    
    if(sta==1){
        if((pstcd>=2900)&&(pstcd<=2999)){
            return true;
        }
        if((pstcd<2600)||(pstcd>2618)){
            alert("Postcode not valid for ATC. (2600 to 2618 and 29## )"); 
            return false;
        }
    }
    if(sta==2){
        if((pstcd>2999)||(pstcd<2000)){
            alert("Postcode not valid for NSW. (2### )");
            return false;
        }
    }
    if(sta==3){
        if((pstcd>999)||(pstcd<800)){
            alert("Postcode not valid for NT. (08## and 09##)");
            return false;
        }
    }
    if(sta==4){
        if((pstcd>4999)||(pstcd<4000)){
            alert("Postcode not valid for QLD. (4###)");
            return false;
        }
    }
    if(sta==5){
        if((pstcd>5999)||(pstcd<5000)){
            alert("Postcode not valid for SA. (5###)");
            return false;
        }
    }
    if(sta==6){
        if((pstcd>7999)||(pstcd<7000)){
            alert("Postcode not valid for TAS. (7###)");
            return false;
        }
    }
    if(sta==7){
        if((pstcd>3999)||(pstcd<3000)){
            alert("Postcode not valid for VIC. (3###)");
            return false;
        }
    }
    if(sta==8){
        if((pstcd>6999)||(pstcd<6000)){
            alert("Postcode not valid for WA. (6###)");
            return false;
        }
    }
    return true;
}

function checkcard(){
    var cval = document.getElementById('cardtype').value;
    var cnum = document.getElementById('cardnum').value;
    var a = cnum.charAt(0);
    cnum = cnum.replace(/[^\d]/g, '');
    
    if(cval==1){
        if(a!="4"){
            alert("Visa cards must start with 4")
            return false;
        }
        else{
            if(!checkLuhn(cnum)) {
                alert('Sorry, that is not a valid credit card number! E.g. (4111-1111-1111-1111)');
                return false;
            }
        }
    }
    if(cval==2){
        if(a!="5"){
            alert("Mastercard cards must start with 5")
            return false;
        }
        else{
            if(!checkLuhn(cnum)) {
                alert('Sorry, that is not a valid credit card number! \nE.g. (4111-1111-1111-1111) / 12 Digits / Credit card number do not exist!');
                return false;
            }
        }
    }
    return true;
}

function checkLuhn(input)
{
    var sum = 0;
    var numdigits = input.length;
    var parity = numdigits % 2;
    for(var i=0; i < numdigits; i++) {
        var digit = parseInt(input.charAt(i))
        if(i % 2 == parity) digit *= 2;
        if(digit > 9) digit -= 9;
        sum += digit;
    }
    return (sum % 10) == 0;
}


function confirmSubmit(){
    if(confirm('Confirm Submit?')){return true;}
    else return false;
}





