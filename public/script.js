
$(document).ready(GetInfo);

function GetInfo(){
    console.log("Hola");
    let url = "/dsaApp/products";
    $.get(url, SaveData);

}



