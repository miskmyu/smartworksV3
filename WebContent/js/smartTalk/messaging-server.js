
var serverUrl = "http://localhost:8011";
var swContext = "/faye";
var fayeServer = "fayeServer";
 
var swSubject = {
	SMARTWORKS : "/smartworks",
	BROADCASTING : "/broadcasting",
	FAYESERVER : "/fayeServer",
	ONLINE : "/online",
	OFFLINE : "/offline",
	CHATREQUEST: "/chatRequest",
	ALL : "/*"
};

var msgType = {
	BROADCASTING : "BCAST",
	NOTICE_COUNT : "NCOUNT",
	CHAT_REQUEST : "CHATREQ",
	JOIN_CHAT : "JOINCHAT"
};

//var http = require('http'), faye = require('./faye-node');
var http = require('http'), faye = require('faye');
var port = 8011;

var bayeux = new faye.NodeAdapter({
	mount : '/faye',
	timeout : 5
});
var server = http.createServer(function(request, response) {
	response.writeHead(200, {
		'Content-Type' : 'text/plain'
	});
	response.write('Hello, non-Bayeux request');
	response.end();
});

bayeux.attach(server);
server.listen(port);

function getUserId(channel){
	var subjects = channel.split('/');
	var newChannel = "";
	if(subjects.length>3)
		newChannel = subjects[3].replace(/_/g, '.' );
	return newChannel;
}
function getCompanyId(channel){
	var subjects = channel.split('/');
	var newChannel = "";
	if(subjects.length>2)
		newChannel = subjects[2];
	return newChannel;
}

bayeux.bind('subscribe', function(clientId, channel) {
	console.log('[  SUBSCRIBE] ' + clientId + ' -> ' + channel);
	var pos = channel.indexOf('@');
	if((pos != -1) && (channel.indexOf('/',pos) == -1)){		
		var userId = getUserId(channel);
		var companyId = getCompanyId(channel);
		bayeux.getClient().publish(channel + swSubject.ONLINE, userId);

		var options = {
				host: "localhost",
				port: "8080",
				path: "/smartworksV3/services/common/smartTalkService.jsp?method=updateChatterStatus&userId=" + userId + "&status=online"  + "&companyId=" + companyId
			};
		try{
			http.request(options, function(response){
				response.on('data', function(data) {
				});
				response.on('end', function() {
				});
			}).end();
		}catch (e){
		}
	}
});

bayeux.bind('unsubscribe', function(clientId, channel) {
	console.log('[UNSUBSCRIBE] ' + clientId + ' -> ' + channel);
	var pos = channel.indexOf('@');
	if((pos != -1) && (channel.indexOf('/',pos) == -1)){
		var userId = getUserId(channel);
		var companyId = getCompanyId(channel);
		bayeux.getClient().publish(channel + swSubject.OFFLINE, userId);

		var options = {
				host: "localhost",
				port: "8080",
				path: "/smartworksV3/services/common/smartTalkService.jsp?method=updateChatterStatus&userId=" + userId + "&status=offline" + "&companyId=" + companyId
			};
		try{
			http.request(options, function(response){
				response.on('data', function(data) {
				});
				response.on('end', function() {
				});
			}).end();
		}catch (e){
		}
	}
});

bayeux.bind('disconnect', function(clientId) {
	console.log('[ DISCONNECT] ' + clientId);
});

bayeux.bind('publish', function(clientId, channel, data) {
	console.log('[ PUBLISH] =======================================');
	console.log(channel, data);
	console.log('==================================================');
});


console.log('Listening on ' + port);

function uniqid() {
	var newDate = new Date;
	return newDate.getTime();
};

var chatRequestCallback = function(message) {
	if (message.msgType === msgType.CHAT_REQUEST) {
		var companyId = message.companyId;
		var chatterInfos = message.chatterInfos;
		var chatId = "chatId" + uniqid();
		if (chatterInfos != null && chatterInfos.length > 0) {
			for ( var i = 0; i < chatterInfos.length; i++) {
				if((chatterInfos[i] == null) || (chatterInfos[i].userId == null)) continue;
				bayeux.getClient().publish(swSubject.SMARTWORKS
						+ "/" + companyId + "/" + chatterInfos[i].userId.replace(/\./g,'_'), {
					msgType : msgType.JOIN_CHAT,
					sender : message.sender,
					chatId : chatId,
					chatterInfos : chatterInfos
				});
			}
		}
	}
};

bayeux.getClient().subscribe(swSubject.SMARTWORKS + swSubject.CHATREQUEST + swSubject.FAYESERVER, chatRequestCallback);
