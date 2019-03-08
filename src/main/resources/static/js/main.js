

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
		workdir: taskWorkdirInput.value
	};
    stompClient.send("/app/hello", {}, JSON.stringify(taskdata));
}

function showGreeting(response) {
	var json = JSON.parse(response.body);
	var chk = "<input type='checkbox' value=''/>";
	var content = "<code id='js' style='display:none'>" + response.body + "</code>";
	var name = json.id;
	content = "<tr name='" + json.id + "'><td>" + chk + json.cmd + content + "</td></tr>";
    $("#greetings").append(content);
}

function runTask() {
	var selected = new Array();
	$('#greetings input[type="checkbox"]:checked').each(function() {
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
	$("#greetings tr[name='" + json.id + "']").remove();
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

