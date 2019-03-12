

var stompClient = null;

var taskform = document.querySelector('#taskform');
var taskEnvnameInput = document.querySelector('#env_name');
var taskCudadevInput = document.querySelector('#cuda_devs');
var taskRuncmdInput = document.querySelector('#run_cmd');
var taskWorkdirInput = document.querySelector('#workdir');

var listform = document.querySelector('#listform');

// below is for modal dialog
var cmdInput = document.querySelector("#cmdinput");
var argName = document.querySelector("#argname");
var argRange = document.querySelector("#argrange");
var textArea = document.querySelector("#batch_cmd_txtara");

var currentHost = null;

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function replacethenAdd(seq) {
	if (!isEmpty(seq)) {
		var str = cmdInput.value;
		var re_txt = new RegExp("\\s(-+" + argName.value + ")\\s[a-zA-Z]*\\b");
		
		var re_num = new RegExp("\\s(-+" + argName.value + ")\\s[0-9]*\\b");
		
		var replaced = "";
		
		if (re_txt.test(str))
			replaced = str.replace(re_txt, " $1 " + seq);
		if (re_num.test(str))
			replaced = str.replace(re_num, " $1 " + seq);
		textArea.value = textArea.value + replaced;
	}
}

function getArgRange() {
	var str = argRange.value;
	var array_s = str.split(",");
	var first = "" == textArea.value;
	console.log(first);
	for (i =0; i< array_s.length; i++) {
		if (!first) {
			textArea.value = textArea.value + "\n";
		} else {
			first = false;
		}
		replacethenAdd(array_s[i]);
	}
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#conversation").find("tbody").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (response) {
            showGreeting(response);
        });
        stompClient.subscribe('/topic/taskcommence', function(response){
        	console.log(response);
        });
        stompClient.subscribe('/topic/alltaskfinished', function(response){
        	onEndTask(response);
        	dequeueTask(response);
        })
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
	var taskdata = {
		envname: taskEnvnameInput.value,
		cudadevs: taskCudadevInput.value,
		runcmd: taskRuncmdInput.value,
		workdir: taskWorkdirInput.value,
		host: $('#inputhost').find("option:selected").text()
	};
    stompClient.send("/app/hello", {}, JSON.stringify(taskdata));
}

function showGreeting(response) {
	var json = JSON.parse(response.body);
	var host = json.host;
	var chk = "<input type='checkbox' checked=true />";
	var content = "<code id='js' style='display:none'>" + response.body + "</code>";
	var name = json.id;
	content = "<tr name='" + json.id + "'><td>" + chk + json.cmd + content + "</td></tr>";
    $("#" + host).find("tbody").append(content);
}

function runTask() {
	var selected = new Array();
	$('#conversation .table:visible tbody input[type="checkbox"]:checked').each(function() {
		var obj = JSON.parse($(this).next().text());
		selected.push(obj); 
	});
	console.log(selected);
	stompClient.send("/app/taskcommence", {}, JSON.stringify(selected));
}

function onEndTask(response) {
	var json = JSON.parse(response.body);
	var link = "/viewoutput/" + json.id;
	var content = "<tr><td>" + json.host + "</td>";
	content += "<td>" + json.cmd + "</td>";
	content += "<td>" + json.etime + "</td>";
	content += "<td>" + "<a target='_blank' href='" + link + "'>View stdout</a>" + "</td></tr>";
	$('#view-finish').append(content);
}

function dequeueTask(response) {
	var json = JSON.parse(response.body);
	var host = json.host;
	$("#" + host + " tbody").find("tr[name='" + json.id + "']").remove();
}

function onRunformSubmitEvent() {
	listform.addEventListener('submit', function(event){
		runTask();
		event.preventDefault();	
	}, true);	
}

function repeatCmd() {
	var modal = $("#picModal");
	$(modal).modal('show');
}

function importAllCmd() {
	var list = textArea.value.split("\n");
	list.forEach(function(value) {
		if (value !== "") {
			var taskdata = {
					envname: taskEnvnameInput.value,
					cudadevs: taskCudadevInput.value,
					runcmd: value,
					workdir: taskWorkdirInput.value,
					host: $('#inputhost').find("option:selected").text()
				};
			console.log(taskdata);
			stompClient.send("/app/hello", {}, JSON.stringify(taskdata));
		}
	});
}

function selectRemotehost() {
	var inputHost = $("#inputhost option:selected");
	if ($(inputHost).val() != -1){
		var host = $(inputHost).text();
		console.log(host);
		if (!$(currentHost).is(':empty')) {
			$(currentHost).hide();
		}
		$('#' + host).show();
		currentHost = $('#' + host);
		setHostSelected(true);
	} else {
		setHostSelected(false);
	}
}

function setHostSelected(selected) {
	$('#send').prop("disabled", !selected);
	$('#repeat').prop("disabled", !selected);
}

$(function () {
	$( "#conversation" ).hide();
	$( '#picModal' ).modal({ show: false });
	
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#buttonform").on('submit', function (e) {
        e.preventDefault();
    });    
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#repeat" ).click(function() { repeatCmd() });
    $( "#gobutton" ).click(function() { importAllCmd() } );
    $( "#generate" ).click(function() { getArgRange(); });
    
    $('#conversation .table').hide();
    $('#inputhost').change(function() { selectRemotehost() });
    setHostSelected(false);
    onRunformSubmitEvent();
});

