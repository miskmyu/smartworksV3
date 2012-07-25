
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
        e.height = e.contentDocument.body.scrollHeight + 35; //높이 조절
        console.log('1contentDocument scrollHeight = ', e.contentDocument.body.scrollHeight, ', offsetHeight = ', e.contentDocument.body.offsetHeight);
        console.log('1document scrollHeight = ', e.contentWindow.document.body.scrollHeight, ', offsetHeight = ', e.contentWindow.document.body.offsetHeight);
    } else {
        e.height = e.contentWindow.document.body.scrollHeight + 35;
        console.log('2contentDocument scrollHeight = ', e.contentDocument.body.scrollHeight, ', offsetHeight = ', e.contentDocument.body.offsetHeight);
        console.log('2document scrollHeight = ', e.contentWindow.document.body.scrollHeight, ', offsetHeight = ', e.contentWindow.document.body.offsetHeight);
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