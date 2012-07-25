
var doIframeAutoHeight = function (){
    o = document.getElementsByTagName('iframe');

    for(var i=0;i<o.length;i++){  
        if (/\bautoHeight\b/.test(o[i].className)){
            setHeight(o[i]);
            addEvent(o[i],'load', doIframeAutoHeight);
        }
    }
};

var setHeight = function(e){
    if(e.contentDocument){
//        e.height = e.contentDocument.body.offsetHeight + 35; //높이 조절
        e.height = e.contentDocument.body.scrollHeight; //높이 조절
    } else {
        e.height = e.contentWindow.document.body.scrollHeight + 35;
    }
};

var addEvent = function(obj, evType, fn){
    if(obj.addEventListener)
    {
    obj.addEventListener(evType, fn,false);
    return true;
    } else if (obj.attachEvent){
    var r = obj.attachEvent("on"+evType, fn);
    return r;
    } else {
    return false;
    }
};