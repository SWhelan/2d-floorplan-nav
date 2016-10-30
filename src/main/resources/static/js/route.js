var pointA = null;
var pointB = null;
var excludePoints = [];
var currWindowWidth = null;
var currWindowHeight = null;
var prevWindowWidth = null;
var prevWindowHeight = null;
var leftMouseDown = false;

document.addEventListener("DOMContentLoaded", function() {
	addClickHandlersForPicking();	
	window.onresize = handleResize;
	updateWindowSize();
});

function addClickHandlersForPicking() {
	setClickHandlersOnFloors(function(event) {
		addPoint(event);
	});
}

function removeClickHandlersForPicking() {
	setClickHandlersOnFloors(null);
}

function addClickHandlersForExcluding() {	
	setClickHandlersOnFloors(function(event) {
		paint(event);
	});
	var floors = getFloors();
	for (var i = 0; i < floors.length; i++) {
		floors[i].onmousemove = function(event) {
			if(leftMouseDown) {
				paint(event);
			}
		}
	}
}

function removeClickHandlersForExcluding() {
	setClickHandlersOnFloors(null);
}

function setClickHandlersOnFloors(onClickFunction) {
	var floors = getFloors();
	for (var i = 0; i < floors.length; i++) {
		floors[i].onclick = onClickFunction;
	}
}

function paint(event) {
	console.log("adding point");
	var point = getClickInformation(event);
	excludePoints.push(point);
}

function addPoint(event) {
	if(pointB != null) {
		return;
	}
	
	var point = getClickInformation(event);
	if (pointA == null) {
		pointA = point;
		displayClick(event);
	} else if(pointB == null) {
		pointB = point;
		displayClick(event);
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

var test = '{"A":{"index":0,"x":1033,"y":331},"fileA":"Glennan2.jpg","B":{"index":0,"x":554,"y":1276},"fileB":"Glennan2.jpg","originalFileNames":["Glennan2.jpg"],"steps":["30,10","30,11","29,12","28,13","27,14","26,15","26,16","25,17","25,18","24,19","24,20","23,21","23,22","22,23","22,24","21,25","21,26","21,27","20,28","20,29","19,30","19,31","18,32","18,33","17,34","17,35","16,36"],"prettySteps":["Start","Go to 30x and 11y.","Go to 28x and 13y.","Go to 26x and 15y.","Go to 25x and 17y.","Go to 24x and 19y.","Go to 23x and 21y.","Go to 22x and 23y.","Go to 21x and 25y.","Go to 21x and 27y.","Go to 20x and 29y.","Go to 19x and 31y.","Go to 18x and 33y.","Go to 17x and 35y.","End"]}';
function sendData(pointA, pointB) {
	// https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/send
	
	var xhr = new XMLHttpRequest();
	xhr.open('POST', '/uploadPoints', true);
	xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

	xhr.onload = function (data) {
		hideLoadingScreen();
		var response = JSON.parse(data.currentTarget.response);
		var steps = response.prettySteps;
		displaySteps(steps);
		swapImages();
	};
	
	var data = JSON.stringify({pointA: pointA, pointB: pointB});
	xhr.send(data);
}

function displayLoadingScreen() {
	
}

function hideLoadingScreen() {
	
}

function handleResize() {
	updateWindowSize();
	var points = document.getElementsByClassName("point");
	for(var i = 0; i < points.length; i++) {
		movePoint(points.item(i));
	}
}

function movePoint(point) {
	if(prevWindowHeight == null) {
		return;
	}
	var newTop = parseInt(point.style.top.substring(0, point.style.top.length - 2)) + ((currWindowHeight - prevWindowHeight)/2) + "px";
	var newLeft = parseInt(point.style.left.substring(0, point.style.left.length - 2)) + ((currWindowWidth - prevWindowWidth)/2) + "px";
	console.log(newTop);
	console.log(newLeft);
	point.style.top = newTop;
	point.style.left = newLeft;
}

function updateWindowSize() {
	prevWindowWidth = currWindowWidth;
	prevWindowHeight = currWindowHeight;
	currWindowWidth = window.innerWidth;
	currWindowHeight = window.innerHeight;
}

function displaySteps(steps) {
	console.log(steps);
	var holder = element("steps-holder");
	for(var i = 0; i < steps.length; i++) {
		holder.innerHTML += "<div class='step'>" + steps[i] + "</div>";
	}
}

function swapImages() {
	var images = document.getElementsByClassName("floor");
	for(var i = 0; i < images.length; i++) {
		var current = images.item(i).src;	
		images.item(i).src = current.substring(0, current.length - 4) + "_path.jpg";
		console.log(images.item(i).src);
	}
}

function submitData() {
	displayLoadingScreen();
	sendData(pointA, pointB);
	console.log("submitting");
}

function pickPointsMode() {
	removeClickHandlersForExcluding();
	addClickHandlersForPicking();
	changeCursor("pointer");
	console.log("picking");
}

function excludePointsMode() {
	removeClickHandlersForPicking();
	addClickHandlersForExcluding();
	changeCursor("url('images/paintingcursor.png'), default");
	console.log("excluding");
}

function changeCursor(value) {
	document.body.style.cursor = value;
	console.log("changed");
}