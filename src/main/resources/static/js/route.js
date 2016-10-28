var pointA = null;
var pointB = null;

document.addEventListener("DOMContentLoaded", function() {
	var floors = getFloors();
	for (var i = 0; i < floors.length; i++) {
		floors[i].onclick = function(event) {
			addPoint(event);
		}
	}
});

function addPoint(event) {
	if(pointB != null) {
		return;
	}
	
	var point = getClickInformation(event);
	if (pointA == null) {
		pointA = point;
		displayClick(event);
	} else {
		pointB = point;
		displayClick(event);
		displayLoadingScreen();
		sendData(pointA, pointB);
	}
}

function getFloors() {
	return document.getElementsByClassName("floor");
}

function getClickInformation(event) {
	var imageElem = event.srcElement;
	var fileName = imageElem.currentSrc;
	var displayWidth = imageElem.width;
	var displayHeight = imageElem.height;
	var naturalWidth = imageElem.naturalWidth;
	var naturalHeight = imageElem.naturalHeight;
	var clientOffsetX = event.offsetX;
	var clientOffsetY = event.offsetY;
	
	var actualX = Math.round((clientOffsetX / displayWidth) * naturalWidth);
	var actualY = Math.round((clientOffsetY / displayHeight) * naturalHeight);
	
	var information = {"filename" : fileName, "xcoord" : actualX, "ycoord" : actualY};
	return information;
}


function displayClick(event) {
	var holder = element("point-holder");
	var margin = 15;
	holder.innerHTML += "<div class='point' style='top: "+ (event.pageY - margin) +"px; left: " + Math.round(event.pageX - holder.getBoundingClientRect().left - margin) + "px;'></div>";
}

function sendData(pointA, pointB) {
	// https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/send
	
	var xhr = new XMLHttpRequest();
	xhr.open('POST', '/uploadPoints', true);
	xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

	xhr.onload = function (data) {
		console.log(data);
	};
	
	var data = JSON.stringify({pointA: pointA, pointB: pointB});
	xhr.send(data);
}

function displayLoadingScreen() {
	
}

function hideLoadingScreen() {}