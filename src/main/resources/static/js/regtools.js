
var cmdInput = document.querySelector("#cmdinput");
var argName = document.querySelector("#argname");
var argRange = document.querySelector("#argrange");
var textArea = document.querySelector("#batch_cmd_txtara");

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

$(function () {
    $("#buttonform").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#generate" ).click(function() { getArgRange(); });

});