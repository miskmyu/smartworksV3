function SE_RegisterCustomPlugins(oEditor, elAppContainer){
	oEditor.registerPlugin(new nhn.husky.SE_ToolbarToggler(elAppContainer));
	oEditor.registerPlugin(new nhn.husky.SE_ImageUpload(elAppContainer));
	oEditor.registerPlugin(new nhn.husky.SE_VideoUpload(elAppContainer));
}

function uploadImageFile(uploadForm, appId, maxWidth)
{

	var theFrm = uploadForm;

	fileName = theFrm.fileSelectImage.value;

	if (fileName == "") {
		alert('본문에 삽입할 이미지를 선택해주세요.');
		return;
	}
	pathpoint = fileName.lastIndexOf('.');
	filepoint = fileName.substring(pathpoint+1,fileName.length);
	filetype = filepoint.toLowerCase();
	if (filetype != 'jpg' && filetype != 'gif' && filetype != 'png' && filetype != 'jpeg' && filetype !='bmp') {
		alert('이미지 파일만 선택할 수 있습니다.');
		return;
	}

    var xhr = new XMLHttpRequest();
    
    xhr.upload.onprogress = function(e){
    	if (e.lengthComputable){
//    		_loaded[id] = e.loaded;
//    		self._options.onProgress(id, name, e.loaded, e.total);
    	}
    };

    xhr.onreadystatechange = function(){            
    	if (xhr.readyState == 4){
    		var responseJSON;
    		try{
	    		responseJSON = eval("(" + xhr.responseText + ")");
	    		var fileFullPath = responseJSON.fullPathName;
	    	    var sHTML = "<img src='" + fileFullPath + "' border='0' style='max-width:" +maxWidth + "px'>";
	    	    parent.parent.oEditors.getById[appId].exec("PASTE_HTML", [sHTML]);
    		}catch (err){
	    		responseJSON = {};
	    	}
    	    parent.parent.oEditors.getById[appId].exec("SE_TOGGLE_IMAGEUPLOAD_LAYER");
    	}
    };
    
    // build query string
    var baseUri = window.location.href.substring(0,window.location.href.lastIndexOf('/'));
    baseUri = baseUri.substring(0, baseUri.lastIndexOf('/'));
    var qqFile = fileName.substring(fileName.lastIndexOf('\\')+1,fileName.length);
    var queryString =  baseUri + '/upload_temp_file.sw?qqFile=' + qqFile;
    xhr.open("POST", queryString, true);
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.setRequestHeader("X-File-Name", encodeURIComponent(qqFile));
    xhr.setRequestHeader("Content-Type", "application/octet-stream");
    xhr.send(theFrm.fileSelectImage.files[0]);
    
    return;
}

function uploadVideoFile(uploadForm, appId, maxWidth)
{

	var theFrm = uploadForm;

	fileName = theFrm.fileSelectVideo.value;

	if (fileName == "") {
		alert('본문에 삽입할 이미지를 선택해주세요.');
		return;
	}
	pathpoint = fileName.lastIndexOf('.');
	filepoint = fileName.substring(pathpoint+1,fileName.length);
	filetype = filepoint.toLowerCase();
//	if (filetype != 'jpg' && filetype != 'gif' && filetype != 'png' && filetype != 'jpeg' && filetype !='bmp') {
//		alert('이미지 파일만 선택할 수 있습니다.');
//		return;
//	}

    var xhr = new XMLHttpRequest();
    
    xhr.upload.onprogress = function(e){
    	if (e.lengthComputable){
//    		_loaded[id] = e.loaded;
//    		self._options.onProgress(id, name, e.loaded, e.total);
    	}
    };

    xhr.onreadystatechange = function(){            
    	if (xhr.readyState == 4){
    		var responseJSON;
    		try{
	    		responseJSON = eval("(" + xhr.responseText + ")");
        		var sHTML =  '<object style="max-width:' + maxWidth + 'px">' + 
	        					 '<param name="movie" value="https://www.youtube.com/v/' + responseJSON.videoYTId + '?version=3&autohide=1&showinfo=0"></param>' +
					 			 '<param name="allowScriptAccess" value="always"></param>' +
					 			 '<param name="allowFullScreen" value="true"></param>' +
					 			 '<embed src="https://www.youtube.com/v/' + responseJSON.videoYTId + '?version=3&autohide=1&showinfo=0"' + 
					 			 	'type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" style="max-width:' + maxWidth + 'px">' +
					 			 '</embed>' + 
				 			 '</object>';
	    	    parent.parent.oEditors.getById[appId].exec("PASTE_HTML", [sHTML]);
    		}catch (err){
	    		responseJSON = {};
	    	}
    	    parent.parent.oEditors.getById[appId].exec("SE_TOGGLE_VIDEOUPLOAD_LAYER");
    	}
    };
    
    // build query string
    var baseUri = window.location.href.substring(0,window.location.href.lastIndexOf('/'));
    baseUri = baseUri.substring(0, baseUri.lastIndexOf('/'));
    var qqFile = fileName.substring(fileName.lastIndexOf('\\')+1,fileName.length);
    var queryString =  baseUri + '/upload_yt_video.sw?qqFile=' + qqFile;
    xhr.open("POST", queryString, true);
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.setRequestHeader("X-File-Name", encodeURIComponent(qqFile));
    xhr.setRequestHeader("Content-Type", "application/octet-stream");
    xhr.send(theFrm.fileSelectVideo.files[0]);
    
    return;
}

