/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var shots = 0;
var stag = 0;
var cow = 0;
var hiker = 0;
var score = 0;
var type;
var img = 0;
var timer;


function loadAllImages(){
    img = new ImageArray(11);
    img[1].src = "images/cowsmall.gif";
    img[2].src = "images/cowsmall2.gif";
    img[3].src = "images/cowxsmall.gif";
    img[4].src = "images/hikersmall.gif";
    img[5].src = "images/hikersmall2.gif";
    img[6].src = "images/hikerxsmall.gif";
    img[7].src = "images/stag.gif";
    img[8].src = "images/stagsmall.gif";
    img[9].src = "images/stagxsmall.gif";
    img[10].src = "images/stagxxsmall.gif";

}

function ImageArray(n){
    this.length = n;
    for(var i=1; i<=n; i++) {
        this[i] = new Image();
    }
    return this;
}

function animation(){
    if(shots>0){
        presentTarget();
        var randtimercount = (Math.random()*8)+1;
        timer  = setTimeout(animation, randtimercount*150);
    }
}

function presentTarget(){
    var image = document.getElementById("sprite")
    var div = document.getElementById("division");
    var seqno = Math.floor(Math.random()*9);
    
    switch(seqno){
        case 0:{
            image.src = img[1].src;
            div.width = 54;
            div.height = 47;
            type = "cow"
            break;
        }
        case 1:{
            image.src = img[2].src;
            div.width = 54;
            div.height = 47;
            type = "cow"
            break;
        }
        case 2:{
            image.src = img[3].src;
            div.width = 46;
            div.height = 40;
            type = "cow";
            break;
        }
        case 3:{
            image.src = img[4].src;
            div.width = 30;
            div.height = 57;
            type = "hiker";
            break;
        }
        case 4:{
            image.src = img[5].src;
            div.width = 30;
            div.height = 57;
            type = "hiker";
            break;
        }
        case 5:{
            image.src = img[6].src;
            div.width = 25;
            div.height = 50;
            type = "hiker";
            break;
        }
        case 6:{
            image.src = img[7].src;
            div.width = 89;
            div.height = 63;
            type = "stag";
            break;
        }
        case 7:{
            image.src = img[8].src;
            div.width = 80;
            div.height = 57;
            type = "stag";
            break;
        }
        case 8:{
            image.src = img[9].src;
            div.width = 66;
            div.height = 47;
            type = "stag";
            break;
        }
        case 9:{
            image.src = img[10].src;
            div.width = 31;
            div.height = 22;
            type = "stag";
            break;
        }
    }
    div.style.left = Math.floor(Math.random()*(600-div.width))  + "px"; 
    div.style.top = Math.floor(Math.random()*(450-div.height))  + "px";  
    div.style.display = "block";
}

function doStart(){
    document.getElementById("start").disabled = true;
    if (img==0){
        loadAllImages();
    }
    shots = 20;
    stagnum = 0;
    cownum = 0;
    hikernum = 0;
    score = 0;
    updateScore();
    animation();
    
    
}

function updateScore(){
    var temp = document.getElementById("shots");
    temp.value = shots;
    temp = document.getElementById("stags");
    temp.value = stagnum;
    temp = document.getElementById("cows");
    temp.value = cownum;
    temp = document.getElementById("hikers");
    temp.value = hikernum;
    temp = document.getElementById("score");
    temp.value = score;
}

function missScore(){
    if(shots != 0){
        shots = shots-1;
        updateScore();
        if(shots == 0){
            clearTimeout(timer);
            document.getElementById("start").disabled = false;
            if(score < 1){
                alert("Score is " + score + "!" + "\nPractice makes perfect!");
            }
            else{
                alert("Score is " + score + "!" + "\nCongratulations!");
            }
            
        }
    }
}

function hitScore(){
    if(type=="cow"){
        score = score - 15;
        cow = cow + 1;
    }
    if(type=="hiker"){
        score = score - 100;
        hiker = hiker + 1;
    }
    if(type=="stag"){
        score = score + 5;
        stag = stag + 1;
    }
    updateScore();
}