function confirmdelete(ident){
    loc = "Delete.php?id=";
    add = loc + ident;
    if(confirm('Confirm Delete?')){
        window.location = add;  
        return true;
    }
    else return false;
}