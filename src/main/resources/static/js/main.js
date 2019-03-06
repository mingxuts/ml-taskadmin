

var stompClient = null;

var taskform = document.querySelector('#taskform');
var taskEnvnameInput = document.querySelector('#env_name');
var taskCudadevInput = document.querySelector('#cuda_devs');
var taskRuncmdInput = document.querySelector('#run_cmd');
var taskWorkdirInput = document.querySelector('#workdir');

var listform = document.querySelector('#listform');

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
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
		workdir: taskWorkdirInput.value
	};
    stompClient.send("/app/hello", {}, JSON.stringify(taskdata));
}

function showGreeting(response) {
	var json = JSON.parse(response.body);
	var chk = "<input type='checkbox' value=''/>";
	var content = "<code id='js' style='display:none'>" + response.body + "</code>";
	content = "<tr><td>" + chk + json.cmd + content + "</td></tr>";
    $("#greetings").append(content);
}

function runTask() {
	var selected = new Array();
	$('#greetings input[type="checkbox"]:checked').each(function() {
		var obj = JSON.parse($(this).next().text());
		selected.push(obj); 
	});
	console.log(selected);
}

function onRunformSubmitEvent() {
	listform.addEventListener('submit', function(event){
		runTask();
		event.preventDefault();	
	}, true);	
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    onRunformSubmitEvent();
});