// Sample plugin. Use CTRL+T to toggle the toolbar
nhn.husky.SE_ToolbarToggler = $Class({
	name : "SE_ToolbarToggler",

	$init : function(oAppContainer){
		this._assignHTMLObjects(oAppContainer);
	},

	_assignHTMLObjects : function(oAppContainer){
		oAppContainer = $(oAppContainer) || document;
		this.toolbarArea = cssquery.getSingle(".tool", oAppContainer);
	},
	
	$ON_MSG_APP_READY : function(){
		this.oApp.exec("SE_TOGGLE_TOOLBAR", []);
		this.oApp.exec("REGISTER_HOTKEY", ["ctrl+s", "SE_TOGGLE_TOOLBAR", []]);
//        this.oApp.exec("REGISTER_UI_EVENT", ["toolbarToggler", "click", "SE_TOGGLE_TOOLBAR"]);
	},
	
	$ON_SE_TOGGLE_TOOLBAR : function(){
		if(this.toolbarArea.style.display == "none"){
			this.toolbarArea.style.display = "block";
			document.getElementById('c_tool').innerHTML = '▲';
		}else{
			this.toolbarArea.style.display = "none";			
			document.getElementById('c_tool').innerHTML = '▼';
		}
		this.oApp.exec("MSG_EDITING_AREA_SIZE_CHANGED", []);
	}
});

nhn.husky.SE_ImageUpload = $Class({
    name : "SE_ImageUpload",

    $init : function(oAppContainer){
    	this._assignHTMLObjects(oAppContainer);
    },

    _assignHTMLObjects : function(oAppContainer){
    	this.oImageUploadLayer = cssquery.getSingle("DIV.husky_seditor_imgupload_layer", oAppContainer);
    	this.oIFrame = cssquery.getSingle("IFRAME#husky_iframe", oAppContainer);
		this.oBtnConfirm=cssquery.getSingle("BUTTON.confirm",this.oImageUploadLayer);
		this.oBtnCancel=cssquery.getSingle("BUTTON.cancel",this.oImageUploadLayer);
    },

    $ON_MSG_APP_READY : function(){
        this.oApp.exec("REGISTER_UI_EVENT", ["imgupload", "click", "SE_TOGGLE_IMAGEUPLOAD_LAYER"]);
    	this.oApp.registerBrowserEvent(this.oBtnConfirm,"mousedown","SE_SUBMIT_IMAGEUPLOAD");
    	this.oApp.registerBrowserEvent(this.oBtnCancel,"mousedown","HIDE_ACTIVE_LAYER");
    },

    $ON_SE_TOGGLE_IMAGEUPLOAD_LAYER : function(){
        this.oApp.exec("TOGGLE_TOOLBAR_ACTIVE_LAYER", [this.oImageUploadLayer]);
     },
     
    $ON_SE_SUBMIT_IMAGEUPLOAD : function(oAppContainer){
    	uploadImageFile(document.getElementById('frmUploadSEImage'), this.oApp.sAppId, this.oIFrame.offsetWidth-20);
     }    
});

nhn.husky.SE_VideoUpload = $Class({
    name : "SE_VideoUpload",

    $init : function(oAppContainer){
    	this._assignHTMLObjects(oAppContainer);
    },

    _assignHTMLObjects : function(oAppContainer){
    	this.oVideoUploadLayer = cssquery.getSingle("DIV.husky_seditor_videoupload_layer", oAppContainer);
    	this.oIFrame = cssquery.getSingle("IFRAME#husky_iframe", oAppContainer);
		this.oBtnConfirm=cssquery.getSingle("BUTTON.confirm",this.oVideoUploadLayer);
		this.oBtnCancel=cssquery.getSingle("BUTTON.cancel",this.oVideoUploadLayer);
    },

    $ON_MSG_APP_READY : function(){
        this.oApp.exec("REGISTER_UI_EVENT", ["videoupload", "click", "SE_TOGGLE_VIDEOUPLOAD_LAYER"]);
    	this.oApp.registerBrowserEvent(this.oBtnConfirm,"mousedown","SE_SUBMIT_VIDEOUPLOAD");
    	this.oApp.registerBrowserEvent(this.oBtnCancel,"mousedown","HIDE_ACTIVE_LAYER");
    },

    $ON_SE_TOGGLE_VIDEOUPLOAD_LAYER : function(){
        this.oApp.exec("TOGGLE_TOOLBAR_ACTIVE_LAYER", [this.oVideoUploadLayer]);
     },
     
    $ON_SE_SUBMIT_VIDEOUPLOAD : function(oAppContainer){
    	uploadVideoFile(document.getElementById('frmUploadSEVideo'), this.oApp.sAppId, this.oIFrame.offsetWidth-20);
     }    
});

